package net.chmielowski.github.utils;

import io.reactivex.Observable;

public final class Assertions {
    private Assertions() {
        throw new AssertionError("Don't use this ctor");
    }

    public static <T> Observable<T> neverCompletes(final Observable<T> upstream) {
        return upstream.doOnComplete(() -> {
            throw new IllegalStateException("Stream has completed");
        });
    }
}
