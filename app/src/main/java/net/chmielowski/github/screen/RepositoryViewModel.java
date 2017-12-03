package net.chmielowski.github.screen;

import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;

import net.chmielowski.github.data.RealmRepo;
import net.chmielowski.github.data.Repositories;

public final class RepositoryViewModel {
    public final SpannableStringBuilder name;
    public final String owner;
    public final String id;
    final String avatar;
    public final String language;

    private RepositoryViewModel(final SpannableStringBuilder name,
                                final String owner,
                                final String id,
                                final String avatar,
                                final String language) {
        this.name = name;
        this.owner = owner;
        this.id = id;
        this.avatar = avatar;
        this.language = language;
    }

    RepositoryViewModel(final Repositories.Item repo, final String query) {
        this(withQueryInBold(repo.name, query), repo.owner.login, repo.fullName,
                repo.owner.avatarUrl, repo.language);
    }

    public RepositoryViewModel(final Repositories.Item repo) {
        this(repo, "");
    }

    public RepositoryViewModel(final RealmRepo repo) {
        this(SpannableStringBuilder.valueOf(repo.name), repo.owner, repo.id, repo.avatar,
                repo.language);
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
