package org.contextmapper.cli.commands;

import java.io.File;

public abstract class AbstractCliCommand implements CliCommand {

    protected boolean isInputFileValid(String inputPath) {
        File inputFile = new File(inputPath);
        if (!inputFile.exists()) {
            System.out.println("ERROR: The file '" + inputPath + "' does not exist.");
            return false;
        }
        if (!inputPath.endsWith(".cml")) {
            System.out.println("ERROR: Please provide a path to a CML (*.cml) file.");
            return false;
        }
        return true;
    }

}
