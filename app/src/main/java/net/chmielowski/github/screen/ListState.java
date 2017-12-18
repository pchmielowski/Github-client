package net.chmielowski.github.screen;

import android.support.annotation.NonNull;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import static java.util.Collections.emptyList;

@ToString
@EqualsAndHashCode
public final class ListState {
    public final List<RepositoryViewModel> results;
    public final boolean loading;

    private ListState(final List<RepositoryViewModel> results, final boolean loading) {
        this.results = results;
        this.loading = loading;
    }

    @NonNull
    static ListState loaded(final List<RepositoryViewModel> data) {
        return new ListState(data, false);
    }

    @NonNull
    static ListState empty() {
        return loaded(emptyList());
    }

    @NonNull
    static ListState loading() {
        return new ListState(emptyList(), true);
    }
}
