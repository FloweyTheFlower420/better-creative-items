package com.floweytf.bettercreativeitems.utils;

import java.util.*;
import java.util.function.Function;

public class SearchedArrayList<T> extends AbstractList<T> {
    private String str = "";
    private final ArrayList<Integer> locations = new ArrayList<>();
    private final ArrayList<T> source;
    private final Function<T, String> searcher;

    public SearchedArrayList(ArrayList<T> source, Function<T, String> searcher) {
        this.source = source;
        this.searcher = searcher;
    }

    @Override
    public T get(int index) {
        return source.get(locations.get(index));
    }

    @Override
    public int size() {
        return locations.size();
    }

    public void setSearchStr(String str) {
        this.str = str;
        locations.clear();
        for(int i = 0; i < source.size(); i++) {

        }
    }
}