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

public class LocationCourseActivity extends AppCompatActivity {

    ArrayList<String> parArray;
    ArrayList<String> titleArray;
    ArrayList<CourseData> courseDataArrayList;
    ListView listView;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_course);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(LocationInfoActivity.this, FindLocationActivity.class));
                finish();
            }
        });

        if( !Config.locationData.LOGO.equals("") ) {
            new DownloadImageTask((ImageView) findViewById(R.id.header_title))
                    .execute(Config.root_link+Config.locationData.LOGO);
        }

        courseDataArrayList = new ArrayList<>();
        parArray = new ArrayList<>();
        titleArray = new ArrayList<>();
        try {
            JSONArray curseArray = new JSONArray(Config.locationData.COURSES);
            for( int i = 0; i < curseArray.length(); i++) {
                JSONObject object = (JSONObject) curseArray.get(i);
                CourseData cd = new CourseData(
                        object.getString("NAME"),
                        object.getString("HALFCOURSE"),
                        object.getString("COURSEID"),
                        object.getJSONArray("HOLES"),0
                );
                int total = 0;
                for( int j = 0; j < cd.HOLES.length(); j++){
                    JSONObject jsonObject = (JSONObject) cd.HOLES.get(j);
                    total += jsonObject.getInt("PAR");
                }
                cd.total = total;
                parArray.add("Par"+total);
                courseDataArrayList.add(cd);
                titleArray.add(object.getString("NAME"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MenuAdapter listAdapter = new MenuAdapter(this, titleArray, parArray);
        listView = findViewById(R.id.listView);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Config.courseData = courseDataArrayList.get(i);
                startActivity(new Intent(LocationCourseActivity.this, PlayboardActivity.class));
            }
        });

        if(titleArray.size() == 0 ) {
            Config.courseData = null;
            startActivity(new Intent(LocationCourseActivity.this, PlayboardActivity.class));
            finish();
        }
    }

}
