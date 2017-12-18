package net.chmielowski.github.data;

import net.chmielowski.github.screen.RepositoryViewModel;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Single;
import io.realm.RealmResults;

public final class PersistentFavourites implements Favourites {

    private final Persistence persistence;

    @Inject
    PersistentFavourites(final Persistence db) {
        this.persistence = db;
    }

    @Override
    public List<RepositoryViewModel> all() {
        return persistence.get(realm ->
                realm.where(RealmFavouriteRepository.class)
                        .findAll()
                        .stream()
                        .map(RepositoryViewModel::new)
                        .collect(Collectors.toList()));
    }

    @Override
    public Single<Boolean> toggle(final Repositories.Item repo) {
        final Boolean like = persistence.get(realm -> {
            final RealmResults<RealmFavouriteRepository> found = realm.where(RealmFavouriteRepository.class)
                    .equalTo(RealmFavouriteRepository.ID, repo.fullName)
                    .findAll();
            if (found.isEmpty()) {
                realm.executeTransaction(transaction ->
                        transaction.copyToRealmOrUpdate(new RealmFavouriteRepository(repo)));
                return true;
            }
            realm.executeTransaction(transaction ->
                    found.deleteAllFromRealm());
            return false;
        });
        return Single.just(like);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean isLiked(final String repo) {
        return persistence.get(realm -> {
            final long count = realm.where(RealmFavouriteRepository.class)
                    .equalTo(RealmFavouriteRepository.ID, repo)
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
