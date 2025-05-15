package org.contextmapper.cli.commands;

import org.contextmapper.cli.ContextMapperCLI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class GenerateCommandTest {

    @TempDir
    Path testOutPath;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    private CommandLine cmd;
    private String testOutDirString;

    @BeforeEach
    void setUp() throws IOException {
        testOutDirString = testOutPath.toFile().getAbsolutePath();
        if (!Files.exists(testOutPath)) {
            Files.createDirectories(testOutPath);
        }

        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        cmd = new CommandLine(new ContextMapperCLI());
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    @DisplayName("run() should print help when called with -h option")
    void run_WhenCalledWithHelp_ThenPrintHelp() {
        // Given
        String[] args = {"generate", "-h"};

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        assertThat(outContent.toString())
            .contains("Usage: cm generate [-hV] [-f=<outputFileName>] -g=<generatorType>")
            .contains("Generates output from a CML file.");
    }

    @Test
    @DisplayName("run() should print error when output directory does not exist")
    void run_WhenCalledWithNonExistingOutDir_ThenPrintError() {
        // Given
        String nonExistingDir = "/just-some-dir-that-hopefully-not-exists-unless-you-are-very-unlucky";
        String[] args = {"generate", "-i", "src/test/resources/test.cml", "-g", "plantuml", "-o", nonExistingDir};

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(1);
        assertThat(errContent.toString()).contains("ERROR: Output directory '" + nonExistingDir + "' does not exist.");
    }

    @Test
    @DisplayName("run() should print error when input CML file does not exist")
    void run_WhenCalledWithNonExistingInputFile_ThenPrintError() {
        // Given
        String nonExistingFile = "just-a-file-that-does-not-exist.cml";
        String[] args = {"generate", "-i", nonExistingFile, "-g", "plantuml", "-o", testOutDirString};

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(1);
        assertThat(errContent.toString()).contains("ERROR: The file '" + nonExistingFile + "' does not exist.");
    }

    @Test
    @DisplayName("run() should generate PlantUML files when plantuml generator is specified")
    void run_WhenCalledWithPlantUMLParam_ThenGeneratePlantUMLFiles() {
        // Given
        String[] args = {"generate", "-i", "src/test/resources/test.cml", "-g", "plantuml", "-o", testOutDirString};

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        assertThat(outContent.toString()).contains("Generated into '" + testOutDirString + "'.");
        assertThat(new File(testOutDirString, "test_BC_CargoBookingContext.puml")).exists();
        assertThat(new File(testOutDirString, "test_BC_LocationContext.puml")).exists();
        assertThat(new File(testOutDirString, "test_BC_VoyagePlanningContext.puml")).exists();
        assertThat(new File(testOutDirString, "test_ContextMap.puml")).exists();
    }

    @Test
    @DisplayName("run() should generate Context Map files when context-map generator is specified")
    void run_WhenCalledWithContextMapParam_ThenGenerateContextMapFiles() {
        // Given
        String[] args = {"generate", "-i", "src/test/resources/test.cml", "-g", "context-map", "-o", testOutDirString};

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        assertThat(outContent.toString()).contains("Generated into '" + testOutDirString + "'.");
        assertThat(new File(testOutDirString, "test_ContextMap.gv")).exists();
        assertThat(new File(testOutDirString, "test_ContextMap.png")).exists();
        assertThat(new File(testOutDirString, "test_ContextMap.svg")).exists();
    }

    @Test
    @DisplayName("run() should generate generic output when generic generator and template are specified")
    void run_WhenCalledWithGenericParam_ThenGenerateGenericOutput() {
        // Given
        String outputFileName = "test.md";
        String[] args = {"generate", "-i", "src/test/resources/test.cml", "-g", "generic", "-o", testOutDirString, "-t", "src/test/resources/test.ftl", "-f", outputFileName};

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        assertThat(outContent.toString()).contains("Generated into '" + testOutDirString + "'.");
        assertThat(new File(testOutDirString, outputFileName)).exists();
    }
    
    @Test
    @DisplayName("run() should print error when generic generator is missing the template parameter")
    void run_WhenGenericGeneratorMissingTemplate_ThenPrintError() {
        // Given
        String[] args = {"generate", "-i", "src/test/resources/test.cml", "-g", "generic", "-o", testOutDirString, "-f", "test.md"};

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isNotEqualTo(0);
        assertThat(errContent.toString()).contains("The --template (-t) parameter is required for the 'generic' generator.");
    }

    @Test
    @DisplayName("run() should print error when generic generator is missing the output file parameter")
    void run_WhenGenericGeneratorMissingOutputFile_ThenPrintError() {
        // Given
        String[] args = {"generate", "-i", "src/test/resources/test.cml", "-g", "generic", "-o", testOutDirString, "-t", "src/test/resources/test.ftl"};

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isNotEqualTo(0);
        assertThat(errContent.toString()).contains("The --outputFile (-f) parameter is required for the 'generic' generator.");
    }

    @Test
    @DisplayName("run() should print error and help when required options are missing")
    void run_WhenRequiredOptionsMissing_ThenPrintErrorAndHelp() {
        // Given
        String[] args = {"generate"};

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isNotEqualTo(0);
        assertThat(errContent.toString())
            .contains("Missing required options: '--input=<inputPath>', '--generator=<generatorType>'")
            .contains("Usage: cm generate [-hV] [-f=<outputFileName>] -g=<generatorType>");
    }
}
