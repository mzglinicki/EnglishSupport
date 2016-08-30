package com.project.mzglinicki.yourowndictionary.application;

import com.memetix.mst.translate.Translate;
import com.project.mzglinicki.yourowndictionary.Constants;

/**
 * Created by Marcin Zglinicki on 30.05.16.
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Translate.setClientId(Constants.CLIENT_ID);
        Translate.setClientSecret(Constants.CLIENT_SECRET);
    }
}