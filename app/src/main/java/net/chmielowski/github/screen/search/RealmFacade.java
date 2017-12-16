package net.chmielowski.github.screen.search;

import net.chmielowski.github.data.Persistence;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.inject.Inject;

import io.realm.Realm;

public final class RealmFacade implements Persistence {
    @Inject
    RealmFacade() {
    }

    // TODO: inject factory
    private final Supplier<Realm> factory = Realm::getDefaultInstance;

    @Override
    public void executeInTransaction(final Consumer<Realm> consumer) {
        try (final Realm realm = factory.get()) {
            realm.executeTransaction(consumer::accept);
        }
    }

    @Override
    public void execute(final Consumer<Realm> consumer) {
        try (final Realm realm = factory.get()) {
            consumer.accept(realm);
        }
    }

    @Override
    public <T> T get(final Function<Realm, T> function) {
        try (final Realm realm = factory.get()) {
            return function.apply(realm);
        }
    }
}
