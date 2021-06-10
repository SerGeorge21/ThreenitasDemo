package com.example.threenitasdemo;

public class Book {

    private int id;
    private String title;
    private String imgUrl;
    private String pdfUrl;
    private String dateReleased;

    public boolean isDownloaded = false;

    public Book(int id, String title, String imgUrl, String pdfUrl, String dateReleased){
        this.id = id;
        this.title = title;
        this.imgUrl = imgUrl;
        this.pdfUrl = pdfUrl;
        this.dateReleased = dateReleased;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getImgUrl() { return imgUrl; }

    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }

    public String getPdfUrl() { return pdfUrl; }

    public void setPdfUrl(String pdfUrl) { this.pdfUrl = pdfUrl; }

    public String getDateReleased() { return dateReleased; }

    public void setDateReleased(String dateReleased) { this.dateReleased = dateReleased; }

    public String toString(){
        return "Title: " + title + "  Release Date: " + dateReleased;
    }
}
