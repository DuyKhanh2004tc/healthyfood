package test;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import util.AccountValidator1;

public class AccountValidator1Test {

    @Test void TC01_validAllFields() {
        assertNull(AccountValidator1.validateName("John Smith"));
        assertNull(AccountValidator1.validateEmail("john@example.com", false));
        assertNull(AccountValidator1.validatePassword("John@1234"));
    }

    @Test void TC02_emptyName() {
        assertEquals("Name is required", AccountValidator1.validateName(""));
    }

    @Test void TC03_nameTooShort() {
        assertEquals("Name must be 2–50 characters and contain only letters, spaces, hyphens, or apostrophes", AccountValidator1.validateName("A"));
    }

    @Test void TC04_nameTooLong() {
        assertEquals("Name must be 2–50 characters and contain only letters, spaces, hyphens, or apostrophes", AccountValidator1.validateName("A".repeat(60)));
    }

    @Test void TC05_nameInvalidChars() {
        assertEquals("Name must be 2–50 characters and contain only letters, spaces, hyphens, or apostrophes", AccountValidator1.validateName("@123!"));
    }

    @Test void TC06_emptyEmail() {
        assertEquals("Email is required", AccountValidator1.validateEmail("", false));
    }

    @Test void TC07_invalidEmailFormat() {
        assertEquals("Invalid email format", AccountValidator1.validateEmail("abc@", false));
    }

    @Test void TC08_duplicateEmail() {
        assertEquals("Email already exists", AccountValidator1.validateEmail("test@abc.com", true));
    }

    @Test void TC09_emptyPassword() {
        assertEquals("Password is required", AccountValidator1.validatePassword(""));
    }

    @Test void TC10_passwordTooShort() {
        assertEquals("Password must be at least 8 characters, with 1 uppercase, 1 lowercase, 1 digit, and 1 special character", AccountValidator1.validatePassword("Ab1@"));
    }

    @Test void TC11_passwordMissingSpecialChar() {
        assertEquals("Password must be at least 8 characters, with 1 uppercase, 1 lowercase, 1 digit, and 1 special character", AccountValidator1.validatePassword("Abc12345"));
    }

    @Test void TC12_passwordMissingUppercase() {
        assertEquals("Password must be at least 8 characters, with 1 uppercase, 1 lowercase, 1 digit, and 1 special character", AccountValidator1.validatePassword("abc@1234"));
    }

    @Test void TC13_emptyPhone() {
        assertNull(AccountValidator1.validatePhone(""));
    }

    @Test void TC14_invalidPhoneFormat() {
        assertEquals("Phone must be a valid Vietnamese phone number starting with +84 or 0 and 10 digits long", AccountValidator1.validatePhone("abc123"));
    }

    @Test void TC15_validPhone() {
        assertNull(AccountValidator1.validatePhone("+84901234567"));
    }

    @Test void TC16_validGenderMale() {
        assertNull(AccountValidator1.validateGender("1"));
    }

    @Test void TC17_validGenderFemale() {
        assertNull(AccountValidator1.validateGender("0"));
    }

    @Test void TC18_invalidGender() {
        assertEquals("Invalid gender selection", AccountValidator1.validateGender("2"));
    }

    @Test void TC19_nullGender() {
        assertEquals("Invalid gender selection", AccountValidator1.validateGender(null));
    }

    @Test void TC20_validAddress() {
        assertNull(AccountValidator1.validateAddress("123 Main St., District 1"));
    }

    @Test void TC21_addressTooShort() {
        assertEquals("Address must be 5–100 characters and contain only letters, numbers, spaces, commas, periods, or hyphens", AccountValidator1.validateAddress("abc"));
    }

    @Test void TC22_invalidAddressChars() {
        assertEquals("Address must be 5–100 characters and contain only letters, numbers, spaces, commas, periods, or hyphens", AccountValidator1.validateAddress("###!!!"));
    }

    @Test void TC23_validDOB() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -20);
        Date dob = cal.getTime();
        assertNull(AccountValidator1.validateDOB(dob));
    }

    @Test void TC24_DOBInFuture() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        Date dob = cal.getTime();
        assertEquals("Date of Birth cannot be in the future", AccountValidator1.validateDOB(dob));
    }

    @Test void TC25_DOBUnder18() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -17);
        Date dob = cal.getTime();
        assertEquals("User must be at least 18 years old", AccountValidator1.validateDOB(dob));
    }

    @Test void TC26_nullDOB() {
        assertEquals("Date of Birth is required", AccountValidator1.validateDOB(null));
    }

    @Test void TC27_validRoleId() {
        Set<Integer> roles = Set.of(1, 2, 3);
        assertNull(AccountValidator1.validateRoleId("2", roles));
    }

    @Test void TC28_invalidRoleIdFormat() {
        Set<Integer> roles = Set.of(1, 2, 3);
        assertEquals("Invalid Role ID format", AccountValidator1.validateRoleId("abc", roles));
    }

    @Test void TC29_roleIdNotFound() {
        Set<Integer> roles = Set.of(1, 2, 3);
        assertEquals("Invalid Role ID: Role does not exist", AccountValidator1.validateRoleId("5", roles));
    }

    @Test void TC30_roleIdEmpty() {
        Set<Integer> roles = Set.of(1, 2, 3);
        assertEquals("Role is required", AccountValidator1.validateRoleId("", roles));
    }
}
