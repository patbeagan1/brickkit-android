package com.wayfair.brickkitdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.reflect.TypeToken;
import com.wayfair.brickkitdemo.models.requests.StatusesUserTimeline;
import com.wayfair.brickkitdemo.models.requests.TokenRequest;
import com.wayfair.brickkitdemo.models.responses.Status;
import com.wayfair.brickkitdemo.models.responses.TokenResponse;
import com.wayfair.golleycore.core.config.GolleyConfig;
import com.wayfair.golleycore.core.request.Golley;
import com.wayfair.golleycore.core.request.GolleyBlock;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Main activity for the app.
 */
public class MainActivity extends AppCompatActivity {
    boolean fetchedToken = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Logging
        GolleyConfig.getInstance().setLoggingEnabled(BuildConfig.DEBUG);

        // Data Mapping
        GolleyConfig.getInstance().setDefaultType(new TypeToken<ArrayList<Status>>() {
        }.getType());
        GolleyConfig.getInstance().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        //GolleyConfig.getInstance().setModelIdField(SCHEMA_ID_KEY);
        //GolleyConfig.getInstance().setCustomGsonAdapter(new GolleyGsonAdapter<Response>());
        GolleyConfig.getInstance().addModelLocation("com.wayfair.brickkit.models.responses");

        // Server Connection
        GolleyConfig.getInstance().setUserAgentName("BrickKitDemoApp");
        GolleyConfig.getInstance().setBuildNumber(BuildConfig.VERSION_CODE);
        GolleyConfig.getInstance().setDefaultMethod(Request.Method.GET);
        //GolleyConfig.getInstance().setTablet(isTablet());
        GolleyConfig.getInstance().setCacheTimeToLive(600);
        GolleyConfig.getInstance().addCookie("guest_id", "v1%3A147636680091090361");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (!fetchedToken) {
                    TokenRequest tokenRequest = new TokenRequest();
                    new Golley<TokenRequest, TokenResponse>(
                            getBaseContext(),
                            tokenRequest
                    )
                            .type(new TypeToken<TokenResponse>() {
                            }.getType())
                            .postSuccess(
                                    new GolleyBlock<TokenRequest, TokenResponse>() {
                                        @Override
                                        public void run(TokenRequest request, TokenResponse golleyBaseResponses) {
                                            if (golleyBaseResponses != null) {
                                                HashMap<String, String> headers = GolleyConfig.getInstance().getGlobalHeaders();
                                                headers.put("Authorization", "Bearer " + golleyBaseResponses.accessToken);
                                                GolleyConfig.getInstance().setGlobalHeaders(headers);
                                                fetchedToken = true;
                                                getStatuses(view);
                                            }
                                        }
                                    }
                            )
                            .build().enqueue();
                } else {
                    getStatuses(view);
                }
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content, new MainActivityFragment()).commit();
        }
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

    /**
     * Request status from Twitter and raise {@link Snackbar} if successful.
     *
     * @param view the view to attach the success {@link Snackbar} to
     */
    private void getStatuses(final View view) {
        StatusesUserTimeline statusesUserTimeline = new StatusesUserTimeline();
        statusesUserTimeline.screenName = "wayfair";

        new Golley<StatusesUserTimeline, ArrayList<Status>>(
                getBaseContext(),
                statusesUserTimeline
        )
                .postSuccess(
                        new GolleyBlock<StatusesUserTimeline, ArrayList<Status>>() {
                            @Override
                            public void run(StatusesUserTimeline request, ArrayList<Status> golleyBaseResponses) {
                                if (golleyBaseResponses != null && golleyBaseResponses.size() > 0) {
                                    Snackbar.make(view, golleyBaseResponses.get(0).text, Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }
                )
                .build().enqueue();
    }
}
