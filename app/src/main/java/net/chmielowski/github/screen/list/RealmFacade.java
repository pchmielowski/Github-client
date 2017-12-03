package net.chmielowski.github.screen.list;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;

final class RealmFacade {
    @Inject
    RealmFacade() {
    }

    // TODO: inject factory
    private final Supplier<Realm> factory = () -> {
        final RealmConfiguration cfg = Realm.getDefaultConfiguration();
        if (cfg == null) {
            throw new IllegalStateException("Realm not configured");
        }
        return Realm.getInstance(cfg);
    };

    void execute(final Consumer<Realm> consumer) {
        try (final Realm realm = factory.get()) {
            consumer.accept(realm);
        }
    }

    <T> T get(final Function<Realm, T> function) {
        try (final Realm realm = factory.get()) {
            return function.apply(realm);
        }
    }
}
