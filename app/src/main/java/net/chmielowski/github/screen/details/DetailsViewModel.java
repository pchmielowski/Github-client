package net.chmielowski.github.screen.details;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import net.chmielowski.github.ReposRepository;
import net.chmielowski.github.RepositoryViewModel;
import net.chmielowski.github.screen.list.Cache;

import javax.inject.Inject;

public final class DetailsViewModel {
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableBoolean favourite = new ObservableBoolean(false);

    private final ReposRepository service;
    private final Cache cache;

    private long id;

    @Inject
    DetailsViewModel(final ReposRepository service, final Cache cache) {
        this.service = service;
        this.cache = cache;
    }

    void setRepo(final long repo) {
        this.id = repo;
        favourite.set(cache.isLiked(id));
        service.item(repo)
                .map(RepositoryViewModel::new)
                .subscribe(item -> name.set(item.name.toString()));
    }

    public void addToFavs() {
        cache.like(id);
        favourite.set(true);
    }
}
