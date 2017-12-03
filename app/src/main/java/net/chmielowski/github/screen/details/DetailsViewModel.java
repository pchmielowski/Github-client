package net.chmielowski.github.screen.details;

import android.databinding.ObservableField;

import net.chmielowski.github.ReposRepository;
import net.chmielowski.github.RepositoryViewModel;
import net.chmielowski.github.screen.list.Cache;

import javax.inject.Inject;

public final class DetailsViewModel {
    public final ObservableField<String> name = new ObservableField<>();

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
        service.item(repo)
                .map(RepositoryViewModel::new)
                .subscribe(item -> name.set(item.name.toString()));
    }

    public void addToFavs() {
        cache.like(id);
    }
}
