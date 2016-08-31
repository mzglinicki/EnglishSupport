package com.project.mzglinicki.yourowndictionary.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.mzglinicki.yourowndictionary.AppUtility;
import com.project.mzglinicki.yourowndictionary.Constants;
import com.project.mzglinicki.yourowndictionary.MediaManager;
import com.project.mzglinicki.yourowndictionary.R;
import com.project.mzglinicki.yourowndictionary.adapters.WordsListRecycleAdapter;
import com.project.mzglinicki.yourowndictionary.realmDb.LessonDbModel;
import com.project.mzglinicki.yourowndictionary.realmDb.RealmDbHelper;
import com.project.mzglinicki.yourowndictionary.realmDb.WordDbModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mzglinicki.96 on 28.08.2016.
 */
public class LessonCreator extends AppCompatActivity implements WordsListRecycleAdapter.OnClickLister {

    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.collapsingToolbarImage)
    ImageView collapsingToolbarImage;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.lessonName)
    AppCompatEditText lessonNameEditText;
    @Bind(R.id.inputNativeWord)
    AppCompatEditText nativeInputEditText;
    @Bind(R.id.inputTranslateWord)
    AppCompatEditText translateInputEditText;
    @Bind(R.id.inputSentence)
    AppCompatEditText inputSentenceEditText;
    @Bind(R.id.wordListRecycleView)
    RecyclerView wordsRecycleView;
    @Bind(R.id.emptyListText)
    TextView emptyListText;
    @Bind(R.id.wordListHeader)
    RelativeLayout wordListHeader;
    @Bind({R.id.inputNativeWord, R.id.inputTranslateWord, R.id.inputSentence})
    List<AppCompatEditText> inputFields;
    @Bind(R.id.addFAB)
    FloatingActionButton addFAB;
    @Bind(R.id.closeEditFAB)
    FloatingActionButton closeEditFAB;
    @Bind(R.id.saveEditFAB)
    FloatingActionButton saveEditFAB;
    @Bind({R.id.addFAB, R.id.closeEditFAB, R.id.saveEditFAB})
    List<FloatingActionButton> fABToToggle;

    private WordsListRecycleAdapter wordListAdapter;
    private List<WordDbModel> itemToRemove;
    private List<WordDbModel> listOfWords;
    private AppUtility appUtilityManager;
    private MediaManager mediaManager;
    private RealmDbHelper dbHelper;
    private String lessonName;
    private String polishWord;
    private String englishWord;
    private String sentence;
    private boolean isFirstSaved;
    private boolean isEditLesson;
    private int lessonId;
    private int editWordId;
    private int lessonToEditId;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator);
        ButterKnife.bind(this);

        initFields();
        initToolbar();
        setupWordsRecycleView();
        setOnTouchListener(coordinatorLayout);

        lessonToEditId = getIntent().getIntExtra(Constants.LESSON_ID_KEY, -1);
        if (lessonToEditId != -1) {
            editLesson(lessonToEditId);
        }
    }

    @Override
    public void onClick(final View imageButton, final WordDbModel model, final int position) {

        if (imageButton.getId() == R.id.deleteImageButton) {
            onDeleteImageBtnClick(model, position);
        } else if (imageButton.getId() == R.id.editImageButton) {
            onEditImageBtnClick(model);
        } else if (imageButton.getId() == R.id.volumeImageBtn) {
            onVolumeImageBtnClick(model);
        }
    }

    @Override
    public void onLongClick(final WordDbModel model) {

        if (isEditLesson) {
            return;
        }

        for (final WordDbModel word : listOfWords) {
            if (word.isEditEnabled() && !word.equals(model)) {
                dbHelper.setWordEditMode(word, false);
            }
        }
        dbHelper.setWordEditMode(model, !model.isEditEnabled());
        wordListAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.addFAB)
    public void onFABClick() {

        if (areInputDataCorrect()) {
            createNewData();
        } else {
            Toast.makeText(this, "Wprowadź wszystkie dane", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.saveEditFAB)
    public void onSaveEditFAB() {

        if (areInputDataCorrect()) {
            editWord();
        } else {
            Toast.makeText(this, R.string.inputAllDate, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.closeEditFAB)
    public void onCloseEditFAB() {
        toggleFABsVisibility();
        setupWordListEditMode(false);
        clearInputFields();
    }

    private void initFields() {
        appUtilityManager = AppUtility.getInstance(this);
        dbHelper = RealmDbHelper.getInstance(this);
        mediaManager = MediaManager.getInstance(this);
        listOfWords = new ArrayList<>();
        itemToRemove = new ArrayList<>();
        isFirstSaved = true;
        isEditLesson = false;
    }

    private void editLesson(final int lessonToEditId) {
        isEditLesson = true;

        final LessonDbModel lessonToEdit = dbHelper.getLesson(lessonToEditId);
        final String editLessonName = lessonToEdit.getLessonName();
        final int editLessonImageResId = lessonToEdit.getLessonImageResId();
        listOfWords = getLessonToEditWords(lessonToEditId);

        setupWordListEditMode(true);
        setupVisibility();
        setupEditLessonCollapsingToolbar(editLessonName, editLessonImageResId);

        lessonNameEditText.setText(editLessonName);
        wordListAdapter.notifyDataSetChanged();
    }

    private void setupEditLessonCollapsingToolbar(final String editLessonName, final int editLessonImageResId) {
        collapsingToolbar.setTitle(editLessonName);
        collapsingToolbarImage.setImageResource(editLessonImageResId);
    }

    private void setupVisibility() {
        emptyListText.setVisibility(View.GONE);
        closeEditFAB.setVisibility(View.GONE);
        wordListHeader.setVisibility(View.VISIBLE);
        saveEditFAB.setVisibility(View.VISIBLE);
        addFAB.setVisibility(View.VISIBLE);
    }

    private List<WordDbModel> getLessonToEditWords(final int lessonToEditId) {

        final List<WordDbModel> editableWords = new ArrayList<>();

        for (final WordDbModel wordDbModel : dbHelper.getLessonWords(lessonToEditId)) {
            editableWords.add(wordDbModel);
        }
        return editableWords;
    }

    private void setupWordsRecycleView() {
        wordListAdapter = new WordsListRecycleAdapter(this, listOfWords, this);
        wordsRecycleView.setHasFixedSize(true);
        wordsRecycleView.setAdapter(wordListAdapter);
        wordsRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void createNewData() {

        if (isFirstSaved && !isEditLesson) {
            lessonId = dbHelper.createLessonInDb(lessonName);
            collapsingToolbar.setTitle(lessonName);
            isFirstSaved = false;
        }

        if (isEditLesson) {
            listOfWords.add(dbHelper.createWord(polishWord, englishWord, sentence, lessonToEditId));
        } else {
            listOfWords.add(dbHelper.createWord(polishWord, englishWord, sentence, lessonId));
        }

        invalidateViewAfterSaved();
    }

    private void invalidateViewAfterSaved() {
        clearInputFields();
        isListOfWordsEmpty();
        wordListAdapter.notifyDataSetChanged();
        Toast.makeText(LessonCreator.this, getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
    }

    private void editWord() {

        if (isEditLesson) {
            dbHelper.updateLessonData(lessonName, lessonToEditId);
            dbHelper.updateWordInDb(polishWord, englishWord, sentence, editWordId);
            collapsingToolbar.setTitle(lessonName);
        } else {
            dbHelper.updateWordInDb(polishWord, englishWord, sentence, editWordId);
            toggleFABsVisibility();
            setupWordListEditMode(false);
        }
        clearInputFields();
        Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
    }

    private void setOnTouchListener(final View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                lessonNameEditText.clearFocus();
                appUtilityManager.hideSoftInput(view);
                return true;
            }
        });
    }

    private boolean areInputDataCorrect() {
        polishWord = nativeInputEditText.getText().toString();
        englishWord = translateInputEditText.getText().toString();
        sentence = inputSentenceEditText.getText().toString();
        lessonName = lessonNameEditText.getText().toString();

        return !(polishWord.isEmpty() || englishWord.isEmpty() || sentence.isEmpty() || lessonName.isEmpty());
    }

    private void clearInputFields() {
        for (final AppCompatEditText inputField : inputFields) {
            inputField.setText("");
            inputField.clearFocus();
        }
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        collapsingToolbar.setTitle(getString(R.string.lessonCreatorTitle));
    }

    private void setupWordListEditMode(final boolean editable) {
        for (final WordDbModel word : listOfWords) {
            dbHelper.setWordEditMode(word, editable);
        }
        wordListAdapter.notifyDataSetChanged();
    }

    private void onVolumeImageBtnClick(final WordDbModel model) {

        mediaManager.tellText(this, model.getNativeWord(), mediaManager.getPolishSpeaker());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaManager.tellText(LessonCreator.this, model.getTranslatedWord(), mediaManager.getEnglishSpeaker());
            }
        }, 2000);
    }

    private void onEditImageBtnClick(final WordDbModel model) {

        editWordId = model.getWordId();

        if (!isEditLesson) {
            setEditModeFABsVisible();
        }

        nativeInputEditText.setText(model.getNativeWord());
        translateInputEditText.setText(model.getTranslatedWord());
        inputSentenceEditText.setText(model.getExampleSentence());
    }

    private void setEditModeFABsVisible() {
        addFAB.setVisibility(View.GONE);
        saveEditFAB.setVisibility(View.VISIBLE);
        closeEditFAB.setVisibility(View.VISIBLE);
    }

    private void toggleFABsVisibility() {
        for (final FloatingActionButton fAB : fABToToggle) {
            fAB.setVisibility(fAB.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        }
    }

    private void onDeleteImageBtnClick(final WordDbModel model, final int position) {
        listOfWords.remove(model);
        wordListAdapter.notifyItemRemoved(position);
        itemToRemove.add(model);
        showSnackbar(model, position);
    }

    private void showSnackbar(final WordDbModel model, final int position) {
        final Snackbar snackbar = Snackbar
                .make(wordsRecycleView, "Usunięto", Snackbar.LENGTH_SHORT)
                .setAction("Cofnij", new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        onUndoClick(model, position);
                    }
                })
                .setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(final Snackbar snackbar, final int event) {
                        super.onDismissed(snackbar, event);
                        onDismissedClick(model);
                    }
                });
        snackbar.show();
    }

    private void onDismissedClick(final WordDbModel model) {
        if (!itemToRemove.isEmpty()) {
            dbHelper.deleteWordFromDb(itemToRemove.get(0).getWordId());
        }
        itemToRemove.remove(model);
        isListOfWordsEmpty();
    }

    private void isListOfWordsEmpty() {
        if (listOfWords.isEmpty()) {
            emptyListText.setVisibility(View.VISIBLE);
            wordListHeader.setVisibility(View.GONE);
        }
    }

    private void onUndoClick(final WordDbModel model, final int position) {
        listOfWords.add(position, model);
        wordListAdapter.notifyItemInserted(position);
        itemToRemove.remove(model);
    }
}