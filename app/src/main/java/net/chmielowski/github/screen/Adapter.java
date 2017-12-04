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
public final class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ORDINARY_ELEMENT = 0;
    private static final int LAST_ELEMENT = 1;
    private final Context context;
    private final List<RepositoryViewModel> items = new ArrayList<>();

    @Inject
    Adapter(@ActivityContext final Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(final int position) {
        return isLastElement(position) ? LAST_ELEMENT : ORDINARY_ELEMENT;
    }

    private boolean isLastElement(final int position) {
        return position == items.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        if (viewType == LAST_ELEMENT) {
            return new RecyclerView.ViewHolder(inflate(parent, R.layout.item_last).getRoot()) {
            };
        }
        return new ViewHolder(inflate(parent, R.layout.item_repo));
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
        // TODO: refactor me
        if (!(holder instanceof ViewHolder)) {
            return;
        }
        final Adapter.ViewHolder casted = (ViewHolder) holder;
        final RepositoryViewModel model = items.get(position);
        casted.bind(model);
        RxView.clicks(holder.itemView)
                .map(__ -> new Pair<>(casted.binding, model.id))
                .subscribe(clickSubject);
        Picasso.with(context)
                .load(model.avatar)
                .placeholder(R.drawable.ic_avatar_placeholder)
                .fit()
                .into(casted.binding.avatar);

    }

    @Override
    public int getItemCount() {
        return items.size() + 1;
    }

    public void update(final Collection<RepositoryViewModel> repositories) {
        items.clear();
        items.addAll(repositories);
        notifyDataSetChanged();
    }

    public void append(final Collection<RepositoryViewModel> repositories) {
        final int size = items.size();
        items.addAll(repositories);
        notifyItemRangeInserted(size, repositories.size());
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
