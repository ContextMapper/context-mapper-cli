package org.contextmapper.cli;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

class ContextMapperCLITest {

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
    @DisplayName("main() should print top-level help when called without command")
    void main_WhenCalledWithoutCommand_ThenPrintsTopLevelHelp() {
        // Given
        // No arguments for this test case

        // When
        cmd.execute();

        // Then
        assertThat(outContent.toString()).contains("Context Mapper CLI. Use 'cm --help' for usage information.");
    }

    @Test
    @DisplayName("main() should print top-level help when called with --help option")
    void main_WhenCalledWithHelpOption_ThenPrintsTopLevelHelp() {
        // Given
        String[] args = {"--help"};

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        assertThat(outContent.toString())
            .contains("Usage: cm [-hV] [COMMAND]")
            .contains("Commands:")
            .contains("validate  Validates a CML file.")
            .contains("generate  Generates output from a CML file.");
    }

    @Test
    @DisplayName("main() should print version when called with --version option")
    void main_WhenCalledWithVersionOption_ThenPrintsVersion() {
        // Given
        String[] args = {"--version"};

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        assertThat(outContent.toString()).contains("Context Mapper CLI");
    }

    @Test
    @DisplayName("main() should print error and usage when called with an invalid option")
    void main_WhenCalledWithInvalidOption_ThenPrintsErrorAndUsage() {
        // Given
        String[] args = {"--invalid-option"};

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isNotEqualTo(0);
        assertThat(errContent.toString())
            .contains("Unknown option: '--invalid-option'")
            .contains("Usage: cm [-hV] [COMMAND]");
    }

    @Test
    @DisplayName("main() should print error and usage when called with an invalid subcommand")
    void main_WhenCalledWithInvalidSubcommand_ThenPrintsErrorAndUsage() {
        // Given
        String[] args = {"invalid-command"};

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isNotEqualTo(0);
        assertThat(errContent.toString()).contains("Unmatched argument at index 0: 'invalid-command'");
    }

    @Test
    @DisplayName("runCLI() should return 0 and print help when called with --help option")
    void runCLI_WhenCalledWithHelpOption_ThenReturnsZeroAndPrintsHelp() {
        // Given
        String[] args = {"--help"};

        // When
        int exitCode = ContextMapperCLI.runCLI(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        assertThat(outContent.toString())
            .contains("Usage: cm [-hV] [COMMAND]")
            .contains("Commands:")
            .contains("validate  Validates a CML file.")
            .contains("generate  Generates output from a CML file.");
    }
}
