package org.contextmapper.cli.commands;

import org.contextmapper.dsl.cml.CMLResource;
import org.contextmapper.dsl.standalone.ContextMapperStandaloneSetup;
import org.contextmapper.dsl.standalone.StandaloneContextMapperAPI;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.util.concurrent.Callable;

@Command(name = "validate", description = "Validates a CML file.", mixinStandardHelpOptions = true)
public class ValidateCommand implements Callable<Integer> {

    @Option(names = {"-i", "--input"}, description = "Path to the CML file which you want to validate.", required = true)
    private String inputPath;

    @Override
    public Integer call() throws Exception {
        if (!isInputFileValid(inputPath)) {
            return 1;
        }

        StandaloneContextMapperAPI cmAPI = ContextMapperStandaloneSetup.getStandaloneAPI();
        CMLResource cmlResource = cmAPI.loadCML(inputPath);

        printValidationMessages(cmlResource, inputPath);

        return cmlResource.getErrors().isEmpty() ? 0 : 1;
    }

    protected boolean isInputFileValid(String path) {
        File inputFile = new File(path);
        if (!inputFile.exists()) {
            System.err.println("ERROR: The file '" + path + "' does not exist.");
            return false;
        }
        if (!path.endsWith(".cml")) {
            System.err.println("ERROR: Please provide a path to a CML (*.cml) file.");
            return false;
        }
        return true;
    }

    protected void printValidationMessages(final CMLResource cmlResource, final String filePath) {
        if (cmlResource.getErrors().isEmpty()) {
            System.out.println("The CML file '" + filePath + "' has been validated without errors.");
        } else {
            for (Diagnostic diagnostic : cmlResource.getErrors()) {
                System.err.println("ERROR in " + diagnostic.getLocation() + " on line " + diagnostic.getLine() + ":" + diagnostic.getMessage());
            }
        }

        for (Diagnostic diagnostic : cmlResource.getWarnings()) {
            System.out.println("WARNING in " + diagnostic.getLocation() + " on line " + diagnostic.getLine() + ":" + diagnostic.getMessage());
        }
    }
}
