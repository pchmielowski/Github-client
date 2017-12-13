package net.chmielowski.github;

import dagger.Subcomponent;

@RepositoryScope
@Subcomponent(modules = RepositoryModule.class)
interface RepositoryComponent {

    DetailsActivityComponent plusDetailsActivityComponent(ActivityModule module);
}
