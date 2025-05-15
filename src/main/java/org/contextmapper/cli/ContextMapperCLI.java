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

import org.contextmapper.cli.commands.GenerateCommand;
import org.contextmapper.cli.commands.ValidateCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "cm", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class,
        description = "Context Mapper CLI",
        subcommands = {
                ValidateCommand.class,
                GenerateCommand.class
        })
public class ContextMapperCLI implements Runnable {

    private static final int REQUIRED_JAVA_VERSION = 11;

    public static void main(String[] args) {
        if (Runtime.version().feature() < REQUIRED_JAVA_VERSION) {
            System.err.printf("Invalid Java version '%s' (>=%s is required).%n", Runtime.version().feature(), REQUIRED_JAVA_VERSION);
            System.exit(1);
        }
        int exitCode = new CommandLine(new ContextMapperCLI()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        // This is executed if no subcommand is specified.
        // Picocli will show the help message by default if mixinStandardHelpOptions = true and no subcommand is given.
        // We can add a custom message here if needed, or rely on Picocli's default behavior.
        System.out.println("Context Mapper CLI. Use 'cm --help' for usage information.");
    }

}

class VersionProvider implements CommandLine.IVersionProvider {
    @Override
    public String[] getVersion() throws Exception {
        String implVersion = ContextMapperCLI.class.getPackage().getImplementationVersion();
        return new String[]{"Context Mapper CLI " + (implVersion != null ? "v" + implVersion : "DEVELOPMENT VERSION")};
    }
}
