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

import org.apache.commons.cli.*;
import org.contextmapper.dsl.cml.CMLResource;
import org.contextmapper.dsl.standalone.ContextMapperStandaloneSetup;
import org.contextmapper.dsl.standalone.StandaloneContextMapperAPI;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;

public class ValidateCommand extends AbstractCliCommand {

    @Override
    public void run(String[] args) {
        Options options = createOptions();

        CommandLineParser commandLineParser = new DefaultParser();

        try {
            CommandLine cmd = commandLineParser.parse(options, args);

            if (cmd.hasOption("help") || cmd.hasOption("h")) {
                printHelp(options);
            } else {
                // check that input CML files exists
                String inputPath = cmd.getOptionValue("input").trim();
                if (!isInputFileValid(inputPath))
                    return;

                // load CML file
                StandaloneContextMapperAPI cmAPI = ContextMapperStandaloneSetup.getStandaloneAPI();
                CMLResource cmlResource = cmAPI.loadCML(inputPath);

                // print validation errors/warnings
                printValidationMessages(cmlResource, inputPath);
            }
        } catch (ParseException e) {
            printHelp(options);
        }
    }

    private Options createOptions() {
        Options options = new Options();

        Option help = new Option("h", "help", false, "Prints this message.");
        options.addOption(help);

        Option input = new Option("i", "input", true, "Path to the CML file which you want to validate.");
        input.setRequired(true);
        options.addOption(input);

        return options;
    }

    protected void printValidationMessages(final CMLResource cmlResource, final String filePath) {
        if (cmlResource.getErrors().isEmpty()) {
            System.out.println("The CML file '" + filePath + "' has been validated without errors.");
        } else {
            for (Diagnostic diagnostic : cmlResource.getErrors()) {
                System.out.println("ERROR in " + diagnostic.getLocation() + " on line " + diagnostic.getLine() + ":"
                        + diagnostic.getMessage());
            }
        }

        for (Diagnostic diagnostic : cmlResource.getWarnings()) {
            System.out.println("WARNING in " + diagnostic.getLocation() + " on line " + diagnostic.getLine() + ":"
                    + diagnostic.getMessage());
        }
    }

    protected void printHelp(final Options options) {
        new HelpFormatter().printHelp("cm validate", options);
    }

}
