package com.sies.avengers;

import android.content.Intent;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sies.avengers.getters.GetHash;
import com.sies.avengers.getters.GetValues;
import com.sies.avengers.series.SeriesActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CharacterAdapter.OnSeriesListener{

    private final String privateKey = "";
    private final String publicKey = "";
    private long timestamp = System.currentTimeMillis();
    public ArrayList<String> name = new ArrayList<String>();
    public ArrayList<String> image = new ArrayList<String>();
    public ArrayList<Integer> id = new ArrayList<Integer>();
    public RecyclerView recyclerView;
    CharacterAdapter characterAdapter;
    public GetValues getValues;

    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.series_home_recycler);
        characterAdapter= new CharacterAdapter(name,image, MainActivity.this);
        getSeriesCall();
    }

    private void getSeriesCall() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://gateway.marvel.com:443/v1/public/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MarvelApi marvelApi = retrofit.create(MarvelApi.class);

        GetHash getHash = new GetHash();
        String hashValue = getHash.getMd5(timestamp+privateKey+publicKey);


        Call<GetValues> call = marvelApi.getSeriesValues(timestamp,publicKey,hashValue);

        call.enqueue(new Callback<GetValues>() {
            @Override
            public void onResponse(Call<GetValues> call1, Response<GetValues> response) {
                if(!response.isSuccessful()){
                    textView = findViewById(R.id.series_title);
                    textView.setText("Unable to connect Code: "+ response.code());
                    return;
                }
                getValues = response.body();


                for (int i = 0;i<getValues.getData().getCount();i++){
                    name.add(getValues.getData().results().get(i).getTitle());
                    image.add(getValues.getData().results().get(i).getThumbnail().getPath()+"/portrait_incredible."+getValues.getData().results().get(i).getThumbnail().getExtension());
                    id.add(getValues.getData().results().get(i).getId());
                }
                TextView attribution = findViewById(R.id.attribution_text);
                attribution.setText(getValues.getAttributionText());

                setRecycler();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                textView=findViewById(R.id.series_title);
                textView.setText("Unable to connect\nPlease check your connection");

            }
        });

    }

    private void setRecycler() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(characterAdapter);
    }


    @Override
    public void onSeriesClick(int position) {
        Intent intent = new Intent(this, SeriesActivity.class);
        intent.putExtra("id",id.get(position));
        startActivity(intent);
    }


}
