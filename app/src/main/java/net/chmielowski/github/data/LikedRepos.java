package net.chmielowski.github.data;

import net.chmielowski.github.screen.search.RealmFacade;

import javax.annotation.Nullable;
import javax.inject.Inject;

import io.realm.Realm;

public final class LikedRepos {
    private RealmFacade realmFacade;

    @Inject
    LikedRepos(RealmFacade realmFacade) {
        this.realmFacade = realmFacade;
    }

    public void like(final String name) {
        realmFacade.execute(realm ->
                realm.executeTransaction(transaction -> {
                    transaction.copyToRealmOrUpdate(new RealmRepo(name));
                }));
    }

    @SuppressWarnings("ConstantConditions")
    public boolean isLiked(final String repo) {
        return realmFacade.get(realm -> {
            final long count = realm.where(RealmRepo.class)
                    .equalTo(RealmRepo.NAME, repo)
                    .count();
            if (count > 1) {
                throw new IllegalStateException(
                        "More than one instances of RealmRepo with name " + repo
                );
            }
            return count == 1;
        });
    }
}
