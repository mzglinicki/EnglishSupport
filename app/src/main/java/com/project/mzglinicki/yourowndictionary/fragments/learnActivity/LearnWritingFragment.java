package com.project.mzglinicki.yourowndictionary.fragments.learnActivity;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.project.mzglinicki.yourowndictionary.AppUtility;
import com.project.mzglinicki.yourowndictionary.R;
import com.project.mzglinicki.yourowndictionary.adapters.WritingFragmentAdapter;
import com.project.mzglinicki.yourowndictionary.adapters.WritingFragmentViewHolder;
import com.project.mzglinicki.yourowndictionary.realmDb.WordDbModel;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mzglinicki.96 on 30.08.2016.
 */
public class LearnWritingFragment extends LearnFragmentParent implements WritingFragmentAdapter.OnClickListener {

    @Bind(R.id.writingFragmentRecycleView)
    RecyclerView recyclerView;
    @Bind(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;

    private int correctAnswer = 0;
    private WritingFragmentAdapter adapter;
    private AppUtility appUtilityManager;

    public LearnWritingFragment() {
        titleId = R.string.tab_practiceSpelling;
        layoutId = R.layout.fragment_writing_v2;
    }

    @Override
    public void init(final View view) {

        ButterKnife.bind(this, view);
        appUtilityManager = AppUtility.getInstance(getContext());
        adapter = new WritingFragmentAdapter(getContext(), listOfWords, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        final LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);

        setOnTouchListener(nestedScrollView);
    }

    private void setOnTouchListener(final View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                appUtilityManager.hideSoftInput(view);
                return true;
            }
        });
    }

    @Override
    public void setupCorrectAnswerAmount(final WritingFragmentViewHolder holder) {
        holder.getCorrectAnswerAmountTextView().setText(String.valueOf(correctAnswer));
    }

    @Override
    public void onSkipPreviousImageBtnClick(final WritingFragmentViewHolder holder) {
//        clearAppCompatEditTexts(holder);
        appUtilityManager.hideSoftInput(nestedScrollView);
        recyclerView.scrollToPosition(holder.getAdapterPosition() - 1);
    }

    @Override
    public void onSkipNextImageBtnClick(final WritingFragmentViewHolder holder) {
//        clearAppCompatEditTexts(holder);
        appUtilityManager.hideSoftInput(nestedScrollView);
        recyclerView.scrollToPosition(holder.getAdapterPosition() + 1);
    }

    @Override
    public void onViewClick(WritingFragmentViewHolder holder) {
        appUtilityManager.hideSoftInput(nestedScrollView);
        clearFocus(holder);
    }

    @Override
    public void onVolumeImageBtnClickListener(final WordDbModel model, final WritingFragmentViewHolder holder) {
        mediaManager.tellText(getContext(), model.getExampleSentence(), mediaManager.getEnglishSpeaker());
    }

    @Override
    public void onCheckAnswerClick(final WordDbModel model, final WritingFragmentViewHolder holder) {
        clearFocus(holder);
        appUtilityManager.hideSoftInput(nestedScrollView);
        final String englishWordUserInput = holder.getEnglishWordUserInput().getText().toString().toLowerCase();
        final String englishSentenceUserInput = holder.getEnglishSentenceUserInput().getText().toString().toLowerCase();
        final String englishWordFromDb = model.getTranslatedWord().toLowerCase();
        final String englishSentenceFromDb = model.getExampleSentence().toLowerCase();

        holder.getTranslatedWordEditText().setText(englishWordFromDb);
        holder.getTranslatedSentenceEditText().setText(englishSentenceFromDb);

        if (englishWordUserInput.equals(englishWordFromDb) && englishSentenceUserInput.equals(englishSentenceFromDb)) {
            correctAnswer = correctAnswer + 1;
            holder.getCorrectAnswerAmountTextView().setText(String.valueOf(correctAnswer));
            holder.getCheckAnswerTextView().setEnabled(false);
        }
    }

    private void clearAppCompatEditTexts(final WritingFragmentViewHolder holder) {
        for (final AppCompatEditText view : holder.getListToClear()) {
            view.setText("");
            view.clearFocus();
        }
    }

    private void clearFocus(final WritingFragmentViewHolder holder) {
        for (final AppCompatEditText appCompatEditText : holder.getListToClear()) {
            appCompatEditText.clearFocus();
        }
    }
}