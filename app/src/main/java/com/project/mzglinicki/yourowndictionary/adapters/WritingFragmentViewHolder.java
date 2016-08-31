package com.project.mzglinicki.yourowndictionary.adapters;

import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.project.mzglinicki.yourowndictionary.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mzglinicki.96 on 30.08.2016.
 */
public class WritingFragmentViewHolder extends RecyclerView.ViewHolder {

//    @Bind(R.id.nestedScrollView)
//    NestedScrollView nestedScrollView;
    @Bind(R.id.polishWord)
    TextView polishWordTextView;
    @Bind(R.id.englishWordLayout)
    TextInputLayout englishWordLayout;
    @Bind(R.id.englishWordUserInput)
    AppCompatEditText englishWordUserInput;
    @Bind(R.id.englishSentenceLayout)
    TextInputLayout englishSentenceLayout;
    @Bind(R.id.englishSentenceUserInput)
    AppCompatEditText englishSentenceUserInput;
    @Bind(R.id.readSentenceImageBtn)
    ImageButton readSentenceImageBtn;
    @Bind(R.id.skipPreviousImageBtn)
    ImageButton skipPreviousImageBtn;
    @Bind(R.id.checkAnswerTextView)
    TextView checkAnswerTextView;
    @Bind(R.id.skipNextImageBtn)
    ImageButton skipNextImageBtn;
    @Bind(R.id.translatedWordTextView)
    TextInputLayout translatedWordTextView;
    @Bind(R.id.translatedWordEditText)
    EditText translatedWordEditText;
    @Bind(R.id.translatedSentenceLayout)
    TextInputLayout translatedSentenceLayout;
    @Bind(R.id.translatedSentenceTextView)
    EditText translatedSentenceTextView;
    @Bind(R.id.correctAnswerAmountTextView)
    TextView correctAnswerAmountTextView;
    @Bind(R.id.currentItemPositionTextView)
    TextView currentItemPositionTextView;
    @Bind(R.id.wordAmountTextView)
    TextView wordAmountTextView;
    @Bind({R.id.englishWordUserInput, R.id.englishSentenceUserInput, R.id.translatedWordEditText, R.id.translatedSentenceTextView})
    List<AppCompatEditText> listToClear;

    public WritingFragmentViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public AppCompatEditText getEnglishSentenceUserInput() {
        return englishSentenceUserInput;
    }

    public ImageButton getReadSentenceImageBtn() {
        return readSentenceImageBtn;
    }

    public ImageButton getSkipPreviousImageBtn() {
        return skipPreviousImageBtn;
    }

    public TextView getCheckAnswerTextView() {
        return checkAnswerTextView;
    }

    public ImageButton getSkipNextImageBtn() {
        return skipNextImageBtn;
    }

    public EditText getTranslatedWordEditText() {
        return translatedWordEditText;
    }

    public EditText getTranslatedSentenceEditText() {
        return translatedSentenceTextView;
    }

    public TextView getCorrectAnswerAmountTextView() {
        return correctAnswerAmountTextView;
    }

    public TextView getCurrentItemPositionTextView() {
        return currentItemPositionTextView;
    }

    public TextView getWordAmountTextView() {
        return wordAmountTextView;
    }

    public TextView getPolishWordTextView() {
        return polishWordTextView;
    }

    public AppCompatEditText getEnglishWordUserInput() {
        return englishWordUserInput;
    }

    public List<AppCompatEditText> getListToClear() {
        return listToClear;
    }

//    public NestedScrollView getNestedScrollView() {
//        return nestedScrollView;
//    }
}
