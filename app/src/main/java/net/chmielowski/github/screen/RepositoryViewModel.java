package net.chmielowski.github.screen;

import android.support.annotation.NonNull;

import net.chmielowski.github.data.RealmRepo;
import net.chmielowski.github.data.Repositories;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public final class RepositoryViewModel {
    private final String name;
    private final String query;
    public final String owner;
    public final String id;
    final String avatar;
    public final String language;

    private RepositoryViewModel(final String name,
                                final String query,
                                final String owner,
                                final String id,
                                final String avatar,
                                final String language) {
        this.name = name;
        this.query = query;
        this.owner = owner;
        this.id = id;
        this.avatar = avatar;
        this.language = language;
    }

    RepositoryViewModel(final Repositories.Item repo, final String query) {
        this(repo.name, query, repo.owner.login, repo.fullName, repo.owner.avatarUrl, repo.language);
    }

    public RepositoryViewModel(final RealmRepo repo) {
        this(repo.name, "", repo.owner, repo.id, repo.avatar, repo.language);
    }

    public FormattedText name() {
        return withQuerySelected(name, query);
    }

    @NonNull
    private static FormattedText withQuerySelected(final String name, final String query) {
        final String normalized = query.trim().toLowerCase();
        final int position = name.toLowerCase().indexOf(normalized); // TODO: find all occurrences
        return new FormattedText(name, position, position == -1 ? -1 : position + normalized.length());
    }

    @ToString
    @EqualsAndHashCode
    static final class FormattedText {
        final String text;
        final int start;
        final int end;

        FormattedText(final String text, final int start, final int end) {
            this.text = text;
            this.start = start;
            this.end = end;
        }
    }
}
