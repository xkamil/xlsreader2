package org.model.filter;

import java.util.List;

public interface Filter<T> {

    void registerFilterSource(FilterSource source);

    List<T> filter(List<T> items);
}
