package net.chmielowski.github;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import net.chmielowski.github.databinding.ActivityDetailsBinding;

public class DetailsActivity extends Activity {
    static final String KEY_ID = "REPOSITORY_ID";

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityDetailsBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_details);
        binding.setModel(new DetailsViewModel(getIntent().getLongExtra(KEY_ID, 0)));
    }
}
