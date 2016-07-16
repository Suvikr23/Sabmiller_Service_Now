package com.example.suvendu.sabmillerservicenow;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener, getAsyncResponse {

    public EditText et1, et2, et3;
    private TextView text;
    private Button btn;
    private ProgressBar pgb;
    static String url,result;
    ImageView imageView;
    Context context = getBaseContext();
    getJsonData get = new getJsonData(text,context, "number");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        et1 = (EditText) findViewById(R.id.username);
        et2 = (EditText) findViewById(R.id.password);
        et3 = (EditText) findViewById(R.id.instance);
        pgb = (ProgressBar) findViewById(R.id.ProgressBar);
        imageView = (ImageView)findViewById(R.id.iv);
        text = (TextView) findViewById(R.id.txt);
        btn = (Button) findViewById(R.id.btn);
        et1.setTextColor(Color.DKGRAY);
        et2.setTextColor(Color.DKGRAY);
        et3.setTextColor(Color.DKGRAY);
        et1.setTypeface(Typeface.MONOSPACE);
        et2.setTypeface(Typeface.MONOSPACE);
        et3.setTypeface(Typeface.MONOSPACE);
        et2.setTransformationMethod(new PasswordTransformationMethod());
        pgb.setBackgroundResource(R.drawable.progress);
        pgb.setBackgroundColor(Color.TRANSPARENT);
        pgb.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        text.setMovementMethod(new ScrollingMovementMethod());
        text.setVisibility(View.GONE);
        //text.setMovementMethod(new ScrollingMovementMethod());
        get.delegate = this;//to set delegate back to this class
        /*pgb.setMinimumWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        pgb.setMinimumHeight(ViewGroup.LayoutParams.WRAP_CONTENT);*/

        url = ".service-now.com/api/now/v1/table/incident?sla_made=" + "false";
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        String username = et1.getText().toString();
        String password = et2.getText().toString();
        String instance = et3.getText().toString();

        if (v == btn) {
            pgb.setVisibility(View.VISIBLE);
            get.execute(url, username, password, instance);
        }

    }

    @Override
    public void getResponse(String name) {

                if (name.equals("401")) {
            Toast.makeText(getApplicationContext(),"Error : 401",Toast.LENGTH_LONG).show();
            et1.setError("Invalid username");
            et2.setError("Invalid password");
            et3.setError("Unauthorised Instance");

        } else {
            et1.setVisibility(View.GONE);
            et2.setVisibility(View.GONE);
            et3.setVisibility(View.GONE);
            btn.setVisibility(View.GONE);
            text.setVisibility(View.VISIBLE);
            text.setText(name);
            imageView.setVisibility(View.VISIBLE);
            //text.setText(result);
        }
        //text.setText(getJsonData.stringBuilder);
        pgb.setVisibility(View.GONE);

    }

    @Override
    public void getResponse(String number, String link) {


        if (!link.equals(null)) {
            String username = et1.getText().toString();
            String password = et2.getText().toString();
            String instance = et3.getText().toString();
            //text.setText(number);
            getJsonData getName = new getJsonData(text,context, "name");
            getName.execute(link, username, password, instance);

        } else if (link.equals(null)) {
            text.setText(number + " " + "not assigned");
        }
    }
}
