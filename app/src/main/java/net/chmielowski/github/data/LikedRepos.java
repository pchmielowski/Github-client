package net.chmielowski.github.data;

import javax.inject.Inject;

public final class LikedRepos {
    private final Persistence persistence;

    @Inject
    LikedRepos(final Persistence persistence) {
        this.persistence = persistence;
    }

    public void like(final Repositories.Item name) {
        persistence.execute(realm ->
                realm.executeTransaction(transaction -> {
                    transaction.copyToRealmOrUpdate(new RealmRepo(name));
                }));
    }

    @SuppressWarnings("ConstantConditions")
    public boolean isLiked(final String repo) {
        return persistence.get(realm -> {
            final long count = realm.where(RealmRepo.class)
                    .equalTo(RealmRepo.ID, repo)
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
