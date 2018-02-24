package net.chmielowski.github.screen.details;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;

import net.chmielowski.github.Browser;
import net.chmielowski.github.CustomApplication;
import net.chmielowski.github.R;
import net.chmielowski.github.databinding.ActivityDetailsBinding;
import net.chmielowski.github.screen.BaseActivity;
import net.chmielowski.github.utils.Factory;

import java.util.Collections;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

import static net.chmielowski.github.screen.details.DetailsViewModel.Action.Type.LIKE;

public class DetailsActivity extends BaseActivity {
    public static final String KEY_ID = "REPOSITORY_ID";

    @Inject
    DetailsViewModelFactory factory;
    DetailsViewModel model;

    @SuppressWarnings("WeakerAccess")
    @Inject
    Browser browser;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.get(this)
                .activityComponent(this, getIntent().getStringExtra(KEY_ID), savedInstanceState == null)
                .inject(this);

        model = ViewModelProviders.of(this, new Factory(() -> factory.create()))
                .get(DetailsViewModel.class);


        final ActivityDetailsBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_details);
        binding.setModel(model);

        model.addedLiveData.observe(this, action -> show(asMessage(action)));
    }

    @NonNull
    @Override
    protected Iterable<Disposable> disposables() {
        return Collections.emptyList();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        if (model.homepage() != null) {
            getMenuInflater().inflate(R.menu.menu_details, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.action_homepage) {
            browser.open(model.homepage());
            return true;
        }
        throw new IllegalArgumentException("Unknown menu item");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            CustomApplication.get(this).releaseRepositoryComponent();
        }
    }
}
