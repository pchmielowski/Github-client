package net.chmielowski.github.screen.details;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import net.chmielowski.github.RepositoryId;
import net.chmielowski.github.RepositoryScope;
import net.chmielowski.github.data.Favourites;
import net.chmielowski.github.data.Repositories;
import net.chmielowski.github.data.RepositoryDataSource;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static net.chmielowski.github.screen.details.DetailsViewModel.Action.Type.LIKE;
import static net.chmielowski.github.screen.details.DetailsViewModel.Action.Type.UNLIKE;

@RepositoryScope
public final class DetailsViewModel {
    public final ObservableField<String> owner = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> description = new ObservableField<>();
    public final ObservableField<String> avatar = new ObservableField<>();

    public final ObservableBoolean favourite = new ObservableBoolean(false);

    private final Favourites likedRepos;

    private final Repositories.Item repo;

    @Inject
    DetailsViewModel(@RepositoryDataSource.WorkOnBackground final RepositoryDataSource service,
                     final Favourites likedRepos,
                     @RepositoryId final String id) {
        this.likedRepos = likedRepos;
        this.repo = service.repositoryFromCache(id);
        bind(this.repo);
    }

    private void bind(final Repositories.Item item) {
        favourite.set(likedRepos.isLiked(item.fullName));
        owner.set(item.owner.login);
        name.set(item.name);
        description.set(item.description);
        avatar.set(item.owner.avatarUrl);
    }

    @EqualsAndHashCode
    @ToString
    final static class Action {
        final String repo;

        enum Type {
            LIKE, UNLIKE

        }

        final Type type;

        Action(final Type type, final String repo) {
            this.repo = repo;
            this.type = type;
        }
    }


    private Subject<Action> addedSubject = PublishSubject.create();

    public void toggleLike() {
        likedRepos.toggle(repo)
                .subscribe(like -> {
                    favourite.set(like);
                    addedSubject.onNext(new Action(like ? LIKE : UNLIKE, name.get()));
                });
    }

    Observable<Action> observeActions() {
        return addedSubject;
    }
}
