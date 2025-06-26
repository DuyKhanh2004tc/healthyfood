package test;

import model.User;
import org.junit.jupiter.api.Test;
import util.UserSearchUtil;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserSearchUtilTest {

    @Test
    public void testFilterUsers_ByNameMatch() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(1, "Alice", "alice@gmail.com", null, null, null, null, false, null, null));
        users.add(new User(2, "Bob", "bob@gmail.com", null, null, null, null, false, null, null));

        ArrayList<User> result = UserSearchUtil.filterUsers(users, "Ali");

        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getName());
    }

    @Test
    public void testFilterUsers_ByEmailMatch() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(1, "Alice", "alice@gmail.com", null, null, null, null, false, null, null));
        users.add(new User(2, "Bob", "bob@gmail.com", null, null, null, null, false, null, null));

        ArrayList<User> result = UserSearchUtil.filterUsers(users, "bob");

        assertEquals(1, result.size());
        assertEquals("Bob", result.get(0).getName());
    }

    @Test
    public void testFilterUsers_EmptyKeyword() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(1, "Alice", "alice@gmail.com", null, null, null, null, false, null, null));

        ArrayList<User> result = UserSearchUtil.filterUsers(users, "");

        assertEquals(1, result.size()); // No filtering
    }

    @Test
    public void testFilterUsers_NoMatch() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(1, "Alice", "alice@gmail.com", null, null, null, null, false, null, null));

        ArrayList<User> result = UserSearchUtil.filterUsers(users, "zzz");

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFilterUsers_NullNameOrEmail() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(1, null, "abc@gmail.com", null, null, null, null, false, null, null));
        users.add(new User(2, "John", null, null, null, null, null, true, null, null));
        users.add(new User(3, null, null, null, null, null, null, false, null, null));
        ArrayList<User> result = UserSearchUtil.filterUsers(users, "abc");
        assertEquals(1, result.size());
        assertEquals("abc@gmail.com", result.get(0).getEmail());
    }

    @Test
    public void testFilterUsers_KeywordWithSpaces() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(1, "Alice", "alice@gmail.com", null, null, null, null, false, null, null));

        ArrayList<User> result = UserSearchUtil.filterUsers(users, "  ALICE  ");
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getName());
    }

    @Test
    public void testFilterUsers_EmptyUserList() {
        ArrayList<User> users = new ArrayList<>();
        ArrayList<User> result = UserSearchUtil.filterUsers(users, "alice");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFilterUsers_NullKeyword() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(1, "Alice", "alice@gmail.com", null, null, null, null, false, null, null));
        ArrayList<User> result = UserSearchUtil.filterUsers(users, null);
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getName());
    }

    @Test
    public void testFilterUsers_CaseInsensitiveNameMatches() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(1, "Alice", "a@gmail.com", null, null, null, null, false, null, null));
        users.add(new User(2, "ALICE", "b@gmail.com", null, null, null, null, false, null, null));
        users.add(new User(3, "aLiCe", "c@gmail.com", null, null, null, null, false, null, null));

        ArrayList<User> result = UserSearchUtil.filterUsers(users, "alice");
        assertEquals(3, result.size());
    }

    @Test
    public void testFilterUsers_NameAndEmailMatch() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(1, "Nam", "nam@gmail.com", null, null, null, null, false, null, null));

        ArrayList<User> result = UserSearchUtil.filterUsers(users, "nam");
        assertEquals(1, result.size());
    }
}
