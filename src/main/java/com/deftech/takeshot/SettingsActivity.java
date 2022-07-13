package com.deftech.takeshot;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            Preference about = findPreference("about");
            Preference contact = findPreference("contact");
            assert about != null;
            about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent i = new Intent(getContext(), AboutActivity.class);
                    startActivity(i);

                    return false;
                }
            });
            assert contact != null;
            contact.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent in = new Intent(Intent.ACTION_SEND);
                    in.putExtra(Intent.EXTRA_EMAIL, "contactusdeftech@gmail.com");
                    in.putExtra(Intent.EXTRA_SUBJECT, "TakeShot Review Mail");
                    startActivity(Intent.createChooser(in, "Send us a mail"));
                    return false;
                }
            });


        }
    }
}