package net.chmielowski.github.data;

import io.reactivex.Single;

public interface LoginService {
    Single<Boolean> login(String user, String password);
}
