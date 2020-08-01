package com.sies.avengers;
import com.sies.avengers.getters.GetValues;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MarvelApi {

   // String series = "Avengers";

    @GET("series?title=Avengers&orderBy=startYear")
    Call<GetValues> getSeriesValues(
            @Query("ts") long ts,
            @Query("apikey") String apiKey,
            @Query("hash") String hashValue
    );

    @GET("series/{id}")
    Call<GetValues> getSeriesData(
            @Path("id") int id,
            @Query("ts") long ts,
            @Query("apikey") String apiKey,
            @Query("hash") String hashValue
    );

    @GET("characters")
    Call<GetValues> getCharacterData(
            @Query("series") int series,
            @Query("ts") long ts,
            @Query("apikey") String apiKey,
            @Query("hash") String hashValue,
            @Query("limit") Integer limit,
            @Query("offset") Integer offset
    );

    @GET("comics?orderBy=issueNumber")
    Call<GetValues> getComicData(
            @Query("series") int series,
            @Query("ts") long ts,
            @Query("apikey") String apiKey,
            @Query("hash") String hashValue,
            @Query("limit") Integer limit,
            @Query("offset") Integer offset
    );
}
