package net.chmielowski.github;

public class DetailsViewModel {
    public final String name;

    DetailsViewModel(final long id) {
        name = String.valueOf(id);
    }
}
