package net.chmielowski.github.screen.list;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;

import net.chmielowski.github.R;
import net.chmielowski.github.RepositoryViewModel;
import net.chmielowski.github.databinding.ItemRepoBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

final class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private final AppCompatActivity context;
    private final List<RepositoryViewModel> items = new ArrayList<>();

    // TODO: context
    @Inject
    Adapter(final AppCompatActivity context) {
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

    void update(final Collection<RepositoryViewModel> repositories) {
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
