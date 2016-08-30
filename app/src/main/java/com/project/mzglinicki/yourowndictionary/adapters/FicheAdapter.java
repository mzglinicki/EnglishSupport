package com.project.mzglinicki.yourowndictionary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.project.mzglinicki.yourowndictionary.FicheModel;
import com.project.mzglinicki.yourowndictionary.R;

import java.util.List;

/**
 * Created by mzglinicki.96 on 29.08.2016.
 */
public class FicheAdapter extends ArrayAdapter<FicheModel> {

    private final List<FicheModel> listOfFiche;
    private final LayoutInflater inflater;
    private final OnSwipeListener onSwipeListener;

    private class ViewHolder {
        TextView ficheTextField;
        TextView currentItemPositionTextView;
        TextView ficheAmountTextView;
    }

    public FicheAdapter(final Context context, final List<FicheModel> listOfFiche, final OnSwipeListener onSwipeListener) {
        super(context, -1);
        this.listOfFiche = listOfFiche;
        this.inflater = LayoutInflater.from(context);
        this.onSwipeListener = onSwipeListener;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final FicheModel model = listOfFiche.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.learn_fiche_model, parent, false);
            viewHolder.ficheTextField = (TextView) convertView.findViewById(R.id.ficheTextField);
            viewHolder.currentItemPositionTextView = (TextView) convertView.findViewById(R.id.currentItemPositionTextView);
            viewHolder.ficheAmountTextView = (TextView) convertView.findViewById(R.id.ficheAmountTextView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        onSwipeListener.onSwipeListener(viewHolder.currentItemPositionTextView, viewHolder.ficheAmountTextView, model);
        final String polishWord = model.getPolishWord();
        final TextView ficheTextField = viewHolder.ficheTextField;
        ficheTextField.setText(polishWord);
        ficheTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ficheTextField.setText(ficheTextField.getText().toString().equals(polishWord) ? model.getEnglishWord() : polishWord);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    @Override
    public FicheModel getItem(final int position) {
        return listOfFiche.get(position);
    }

    @Override
    public int getCount() {
        return listOfFiche.size();
    }

    @Override
    public int getPosition(final FicheModel item) {
        return super.getPosition(item);
    }

    public interface OnSwipeListener {
        void onSwipeListener(final TextView currentItemPositionTextView, final TextView ficheAmountTextView, final FicheModel model);
    }
}