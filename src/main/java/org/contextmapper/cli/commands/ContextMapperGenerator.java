package org.contextmapper.cli.commands;

import org.contextmapper.dsl.generator.ContextMapGenerator;
import org.contextmapper.dsl.generator.GenericContentGenerator;
import org.contextmapper.dsl.generator.PlantUMLGenerator;
import org.eclipse.xtext.generator.IGenerator2;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum ContextMapperGenerator {

    CONTEXT_MAP("context-map", "Graphical DDD Context Map"),
    PLANT_UML("plantuml", "PlantUML class-, component-, and state diagrams."),
    GENERIC("generic", "Generate generic text with Freemarker template");

    private final String name;
    private final String description;

    ContextMapperGenerator(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplayName() {
        return this.name + " (" + this.description + ")";
    }

    @Override
    public String toString() {
        // Picocli uses toString() for default value display and for completion candidates if no specific provider is set.
        // Returning only the name makes it cleaner for command-line usage.
        return this.name;
    }

    public static ContextMapperGenerator byName(String name) {
        if (name == null || "".equals(name))
            throw new IllegalArgumentException("Please provide a name for the generator.");

        for (ContextMapperGenerator generator : values()) {
            if (generator.getName().equalsIgnoreCase(name)) // Make it case-insensitive for user-friendliness
                return generator;
        }

        throw new IllegalArgumentException("No generator found for the name '" + name + "'. Valid values are: " +
                Arrays.stream(values()).map(ContextMapperGenerator::getName).collect(Collectors.joining(", ")));
    }

    public IGenerator2 getGenerator() {
        if (this == CONTEXT_MAP)
            return new ContextMapGenerator();
        if (this == PLANT_UML)
            return new PlantUMLGenerator();
        // Assumes GENERIC is the only other case based on current enum values
        return new GenericContentGenerator();
    }

}
