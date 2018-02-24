package net.chmielowski.github.screen.fav;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import net.chmielowski.github.data.Favourites;
import net.chmielowski.github.data.RepositoryDataSource;
import net.chmielowski.github.screen.RepositoryViewModel;

import java.util.Collection;

import javax.inject.Inject;

import io.reactivex.Single;

@AutoFactory
public class FavsViewModel extends ViewModel {

    public final ObservableBoolean loading = new ObservableBoolean();

    final MutableLiveData<Collection<RepositoryViewModel>> favourites;

    private final RepositoryDataSource data;

    @Inject
    FavsViewModel(@Provided @RepositoryDataSource.WorkOnBackground final RepositoryDataSource data,
                  @Provided final Favourites favourites) {
        this.data = data;
        this.favourites = new MutableLiveData<>();
        this.favourites.setValue(favourites.all());
    }

    Single<Boolean> cache(final String repo) {
        return data.cacheRepository(repo)
                .doOnSubscribe(__ -> loading.set(true))
                .doOnSuccess(__ -> loading.set(false));
    }
}
