package net.chmielowski.github.screen.search;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import net.chmielowski.github.CustomApplication;
import net.chmielowski.github.R;
import net.chmielowski.github.databinding.ActivitySearchBinding;
import net.chmielowski.github.pagination.RxPagination;
import net.chmielowski.github.screen.Adapter;
import net.chmielowski.github.screen.BaseActivity;
import net.chmielowski.github.screen.OpenDetails;
import net.chmielowski.github.screen.SearchViewModel;
import net.chmielowski.github.screen.SearchesAdapter;
import net.chmielowski.github.screen.fav.FavsActivity;

import java.util.Arrays;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

import static com.jakewharton.rxbinding2.view.RxView.clicks;
import static com.jakewharton.rxbinding2.widget.RxTextView.textChanges;

public class SearchActivity extends BaseActivity {

    @Inject
    OpenDetails openDetails;

    @Inject
    SearchViewModel model;

    @Inject
    Adapter reposAdapter;

    @Inject
    SearchesAdapter searchAdapter;

    private ActivitySearchBinding binding;
    private LinearLayoutManager resultsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CustomApplication) getApplication()).component(this).inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        binding.setModel(model);
        resultsManager = new LinearLayoutManager(this);
        binding.list.setLayoutManager(resultsManager);
        binding.list.setAdapter(reposAdapter);
        ViewCompat.setNestedScrollingEnabled(binding.list, false);

        binding.searches.setLayoutManager(new LinearLayoutManager(this));
        binding.searches.setAdapter(searchAdapter);
    }

    @NonNull
    @Override
    protected Iterable<Disposable> disposables() {
        return Arrays.asList(
                reposAdapter.observeClicks().subscribe(clickedItem -> openDetails.invoke(clickedItem)),
                model.searchResults().subscribe(results -> reposAdapter.append(results)),
                model.searches().subscribe(queries -> searchAdapter.update(queries)),
                model.searchVisibleDisposable());
    }

    @Override
    protected void onResume() {
        super.onResume();
        clicks(binding.fab).subscribe(model.searchClicked());
        textChanges(binding.search).subscribe(model.queryChanged());
        RxPagination.scrolledCloseToEnd(binding.list, resultsManager)
                .subscribe(model.scrolledCloseToEnd());
        searchAdapter.observeClicks().subscribe(model.search());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startFavouritesActivity();
            return true;
        }
        throw new IllegalArgumentException("Unknown menu item");
    }

    private void startFavouritesActivity() {
        startActivity(new Intent(this, FavsActivity.class));
    }

}
