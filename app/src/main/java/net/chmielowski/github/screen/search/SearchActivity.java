package net.chmielowski.github.screen.search;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import net.chmielowski.github.R;
import net.chmielowski.github.databinding.ActivitySearchBinding;
import net.chmielowski.github.pagination.RxPagination;
import net.chmielowski.github.screen.Adapter;
import net.chmielowski.github.screen.BaseActivity;
import net.chmielowski.github.screen.OpenDetails;
import net.chmielowski.github.screen.QueryHistory;
import net.chmielowski.github.screen.SearchViewModel;
import net.chmielowski.github.screen.SearchesAdapter;
import net.chmielowski.github.screen.fav.FavsActivity;

import java.util.Arrays;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

import static com.jakewharton.rxbinding2.widget.RxTextView.editorActions;
import static com.jakewharton.rxbinding2.widget.RxTextView.textChanges;
import static net.chmielowski.github.utils.ApplicationUtils.application;

public class SearchActivity extends BaseActivity {

    @Inject
    OpenDetails openDetails;
    @Inject
    SearchViewModel model;
    @Inject
    Adapter resultsAdapter;
    @Inject
    SearchesAdapter searchHistoryAdapter;
    @Inject
    QueryHistory queryHistory;
    @Inject
    NetworkIndicatorViewModel networkIndicatorViewModel;

    private ActivitySearchBinding binding;
    private LinearLayoutManager resultsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application(this).activityComponent(this).inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        binding.setIndicatorViewModel(networkIndicatorViewModel);
        binding.setModel(model);
        resultsManager = new LinearLayoutManager(this);
        binding.results.setLayoutManager(resultsManager);
        binding.results.setAdapter(resultsAdapter);
        ViewCompat.setNestedScrollingEnabled(binding.results, false);

        binding.searches.setLayoutManager(new LinearLayoutManager(this));
        binding.searches.setAdapter(searchHistoryAdapter);

        final LayoutTransition transition = binding.layout.getLayoutTransition();
        transition.setInterpolator(LayoutTransition.CHANGE_APPEARING, new OvershootInterpolator(10));
        binding.layout.setLayoutTransition(transition);
    }

    @NonNull
    @Override
    protected Iterable<Disposable> disposables() {
        return Arrays.asList(
                model.replaceResults(
                        editorActions(binding.search, action -> action == EditorInfo.IME_ACTION_SEARCH)
                                .doOnNext(__ -> hideKeyboard()),
                        searchHistoryAdapter.observeClicks().doOnNext(__ -> hideKeyboard()),
                        textChanges(binding.search))
                        .subscribe(results -> resultsAdapter.replace(results)),
                model.appendResults(RxPagination.scrolledCloseToEnd(binding.results, resultsManager))
                        .subscribe(results -> resultsAdapter.append(results)),
                queryHistory.observe().subscribe(queries -> searchHistoryAdapter.update(queries)),
                resultsAdapter.observeClicks().subscribe(clickedItem -> openDetails.invoke(clickedItem)),
                networkIndicatorViewModel.observe().subscribe(state -> {
                    switch (state) {
                        case OFFLINE:
                            binding.networkIndicator.onOffline();
                            break;
                        case ONLINE:
                            binding.networkIndicator.onOnline();
                            break;
                    }
                }));
    }

    @SuppressWarnings("ConstantConditions")
    private void hideKeyboard() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(binding.search.getWindowToken(), 0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, FavsActivity.class));
            return true;
        }
        throw new IllegalArgumentException("Unknown menu item");
    }

}
