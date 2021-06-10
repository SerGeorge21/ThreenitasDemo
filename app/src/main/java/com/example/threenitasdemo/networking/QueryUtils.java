package com.example.threenitasdemo.networking;

import android.text.TextUtils;
import android.util.Log;

import com.example.threenitasdemo.Book;
import com.example.threenitasdemo.ListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class QueryUtils {

    //private static final String ACCESS_TOKEN = "T1amGT21.Idup.298885bf38e99053dca3434eb59c6aa";

    private QueryUtils(){
        //private constructor because no one should ever create QueryUtils object
    }

    public static  ArrayList<Book> fetchBookData(String requestUrl){
        //Create URL object
        URL url = createUrl(requestUrl);

        //Perform HTTP request to the URL to receive JSON response
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        }catch(IOException e){
            e.printStackTrace();
            Log.e("ERROR", "Problem making HTTP request");
        }

        ArrayList<Book> books = extractBooksFromJson(jsonResponse);
        return books;
    }

    private static URL createUrl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        }catch(MalformedURLException e) {
            e.printStackTrace();
            Log.e("ERROR", "Problem building URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String token = ListActivity.token;
        String jsonResponse = "";

        //if passed url is null return early
        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            //urlConnection.setReadTimeout(10000 /*milliseconds*/);
            //urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Authorization", "Bearer " + token);
            urlConnection.connect();

            if(urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);

            }else{
                Log.e("--------ERROR--------", "---------------------------Error response code: " + urlConnection.getResponseCode());
            }

        }catch(IOException e){
            Log.e("ERROR", "Problem retrieving the book JSON results.", e);
        }finally{
            if(urlConnection != null) urlConnection.disconnect();

            if(inputStream !=null) inputStream.close();
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null){
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString(); //jsonResponse
    }

    private static ArrayList<Book> extractBooksFromJson(String response){
        if(TextUtils.isEmpty(response)) return null;
        ArrayList<Book> books = new ArrayList<>();

        try{
            JSONArray root = new JSONArray(response); //Convert String in JSONObject
            for(int i=0; i<root.length(); i++){
                JSONObject book = root.getJSONObject(i);
                // Extract values
                int id = book.getInt("id");
                String title = book.getString("title");
                String img_url = book.getString("img_url");
                String date_released = book.getString("date_released");
                String pdf_url = book.getString("pdf_url");
                Book b = new Book(id, title, img_url, pdf_url, date_released);
                books.add(b);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return books;
    }

}
