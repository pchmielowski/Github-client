package net.chmielowski.github.screen.list;

import java.util.Optional;

import javax.inject.Inject;

final class LastQueryCache {
    private final RealmFacade db;

    @Inject
    LastQueryCache(final RealmFacade realm) {
        this.db = realm;
    }

    void saveToRealm(String query) {
        db.execute(realm ->
                realm.executeTransaction(transaction ->
                        transaction.copyToRealmOrUpdate(RealmString.from(query))));
    }

    Optional<String> getFromRealm() {
        return db.get(realm ->
                Optional.ofNullable(realm
                        .where(RealmString.class)
                        .findFirst()
                ).map(it -> it.value));
    }
}
