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

import org.contextmapper.cli.commands.CliCommand;
import org.contextmapper.cli.commands.CompileCommand;
import org.contextmapper.cli.commands.GenerateCommand;

import java.util.Arrays;

public class ContextMapperCLI {

    private static final String COMPILE_COMMAND = "compile";
    private static final String GENERATE_COMMAND = "generate";

    private CliCommand generateCommand;
    private CliCommand compileCommand;

    public ContextMapperCLI() {
        this.generateCommand = new GenerateCommand();
        this.compileCommand = new CompileCommand();
    }

    public static void main(String[] args) {
        new ContextMapperCLI().run(args);
    }

    protected void run(String[] args) {
        System.out.println("Context Mapper CLI");

        if (args == null || args.length == 0) {
            printUsages();
        } else if (COMPILE_COMMAND.equalsIgnoreCase(args[0])) {
            compileCommand.run(Arrays.copyOfRange(args, 1, args.length));
        } else if (GENERATE_COMMAND.equalsIgnoreCase(args[0])) {
            generateCommand.run(Arrays.copyOfRange(args, 1, args.length));
        }
    }

    private void printUsages() {
        System.out.println("Usage: cm " + COMPILE_COMMAND + "|" + GENERATE_COMMAND + " [options]");
    }

}
