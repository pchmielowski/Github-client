package net.chmielowski.github.screen.details;

import android.databinding.ObservableField;

import net.chmielowski.github.ReposRepository;
import net.chmielowski.github.RepositoryViewModel;

import javax.inject.Inject;

public final class DetailsViewModel {
    public final ObservableField<String> name = new ObservableField<>();

    private final ReposRepository service;

    @Inject
    DetailsViewModel(final ReposRepository service) {
        this.service = service;
    }

    void setRepo(final long repo) {
        service.item(repo)
                .map(RepositoryViewModel::new)
                .subscribe(item -> name.set(item.name.toString()));
    }
}
