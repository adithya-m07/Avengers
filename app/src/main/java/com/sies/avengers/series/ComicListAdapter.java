package com.sies.avengers.series;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.sies.avengers.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ComicListAdapter extends RecyclerView.Adapter<ComicListAdapter.ComicListViewHolder> {
    private ArrayList<String> names;
    private ArrayList<String> thumbnails;
    private OnComicListener mOnComicListener;


    public static class ComicListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;
        public ImageView imageView;
        OnComicListener onComicListener;

        public ComicListViewHolder(View v,ComicListAdapter.OnComicListener onComicListener) {
            super(v);
            textView = v.findViewById(R.id.comic_title_recycle);
            imageView = v.findViewById(R.id.comic_thumbnail_recycle);
            this.onComicListener = onComicListener;
            v.setOnClickListener(this);
        }

       @Override
        public void onClick(View v) {
            onComicListener.onComicClick(getAdapterPosition());
        }
    }


    public ComicListAdapter(ArrayList<String> nameSet, ArrayList<String> imageSet, ComicListAdapter.OnComicListener onComicListener) {
        names = nameSet;
        thumbnails = imageSet;
        this.mOnComicListener = onComicListener;
    }




    @Override
    public ComicListAdapter.ComicListViewHolder onCreateViewHolder(ViewGroup parent,
                                                                           int viewType) {

        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comic_recycler, parent, false);

        ComicListAdapter.ComicListViewHolder vh = new ComicListAdapter.ComicListViewHolder(v,mOnComicListener);//,mOnSeriesListener);
        return vh;
    }


    @Override
    public void onBindViewHolder(ComicListAdapter.ComicListViewHolder holder, int position) {

        Picasso.get().load(thumbnails.get(position)).into(holder.imageView);
        holder.textView.setText(names.get(position));
    }


    @Override
    public int getItemCount() {
        return names.size();
    }

    public interface OnComicListener{
        void onComicClick(int position);
    }
}
