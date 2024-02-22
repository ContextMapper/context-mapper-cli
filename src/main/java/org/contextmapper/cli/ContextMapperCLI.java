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
import org.contextmapper.cli.commands.GenerateCommand;
import org.contextmapper.cli.commands.ValidateCommand;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ContextMapperCLI {

    private static final int REQUIRED_JAVA_VERSION = 11;
    private static final String VALIDATE_COMMAND = "validate";
    private static final String GENERATE_COMMAND = "generate";

    private CliCommand generateCommand;
    private CliCommand validateCommand;

    public ContextMapperCLI() {
        this.generateCommand = new GenerateCommand();
        this.validateCommand = new ValidateCommand();
    }

    public static void main(String[] args) {
        int javaVersion = Runtime.version().feature();

        if (Runtime.version().feature() >= REQUIRED_JAVA_VERSION) {
            new ContextMapperCLI().run(args);
        } else {
            System.out.printf("Invalid Java version '%s' (>=%s is required).", javaVersion, REQUIRED_JAVA_VERSION);
            System.exit(1);
        }
    }

    protected void run(String[] args) {
        System.out.println("Context Mapper CLI " + getVersion());

        if (args == null || args.length == 0) {
            printUsages();
        } else if (VALIDATE_COMMAND.equalsIgnoreCase(args[0])) {
            validateCommand.run(Arrays.copyOfRange(args, 1, args.length));
        } else if (GENERATE_COMMAND.equalsIgnoreCase(args[0])) {
            generateCommand.run(Arrays.copyOfRange(args, 1, args.length));
        } else {
            System.out.println("Invalid input");
            System.exit(127);
        }
    }

    private void printUsages() {
        System.out.println("Usage: cm " + VALIDATE_COMMAND + "|" + GENERATE_COMMAND + " [options]");
    }

    private String getVersion() {
        String implVersion = ContextMapperCLI.class.getPackage().getImplementationVersion();
        return implVersion != null ? "v" + implVersion : "DEVELOPMENT VERSION";
    }

}
