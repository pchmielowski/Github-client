package net.chmielowski.github.screen.login;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.inputmethod.EditorInfo;

import com.jakewharton.rxbinding2.view.RxView;

import net.chmielowski.github.CustomApplication;
import net.chmielowski.github.R;
import net.chmielowski.github.databinding.ActivityLoginBinding;
import net.chmielowski.github.screen.BaseActivity;
import net.chmielowski.github.screen.search.SearchActivity;
import net.chmielowski.github.utils.Factory;

import java.util.Arrays;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;
import static com.jakewharton.rxbinding2.widget.RxTextView.editorActions;
import static io.reactivex.Observable.merge;


public final class LoginActivity extends BaseActivity {
    @Inject
    LoginViewModelFactory factory;

    private LoginViewModel model;
    private ActivityLoginBinding binding;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.get(this).activityComponent(this).inject(this);
        model = ViewModelProviders.of(this, new Factory(() -> factory.create()))
                .get(LoginViewModel.class);

        user.token().ifPresent(__ -> goToSearch());
        binding = DataBindingUtil
                .setContentView(this, R.layout.activity_login);
        binding.setModel(model);
    }

    @NonNull
    @Override
    protected Iterable<Disposable> disposables() {
        return Arrays.asList(
                RxView.clicks(binding.noLogin)
                        .subscribe(__ -> goToSearch()),
                merge(
                        editorActions(binding.password, action -> action == EditorInfo.IME_ACTION_SEND),
                        RxView.clicks(binding.login)
                ).flatMapSingle(__ -> model.login())
                        .subscribe(this::onLogin)
        );
    }

    private void onLogin(final Boolean success) {
        if (success) {
            goToSearch();
        } else {
            Snackbar.make(binding.getRoot(), "Login failed", LENGTH_SHORT).show();
        }
    }

    private void goToSearch() {
        startActivity(new Intent(this, SearchActivity.class));
        finish();
    }
}
