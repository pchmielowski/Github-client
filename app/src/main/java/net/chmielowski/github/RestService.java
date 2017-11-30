package net.chmielowski.github;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface RestService {
    @GET("search/repositories")
    Single<Repositories> searchRepositories(@SuppressWarnings("SameParameterValue") @Query("q") String query);
}
