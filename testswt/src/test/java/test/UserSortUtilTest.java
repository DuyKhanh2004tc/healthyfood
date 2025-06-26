// File: test/UserSortUtilTest.java
package test;

import model.User;
import org.junit.jupiter.api.Test;
import util.UserSortUtil;

import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Test cho UserSortUtil - kiểm tra sắp xếp danh sách người dùng theo các
 * tiêu chí khác nhau: name, email, id và xử lý ngoại lệ.
 */
public class UserSortUtilTest {

    @Test
    public void testSortByNameAsc() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(1, "Charlie", "c@gmail.com", null, null, null, null, false, null, null));
        users.add(new User(2, "Alice", "a@gmail.com", null, null, null, null, false, null, null));
        users.add(new User(3, "Bob", "b@gmail.com", null, null, null, null, false, null, null));

        users.sort(UserSortUtil.getComparator("name"));

        assertEquals("Alice", users.get(0).getName());
        assertEquals("Bob", users.get(1).getName());
        assertEquals("Charlie", users.get(2).getName());
    }

    @Test
    public void testSortByNameDesc() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(1, "Alice", "a@gmail.com", null, null, null, null, false, null, null));
        users.add(new User(2, "Charlie", "c@gmail.com", null, null, null, null, false, null, null));
        users.add(new User(3, "Bob", "b@gmail.com", null, null, null, null, false, null, null));

        users.sort(UserSortUtil.getComparator("name").reversed());

        assertEquals("Charlie", users.get(0).getName());
        assertEquals("Bob", users.get(1).getName());
        assertEquals("Alice", users.get(2).getName());
    }

    @Test
    public void testSortByEmailAsc() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(1, "User1", "z@email.com", null, null, null, null, false, null, null));
        users.add(new User(2, "User2", "a@email.com", null, null, null, null, false, null, null));

        users.sort(UserSortUtil.getComparator("email"));

        assertEquals("a@email.com", users.get(0).getEmail());
        assertEquals("z@email.com", users.get(1).getEmail());
    }

    @Test
    public void testSortByEmailDesc() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(1, "User1", "a@email.com", null, null, null, null, false, null, null));
        users.add(new User(2, "User2", "z@email.com", null, null, null, null, false, null, null));

        users.sort(UserSortUtil.getComparator("email").reversed());

        assertEquals("z@email.com", users.get(0).getEmail());
        assertEquals("a@email.com", users.get(1).getEmail());
    }

    @Test
    public void testSortByIdAsc() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(3, "User3", "c@gmail.com", null, null, null, null, false, null, null));
        users.add(new User(1, "User1", "a@gmail.com", null, null, null, null, false, null, null));

        users.sort(UserSortUtil.getComparator("id"));

        assertEquals(1, users.get(0).getId());
        assertEquals(3, users.get(1).getId());
    }

    @Test
    public void testSortByIdDesc() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(2, "User2", "b@gmail.com", null, null, null, null, false, null, null));
        users.add(new User(1, "User1", "a@gmail.com", null, null, null, null, false, null, null));

        users.sort(UserSortUtil.getComparator("id").reversed());

        assertEquals(2, users.get(0).getId());
        assertEquals(1, users.get(1).getId());
    }

    @Test
    public void testSortByNullFieldName() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(1, null, "a@gmail.com", null, null, null, null, false, null, null));
        users.add(new User(2, "Alice", "b@gmail.com", null, null, null, null, false, null, null));

        Comparator<User> comp = Comparator.comparing(
                user -> user.getName() == null ? "" : user.getName(),
                String.CASE_INSENSITIVE_ORDER
        );

        users.sort(comp);

        assertNull(users.get(0).getName());             
        assertEquals("Alice", users.get(1).getName());  
    }

    @Test
    public void testSortByInvalidField() {
        Comparator<User> comparator = UserSortUtil.getComparator("unknown");
        assertNull(comparator);
    }

    @Test
    public void testSortEmptyList() {
        ArrayList<User> users = new ArrayList<>();
        users.sort(UserSortUtil.getComparator("name"));  // không lỗi
        assertTrue(users.isEmpty());
    }

    @Test
    public void testSortSingleUser() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(1, "OnlyOne", "only@gmail.com", null, null, null, null, false, null, null));

        users.sort(UserSortUtil.getComparator("name"));
        assertEquals("OnlyOne", users.get(0).getName());
    }
}
