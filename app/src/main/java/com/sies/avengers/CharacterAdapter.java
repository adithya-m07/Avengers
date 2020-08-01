package com.sies.avengers;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {
    private final ArrayList<String> names;
    private final ArrayList<String> thumbnails;

    private final OnSeriesListener mOnSeriesListener;


    public static class CharacterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;
        public ImageView imageView;
        OnSeriesListener onSeriesListener;

        public CharacterViewHolder(View v, OnSeriesListener onSeriesListener) {
            super(v);
            textView = v.findViewById(R.id.series_names);
            imageView = v.findViewById(R.id.series_thumbnails);
            this.onSeriesListener = onSeriesListener;
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onSeriesListener.onSeriesClick(getAdapterPosition());
        }
    }


    public CharacterAdapter(ArrayList<String> nameSet, ArrayList<String> imageSet, OnSeriesListener onSeriesListener) {
        names = nameSet;
        thumbnails = imageSet;

        this.mOnSeriesListener = onSeriesListener;
    }

    @Override
    public CharacterAdapter.CharacterViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {

        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.series_home_view, parent, false);

        CharacterViewHolder vh = new CharacterViewHolder(v,mOnSeriesListener);
        return vh;
    }


    @Override
    public void onBindViewHolder(CharacterViewHolder holder, int position) {

        Picasso.get().load(thumbnails.get(position)).into(holder.imageView);
        holder.textView.setText(names.get(position));
    }


    @Override
    public int getItemCount() {
        return names.size();
    }

    public interface OnSeriesListener{
        void onSeriesClick(int position);
    }

}
