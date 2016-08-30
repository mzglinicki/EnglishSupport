package com.project.mzglinicki.yourowndictionary.fragments.learnActivity;

import android.view.View;
import android.widget.TextView;

import com.project.mzglinicki.yourowndictionary.FicheModel;
import com.project.mzglinicki.yourowndictionary.R;
import com.project.mzglinicki.yourowndictionary.adapters.FicheAdapter;
import com.project.mzglinicki.yourowndictionary.realmDb.WordDbModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.arjsna.swipecardlib.SwipeCardView;

/**
 * Created by Marcin Zglinicki on 19.05.2016.
 */
public class LearnFicheFragment extends LearnFragmentParent implements FicheAdapter.OnSwipeListener {

    @Bind(R.id.ficheViewFrame)
    SwipeCardView cardViewFrame;

    private FicheAdapter adapter;

    public LearnFicheFragment() {
        titleId = R.string.tab_fiche;
        layoutId = R.layout.fragment_fiche_v2;
    }

    @Override
    public void init(final View view) {

        ButterKnife.bind(this, view);

        listOfFiche = new ArrayList<>();

        for (final WordDbModel word : dbHelper.getLessonWords(lessonId)) {
            final String polishWord = word.getNativeWord();
            final String englishWord = word.getTranslatedWord();
            listOfFiche.add(new FicheModel(polishWord, englishWord));
        }

        adapter = new FicheAdapter(getContext(), listOfFiche, this);
        cardViewFrame.setAdapter(adapter);
        cardViewFrame.setFlingListener(new SwipeCardView.OnCardFlingListener() {
            @Override
            public void onCardExitLeft(final Object dataObject) {
            }

            @Override
            public void onCardExitRight(final Object dataObject) {
            }

            @Override
            public void onAdapterAboutToEmpty(final int itemsInAdapter) {
            }

            @Override
            public void onScroll(final float scrollProgressPercent) {
            }

            @Override
            public void onCardExitTop(final Object dataObject) {
                onItemSwiped(dataObject);
            }

            @Override
            public void onCardExitBottom(final Object dataObject) {
                onItemSwiped(dataObject);
            }
        });
    }

    private void onItemSwiped(final Object dataObject) {
        listOfFiche.add((FicheModel) dataObject);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSwipeListener(final TextView currentItemPositionTextView, final TextView ficheAmountTextView, final FicheModel model) {
        currentItemPositionTextView.setText(String.valueOf(listOfFiche.indexOf(model)+1));
        ficheAmountTextView.setText(String.valueOf(dbHelper.getLessonWords(lessonId).size()));
    }
}