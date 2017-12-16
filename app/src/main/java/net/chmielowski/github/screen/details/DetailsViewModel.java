package net.chmielowski.github.screen.details;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Pair;

import net.chmielowski.github.RepositoryId;
import net.chmielowski.github.RepositoryScope;
import net.chmielowski.github.data.Favourites;
import net.chmielowski.github.data.RepoService;
import net.chmielowski.github.data.Repositories;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import static net.chmielowski.github.screen.details.DetailsViewModel.Action.LIKE;

@RepositoryScope
public final class DetailsViewModel {
    public final ObservableField<String> owner = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> description = new ObservableField<>();
    public final ObservableField<String> avatar = new ObservableField<>();

    public final ObservableBoolean favourite = new ObservableBoolean(false);
    public final ObservableBoolean loading = new ObservableBoolean(false);

    private final Favourites likedRepos;

    private final Repositories.Item repo;

    @Inject
    DetailsViewModel(@RepoService.OnMainThread final RepoService service,
                     final Favourites likedRepos,
                     @RepositoryId final String id) {
        this.likedRepos = likedRepos;
        this.repo = service.cached(id);
        bind(this.repo);
    }

    private void bind(final Repositories.Item item) {
        favourite.set(likedRepos.isLiked(item.fullName));
        owner.set(item.owner.login);
        name.set(item.name);
        description.set(item.description);
        avatar.set(item.owner.avatarUrl);
    }

    enum Action {
        LIKE, UNLIKE
    }

    private Subject<Pair<Action, String>> addedSubject = PublishSubject.create();

    public void addToFavs() {
        addedSubject.onNext(Pair.create(LIKE, name.get()));
        likedRepos.like(repo);
        favourite.set(true);
    }

    Observable<Pair<Action, String>> observeActions() {
        return addedSubject;
    }
}
