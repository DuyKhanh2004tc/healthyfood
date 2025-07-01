package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import util.AccountValidator;

public class AccountValidatorTest {

    @Test void TC01_validAllFields() {
        assertNull(AccountValidator.validateName("John Smith"));
        assertNull(AccountValidator.validateEmail("john@example.com", false));
        assertNull(AccountValidator.validatePassword("John@1234"));
    }

    @Test void TC02_emptyName() {
        assertEquals("Name is required", AccountValidator.validateName(""));
    }

    @Test void TC03_nameTooShort() {
        assertEquals("Invalid name format", AccountValidator.validateName("A"));
    }

    @Test void TC04_nameTooLong() {
        assertEquals("Invalid name format", AccountValidator.validateName("A".repeat(60)));
    }

    @Test void TC05_nameInvalidChars() {
        assertEquals("Invalid name format", AccountValidator.validateName("@123!"));
    }

    @Test void TC06_emptyEmail() {
        assertEquals("Email is required", AccountValidator.validateEmail("", false));
    }

    @Test void TC07_invalidEmailFormat() {
        assertEquals("Invalid email format", AccountValidator.validateEmail("abc@", false));
    }

    @Test void TC08_duplicateEmail() {
        assertEquals("Email already exists", AccountValidator.validateEmail("test@abc.com", true));
    }

    @Test void TC09_emptyPassword() {
        assertEquals("Password is required", AccountValidator.validatePassword(""));
    }

    @Test void TC10_passwordTooShort() {
        assertEquals("Password too weak", AccountValidator.validatePassword("Ab1@"));
    }

    @Test void TC11_passwordMissingSpecialChar() {
        assertEquals("Password too weak", AccountValidator.validatePassword("Abc12345"));
    }

    @Test void TC12_passwordMissingUppercase() {
        assertEquals("Password too weak", AccountValidator.validatePassword("abc@1234"));
    }

    @Test void TC13_emptyPhone() {
        assertNull(AccountValidator.validatePhone(""));
    }

    @Test void TC14_invalidPhoneFormat() {
        assertEquals("Invalid phone number", AccountValidator.validatePhone("abc123"));
    }

    @Test void TC15_validPhone() {
        assertNull(AccountValidator.validatePhone("+84901234567"));
    }

    @Test void TC16_validGenderMale() {
        assertNull(AccountValidator.validateGender("1"));
    }

    @Test void TC17_validGenderFemale() {
        assertNull(AccountValidator.validateGender("0"));
    }

    @Test void TC18_invalidGender() {
        assertEquals("Invalid gender", AccountValidator.validateGender("2"));
    }

    @Test void TC19_emptyAddress() {
        assertNull(AccountValidator.validateAddress(""));
    }

    @Test void TC20_addressTooShort() {
        assertEquals("Invalid address", AccountValidator.validateAddress("abc"));
    }

    @Test void TC21_addressInvalidChars() {
        assertEquals("Invalid address", AccountValidator.validateAddress("###!!!"));
    }

    @Test void TC22_validAddress() {
        assertNull(AccountValidator.validateAddress("123 Main St., District 1"));
    }

    @Test void TC23_nullName() {
        assertEquals("Name is required", AccountValidator.validateName(null));
    }

    @Test void TC24_nullEmail() {
        assertEquals("Email is required", AccountValidator.validateEmail(null, false));
    }

    @Test void TC25_nullPassword() {
        assertEquals("Password is required", AccountValidator.validatePassword(null));
    }

    @Test void TC26_nullPhone() {
        assertNull(AccountValidator.validatePhone(null));
    }

    @Test void TC27_nullGender() {
        assertEquals("Invalid gender", AccountValidator.validateGender(null));
    }

    @Test void TC28_nullAddress() {
        assertNull(AccountValidator.validateAddress(null));
    }

    @Test void TC29_multipleErrors() {
        assertEquals("Name is required", AccountValidator.validateName(""));
        assertEquals("Email is required", AccountValidator.validateEmail("", false));
        assertEquals("Password is required", AccountValidator.validatePassword(""));
    }

    @Test void TC30_validScenarioAgain() {
        assertNull(AccountValidator.validateName("Alice Johnson"));
        assertNull(AccountValidator.validateEmail("alice@mail.com", false));
        assertNull(AccountValidator.validatePassword("Alice@123"));
    }
}
