package com.awang.game.minetetris;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Administrator on 2017/11/29.
 */

public class MainActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClick_easy(View v)
    {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, GameScreenActivity.class);
        intent.putExtra("v", 500);

        startActivity(intent);

    }
    public void onClick_usually(View v)
    {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, GameScreenActivity.class);
        intent.putExtra("v", 400);

        startActivity(intent);

    }
    public void onClick_difficult(View v)
    {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, GameScreenActivity.class);
        intent.putExtra("v", 300);

        startActivity(intent);

    }
    public void onClick_score(View v)
    {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ScoreRandActivity.class);
        startActivity(intent);

    }



}