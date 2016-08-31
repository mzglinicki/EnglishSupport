package com.project.mzglinicki.yourowndictionary.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.project.mzglinicki.yourowndictionary.Constants;
import com.project.mzglinicki.yourowndictionary.R;
import com.project.mzglinicki.yourowndictionary.ViewPagerAnimator;
import com.project.mzglinicki.yourowndictionary.adapters.LearnFragmentPagerAdapter;
import com.project.mzglinicki.yourowndictionary.fragments.learnActivity.LearnFicheFragment;
import com.project.mzglinicki.yourowndictionary.fragments.learnActivity.LearnFragmentParent;
import com.project.mzglinicki.yourowndictionary.fragments.learnActivity.LearnListFragment;
import com.project.mzglinicki.yourowndictionary.fragments.learnActivity.LearnWritingFragment;
import com.project.mzglinicki.yourowndictionary.realmDb.RealmDbHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mzglinicki.96 on 29.08.2016.
 */
public class LearnActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.collapsingToolbarImage)
    ImageView collapsingToolbarImage;
    @Bind(R.id.viewpager)
    ViewPager viewPager;

    private int lessonId;
    private RealmDbHelper dbHelper;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        ButterKnife.bind(this);

        dbHelper = RealmDbHelper.getInstance(this);
        lessonId = getIntent().getIntExtra(Constants.LESSON_ID_KEY, 0);
        initToolbar(dbHelper.getLesson(lessonId).getLessonImageResId());
        setupUI();
    }

    private void initToolbar(final int lessonImageResId) {
        collapsingToolbarImage.setImageResource(lessonImageResId);
    }

    public void setupUI() {

        viewPager.setAdapter(new LearnFragmentPagerAdapter(this, getSupportFragmentManager(), createFragments()));
        viewPager.setPageTransformer(false, new ViewPagerAnimator());

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setViewPagerClickable(boolean clickable) {
        viewPager.setClickable(clickable);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    public List<LearnFragmentParent> createFragments() {

        final List<LearnFragmentParent> listOfFragments = new ArrayList<>();
        listOfFragments.add(new LearnListFragment());
        listOfFragments.add(new LearnFicheFragment());
        listOfFragments.add(new LearnWritingFragment());
        return listOfFragments;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);
    }
}
