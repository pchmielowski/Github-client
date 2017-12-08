package net.chmielowski.github.screen;

import java.util.Collection;

import io.reactivex.Observable;

public interface QueryHistory {
    void searched(String query);

    Observable<Collection<String>> observe();
}
