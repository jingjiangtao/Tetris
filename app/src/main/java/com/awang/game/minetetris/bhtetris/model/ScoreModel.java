package com.awang.game.minetetris.bhtetris.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import com.awang.game.minetetris.bhtetris.utils.SQLUtils;


/**
 * Created by jing on 17-10-29.
 */

public class ScoreModel {

    //当前分数
    public int scoreNow;
    //最高分数
    public int scoreMax;

    public SQLUtils sqlUtils;


    public ScoreModel(Context context){
        sqlUtils = new SQLUtils(context,"score.db",null,1);
    }
    //加分 y=2x-1
    public void addScore(int lines) {
        if(lines ==0){
            return;
        }
        scoreNow += lines + (lines - 1);
    }

    //更新最高分数
    public void updateScoreMax(boolean isover){

        if(scoreMax==0){
           scoreMax = sqlUtils.getMaxScore();
        }
        if(scoreNow>scoreMax){
            scoreMax = scoreNow;
            if(isover)
            sqlUtils.putScore(scoreMax);
        }
    }

    //显示当前分数
    public void showNowScore(TextView textNowScore){
        if(textNowScore != null){
            textNowScore.setText(""+ scoreNow);
        }
    }

    //显示最高分
    public void showMaxScore(boolean isover,TextView textMaxScore){
        if(textMaxScore != null){
            updateScoreMax(isover);
            textMaxScore.setText(""+scoreMax);
        }
    }
}
