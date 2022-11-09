package com.wddonline.minigolf.myminigolfscorecard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LocationInfoActivity extends AppCompatActivity {

    ArrayList<String> contentArray;
    ArrayList<String> titleArray;
    ListView listView;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_info);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(LocationInfoActivity.this, FindLocationActivity.class));
                finish();
            }
        });

        ImageButton btnPlay = findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.parseData = Config.locationData;
                startActivity(new Intent(LocationInfoActivity.this, LocationCourseActivity.class));
            }
        });

        if( !Config.locationData.LOGO.equals("") ) {
            new DownloadImageTask((ImageView) findViewById(R.id.header_title))
                    .execute(Config.root_link+Config.locationData.LOGO);
        }

        webView = findViewById(R.id.webView);
        webView.loadData(Config.locationData.INFOWINDOW,"text/html; charset=UTF-8",null);

        contentArray = new ArrayList<>();
        titleArray = new ArrayList<>();
        contentArray.add(Config.locationData.INFOWINDOW);
        titleArray.add("Main Information");
        listView = findViewById(R.id.listView);
        if(Config.locationData.PAGES!=null){
            try {
                JSONArray pageArray = new JSONArray(Config.locationData.PAGES);
                for( int i = 0; i < pageArray.length(); i++) {
                    JSONObject object = (JSONObject) pageArray.get(i);
                    titleArray.add(object.getString("NAME"));
                    contentArray.add(object.getString("CONTENT"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        MenuAdapter listAdapter = new MenuAdapter(this, titleArray);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                webView.loadData(contentArray.get(i),"text/html; charset=UTF-8",null);
            }
        });

        if(titleArray.size()<=1) {
            listView.setVisibility(View.GONE);
        }

    }

}
