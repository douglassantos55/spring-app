package br.com.fastfood.restaurant;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class ValidZipcodeProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of("01001000"),
                Arguments.of("13845-000"),
                Arguments.of("20020050"),
                Arguments.of("63240-000"),
                Arguments.of("69316-534"),
                Arguments.of("89809-535"),
                Arguments.of("49030-533"),
                Arguments.of("76804-371"),
                Arguments.of("07179-293")
        );
    }
}
