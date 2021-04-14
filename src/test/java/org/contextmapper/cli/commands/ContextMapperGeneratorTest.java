package org.contextmapper.cli.commands;

import org.eclipse.xtext.generator.IGenerator2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ContextMapperGeneratorTest {

    @ParameterizedTest
    @ValueSource(strings = {"CONTEXT_MAP", "PLANT_UML", "GENERIC"})
    void getName_WhenUsingGeneratorEnumValue_ThenCanGetGeneratorName(final String enumValueAsString) {
        // given
        final ContextMapperGenerator generator = ContextMapperGenerator.valueOf(enumValueAsString);

        // when
        final String name = generator.getName();

        // then
        assertThat(name)
                .isNotNull()
                .isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"CONTEXT_MAP", "PLANT_UML", "GENERIC"})
    void getDescription_WhenUsingGeneratorEnumValue_ThenCanGetGeneratorDescription(final String enumValueAsString) {
        // given
        final ContextMapperGenerator generator = ContextMapperGenerator.valueOf(enumValueAsString);

        // when
        final String description = generator.getDescription();

        // then
        assertThat(description)
                .isNotNull()
                .isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"CONTEXT_MAP", "PLANT_UML", "GENERIC"})
    void toString_WhenUsingGeneratorEnumValue_ThenCanGetStringRepresentation(final String enumValueAsString) {
        // given
        final ContextMapperGenerator generator = ContextMapperGenerator.valueOf(enumValueAsString);

        // when
        final String stringRepresentation = generator.toString();

        // then
        assertThat(stringRepresentation)
                .isNotNull()
                .isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"context-map", "plantuml", "generic"})
    void byName_WhenWithValidName_ThenReturnGenerator(final String validGeneratorKey) {
        // when
        final ContextMapperGenerator generator = ContextMapperGenerator.byName(validGeneratorKey);

        // then
        assertThat(generator).isNotNull();
    }

    @Test
    void byName_WhenWithoutName_ThenThrowIllegalArgumentException() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                ContextMapperGenerator.byName(null));
    }

    @Test
    void byName_WhenWithEmptyName_ThenThrowIllegalArgumentException() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                ContextMapperGenerator.byName(""));
    }

    @Test
    void byName_WhenWithInvalidName_ThenThrowIllegalArgumentException() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                ContextMapperGenerator.byName("just a string"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"CONTEXT_MAP", "PLANT_UML", "GENERIC"})
    void getGenerator_WhenCalled_ThenReturnGeneratorImplementation(final String enumValueAsString) {
        // given
        final ContextMapperGenerator generator = ContextMapperGenerator.valueOf(enumValueAsString);

        // when
        final IGenerator2 generatorImpl = generator.getGenerator();

        // then
        assertThat(generatorImpl).isNotNull();
    }

}
