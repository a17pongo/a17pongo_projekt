package com.example.brom.listviewjsonapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

// Retrieve data from Internet service using AsyncTask and the included networking code
// Parse the retrieved JSON and update the ListView adapter
// Implement a "refresh" functionality using Android's menu system
public class MainActivity extends AppCompatActivity {
    private List<Mountain> mountains = new ArrayList<>();
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new FetchData().execute();

        adapter = new ArrayAdapter(MainActivity.this, R.layout.view_items, R.id.my_text, mountains);

        ListView listView = (ListView) findViewById(R.id.my_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Mountain testing = new Mountain.getPosition(position);
                    Toast.makeText(MainActivity.this, testing.info(), Toast.LENGTH_SHORT).show();
                }});
    }

    private class FetchData extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... params) {
            // These two variables need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a Java string.
            String jsonStr = null;

            try {
                // Construct the URL for the Internet service
                URL url = new URL("http://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?type=brom");

                // Create the request to the PHP-service, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                jsonStr = buffer.toString();
                return jsonStr;
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in
                // attempting to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Network error", "Error closing stream", e);
                    }
                }
            }
        }
        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);

            try {
                JSONArray json1 = new JSONArray(o);
                adapter.clear();
                for(int i=0; i<json1.length();i++){
                    JSONObject berg = json1.getJSONObject(i);
                    String bergNamn = berg.getString("name");
                    String bergPlats = berg.getString("location");
                    String bergTyp = berg.getString("type");
                    Mountain test = new Mountain(bergNamn,bergPlats,bergTyp);
                    adapter.add(test);
                    Log.d("pontlog",bergPlats);
                }
            } catch (JSONException e) {
                Log.e("brom","E:"+e.getMessage());
            }
        }
    }
}

