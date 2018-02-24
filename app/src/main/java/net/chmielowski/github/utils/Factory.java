package net.chmielowski.github.utils;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Provider;

public final class Factory implements ViewModelProvider.Factory {
    private final Provider<?> provider;

    public Factory(Provider<?> provider) {
        this.provider = provider;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull final Class<T> modelClass) {
        return (T) provider.get();
    }
}
