package net.chmielowski.github.data;

import net.chmielowski.github.screen.search.RealmFacade;

import javax.annotation.Nullable;
import javax.inject.Inject;

import io.realm.Realm;

public class Cache {
    private RealmFacade realmFacade;

    @Inject
    Cache(RealmFacade realmFacade) {
        this.realmFacade = realmFacade;
    }

    public Repositories.Item store(Repositories.Item repo) {
        // TODO: move to another class
        realmFacade.executeInTransaction(realm -> {
            final RealmRepo realmRepo = new RealmRepo();
            realmRepo.name = repo.fullName;
            realmRepo.id = repo.id;
            // TODO: what if favorite was set before
            realm.copyToRealmOrUpdate(realmRepo);
        });
        return repo;
    }

    public void like(long id) {
        realmFacade.execute(realm ->
                realm.executeTransaction(transaction -> {
                    final RealmRepo repo = first(id, transaction);
                    assert repo != null;
                    repo.favourite = true;
                    transaction.copyToRealmOrUpdate(repo);
                }));
    }

    @SuppressWarnings("ConstantConditions")
    public boolean isLiked(long id) {
        return realmFacade.get(realm -> first(id, realm).favourite);
    }

    public Repositories.Item get(long id) {
        return realmFacade.get(realm -> {
            final RealmRepo realmModel = first(id, realm);
            final Repositories.Item domainModel = new Repositories.Item();
            assert realmModel != null;
            domainModel.fullName = realmModel.name;
            domainModel.id = realmModel.id;
            return domainModel;
        });
    }

    @Nullable
    private static RealmRepo first(long id, Realm realm) {
        return realm.where(RealmRepo.class)
                .equalTo(RealmRepo.ID, id)
                .findFirst();
    }
}
