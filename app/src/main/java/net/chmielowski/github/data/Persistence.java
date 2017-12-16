package net.chmielowski.github.data;

import java.util.function.Consumer;
import java.util.function.Function;

import io.realm.Realm;

public interface Persistence {
    void executeInTransaction(Consumer<Realm> consumer);

    void execute(Consumer<Realm> consumer);

    <T> T get(Function<Realm, T> function);
}
