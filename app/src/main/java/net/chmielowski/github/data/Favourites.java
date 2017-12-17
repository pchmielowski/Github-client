package net.chmielowski.github.data;

import net.chmielowski.github.screen.RepositoryViewModel;

import java.util.List;

import io.reactivex.Single;

public interface Favourites {
    List<RepositoryViewModel> all();

    Single<Boolean> toggle(Repositories.Item name);

    @SuppressWarnings("ConstantConditions")
    boolean isLiked(String repo);
}
