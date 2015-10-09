package com.example.android.movies;

/**
 * Created by Abd elrhman Arafa on 29/08/2015.
 */
public class GridItem   {
    private final String imageBaseUrl="http://image.tmdb.org/t/p/w185/";
    private String image;
    private String title;
    private String overview;
    private String vote_average;
    private String release_date;
    String id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GridItem() {
        super();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = imageBaseUrl+image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}