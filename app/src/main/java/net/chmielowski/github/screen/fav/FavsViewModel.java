package net.chmielowski.github.screen.fav;

import net.chmielowski.github.data.RealmRepo;
import net.chmielowski.github.screen.RepositoryViewModel;
import net.chmielowski.github.screen.search.RealmFacade;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Observable;

public class FavsViewModel {
    private final RealmFacade realm;

    @Inject
    FavsViewModel(final RealmFacade realm) {
        this.realm = realm;
    }

    public Observable<Collection<RepositoryViewModel>> data() {
        // TODO: move to Service class
        return Observable.just(realm.get(realm ->
                realm.where(RealmRepo.class)
                        .equalTo(RealmRepo.FAVOURITE, true)
                        .findAll()
                        .stream()
                        .map(RepositoryViewModel::new)
                        .collect(Collectors.toList())));
    }
}
