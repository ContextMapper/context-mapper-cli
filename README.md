![Context Mapper](https://raw.githubusercontent.com/wiki/ContextMapper/context-mapper-dsl/logo/cm-logo-github-small.png)
# Context Mapper CLI
[![Build (master)](https://github.com/ContextMapper/context-mapper-cli/actions/workflows/build_master.yml/badge.svg)](https://github.com/ContextMapper/context-mapper-cli/actions) [![codecov](https://codecov.io/gh/ContextMapper/context-mapper-cli/branch/master/graph/badge.svg?token=OMqxkddZOJ)](https://codecov.io/gh/ContextMapper/context-mapper-cli) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

This repository contains the Context Mapper CLI - a command line interface to compile CML files and call generators
(currently PlantUML, Context Map, and generic text files via Freemarker template).

## Compile Command Usage
```shell
$ ./cm compile -h
Context Mapper CLI
usage: cm compile
 -h,--help          Prints this message.
 -i,--input <arg>   Path to the CML file which you want to compile.
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

### Compile/Validate *.cml File

```shell
./cm compile -i DDD-Sample.cml
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
* Create Github issues if you find bugs or just want to give suggestions for improvements.
* This is an open source project: if you want to code, [create pull requests](https://help.github.com/articles/creating-a-pull-request/) from [forks of this repository](https://help.github.com/articles/fork-a-repo/). Please refer to a Github issue if you contribute this way.
* If you want to contribute to our documentation and user guides on our website [https://contextmapper.org/](https://contextmapper.org/), create pull requests from forks of the corresponding page repo [https://github.com/ContextMapper/contextmapper.github.io](https://github.com/ContextMapper/contextmapper.github.io) or create issues [there](https://github.com/ContextMapper/contextmapper.github.io/issues).

## Licence
ContextMapper is released under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

