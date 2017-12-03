package net.chmielowski.github.screen.details;

import android.databinding.ObservableField;

import net.chmielowski.github.ReposRepository;
import net.chmielowski.github.RepositoryViewModel;
import net.chmielowski.github.screen.fav.RealmRepo;
import net.chmielowski.github.screen.list.RealmFacade;

import java.util.function.Consumer;

import javax.inject.Inject;

import io.realm.Realm;

public final class DetailsViewModel {
    public final ObservableField<String> name = new ObservableField<>();

    private final ReposRepository service;
    private final RealmFacade realmFacade;
    private long id;

    @Inject
    DetailsViewModel(final ReposRepository service, final RealmFacade realmFacade) {
        this.service = service;
        this.realmFacade = realmFacade;
    }

    void setRepo(final long repo) {
        this.id = repo;
        service.item(repo)
                .map(RepositoryViewModel::new)
                .subscribe(item -> name.set(item.name.toString()));
    }

    public void addToFavs() {
        realmFacade.execute(realm ->
                realm.executeTransaction(transaction -> {
                    final RealmRepo repo = transaction.where(RealmRepo.class)
                            .equalTo(RealmRepo.ID, id)
                            .findFirst();
                    repo.favourite = true;
                    transaction.copyToRealmOrUpdate(repo);
                }));
    }
}
