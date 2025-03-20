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

import org.apache.commons.cli.*;
import org.contextmapper.dsl.cml.CMLResource;
import org.contextmapper.dsl.generator.GenericContentGenerator;
import org.contextmapper.dsl.standalone.ContextMapperStandaloneSetup;
import org.contextmapper.dsl.standalone.StandaloneContextMapperAPI;
import org.eclipse.xtext.generator.IGenerator2;
import org.contextmapper.dsl.cml.CMLImportResolver;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

public class GenerateCommand extends AbstractCliCommand {

    private String outputDir = "./";

    @Override
    public void run(String[] args) {
        Options options = createOptions();

        CommandLineParser commandLineParser = new DefaultParser();

        try {
            CommandLine cmd = commandLineParser.parse(options, args);

            if (cmd.hasOption("help") || cmd.hasOption("h")) {
                printHelp(options);
            } else {
                String inputPath = cmd.getOptionValue("input").trim();
                if (!isInputFileValid(inputPath))
                    return;

                if (cmd.hasOption("outputDir"))
                    this.outputDir = cmd.getOptionValue("outputDir");

                if (doesOutputDirExist(this.outputDir)) {
                    StandaloneContextMapperAPI cmAPI = ContextMapperStandaloneSetup.getStandaloneAPI();
                    File inputFile = new File(inputPath).getAbsoluteFile();
                    CMLResource cmlResource = cmAPI.loadCML(inputFile);
                    
                    // Ensure imports are resolved before generation
                    new CMLImportResolver().resolveImportedResources(cmlResource);
                    
                    cmAPI.callGenerator(cmlResource, getGenerator(cmd), this.outputDir);
                    System.out.println("Generated into '" + this.outputDir + "'.");
                }
            }
        } catch (ParseException e) {
            printHelp(options);
        }
    }

    private IGenerator2 getGenerator(CommandLine cmd) {
        final ContextMapperGenerator generator = ContextMapperGenerator.byName(cmd.getOptionValue("generator"));
        if (generator.getGenerator() instanceof GenericContentGenerator) {
            final GenericContentGenerator genericContentGenerator = (GenericContentGenerator) generator.getGenerator();
            genericContentGenerator.setFreemarkerTemplateFile(new File(cmd.getOptionValue("template")));
            genericContentGenerator.setTargetFileName(cmd.getOptionValue("outputFile"));
            return genericContentGenerator;
        }
        return generator.getGenerator();
    }

    private Options createOptions() {
        Options options = new Options();

        Option input = new Option("i", "input", true, "Path to the CML file for which you want to generate output.");
        input.setRequired(true);
        options.addOption(input);

        Option generator = new Option("g", "generator", true,
                "The generator you want to call. Use one of the following values: " +
                        Arrays.stream(ContextMapperGenerator.values()).map(ContextMapperGenerator::toString).collect(Collectors.joining(", ")));
        generator.setRequired(true);
        options.addOption(generator);

        options.addOption(new Option("o", "outputDir", true, "Path to the directory into which you want to generate."));
        options.addOption(new Option("t", "template", true,
                "Path to the Freemarker template you want to use. This parameter is only used if you pass 'generic' to the 'generator' (-g) parameter."));
        options.addOption(new Option("f", "outputFile", true,
                "The name of the file that shall be generated (only used by Freemarker generator, as we cannot know the file extension)."));
        options.addOption(new Option("h", "help", false, "Prints this message."));

        return options;
    }

    protected void printHelp(final Options options) {
        new HelpFormatter().printHelp("cm generate", options);
    }

    private boolean doesOutputDirExist(String outputDir) {
        if (outputDir == null || "".equals(outputDir)) {
            System.out.println("ERROR: '" + outputDir + "' is not a directory.");
            return false;
        }

        File dir = new File(outputDir);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("ERROR: '" + outputDir + "' is not a directory.");
            return false;
        }
        return true;
    }

}
