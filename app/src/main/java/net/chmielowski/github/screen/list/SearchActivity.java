package net.chmielowski.github.screen.list;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.chmielowski.github.CustomApplication;
import net.chmielowski.github.R;
import net.chmielowski.github.databinding.ActivitySearchBinding;
import net.chmielowski.github.screen.details.DetailsActivity;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

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
        setSupportActionBar(binding.toolbar);
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

    @Override
    protected void onPause() {
        disposable.clear();
        super.onPause();
    }

    private void startDetailsActivity(Long id) {


        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.KEY_ID, id);

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, binding.test, getString(R.string.shared_element_transition));
        startActivity(intent, options.toBundle());


//
//        final Intent intent = new Intent(this, DetailsActivity.class);
//        intent.putExtra(DetailsActivity.KEY_ID, id);
//        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
