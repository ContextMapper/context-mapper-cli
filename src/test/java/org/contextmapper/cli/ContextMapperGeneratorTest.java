package org.contextmapper.cli;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.eclipse.xtext.generator.IGenerator2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class ContextMapperGeneratorTest {

    @ParameterizedTest
    @CsvSource({
            "CONTEXT_MAP, context-map",
            "PLANT_UML, plantuml",
            "GENERIC, generic"
    })
    void getName_WhenUsingGeneratorEnumValue_ThenCanGetGeneratorName(ContextMapperGenerator generator,
            String expectedName) {
        // Given
        // generator and expectedName are provided by @CsvSource

        // When
        final String name = generator.getName();

        // Then
        assertThat(name).isEqualTo(expectedName);
    }

    @ParameterizedTest
    @ValueSource(strings = { "CONTEXT_MAP", "PLANT_UML", "GENERIC" })
    void getDescription_WhenUsingGeneratorEnumValue_ThenCanGetGeneratorDescription(final String enumValueAsString) {
        // Given
        final ContextMapperGenerator generator = ContextMapperGenerator.valueOf(enumValueAsString);

        // When
        final String description = generator.getDescription();

        // Then
        assertThat(description)
                .isNotNull()
                .isNotEmpty();
    }

    @ParameterizedTest
    @CsvSource({
            "CONTEXT_MAP, context-map",
            "PLANT_UML, plantuml",
            "GENERIC, generic"
    })
    void toString_ReturnsName(ContextMapperGenerator generator, String expectedName) {
        // Given
        // generator and expectedName are provided by @CsvSource

        // When
        final String stringRepresentation = generator.toString();

        // Then
        assertThat(stringRepresentation).isEqualTo(expectedName);
    }

    @ParameterizedTest
    @ValueSource(strings = { "context-map", "plantuml", "generic", "CONTEXT-MAP", "PlantUML", "GeNeRiC" })
    void byName_WhenWithValidName_ThenReturnGenerator(final String validGeneratorKey) {
        // Given
        // validGeneratorKey is provided by @ValueSource

        // When
        final ContextMapperGenerator generator = ContextMapperGenerator.byName(validGeneratorKey);

        // Then
        assertThat(generator).isNotNull();
    }

    @Test
    void byName_WhenWithoutName_ThenThrowIllegalArgumentException() {
        // Given
        // No specific setup needed

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> ContextMapperGenerator.byName(null))
                .withMessageContaining("Please provide a name for the generator.");
    }

    @Test
    void byName_WhenWithEmptyName_ThenThrowIllegalArgumentException() {
        // Given
        // No specific setup needed

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> ContextMapperGenerator.byName(""))
                .withMessageContaining("Please provide a name for the generator.");
    }

    @Test
    void byName_WhenWithInvalidName_ThenThrowIllegalArgumentException() {
        // Given
        // No specific setup needed

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ContextMapperGenerator.byName("just a string"))
                .withMessageContaining(
                        "No generator found for the name 'just a string'. Valid values are: context-map, plantuml, generic");
    }

    @ParameterizedTest
    @ValueSource(strings = { "CONTEXT_MAP", "PLANT_UML", "GENERIC" })
    void getGenerator_WhenCalled_ThenReturnGeneratorImplementation(final String enumValueAsString) {
        // Given
        final ContextMapperGenerator generator = ContextMapperGenerator.valueOf(enumValueAsString);

        // When
        final IGenerator2 generatorImpl = generator.getGenerator();

        // Then
        assertThat(generatorImpl).isNotNull();
    }

    @ParameterizedTest
    @CsvSource({
            "CONTEXT_MAP, context-map (Graphical DDD Context Map)",
            "PLANT_UML, plantuml (PlantUML class-, component-, and state diagrams.)",
            "GENERIC, generic (Generate generic text with Freemarker template)"
    })
    void getDisplayName_ReturnsCorrectFormat(ContextMapperGenerator generator, String expectedDisplayName) {
        // Given
        // generator and expectedDisplayName are provided by @CsvSource

        // When
        String displayName = generator.getDisplayName();

        // Then
        assertThat(displayName).contains(expectedDisplayName);
    }
}
