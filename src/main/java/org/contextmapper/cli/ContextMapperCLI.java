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

    private static final int REQUIRED_JAVA_VERSION = 11;
    static Supplier<Integer> javaVersionSupplier = () -> Runtime.version().feature();

    @Override
    public void run() {
        System.out.println("Context Mapper CLI. Use 'cm --help' for usage information.");
    }

    static void checkJavaVersion(Runnable exiter) {
        int currentVersion = javaVersionSupplier.get();
        if (currentVersion < REQUIRED_JAVA_VERSION) {
            System.err.printf("Invalid Java version '%s' (>=%s is required).%n", currentVersion, REQUIRED_JAVA_VERSION);
            exiter.run();
        }
    }

    private static void checkJavaVersion() {
        checkJavaVersion(() -> System.exit(1));
    }

    static int runCLI(String[] args) {
        return new CommandLine(new ContextMapperCLI()).execute(args);
    }

    public static void main(String[] args) {
        checkJavaVersion();
        int exitCode = runCLI(args);
        System.exit(exitCode);
    }
}
