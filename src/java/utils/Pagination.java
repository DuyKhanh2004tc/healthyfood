/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class Pagination {

    public static <T> List<T> paginate(List<T> fullList, int page, int pageSize) {
        int totalItems = fullList.size();
        int fromIndex = Math.max(0, (page - 1) * pageSize);
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        if (fromIndex >= totalItems || fromIndex > toIndex) {
            return new ArrayList<>();
        }
        return fullList.subList(fromIndex, toIndex);
    }
}
