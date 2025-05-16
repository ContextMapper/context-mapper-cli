package org.contextmapper.cli;

import java.util.Optional;

import picocli.CommandLine.IVersionProvider;

class VersionProvider implements IVersionProvider {

    @Override
    public String[] getVersion() throws Exception {
        Package programPackage = getPackageToInspect();
        String versionString = Optional.ofNullable(programPackage)
                .map(Package::getImplementationVersion)
                .map(v -> "v" + v)
                .orElse("DEVELOPMENT VERSION");
        return new String[]{"Context Mapper CLI " + versionString};
    }

    //Refactored to help the testing process
    protected Package getPackageToInspect() {
        return ContextMapperCLI.class.getPackage();
    }
}
