package org.model.filter;

import org.model.SuperCell;

import java.util.function.Predicate;

public interface FilterSource {
    Predicate<SuperCell> getFilter();
}
