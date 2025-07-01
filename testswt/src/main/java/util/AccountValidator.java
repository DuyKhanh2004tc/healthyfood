package util;

import java.util.Date;
import java.util.Set;

public class AccountValidator {

    public static String validateName(String name) {
        if (name == null || name.trim().isEmpty()) return "Name is required";
        if (!name.matches("^[a-zA-Z\\s'-]{2,50}$")) return "Invalid name format";
        return null;
    }

    public static String validateEmail(String email, boolean emailExists) {
        if (email == null || email.trim().isEmpty()) return "Email is required";
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) return "Invalid email format";
        if (emailExists) return "Email already exists";
        return null;
    }

    public static String validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) return "Password is required";
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$")) {
            return "Password too weak";
        }
        return null;
    }

    public static String validatePhone(String phone) {
        if (phone == null || phone.isEmpty()) return null;
        if (!phone.matches("^(\\+\\d{1,3})?\\d{10,12}$")) return "Invalid phone number";
        return null;
    }

    public static String validateGender(String gender) {
        if (gender == null || (!"0".equals(gender) && !"1".equals(gender))) return "Invalid gender";
        return null;
    }

    public static String validateAddress(String address) {
        if (address == null || address.isEmpty()) return null;
        if (!address.matches("^[a-zA-Z0-9\\s,.-]{5,100}$")) return "Invalid address";
        return null;
    }

    public static String validateDOB(Date dob) {
        if (dob == null) return "DOB is required";

        Date now = new Date();
        if (dob.after(now)) return "DOB cannot be in the future";

        long ageInMillis = now.getTime() - dob.getTime();
        long ageInYears = ageInMillis / (1000L * 60 * 60 * 24 * 365);

        if (ageInYears < 18) return "Must be at least 18 years old";
        return null;
    }

    public static String validateRoleId(String roleId, Set<Integer> existingRoleIds) {
        if (roleId == null || roleId.isBlank()) return "Role ID is missing";
        try {
            int id = Integer.parseInt(roleId);
            if (!existingRoleIds.contains(id)) return "Role not found";
        } catch (NumberFormatException e) {
            return "Invalid Role ID format";
        }
        return null;
    }
}
