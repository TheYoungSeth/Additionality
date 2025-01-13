package com.theyoungseth.mod.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MathForDummies {
    public static <T> List<T> convertIteratorToList(Iterator<T> iterator) {
        List<T> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }
}
