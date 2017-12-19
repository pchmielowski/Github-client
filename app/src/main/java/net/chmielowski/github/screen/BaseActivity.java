package net.chmielowski.github.screen;

import android.app.AlertDialog;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import net.chmielowski.github.R;
import net.chmielowski.github.data.RequestLimit;
import net.chmielowski.github.data.User;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity {
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    RequestLimit limit;

    @Inject
    protected User user;

    @NonNull
    protected abstract Iterable<Disposable> disposables();

    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();
        disposables.add(limit.observe()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(__ -> notifyUser()));
        disposables().forEach(disposables::add);
    }

    private AlertDialog notifyUser() {
        return new AlertDialog.Builder(this)
                .setTitle(R.string.msg_request_limit_reached)
                .setMessage(message())
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private int message() {
        return user.token()
                .map(__ -> R.string.msg_log_in)
                .orElse(R.string.msg_wait);
    }

    @CallSuper
    @Override
    protected void onPause() {
        disposables.clear();
        super.onPause();
    }
}
