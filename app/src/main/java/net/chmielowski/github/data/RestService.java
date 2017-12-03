package net.chmielowski.github.data;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestService {
    @GET("search/repositories")
    Single<Repositories> searchRepositories(@SuppressWarnings("SameParameterValue") @Query("q") String query);
}
