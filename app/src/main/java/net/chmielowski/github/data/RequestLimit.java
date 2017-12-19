package net.chmielowski.github.data;

import net.chmielowski.github.utils.ValueIgnored;

import io.reactivex.Observable;

public interface RequestLimit {
    void onReached();

    Observable<ValueIgnored> observe();
}
