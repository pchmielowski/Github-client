package net.chmielowski.github.screen.search;

import android.animation.LayoutTransition;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.Task;

import net.chmielowski.github.CustomApplication;
import net.chmielowski.github.R;
import net.chmielowski.github.SendNetworkConnectedBroadcast;
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
                model.searchVisibleDisposable(textChanges(binding.search)));
    }

    // TODO: unregister
    private final BroadcastReceiver networkConnectedBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            Log.d("pchm", "SearchActivity onReceive ");
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        searchAdapter.observeClicks().subscribe(model.search());

        waitForOnline();

        LocalBroadcastManager.getInstance(this).registerReceiver(
                networkConnectedBroadcastReceiver,
                new IntentFilter(SendNetworkConnectedBroadcast.NETWORK_AVAILABLE));
    }

    @SuppressWarnings("ConstantConditions")
    boolean isOnline() {
        return connectivityManager().getActiveNetworkInfo().isConnected();
    }

    private final static String TAG = "NETWORK_CONNECTED";


    void waitForOnline() {
        final long ONE_HOUR = 3600L;
        final OneoffTask task = new OneoffTask.Builder()
                .setService(SendNetworkConnectedBroadcast.class)
                .setTag(TAG)
                .setExecutionWindow(0L, ONE_HOUR)
                .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                .build();
        GcmNetworkManager.getInstance(this).schedule(task);
    }


    private ConnectivityManager connectivityManager() {
        return (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    }


    void onOffline() {
        setTo(binding.offline);
    }

    void onOnline() {
        setTo(binding.online);
    }

    private void setTo(final TextView view) {
        final ViewSwitcher indicator = binding.networkIndicator;
        indicator.setVisibility(View.VISIBLE);
        final View current = indicator.getCurrentView();
        if (current == view) {
            return;
        }
        indicator.showNext();
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
