package com.roxwin.imagefilter.filters.util;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by PhucTV on 4/25/16.
 */
public class FilterList {
    public List<String> names = new LinkedList<String>();
    public List<FilterType> filters = new LinkedList<FilterType>();

    public void addFilter(final String name, final FilterType filter) {
        names.add(name);
        filters.add(filter);
    }
}