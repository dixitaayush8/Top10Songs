package com.aayushdixit.top10songs;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by Aayush on 10/8/2016.
 */
public class ParseSongs {
    private String xmlData;
    private ArrayList<Song> songs;
    //private ImageView view;
   // private Context context;
   // private ImageView target;

    public ParseSongs(String xmlData)
    {
       // view = (ImageView) findViewById(R.id.xmlImageView);
       // target = new ImageView(context);
        this.xmlData = xmlData;
        songs = new ArrayList<Song>(); //ArrayList of Song objects
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public boolean process() //processes the songs from the RSS site to the app
    {
        boolean status = true;
        Song songRecord = null;
        boolean inEntry = false;
        String textValue = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); //new instance of XML pull parser factory, used to create XML pull parsers
            factory.setNamespaceAware(true); //provides support for XML namespaces
            XmlPullParser xpp = factory.newPullParser(); //pulls the XML data
            xpp.setInput(new StringReader(this.xmlData)); //converts data to StringReader
            int eventType = xpp.getEventType(); //gets event type for the XML Pull Parser
            while(eventType != XmlPullParser.END_DOCUMENT) { //while xml pull parser is not at the end of the document
                String tagName = xpp.getName(); //gets the name tag in the data
                switch (eventType) {
                    case XmlPullParser.START_TAG: //starts the tag for the data
                        //Log.d("ParseSongs", "Starting tag for " + tagName);
                        if (tagName.equalsIgnoreCase("entry")) {
                            inEntry = true;
                            songRecord = new Song(); //declares new Song if there's an entry
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText(); //gets the text
                        break;
                    case XmlPullParser.END_TAG: //at the end of the tag
                        //Log.d("ParseSongs", "Ending tag for " + tagName);
                        if(inEntry) {
                            if(tagName.equalsIgnoreCase("entry"))
                            {
                                songs.add(songRecord); //adds the song to the ArrayList of songs
                                inEntry = false; //inEntry is changed to false
                            }
                            else if(tagName.equalsIgnoreCase("title"))
                            {
                                songRecord.setName(textValue); //sets name to the title
                            }
                            else if(tagName.equalsIgnoreCase("artist"))
                            {
                                songRecord.setArtist(textValue); //sets artist
                            }
                            else if(tagName.equalsIgnoreCase("releaseDate"))
                            {
                                songRecord.setReleaseDate(textValue); //sets release date
                            }
                   
                        }
                        break;

                    default:
                        //Nothing else to do
                }
                eventType = xpp.next(); //goes to the next data object
            }
        }
        catch(Exception e)
        {
            status = false;
            e.printStackTrace(); //if exception is caught, print the stack trace
        }

        for(Song s : songs)
        {
            Log.d("ParseSongs","********");
            Log.d("ParseSongs","Name: " + s.getName());
            Log.d("ParseSongs","Artist: " + s.getArtist());
            Log.d("ParseSongs","Release Date: " + s.getReleaseDate());
            Log.d("ParseSongs","Photo URL: " + s.getPhoto());
        }
        return true;
    }
}
