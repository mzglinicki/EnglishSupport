package com.project.mzglinicki.yourowndictionary.fragments.learnActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.project.mzglinicki.yourowndictionary.Constants;
import com.project.mzglinicki.yourowndictionary.FicheModel;
import com.project.mzglinicki.yourowndictionary.MediaManager;
import com.project.mzglinicki.yourowndictionary.ViewPagerAnimator;
import com.project.mzglinicki.yourowndictionary.realmDb.RealmDbHelper;
import com.project.mzglinicki.yourowndictionary.realmDb.WordDbModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.realm.OrderedRealmCollection;

/**
 * Created by Marcin Zglinicki on 19.05.2016.
 * Parent for LearnListFragment, LearnFicheFragment, LearnWritingFragment
 */
public abstract class LearnFragmentParent extends Fragment {

    protected int titleId;
    protected int layoutId;
    protected RealmDbHelper dbHelper;
    protected OrderedRealmCollection<WordDbModel> listOfWords;
    protected MediaManager mediaManager;
    protected int lessonId;
    protected List<FicheModel> listOfFiche;


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(layoutId, container, false);

        getStringFromBundle();
        mediaManager = MediaManager.getInstance(getContext());
        dbHelper = RealmDbHelper.getInstance(getContext());
        listOfWords = dbHelper.getLessonWords(lessonId);

        init(view);
        return view;
    }

    private void getStringFromBundle() {
        lessonId = getActivity().getIntent().getIntExtra(Constants.LESSON_ID_KEY, 0);
    }

    public int getTitleId() {
        return titleId;
    }

    protected abstract void init(final View view);
}