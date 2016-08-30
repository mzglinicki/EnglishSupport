package com.project.mzglinicki.yourowndictionary.fragments;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.memetix.mst.language.Language;
import com.project.mzglinicki.yourowndictionary.AppUtility;
import com.project.mzglinicki.yourowndictionary.DictionarySupport;
import com.project.mzglinicki.yourowndictionary.MediaManager;
import com.project.mzglinicki.yourowndictionary.R;
import com.project.mzglinicki.yourowndictionary.TranslatedWord;
import com.project.mzglinicki.yourowndictionary.activities.MainActivity;
import com.project.mzglinicki.yourowndictionary.adapters.TranslateWordsAdapter;
import com.project.mzglinicki.yourowndictionary.realmDb.RealmDbHelper;
import com.project.mzglinicki.yourowndictionary.realmDb.WordDbModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mzglinicki.96 on 27.08.2016.
 */
public class DictionaryFragment extends Fragment implements TranslateWordsAdapter.OnClickListener {

    @Bind(R.id.dictionaryLayout)
    LinearLayout dictionaryLayout;
    @Bind(R.id.btnBing)
    Button btnBing;
    @Bind(R.id.btnMyDictionary)
    Button btnMyDictionary;
    @Bind(R.id.nativeInput)
    AppCompatEditText nativeInput;
    @Bind(R.id.nativeInputLayout)
    TextInputLayout nativeInputLayout;
    @Bind(R.id.nativeLanguageField)
    TextView nativeLanguageField;
    @Bind(R.id.translateLanguageField)
    TextView translateLanguageField;
    @Bind(R.id.translatedWordListView)
    ListView translatedWordListView;

    private RealmDbHelper dbHelper;
    private boolean polishNativeLanguage = true;
    private List<TranslatedWord> translatedWords;
    private TranslateWordsAdapter wordsAdapter;
    private MediaManager mediaManager;
    private AppUtility appManager;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_dictionary, container, false);
        ButterKnife.bind(this, view);

        dbHelper = RealmDbHelper.getInstance(getContext());
        mediaManager = MediaManager.getInstance(getContext());
        appManager = AppUtility.getInstance(getContext());
        translatedWords = new ArrayList<>();
        wordsAdapter = new TranslateWordsAdapter(getContext(), translatedWords, this);
        translatedWordListView.setAdapter(wordsAdapter);

        setOnTouchListener(dictionaryLayout);

        return view;
    }

    private void setOnTouchListener(final View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                nativeInput.clearFocus();
                return true;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).setToolbarTitle("Słownik");
        ((MainActivity) getActivity()).setFABVisibility(false);
    }

    @OnClick(R.id.btnBing)
    public void onBingBtnClick() {
        translatedWords.clear();
        wordsAdapter.notifyDataSetChanged();
        translateWithBing();
        doAfterTranslate();
    }

    @OnClick(R.id.btnMyDictionary)
    public void onMyDictionaryBtnClick() {
        translatedWords.clear();
        wordsAdapter.notifyDataSetChanged();
        translateWithApp();
        doAfterTranslate();
    }

    private void doAfterTranslate() {
        nativeInputLayout.clearFocus();
        nativeInput.clearFocus();
        appManager.hideSoftInput(dictionaryLayout);
    }

    private void translateWithApp() {

        List<WordDbModel> translatedWords = null;
        try {
            if (polishNativeLanguage) {
                translatedWords = dbHelper.getTranslationFromPolish(getUserInput());
            } else {
                translatedWords = dbHelper.getTranslationFromEnglish(getUserInput());
            }
        } catch (final IllegalArgumentException wordNotFound) {
            setupListForAdapter(null, null);
        }
        setupListForAdapter(translatedWords, null);
    }

    private void setupListForAdapter(@Nullable final List<WordDbModel> translatedWords, @Nullable String wordFromBing) {

        if ((translatedWords == null && wordFromBing == null) || (translatedWords != null && translatedWords.isEmpty())) {
            Toast.makeText(getContext(), "Nie wiem", Toast.LENGTH_SHORT).show();
        } else if (translatedWords != null) {
            for (final WordDbModel wordDbModel : translatedWords) {
                final String translatedWord = polishNativeLanguage ? wordDbModel.getTranslatedWord() : wordDbModel.getNativeWord();
                final String exampleSentence = polishNativeLanguage ? wordDbModel.getExampleSentence() : null;
                this.translatedWords.add(new TranslatedWord(translatedWord, exampleSentence));
                wordsAdapter.notifyDataSetChanged();
            }
        } else {
            final String exampleSentence = null;
            final TranslatedWord word = new TranslatedWord(wordFromBing, exampleSentence);
            this.translatedWords.add(word);
        }
        wordsAdapter.notifyDataSetChanged();
    }

    private void translateWithBing() {

        final DictionarySupport dictionarySupport = new DictionarySupport(new DictionarySupport.OnCompleteListener() {
            @Override
            public Language setupTranslateLanguage() {
                if (polishNativeLanguage) {
                    return Language.ENGLISH;
                } else {
                    return Language.POLISH;
                }
            }

            @Override
            public void onTranslateComplete(final String translatedWord) {
                setupListForAdapter(null, translatedWord);
            }
        });
        dictionarySupport.execute(getUserInput());
    }

    private String getUserInput() {
        return nativeInput.getText().toString().toLowerCase();
    }

    @OnClick(R.id.btnChangeTransLanguage)
    public void onChangeTransLanguageBtnClick() {
        final String previousNativeLanguage = nativeLanguageField.getText().toString();
        final String previousTranslateLanguage = translateLanguageField.getText().toString();
        nativeLanguageField.setText(previousTranslateLanguage);
        translateLanguageField.setText(previousNativeLanguage);
        nativeInputLayout.setHint(previousTranslateLanguage);
        polishNativeLanguage = !polishNativeLanguage;
    }

    @Override
    public void onVolumeBtnClick(final TranslatedWord word) {
        final TextToSpeech speaker = polishNativeLanguage ? mediaManager.getEnglishSpeaker() : mediaManager.getPolishSpeaker();
        mediaManager.tellText(getContext(), word.getTranslatedWord(), speaker);
    }

    @Override
    public void onAddBtnClick(final TranslatedWord word) {
        Toast.makeText(getContext(), "not yet", Toast.LENGTH_SHORT).show();
    }
}