package net.chmielowski.github.screen;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class ListState {
    public final List<RepositoryViewModel> results;
    public final boolean loading;

    public ListState(final List<RepositoryViewModel> results, final boolean loading) {
        this.results = results;
        this.loading = loading;
    }

    @NonNull
    public static ListState loaded(final List<RepositoryViewModel> data) {
        return new ListState(data, false);
    }

    @NonNull
    public static ListState loading() {
        return new ListState(Collections.emptyList(), true);
    }

    @NonNull
    public static ListState initial() {
        return new ListState(Collections.emptyList(), false);
    }
}
