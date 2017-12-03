package net.chmielowski.github.screen;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import net.chmielowski.github.ActivityContext;
import net.chmielowski.github.ActivityScope;
import net.chmielowski.github.R;
import net.chmielowski.github.databinding.ItemRepoBinding;
import net.chmielowski.github.databinding.ItemSearchBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

@ActivityScope
public final class SearchesAdapter extends RecyclerView.Adapter<SearchesAdapter.ViewHolder> {
    private final Context context;
    private final List<String> items = new ArrayList<>();

    @Inject
    public SearchesAdapter(@ActivityContext final Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.item_search, parent, false));
    }

    private final Subject<Pair<ItemRepoBinding, String>> clickSubject = PublishSubject.create();

    public Observable<Pair<ItemRepoBinding, String>> observeClicks() {
        return clickSubject;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.binding.text.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void add(final String query) {
        items.add(query);
        notifyItemInserted(items.size());
    }

    public void update(final Collection<String> queries) {
        items.clear();
        items.addAll(queries);
        notifyDataSetChanged();
    }

    final class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemSearchBinding binding;

        ViewHolder(final ItemSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
