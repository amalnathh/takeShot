package com.deftech.takeshot;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.deftech.takeshot.ui.main.SectionsPagerAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class TabbedActivity extends AppCompatActivity {
    private TabLayout tabs;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        appBarLayout = findViewById(R.id.appbar);
        toolbar = findViewById(R.id.toolbar);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        setSupportActionBar(toolbar);

        viewPager.setCurrentItem(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void openSettings(MenuItem item) {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }
}