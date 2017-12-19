package net.chmielowski.github.screen.search;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import net.chmielowski.github.CustomApplication;
import net.chmielowski.github.R;
import net.chmielowski.github.data.User;
import net.chmielowski.github.databinding.ActivitySearchBinding;
import net.chmielowski.github.network.NetworkIndicatorViewModel;
import net.chmielowski.github.pagination.RxPagination;
import net.chmielowski.github.screen.BaseActivity;
import net.chmielowski.github.screen.OpenDetails;
import net.chmielowski.github.screen.QueryHistory;
import net.chmielowski.github.screen.SearchResultsAdapter;
import net.chmielowski.github.screen.SearchesAdapter;
import net.chmielowski.github.screen.fav.FavsActivity;
import net.chmielowski.github.screen.login.LoginActivity;

import java.util.Arrays;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

import static com.jakewharton.rxbinding2.widget.RxTextView.editorActions;

public class SearchActivity extends BaseActivity {

    @Inject
    OpenDetails openDetails;
    @Inject
    SearchViewModel model;
    @Inject
    SearchResultsAdapter resultsAdapter;
    @Inject
    SearchesAdapter searchHistoryAdapter;
    @Inject
    QueryHistory queryHistory;
    @Inject
    NetworkIndicatorViewModel networkIndicatorViewModel;
    @Inject
    User user;

    private ActivitySearchBinding binding;
    private LinearLayoutManager resultsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.get(this).activityComponent(this).inject(this);
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

    @Override
    public void onBackPressed() {
        model.onBackPressed()
                .filter(consumed -> consumed)
                .subscribe(__ -> super.onBackPressed());
    }

    @NonNull
    @Override
    protected Iterable<Disposable> disposables() {
        return Arrays.asList(
                model.error().subscribe(this::showError),
                model.replaceResults(
                        editorActions(binding.search, action -> action == EditorInfo.IME_ACTION_SEARCH)
                                .doOnNext(__ -> hideKeyboard()),
                        searchHistoryAdapter.observeClicks().doOnNext(__ -> hideKeyboard()))
                        .subscribe(results -> resultsAdapter.replace(results)),
                model.appendResults(RxPagination.scrolledCloseToEnd(binding.results, resultsManager))
                        .subscribe(results -> resultsAdapter.append(results)),
                queryHistory.observe().subscribe(queries -> searchHistoryAdapter.update(queries)),
                resultsAdapter.observeClicks().subscribe(clickedItem -> openDetails.invoke(clickedItem)),
                networkIndicatorViewModel.start());
    }

    private void showError(final SearchViewModel.ErrorMessage message) {
        switch (message) {
            case EMPTY_QUERY:
                final EditText search = binding.search;
                search.setError("Query can not be empty");
                search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(final CharSequence sequence, final int i, final int i1, final int i2) {

                    }

                    @Override
                    public void onTextChanged(final CharSequence sequence, final int i, final int i1, final int i2) {
                        search.setError(null);
                    }

                    @Override
                    public void afterTextChanged(final Editable editable) {

                    }
                });
                break;
        }
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
        if (item.getItemId() == R.id.action_favourites) {
            startActivity(new Intent(this, FavsActivity.class));
            return true;
        }
        if (item.getItemId() == R.id.action_logout) {
            user.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        throw new IllegalArgumentException("Unknown menu item");
    }

}
