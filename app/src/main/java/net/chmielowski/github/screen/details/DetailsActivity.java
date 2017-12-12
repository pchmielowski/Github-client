package net.chmielowski.github.screen.details;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Pair;

import com.squareup.picasso.Picasso;

import net.chmielowski.github.R;
import net.chmielowski.github.databinding.ActivityDetailsBinding;
import net.chmielowski.github.screen.BaseActivity;

import java.util.Collections;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

import static net.chmielowski.github.screen.details.DetailsViewModel.Action.LIKE;
import static net.chmielowski.github.utils.ApplicationUtils.application;

public class DetailsActivity extends BaseActivity {
    public static final String KEY_ID = "REPOSITORY_ID";

    @Inject
    DetailsViewModel model;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application(this).activityComponent(this).inject(this);
        model.setRepo(getIntent().getStringExtra(KEY_ID));

        final ActivityDetailsBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_details);
        binding.setModel(model);

        model.url()
                .subscribe(url -> Picasso.with(this)
                        .load(url)
                        .placeholder(R.drawable.ic_avatar_placeholder)
                        .fit()
                        .into(binding.avatar));
    }

    @NonNull
    @Override
    protected Iterable<Disposable> disposables() {
        return Collections.singletonList(model.observeActions()
                .map(this::asMessage)
                .subscribe(this::show));
    }

    private void show(final String message) {
        Snackbar.make(findViewById(R.id.container), message, Snackbar.LENGTH_LONG)
                .show();
    }

    @NonNull
    private String asMessage(Pair<DetailsViewModel.Action, String> action) {
        return String.format("%s %s",
                getString(action.first == LIKE ? R.string.now_you_like : R.string.you_unlike),
                action.second);
    }
}
