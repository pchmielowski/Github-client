package net.chmielowski.github.data;

import net.chmielowski.github.screen.RepositoryViewModel;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public final class FavouriteRepos implements IFavouriteRepos{

    private final Persistence db;

    @Inject
    FavouriteRepos(final Persistence db) {
        this.db = db;
    }

    @Override
    public List<RepositoryViewModel> all() {
        return db.get(realm ->
                realm.where(RealmRepo.class)
                        .findAll()
                        .stream()
                        .map(RepositoryViewModel::new)
                        .collect(Collectors.toList()));
    }

}
