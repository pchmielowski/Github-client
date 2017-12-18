package net.chmielowski.github.screen.details;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import net.chmielowski.github.CustomApplication;
import net.chmielowski.github.R;
import net.chmielowski.github.databinding.ActivityDetailsBinding;
import net.chmielowski.github.screen.BaseActivity;

import java.util.Collections;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

import static net.chmielowski.github.screen.details.DetailsViewModel.Action.Type.LIKE;

public class DetailsActivity extends BaseActivity {
    public static final String KEY_ID = "REPOSITORY_ID";

    @Inject
    DetailsViewModel model;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.get(this)
                .component(this, getIntent().getStringExtra(KEY_ID), savedInstanceState == null)
                .inject(this);

        final ActivityDetailsBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_details);
        binding.setModel(model);
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
    private String asMessage(final DetailsViewModel.Action action) {
        return String.format("%s %s",
                getString(action.type == LIKE ? R.string.msg_added_to_favs : R.string.msg_you_unlike),
                action.repo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            CustomApplication.get(this).releaseRepositoryComponent();
        }
    }
}
