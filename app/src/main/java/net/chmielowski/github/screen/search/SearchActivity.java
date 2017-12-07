package net.chmielowski.github.screen.search;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.ViewSwitcher;

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
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.view.animation.AnimationUtils.loadAnimation;
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

        final LayoutTransition transition = binding.layout.getLayoutTransition();
        transition.setInterpolator(LayoutTransition.CHANGE_APPEARING, new OvershootInterpolator(10));
        binding.layout.setLayoutTransition(transition);
    }

    @NonNull
    @Override
    protected Iterable<Disposable> disposables() {
        return Arrays.asList(
                model.searchResults(clicks(binding.fab),
                        searchAdapter.observeClicks(),
                        RxPagination.scrolledCloseToEnd(binding.list, resultsManager),
                        textChanges(binding.search).map(CharSequence::toString)) // TODO: just use CharSequence everywhere
                        .subscribe(results -> reposAdapter.append(results)),
                model.searches().subscribe(queries -> searchAdapter.update(queries)),
                model.searchVisibleDisposable());
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchAdapter.observeClicks().subscribe(model.search());

        binding.offline.setFactory(() -> {
            TextView myText = new TextView(this);
            myText.setGravity(Gravity.CENTER_HORIZONTAL);
            return myText;
        });

        binding.offline.setInAnimation(loadAnimation(this, android.R.anim.fade_in));
        binding.offline.setOutAnimation(loadAnimation(this, android.R.anim.fade_out));

        binding.offline.setText("Hello");

        Observable.interval(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(final Long aLong) throws Exception {
                        binding.offline.setText(aLong % 3 == 2 ? "You are online" : "You are offline");
                        binding.offline.setBackgroundResource(aLong % 3 == 2 ? R.color.colorOnline : R.color.colorAccent);
                        binding.offline.setVisibility(aLong % 3 == 0 ? View.GONE : View.VISIBLE);
                    }
                });
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
