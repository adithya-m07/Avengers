package com.sies.avengers.series;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sies.avengers.*;
import com.sies.avengers.getters.GetHash;
import com.sies.avengers.getters.GetValues;
import com.sies.avengers.series.details.DetailsActivity;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;

public class SeriesActivity extends AppCompatActivity implements CharacterListAdapter.OnCharacterListener,ComicListAdapter.OnComicListener {

    private final String privateKey = "";
    private final String publicKey = "";
    public ArrayList<String> characterName = new ArrayList<>();
    public ArrayList<String> characterImage = new ArrayList<>();
    public ArrayList<String> comicName = new ArrayList<>();
    public ArrayList<String> comicImage = new ArrayList<>();
   // public ArrayList<Integer> comicId = new ArrayList<>();
    //public ArrayList<Integer> characterId = new ArrayList<>();
    private final long timestamp = System.currentTimeMillis();
    public TextView title;
    public TextView desc;
    public ImageView thumbnail;
    private int id;
    public RecyclerView characterRecycler;
    public RecyclerView comicRecycler;
    private GetValues getCharacterValues;
    private ComicListAdapter comicListAdapter;
    private CharacterListAdapter characterListAdapter;
    private GetValues getComicValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);

        Intent intent = getIntent();
        id=intent.getIntExtra("id",-1);

        title = findViewById(R.id.series_description_title);
        desc = findViewById(R.id.series_description);
        thumbnail = findViewById(R.id.series_image);

        ImageButton back = findViewById(R.id.back_button_series);
        back.setOnClickListener(v -> onBackPressed());

        getSeriesDetailsCall();
    }
    private void getSeriesDetailsCall() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://gateway.marvel.com:443/v1/public/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MarvelApi marvelApi = retrofit.create(MarvelApi.class);

        GetHash getHash = new GetHash();
        String hashValue = getHash.getMd5(timestamp+privateKey+publicKey);


        Call<GetValues> seriesCall = marvelApi.getSeriesData(id,timestamp,publicKey,hashValue);
        Call<GetValues> characterCall = marvelApi.getCharacterData(id,timestamp,publicKey,hashValue,20,0);
        Call<GetValues> comicCall = marvelApi.getComicData(id,timestamp,publicKey,hashValue,20,0);

        seriesCall.enqueue(new Callback<GetValues>() {
            @Override
            public void onResponse(Call<GetValues> call1, Response<GetValues> response) {
                if(!response.isSuccessful()){
                    title.setText("Unable to connect\nCode: "+ response.code());
                    return;
                }

                GetValues getValues = response.body();
                title.setText(getValues.getData().results().get(0).getTitle());
                desc.setText(getValues.getData().results().get(0).getDescription());
                Picasso.get().load(getValues.getData().results().get(0).getThumbnail().getPath()+"/landscape_incredible."+getValues.getData().results().get(0).getThumbnail().getExtension()).into(thumbnail);

                String string = getResources().getString(R.string.desc);
                if(getValues.getData().results().get(0).getDescription() != null){
                    TextView description = findViewById(R.id.description);
                    description.setText(string);
                }
                TextView comics = findViewById(R.id.comics);
                TextView characters = findViewById(R.id.characters);
                TextView all_comics = findViewById(R.id.all_comics);
                TextView all_characters = findViewById(R.id.all_characters);

                string = getResources().getString(R.string.comics);
                comics.setText(string);
                string = getResources().getString(R.string.characters);
                characters.setText(string);
                string = getResources().getString(R.string.all_characters);
                all_characters.setText(string);
                string = getResources().getString(R.string.all_comics);
               all_comics.setText(string);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                title.setText("Unable to connect\nPlease check your connection");
            }
        });

        characterCall.enqueue(new Callback<GetValues>() {
            @Override
            public void onResponse(Call<GetValues> call, Response<GetValues> response) {
                if(!response.isSuccessful()){
                    TextView textView = findViewById(R.id.attribution_text);
                    textView.setText("Unable to connect\nCode: "+ response.code());
                    return;
                }
                getCharacterValues = response.body();
                for (int i = 0;i<getCharacterValues.getData().getCount();i++){
                    characterName.add(getCharacterValues.getData().results().get(i).getName());
                    characterImage.add(getCharacterValues.getData().results().get(i).getThumbnail().getPath()+"/portrait_incredible."+getCharacterValues.getData().results().get(i).getThumbnail().getExtension());
                }

                setRecycler("character");

            }
            @Override
            public void onFailure(Call<GetValues> call, Throwable t) {
                title.setText("Unable to connect\nPlease check your connection");
            }
        });

        comicCall.enqueue(new Callback<GetValues>() {
            @Override
            public void onResponse(Call<GetValues> call, Response<GetValues> response) {
                if(!response.isSuccessful()){
                    title.setText("Unable to connect\nCode: "+ response.code());
                    return;
                }
                getComicValues = response.body();
                for (int i = 0;i<getComicValues.getData().getCount();i++){
                    comicName.add(getComicValues.getData().results().get(i).getTitle());
                    comicImage.add(getComicValues.getData().results().get(i).getThumbnail().getPath()+"/portrait_incredible."+getComicValues.getData().results().get(i).getThumbnail().getExtension());
                }

                setRecycler("comic");

            }
            @Override
            public void onFailure(Call<GetValues> call, Throwable t) {
                title.setText("Unable to connect\nPlease check your connection");
            }
        });

    }

    private void setRecycler(String from) {
        if(from.equals("comic")){
            comicRecycler = findViewById(R.id.comic_list_recycler);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext(), LinearLayout.HORIZONTAL,false);
            comicRecycler.setLayoutManager(layoutManager);
            comicListAdapter = new ComicListAdapter(comicName,comicImage, SeriesActivity.this);
            comicRecycler.setAdapter(comicListAdapter);
        }
        else{
            characterRecycler = findViewById(R.id.character_list_recycler);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext(), LinearLayout.HORIZONTAL,false);
            characterRecycler.setLayoutManager(layoutManager);
            characterListAdapter = new CharacterListAdapter(characterName,characterImage, SeriesActivity.this);
            characterRecycler.setAdapter(characterListAdapter);
        }
    }

    @Override
    public void onCharacterClick(int position) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("name",characterName.get(position));
        intent.putExtra("image",characterImage.get(position));
        intent.putExtra("description",getCharacterValues.getData().results().get(position).getDescription());
        intent.putExtra("url",getCharacterValues.getData().results().get(position).getUrls().get(1).getUrl());
        startActivity(intent);
    }

    @Override
    public void onComicClick(int position) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("name",comicName.get(position));
        intent.putExtra("image",comicImage.get(position));
        intent.putExtra("description",getComicValues.getData().results().get(position).getDescription());
        intent.putExtra("url",getComicValues.getData().results().get(position).getUrls().get(0).getUrl());
        startActivity(intent);
    }

    public void onAllComicClick(View view) {
        Intent intent = new Intent(this,DetailListActivity.class);
        intent.putExtra("from","comic");
        intent.putExtra("id",id);
        startActivity(intent);
    }

    public void onAllCharacterClick(View view) {
        Intent intent = new Intent(this,DetailListActivity.class);
        intent.putExtra("from","character");
        intent.putExtra("id",id);
        startActivity(intent);
    }
}