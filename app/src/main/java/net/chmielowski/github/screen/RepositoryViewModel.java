package net.chmielowski.github.screen;

import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;

import net.chmielowski.github.data.Repositories;

public final class RepositoryViewModel {
    public final SpannableStringBuilder name;
    public final String owner;
    public final String id;

    private RepositoryViewModel(final SpannableStringBuilder name, String owner, final String id) {
        this.name = name;
        this.owner = owner;
        this.id = id;
    }

    RepositoryViewModel(final Repositories.Item repo, final String query) {
        this(withQueryInBold(repo.name, query), repo.owner.login, repo.fullName);
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
