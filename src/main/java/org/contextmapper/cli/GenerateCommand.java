package org.contextmapper.cli;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.Callable;

import org.contextmapper.dsl.cml.CMLResource;
import org.contextmapper.dsl.generator.GenericContentGenerator;
import org.contextmapper.dsl.standalone.ContextMapperStandaloneSetup;
import org.contextmapper.dsl.standalone.StandaloneContextMapperAPI;
import org.eclipse.xtext.generator.IGenerator2;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "generate",
    description = "Generates output from a CML file.",
    mixinStandardHelpOptions = true)
public class GenerateCommand implements Callable<Integer> {

    @Option(
        names = {"-i", "--input"},
        description = "Path to the CML file for which you want to generate output.",
        required = true)
    private String inputPath;

    @Option(
        names = {"-g", "--generator"},
        description = """
            The generator you want to call.
            Use one of the following values: ${COMPLETION-CANDIDATES}
            """)
    private ContextMapperGenerator generatorType;

    @Option(
        names = {"-o", "--outputDir"},
        description = "Path to the directory into which you want to generate.",
        defaultValue = ".")
    private String outputDir;

    @Option(
        names = {"-t", "--template"},
        description = """
            Path to the Freemarker template you want to use.
            This parameter is only used if you pass 'generic' to the 'generator' (-g) parameter.
            """
        )
    private File templateFile;

    @Option(
        names = {"-f", "--outputFile"},
        description = """
            The name of the file that shall be generated (only used by Freemarker generator,
            as we cannot know the file extension).
            """
        )
    private String outputFileName;

    private boolean isInputFileValid(String path) {
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
        if (Objects.isNull(dirPath) || dirPath.trim().isEmpty()) {
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

    private IGenerator2 getGenerator() {
        IGenerator2 selectedGenerator = generatorType.getGenerator();
        if (selectedGenerator instanceof GenericContentGenerator) {
            if (Objects.isNull(templateFile)) {
                throw new IllegalArgumentException("The --template (-t) parameter is required for the 'generic' generator.");
            }
            if (Objects.isNull(outputFileName) || outputFileName.trim().isEmpty()) {
                throw new IllegalArgumentException("The --outputFile (-f) parameter is required for the 'generic' generator.");
            }
            final GenericContentGenerator genericContentGenerator = (GenericContentGenerator) selectedGenerator;
            genericContentGenerator.setFreemarkerTemplateFile(templateFile);
            genericContentGenerator.setTargetFileName(outputFileName);
            return genericContentGenerator;
        }
        return selectedGenerator;
    }

    private Integer runCall() {
        // Preconditions check
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

    @Override
    public Integer call() throws Exception {
        return runCall();
    }
}
