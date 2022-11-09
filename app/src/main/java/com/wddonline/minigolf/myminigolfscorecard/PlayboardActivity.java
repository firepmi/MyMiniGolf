package com.wddonline.minigolf.myminigolfscorecard;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PlayboardActivity extends AppCompatActivity implements SelectScoreInterface {

    int table[][] = new int[18][6];
    int totalArray[] = new int[6];
    int tableView[][] = {
            {R.id.btn_1_1,R.id.btn_1_2,R.id.btn_1_3,R.id.btn_1_4,R.id.btn_1_5,R.id.btn_1_6},
            {R.id.btn_2_1,R.id.btn_2_2,R.id.btn_2_3,R.id.btn_2_4,R.id.btn_2_5,R.id.btn_2_6},
            {R.id.btn_3_1,R.id.btn_3_2,R.id.btn_3_3,R.id.btn_3_4,R.id.btn_3_5,R.id.btn_3_6},
            {R.id.btn_4_1,R.id.btn_4_2,R.id.btn_4_3,R.id.btn_4_4,R.id.btn_4_5,R.id.btn_4_6},
            {R.id.btn_5_1,R.id.btn_5_2,R.id.btn_5_3,R.id.btn_5_4,R.id.btn_5_5,R.id.btn_5_6},
            {R.id.btn_6_1,R.id.btn_6_2,R.id.btn_6_3,R.id.btn_6_4,R.id.btn_6_5,R.id.btn_6_6},
            {R.id.btn_7_1,R.id.btn_7_2,R.id.btn_7_3,R.id.btn_7_4,R.id.btn_7_5,R.id.btn_7_6},
            {R.id.btn_8_1,R.id.btn_8_2,R.id.btn_8_3,R.id.btn_8_4,R.id.btn_8_5,R.id.btn_8_6},
            {R.id.btn_9_1,R.id.btn_9_2,R.id.btn_9_3,R.id.btn_9_4,R.id.btn_9_5,R.id.btn_9_6},
            {R.id.btn_10_1,R.id.btn_10_2,R.id.btn_10_3,R.id.btn_10_4,R.id.btn_10_5,R.id.btn_10_6},
            {R.id.btn_11_1,R.id.btn_11_2,R.id.btn_11_3,R.id.btn_11_4,R.id.btn_11_5,R.id.btn_11_6},
            {R.id.btn_12_1,R.id.btn_12_2,R.id.btn_12_3,R.id.btn_12_4,R.id.btn_12_5,R.id.btn_12_6},
            {R.id.btn_13_1,R.id.btn_13_2,R.id.btn_13_3,R.id.btn_13_4,R.id.btn_13_5,R.id.btn_13_6},
            {R.id.btn_14_1,R.id.btn_14_2,R.id.btn_14_3,R.id.btn_14_4,R.id.btn_14_5,R.id.btn_14_6},
            {R.id.btn_15_1,R.id.btn_15_2,R.id.btn_15_3,R.id.btn_15_4,R.id.btn_15_5,R.id.btn_15_6},
            {R.id.btn_16_1,R.id.btn_16_2,R.id.btn_16_3,R.id.btn_16_4,R.id.btn_16_5,R.id.btn_16_6},
            {R.id.btn_17_1,R.id.btn_17_2,R.id.btn_17_3,R.id.btn_17_4,R.id.btn_17_5,R.id.btn_17_6},
            {R.id.btn_18_1,R.id.btn_18_2,R.id.btn_18_3,R.id.btn_18_4,R.id.btn_18_5,R.id.btn_18_6}
    };
    int ballResource[] = {
        R.drawable.blue_ball,
        R.drawable.pink_ball,
        R.drawable.red_ball,
        R.drawable.orange_ball,
        R.drawable.peach_ball,
        R.drawable.yellow_ball,
        R.drawable.green_ball,
        R.drawable.lime_ball,
        R.drawable.teal_ball,
        R.drawable.skyblue_ball,
        R.drawable.violet_ball,
        R.drawable.indigo_ball,
        R.drawable.purple_ball,
        R.drawable.magenta_ball,
        R.drawable.white_ball,
        R.drawable.black_ball
    };
    int ballIndexArray[] = {
            14,14,14,14,14,14
    };

    public static PlayboardActivity instanceOfPlayboard;
    Button selectedCell;
    int totalViewArray[] = {R.id.txt_total_score_1,R.id.txt_total_score_2,R.id.txt_total_score_3
            ,R.id.txt_total_score_4
            ,R.id.txt_total_score_5
            ,R.id.txt_total_score_6};
    int playerViewArray[] = {R.id.txt_player1,R.id.txt_player2,R.id.txt_player3,R.id.txt_player4,R.id.txt_player5,R.id.txt_player6};
    int ballViewArray[] = {R.id.ball_1,R.id.ball_2,R.id.ball_3,R.id.ball_4,R.id.ball_5,R.id.ball_6};
    int courseViewArray[] = {
            R.id.course_1,R.id.course_2,R.id.course_3,R.id.course_4,R.id.course_5,R.id.course_6,R.id.course_7,R.id.course_8,R.id.course_9,R.id.course_10,
            R.id.course_11,R.id.course_12,R.id.course_13,R.id.course_14,R.id.course_15,R.id.course_16,R.id.course_17,R.id.course_18,
    };
    String courseName = "";
    int courseArray[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

    String name = "";

    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playboard);

        callbackManager = CallbackManager.Factory.create();

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(LocationInfoActivity.this, FindLocationActivity.class));
                finish();
            }
        });
        ImageButton btnSetting = findViewById(R.id.btn_setting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence options[] = new CharSequence[] {"Share Scores to Facebook", "Save Scorecard to Photos", /*"Take a Photo",*/ "Version Info", "New Game"};
                AlertDialog.Builder builder = new AlertDialog.Builder(PlayboardActivity.this);
                builder.setCancelable(true);
//                builder.setTitle("");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on options[which]
                        switch (which){
                            case 0:
                                //Facebook share
                                onFBShare();
                                break;
                            case 1:
                                //Save Scoreboard to Photos
                                onSaveImage();
                                break;
                            /*case 2:
                                //Take a Photo
                                break;*/
                            case 2:
                                //Version info
                                startActivity(new Intent(PlayboardActivity.this, VersionInfoActivity.class));
                                break;
                            case 3:
                                //New Game
                                onNewGame();
                                break;
                            case 4:
                                //Cancel
                                break;
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //the user clicked on Cancel
                    }
                });
                builder.show();

            }
        });

        TextView txtDescription = findViewById(R.id.txt_description);
        txtDescription.setTypeface(Config.mainTypeFace);

        if(Config.parseData != null)
            name = Config.parseData.NAME;
        else name = "";

        txtDescription.setText(name);

        if(Config.courseData != null ){
            TextView tv = findViewById(R.id.txt_course);
            tv.setText(Config.courseData.NAME);
            tv.setTypeface(Config.mainTypeFace);
            TextView totalCourse = findViewById(R.id.course_total);
            totalCourse.setText(""+Config.courseData.total);
            totalCourse.setTypeface(Config.ziptyTypeFace);
        }
        else {
            TextView tv = findViewById(R.id.txt_course);
            tv.setText("");
            tv.setTypeface(Config.mainTypeFace);
            TextView totalCourse = findViewById(R.id.course_total);
            totalCourse.setText("");
            totalCourse.setTypeface(Config.ziptyTypeFace);
        }

        for ( int i = 0; i < 18; i ++) {
            for ( int j = 0; j < 6;j++) {
                table[i][j] = 0;
            }
            if(Config.courseData != null ){
                TextView v = findViewById(courseViewArray[i]);
                v.setTypeface(Config.ziptyTypeFace);
                try {
                    JSONObject jsonObject = (JSONObject) Config.courseData.HOLES.get(i);
                    v.setText(jsonObject.getInt("PAR")+"");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                TextView v = findViewById(courseViewArray[i]);
                v.setTypeface(Config.ziptyTypeFace);
                v.setText("");
            }
        }
        for ( int i = 0; i < 6; i++ ) {
            totalArray[i] = 0;
            EditText p = findViewById(playerViewArray[i]);
            p.setTypeface(Config.mainTypeFace);
        }

        instanceOfPlayboard = this;

    }
    void onNewGame(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("New Game");
        builder.setMessage("Are you sure you wish to start a new game? This will clear the current scores and cannot be undone.");
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //the user clicked on Cancel
            }
        });
        builder.setNegativeButton("Clear Everything", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //the user clicked on Cancel
                for ( int i = 0; i < 18; i ++) {
                    for ( int j = 0; j < 6;j++) {
                        Button b = findViewById(tableView[i][j]);
                        b.setText("");
                        table[i][j] = 0;
                    }
                }
                for ( int i = 0; i < 6; i++ ) {
                    totalArray[i] = 0;
                    TextView v = findViewById(totalViewArray[i]);
                    v.setText("");
                    EditText p = findViewById(playerViewArray[i]);
                    p.setText("");
                    ImageButton b = findViewById(ballViewArray[i]);
                    b.setImageResource(R.drawable.white_ball);
                }
            }
        });
        builder.setNeutralButton("Clear Scores", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearScores();

            }
        });
        builder.show();
    }
    void clearScores(){
        for ( int i = 0; i < 18; i ++) {
            for ( int j = 0; j < 6;j++) {
                Button b = findViewById(tableView[i][j]);
                b.setText("");
                table[i][j] = 0;
            }
        }
        for ( int i = 0; i < 6; i++ ) {
            totalArray[i] = 0;
            TextView v = findViewById(totalViewArray[i]);
            v.setText("");
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TAG","STOPPED");
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name",name);
        TextView tv = findViewById(R.id.txt_course);
        editor.putString("course",tv.getText().toString());
        for( int i = 0; i < 6; i ++ ) {
            EditText e = findViewById(playerViewArray[i]);
            editor.putString("player"+i,e.getText().toString());
            TextView t = findViewById(totalViewArray[i]);
            editor.putString("total"+i,t.getText().toString());
            editor.putInt("ball"+i,ballIndexArray[i]);
        }
        for ( int i = 0; i < 18; i ++) {
            for ( int j = 0; j < 6;j++) {
                Button b = findViewById(tableView[i][j]);
                editor.putString("table"+i+"_"+j,b.getText().toString());
            }
            TextView tcv = findViewById(courseViewArray[i]);
            editor.putString("course"+i,tcv.getText().toString());
        }
        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("REAsumed","resumed");

        final SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String oldName = sharedPreferences.getString("name","none");
        final String courseName = sharedPreferences.getString("course","");
        final TextView txtDescription = findViewById(R.id.txt_description);
        final TextView txtCourse = findViewById(R.id.txt_course);
        if(oldName.equals("none")) {
            return;
        }
        if( Config.parseData != null) {
            if(!Config.parseData.NAME.equals(oldName) || (Config.courseData!=null&& !Config.courseData.NAME.equals(courseName))) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setTitle("Warning");
                builder.setMessage("The course you chose does not match the saved scorecard. Do you wish to cancel or start a new game?");
                builder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //the user clicked on Cancel
                    }
                });
                builder.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtDescription.setText(name);
                        if(name.equals("")) txtCourse.setText("");
                        else txtCourse.setText(courseName);
                        for( int i = 0; i < 6; i ++ ) {
                            EditText e = findViewById(playerViewArray[i]);
                            e.setText(sharedPreferences.getString("player"+i,""));
                            TextView t = findViewById(totalViewArray[i]);
                            t.setText(sharedPreferences.getString("total"+i,""));
                            t.setTypeface(Config.ziptyTypeFace);
                            ImageButton imageView = findViewById(ballViewArray[i]);
                            int ballIndex = sharedPreferences.getInt("ball"+i,14);
                            if(ballIndex < 0 || ballIndex > 15) ballIndex = 14;
                            Log.d("TAG",ballIndex+"");
                            ballIndexArray[i] = ballIndex;
                            imageView.setImageResource(ballResource[ballIndex]);
                        }
                        for ( int i = 0; i < 18; i ++) {
                            for ( int j = 0; j < 6;j++) {
                                Button b = findViewById(tableView[i][j]);
                                String strValue = sharedPreferences.getString("table"+i+"_"+j,"");
                                b.setText(strValue);
                                if(strValue.equals("")) strValue = "0";
                                table[i][j] = Integer.valueOf(strValue);

                                b.setTypeface(Config.ziptyTypeFace);
                            }
                            TextView tv = findViewById(courseViewArray[i]);
                            tv.setText(sharedPreferences.getString("course"+i,""));
                        }

                        finish();
                    }
                });
                builder.show();
                return;
            }
            else if(!Config.parseData.NAME.equals(oldName)) {

            }
        }
        txtDescription.setText(name);
        if(name.equals("")) txtCourse.setText("");
        else txtCourse.setText(courseName);
        for( int i = 0; i < 6; i ++ ) {
            EditText e = findViewById(playerViewArray[i]);
            e.setText(sharedPreferences.getString("player"+i,""));
            TextView t = findViewById(totalViewArray[i]);
            t.setText(sharedPreferences.getString("total"+i,""));
            t.setTypeface(Config.ziptyTypeFace);
            ImageButton imageView = findViewById(ballViewArray[i]);
            int ballIndex = sharedPreferences.getInt("ball"+i,14);
            if(ballIndex < 0 || ballIndex > 15) ballIndex = 14;
            Log.d("TAG",ballIndex+"");
            ballIndexArray[i] = ballIndex;
            imageView.setImageResource(ballResource[ballIndex]);
        }
        for ( int i = 0; i < 18; i ++) {
            for ( int j = 0; j < 6;j++) {
                Button b = findViewById(tableView[i][j]);
                String strValue = sharedPreferences.getString("table"+i+"_"+j,"");
                b.setText(strValue);
                if(strValue.equals("")) strValue = "0";
                table[i][j] = Integer.valueOf(strValue);

                b.setTypeface(Config.ziptyTypeFace);
            }
        }
    }
    void onFBShare(){
        Log.d("TAG","FB Share");
        // do the login action
        LoginButton loginButton = findViewById(R.id.fb_login);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        accessToken = loginResult.getAccessToken();
                        onLoggedIn();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
        if(AccessToken.getCurrentAccessToken()!=null) {
            accessToken = AccessToken.getCurrentAccessToken();
            onLoggedIn();
        }
        else {
            loginButton.performClick();
        }
    }
    void onLoggedIn(){
        Log.d("TAG","Trying facebook share");
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(getScreenshot())
                .setCaption("We just used My Mini Golf Scorecard - Check out our scores!")
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
//        Photo photo = new Photo.Builder()
//                .setImage(getScreenshot())
//                .setName("We just used My Mini Golf Scorecard - Check out our scores!")
//                .build();
//        progress = new ProgressDialog(this);
//        progress.setMessage("Uploading scorecard to Facebook...");
//        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progress.setCancelable(false);
//        progress.show();
//        mSimpleFacebook.publish(photo, false, onPublishListener);
    }
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
    void onSaveImage(){
        takeScreenShot();
    }

    Bitmap getScreenshot(){
        View u1 = findViewById(R.id.rl_table_name);
        View u2 = findViewById(R.id.hs_table_score);
        View u3 = findViewById(R.id.rl_table_total);

        RelativeLayout rlName = findViewById(R.id.rl_table_name);
        int nameHeight = rlName.getHeight();
        int nameWidth = rlName.getWidth();
        HorizontalScrollView z = findViewById(R.id.hs_table_score);
        int scoreHeight = z.getChildAt(0).getHeight();//*/307;
        int scoreWidth = z.getChildAt(0).getWidth();//*/792;
        RelativeLayout rlTotal = findViewById(R.id.rl_table_total);
        int totalHeight = rlTotal.getHeight();
        int totalWidth = rlTotal.getWidth();

        Log.d("TAG", "width: "+scoreWidth+", "+"height: "+scoreHeight);

        Bitmap b1 = getBitmapFromView(u1,nameHeight,nameWidth);
        b1 = Bitmap.createScaledBitmap(b1,260,614, false);
        Bitmap b2 = getBitmapFromView(u2,scoreHeight,scoreWidth);
        b2 = Bitmap.createScaledBitmap(b2,1584,614, false);
        Bitmap b3 = getBitmapFromView(u3,totalHeight,totalWidth);
        b3 = Bitmap.createScaledBitmap(b3,102,614, false);
        Bitmap h1 = BitmapFactory.decodeResource(getResources(),R.drawable.mini_golf_header);
        h1 = Bitmap.createScaledBitmap(h1,655,163, false);
        Bitmap h2 = BitmapFactory.decodeResource(getResources(),R.drawable.bg_scorecard_export_header);
        h2 = Bitmap.createScaledBitmap(h2,1291,163, false);
        Bitmap bt = Bitmap.createBitmap(1946,777,b2.getConfig());

        Canvas comboImage = new Canvas(bt);
        comboImage.drawBitmap(h1,0f,0f,null);
        comboImage.drawBitmap(h2,655f,0f,null);
        comboImage.drawBitmap(b1,0f,163f,null);
        comboImage.drawBitmap(b2,260f,163f,null);
        comboImage.drawBitmap(b3,1844f,163f,null);

        return bt;
    }
    private void takeScreenShot()
    {
        Bitmap bt = getScreenshot();
        //Save bitmap
        String extr = Environment.getExternalStorageDirectory()+"/MyMiniGolf/";
        String fileName = "MyMiniGolf.jpg";
        final boolean mkdirs = new File(extr).mkdirs();
        File myPath = new File(extr, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            bt.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            MediaStore.Images.Media.insertImage(getContentResolver(), bt, "My Mini Golf", "My Mini Golf");
            Toast.makeText(this,"Image saved to your phone's photo gallery.",Toast.LENGTH_LONG).show();
        }catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {

        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth,totalHeight , Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    void tapTableCell(View v, int x, int y) {
        selectedCell = (Button)v;
        selectedCell.setTypeface(Config.ziptyTypeFace);
        Intent intent = new Intent(PlayboardActivity.this, SelectScoreActivity.class);
        intent.putExtra("player", y);
        intent.putExtra("hole",x);
        startActivity(intent);
    }
    @Override
    public void onSelectedScore(final int score, final int player, final int hole) {
        Handler timerHandler = new Handler();
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                if(score == 0) {
                    selectedCell.setText("");
                }
                else {
                    selectedCell.setText(""+score);
                }
                table[hole-1][player-1] = score;

                int total = 0;
                for( int i = 0; i < 18; i++){
                    total += table[i][player-1];
                }
                totalArray[player-1] = total;
                TextView tv = findViewById(totalViewArray[player-1]);
                tv.setTypeface(Config.ziptyTypeFace);
                if( total == 0) {
                    tv.setText("");
                }
                else {
                    tv.setText(""+total);
                }
                int winner = checkGameOver();
                if(winner!=-1){
                    onGameOver(winner);
                }
                if( score == 1 ) {
                    Log.d("TAG", "Score 1 animation!!!");
                    Intent intent = new Intent(PlayboardActivity.this, AnimationActivity.class);
                    EditText t = findViewById(playerViewArray[player - 1]);
                    String name = t.getText().toString();
                    if(name.equals("")){
                        name = "Player " + player;
                    }
                    intent.putExtra("player", name);
                    intent.putExtra("type", "holeinone");
                    startActivity(intent);
                }


            }
        };
        timerHandler.postDelayed(timerRunnable, 200);
        Log.d("TAG","score is " + score + " player: " +player+", hole: "+hole);
    }
    void onGameOver(int winner){
        Intent intent = new Intent(PlayboardActivity.this, AnimationActivity.class);
        EditText t = findViewById(playerViewArray[winner]);
        String name = t.getText().toString();
        if(name.equals("")){
            name = "Player " + winner+1;
        }
        intent.putExtra("player", name);
        intent.putExtra("type", "gameover");
        startActivity(intent);
    }
    public void showAlertGameOver(){
        Handler timerHandler = new Handler();
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                CharSequence options[] = new CharSequence[] {"Post on facebook","Clear scores"};
                AlertDialog.Builder builder = new AlertDialog.Builder(PlayboardActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Good Game!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on options[which]
                        switch (which){
                            case 0:
                                //Facebook share
                                onFBShare();
                                break;
                            case 1:
                                //Save Scoreboard to Photos
                                clearScores();
                                break;
                        }

                    }
                });
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //the user clicked on Cancel
                    }
                });
                builder.show();
            }
        };
        timerHandler.postDelayed(timerRunnable, 200);
    }

    public int checkGameOver(){
        int c = 0;
        int win = 0;
        int win_score = 300;
        for( int i = 0; i < 6; i++) {
            int t = 0;
            int b = 0;
            int w = 1;
            for( int j = 0; j < 18; j++) {
                if(table[j][i]!=0) {
                    t += table[j][i];
                    b = 1;
                }
                else {
                    w = 0;
                }
            }
            if( t == 0 && b == 0) {
                c++;
            }
            if( t != 0 && w == 0 ) {
                return -1;
            }
            if( t != 0 ) {
                if( win_score > t ){
                    win_score = t;
                    win = i;
                }
            }
        }
        if( c == 6) return -1;
        return win;
    }
    public void onSelectedBall(final int ballIndex, final int player){
        Handler timerHandler = new Handler();
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                ballIndexArray[player] = ballIndex;
                ImageButton imageView = findViewById(ballViewArray[player]);
                imageView.setImageResource(ballResource[ballIndex]);
            }
        };
        timerHandler.postDelayed(timerRunnable, 100);
    }
    public void btnBallClick(View view) {
        Log.d("TAG","Ball clicked");
        for( int i = 0; i < 6; i++) {
            ImageButton imageButton = findViewById(ballViewArray[i]);
            if( imageButton == view ) {
                Intent intent = new Intent(PlayboardActivity.this, SelectBallActivity.class);
                intent.putExtra("player", i);
                startActivity(intent);
                break;
            }
        }
    }
    public void btnClick(View view) {
        switch (view.getId()){
            case R.id.btn_1_1:
                Log.d("Button Clicked","1-1");
                tapTableCell(view, 1, 1);
                break;
            case R.id.btn_1_2:
                Log.d("Button Clicked","1-2");
                tapTableCell(view, 1, 2);
                break;
            case R.id.btn_1_3:
                Log.d("Button Clicked","1-3");
                tapTableCell(view, 1, 3);
                break;
            case R.id.btn_1_4:
                Log.d("Button Clicked","1-4");
                tapTableCell(view, 1, 4);
                break;
            case R.id.btn_1_5:
                Log.d("Button Clicked","1-5");
                tapTableCell(view, 1, 5);
                break;
            case R.id.btn_1_6:
                Log.d("Button Clicked","1-6");
                tapTableCell(view, 1, 6);
                break;
            case R.id.btn_2_1:
                Log.d("Button Clicked","1-1");
                tapTableCell(view, 2, 1);
                break;
            case R.id.btn_2_2:
                Log.d("Button Clicked","1-2");
                tapTableCell(view, 2, 2);
                break;
            case R.id.btn_2_3:
                Log.d("Button Clicked","1-3");
                tapTableCell(view, 2, 3);
                break;
            case R.id.btn_2_4:
                Log.d("Button Clicked","1-4");
                tapTableCell(view, 2, 4);
                break;
            case R.id.btn_2_5:
                Log.d("Button Clicked","1-5");
                tapTableCell(view, 2, 5);
                break;
            case R.id.btn_2_6:
                Log.d("Button Clicked","1-6");
                tapTableCell(view, 2, 6);
                break;
            case R.id.btn_3_1:
                Log.d("Button Clicked","1-1");
                tapTableCell(view, 3, 1);
                break;
            case R.id.btn_3_2:
                Log.d("Button Clicked","1-2");
                tapTableCell(view, 3, 2);
                break;
            case R.id.btn_3_3:
                Log.d("Button Clicked","1-3");
                tapTableCell(view, 3, 3);
                break;
            case R.id.btn_3_4:
                Log.d("Button Clicked","1-4");
                tapTableCell(view, 3, 4);
                break;
            case R.id.btn_3_5:
                Log.d("Button Clicked","1-5");
                tapTableCell(view, 3, 5);
                break;
            case R.id.btn_3_6:
                Log.d("Button Clicked","1-6");
                tapTableCell(view, 3, 6);
                break;
            case R.id.btn_4_1:
                Log.d("Button Clicked","1-1");
                tapTableCell(view, 4, 1);
                break;
            case R.id.btn_4_2:
                Log.d("Button Clicked","1-2");
                tapTableCell(view, 4, 2);
                break;
            case R.id.btn_4_3:
                Log.d("Button Clicked","1-3");
                tapTableCell(view, 4, 3);
                break;
            case R.id.btn_4_4:
                Log.d("Button Clicked","1-4");
                tapTableCell(view, 4, 4);
                break;
            case R.id.btn_4_5:
                Log.d("Button Clicked","1-5");
                tapTableCell(view, 4, 5);
                break;
            case R.id.btn_4_6:
                Log.d("Button Clicked","1-6");
                tapTableCell(view, 4, 6);
                break;
            case R.id.btn_5_1:
                Log.d("Button Clicked","1-1");
                tapTableCell(view, 5, 1);
                break;
            case R.id.btn_5_2:
                Log.d("Button Clicked","1-2");
                tapTableCell(view, 5, 2);
                break;
            case R.id.btn_5_3:
                Log.d("Button Clicked","1-3");
                tapTableCell(view, 5, 3);
                break;
            case R.id.btn_5_4:
                Log.d("Button Clicked","1-4");
                tapTableCell(view, 5, 4);
                break;
            case R.id.btn_5_5:
                Log.d("Button Clicked","1-5");
                tapTableCell(view, 5, 5);
                break;
            case R.id.btn_5_6:
                Log.d("Button Clicked","1-6");
                tapTableCell(view, 5, 6);
                break;
            case R.id.btn_6_1:
                Log.d("Button Clicked","1-1");
                tapTableCell(view, 6, 1);
                break;
            case R.id.btn_6_2:
                Log.d("Button Clicked","1-2");
                tapTableCell(view, 6, 2);
                break;
            case R.id.btn_6_3:
                Log.d("Button Clicked","1-3");
                tapTableCell(view, 6, 3);
                break;
            case R.id.btn_6_4:
                Log.d("Button Clicked","1-4");
                tapTableCell(view, 6, 4);
                break;
            case R.id.btn_6_5:
                Log.d("Button Clicked","1-5");
                tapTableCell(view, 6, 5);
                break;
            case R.id.btn_6_6:
                Log.d("Button Clicked","1-6");
                tapTableCell(view, 6, 6);
                break;
            case R.id.btn_7_1:
                Log.d("Button Clicked","1-1");
                tapTableCell(view, 7, 1);
                break;
            case R.id.btn_7_2:
                Log.d("Button Clicked","1-2");
                tapTableCell(view, 7, 2);
                break;
            case R.id.btn_7_3:
                Log.d("Button Clicked","1-3");
                tapTableCell(view, 7, 3);
                break;
            case R.id.btn_7_4:
                Log.d("Button Clicked","1-4");
                tapTableCell(view, 7, 4);
                break;
            case R.id.btn_7_5:
                Log.d("Button Clicked","1-5");
                tapTableCell(view, 7, 5);
                break;
            case R.id.btn_7_6:
                Log.d("Button Clicked","1-6");
                tapTableCell(view, 7, 6);
                break;
            case R.id.btn_8_1:
                Log.d("Button Clicked","1-1");
                tapTableCell(view, 8, 1);
                break;
            case R.id.btn_8_2:
                Log.d("Button Clicked","1-2");
                tapTableCell(view, 8, 2);
                break;
            case R.id.btn_8_3:
                Log.d("Button Clicked","1-3");
                tapTableCell(view, 8, 3);
                break;
            case R.id.btn_8_4:
                Log.d("Button Clicked","1-4");
                tapTableCell(view, 8, 4);
                break;
            case R.id.btn_8_5:
                Log.d("Button Clicked","1-5");
                tapTableCell(view, 8, 5);
                break;
            case R.id.btn_8_6:
                Log.d("Button Clicked","1-6");
                tapTableCell(view, 8, 6);
                break;
            case R.id.btn_9_1:
                Log.d("Button Clicked","1-1");
                tapTableCell(view, 9, 1);
                break;
            case R.id.btn_9_2:
                Log.d("Button Clicked","1-2");
                tapTableCell(view, 9, 2);
                break;
            case R.id.btn_9_3:
                Log.d("Button Clicked","1-3");
                tapTableCell(view, 9, 3);
                break;
            case R.id.btn_9_4:
                Log.d("Button Clicked","1-4");
                tapTableCell(view, 9, 4);
                break;
            case R.id.btn_9_5:
                Log.d("Button Clicked","1-5");
                tapTableCell(view, 9, 5);
                break;
            case R.id.btn_9_6:
                Log.d("Button Clicked","1-6");
                tapTableCell(view, 9, 6);
                break;
            case R.id.btn_10_1:
                Log.d("Button Clicked","1-1");
                tapTableCell(view, 10, 1);
                break;
            case R.id.btn_10_2:
                Log.d("Button Clicked","1-2");
                tapTableCell(view, 10, 2);
                break;
            case R.id.btn_10_3:
                Log.d("Button Clicked","1-3");
                tapTableCell(view, 10, 3);
                break;
            case R.id.btn_10_4:
                Log.d("Button Clicked","1-4");
                tapTableCell(view, 10, 4);
                break;
            case R.id.btn_10_5:
                Log.d("Button Clicked","1-5");
                tapTableCell(view, 10, 5);
                break;
            case R.id.btn_10_6:
                Log.d("Button Clicked","1-6");
                tapTableCell(view, 10, 6);
                break;
            case R.id.btn_11_1:
                Log.d("Button Clicked","1-1");
                tapTableCell(view, 11, 1);
                break;
            case R.id.btn_11_2:
                Log.d("Button Clicked","1-2");
                tapTableCell(view, 11, 2);
                break;
            case R.id.btn_11_3:
                Log.d("Button Clicked","1-3");
                tapTableCell(view, 11, 3);
                break;
            case R.id.btn_11_4:
                Log.d("Button Clicked","1-4");
                tapTableCell(view, 11, 4);
                break;
            case R.id.btn_11_5:
                Log.d("Button Clicked","1-5");
                tapTableCell(view, 11, 5);
                break;
            case R.id.btn_11_6:
                Log.d("Button Clicked","1-6");
                tapTableCell(view, 11, 6);
                break;
            case R.id.btn_12_1:
                Log.d("Button Clicked","1-1");
                tapTableCell(view, 12, 1);
                break;
            case R.id.btn_12_2:
                Log.d("Button Clicked","1-2");
                tapTableCell(view, 12, 2);
                break;
            case R.id.btn_12_3:
                Log.d("Button Clicked","1-3");
                tapTableCell(view, 12, 3);
                break;
            case R.id.btn_12_4:
                Log.d("Button Clicked","1-4");
                tapTableCell(view, 12, 4);
                break;
            case R.id.btn_12_5:
                Log.d("Button Clicked","1-5");
                tapTableCell(view, 12, 5);
                break;
            case R.id.btn_12_6:
                Log.d("Button Clicked","1-6");
                tapTableCell(view, 12, 6);
                break;
            case R.id.btn_13_1:
                Log.d("Button Clicked","1-1");
                tapTableCell(view, 13, 1);
                break;
            case R.id.btn_13_2:
                Log.d("Button Clicked","1-2");
                tapTableCell(view, 13, 2);
                break;
            case R.id.btn_13_3:
                Log.d("Button Clicked","1-3");
                tapTableCell(view, 13, 3);
                break;
            case R.id.btn_13_4:
                Log.d("Button Clicked","1-4");
                tapTableCell(view, 13, 4);
                break;
            case R.id.btn_13_5:
                Log.d("Button Clicked","1-5");
                tapTableCell(view, 13, 5);
                break;
            case R.id.btn_13_6:
                Log.d("Button Clicked","1-6");
                tapTableCell(view, 13, 6);
                break;
            case R.id.btn_14_1:
                Log.d("Button Clicked","1-1");
                tapTableCell(view, 14, 1);
                break;
            case R.id.btn_14_2:
                Log.d("Button Clicked","1-2");
                tapTableCell(view, 14, 2);
                break;
            case R.id.btn_14_3:
                Log.d("Button Clicked","1-3");
                tapTableCell(view, 14, 3);
                break;
            case R.id.btn_14_4:
                Log.d("Button Clicked","1-4");
                tapTableCell(view, 14, 4);
                break;
            case R.id.btn_14_5:
                Log.d("Button Clicked","1-5");
                tapTableCell(view, 14, 5);
                break;
            case R.id.btn_14_6:
                Log.d("Button Clicked","1-6");
                tapTableCell(view, 14, 6);
                break;
            case R.id.btn_15_1:
                Log.d("Button Clicked","1-1");
                tapTableCell(view, 15, 1);
                break;
            case R.id.btn_15_2:
                Log.d("Button Clicked","1-2");
                tapTableCell(view, 15, 2);
                break;
            case R.id.btn_15_3:
                Log.d("Button Clicked","1-3");
                tapTableCell(view, 15, 3);
                break;
            case R.id.btn_15_4:
                Log.d("Button Clicked","1-4");
                tapTableCell(view, 15, 4);
                break;
            case R.id.btn_15_5:
                Log.d("Button Clicked","1-5");
                tapTableCell(view, 15, 5);
                break;
            case R.id.btn_15_6:
                Log.d("Button Clicked","1-6");
                tapTableCell(view, 15, 6);
                break;
            case R.id.btn_16_1:
                Log.d("Button Clicked","1-1");
                tapTableCell(view, 16, 1);
                break;
            case R.id.btn_16_2:
                Log.d("Button Clicked","1-2");
                tapTableCell(view, 16, 2);
                break;
            case R.id.btn_16_3:
                Log.d("Button Clicked","1-3");
                tapTableCell(view, 16, 3);
                break;
            case R.id.btn_16_4:
                Log.d("Button Clicked","1-4");
                tapTableCell(view, 16, 4);
                break;
            case R.id.btn_16_5:
                Log.d("Button Clicked","1-5");
                tapTableCell(view, 16, 5);
                break;
            case R.id.btn_16_6:
                Log.d("Button Clicked","1-6");
                tapTableCell(view, 16, 6);
                break;
            case R.id.btn_17_1:
                Log.d("Button Clicked","1-1");
                tapTableCell(view, 17, 1);
                break;
            case R.id.btn_17_2:
                Log.d("Button Clicked","1-2");
                tapTableCell(view, 17, 2);
                break;
            case R.id.btn_17_3:
                Log.d("Button Clicked","1-3");
                tapTableCell(view, 17, 3);
                break;
            case R.id.btn_17_4:
                Log.d("Button Clicked","1-4");
                tapTableCell(view, 17, 4);
                break;
            case R.id.btn_17_5:
                Log.d("Button Clicked","1-5");
                tapTableCell(view, 17, 5);
                break;
            case R.id.btn_17_6:
                Log.d("Button Clicked","1-6");
                tapTableCell(view, 17, 6);
                break;
            case R.id.btn_18_1:
                Log.d("Button Clicked","1-1");
                tapTableCell(view, 18, 1);
                break;
            case R.id.btn_18_2:
                Log.d("Button Clicked","1-2");
                tapTableCell(view, 18, 2);
                break;
            case R.id.btn_18_3:
                Log.d("Button Clicked","1-3");
                tapTableCell(view, 18, 3);
                break;
            case R.id.btn_18_4:
                Log.d("Button Clicked","1-4");
                tapTableCell(view, 18, 4);
                break;
            case R.id.btn_18_5:
                Log.d("Button Clicked","1-5");
                tapTableCell(view, 18, 5);
                break;
            case R.id.btn_18_6:
                Log.d("Button Clicked","1-6");
                tapTableCell(view, 18, 6);
                break;
        }
    }

}
