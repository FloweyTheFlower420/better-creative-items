package com.floweytf.bettercreativeitems.utils;

import java.util.*;
import java.util.function.Function;

public class SearchedArrayList<T> extends AbstractList<T> {
    private String str;
    private List<Integer> locations = new ArrayList<>();
    private final ArrayList<T> source;
    private final Function<T, String> searcher;

    public SearchedArrayList(ArrayList<T> source, Function<T, String> searcher) {
        this.source = source;
        this.searcher = searcher;
        this.str = "";
    }

    @Override
    public T get(int index) {
        if (str.length() == 0)
            return source.get(index);
        return source.get(locations.get(index));
    }

    @Override
    public int size() {
        if (str.length() == 0)
            return source.size();
        return locations.size();
    }

    public void setSearchStr(String str) {
        this.str = str;
        str = str.toLowerCase();
        locations.clear();
        for (int i = 0; i < source.size(); i++) {
            String name = searcher.apply(source.get(i));
            if (name.contains(str))
                locations.add(i);
        }
    }

    public void appendSearchChar(char ch) {
        str += ch;
        str = str.toLowerCase();
        // redo search
        List<Integer> other = new ArrayList<>();
        for (int i = 0; i < locations.size(); i++) {
            String name = searcher.apply(source.get(locations.get(i))).toLowerCase();
            if (name.contains(str))
                other.add(locations.get(i));
        }
        locations = other;
    }

    public void reset() {
        str = "";
    }

    public String getSearchStr() {
        return str;
    }
}