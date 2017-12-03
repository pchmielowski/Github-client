package net.chmielowski.github.screen;

import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;

import net.chmielowski.github.data.RealmRepo;
import net.chmielowski.github.data.Repositories;

public final class RepositoryViewModel {
    public final SpannableStringBuilder name;
    public final long id;

    private RepositoryViewModel(final SpannableStringBuilder name, final long id) {
        this.name = name;
        this.id = id;
    }

    RepositoryViewModel(final Repositories.Item repo, final String query) {
        this(withQueryInBold(repo.fullName, query), repo.id);
    }

    public RepositoryViewModel(final RealmRepo realmModel) {
        this(SpannableStringBuilder.valueOf(realmModel.name), realmModel.id);
    }

    public RepositoryViewModel(final Repositories.Item repo) {
        this(repo, "");
    }

    @NonNull
    private static SpannableStringBuilder withQueryInBold(final String name, final String query) {
        final SpannableStringBuilder builder = new SpannableStringBuilder(name);
        final String normalized = query.trim().toLowerCase();
        final int position = name.toLowerCase().indexOf(normalized); // TODO: find all occurrences
        if (position != -1) {
            builder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                    position, position + normalized.length(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return builder;
    }
}
