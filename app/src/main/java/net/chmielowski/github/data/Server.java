package net.chmielowski.github.data;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Server {
    @GET("search/repositories")
    Single<Response<Repositories>> find(
            @Query("q") String query,
            @Query("page") int page);

    @GET("repos/{owner}/{repository}")
    Single<Response<Repositories.Item>> repository(
            @Path("owner") String owner,
            @Path("repository") String name);

    @GET("/")
    Single<Response<Void>> root();
}
