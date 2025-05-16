package org.contextmapper.cli;

import java.util.Objects;

import picocli.CommandLine.IVersionProvider;

class VersionProvider implements IVersionProvider {

    @Override
    public String[] getVersion() throws Exception {
        Package programPackage = getPackageToInspect();
        String implVersion = null;
        if (Objects.nonNull(programPackage)) {
            implVersion = programPackage.getImplementationVersion();
        }
        return new String[]{"Context Mapper CLI " + (implVersion != null ? "v" + implVersion : "DEVELOPMENT VERSION")};
    }

    //Refactored to help the testing process
    protected Package getPackageToInspect() {
        return ContextMapperCLI.class.getPackage();
    }
}
