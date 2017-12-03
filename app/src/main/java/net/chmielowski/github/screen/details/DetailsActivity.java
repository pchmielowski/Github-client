package net.chmielowski.github.screen.details;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import net.chmielowski.github.CustomApplication;
import net.chmielowski.github.R;
import net.chmielowski.github.databinding.ActivityDetailsBinding;

import javax.inject.Inject;

public class DetailsActivity extends AppCompatActivity {
    public static final String KEY_ID = "REPOSITORY_ID";

    @Inject
    DetailsViewModel model;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CustomApplication) getApplication()).component(this).inject(this);
        model.setRepo(getIntent().getStringExtra(KEY_ID));

        final ActivityDetailsBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_details);
        binding.setModel(model);
    }
}
