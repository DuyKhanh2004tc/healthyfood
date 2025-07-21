package util;

import java.util.Date;
import java.util.Set;
import java.util.Calendar;

public class AccountValidator1 {

    public static String validateName(String name) {
        if (name == null || name.trim().isEmpty()) return "Name is required";
        if (!name.matches("^[a-zA-Z\\s'-]{2,50}$")) return "Name must be 2–50 characters and contain only letters, spaces, hyphens, or apostrophes";
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
            return "Password must be at least 8 characters, with 1 uppercase, 1 lowercase, 1 digit, and 1 special character";
        }
        return null;
    }

    public static String validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) return null;
        if (!phone.matches("^((\\+84)|0)\\d{9}$")) return "Phone must be a valid Vietnamese phone number starting with +84 or 0 and 10 digits long";
        return null;
    }

    public static String validateGender(String gender) {
        if (gender == null || (!"0".equals(gender) && !"1".equals(gender))) return "Invalid gender selection";
        return null;
    }

    public static String validateAddress(String address) {
        if (address == null || address.trim().isEmpty()) return null;
        if (!address.matches("^[a-zA-Z0-9\\s,.-]{5,100}$")) return "Address must be 5–100 characters and contain only letters, numbers, spaces, commas, periods, or hyphens";
        return null;
    }

    public static String validateDOB(Date dob) {
        if (dob == null) return "Date of Birth is required";

        Date now = new Date();
        if (dob.after(now)) return "Date of Birth cannot be in the future";

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -18);
        if (dob.after(cal.getTime())) return "User must be at least 18 years old";

        return null;
    }

    public static String validateRoleId(String roleId, Set<Integer> existingRoleIds) {
        if (roleId == null || roleId.trim().isEmpty()) return "Role is required";
        try {
            int id = Integer.parseInt(roleId);
            if (!existingRoleIds.contains(id)) return "Invalid Role ID: Role does not exist";
        } catch (NumberFormatException e) {
            return "Invalid Role ID format";
        }
        return null;
    }
}
