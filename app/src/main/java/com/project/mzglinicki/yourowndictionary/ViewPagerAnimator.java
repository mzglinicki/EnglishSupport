package com.project.mzglinicki.yourowndictionary;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by marcin on 10.06.16.
 */
public class ViewPagerAnimator implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View page, float position) {
        final float normalizedPosition = Math.abs(Math.abs(position) - 1);
        page.setScaleX(normalizedPosition / 2 + 0.5f);
        page.setScaleY(normalizedPosition / 2 + 0.5f);
    }
}
