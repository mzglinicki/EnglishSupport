package com.project.mzglinicki.yourowndictionary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.project.mzglinicki.yourowndictionary.R;
import com.project.mzglinicki.yourowndictionary.fragments.MainFragment;
import com.project.mzglinicki.yourowndictionary.realmDb.RealmDbHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mzglinicki.96 on 27.08.2016.
 */
public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.addFAB)
    FloatingActionButton addFAB;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        RealmDbHelper.getInstance(this).createDefaultData(this);
        initToolbar();

        setupMainFragment();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
    }

    public void setToolbarTitle(final String toolbarTitle) {
        collapsingToolbar.setTitle(toolbarTitle);
    }

    private void setupMainFragment() {

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.placeHolder, new MainFragment());
        transaction.commit();
    }

    public void toggleFABVisibility() {
        addFAB.setVisibility(addFAB.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    public void setFABVisibility(boolean visibility) {
        addFAB.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.addFAB)
    public void onFABClick() {
        startActivity(new Intent(this, LessonCreator.class));
    }


//    @Override
//    public void onBackPressed() {
//        if (AppUtility.getInstance(this).close()) {
//            System.exit(0);
//        } else {
//            Toast.makeText(this, R.string.press_again_to_out, Toast.LENGTH_SHORT).show();
//        }
//    }
}