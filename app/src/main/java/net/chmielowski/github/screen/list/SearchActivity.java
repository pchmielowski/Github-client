package net.chmielowski.github.screen.list;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;

import net.chmielowski.github.CustomApplication;
import net.chmielowski.github.R;
import net.chmielowski.github.databinding.ActivitySearchBinding;
import net.chmielowski.github.databinding.ItemRepoBinding;
import net.chmielowski.github.screen.details.DetailsActivity;
import net.chmielowski.github.screen.fav.FavouritesActivity;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

import static android.support.v4.app.ActivityOptionsCompat.makeSceneTransitionAnimation;
import static com.jakewharton.rxbinding2.view.RxView.clicks;
import static com.jakewharton.rxbinding2.widget.RxTextView.textChanges;

public class SearchActivity extends AppCompatActivity {
    @Inject
    SearchViewModel model;

    @Inject
    Adapter adapter;

    private ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CustomApplication) getApplication()).component(this).inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        binding.setModel(model);
        binding.list.setLayoutManager(new LinearLayoutManager(this));
        binding.list.setAdapter(adapter);
    }

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onResume() {
        super.onResume();
        clicks(binding.fab).subscribe(model.searchClicked());
        textChanges(binding.search).subscribe(model.queryChanged());
        disposable.addAll(
                adapter.observeClicks().subscribe(this::startDetailsActivity),
                model.searchResults().subscribe(results -> adapter.update(results)),
                model.searchVisibleDisposable());
    }

    private void startDetailsActivity(final Pair<ItemRepoBinding, Long> clickedItem) {
        final Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.KEY_ID, clickedItem.second);
        final ActivityOptionsCompat options = makeSceneTransitionAnimation(
                this,
                clickedItem.first.name,
                getString(R.string.shared_element_transition));
        startActivity(intent, options.toBundle());
    }

    @Override
    protected void onPause() {
        disposable.clear();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startFavouritesActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startFavouritesActivity() {
        startActivity(new Intent(this, FavouritesActivity.class));
    }
}
