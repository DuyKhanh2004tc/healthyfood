/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.util.ArrayList;
import model.User;

/**
 *
 * @author Cuong
 */
public class UserSearchUtil {

    public static ArrayList<User> filterUsers(ArrayList<User> uList, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return uList;
        }

        final String searchKey = keyword.trim().toLowerCase();  // <- FIX HERE
        ArrayList<User> filteredList = new ArrayList<>();

        for (User user : uList) {
            String name = user.getName();
            String email = user.getEmail();

            boolean matchName = name != null && name.toLowerCase().contains(searchKey);
            boolean matchEmail = email != null && email.toLowerCase().contains(searchKey);

            if (matchName || matchEmail) {
                filteredList.add(user);
            }
        }

        return filteredList;
    }

}
