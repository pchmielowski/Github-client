package net.chmielowski.github.data;

import net.chmielowski.github.screen.RepositoryViewModel;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public final class PersistentFavourites implements Favourites {

    private final Persistence persistence;

    @Inject
    PersistentFavourites(final Persistence db) {
        this.persistence = db;
    }

    @Override
    public List<RepositoryViewModel> all() {
        return persistence.get(realm ->
                realm.where(RealmRepo.class)
                        .findAll()
                        .stream()
                        .map(RepositoryViewModel::new)
                        .collect(Collectors.toList()));
    }

    @Override
    public void like(final Repositories.Item name) {
        persistence.execute(realm ->
                realm.executeTransaction(transaction -> {
                    transaction.copyToRealmOrUpdate(new RealmRepo(name));
                }));
    }

    @Override
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
