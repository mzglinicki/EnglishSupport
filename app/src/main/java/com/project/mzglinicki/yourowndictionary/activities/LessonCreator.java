package com.project.mzglinicki.yourowndictionary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
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
import com.project.mzglinicki.yourowndictionary.Card;
import com.project.mzglinicki.yourowndictionary.Constants;
import com.project.mzglinicki.yourowndictionary.MediaManager;
import com.project.mzglinicki.yourowndictionary.R;
import com.project.mzglinicki.yourowndictionary.adapters.CardsAdapter;
import com.project.mzglinicki.yourowndictionary.adapters.WordsListRecycleAdapter;
import com.project.mzglinicki.yourowndictionary.realmDb.LessonDbModel;
import com.project.mzglinicki.yourowndictionary.realmDb.RealmDbHelper;
import com.project.mzglinicki.yourowndictionary.realmDb.WordDbModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    @Bind(R.id.appBar)
    AppBarLayout appBarLayout;
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
    //    @Bind(R.id.cardViewFrame)
//    SwipeCardView cardViewFrame;
//    @Bind(R.id.selectImageTextView)
//    TextView selectImageTextView;
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
    private MediaManager mediaManager;
    private AppUtility appUtilityManager;
    private RealmDbHelper dbHelper;
    private List<WordDbModel> listOfWords;
    private List<WordDbModel> itemToRemove;
    private String lessonName;
    private String nativeWord;
    private String translateWord;
    private String sentence;
    private int lessonId;
    private boolean isFirstSaved = true;
    //    private ArrayList<Card> listOfCards;
    private CardsAdapter arrayAdapter;
    private int lessonImageResId;
    private int editWordId;
    private boolean editLesson = false;
    private LessonDbModel lessonToEdit;
    private int randomImage;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator);
        ButterKnife.bind(this);


        appUtilityManager = AppUtility.getInstance(this);
        dbHelper = RealmDbHelper.getInstance(this);
        mediaManager = MediaManager.getInstance(this);


        lessonImageResId = getRandomImage();


        initToolbar();

//        listOfCards = new ArrayList<>();

//        addLessonImages(listOfCards);
//        arrayAdapter = new CardsAdapter(this, listOfCards);
//        cardViewFrame.setAdapter(arrayAdapter);
//        cardViewFrame.setFlingListener(new SwipeCardView.OnCardFlingListener() {
//            @Override
//            public void onCardExitLeft(final Object dataObject) {
//                listOfCards.add((Card) dataObject);
//                arrayAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCardExitRight(final Object dataObject) {
//                listOfCards.add((Card) dataObject);
//                arrayAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onAdapterAboutToEmpty(final int itemsInAdapter) {
//            }
//
//            @Override
//            public void onScroll(final float scrollProgressPercent) {
//            }
//
//            @Override
//            public void onCardExitTop(final Object dataObject) {
//                listOfCards.add((Card) dataObject);
//                arrayAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCardExitBottom(final Object dataObject) {
//                listOfCards.add((Card) dataObject);
//                arrayAdapter.notifyDataSetChanged();
//            }
//        });

        setOnTouchListener(coordinatorLayout);

        listOfWords = new ArrayList<>();
        itemToRemove = new ArrayList<>();
        setupWordsRecycleView();

        final int lessonToEditId = getIntent().getIntExtra(Constants.LESSON_ID_KEY, -1);

        if (lessonToEditId != -1) {
            editLesson = true;
            lessonToEdit = dbHelper.getLesson(lessonToEditId);
            lessonNameEditText.setText(lessonToEdit.getLessonName());

            for (final WordDbModel wordDbModel : dbHelper.getLessonWords(lessonToEditId)) {
                listOfWords.add(wordDbModel);
            }
            emptyListText.setVisibility(View.GONE);
            wordListHeader.setVisibility(View.VISIBLE);
            wordListAdapter.notifyDataSetChanged();
            nativeInputEditText.setText(listOfWords.get(0).getNativeWord());
            translateInputEditText.setText(listOfWords.get(0).getTranslatedWord());
            inputSentenceEditText.setText(listOfWords.get(0).getExampleSentence());
            collapsingToolbar.setTitle("Edytor lekcji");
            editWordId = listOfWords.get(0).getWordId();
            collapsingToolbarImage.setImageResource(lessonToEdit.getLessonImageResId());
        }
    }

    private void addLessonImages(final ArrayList<Card> listOfCards) {

        //TODO

        final Card card2 = new Card();
        card2.setLessonImageResId(R.mipmap.palace);
        listOfCards.add(card2);

        final Card card = new Card();
        card.setLessonImageResId(R.mipmap.flag);
        listOfCards.add(card);

        final Card card3 = new Card();
        card3.setLessonImageResId(R.mipmap.cab);
        listOfCards.add(card3);
    }

