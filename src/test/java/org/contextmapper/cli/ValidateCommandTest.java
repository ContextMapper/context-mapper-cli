package org.contextmapper.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import picocli.CommandLine;

class ValidateCommandTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private CommandLine cmd;

    @BeforeEach
    void setUp() {
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
        String[] args = { "validate", "-h" };

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        assertThat(outContent.toString())
                .contains("Usage: cm validate [-hV] -i=<inputPath>")
                .contains("Validates a CML file.");
    }

    @Test
    @DisplayName("run() should validate successfully for a valid CML file")
    void run_WhenWithValidCMLFile_ThenValidateWithoutErrors() {
        // Given
        String[] args = { "validate", "-i", "src/test/resources/test.cml" };

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        assertThat(outContent.toString())
                .contains("The CML file 'src/test/resources/test.cml' has been validated without errors.");
    }

    @Test
    @DisplayName("run() should print error for an invalid CML file")
    void run_WhenWithInvalidCMLFile_ThenPrintError() {
        // Given
        String[] args = { "validate", "-i", "src/test/resources/test-with-error.cml" };

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(1);
        assertThat(errContent.toString())
                .contains("ERROR in null on line 2:mismatched input '<EOF>' expecting RULE_CLOSE");
    }

    @Test
    @DisplayName("run() should print error when input file does not exist")
    void run_WhenInputFileDoesNotExist_ThenPrintError() {
        // Given
        String nonExistingFile = "nonexistent.cml";
        String[] args = { "validate", "-i", nonExistingFile };

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(1);
        assertThat(errContent.toString()).contains("ERROR: The file '" + nonExistingFile + "' does not exist.");
    }

    @Test
    @DisplayName("run() should print error when input file is not a CML file")
    void run_WhenInputFileIsNotCML_ThenPrintError() {
        // Given
        String notACmlFile = "src/test/resources/test.txt";
        String[] args = { "validate", "-i", notACmlFile };

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(1);
        assertThat(errContent.toString()).contains("ERROR: Please provide a path to a CML (*.cml) file.");
    }

    @Test
    @DisplayName("run() should print error and help when no input file is provided")
    void run_WhenNoInputFileProvided_ThenPrintErrorAndHelp() {
        // Given
        String[] args = { "validate" };

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isNotEqualTo(0);
        assertThat(errContent.toString())
                .contains("Missing required option: '--input=<inputPath>'")
                .contains("Usage: cm validate [-hV] -i=<inputPath>");
    }
}
