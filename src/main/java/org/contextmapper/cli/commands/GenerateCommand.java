/*
 * Copyright 2021 The Context Mapper Project Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.contextmapper.cli.commands;

import org.contextmapper.dsl.cml.CMLResource;
import org.contextmapper.dsl.generator.GenericContentGenerator;
import org.contextmapper.dsl.standalone.ContextMapperStandaloneSetup;
import org.contextmapper.dsl.standalone.StandaloneContextMapperAPI;
import org.eclipse.xtext.generator.IGenerator2;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Command(name = "generate", description = "Generates output from a CML file.", mixinStandardHelpOptions = true)
public class GenerateCommand implements Callable<Integer> {

    @Option(names = {"-i", "--input"}, description = "Path to the CML file for which you want to generate output.", required = true)
    private String inputPath;

    @Option(names = {"-g", "--generator"}, description = "The generator you want to call. Use one of the following values: ${COMPLETION-CANDIDATES}", required = true)
    private ContextMapperGenerator generatorType;

    @Option(names = {"-o", "--outputDir"}, description = "Path to the directory into which you want to generate.", defaultValue = ".")
    private String outputDir;

    @Option(names = {"-t", "--template"}, description = "Path to the Freemarker template you want to use. This parameter is only used if you pass 'generic' to the 'generator' (-g) parameter.")
    private File templateFile;

    @Option(names = {"-f", "--outputFile"}, description = "The name of the file that shall be generated (only used by Freemarker generator, as we cannot know the file extension).")
    private String outputFileName;

    @Override
    public Integer call() throws Exception {
        if (!isInputFileValid(inputPath)) {
            return 1;
        }

        if (!doesOutputDirExist(this.outputDir)) {
            return 1;
        }

        StandaloneContextMapperAPI cmAPI = ContextMapperStandaloneSetup.getStandaloneAPI();
        CMLResource cmlResource = cmAPI.loadCML(inputPath);
        IGenerator2 generator = getGenerator();

        cmAPI.callGenerator(cmlResource, generator, this.outputDir);
        System.out.println("Generated into '" + this.outputDir + "'.");
        return 0;
    }

    private IGenerator2 getGenerator() {
        IGenerator2 selectedGenerator = generatorType.getGenerator();
        if (selectedGenerator instanceof GenericContentGenerator) {
            if (templateFile == null) {
                throw new IllegalArgumentException("The --template (-t) parameter is required for the 'generic' generator.");
            }
            if (outputFileName == null || outputFileName.trim().isEmpty()) {
                throw new IllegalArgumentException("The --outputFile (-f) parameter is required for the 'generic' generator.");
            }
            final GenericContentGenerator genericContentGenerator = (GenericContentGenerator) selectedGenerator;
            genericContentGenerator.setFreemarkerTemplateFile(templateFile);
            genericContentGenerator.setTargetFileName(outputFileName);
            return genericContentGenerator;
        }
        return selectedGenerator;
    }

    protected boolean isInputFileValid(String path) {
        File inputFile = new File(path);
        if (!inputFile.exists()) {
            System.err.println("ERROR: The file '" + path + "' does not exist.");
            return false;
        }
        if (!path.endsWith(".cml")) {
            System.err.println("ERROR: Please provide a path to a CML (*.cml) file.");
            return false;
        }
        return true;
    }

    private boolean doesOutputDirExist(String dirPath) {
        if (dirPath == null || "".equals(dirPath)) {
            // Should not happen with defaultValue, but good for robustness
            System.err.println("ERROR: Output directory path is empty.");
            return false;
        }

        File dir = new File(dirPath);
        if (!dir.exists()) {
            System.err.println("ERROR: Output directory '" + dirPath + "' does not exist.");
            return false;
        }
        if (!dir.isDirectory()) {
            System.err.println("ERROR: '" + dirPath + "' is not a directory.");
            return false;
        }
        return true;
    }
    
    // Used by Picocli for auto-completion help for the generatorType option
    static class ContextMapperGeneratorConverter implements picocli.CommandLine.ITypeConverter<ContextMapperGenerator> {
        @Override
        public ContextMapperGenerator convert(String value) throws Exception {
            return ContextMapperGenerator.byName(value);
        }
    }
    
    // This is to provide dynamic completion candidates for the --generator option in help messages.
    // Picocli will replace ${COMPLETION-CANDIDATES} with the output of this.
    // However, for this to work properly, ContextMapperGenerator.toString() should be just the name.
    // Or we can provide a custom ICompletionCandidate provider.
    // For now, I will adjust ContextMapperGenerator.toString() and how its help is constructed.
    // Let's ensure the ContextMapperGenerator class is also updated for better Picocli integration.
}
