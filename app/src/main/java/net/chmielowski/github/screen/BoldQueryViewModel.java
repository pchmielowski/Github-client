package net.chmielowski.github.screen;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;

// TODO: find better name
public final class BoldQueryViewModel {

    private final RepositoryViewModel model;

    public SpannableStringBuilder name() {
        final RepositoryViewModel.FormattedText name = model.name();

        final SpannableStringBuilder builder = new SpannableStringBuilder(name.text);
        if (name.start != -1) {
            builder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                    name.start, name.end,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return builder;
    }

    public String owner() {
        return model.owner;
    }

    public String id() {
        return model.id;
    }

    public String avatar() {
        return model.avatar;
    }

    public String language() {
        return model.language;
    }

    BoldQueryViewModel(final RepositoryViewModel model) {this.model = model;}
}
