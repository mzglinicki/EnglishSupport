package com.project.mzglinicki.yourowndictionary.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.mzglinicki.yourowndictionary.Constants;
import com.project.mzglinicki.yourowndictionary.MediaManager;
import com.project.mzglinicki.yourowndictionary.R;
import com.project.mzglinicki.yourowndictionary.activities.LearnActivity;
import com.project.mzglinicki.yourowndictionary.activities.LessonCreator;
import com.project.mzglinicki.yourowndictionary.activities.MainActivity;
import com.project.mzglinicki.yourowndictionary.realmDb.LessonDbModel;
import com.project.mzglinicki.yourowndictionary.realmDb.RealmDbHelper;
import com.project.mzglinicki.yourowndictionary.realmDb.WordDbModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mzglinicki.96 on 27.08.2016.
 */
public class MainFragment extends Fragment {

    @Bind(R.id.btnLearn)
    Button btnLearn;
    @Bind(R.id.btnDictionary)
    Button btnDictionary;
    @Bind(R.id.btnCredits)
    Button btnCredits;
    @Bind(R.id.dailyWord)
    TextView dailyWordTextView;
    @Bind(R.id.dailyFiche)
    CardView dailyFicheCardView;
    @Bind(R.id.lessonImage)
    ImageView lessonImage;
    @Bind(R.id.lessonName)
    TextView lessonNameTextView;
    @Bind(R.id.emptyStarImageBtn)
    ImageButton starImageBtn;
    @Bind({R.id.emptyStarImageBtn, R.id.starImageBtn})
    List<ImageButton> starsImageBtns;

    private RealmDbHelper dbHelper;
    private MediaManager mediaManager;
    private WordDbModel randomWord;
    private String wordOnFiche;
    private TextToSpeech speaker;
    private LessonDbModel lesson;

    public MainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        dbHelper = RealmDbHelper.getInstance(getContext());
        mediaManager = MediaManager.getInstance(getContext());

        setupClickListeners();
        setupDailyFiche();
        setupDefaultLesson();
        return view;
    }

    private void setupClickListeners() {
        setOnBtnClickListener(btnLearn);
        setOnBtnClickListener(btnDictionary);
        setOnBtnClickListener(btnCredits);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).setToolbarTitle("English support");
        ((MainActivity) getActivity()).setFABVisibility(false);
    }

    private void setOnBtnClickListener(final Button button) {

        final FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                switch (button.getId()) {
                    case R.id.btnLearn:
                        transaction.replace(R.id.placeHolder, new LessonsFragment());
                        break;
                    case R.id.btnDictionary:
                        transaction.replace(R.id.placeHolder, new DictionaryFragment());
                        break;
                    case R.id.btnCredits:
                        Toast.makeText(getContext(), "not yet", Toast.LENGTH_SHORT).show();
                    default:
                        break;
                }
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void setupDailyFiche() {

        randomWord = dbHelper.getRandomWord();
        final String nativeWord = randomWord.getNativeWord();
        dailyWordTextView.setText(nativeWord);
        wordOnFiche = nativeWord;
    }

    private void setupDefaultLesson() {
        lesson = dbHelper.getLesson(randomWord.getLessonId());
        lessonImage.setImageResource(lesson.getLessonImageResId());
        lessonNameTextView.setText(lesson.getLessonName());
    }

    @OnClick(R.id.dailyFiche)
    public void onFicheClickNotification() {
        dailyWordTextView.setText(dailyWordTextView.getText().equals(randomWord.getNativeWord()) ? randomWord.getTranslatedWord() : randomWord.getNativeWord());
        wordOnFiche = dailyWordTextView.getText().toString();
    }

    @OnClick(R.id.learnTextView)
    public void onLearnTextView() {
        final Intent intent = new Intent(getContext(), LearnActivity.class);
        intent.putExtra(Constants.LESSON_ID_KEY, lesson.getLessonId());
        startActivity(intent);
    }

    @OnClick(R.id.testTextView)
    public void onTestTextView() {
        //TODO
        Toast.makeText(getContext(), "not yet", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.editTextView)
    public void onEditTextView() {
        final Intent intent = new Intent(getContext(), LessonCreator.class);
        intent.putExtra(Constants.LESSON_ID_KEY, lesson.getLessonId());
        startActivity(intent);
    }

    @OnClick(R.id.emptyStarImageBtn)
    public void onEmptyStarImageBtn() {
        //TODO
        for (final ImageButton starsImageBtn : starsImageBtns) {
            starsImageBtn.setVisibility(starsImageBtn.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        }
    }

    @OnClick(R.id.volumeImageBtn)
    public void onVolumeImageBtn() {
        speaker = wordOnFiche.equals(randomWord.getNativeWord()) ? mediaManager.getPolishSpeaker() : mediaManager.getEnglishSpeaker();
        mediaManager.tellText(getContext(), wordOnFiche, speaker);
    }
}