//    @Override
//    public void onBackPressed() {
//
//        startActivity(new Intent(LessonCreator.this, MainActivity.class));
//        overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);
//        finish();
//    }

    private void setupWordsRecycleView() {
        wordListAdapter = new WordsListRecycleAdapter(this, listOfWords, this);
        wordsRecycleView.setHasFixedSize(true);
        wordsRecycleView.setAdapter(wordListAdapter);
        wordsRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    @OnClick(R.id.addFAB)
    public void onFABClick() {

        if (areInputDataCorrect()) {
            if (!editLesson) {
                createNewData();
            } else {
                editExistingLesson();
            }
        } else {
            Toast.makeText(this, "Wprowadź wszystkie dane", Toast.LENGTH_SHORT).show();
        }
    }

    private void createNewData() {
        if (isFirstSaved) {
            lessonId = dbHelper.createLessonInDb(lessonName, lessonImageResId);
            setupLessonNamedView();
            isFirstSaved = false;
        }
        listOfWords.add(dbHelper.createWord(nativeWord, translateWord, sentence, lessonId));
        doAfterWordSaved();
    }

    private void editExistingLesson() {
        if (isFirstSaved) {
            dbHelper.updateLessonData(lessonName, lessonImageResId, lessonToEdit.getLessonId());
            isFirstSaved = false;
            setupLessonNamedView();
        }
        dbHelper.updateWordInDb(nativeWord, translateWord, sentence, editWordId);
        doAfterWordSaved();
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

    private void doAfterWordSaved() {
        clearInputFields();
        wordListAdapter.notifyDataSetChanged();
        if (!listOfWords.isEmpty()) {
            emptyListText.setVisibility(View.GONE);
            wordListHeader.setVisibility(View.VISIBLE);
        }
        Toast.makeText(LessonCreator.this, getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
    }

    private void setupLessonNamedView() {
        collapsingToolbar.setTitle(lessonName);
        lessonNameEditText.setVisibility(View.GONE);
//        cardViewFrame.setVisibility(View.GONE);
//        selectImageTextView.setVisibility(View.GONE);
    }

    private boolean areInputDataCorrect() {
        nativeWord = nativeInputEditText.getText().toString();
        translateWord = translateInputEditText.getText().toString();
        sentence = inputSentenceEditText.getText().toString();
        lessonName = lessonNameEditText.getText().toString();

        return !(nativeWord.isEmpty() || translateWord.isEmpty() || sentence.isEmpty() || lessonName.isEmpty());
    }

    private void clearInputFields() {
        for (final AppCompatEditText inputField : inputFields) {
            inputField.setText("");
            inputField.clearFocus();
        }
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        collapsingToolbarImage.setImageResource(lessonImageResId);
        collapsingToolbar.setTitle("Kreator nowej lekcji");
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
        for (final WordDbModel word : listOfWords) {
            if (word.isEditEnabled() && !word.equals(model)) {
                dbHelper.setWordEditMode(word, false);
            }
        }
        dbHelper.setWordEditMode(model, !model.isEditEnabled());
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

        setEditModeFABsVisible();

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

//    @OnClick(R.id.selectImageTextView)
//    public void onSelectImageClick() {
//        lessonImageResId = arrayAdapter.getCurrentItemResId();
//        collapsingToolbarImage.setImageResource(lessonImageResId);
//    }

    @OnClick(R.id.saveEditFAB)
    public void onSaveEditFAB() {
        dbHelper.updateWordInDb(nativeInputEditText.getText().toString(), translateInputEditText.getText().toString(), inputSentenceEditText.getText().toString(), editWordId);
        toggleFABsVisibility();
        clearInputFields();
        clearEditMode();
        Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.closeEditFAB)
    public void onCloseEditFAB() {
        toggleFABsVisibility();
        clearEditMode();
        clearInputFields();
    }

    private void clearEditMode() {
        for (final WordDbModel word : listOfWords) {
            dbHelper.setWordEditMode(word, false);
        }
        wordListAdapter.notifyDataSetChanged();
    }

    public int getRandomImage() {

        final List<Integer> images = new ArrayList<>();
        final Random random = new Random();
        images.add(R.mipmap.palace);
        images.add(R.mipmap.flag);
        images.add(R.mipmap.cab);
        images.add(R.mipmap.london);

        return images.get(random.nextInt(images.size()));
    }
}