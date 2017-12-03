package net.chmielowski.github.screen;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;

import net.chmielowski.github.R;
import net.chmielowski.github.databinding.ItemRepoBinding;
import net.chmielowski.github.screen.details.DetailsActivity;

import javax.inject.Inject;

import static android.support.v4.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

public final class OpenDetails {

    private final AppCompatActivity activity;

    @Inject
    OpenDetails(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void invoke(final Pair<ItemRepoBinding, String> clickedItem) {
        final Intent intent = new Intent(activity, DetailsActivity.class);
        intent.putExtra(DetailsActivity.KEY_ID, clickedItem.second);
        final ActivityOptionsCompat options = makeSceneTransitionAnimation(
                activity,
                clickedItem.first.name,
                activity.getString(R.string.shared_element_transition));
        activity.startActivity(intent, options.toBundle());
    }
}
