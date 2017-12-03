package net.chmielowski.github.screen.details;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import net.chmielowski.github.data.ReposRepository;
import net.chmielowski.github.data.Repositories;
import net.chmielowski.github.screen.RepositoryViewModel;
import net.chmielowski.github.data.LikedRepos;

import javax.inject.Inject;

public final class DetailsViewModel {
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableBoolean favourite = new ObservableBoolean(false);

    private final ReposRepository service;
    private final LikedRepos likedRepos;

    private String id;

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
                    name.set(new RepositoryViewModel(item).name.toString());
                });
    }

    public void addToFavs() {
        service.item(id)
                .subscribe(item -> {
                    likedRepos.like(item);
                    favourite.set(true);
                });
    }
}
