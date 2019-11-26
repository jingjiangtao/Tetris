package com.awang.game.minetetris;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.awang.game.minetetris.bhtetris.model.SingleScoreModel;
import com.awang.game.minetetris.bhtetris.utils.SQLUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/11/30.
 */

public class ScoreRandActivity extends Activity {
    List<SingleScoreModel> scoreList;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        SQLUtils oh = new SQLUtils(ScoreRandActivity.this,"score.db",null,1);
        scoreList = new ArrayList<SingleScoreModel>();
        Cursor cursor = oh.getScore();
        while(cursor.moveToNext()){
            String id = "0";
            String tscore = cursor.getString(0);
            SingleScoreModel p = new SingleScoreModel(id,tscore);
            scoreList.add(p);
        }
        ListView lv = (ListView) findViewById(R.id.scorelistview);
        lv.setAdapter(new MyAdapter());
    }


    class MyAdapter extends BaseAdapter {

        //系统调用，用来获知集合中有多少条元素
        @Override
        public int getCount() {
            return scoreList.size();
        }

        //由系统调用，获取一个View对象，作为ListView的条目
        //position:本次getView方法调用所返回的View对象，在listView中是处于第几个条目，那么position的值就是多少
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SingleScoreModel s=scoreList.get(position);
            View v = null;
            //判断条目是否有缓存
            if(convertView == null){
                //把布局文件填充成一个View对象
                v = View.inflate(ScoreRandActivity.this, R.layout.activity_score_listview, null);
            }
            else{
                v = convertView;
            }


            //获取布局填充器对象
            //通过资源id查找组件,注意调用的是View对象的findViewById
            TextView id = (TextView) v.findViewById(R.id.id);
            id.setText(String.valueOf(position+1));
            TextView textViewscore = (TextView) v.findViewById(R.id.score);
            textViewscore.setText(s.getScore());
            return v;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }



    }
}
