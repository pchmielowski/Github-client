package net.chmielowski.github.data;

import net.chmielowski.github.screen.SearchViewModel;

import java.util.Collection;

import io.reactivex.Single;

// TODO: rename to service
public interface ReposRepository {
    Single<Repositories.Item> item(String id);

    Single<Collection<Repositories.Item>> items(SearchViewModel.Query query);
}
