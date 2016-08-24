package recyclerview.prithvi.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import recyclerview.prithvi.R;
import recyclerview.prithvi.adapter.LinearAdapter;
import recyclerview.prithvi.model.UnsplashData;
import recyclerview.prithvi.utils.VolleySingleton;

import static recyclerview.prithvi.model.Endpoints.*;
import static recyclerview.prithvi.model.ResponseKeys.*;

public class LinearActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue = null;
    private ArrayList<UnsplashData> unsplashList = new ArrayList();
    private LinearAdapter moviesAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private int visibleItemCount, totalItemCount, firstVisibleItem;
    private int previousTotal = 0, pageCount = 1, visibleThreshold = 4;
    private boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear);

        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getRequestQueue();
        mLinearLayoutManager = new LinearLayoutManager(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvUnsplash);
        moviesAdapter = new LinearAdapter(unsplashList, getApplicationContext());

        sendJSONRequest(PHOTOS_URL);
        setUpRecyclerView(mRecyclerView);
    }

    public void sendJSONRequest(String url){
        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        unsplashList = parseJSONResponse(response);
                        moviesAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(request);
    }

    private ArrayList<UnsplashData> parseJSONResponse(JSONArray response){

        try{
            for(int i = 0; i < response.length(); i++){
                String id, width, height, urlRegular;
                JSONObject image, urls;
                image = (JSONObject) response.get(i);
                id = image.getString(IMAGE_ID);
                width = image.getString(IMAGE_WIDTH);
                height = image.getString(IMAGE_HEIGHT);
                urls = image.getJSONObject(IMAGE_URLS);
                urlRegular = urls.getString(IMAGE_URLS_REGULAR);

                UnsplashData unsplashData = new UnsplashData();
                unsplashData.setId(id);
                unsplashData.setWidth(width);
                unsplashData.setHeight(height);
                unsplashData.setUrlRegular(urlRegular);
                unsplashList.add(unsplashData);
            }
        }catch (JSONException e){

        }
        return unsplashList;
    }

    private void setUpRecyclerView(RecyclerView rv){
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setLayoutManager(mLinearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                super.onScrolled(rv, dx, dy);

                String url;

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mLinearLayoutManager.getItemCount();
                firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                        pageCount++;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)){
                    url = PHOTOS_URL + "&page=" + String.valueOf(pageCount);
                    sendJSONRequest(url);
                    loading = true;
                }
            }
        });
        rv.setAdapter(moviesAdapter);
    }
}
