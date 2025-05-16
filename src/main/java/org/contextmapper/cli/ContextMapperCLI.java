package org.contextmapper.cli;

import org.contextmapper.cli.commands.GenerateCommand;
import org.contextmapper.cli.commands.ValidateCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
    name = "cm",
    versionProvider = VersionProvider.class,
    description = "Context Mapper CLI",
    subcommands = {
        ValidateCommand.class,
        GenerateCommand.class
    },
    mixinStandardHelpOptions = true,
    usageHelpAutoWidth = true)
public class ContextMapperCLI implements Runnable {

    @Override
    public void run() {
        System.out.println("Context Mapper CLI. Use 'cm --help' for usage information.");
    }

    public static int runCLI(String[] args) {
        return new CommandLine(new ContextMapperCLI()).execute(args);
    }

    public static void main(String[] args) {
        int exitCode = runCLI(args);
        System.exit(exitCode);
    }
}
