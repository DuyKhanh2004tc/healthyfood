// Gợi ý class tiện ích mới
package util;

import model.User;
import java.util.Comparator;

public class UserSortUtil {
    public static Comparator<User> getComparator(String sortBy) {
        switch (sortBy) {
            case "id":
                return Comparator.comparingInt(User::getId);
            case "name":
                return Comparator.comparing(User::getName, String.CASE_INSENSITIVE_ORDER);
            case "email":
                return Comparator.comparing(User::getEmail, String.CASE_INSENSITIVE_ORDER);
            default:
                return null;
        }
    }
}
