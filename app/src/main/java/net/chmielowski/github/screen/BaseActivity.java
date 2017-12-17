package net.chmielowski.github.screen;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity {
    private final CompositeDisposable disposables = new CompositeDisposable();

    @NonNull
    protected abstract Iterable<Disposable> disposables();

    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();
        disposables().forEach(disposables::add);
    }

    @CallSuper
    @Override
    protected void onPause() {
        disposables.clear();
        super.onPause();
    }
}
