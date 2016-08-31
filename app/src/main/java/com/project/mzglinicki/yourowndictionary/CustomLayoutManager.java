package com.project.mzglinicki.yourowndictionary;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * Created by mzglinicki.96 on 31.08.2016.
 */
public class CustomLayoutManager extends LinearLayoutManager {

    private static final float MILLISECONDS_PER_INCH = 80f;
    private static final float MANUAL_SCROLL_SLOW_RATIO = 1.5f;
    private final Context context;

    public CustomLayoutManager(final Context context) {
        super(context, LinearLayoutManager.VERTICAL, false);
        this.context = context;
    }

    @Override
    public void smoothScrollToPosition(final RecyclerView recyclerView, final RecyclerView.State state, final int position) {

        final LinearSmoothScroller smoothScroller = new LinearSmoothScroller(context) {

            //This controls the direction in which smoothScroll looks for your view
            @Override
            public PointF computeScrollVectorForPosition
            (int targetPosition) {
                return CustomLayoutManager.this
                        .computeScrollVectorForPosition(targetPosition);
            }

            //This returns the milliseconds it takes to scroll one pixel.
            @Override
            protected float calculateSpeedPerPixel
            (DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
            }
        };
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

    @Override
    public int scrollVerticallyBy(int delta, RecyclerView.Recycler recycler, RecyclerView.State state) {
        // write your limiting logic here to prevent the delta from exceeding the limits of your list.
//        int prevDelta = delta;
//        if (getScrollState() == SCROLL_STATE_DRAGGING)
        delta = (int) (delta > 0 ? Math.max(delta * MANUAL_SCROLL_SLOW_RATIO, 1) : Math.min(delta * MANUAL_SCROLL_SLOW_RATIO, -1));

        // MANUAL_SCROLL_SLOW_RATIO is between 0 (no manual scrolling) to 1 (normal speed) or more (faster speed).
        // write your scrolling logic code here whereby you move each view by the given delta

//        if (getScrollState() == SCROLL_STATE_DRAGGING)
//            delta = prevDelta;

        return delta;
    }


}