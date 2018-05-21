package com.example.brom.listviewjsonapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
    private List<Game> games = new ArrayList<Game>();
    SQLiteDatabase dbR;
    SQLiteDatabase dbW;
    MountainReaderDbHelper mDbHelper;

    private boolean sortSize = false;
    private boolean sortNameDesc = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new FetchData().execute();

        //RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.my_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(new CustomAdapter(games, new CustomAdapter.OnItemClickListener() {
            @Override public void onItemClick(Game item) {
                Intent intent = new Intent(getApplicationContext(), GameDetails.class);

                String gameNameInfo = item.nameInfo();
                String gameCompanyInfo = item.companyInfo();
                String gameCategoryinfo = item.categoryInfo();
                //String gameDescInfo = item.gameDescInfo();
                intent.putExtra("Name", gameNameInfo);
                intent.putExtra("Company", gameCompanyInfo);
                intent.putExtra("Category", gameCategoryinfo);
                //intent.putExtra("Desc", gameDescInfo);
                startActivity(intent);
            }
        }));
        mRecyclerView.setAdapter(mAdapter);
        mDbHelper = new MountainReaderDbHelper(getApplicationContext());
        //Log.d("list",games.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.about:
                Toast.makeText(getApplicationContext(),
                        "An app for everybody that play computer games and want to know if the game will fit on their computer" + "\n"  + "made by: Pontus Göth"
                        ,Toast.LENGTH_LONG).show();
                return true;
            case R.id.refresh:
                mRecyclerView.setAdapter(null);
                games.clear();
                mRecyclerView.setAdapter(new CustomAdapter(games, new CustomAdapter.OnItemClickListener() {
                    @Override public void onItemClick(Game item) {
                        Intent intent = new Intent(getApplicationContext(), GameDetails.class);

                        String gameNameInfo = item.nameInfo();
                        String gameCompanyInfo = item.companyInfo();
                        String gameCategoryinfo = item.categoryInfo();
                        //String gameDescInfo = item.gameDescInfo();
                        intent.putExtra("Name", gameNameInfo);
                        intent.putExtra("Company", gameCompanyInfo);
                        intent.putExtra("Category", gameCategoryinfo);
                        //intent.putExtra("Desc", gameDescInfo);
                        startActivity(intent);
                    }
                }));
                new FetchData().execute();
                return true;
            case R.id.Sort_size:
                sortNameDesc = false;
                sortSize = true;
                readDB();
                return true;
            case R.id.sort_name_desc:
                sortNameDesc = true;
                sortSize = false;
                readDB();
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
                URL url = new URL("http://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?type=a17pongo");

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
                games.clear();
                dbW = mDbHelper.getWritableDatabase();
                mRecyclerView.setAdapter(null);

                for(int i=0; i<json1.length();i++){
                    JSONObject game = json1.getJSONObject(i);
                    String gameNamn = game.getString("name");
                    String gameCompany = game.getString("company");
                    final String gameCategory = game.getString("category");
                    int gameSize = game.getInt("size");
                    //JSONObject gameAux = new JSONObject(game.getString("auxdata"));
                    //String gameDesc = gameAux.getString("desc");

                    ContentValues values = new ContentValues();
                    values.put(MountainReaderContract.MountainEntry.COLUMN_NAME_NAME,gameNamn);
                    values.put(MountainReaderContract.MountainEntry.COLUMN_NAME_COMPANY,gameCompany);
                    values.put(MountainReaderContract.MountainEntry.COLUMN_NAME_CATEGORY,gameCategory);
                    values.put(MountainReaderContract.MountainEntry.COLUMN_NAME_SIZE,gameSize);
                    //values.put(MountainReaderContract.MountainEntry.COLUMN_NAME_DESC,gameDesc);

                    dbW.insertWithOnConflict(MountainReaderContract.MountainEntry.TABLE_NAME, null, values,SQLiteDatabase.CONFLICT_IGNORE);

                    mRecyclerView.setAdapter(new CustomAdapter(games, new CustomAdapter.OnItemClickListener() {
                        @Override public void onItemClick(Game item) {
                            Intent intent = new Intent(getApplicationContext(), GameDetails.class);

                            String gameNameInfo = item.nameInfo();
                            String gameCompanyInfo = item.companyInfo();
                            String gameCategoryinfo = item.categoryInfo();
                            //String gameDescInfo = item.gameDescInfo();
                            intent.putExtra("Name", gameNameInfo);
                            intent.putExtra("Company", gameCompanyInfo);
                            intent.putExtra("Category", gameCategoryinfo);
                            //intent.putExtra("Desc", gameDescInfo);
                            startActivity(intent);
                        }
                    }));
                    //Log.d("aux",gameDesc);
                }
                readDB();
            } catch (JSONException e) {
                Log.e("a17pongo","E:"+e.getMessage());
            }
        }
    }



    public void readDB(){

        mRecyclerView.setAdapter(null);
        games.clear();
        mRecyclerView.setAdapter(new CustomAdapter(games, new CustomAdapter.OnItemClickListener() {
            @Override public void onItemClick(Game item) {
                Intent intent = new Intent(getApplicationContext(), GameDetails.class);

                String gameNameInfo = item.nameInfo();
                String gameCompanyInfo = item.companyInfo();
                String gameCategoryinfo = item.categoryInfo();
                //String gameDescInfo = item.gameDescInfo();
                intent.putExtra("Name", gameNameInfo);
                intent.putExtra("Company", gameCompanyInfo);
                intent.putExtra("Category", gameCategoryinfo);
                //intent.putExtra("Desc", gameDescInfo);
                startActivity(intent);
            }
        }));

        dbR = mDbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                MountainReaderContract.MountainEntry.COLUMN_NAME_NAME,
                MountainReaderContract.MountainEntry.COLUMN_NAME_COMPANY,
                MountainReaderContract.MountainEntry.COLUMN_NAME_CATEGORY,
                MountainReaderContract.MountainEntry.COLUMN_NAME_SIZE,
                //MountainReaderContract.MountainEntry.COLUMN_NAME_DESC
        };

        String sortOrder = MountainReaderContract.MountainEntry.COLUMN_NAME_NAME  + " ASC";

        if(sortNameDesc){
            sortOrder = MountainReaderContract.MountainEntry.COLUMN_NAME_NAME  + " DESC";
        }else if(sortSize){
            sortOrder = MountainReaderContract.MountainEntry.COLUMN_NAME_SIZE  + " ASC";
        }

        Cursor cursor = dbR.query(
                MountainReaderContract.MountainEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        while(cursor.moveToNext()) {
            String gameNamn = cursor.getString(cursor.getColumnIndexOrThrow(MountainReaderContract.MountainEntry.COLUMN_NAME_NAME));
            String gameCompany = cursor.getString(cursor.getColumnIndexOrThrow(MountainReaderContract.MountainEntry.COLUMN_NAME_COMPANY));
            String gameCategory = cursor.getString(cursor.getColumnIndexOrThrow(MountainReaderContract.MountainEntry.COLUMN_NAME_CATEGORY));
            int gameSize = cursor.getInt(cursor.getColumnIndexOrThrow(MountainReaderContract.MountainEntry.COLUMN_NAME_SIZE));
            //String gameDesc = cursor.getString(cursor.getColumnIndexOrThrow(MountainReaderContract.MountainEntry.COLUMN_NAME_DESC));

            Game gm = new Game(gameNamn,gameCompany,gameCategory,gameSize);
            games.add(gm);
        }
        cursor.close();
    }
}