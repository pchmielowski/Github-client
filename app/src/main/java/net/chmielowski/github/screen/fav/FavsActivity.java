package net.chmielowski.github.screen.fav;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import net.chmielowski.github.CustomApplication;
import net.chmielowski.github.R;
import net.chmielowski.github.databinding.ActivityFavsBinding;
import net.chmielowski.github.screen.list.Adapter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public final class FavsActivity extends AppCompatActivity {
    @Inject
    FavsViewModel model;

    @Inject
    Adapter adapter;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CustomApplication) getApplication()).component(this).inject(this);
        ActivityFavsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_favs);
        binding.setModel(model);
        binding.list.setLayoutManager(new LinearLayoutManager(this));
        binding.list.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        disposable.addAll(
//                adapter.observeClicks().subscribe(this::startDetailsActivity),
                model.data().subscribe(results -> adapter.update(results)));
    }

    @Override
    protected void onPause() {
        disposable.clear();
        super.onPause();
    }
}
