package net.chmielowski.github.screen.list;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;

import net.chmielowski.github.CustomApplication;
import net.chmielowski.github.R;
import net.chmielowski.github.RepositoryViewModel;
import net.chmielowski.github.databinding.ActivityMainBinding;
import net.chmielowski.github.databinding.ItemRepoBinding;
import net.chmielowski.github.screen.details.DetailsActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class MainActivity extends AppCompatActivity {
    @Inject
    MainViewModel model;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CustomApplication) getApplication()).component().inject(this);
        // TODO: binding
        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);

        binding.list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(MainActivity.this);
        binding.list.setAdapter(adapter);

        binding.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

    @Override
    protected void onResume() {
        super.onResume();
        model.fetchData()
                .subscribe(repositories -> adapter.update(repositories));
        // TODO: unsubscribe
        adapter.observeClicks()
                .subscribe(id -> {
                    final Intent intent = new Intent(this, DetailsActivity.class);
                    intent.putExtra(DetailsActivity.KEY_ID, id);
                    startActivity(intent);
                });
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


    private static final class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        private final MainActivity context;
        private final List<RepositoryViewModel> items = new ArrayList<>();

        Adapter(final MainActivity context) {
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context),
                    R.layout.item_repo, parent, false));
        }

        private final Subject<Long> clickSubject = PublishSubject.create();

        Observable<Long> observeClicks() {
            return clickSubject;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final RepositoryViewModel model = items.get(position);
            holder.bind(model);
            RxView.clicks(holder.itemView)
                    .map(__ -> model.id)
                    .subscribe(clickSubject);
        }

        @Override
        public void onViewRecycled(final ViewHolder holder) {
            super.onViewRecycled(holder);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        private void update(final Collection<RepositoryViewModel> repositories) {
            items.clear();
            items.addAll(repositories);
            notifyDataSetChanged();
        }

        final class ViewHolder extends RecyclerView.ViewHolder {
            private final ItemRepoBinding binding;

            ViewHolder(final ItemRepoBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            private void bind(final RepositoryViewModel model) {
                binding.setModel(model);
            }
        }
    }
}
