package org.contextmapper.cli.commands;

import org.contextmapper.dsl.generator.ContextMapGenerator;
import org.contextmapper.dsl.generator.GenericContentGenerator;
import org.contextmapper.dsl.generator.PlantUMLGenerator;
import org.eclipse.xtext.generator.IGenerator2;

import java.util.Arrays;
import java.util.Objects;
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
        return this.name;
    }

    public static ContextMapperGenerator byName(String name) {
        // Preconditions check
        if (Objects.isNull(name) || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Please provide a name for the generator.");
        }
        return Arrays.stream(values())
                .filter(gen -> gen.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No generator found for the name '" + name +
                        "'. Valid values are: " +
                        Arrays.stream(values()).map(ContextMapperGenerator::getName).collect(Collectors.joining(", "))));
    }

    public IGenerator2 getGenerator() {
        return switch (this) {
            case CONTEXT_MAP -> new ContextMapGenerator();
            case PLANT_UML -> new PlantUMLGenerator();
            case GENERIC -> new GenericContentGenerator();
        };
    }
}
