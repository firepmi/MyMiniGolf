package com.wddonline.minigolf.myminigolfscorecard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class SelectScoreActivity extends AppCompatActivity {

    int player, hole;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_score);

        Intent intent = getIntent();
        player = intent.getIntExtra("player",0);
        hole = intent.getIntExtra("hole",0);

        TextView txtPlayer = findViewById(R.id.txt_player);
        txtPlayer.setText("Player "+player);
        txtPlayer.setTypeface(Config.mainTypeFace);

        TextView txtHole = findViewById(R.id.txt_hole);
        txtHole.setText("Hole "+hole);
        txtHole.setTypeface(Config.mainTypeFace);

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
        switch (view.getId()){
            case R.id.btn_1:
                PlayboardActivity.instanceOfPlayboard.onSelectedScore(1,player,hole);
//                startActivity(new Intent(SelectScoreActivity.this, AnimationActivity.class));
                break;
            case R.id.btn_2:
                PlayboardActivity.instanceOfPlayboard.onSelectedScore(2,player,hole);
                break;
            case R.id.btn_3:
                PlayboardActivity.instanceOfPlayboard.onSelectedScore(3,player,hole);
                break;
            case R.id.btn_4:
                PlayboardActivity.instanceOfPlayboard.onSelectedScore(4,player,hole);
                break;
            case R.id.btn_5:
                PlayboardActivity.instanceOfPlayboard.onSelectedScore(5,player,hole);
                break;
            case R.id.btn_6:
                PlayboardActivity.instanceOfPlayboard.onSelectedScore(6,player,hole);
                break;
            case R.id.btn_x:
                PlayboardActivity.instanceOfPlayboard.onSelectedScore(0,player,hole);
                break;
        }
        finish();
    }
}
