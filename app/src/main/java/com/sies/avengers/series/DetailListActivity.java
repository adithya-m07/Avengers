package com.sies.avengers.series;

import android.content.Intent;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sies.avengers.*;
import com.sies.avengers.getters.GetHash;
import com.sies.avengers.getters.GetValues;
import com.sies.avengers.series.details.DetailsActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.Objects;


public class DetailListActivity extends AppCompatActivity  implements CharacterListAdapter.OnCharacterListener,ComicListAdapter.OnComicListener {

    private final String privateKey = "";
    private final String publicKey = "";
    private String hashValue;
    private int offset = 0;
    private final long timestamp = System.currentTimeMillis();
    private final int iniLimit = 20;
    private final int finalLimit = 100;
    private int id;
    private String from;
    public ArrayList<String> name = new ArrayList<>();
    public ArrayList<String> image = new ArrayList<>();
    public ArrayList<String> url = new ArrayList<>();
    public ArrayList<String> desc = new ArrayList<>();
    MarvelApi marvelApi;
    GridLayoutManager gridLayoutManager;
    GetValues getValues;
    CharacterListAdapter characterListAdapter;
    ComicListAdapter comicListAdapter;
    Call<GetValues> characterCall;
    Call<GetValues> comicCall;
    RecyclerView recyclerView;
    private DetailScroll detailScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list);
        Intent fromIntent = getIntent();
        id = fromIntent.getIntExtra("id", -1);
        from = fromIntent.getStringExtra("from");
        recyclerView = findViewById(R.id.complete_recycler);
        gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        ImageButton imageButton = findViewById(R.id.complete_back_button);
        imageButton.setOnClickListener(v -> onBackPressed());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://gateway.marvel.com:443/v1/public/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        marvelApi = retrofit.create(MarvelApi.class);

        GetHash getHash = new GetHash();
        hashValue = getHash.getMd5(timestamp + privateKey + publicKey);

        if(from.equals("character")){
            TextView title = findViewById(R.id.complete_title);
            title.setText("All Characters");
            getAllCharacter();
        }
        else{
            TextView title = findViewById(R.id.complete_title);
            title.setText("All Comics");
            getAllComics();
        }
    }

    private void getAllCharacter() {
        characterCall = marvelApi.getCharacterData(id, timestamp, publicKey, hashValue, iniLimit, offset);
        characterCall.enqueue(new Callback<GetValues>() {
            @Override
            public void onResponse(Call<GetValues> call, Response<GetValues> response) {
                if(!response.isSuccessful()){
                    TextView title = findViewById(R.id.complete_title);
                    title.setText("Unable to Connect\nCode: "+response.code());
                    return;
                }
                getValues = response.body();
                for (int i = 0; i < getValues.getData().getCount(); i++) {
                    name.add(getValues.getData().results().get(i).getName());
                    image.add(getValues.getData().results().get(i).getThumbnail().getPath() + "/portrait_incredible." + getValues.getData().results().get(i).getThumbnail().getExtension());
                    desc.add(getValues.getData().results().get(i).getDescription());
                    url.add(getValues.getData().results().get(i).getUrls().get(1).getUrl());
                }

                characterListAdapter = new CharacterListAdapter(name, image, DetailListActivity.this);
                recyclerView.setAdapter(characterListAdapter);
            }

            @Override
            public void onFailure(Call<GetValues> call, Throwable t) {
                TextView title = findViewById(R.id.complete_title);
                title.setText("Unable to connect\nPlease check your connection");
            }
        });
        detailScroll = new DetailScroll(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromCharacter(page);
            }
        };
        recyclerView.addOnScrollListener(detailScroll);
    }

    private void getAllComics() {
        comicCall = marvelApi.getComicData(id, timestamp, publicKey, hashValue, iniLimit, offset);
        comicCall.enqueue(new Callback<GetValues>() {
            @Override
            public void onResponse(Call<GetValues> call, Response<GetValues> response) {
                if(!response.isSuccessful()){
                    TextView title = findViewById(R.id.complete_title);
                    title.setText("Unable to Connect\nCode: "+response.code());
                    return;
                }
                getValues = response.body();
                for (int i = 0; i < getValues.getData().getCount(); i++) {
                    name.add(getValues.getData().results().get(i).getTitle());
                    image.add(getValues.getData().results().get(i).getThumbnail().getPath() + "/portrait_incredible." + getValues.getData().results().get(i).getThumbnail().getExtension());
                    desc.add(getValues.getData().results().get(i).getDescription());
                    url.add(getValues.getData().results().get(i).getUrls().get(1).getUrl());
                }

                comicListAdapter = new ComicListAdapter(name, image, DetailListActivity.this);
                recyclerView.setAdapter(comicListAdapter);
            }

            @Override
            public void onFailure(Call<GetValues> call, Throwable t) {
                TextView title = findViewById(R.id.complete_title);
                title.setText("Unable to connect\nPlease check your connection");
            }
        });
        detailScroll = new DetailScroll(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromComic(page);
            }
        };
        recyclerView.addOnScrollListener(detailScroll);
    }

    private void loadNextDataFromCharacter(int page) {
        characterCall = marvelApi.getCharacterData(id, timestamp, publicKey, hashValue, finalLimit, page * 100);

        characterCall.enqueue(new Callback<GetValues>() {
            @Override
            public void onResponse(Call<GetValues> call, Response<GetValues> response) {
                if(!response.isSuccessful()){
                    TextView title = findViewById(R.id.complete_title);
                    title.setText("Unable to Connect\nCode: "+response.code());
                    return;
                }
                getValues = response.body();
                for (int i = 0; i < getValues.getData().getCount(); i++) {
                    name.add(getValues.getData().results().get(i).getName());
                    image.add(getValues.getData().results().get(i).getThumbnail().getPath() + "/portrait_incredible." + getValues.getData().results().get(i).getThumbnail().getExtension());
                    desc.add(getValues.getData().results().get(i).getDescription());
                    url.add(getValues.getData().results().get(i).getUrls().get(1).getUrl());
                }

                Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<GetValues> call, Throwable t) {
                TextView title = findViewById(R.id.complete_title);
                title.setText("Unable to connect\nPlease check your connection");
            }
        });
    }

    private void loadNextDataFromComic(int page) {
        comicCall = marvelApi.getComicData(id, timestamp, publicKey, hashValue, finalLimit, page * 100);

        comicCall.enqueue(new Callback<GetValues>() {
            @Override
            public void onResponse(Call<GetValues> call, Response<GetValues> response) {
                if(!response.isSuccessful()){
                    TextView title = findViewById(R.id.complete_title);
                    title.setText("Unable to Connect\nCode: "+response.code());
                    return;
                }
                getValues = response.body();
                for (int i = 0; i < getValues.getData().getCount(); i++) {
                    name.add(getValues.getData().results().get(i).getTitle());
                    image.add(getValues.getData().results().get(i).getThumbnail().getPath() + "/portrait_incredible." + getValues.getData().results().get(i).getThumbnail().getExtension());
                    desc.add(getValues.getData().results().get(i).getDescription());
                    url.add(getValues.getData().results().get(i).getUrls().get(1).getUrl());
                }
                Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<GetValues> call, Throwable t) {
                TextView title = findViewById(R.id.complete_title);
                title.setText("Unable to connect\nPlease check your connection");
            }
        });
    }

    @Override
    public void onCharacterClick(int position) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("name", name.get(position));
        intent.putExtra("image", image.get(position));
        intent.putExtra("description", desc.get(position));
        intent.putExtra("url", url.get(position) );
        startActivity(intent);
    }

    @Override
    public void onComicClick(int position) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("name", name.get(position));
        intent.putExtra("image", image.get(position));
        intent.putExtra("description", desc.get(position));
        intent.putExtra("url", url.get(position) );
        startActivity(intent);
    }
}




