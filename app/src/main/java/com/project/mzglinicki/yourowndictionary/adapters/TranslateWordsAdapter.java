package com.project.mzglinicki.yourowndictionary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.mzglinicki.yourowndictionary.Card;
import com.project.mzglinicki.yourowndictionary.R;
import com.project.mzglinicki.yourowndictionary.TranslatedWord;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mzglinicki.96 on 29.08.2016.
 */
public class TranslateWordsAdapter extends ArrayAdapter<TranslatedWord> {

    @Bind(R.id.translatedWord)
    TextView translatedWord;
    @Bind(R.id.exampleSentence)
    TextView exampleSentence;
    @Bind(R.id.volumeImageBtn)
    ImageButton volumeImageBtn;
    @Bind(R.id.addImageButton)
    ImageButton addImageButton;

    private final LayoutInflater inflater;
    private final List<TranslatedWord> translatedWords;
    private final OnClickListener onClickListener;

    public TranslateWordsAdapter(final Context context, final List<TranslatedWord> translatedWords, final OnClickListener onClickListener) {
        super(context, -1);
        this.inflater = LayoutInflater.from(context);
        this.translatedWords = translatedWords;
        this.onClickListener = onClickListener;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final TranslatedWord word = translatedWords.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.dictionary_translation_model, parent, false);
            ButterKnife.bind(this, convertView);
            convertView.setTag("tag");
        } else {
            convertView.getTag();
        }
        translatedWord.setText(word.getTranslatedWord());
        if (word.getExampleSentence() == null) {
            exampleSentence.setVisibility(View.GONE);
        } else {
            exampleSentence.setText(word.getExampleSentence());
        }
        setupImageClickListener(addImageButton, word);
        setupImageClickListener(volumeImageBtn, word);

        return convertView;
    }

    private void setupImageClickListener(final ImageButton imageButton, final TranslatedWord word) {
        if (onClickListener == null) {
            return;
        }
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                switch (imageButton.getId()) {
                    case R.id.volumeImageBtn:
                        onClickListener.onVolumeBtnClick(word);
                        break;
                    case R.id.addImageButton:
                        onClickListener.onAddBtnClick(word);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public TranslatedWord getItem(final int position) {
        return translatedWords.get(position);
    }

    @Override
    public int getCount() {
        return translatedWords.size();
    }

    public interface OnClickListener {
        void onVolumeBtnClick(final TranslatedWord word);

        void onAddBtnClick(final TranslatedWord word);
    }
}