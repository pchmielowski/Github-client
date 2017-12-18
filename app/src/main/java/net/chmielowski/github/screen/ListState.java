package net.chmielowski.github.screen;

import android.support.annotation.NonNull;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import static java.util.Collections.emptyList;

@ToString
@EqualsAndHashCode
public final class ListState {
    final List<RepositoryViewModel> results;
    final boolean loading;

    private ListState(final List<RepositoryViewModel> results, final boolean loading) {
        this.results = results;
        this.loading = loading;
    }

    @NonNull
    public static ListState loaded(final List<RepositoryViewModel> data) {
        return new ListState(data, false);
    }

    @NonNull
    public static ListState empty() {
        return loaded(emptyList());
    }

    @NonNull
    public static ListState loading() {
        return new ListState(emptyList(), true);
    }
}
