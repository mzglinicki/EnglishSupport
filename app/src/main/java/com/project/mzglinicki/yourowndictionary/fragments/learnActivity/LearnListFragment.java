package com.project.mzglinicki.yourowndictionary.fragments.learnActivity;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.project.mzglinicki.yourowndictionary.R;
import com.project.mzglinicki.yourowndictionary.adapters.WordsListRecycleAdapter;
import com.project.mzglinicki.yourowndictionary.realmDb.WordDbModel;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Marcin Zglinicki on 19.05.2016.
 * Part of LearnActivity.
 */
public class LearnListFragment extends LearnFragmentParent implements WordsListRecycleAdapter.OnClickLister {

    @Bind(R.id.wordListRecycleView)
    RecyclerView wordsRecycleView;
    @Bind(R.id.nativeLanguageField)
    TextView nativeLanguageField;
    @Bind(R.id.translateLanguageField)
    TextView translateLanguageField;

    public LearnListFragment() {
        titleId = R.string.tab_list;
        layoutId = R.layout.word_list_fragment;
    }

    @Override
    public void init(final View view) {

        ButterKnife.bind(this, view);
        setupRecycleView();
    }

    private void setupRecycleView() {
        wordsRecycleView.setHasFixedSize(true);
        wordsRecycleView.setAdapter(new WordsListRecycleAdapter(getContext(), listOfWords, this));
        wordsRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onClick(final View imageButton, final WordDbModel model, final int position) {

        final TextToSpeech polishSpeaker = mediaManager.getPolishSpeaker();
        final TextToSpeech englishSpeaker = mediaManager.getEnglishSpeaker();
        mediaManager.tellText(getContext(), model.getNativeWord(), polishSpeaker);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaManager.tellText(getContext(), model.getTranslatedWord(), englishSpeaker);
            }
        }, 1000);
    }

    @Override
    public void onLongClick(final WordDbModel model) {
    }
}
