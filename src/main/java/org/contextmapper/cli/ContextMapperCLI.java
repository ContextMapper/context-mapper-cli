package org.contextmapper.cli;

import org.contextmapper.cli.commands.GenerateCommand;
import org.contextmapper.cli.commands.ValidateCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import java.util.function.Supplier;

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

    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(new ContextMapperCLI());
        int exitCode = commandLine.execute(args);
        System.exit(exitCode);
    }
}
