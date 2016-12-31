package com.aayushdixit.top10songs;

import android.widget.ImageView;

/**
 * Created by Aayush on 10/8/2016.
 */
public class Song {

    private String name; //instance variable for name
    private String artist; //instance variable for artist
    private String releaseDate; //instance variable for release date
    private String photo;

    public String getName() {
        return name;
    } //returns the name

    public String getArtist() {
        return artist;
    } //returns the artist

    public String getReleaseDate() {
        return releaseDate;
    } //returns the release date

    public String getPhoto()
    {
        return photo;
    }

    public void setName(String name) {
        this.name = name;
    } // sets name of the Song object

    public void setArtist(String artist) {
        this.artist = artist;
    } //sets artist of the song object

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    } //sets release date of the song object

    public void setPhoto(String photo)
    {
        this.photo = photo;
    }

    public String toString() //overrides toString method
    {
        return ("Name: " + getName() + "\n" + "Artist: " + getArtist() + "\n" + "Release Date: " + getReleaseDate() + "\n");
    }
}
