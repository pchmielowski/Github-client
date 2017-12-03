package net.chmielowski.github.data;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestService {
    @GET("search/repositories")
    Single<Repositories> searchRepositories(@Query("q") String query);

    @GET("repos/{owner}/{repo}")
    Single<Repositories.Item> repo(@Path("owner") String id, @Path("repo") String repo);
}
