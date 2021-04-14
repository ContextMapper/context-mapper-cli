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
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompileCommandTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @ParameterizedTest
    @ValueSource(strings = {"-h", "--help", "-i some-file.cml -h", "-i some-file.cml --help"})
    void run_WhenCalledWithHelp_ThenPrintHelp(final String params) {
        // given
        final CompileCommand command = spy(new CompileCommand());

        // when
        command.run(params.split(" "));

        // then
        verify(command).printHelp(any());
    }

    @Test
    void run_WhenWithValidCMLFile_ThenCompileWithoutErrors() {
        // given
        final CompileCommand command = spy(new CompileCommand());

        // when
        command.run(new String[]{"-i src/test/resources/test.cml"});

        // then
        verify(command).printValidationMessages(any(), any());
        assertThat(outContent.toString()).contains("The CML file 'src/test/resources/test.cml' has been compiled without errors.");
    }

    @Test
    void run_WhenWithInvalidCMLFile_ThenPrintError() {
        // given
        final CompileCommand command = spy(new CompileCommand());

        // when
        command.run(new String[]{"-i src/test/resources/test-with-error.cml"});

        // then
        verify(command).printValidationMessages(any(), any());
        assertThat(outContent.toString()).contains("ERROR in null on line 2:mismatched input '<EOF>' expecting RULE_CLOSE");
    }

}
