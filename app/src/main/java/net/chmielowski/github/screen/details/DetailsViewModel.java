package net.chmielowski.github.screen.details;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Pair;

import net.chmielowski.github.OnMainThread;
import net.chmielowski.github.data.LikedRepos;
import net.chmielowski.github.data.RepoService;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import static net.chmielowski.github.screen.details.DetailsViewModel.Action.LIKE;

public final class DetailsViewModel {
    public final ObservableField<String> owner = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableBoolean favourite = new ObservableBoolean(false);
    public final ObservableField<String> description = new ObservableField<>();

    private final RepoService service;
    private final LikedRepos likedRepos;

    private String id;
    public int url;

    @Inject
    DetailsViewModel(@OnMainThread final RepoService service, final LikedRepos likedRepos) {
        this.service = service;
        this.likedRepos = likedRepos;
    }

    void setRepo(final String repo) {
        this.id = repo;
        service.item(repo)
                .subscribe(item -> {
                    favourite.set(likedRepos.isLiked(item.fullName));
                    owner.set(item.owner.login);
                    name.set(item.name);
                    description.set(item.description);
                });
    }

    enum Action {
        LIKE, UNLIKE
    }

    private Subject<Pair<Action, String>> addedSubject = PublishSubject.create();

    public void addToFavs() {
        service.item(id)
                .subscribe(item -> {
                    addedSubject.onNext(Pair.create(LIKE, name.get()));
                    likedRepos.like(item);
                    favourite.set(true);
                });
    }

    Single<String> url() {
        return service.item(id)
                .map(item -> item.owner.avatarUrl);
    }

    Observable<Pair<Action, String>> observeActions() {
        return addedSubject;
    }
}
