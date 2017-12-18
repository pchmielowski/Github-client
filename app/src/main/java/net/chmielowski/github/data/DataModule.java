package net.chmielowski.github.data;

import android.support.annotation.NonNull;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import net.chmielowski.github.screen.PersistentQueryHistory;
import net.chmielowski.github.screen.QueryHistory;
import net.chmielowski.github.screen.search.RealmFacade;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

@Module
public abstract class DataModule {
    @RepositoryDataSource.WorkOnBackground
    @Binds
    abstract RepositoryDataSource bindRepoService(WorkingOnBackgroundRepoService impl);

    @RepositoryDataSource.Github
    @Binds
    abstract RepositoryDataSource bindRepoRepository(WebRepositoryDataSource impl);

    @Binds
    abstract QueryHistory bindQueryHistory(PersistentQueryHistory impl);

    @Binds
    abstract Persistence bindRealmFacade(RealmFacade impl);

    @Binds
    abstract Favourites bindFavouriteRepos(PersistentFavourites impl);

    @Provides
    @Singleton
    @NonNull
    static Server provideRestService(final Retrofit retrofit) {
        return retrofit.create(Server.class);
    }

    @Provides
    @Singleton
    @NonNull
    static Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder()
                                .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES)
                                .create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(new OkHttpClient.Builder()
                        .addInterceptor(new HttpLoggingInterceptor()
                                .setLevel(HttpLoggingInterceptor.Level.BODY))
                        .addNetworkInterceptor(new StethoInterceptor())
                        .build())
                .build();
    }


    @Provides
    @Singleton
    @NonNull
    static Map<String, Repositories.Item> provideCache() {
        return new HashMap<>();
    }

}
