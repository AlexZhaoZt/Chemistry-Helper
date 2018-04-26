package edu.illinois.cs.cs125.chemHelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class searchResult extends AppCompatActivity {

    private static RequestQueue requestQueue;
    private static final String TAG = "Search Result";
    public String api = "jgXxAMt3w8GXg3aZaAkFJHTN5xBYaVLD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        String chemical = null;
        if (getIntent().hasExtra("searchContent")) {
            chemical = getIntent().getExtras().getString("searchContent");
        }
        boolean checked = getIntent().getExtras().getBoolean("option");
        if (chemical != null && !checked) {
            APICallNameGetQueryID(chemical);
        } else if (chemical != null && checked) {
            //implement
        } else {
            //make textview display error message.
        }

    }
    void APICallNameGetQueryID(String chemical) {
        JSONObject jsonReq = new JSONObject();
        try {
            jsonReq.put("name", chemical);
            jsonReq.put("orderBy", "");
            jsonReq.put("orderDirection", "");
        } catch (JSONException ignored) {}
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    "https://api.rsc.org/compounds/v1/filter/name",
                    jsonReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                String queryId = response.getString("queryId");
                                JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(
                                        "https://api.rsc.org/compounds/v1/filter/"
                                                + queryId + "/results?start=0&count=1",
                                        null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    String chemID = response.getJSONArray("results").get(0).toString();
                                                    JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(
                                                            "https://api.rsc.org/compounds/v1/records/"
                                                                    + chemID + "/details?fields=SMILES,Formula,CommonName,MolecularWeight",
                                                            null,
                                                            new Response.Listener<JSONObject>() {
                                                                @Override
                                                                public void onResponse(JSONObject response) {
                                                                    try {
                                                                        String smiles = response.getString("smiles");
                                                                    } catch (JSONException ignored) {
                                                                        //implement
                                                                    }
                                                                }
                                                            }, new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Log.e(TAG, error.toString());
                                                        }
                                                    }

                                                    )
                                                } catch (JSONException ignored) {}
                                            }
                                        }

                                );
                            } catch (JSONException ignored) {
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.e(TAG, error.toString());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("apikey", api);
                    return params;
                }
            };
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void APICallQueryIDGetDetails(String queryID) {
        //implement
    }
    void APICallFormula(String chemical) {
        //implement
    }
}
