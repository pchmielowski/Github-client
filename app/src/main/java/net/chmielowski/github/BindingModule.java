package net.chmielowski.github;

import net.chmielowski.github.screen.list.Adapter;
import net.chmielowski.github.screen.list.RepositoriesView;

import dagger.Binds;
import dagger.Module;

@Module
abstract class BindingModule {
    @Binds
    abstract RepositoriesView bindView(Adapter adapter);

}
