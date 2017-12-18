package net.chmielowski.github.data;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Credentials;

@Singleton
public final class User {
    private final Persistence persistence;

    @Inject
    User(final Persistence persistence) {
        this.persistence = persistence;
    }

    public void logout() {
        persistence.executeInTransaction(realm ->
                realm.where(RealmUser.class)
                        .findAll()
                        .deleteAllFromRealm());
    }

    public void login(final String user, final String password) {
        persistence.executeInTransaction(realm ->
                realm.copyToRealmOrUpdate(new RealmUser(Credentials.basic(user, password))));
    }

    public Optional<String> token() {
        return persistence.get(realm ->
                Optional.ofNullable(realm.where(RealmUser.class)
                        .findFirst())
                        .map(it -> it.token));
    }
}
