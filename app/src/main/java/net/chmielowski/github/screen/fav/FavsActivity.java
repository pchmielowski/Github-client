package net.chmielowski.github.screen.fav;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import net.chmielowski.github.CustomApplication;
import net.chmielowski.github.R;
import net.chmielowski.github.databinding.ActivityFavsBinding;
import net.chmielowski.github.screen.Adapter;
import net.chmielowski.github.screen.BaseActivity;
import net.chmielowski.github.screen.OpenDetails;

import java.util.Arrays;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public final class FavsActivity extends BaseActivity {
    @Inject
    FavsViewModel model;

    @Inject
    Adapter adapter;

    @Inject
    OpenDetails openDetails;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.get(this).component(this).inject(this);
        ActivityFavsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_favs);
        binding.setModel(model);
        binding.list.setLayoutManager(new LinearLayoutManager(this));
        binding.list.setAdapter(adapter);
    }


    @Override
    @NonNull
    protected Iterable<Disposable> disposables() {
        return Arrays.asList(
                adapter.observeClicks().subscribe(clickedItem -> openDetails.invoke(clickedItem)),
                model.data().subscribe(results -> adapter.update(results))
        );
    }
}
