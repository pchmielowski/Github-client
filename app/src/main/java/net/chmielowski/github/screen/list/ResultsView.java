package net.chmielowski.github.screen.list;


import net.chmielowski.github.RepositoryViewModel;

import java.util.Collection;

public interface ResultsView {
    void update(Collection<RepositoryViewModel> repositories);
}
