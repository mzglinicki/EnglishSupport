package com.project.mzglinicki.yourowndictionary;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * Created by mzglinicki.96 on 11.08.2016.
 */
public class AppUtility {

    private static AppUtility utilityManager = null;
    private boolean doubleClickOnBackPressed = false;
    private Context context;

    private AppUtility(final Context context) {
        this.context = context;
    }

    public static AppUtility getInstance(final Context context) {
        if (utilityManager == null) {
            utilityManager = new AppUtility(context.getApplicationContext());
        }
        return utilityManager;
    }

    public void hideSoftInput(final View view) {
        final InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public boolean close() {
        if (doubleClickOnBackPressed) {
            return true;
        }
        this.doubleClickOnBackPressed = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleClickOnBackPressed = false;
            }
        }, 2000);
        return false;
    }
}
