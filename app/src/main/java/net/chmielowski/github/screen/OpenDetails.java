package net.chmielowski.github.screen;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;

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

    public void invoke(final android.util.Pair<ItemRepoBinding, String> clickedItem) {
        final Intent intent = new Intent(activity, DetailsActivity.class);
        intent.putExtra(DetailsActivity.KEY_ID, clickedItem.second);

        @SuppressWarnings("unchecked") final ActivityOptionsCompat options = makeSceneTransitionAnimation(
                activity,
                Pair.create(clickedItem.first.name, activity.getString(R.string.transition_name)),
                Pair.create(clickedItem.first.avatar, activity.getString(R.string.transition_avatar)),
                Pair.create(clickedItem.first.owner, activity.getString(R.string.transition_owner))
        );
        activity.startActivity(intent, options.toBundle());
    }
}
