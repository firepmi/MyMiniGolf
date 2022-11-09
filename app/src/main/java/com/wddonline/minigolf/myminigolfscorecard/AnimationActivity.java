package com.wddonline.minigolf.myminigolfscorecard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class AnimationActivity extends AppCompatActivity {

    String name;
    int frame = 0;
    String type;
    ImageView imageView;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            frame++;
            if(frame % 2 == 0){
                if(type.equals("holeinone")) {
                    imageView.setImageResource(R.drawable.holeinone_1);
                }
                else {
                    imageView.setImageResource(R.drawable.win_1);
                }
            }
            else {
                if(type.equals("holeinone")) {
                    imageView.setImageResource(R.drawable.holeinone_2);
                }
                else {
                    imageView.setImageResource(R.drawable.win_2);
                }
            }
            if(frame > 10) {
                timerHandler.removeCallbacks(timerRunnable);
                if(type.equals("gameover")) {
                    PlayboardActivity.instanceOfPlayboard.showAlertGameOver();
                }
                finish();
            }
            else {
                timerHandler.postDelayed(this, 500);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        Intent intent = getIntent();
        name = intent.getStringExtra("player");
        type = intent.getStringExtra("type");

        TextView textView = findViewById(R.id.text);
        if(type.equals("holeinone")) {
            textView.setText(name + " is on a roll with\n that amazing\n hole-in-one");
            textView.setTextColor(getResources().getColor(R.color.white));
        }
        else {
            textView.setText("Congratulations, " + name + "\nis #1!");
            textView.setTextColor(getResources().getColor(R.color.black));
        }

        textView.setTypeface(Config.mainTypeFace);
        imageView = findViewById(R.id.image_anim);
        imageView.setImageResource(R.drawable.holeinone_1);
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.selectedScore = -1;
                if(type.equals("gameover")) {
                    PlayboardActivity.instanceOfPlayboard.showAlertGameOver();
                }
                finish();
            }
        });
        timerHandler.postDelayed(timerRunnable, 0);

    }
}
