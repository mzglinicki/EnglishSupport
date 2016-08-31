package com.project.mzglinicki.yourowndictionary.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.mzglinicki.yourowndictionary.R;
import com.project.mzglinicki.yourowndictionary.realmDb.WordDbModel;

import java.util.List;

/**
 * Created by mzglinicki.96 on 30.08.2016.
 */
public class WritingFragmentAdapter extends RecyclerView.Adapter<WritingFragmentViewHolder> {

    private final LayoutInflater inflater;
    private final List<WordDbModel> listOfWords;
    private final OnClickListener onClickListener;

    public WritingFragmentAdapter(final Context context, final List<WordDbModel> listOfWords, final OnClickListener onClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.listOfWords = listOfWords;
        this.onClickListener = onClickListener;
    }

    @Override
    public WritingFragmentViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = inflater.inflate(R.layout.learn_horizontal_writing_model, parent, false);
        return new WritingFragmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WritingFragmentViewHolder holder, final int position) {

        final WordDbModel model = listOfWords.get(position);
        clearAppCompatEditTexts(holder);
        holder.getPolishWordTextView().setText(model.getNativeWord().toUpperCase());
        holder.getCurrentItemPositionTextView().setText(String.valueOf(position + 1));
        holder.getWordAmountTextView().setText(String.valueOf(listOfWords.size()));

        if (onClickListener != null) {
            onClickListener.setupCorrectAnswerAmount(holder);
        }
        setupClickListener(holder.getReadSentenceImageBtn(), model, holder, position);
        setupClickListener(holder.getReadSentenceImageBtn(), model, holder, position);
        setupClickListener(holder.getSkipPreviousImageBtn(), model, holder, position);
        setupClickListener(holder.getSkipNextImageBtn(), model, holder, position);
        setupClickListener(holder.getCheckAnswerTextView(), model, holder, position);
    }

    private void setupClickListener(final View view, final WordDbModel model, final WritingFragmentViewHolder holder, final int position) {

        if (onClickListener == null) {
            return;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                switch (view.getId()) {
                    case R.id.readSentenceImageBtn:
                        onClickListener.onVolumeImageBtnClickListener(model, holder);
                        break;
                    case R.id.skipPreviousImageBtn:
                        onClickListener.onSkipPreviousImageBtnClick(holder);
                        break;
                    case R.id.checkAnswerTextView:
                        onClickListener.onCheckAnswerClick(model, holder);
                        break;
                    case R.id.skipNextImageBtn:
                        onClickListener.onSkipNextImageBtnClick(holder, position);
                        break;
                    case R.id.nestedScrollView:
                        onClickListener.onViewClick(holder);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void clearAppCompatEditTexts(final WritingFragmentViewHolder holder) {
        for (final AppCompatEditText view : holder.getListToClear()) {
            view.setText("");
            view.clearFocus();
        }
    }

    @Override
    public int getItemCount() {
        return listOfWords.size();
    }

    public interface OnClickListener {
        void onVolumeImageBtnClickListener(final WordDbModel model, final WritingFragmentViewHolder holder);

        void onCheckAnswerClick(final WordDbModel model, final WritingFragmentViewHolder holder);

        void setupCorrectAnswerAmount(final WritingFragmentViewHolder holder);

        void onSkipPreviousImageBtnClick(final WritingFragmentViewHolder holder);

        void onSkipNextImageBtnClick(final WritingFragmentViewHolder holder, final int position);

        void onViewClick(final WritingFragmentViewHolder holder);
    }
}
