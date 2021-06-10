package com.example.threenitasdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.threenitasdemo.networking.BookLoader;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<ArrayList<Book>> {

    private static final int BOOK_LOADER_ID = 1;
    private static final String THREENITAS_REQUEST_URL = "https://3nt-demo-backend.azurewebsites.net/Access/Books"; //BASE URL
    public static String token = "";

    ArrayList<String> booksArrayList;
    BookAdapter booksAdapter; //maybe needs to be of type string?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //get token from MainActivity
        Intent i = getIntent();
        token = i.getStringExtra("TOKEN");

        GridView listView = (GridView) findViewById(R.id.list1);

        booksArrayList = new ArrayList<String>();

        booksAdapter = new BookAdapter(ListActivity.this, new ArrayList<Book>(), this);

        listView.setAdapter(booksAdapter);


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        /*NetworkInfo class seems to be depricated but I also checked
        https://developer.android.com/training/monitoring-device-state/connectivity-status-type
         */
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(BOOK_LOADER_ID, null, this);

        }else{
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, THREENITAS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> books) {
        booksAdapter.clear();
        if(books != null && !books.isEmpty()){
            booksAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        booksAdapter.clear();
    }
}