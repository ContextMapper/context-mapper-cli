![Context Mapper](https://raw.githubusercontent.com/wiki/ContextMapper/context-mapper-dsl/logo/cm-logo-github-small.png)
# Context Mapper CLI
[![Build (master)](https://github.com/ContextMapper/context-mapper-cli/actions/workflows/build_master.yml/badge.svg)](https://github.com/ContextMapper/context-mapper-cli/actions) [![codecov](https://codecov.io/gh/ContextMapper/context-mapper-cli/branch/master/graph/badge.svg?token=OMqxkddZOJ)](https://codecov.io/gh/ContextMapper/context-mapper-cli) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![Maven Central](https://img.shields.io/maven-central/v/org.contextmapper/context-mapper-cli.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22org.contextmapper%22%20AND%20a:%22context-mapper-cli%22)

This repository contains the Context Mapper CLI - a command line interface to validate CML files and call generators
(currently PlantUML, Context Map, and generic text files via Freemarker template).

## Download
The CLI can be downloaded under the following links:
 * [TAR](https://repo1.maven.org/maven2/org/contextmapper/context-mapper-cli/6.11.1/context-mapper-cli-6.11.1.tar) (Linux, Mac)
 * [ZIP](https://repo1.maven.org/maven2/org/contextmapper/context-mapper-cli/6.11.1/context-mapper-cli-6.11.1.zip) (Windows)

In case you want to include Context Mapper in your Maven or Gradle build, you can use the CLI to call generators (for example with Maven exec plugin).

**Gradle:**
```gradle
implementation 'org.contextmapper:context-mapper-cli:6.11.1'
```

**Maven:**
```xml
<dependency>
  <groupId>org.contextmapper</groupId>
  <artifactId>context-mapper-cli</artifactId>
  <version>6.11.1</version>
</dependency>
```

## Validate Command Usage
```shell
$ ./cm validate -h
Context Mapper CLI
usage: cm validate
 -h,--help          Prints this message.
 -i,--input <arg>   Path to the CML file which you want to validate.
```

## Generate Command Usage
```shell
$ ./cm generate -h
Context Mapper CLI
usage: cm generate
 -f,--outputFile <arg>   The name of the file that shall be generated
                         (only used by Freemarker generator, as we cannot
                         know the file extension).
 -g,--generator <arg>    The generator you want to call. Use one of the
                         following values: context-map (Graphical DDD
                         Context Map), plantuml (PlantUML class-,
                         component-, and state diagrams.), generic
                         (Generate generic text with Freemarker template)
 -h,--help               Prints this message.
 -i,--input <arg>        Path to the CML file for which you want to
                         generate output.
 -o,--outputDir <arg>    Path to the directory into which you want to
                         generate.
 -t,--template <arg>     Path to the Freemarker template you want to use.
                         This parameter is only used if you pass 'generic'
                         to the 'generator' (-g) parameter.
```

## Usage examples
The following examples illustrate the CLI usage.

### Validate *.cml File

```shell
./cm validate -i DDD-Sample.cml
```

### Generate PlantUML

```shell
./cm generate -i DDD-Sample.cml -g plantuml -o ./output-directory
```

### Generate Context Map

```shell
./cm generate -i DDD-Sample.cml -g context-map -o ./output-directory
```

### Generate Arbitrary Text File with Freemarker Template

```shell
./cm generate -i DDD-Sample.cml -g generic -o ./output-directory -t template.md.ftl -f glossary.md
```

## Development / Build
If you want to contribute to this project you can create a fork and a pull request. The project is built with Gradle, so you can import it as Gradle project within Eclipse or IntelliJ IDEA (or any other IDE supporting Gradle).

## Contributing
Contribution is always welcome! Here are some ways how you can contribute:
* Create GitHub issues if you find bugs or just want to give suggestions for improvements.
* This is an open source project: if you want to code, [create pull requests](https://help.github.com/articles/creating-a-pull-request/) from [forks of this repository](https://help.github.com/articles/fork-a-repo/). Please refer to a Github issue if you contribute this way.
* If you want to contribute to our documentation and user guides on our website [https://contextmapper.org/](https://contextmapper.org/), create pull requests from forks of the corresponding page repo [https://github.com/ContextMapper/contextmapper.github.io](https://github.com/ContextMapper/contextmapper.github.io) or create issues [there](https://github.com/ContextMapper/contextmapper.github.io/issues).

## Licence
ContextMapper is released under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

