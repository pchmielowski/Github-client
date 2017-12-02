package net.chmielowski.github;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import net.chmielowski.github.screen.list.Adapter;
import net.chmielowski.github.screen.list.ListViewModel;

import dagger.Module;
import dagger.Provides;

@Module
final class ActivityModule {
    @NonNull
    private final AppCompatActivity activity;

    ActivityModule(@NonNull final AppCompatActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Provides
    AppCompatActivity provideActivity() {
        return activity;
    }

    /*@ActivityContext*/
    @NonNull
    @Provides
    Context provideContext() {
        return activity;
    }

    @Provides
    ListViewModel provideListViewModel(ReposRepository service, Adapter adapter) {
        return new ListViewModel(service, adapter);
    }

    @Provides
    Adapter provideAdapter(Context context) {
        return new Adapter(context);
    }
}
