package com.example.threenitasdemo.networking;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.threenitasdemo.Book;

import java.util.ArrayList;

public class BookLoader extends AsyncTaskLoader {

    String mUrl;


    public BookLoader(Context context, String url){
        super(context);
        mUrl = url;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Book> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        ArrayList<Book> books = QueryUtils.fetchBookData(mUrl);
        return books;
    }
}
