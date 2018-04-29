package edu.illinois.cs.cs125.chemHelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;


public class searchResult extends AppCompatActivity {

    public static RequestQueue requestQueue;
    private static final String TAG = "Search Result";
    public String api = "jgXxAMt3w8GXg3aZaAkFJHTN5xBYaVLD";
    public String recordID;
    public String queryID = null;

    public void setRecordID(String rid) {
        recordID = rid;
    }

    public void setQueryID(String qid) {
        queryID = qid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        requestQueue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        String chemical = null;

        final TextView name = (TextView) findViewById(R.id.name);
        final TextView smile = (TextView) findViewById(R.id.smile);
        final TextView molarMass = (TextView) findViewById(R.id.molarMass);
        final TextView formula = (TextView) findViewById(R.id.formula);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        chemical = intent.getStringExtra("content");
        Log.i(TAG, chemical);
        boolean checked = getIntent().getExtras().getBoolean("option");

        JSONObject jsonReq = new JSONObject();
        try {
            jsonReq.put("name", chemical);
            jsonReq.put("orderDirection", "");
        } catch (JSONException ignored) {
            Log.e(TAG, "you screwed up the json package");
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "https://api.rsc.org/compounds/v1/filter/name",
                jsonReq,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        try {
                            setQueryID(response.getString("queryId"));
                            Log.i(TAG, "Query ID: " + queryID);
                            Log.i(TAG, "success on query");
                            final JsonObjectRequest recordIDRequest = new JsonObjectRequest(
                                    "https://api.rsc.org/compounds/v1/filter/"
                                            + queryID + "/results?start=0&count=1",
                                    null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response1) {
                                            try {
                                                setRecordID(response1.getJSONArray("results").getString(0));
                                                Log.i(TAG, "Record ID: " + recordID);
                                                final JsonObjectRequest detailsRequest = new JsonObjectRequest(
                                                        "https://api.rsc.org/compounds/v1/records/" + recordID + "/details?fields=SMILES,Formula,CommonName,MolecularWeight",
                                                        null,
                                                        new Response.Listener<JSONObject>() {
                                                            @Override
                                                            public void onResponse(JSONObject response2) {
                                                                try {
                                                                    Log.i(TAG, "Details: " + response2.getString("commonName"));
                                                                    name.setText(response2.getString("commonName"));
                                                                    smile.setText("SMILES: " + response2.getString("smiles"));
                                                                    String rawFormula = response2.getString("formula");
                                                                    String regx = "_{}";
                                                                    char[] ca = regx.toCharArray();
                                                                    for (char c : ca) {
                                                                        rawFormula = rawFormula.replace(""+c, "");
                                                                    }
                                                                    formula.setText("Formula: " + rawFormula);
                                                                    molarMass.setText("Molar Mass: " + Double.toString(response2.getDouble("molecularWeight")));
                                                                } catch (JSONException e) {
                                                                    Log.e(TAG, "Error on detailsRequest.onResponse.");

                                                                }
                                                            }
                                                        },
                                                        new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                Log.e(TAG, "Error on detailsRequest.");
                                                            }
                                                        }
                                                ) {
                                                    @Override
                                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                                        Map<String, String> params = new HashMap<>();
                                                        params.put("apikey", api);
                                                        return params;
                                                    }
                                                };
                                                final JsonObjectRequest imageRequest = new JsonObjectRequest(
                                                        "https://api.rsc.org/compounds/v1/records/" + recordID +"/image",
                                                        null,
                                                        new Response.Listener<JSONObject>() {
                                                            @Override
                                                            public void onResponse(JSONObject response3) {
                                                                try {
                                                                    Log.i(TAG, "Image (Base64 coded): " + response3.getString("image"));
                                                                    byte[] decoded = Base64.decode(response3.getString("image"), Base64.DEFAULT);
                                                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
                                                                    imageView.setImageBitmap(decodedByte);
                                                                } catch (JSONException e) {
                                                                    Log.e(TAG, "Error on imageRequest.onResponse.");
                                                                }
                                                            }
                                                        },
                                                        new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                Log.e(TAG, "Error on detailsRequest.");
                                                            }
                                                        }
                                                ) {
                                                    @Override
                                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                                        Map<String, String> params = new HashMap<>();
                                                        params.put("apikey", api);
                                                        return params;
                                                    }
                                                };
                                                requestQueue.add(imageRequest);
                                                requestQueue.add(detailsRequest);
                                            } catch (JSONException e) {
                                                Log.e(TAG, "Error on APICallQueryIDGetRecordID.OnResponse.");
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e(TAG, error.toString());
                                            //implement
                                        }
                                    }
                            ) {
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("apikey", api);
                                    return params;
                                }
                            };

                            requestQueue.add(recordIDRequest);

                        } catch (JSONException ignored) {
                            Log.e(TAG, "you screwed up the onResponse:\n" + ignored.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("apikey", api);
                return params;
            }
        };

        JSONObject formulaJson = new JSONObject();
        try {
            formulaJson.put("formula", chemical);
            formulaJson.put("orderDirection", "");
        } catch (JSONException ignored) {
            Log.e(TAG, "you screwed up the json package");
        }
        JsonObjectRequest formulaRequest = new JsonObjectRequest(
                Request.Method.POST,
                "https://api.rsc.org/compounds/v1/filter/formula",
                formulaJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        try {
                            setQueryID(response.getString("queryId"));
                            Log.i(TAG, "Query ID: " + queryID);
                            Log.i(TAG, "success on query");
                            final JsonObjectRequest recordIDRequest = new JsonObjectRequest(
                                    "https://api.rsc.org/compounds/v1/filter/"
                                            + queryID + "/results?start=0&count=1",
                                    null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response1) {
                                            try {
                                                setRecordID(response1.getJSONArray("results").getString(0));
                                                Log.i(TAG, "Record ID: " + recordID);
                                                final JsonObjectRequest detailsRequest = new JsonObjectRequest(
                                                        "https://api.rsc.org/compounds/v1/records/" + recordID + "/details?fields=SMILES,Formula,CommonName,MolecularWeight",
                                                        null,
                                                        new Response.Listener<JSONObject>() {
                                                            @Override
                                                            public void onResponse(JSONObject response2) {
                                                                try {
                                                                    Log.i(TAG, "Details: " + response2.getString("commonName"));
                                                                    name.setText(response2.getString("commonName"));
                                                                    smile.setText("SMILES: " + response2.getString("smiles"));
                                                                    String rawFormula = response2.getString("formula");
                                                                    String regx = "_{}";
                                                                    char[] ca = regx.toCharArray();
                                                                    for (char c : ca) {
                                                                        rawFormula = rawFormula.replace(""+c, "");
                                                                    }
                                                                    formula.setText("Formula: " + rawFormula);
                                                                    molarMass.setText("Molar Mass: " + Double.toString(response2.getDouble("molecularWeight")));
                                                                } catch (JSONException e) {
                                                                    Log.e(TAG, "Error on detailsRequest.onResponse.");

                                                                }
                                                            }
                                                        },
                                                        new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                Log.e(TAG, "Error on detailsRequest.");
                                                            }
                                                        }
                                                ) {
                                                    @Override
                                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                                        Map<String, String> params = new HashMap<>();
                                                        params.put("apikey", api);
                                                        return params;
                                                    }
                                                };
                                                final JsonObjectRequest imageRequest = new JsonObjectRequest(
                                                        "https://api.rsc.org/compounds/v1/records/" + recordID +"/image",
                                                        null,
                                                        new Response.Listener<JSONObject>() {
                                                            @Override
                                                            public void onResponse(JSONObject response3) {
                                                                try {
                                                                    Log.i(TAG, "Image (Base64 coded): " + response3.getString("image"));
                                                                    byte[] decoded = Base64.decode(response3.getString("image"), Base64.DEFAULT);
                                                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
                                                                    imageView.setImageBitmap(decodedByte);
                                                                } catch (JSONException e) {
                                                                    Log.e(TAG, "Error on imageRequest.onResponse.");
                                                                }
                                                            }
                                                        },
                                                        new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                Log.e(TAG, "Error on detailsRequest.");
                                                            }
                                                        }
                                                ) {
                                                    @Override
                                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                                        Map<String, String> params = new HashMap<>();
                                                        params.put("apikey", api);
                                                        return params;
                                                    }
                                                };
                                                requestQueue.add(imageRequest);
                                                requestQueue.add(detailsRequest);
                                            } catch (JSONException e) {
                                                Log.e(TAG, "Error on APICallQueryIDGetRecordID.OnResponse.");
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e(TAG, error.toString());
                                            //implement
                                        }
                                    }
                            ) {
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("apikey", api);
                                    return params;
                                }
                            };

                            requestQueue.add(recordIDRequest);

                        } catch (JSONException ignored) {
                            Log.e(TAG, "you screwed up the onResponse:\n" + ignored.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("apikey", api);
                return params;
            }
        };
        if (checked) {
            requestQueue.add(formulaRequest);
        } else {
            requestQueue.add(request);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent1 = new Intent(this, about.class);
                this.startActivity(intent1);
                return true;
            case R.id.about:
                Intent intent2 = new Intent(this, about.class);
                this.startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /*
        public void APICallQueryIDGetRecordID(String queryID) {
            JSONObject jsonReq = new JSONObject();
            try {
                jsonReq.put("name", chemical);
                jsonReq.put("orderDirection", "");
            } catch (JSONException ignored) {
                Log.e(TAG, "you screwed up the json package");
            }
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    "https://api.rsc.org/compounds/v1/filter/name",
                    jsonReq,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(final JSONObject response) {
                            try {

                                queryID = response.getString("queryId");
                                Log.i(TAG, queryID);
                                Log.i(TAG, "success on query");
                            } catch (JSONException ignored) {
                                Log.e(TAG, "you screwed up the onResponse:\n" + ignored.toString());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, error.toString());
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("apikey", api);
                    return params;
                }
            };
            requestQueue.add(request);
        }

    public void APICallQueryIDGetRecordID(String queryID) {
        try {
            JsonObjectRequest recordIDRequest = new JsonObjectRequest(
                    "https://api.rsc.org/compounds/v1/filter/"
                            + queryID + "/results?start=0&count=1",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                setRecordID(response.getJSONArray("results").getString(0));
                                Log.i(TAG, recordID);
                            } catch (JSONException e) {
                                Log.e(TAG, "Error on APICallQueryIDGetRecordID.OnResponse.");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, error.toString());
                            //implement
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("apikey", api);
                    return params;
                }
            };
            requestQueue.add(recordIDRequest);
        } catch (Exception e) {
            e.printStackTrace();
            //TODO : implement
        }
    }

    public void APICallRecordIDGetDetails(String recordID) {
        JsonObjectRequest detailsRequest = new JsonObjectRequest(
                "https://api.rsc.org/compounds/v1/records/" + recordID + "/details?fields=SMILES,Formula,CommonName,MolecularWeight",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {


                        }
                    }
                }
        )
    }

    void APICallFormula(String chemical) {
        //implement
    }
    */
}
