package com.project.mzglinicki.yourowndictionary.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.project.mzglinicki.yourowndictionary.fragments.learnActivity.LearnFragmentParent;

import java.util.List;

/**
 * Created by Marcin Zglinicki on 19.05.2016.
 */
public class LearnFragmentPagerAdapter extends FragmentPagerAdapter {

    private final List<LearnFragmentParent> listOfFragments;
    private final Context context;

    public LearnFragmentPagerAdapter(final Context context, final FragmentManager fm, final List<LearnFragmentParent> fragments) {
        super(fm);
        this.listOfFragments = fragments;
        this.context = context;
    }

    @Override
    public Fragment getItem(final int position) {
        return listOfFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        return context.getResources().getString(listOfFragments.get(position).getTitleId());
    }

    @Override
    public int getCount() {
        return listOfFragments.size();
    }
}
