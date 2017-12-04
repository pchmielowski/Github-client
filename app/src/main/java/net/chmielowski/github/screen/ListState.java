package net.chmielowski.github.screen;

import java.util.List;

import lombok.ToString;

@ToString
class ListState {
    final List<RepositoryViewModel> results;
    final boolean loading;

    ListState(final List<RepositoryViewModel> results, final boolean loading) {this.results = results;
        this.loading = loading;
    }
}
