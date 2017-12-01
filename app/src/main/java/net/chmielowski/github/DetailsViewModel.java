package net.chmielowski.github;

import android.databinding.ObservableField;

import javax.inject.Inject;

public final class DetailsViewModel {
    public final ObservableField<String> name = new ObservableField<>();

    private final ReposRepository service;

    @Inject
    DetailsViewModel(final ReposRepository service) {
        this.service = service;
    }

    void setRepo(final long repo) {
        service.repository(repo)
                .subscribe(item -> name.set(item.fullName));
    }
}
