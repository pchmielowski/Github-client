package net.chmielowski.github.screen;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.squareup.picasso.Picasso;

import net.chmielowski.github.ActivityContext;
import net.chmielowski.github.ActivityScope;
import net.chmielowski.github.R;
import net.chmielowski.github.databinding.ItemRepoBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

@ActivityScope
public final class SearchResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_REPO = 0;
    static final int TYPE_SPINNER = 1;

    private final Context context;
    private final List<RepositoryViewModel> items = new ArrayList<>();
    private boolean loading = false;

    @Inject
    SearchResultsAdapter(@ActivityContext final Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(final int position) {
        return position == items.size() ? TYPE_SPINNER : TYPE_REPO;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return viewType == TYPE_SPINNER ? new SpinnerViewHolder(parent) : new RepoViewHolder(parent);
    }

    private <T extends ViewDataBinding> T inflate(final ViewGroup parent, final int layout) {
        return DataBindingUtil.inflate(LayoutInflater.from(context), layout, parent, false);
    }

    private final Subject<Pair<ItemRepoBinding, String>> clickSubject = PublishSubject.create();

    public Observable<Pair<ItemRepoBinding, String>> observeClicks() {
        return clickSubject;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof RepoViewHolder) {
            bindViewHolder((RepoViewHolder) holder, position);
        }
    }

    private void bindViewHolder(final RepoViewHolder holder, final int position) {
        final RepositoryViewModel model = items.get(position);
        holder.bind(model);
        RxView.clicks(holder.itemView)
                .map(__ -> new Pair<>(holder.binding, model.id))
                .subscribe(clickSubject);
        Picasso.with(context)
                .load(model.avatar)
                .placeholder(R.drawable.ic_avatar_placeholder)
                .fit()
                .into(holder.binding.avatar);
    }

    @Override
    public int getItemCount() {
        return items.size() + (loading ? 1 : 0);
    }

    public void update(final Collection<RepositoryViewModel> repositories) {
        items.clear();
        items.addAll(repositories);
        notifyDataSetChanged();
    }

    public void append(final ListState state) {
        loading = state.loading;
        items.addAll(state.results);
        notifyDataSetChanged();
    }

    public void replace(final ListState state) {
        loading = state.loading;
        update(state.results);
    }

    final class RepoViewHolder extends RecyclerView.ViewHolder {
        private final ItemRepoBinding binding;

        private RepoViewHolder(final ItemRepoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        RepoViewHolder(final ViewGroup parent) {
            this(inflate(parent, R.layout.item_repo));
        }

        private void bind(final RepositoryViewModel model) {
            binding.setModel(new BoldQueryViewModel(model));
        }

    }

    final class SpinnerViewHolder extends RecyclerView.ViewHolder {
        SpinnerViewHolder(final ViewGroup parent) {
            super(inflate(parent, R.layout.item_spinner).getRoot());
        }
    }

}
