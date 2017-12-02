package net.chmielowski.github;

import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;

public final class RepositoryViewModel {
    public final SpannableStringBuilder name;
    public final long id;

    public RepositoryViewModel(final Repositories.Item repo, String query) {
        this.name = withQueryInBold(repo.fullName, query);
        this.id = repo.id;
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

    public RepositoryViewModel(Repositories.Item repo) {
        this(repo, "");
    }
}
