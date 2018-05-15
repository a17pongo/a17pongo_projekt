package com.example.brom.listviewjsonapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Mountain> mountains = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new FetchData().execute();

        //RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_list);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter = new CustomAdapter(mountains, new CustomAdapter.OnItemClickListener() {
            @Override public void onItemClick(Mountain item) {
                Intent intent = new Intent(getApplicationContext(), MountainDetails.class);
                //Toast.makeText(getApplicationContext(), item.info(), Toast.LENGTH_LONG).show();

                String nameInfo = item.nameInfo();
                String locationInfo = item.locationInfo();
                String heightInfo = item.heightInfo();
                intent.putExtra("Name", nameInfo);
                intent.putExtra("Location", locationInfo);
                intent.putExtra("Height", heightInfo);
                intent.putExtra("Image", item.imageUrl());
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh:
                mRecyclerView.setAdapter(null);
                mountains.clear();
                mRecyclerView.setAdapter(new CustomAdapter(mountains, new CustomAdapter.OnItemClickListener() {
                    @Override public void onItemClick(Mountain item) {
                        Intent intent = new Intent(getApplicationContext(), MountainDetails.class);
                        //Toast.makeText(getApplicationContext(), item.info(), Toast.LENGTH_LONG).show();

                        String nameInfo = item.nameInfo();
                        String locationInfo = item.locationInfo();
                        String heightInfo = item.heightInfo();
                        intent.putExtra("Name", nameInfo);
                        intent.putExtra("Location", locationInfo);
                        intent.putExtra("Height", heightInfo);
                        intent.putExtra("Image", item.imageUrl());
                        startActivity(intent);
                    }
                }));
                new FetchData().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                mRecyclerView.setAdapter(null);
                mountains.clear();
                for(int i=0; i<json1.length();i++){
                    JSONObject berg = json1.getJSONObject(i);
                    String bergNamn = berg.getString("name");
                    String bergTyp = berg.getString("type");
                    String bergPlats = berg.getString("location");
                    String bergComp = berg.getString("company");
                    int bergId = berg.getInt("ID");
                    String bergCategory = berg.getString("category");
                    int bergSize = berg.getInt("size");
                    int bergCost = berg.getInt("cost");
                    JSONObject bergAux = new JSONObject(berg.getString("auxdata"));
                    String bergImg = bergAux.getString("img");
                    String bergUrl = bergAux.getString("url");

                    Mountain m = new Mountain(bergNamn,bergTyp,bergPlats,bergId,bergComp,bergCategory,bergSize,bergCost,bergImg,bergUrl);
                    mountains.add(m);
                    mRecyclerView.setAdapter(new CustomAdapter(mountains, new CustomAdapter.OnItemClickListener() {
                        @Override public void onItemClick(Mountain item) {
                            Intent intent = new Intent(getApplicationContext(), MountainDetails.class);

                            String nameInfo = item.nameInfo();
                            String locationInfo = item.locationInfo();
                            String heightInfo = item.heightInfo();
                            intent.putExtra("Name", nameInfo);
                            intent.putExtra("Location", locationInfo);
                            intent.putExtra("Height", heightInfo);
                            intent.putExtra("Image", item.imageUrl());
                            startActivity(intent);
                        }
                    }));
                }
            } catch (JSONException e) {
                Log.e("brom","E:"+e.getMessage());
            }
        }
    }
}

