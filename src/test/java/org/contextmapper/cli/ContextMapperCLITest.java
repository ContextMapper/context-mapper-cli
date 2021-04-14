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
package org.contextmapper.cli;

import org.contextmapper.cli.commands.CompileCommand;
import org.contextmapper.cli.commands.GenerateCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ContextMapperCLITest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Mock
    private CompileCommand compileCommand;

    @Mock
    private GenerateCommand generateCommand;

    @InjectMocks
    private ContextMapperCLI contextMapperCLI;

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

    @Test
    void main_WhenCalledWithoutCommand_ThenPrintUsage() {
        // given
        final String[] params = new String[]{};

        // when
        ContextMapperCLI.main(params);

        // then
        assertThat(outContent.toString()).isEqualTo("Context Mapper CLI DEVELOPMENT VERSION" + System.lineSeparator() +
                "Usage: cm compile|generate [options]" + System.lineSeparator());
    }

    @Test
    void run_WhenCalledWithoutCommand_ThenPrintUsage() {
        // given
        final String[] params = new String[]{};

        // when
        contextMapperCLI.run(params);

        // then
        assertThat(outContent.toString()).isEqualTo("Context Mapper CLI DEVELOPMENT VERSION" + System.lineSeparator() +
                "Usage: cm compile|generate [options]" + System.lineSeparator());
    }

    @Test
    void run_WhenCalledWithCompile_ThenCallCompileCommand() {
        // given
        final String[] params = new String[]{"compile"};

        // when
        contextMapperCLI.run(params);

        // then
        verify(compileCommand).run(new String[]{});
    }

    @Test
    void run_WhenCalledWithCompileAndAdditionalParams_ThenCallCompileCommandWithParams() {
        // given
        final String[] params = new String[]{"compile", "test-param"};

        // when
        contextMapperCLI.run(params);

        // then
        verify(compileCommand).run(new String[]{"test-param"});
    }

    @Test
    void run_WhenCalledWithGenerate_ThenCallGenerateCommand() {
        // given
        final String[] params = new String[]{"generate"};

        // when
        contextMapperCLI.run(params);

        // then
        verify(generateCommand).run(new String[]{});
    }

    @Test
    void run_WhenCalledWithGenerateAndAdditionalParams_ThenCallGenerateCommandWithParams() {
        // given
        final String[] params = new String[]{"generate", "plantuml"};

        // when
        contextMapperCLI.run(params);

        // then
        verify(generateCommand).run(new String[]{"plantuml"});
    }

}
