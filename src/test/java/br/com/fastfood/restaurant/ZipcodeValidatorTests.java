package br.com.fastfood.restaurant;

import br.com.fastfood.restaurant.validation.ZipcodeValidator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
@ContextConfiguration(classes = Config.class)
public class ZipcodeValidatorTests {
    @Autowired
    private ZipcodeValidator validator;

    @ParameterizedTest
    @ArgumentsSource(ValidZipcodeProvider.class)
    void validZipcodes(String zipcode) {
        assertTrue(String.format("%s is a valid zipcode", zipcode), this.validator.isValid(zipcode, null));
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidZipcodeProvider.class)
    void invalidZipcodes(String zipcode) {
        assertFalse(String.format("%s is a invalid zipcode", zipcode), this.validator.isValid(zipcode, null));
    }
}
