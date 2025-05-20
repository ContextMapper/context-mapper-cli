Feature: Context Mapper CLI Usage
  As a user of the Context Mapper CLI
  I want to be able to validate CML files and generate various outputs
  So that I can effectively use the tool for my Domain-Driven Design modeling.

  Background:
    Given the Context Mapper CLI is installed

  Scenario: Display help for the validate command
    When I run the command `./cm validate -h`
    Then the output should contain:
      """
      Context Mapper CLI
      usage: cm validate
       -h,--help          Prints this message.
       -i,--input <arg>   Path to the CML file which you want to validate.
      """

  Scenario: Validate a CML file
    Given a CML file named "DDD-Sample.cml" exists
    When I run the command `./cm validate -i DDD-Sample.cml`
    Then the CLI should validate "DDD-Sample.cml" successfully

  Scenario: Display help for the generate command
    When I run the command `./cm generate -h`
    Then the output should contain:
      """
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
      """

  Scenario: Generate PlantUML output
    Given a CML file named "DDD-Sample.cml" exists
    And an output directory named "output-directory"
    When I run the command `./cm generate -i DDD-Sample.cml -g plantuml -o ./output-directory`
    Then PlantUML diagrams should be generated in "./output-directory" from "DDD-Sample.cml"

  Scenario: Generate Context Map output
    Given a CML file named "DDD-Sample.cml" exists
    And an output directory named "output-directory"
    When I run the command `./cm generate -i DDD-Sample.cml -g context-map -o ./output-directory`
    Then a Context Map should be generated in "./output-directory" from "DDD-Sample.cml"

  Scenario: Generate arbitrary text file with Freemarker template
    Given a CML file named "DDD-Sample.cml" exists
    And an output directory named "output-directory"
    And a Freemarker template file named "template.md.ftl"
    When I run the command `./cm generate -i DDD-Sample.cml -g generic -o ./output-directory -t template.md.ftl -f glossary.md`
    Then a file named "glossary.md" should be generated in "./output-directory" using "template.md.ftl" and "DDD-Sample.cml" 