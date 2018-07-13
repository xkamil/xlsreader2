package org.model.filter;

import org.model.SuperCell;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CellFilter implements Filter<SuperCell> {

    private List<FilterSource> filterSources = new ArrayList<>();

    @Override
    public void registerFilterSource(final FilterSource source) {
        filterSources.add(source);
    }

    @Override
    public List<SuperCell> filter(List<SuperCell> cells) {
        return cells.parallelStream()
                .filter(cell -> filterSources.stream()
                        .map(FilterSource::getFilter)
                        .filter(filter -> filter.test(cell)).count() == filterSources.size())
                .collect(Collectors.toList());
    }


}
