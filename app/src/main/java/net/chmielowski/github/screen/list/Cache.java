package net.chmielowski.github.screen.list;

import net.chmielowski.github.Repositories;
import net.chmielowski.github.screen.fav.RealmRepo;

import javax.inject.Inject;

public class Cache {
    private RealmFacade realmFacade;

    @Inject
    Cache(RealmFacade realmFacade) {
        this.realmFacade = realmFacade;
    }

    Repositories.Item store(Repositories.Item repo) {
        // TODO: move to another class
        realmFacade.executeInTransaction(realm -> {
            final RealmRepo realmRepo = new RealmRepo();
            realmRepo.name = repo.fullName;
            realmRepo.id = repo.id;
            // TODO: what if favorite was set before
            realm.copyToRealm(realmRepo);
        });
        return repo;
    }

    public void like(long id) {
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
