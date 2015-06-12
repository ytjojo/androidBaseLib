package com.kerkr.edu.utill;

import java.util.Collection;

public class CollectionUtils {
    public static <T> boolean isValid(Collection<T> c) {
        if (c == null || c.size() == 0) {
            return false;
        }
        return true;
    }
}
