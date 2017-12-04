package net.chmielowski.github.screen;

import java.util.List;

import lombok.ToString;

@ToString
public class ListState {
    public final List<RepositoryViewModel> results;
    public final boolean loading;

    public ListState(final List<RepositoryViewModel> results, final boolean loading) {
        this.results = results;
        this.loading = loading;
    }
}
