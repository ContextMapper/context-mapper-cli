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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenerateCommandTest {

    private static final String TEST_OUT_DIR = "build/test-out";

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    private File testOutDir;

    @BeforeEach
    public void setUpStreams() throws IOException {
        testOutDir = new File(TEST_OUT_DIR);
        if (testOutDir.exists()) {
            Files.walk(testOutDir.toPath())
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
        testOutDir.mkdir();

        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @ParameterizedTest
    @ValueSource(strings = {"-h", "--help", "-i some-file.cml -g plantuml -h", "-i some-file.cml -g plantuml --help"})
    void run_WhenCalledWithHelp_ThenPrintHelp(final String params) {
        // given
        final GenerateCommand command = spy(new GenerateCommand());

        // when
        command.run(params.split(" "));

        // then
        verify(command).printHelp(any());
    }

    @Test
    void run_WhenCalledWithNonExistingOutDir_ThenPrintError() {
        // given
        final GenerateCommand command = spy(new GenerateCommand());

        // when
        command.run(new String[]{"-i", "src/test/resources/test.cml", "-g", "plantuml", "-o", "/just-some-dir-that-hopefully-not-exists"});

        // then
        assertThat(outContent.toString()).contains("ERROR: '/just-some-dir-that-hopefully-not-exists' is not a directory.");
    }

    @Test
    void run_WhenCalledWithNonExistingInputFile_ThenPrintError() {
        // given
        final GenerateCommand command = spy(new GenerateCommand());

        // when
        command.run(new String[]{"-i", "just-a-file.cml", "-g", "plantuml", "-o", "/build"});

        // then
        assertThat(outContent.toString()).contains("ERROR: The file 'just-a-file.cml' does not exist.");
    }

    @Test
    void run_WhenCalledWithPlantUMLParam_ThenGeneratePlantUMLFiles() {
        // given
        final GenerateCommand command = spy(new GenerateCommand());
        new File("build/test-out").mkdir();

        // when
        command.run(new String[]{"-i", "src/test/resources/test.cml", "-g", "plantuml", "-o", "build/test-out"});

        // then
        assertThat(outContent.toString()).contains("Generated into 'build/test-out'.");
        assertThat(new File("build/test-out/test_BC_CargoBookingContext.puml").exists()).isTrue();
        assertThat(new File("build/test-out/test_BC_LocationContext.puml").exists()).isTrue();
        assertThat(new File("build/test-out/test_BC_VoyagePlanningContext.puml").exists()).isTrue();
        assertThat(new File("build/test-out/test_ContextMap.puml").exists()).isTrue();
    }

    @Test
    void run_WhenCalledWithContextMapParam_ThenGenerateContextMapFiles() {
        // given
        final GenerateCommand command = spy(new GenerateCommand());
        new File("build/test-out").mkdir();

        // when
        command.run(new String[]{"-i", "src/test/resources/test.cml", "-g", "context-map", "-o", "build/test-out"});

        // then
        assertThat(outContent.toString()).contains("Generated into 'build/test-out'.");
        assertThat(new File("build/test-out/test_ContextMap.gv").exists()).isTrue();
        assertThat(new File("build/test-out/test_ContextMap.png").exists()).isTrue();
        assertThat(new File("build/test-out/test_ContextMap.svg").exists()).isTrue();
    }

    @Test
    void run_WhenCalledWithGenericParam_ThenGenerateGenericOutput() {
        // given
        final GenerateCommand command = spy(new GenerateCommand());
        new File("build/test-out").mkdir();

        // when
        command.run(new String[]{"-i", "src/test/resources/test.cml", "-g", "generic", "-o", "build/test-out", "-t", "src/test/resources/test.ftl", "-f", "test.md"});

        // then
        assertThat(outContent.toString()).contains("Generated into 'build/test-out'.");
        assertThat(new File("build/test-out/test.md").exists()).isTrue();
    }

}
