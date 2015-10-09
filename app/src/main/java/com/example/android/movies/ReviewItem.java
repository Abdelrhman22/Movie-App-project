package com.example.android.movies;


public class ReviewItem  implements  ParentItemInterface{

    String ID;
    String author;
    String content;
    String url;



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public void setId(String id) {
        this.ID=id;
    }

    @Override
    public String getId() {
        return this.ID;
    }

    public  static int ReviewItem=2;
    @Override
    public int getListType() {
        return this.ReviewItem;
    }

    @Override
    public void setListType(int type) {

    }
}
