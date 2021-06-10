package com.example.threenitasdemo;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BookAdapter extends ArrayAdapter<Book> {

    ListActivity activity;

    public long downloadId;

    public BookAdapter(Context context, List<Book> books, ListActivity activity){
        super(context,0,books);
        this.activity = activity;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent)

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Book currentBook = getItem(position);

        TextView title =(TextView) listItemView.findViewById(R.id.bookTitle);
        title.setText(currentBook.getTitle());
        //TODO: GLIDE FOR IMAGE
        ImageView img = (ImageView) listItemView.findViewById(R.id.bookImage);
        Glide.with(activity.getApplicationContext())
                .load(currentBook.getImgUrl())
                //.load("https://cdn.pixabay.com/photo/2017/01/19/09/10/logo-google-1991833__340.png")
                .centerCrop()
                .into(img);

        //Download resource pdf
        ImageView downloadButton = (ImageView) listItemView.findViewById(R.id.downloadButton);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentBook.isDownloaded) return;
                currentBook.isDownloaded = true;
                downloadButton.setBackgroundResource(R.drawable.ic_baseline_done_24);
                //start downloading
                startNewDownload(currentBook.getPdfUrl(), currentBook.getTitle());
            }
        });

        return listItemView;
    }

    public void startNewDownload(String url, String title) {
        File file = new File(activity.getExternalFilesDir(null), "DummyBooks");
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url)); /*init a request*/
        request.setDescription("My description"); //this description apears inthe android notification
        request.setTitle(title);//this description apears inthe android notification
        request.setDestinationUri(Uri.fromFile(file));
        DownloadManager manager = (DownloadManager) activity.getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        downloadId = manager.enqueue(request); //start the download and return the id of the download. this id can be used to get info about the file (the size, the download progress ...) you can also stop the download by using this id
    }

}
