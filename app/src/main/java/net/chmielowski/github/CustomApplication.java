package net.chmielowski.github;

import android.app.Application;
import android.app.Service;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

public class CustomApplication extends Application {

    private MainComponent component;
    @Nullable
    private RepositoryComponent repositoryComponent;

    public static CustomApplication get(final Service service) {
        return (CustomApplication) service.getApplication();
    }

    public static CustomApplication get(final AppCompatActivity activity) {
        return (CustomApplication) activity.getApplication();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerMainComponent
                .builder()
                .application(this)
                .build();
        if (isTest()) {
            return;
        }
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
        Realm.init(this);
        Realm.setDefaultConfiguration(
                new RealmConfiguration.Builder()
                        .deleteRealmIfMigrationNeeded()
                        .build());
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());
        Timber.plant(new Timber.DebugTree());
    }

    @SuppressWarnings("WeakerAccess")
    protected boolean isTest() {
        return false;
    }

    public DetailsActivityComponent activityComponent(final AppCompatActivity activity,
                                                      final boolean createNew) {
        if (createNew) {
            repositoryComponent = component
                    .plusRepositoryComponent();
        }
        assert repositoryComponent != null;
        return repositoryComponent
                .plusDetailsActivityComponent()
                .activity(activity)
                .build();
    }

    public ActivityComponent activityComponent(final AppCompatActivity activity) {
        return component
                .plusActivityComponent()
                .activity(activity)
                .build();
    }

    public MainComponent mainComponent() {
        return component;
    }

    public void releaseRepositoryComponent() {
        repositoryComponent = null;
    }
}
