package net.chmielowski.github;

import dagger.Subcomponent;

@Subcomponent
interface RepositoryComponent {

    DetailsActivityComponent.Builder plusDetailsActivityComponent();
}
