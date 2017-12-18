package net.chmielowski.github.screen.login;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import com.jakewharton.rxbinding2.view.RxView;

import net.chmielowski.github.CustomApplication;
import net.chmielowski.github.R;
import net.chmielowski.github.databinding.ActivityLoginBinding;
import net.chmielowski.github.screen.BaseActivity;
import net.chmielowski.github.screen.search.SearchActivity;

import java.util.Collections;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;


public final class LoginActivity extends BaseActivity {
    @Inject
    LoginViewModel model;
    private ActivityLoginBinding binding;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CustomApplication.get(this).activityComponent(this).inject(this);
        binding = DataBindingUtil
                .setContentView(this, R.layout.activity_login);
        binding.setModel(model);
    }

    @NonNull
    @Override
    protected Iterable<Disposable> disposables() {
        return Collections.singletonList(
                RxView.clicks(binding.login)
                        .flatMapSingle(__ -> model.login())
                        .subscribe(success -> {
                            if (success) {
                                startActivity(new Intent(this, SearchActivity.class));
                                finish();
                            } else {
                                Snackbar.make(binding.getRoot(), "Login failed", LENGTH_SHORT).show();
                            }
                        })
        );
    }
}
