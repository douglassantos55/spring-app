package br.com.fastfood.restaurant;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class InvalidZipcodeProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of("01001-0000"),
                Arguments.of("010010000"),
                Arguments.of("13840-000"),
                Arguments.of("00000-000"),
                Arguments.of("string"),
                Arguments.of("O1OO1OOO"),
                Arguments.of("68310")
        );
    }
}
