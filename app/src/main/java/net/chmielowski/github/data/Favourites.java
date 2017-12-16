package net.chmielowski.github.data;

import net.chmielowski.github.screen.RepositoryViewModel;

import java.util.List;

public interface Favourites {
    List<RepositoryViewModel> all();

    void like(Repositories.Item name);

    @SuppressWarnings("ConstantConditions")
    boolean isLiked(String repo);
}
