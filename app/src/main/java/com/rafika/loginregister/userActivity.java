package com.rafika.loginregister;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class userActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    ListView list;
    SwipeRefreshLayout swipe;
    List<data> itemList = new ArrayList<>();
    akunAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user2);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        list = (ListView) findViewById(R.id.listview);

        adapter = new akunAdapter(userActivity.this, itemList);
        list.setAdapter(adapter);

        try {
            swipe.setOnRefreshListener(this);

            swipe.post(new Runnable() {
                @Override
                public void run() {
                    swipe.setRefreshing(true);
                    itemList.clear();
                    adapter.notifyDataSetChanged();
                    callVolley();
                }
            });
        } catch (Exception e) {
            Log.d("Test", e.getMessage());
        }

    }
    @Override
    public  void  onRefresh(){
        itemList.clear();
        adapter.notifyDataSetChanged();
        callVolley();
    }
    public void callVolley(){
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        JsonArrayRequest jArr = new JsonArrayRequest(Db_Contract.URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        data item = new data();

                        item.setUsername(obj.getString("username"));
                        item.setNama_lengkap(obj.getString("nama_lengkap"));

                        itemList.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifDataSetChanged();
                swipe.setRefreshing(false);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

    });
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mRequestQueue.add(jArr);
    }
}