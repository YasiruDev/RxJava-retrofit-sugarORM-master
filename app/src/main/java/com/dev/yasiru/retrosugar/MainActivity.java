package com.dev.yasiru.retrosugar;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.yasiru.retrosugar.REST.APIinterface;
import com.dev.yasiru.retrosugar.DB.Movie;
import com.dev.yasiru.retrosugar.REST.APIClient;
import com.dev.yasiru.retrosugar.REST.APIinterface;
import com.dev.yasiru.retrosugar.REST.MovieResponce;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class MainActivity extends Activity {
    private final static String API_KEY = "7e8f60e325cd06e164799af1e317d7a7";
    APIinterface apIinterface;
    @BindView(R.id.click) Button mLoad;
    @BindView(R.id.deslist)ListView mMovieList;
    @BindView(R.id.myspin)Spinner mSpinList;

    ArrayList<String> movie_array=new ArrayList<String>();
    ArrayAdapter adapter,desc_adapter;
    String selmovie;
    ConnectivityManager cm;
    private Subscription loaddatasubscription,loaddescsubcription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SugarContext.init(this);
        ButterKnife.bind(this);
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        DBbackup.copyDatabase(this);
        apIinterface= APIClient.getClient().create(APIinterface.class);

        checkconnection ();

        RxView.clicks(mLoad).subscribe(loadAction);

    }

    Action1 loadAction=new Action1() {
        @Override
        public void call(Object o) {
            if ((cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) || (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {

                loadmovilist();
            }else {
                Toast.makeText(MainActivity.this,"Please check your Internet connection",Toast.LENGTH_LONG).show();
            }
        }
    };

    //check data,wifi
    public void checkconnection (){

            getmovies();

    }

    //Retrofit
    public void getmovies(){

        Call<MovieResponce> call=apIinterface.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MovieResponce>() {
            @Override
            public void onResponse(Call<MovieResponce> call, Response<MovieResponce> response) {

                List<Movie> mv= response.body().getResults();

                Log.d("movieresult"," "+mv.size() );

                for(Movie m :mv){

                    Log.d("movieresult"," "+m.getTitle() );

                    m.save();
                }
            }

            @Override
            public void onFailure(Call<MovieResponce> call, Throwable t) {

            }
        });
    }

    //SugarORM sQLite
    public List<String> getdata(){

        List<String> type = new ArrayList<String>();

        List<Movie> data=Movie.findWithQuery(Movie.class,"select * from Movie");

        for (Movie m:data){

            type.add(m.getTitle());
        }

        return type;
    }

    //load movies RXJAVA
    public void loadmovilist(){
        loaddatasubscription= Observable.just(getdata())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<String> strings) {


                        adapter=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,strings);
                        mSpinList.setAdapter(adapter);
                    }
                });

        descrip();
    }

    public void descrip(){

        mSpinList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selmovie=mSpinList.getSelectedItem().toString();
                loaddesc();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    public List<String> getdescdata(){

        List<String> descdata = new ArrayList<String>();

        if(!selmovie.contentEquals("")){
        List<Movie> data=Movie.findWithQuery(Movie.class,"select * from Movie where title='"+selmovie+"'");

        for (Movie m:data){

            descdata.add(m.getOverview());
        }
        }
        return descdata;


    }
    public void loaddesc(){

        loaddescsubcription=Observable.just(getdescdata())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<String> strings) {
                        desc_adapter=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,strings);
                        mMovieList.setAdapter(desc_adapter);
                    }
                });

    }


}
