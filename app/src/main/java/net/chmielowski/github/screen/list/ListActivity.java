package net.chmielowski.github.screen.list;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.jakewharton.rxbinding2.widget.RxTextView;

import net.chmielowski.github.CustomApplication;
import net.chmielowski.github.R;
import net.chmielowski.github.databinding.ActivityMainBinding;
import net.chmielowski.github.screen.details.DetailsActivity;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class ListActivity extends AppCompatActivity {
    @Inject
    ListViewModel model;

    @Inject
    Adapter adapter;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CustomApplication) getApplication()).component(this).inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setModel(model);
        setSupportActionBar(binding.toolbar);
        binding.list.setLayoutManager(new LinearLayoutManager(this));
        binding.list.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: unsubscribe
        adapter.observeClicks()
                .subscribe(id -> {
                    final Intent intent = new Intent(this, DetailsActivity.class);
                    intent.putExtra(DetailsActivity.KEY_ID, id);
                    startActivity(intent);
                });
        RxTextView.textChanges(binding.search)
                .debounce(1, TimeUnit.SECONDS)
                .map(String::valueOf)
                .filter(text -> !text.isEmpty())
                .flatMapSingle(text -> model.fetchData(text))
                .subscribe(repositories -> adapter.update(repositories));
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
