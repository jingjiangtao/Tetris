
package com.awang.game.minetetris;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.awang.game.minetetris.bhtetris.Config;
import com.awang.game.minetetris.bhtetris.controller.GameController;


public class GameScreenActivity extends Activity implements View.OnClickListener {
    public int initiaVilocity;
    public int i;
    //游戏区域控件
    public View view;
    //下一块预览区域
    public View nextPanel;
    //当前分数TextView
    public TextView scoreNowTextView;
    //最高分TextView
    public TextView scoreMaxTextView;
    //控制器
    GameController gameController;
    FrameLayout layoutGame;

    public final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String type = msg.obj.toString();
            Button btnPause = findViewById(R.id.btnPause);

            switch (type) {
                case "invalidate":
                    //重绘
                    view.invalidate();
                    nextPanel.invalidate();
                    //刷新分数
                    gameController.scoreModel.showNowScore(scoreNowTextView);
                    gameController.scoreModel.showNowScore(scoreNowTextView);
                    gameController.scoreModel.showMaxScore(gameController.isOver, scoreMaxTextView);
                    break;
                case "pause":
                    btnPause.setText("暂停");
                    break;
                case "continue":
                    btnPause.setText("继续");
                    break;
                default:
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gamescreen);
        Intent intent=this.getIntent();
        //取得方法1
        initiaVilocity=intent.getIntExtra("v",500);
        i=1;
        gameController = new GameController(handler, this);
        initView();
        gameController.scoreModel.showMaxScore(false,scoreMaxTextView);
        initListener();
    }
    //初始化监听
    public void initListener() {
        findViewById(R.id.btnLeft).setOnClickListener(this);
        findViewById(R.id.btnTop).setOnClickListener(this);
        findViewById(R.id.btnRight).setOnClickListener(this);
        findViewById(R.id.btnBottom).setOnClickListener(this);
        findViewById(R.id.btnStart).setOnClickListener(this);
        findViewById(R.id.btnPause).setOnClickListener(this);
        findViewById(R.id.btnMoveDown).setOnClickListener(this);
        findViewById(R.id.btnAuxiliaryLines).setOnClickListener(this);
    }


    //初始化视图
    public void initView() {


        //1.得到父容器
        layoutGame = (FrameLayout) findViewById(R.id.layoutGame);
        //2.实例化游戏区域
        view = new View(this){
            //重写游戏区域 绘制

            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                //绘制

                gameController.draw(canvas);
            }
        };
        //3.设置游戏区域大小
        view.setLayoutParams(new ViewGroup.LayoutParams(Config.XWIDTH, Config.YHEIGH));
        //设置背景颜色
        view.setBackgroundColor(Config.GAME_BG);

        view.setPadding(Config.PADDING,Config.PADDING,Config.PADDING,Config.PADDING);
        //4.添加到父容器中
        layoutGame.addView(view);

        //设置信息区域间距
        LinearLayout layoutInfo = (LinearLayout) findViewById(R.id.layoutInfo);
        layoutInfo.setPadding(0, Config.PADDING,Config.PADDING/2,Config.PADDING);


        //实例化下一块预览区域
        FrameLayout layoutNext = (FrameLayout) findViewById(R.id.layoutNext);
        nextPanel = new View(this){
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                gameController.drawNext(canvas, nextPanel.getWidth());
            }
        };
        //设置大小
        nextPanel.setLayoutParams(new ViewGroup.LayoutParams(-1,220));
        nextPanel.setBackgroundColor(Config.INFO_BG);
        //添加进父容器
        layoutNext.addView(nextPanel);

        scoreNowTextView = (TextView) findViewById(R.id.textNowScore);
        scoreMaxTextView = (TextView) findViewById(R.id.textMaxScore);

    }


    @Override
    public void onClick(View v) {
        gameController.onClick(v.getId(),initiaVilocity);
        //刷新视图
        view.invalidate();
        nextPanel.invalidate();
    }

}

