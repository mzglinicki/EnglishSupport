package com.project.mzglinicki.yourowndictionary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.project.mzglinicki.yourowndictionary.Card;
import com.project.mzglinicki.yourowndictionary.R;

import java.util.List;

/**
 * Created by mzglinicki.96 on 28.08.2016.
 */
public class CardsAdapter extends ArrayAdapter<Card> {

    private final List<Card> cards;
    private final LayoutInflater layoutInflater;
    private int currentItemResId;

    private class ViewHolder {
        ImageView resId;
    }

    public CardsAdapter(final Context context, final List<Card> cards) {
        super(context, -1);
        this.cards = cards;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder viewHolder;
        final Card card = cards.get(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.lesson_image_model, parent, false);
            viewHolder.resId = (ImageView) convertView.findViewById(R.id.lessonImageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        currentItemResId = cards.get(position).getLessonImageResId();
        viewHolder.resId.setImageResource(currentItemResId);
        return convertView;
    }

    @Override
    public Card getItem(final int position) {
        return cards.get(position);
    }

    public int getCurrentItemResId() {
        return currentItemResId;
    }


    @Override
    public int getCount() {
        return cards.size();
    }
}