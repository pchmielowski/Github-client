package net.chmielowski.github.screen.fav;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import net.chmielowski.github.CustomApplication;
import net.chmielowski.github.R;
import net.chmielowski.github.Repositories;
import net.chmielowski.github.RepositoryViewModel;
import net.chmielowski.github.databinding.ActivityFavsBinding;
import net.chmielowski.github.screen.list.Adapter;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

public final class FavsActivity extends AppCompatActivity {
    @Inject
    FavsViewModel model;

    @Inject
    Adapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CustomApplication) getApplication()).component(this).inject(this);
        ActivityFavsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_favs);
        binding.setModel(model);

        binding.list.setLayoutManager(new LinearLayoutManager(this));
        binding.list.setAdapter(adapter);

        adapter.update(Stream.of(
                "hello", "world", "dupa"
        ).map(word -> {
            final Repositories.Item item = new Repositories.Item();
            item.fullName = word;
            item.id = 13L;
            return new RepositoryViewModel(item);
        }).collect(Collectors.toList()));

    }
}
