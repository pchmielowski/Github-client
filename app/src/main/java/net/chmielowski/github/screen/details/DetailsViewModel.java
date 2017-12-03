package net.chmielowski.github.screen.details;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import net.chmielowski.github.data.LikedRepos;
import net.chmielowski.github.data.ReposRepository;

import javax.inject.Inject;

import io.reactivex.Single;

public final class DetailsViewModel {
    public final ObservableField<String> owner = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableBoolean favourite = new ObservableBoolean(false);
    public final ObservableField<String> description = new ObservableField<>();

    private final ReposRepository service;
    private final LikedRepos likedRepos;

    private String id;
    public int url;

    @Inject
    DetailsViewModel(final ReposRepository service, final LikedRepos likedRepos) {
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

    public void addToFavs() {
        service.item(id)
                .subscribe(item -> {
                    likedRepos.like(item);
                    favourite.set(true);
                });
    }

    Single<String> url() {
        return service.item(id)
                .map(item -> item.owner.avatarUrl);
    }
}
