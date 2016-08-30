package com.project.mzglinicki.yourowndictionary;

import android.os.AsyncTask;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

/**
 * Created by Marcin Zglinicki on 30.05.16.
 */
public class DictionarySupport extends AsyncTask<String, Void, String> {

    private OnCompleteListener listener;
    private String translateResult;
    private Language translateLanguage;

    public interface OnCompleteListener {
        Language setupTranslateLanguage();

        void onTranslateComplete(final String translatedWord);
    }

    public DictionarySupport(final OnCompleteListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        translateLanguage = listener.setupTranslateLanguage();
    }

    @Override
    protected String doInBackground(final String... params) {

        try {
            translateResult = Translate.execute(params[0], translateLanguage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return translateResult;
    }

    @Override
    protected void onPostExecute(final String strings) {
        super.onPostExecute(strings);
        listener.onTranslateComplete(strings);
    }
}
