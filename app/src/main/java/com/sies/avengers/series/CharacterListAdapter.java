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

public class CharacterListAdapter extends RecyclerView.Adapter<CharacterListAdapter.CharacterListViewHolder> {
    private ArrayList<String> names;
    private ArrayList<String> thumbnails;
    private OnCharacterListener mOnCharacterListener;

    public static class CharacterListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;
        public ImageView imageView;
        OnCharacterListener onCharacterListener;

        public CharacterListViewHolder(View v, CharacterListAdapter.OnCharacterListener onCharacterListener){ //CharacterListAdapter.OnSeriesListener onSeriesListener) {
            super(v);
            textView = v.findViewById(R.id.character_title_recycle);
            imageView = v.findViewById(R.id.character_thumbnail_recycle);
            this.onCharacterListener = onCharacterListener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCharacterListener.onCharacterClick(getAdapterPosition());
        }
    }


    public CharacterListAdapter(ArrayList<String> nameSet, ArrayList<String> imageSet, CharacterListAdapter.OnCharacterListener onCharacterListener){
        names = nameSet;
        System.out.println(names);
        thumbnails = imageSet;
       this.mOnCharacterListener = onCharacterListener;
    }




    @Override
    public CharacterListAdapter.CharacterListViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                       int viewType) {

        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.character_recycler, parent, false);

        CharacterListAdapter.CharacterListViewHolder vh = new CharacterListAdapter.CharacterListViewHolder(v,mOnCharacterListener);//,mOnSeriesListener);
        return vh;
    }


    @Override
    public void onBindViewHolder(CharacterListAdapter.CharacterListViewHolder holder, int position) {

        Picasso.get().load(thumbnails.get(position)).into(holder.imageView);
        holder.textView.setText(names.get(position));
    }


    @Override
    public int getItemCount() {
        return names.size();
    }

    public interface OnCharacterListener{
        void onCharacterClick(int position);
    }
}
