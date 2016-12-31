package com.aayushdixit.top10songs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private String mFileContents;
    private Button btnParse;
    private ListView listSongs;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnParse = (Button) findViewById(R.id.btnParse);
        listSongs = (ListView) findViewById(R.id.xmlListView);
        btnParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseSongs parseSongs = new ParseSongs(mFileContents);
                parseSongs.process();
                ArrayAdapter<Song> arrayAdapter = new ArrayAdapter<Song>(
                        MainActivity.this, R.layout.list_item, parseSongs.getSongs());
                listSongs.setAdapter(arrayAdapter);
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=10/xml");

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DownloadData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) { //what happens in the background
            mFileContents = downloadXMLFile(params[0]); //downloads the file
            if (mFileContents == null) {
                Log.d("DownloadData", "Error downloading");
            }
            return mFileContents;
        }

        @Override
        protected void onPostExecute(String result) { //what happens after file is downloaded
            super.onPostExecute(result);
            Log.d("DownloadData", "Result was: " + result);
        }

        private String downloadXMLFile(String urlPath) { //what happens while the file downloads
            StringBuilder tempBuffer = new StringBuilder(); //StringBuilder object to represent the data
            try {
                URL url = new URL(urlPath); //gets the URL Path
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //opens the URL connection from the path
                int response = connection.getResponseCode(); //gets the response code. 200 is true, works.
                Log.d("DownloadData", "The response code was: " + response);
                InputStream is = connection.getInputStream(); //gets the input stream to analyze the data from URL connection
                InputStreamReader isr = new InputStreamReader(is); //reader for the input stream of the data

                int charRead;
                char[] inputBuffer = new char[500];
                while (true) {
                    charRead = isr.read(inputBuffer); //reads the file 500 characters at a time
                    if (charRead <= 0) //if no more characters to read, break out of loop
                    {
                        break;
                    }
                    tempBuffer.append(String.copyValueOf(inputBuffer, 0, charRead)); //appends the String value of the characters to tempBuffer
                }
                return tempBuffer.toString(); //returns the String value of data
            } catch (IOException e) {
                Log.d("DownloadData", "IOException reading data: " + e.getMessage());
                e.printStackTrace();
            } catch (SecurityException e) {
                Log.d("DownloadData", "Security exception. Needs permissions?" + e.getMessage());
            }
            return null;
        }

    }
}
