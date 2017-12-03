package net.chmielowski.github.screen.list;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.inject.Inject;

import io.realm.Realm;

public final class RealmFacade {
    @Inject
    RealmFacade() {
    }

    // TODO: inject factory
    private final Supplier<Realm> factory = Realm::getDefaultInstance;

    void execute(final Consumer<Realm> consumer) {
        try (final Realm realm = factory.get()) {
            consumer.accept(realm);
        }
    }

    public <T> T get(final Function<Realm, T> function) {
        try (final Realm realm = factory.get()) {
            return function.apply(realm);
        }
    }
}
