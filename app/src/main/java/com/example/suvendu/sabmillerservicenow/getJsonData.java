package com.example.suvendu.sabmillerservicenow;

import android.content.Context;
import android.os.AsyncTask;
import android.os.RecoverySystem;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Suvendu on 7/10/2016.
 */
public class getJsonData extends AsyncTask<String, String, String>{

    private String edit1, edit2, edit3, field, url, jsnarray = "result";
    static StringBuilder stringBuilder = new StringBuilder();
    ProgressBar progress;
    TextView textView;
    public getAsyncResponse delegate;
    Context con;
    Thread thread = Thread.currentThread();
    //CountDownLatch latch = new CountDownLatch(1);


    public getJsonData(TextView text, Context context, String... strings) {

        this.field = strings[0];
        textView = text;
        con = context;
    }

    @Override
    protected String doInBackground(String... params) {
        BasicHttpParams param = new BasicHttpParams();
        HttpProtocolParams.setContentCharset(param, "UTF-8");
        Credentials credentials = new UsernamePasswordCredentials(params[1], params[2]);
        DefaultHttpClient client = new DefaultHttpClient(param);
        client.getCredentialsProvider().setCredentials(AuthScope.ANY, credentials);
        StringBuilder stringBuilder = new StringBuilder();
        //check if the request is for incident number or name
        HttpGet get;
        if (params[0].contains("https")) {
            get = new HttpGet(params[0]);
        } else {
            get = new HttpGet("https://" + params[3] + params[0]);
        }

        get.setHeader("Accept", "application/json");
        get.setHeader("Content-Type", "application/json");

        try {
            HttpResponse response = client.execute(get);
            StatusLine line = response.getStatusLine();
            int sc = line.getStatusCode();
            if (sc != 200) return sc + "";
            if (sc == 200) {
                HttpEntity content = response.getEntity();
                InputStream is = content.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String readline;
                while ((readline = br.readLine()) != null) {
                    stringBuilder.append(readline);
                }
            }
        } catch (IOException e) {
            Log.e("Error IO Exception", e.getMessage());
        }
        return stringBuilder.toString();
    }

    @Override
    protected void onPostExecute(String s) {

        MainActivity main = (MainActivity) con;

        //progress.setVisibility(View.GONE);
        if (s.equals("401")) {
            stringBuilder.append(s);
            delegate.getResponse(stringBuilder.toString());
        }

        if (!s.equals(null) && !s.equals("401")) {


            try {
                JSONObject jsn = new JSONObject(s);

                if (field.equals("number")) {


                    JSONArray result = jsn.getJSONArray("result");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject obj = result.getJSONObject(i);
                        String number = obj.getString(field);
                        String opened = obj.getString("opened_at");
                        String pri = obj.getString("priority");
                        String due = obj.getString("activity_due");
                        JSONObject assigned = new JSONObject(obj.getString("assigned_to"));
                        String link = assigned.getString("link");
                        stringBuilder.append(" " + (i + 1) + ") " + number +
                                " is out of SLA time as opened at: " + opened + "" +
                                " is of Priority " + pri + " and activity due on " + due + "\n\n");
                        delegate.getResponse(number, link);
                        delegate.getResponse(stringBuilder.toString());
                        //latch.await();

                    }
                } else if (field.equals("name")) {
                    JSONObject nameJsn = new JSONObject(jsn.getString("result"));
                    String name = nameJsn.getString(field);
                    Log.e("name", name);
                    stringBuilder.append(" is assigned respectively to " + name + "\n");
                    //latch.countDown();
                    //main.result = stringBuilder.toString();
                    //delegate.getResponse(stringBuilder.toString());
                }

            } catch (JSONException e) {
                e.getStackTrace();
            }            }

        }

    @Override
    protected void onPreExecute() {
        //progress.setVisibility(View.VISIBLE);
    }

    public static String getResult() {

        return stringBuilder.toString();

    }
}
