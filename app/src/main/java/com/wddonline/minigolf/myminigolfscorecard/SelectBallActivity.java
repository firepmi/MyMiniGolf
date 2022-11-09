package com.wddonline.minigolf.myminigolfscorecard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class SelectBallActivity extends AppCompatActivity {

    int player, hole;
    int ballView[] = {
            R.id.blue_ball,
            R.id.pink_ball,
            R.id.red_ball,
            R.id.orange_ball,
            R.id.peach_ball,
            R.id.yellow_ball,
            R.id.green_ball,
            R.id.lime_ball,
            R.id.teal_ball,
            R.id.skyblue_ball,
            R.id.violet_ball,
            R.id.indigo_ball,
            R.id.purple_ball,
            R.id.magenta_ball,
            R.id.white_ball,
            R.id.black_ball
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_ball);

        Intent intent = getIntent();
        player = intent.getIntExtra("player",0);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.selectedScore = -1;
                finish();
            }
        });

    }

    public void onClick(View view) {
        for( int i = 0; i < 16; i++){
            View ball = findViewById(ballView[i]);
            if( ball == view) {
                PlayboardActivity.instanceOfPlayboard.onSelectedBall(i, player);
                break;
            }
        }
        finish();
    }
}
