package net.chmielowski.github.screen.fav;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import net.chmielowski.github.CustomApplication;
import net.chmielowski.github.R;
import net.chmielowski.github.databinding.ActivityFavsBinding;
import net.chmielowski.github.screen.BaseActivity;
import net.chmielowski.github.screen.OpenDetails;
import net.chmielowski.github.screen.SearchResultsAdapter;

import java.util.Collections;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public final class FavsActivity extends BaseActivity {

    @Inject
    FavsViewModelFactory factory;
    FavsViewModel model;

    @SuppressWarnings("WeakerAccess")
    @Inject
    SearchResultsAdapter adapter;

    @SuppressWarnings("WeakerAccess")
    @Inject
    OpenDetails openDetails;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.get(this).activityComponent(this).inject(this);

        model = ViewModelProviders.of(this, new ViewModelProvider.Factory() {
            @SuppressWarnings("unchecked")
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull final Class<T> modelClass) {
                return (T) factory.create();
            }
        }).get(FavsViewModel.class);

        ActivityFavsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_favs);
        binding.setModel(model);
        binding.list.setLayoutManager(new LinearLayoutManager(this));
        binding.list.setAdapter(adapter);

        model.favourites.observe(this, data -> adapter.update(data));
    }


    @Override
    @NonNull
    protected Iterable<Disposable> disposables() {
        return Collections.singletonList(
                adapter.observeClicks()
                        .flatMapMaybe(clicked -> model.cache(clicked.second)
                                .filter(success -> success)
                                .map(__ -> clicked))
                        .subscribe(clicked -> openDetails.invoke(clicked))
        );
    }
}
