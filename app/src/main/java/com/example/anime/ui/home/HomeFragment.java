package com.example.anime.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.anime.ArrayAdapterTopAnimes;
import com.example.anime.JikanApi_TopAnime;
import com.example.anime.R;
import com.example.anime.TopAnimeResponse;
import com.example.anime.Anime;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment {

    private static final String BASE_URL = "https://api.jikan.moe/v4/";
    private ListView listviewtopanime;
    ArrayAdapterTopAnimes adapterTopAnimes;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        listviewtopanime = rootView.findViewById(R.id.listview);
        List<Anime> animes = new ArrayList<>();
        adapterTopAnimes = new ArrayAdapterTopAnimes(getContext(), R.layout.item_listview_top_anime, animes);
        listviewtopanime.setAdapter(adapterTopAnimes);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JikanApi_TopAnime jikanApiTopAnime = retrofit.create(JikanApi_TopAnime.class);
        Call<TopAnimeResponse> call = jikanApiTopAnime.getTopAnimeByPopularity();

        call.enqueue(new Callback<TopAnimeResponse>() {
            @Override
            public void onResponse(Call<TopAnimeResponse> call, Response<TopAnimeResponse> response) {
                if (response.isSuccessful()) {
                    TopAnimeResponse topAnimeResponse = response.body();
                    if (topAnimeResponse != null) {
                        List<Anime> animeList = topAnimeResponse.getData();
                        adapterTopAnimes.clear();
                        adapterTopAnimes.addAll(animeList);
                    } else {
                        Toast.makeText(getContext(), "Respuesta vacía", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TopAnimeResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        listviewtopanime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Anime selectedAnime=(Anime) adapterTopAnimes.getItem(position);
                Intent intent=new Intent(getActivity(), HomeExtended.class);
                intent.putExtra("selectedAnime", selectedAnime);
                startActivity(intent);
            }
        });

        return rootView;

    }

}

