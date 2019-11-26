package com.awang.game.minetetris.bhtetris.controller;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.awang.game.minetetris.R;
import com.awang.game.minetetris.bhtetris.Config;
import com.awang.game.minetetris.bhtetris.model.BoxsModel;
import com.awang.game.minetetris.bhtetris.model.MapsModel;
import com.awang.game.minetetris.bhtetris.model.ScoreModel;


/**
 * Created by jing on 17-10-27.
 */

public class GameController {

    //地图
    MapsModel mapsModel;
    //游戏区域宽 高

    //方块
    BoxsModel boxsModel;

    //分数模型
    public ScoreModel scoreModel;
    //方块大小
    int boxSize;
    //自动下落线程
    public Thread downThread;
    //暂停状态
    public boolean isPause;
    //结束状态
    public boolean isOver;
    //辅助线开关
    public boolean isAuxiliaryLines = true;

    Handler handler;

    public GameController(Handler handler, Context context) {
        this.handler = handler;
        initData(context);
    }


    public void initData(Context context) {
        //获得屏幕宽度
        int width = getScreenWidth(context);
        //游戏区域宽度==屏幕宽度*14/24
        Config.XWIDTH = width * 14/24;
        //游戏区域地高度==宽度*2
        Config.YHEIGH = 2* Config.XWIDTH;
        //设置间距=屏幕宽度的1/20
        Config.PADDING = width/Config.SPLIT_PADDING;

        //初始化方块大小=游戏区域宽度/10
        boxSize = Config.XWIDTH/ Config.MAPSX;

        //初始化地图
        mapsModel = new MapsModel(boxSize,Config.XWIDTH,Config.YHEIGH);
        //实例化方块
        boxsModel = new BoxsModel(boxSize);
        //实例化分数模型
        scoreModel = new ScoreModel(context);

    }


    //获得屏幕宽度
    public static int getScreenWidth(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }




    //开始游戏
    public void startGame(final int INITIAVILOCITY) {
        //清除地图
        mapsModel.cleanMaps();
        //重置当前分数
        scoreModel.scoreNow = 0;
        if(downThread==null){
            downThread  = new Thread(){
                @Override
                public void run() {
                    super.run();
                    while (true){
                        try {
                            if(scoreModel.scoreNow<50) {
                                sleep(INITIAVILOCITY);
                            }
                            else if(scoreModel.scoreNow>=50 && scoreModel.scoreNow<100){
                                sleep(INITIAVILOCITY-50);
                            }
                            else if(scoreModel.scoreNow>=100 && scoreModel.scoreNow<150){
                                sleep(INITIAVILOCITY-100);
                            }
                            else if(scoreModel.scoreNow>=150 && scoreModel.scoreNow<200){
                                sleep(INITIAVILOCITY-150);
                            }else{
                                sleep(INITIAVILOCITY-200);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //判断游戏是否结束
                        //判断暂停状态
                        if(isOver||isPause){
                            continue;
                        }
                        //执行一次下落
                        moveDown();
                        Message msg = new Message();
                        msg.obj = "invalidate";
                        //通知主线程刷新
                        handler.sendMessage(msg);


                    }

                }
            };
            downThread.start();
        }
        //游戏结束和暂停设为false
        isOver = false;
        isPause = false;
        //生成新的方块
        boxsModel.newBoxs();
    }


    //下落
    public boolean moveDown(){

        if(isOver){
            return false;
        }
        int lines = 0;
        //1.移动成功，不做处理
        if(boxsModel.move(0,1,mapsModel)){

            return true;
        }
        //2.移动失败，堆积处理
        addBoxs();
        //3.消行处理
         lines = mapsModel.cleanLine();
        //加分
        scoreModel.addScore(lines);

        //4.生成新的方块
        boxsModel.newBoxs();
        //5.结束判断
        isOver = checkOver();
        if(isOver&&scoreModel.scoreNow!=0) scoreModel.sqlUtils.putScore(scoreModel.scoreNow);
        return false;
    }


    public boolean checkOver(){
        for(int i=0; i<boxsModel.boxs.length; i++){
            if(mapsModel.maps[boxsModel.boxs[i].x][boxsModel.boxs[i].y]){
                return true;
            }
        }
        return false;
    }

    //堆积处理
    public void addBoxs(){
        for(int i=0; i<boxsModel.boxs.length; i++){
            mapsModel.maps[boxsModel.boxs[i].x][boxsModel.boxs[i].y] = true;
            mapsModel.color[boxsModel.boxs[i].x][boxsModel.boxs[i].y] = boxsModel.boxType;
        }
    }

    //绘制游戏区域
    public void draw(Canvas canvas) {
        //绘制地图

       mapsModel.drawMaps(canvas);
        //地图辅助线
        mapsModel.drawLines(canvas, isAuxiliaryLines);
        //方块绘制
        boxsModel.drawBoxs(canvas);

        //绘制状态
        mapsModel.drawState(canvas,isOver,isPause);
    }

    //设置暂停
    public void setPause() {
        Message msg = new Message();
        if(isPause){
            isPause = false;
            msg.obj = "pause";

        }else{
            isPause = true;
            msg.obj = "continue";
        }
        handler.sendMessage(msg);
    }

    //设置辅助线开关
    private void setAuxiliaryLines() {
        isAuxiliaryLines = (isAuxiliaryLines?false:true);

    }

    public void onClick(int id,final int INITIAVILOCITY) {
        switch (id){
            case R.id.btnLeft:
                if(isPause)return;
                boxsModel.move(-1,0,mapsModel);
                break;
            case R.id.btnTop:
                if(isPause)return;
                boxsModel.rotate(mapsModel);
                break;
            case R.id.btnRight:
                if(isPause)return;
                boxsModel.move(1,0,mapsModel);
                break;
            case R.id.btnBottom:
                if(isPause)return;
                //快速下落
                while(true){
                    //如果下落失败，结束循环
                    if(!moveDown()){
                        break;
                    }
                }
                break;
            case R.id.btnMoveDown:
                if(isPause){
                    return;
                }
                moveDown();
                break;
            case R.id.btnStart:
                startGame(INITIAVILOCITY);
                break;
            case R.id.btnPause:
                setPause();
                break;
            case R.id.btnAuxiliaryLines:
                setAuxiliaryLines();
                break;
        }
    }


    //绘制下一块预览区域
    public void drawNext(Canvas canvas, int width) {
        boxsModel.drawNext(canvas, width);
    }
}
