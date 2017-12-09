package net.chmielowski.github;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

public final class CustomApplication extends Application {

    /*
     * TODO:
     *  pagination
     *  pull to refresh
     *  collapsing action bar on details screen
     *  removing from favourites
     *  "no results"
     *  more info on details
     *
     *  tests
     */

    private MainComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerMainComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
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

    public ActivityComponent activityComponent(final AppCompatActivity activity) {
        return component.plusActivityComponent(new ActivityModule(activity));
    }

    MainComponent mainComponent() {
        return component;
    }
}
