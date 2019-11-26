package com.awang.game.minetetris.bhtetris.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.awang.game.minetetris.bhtetris.Config;


/**
 * Created by jing on 17-10-27.
 */

public class MapsModel {
//测试画笔
public int []paintcolor=new int[]{Color.RED,Color.parseColor("#663300"),Color.YELLOW,Color.GREEN,Color.parseColor("#FF6600"),Color.BLUE,Color.MAGENTA};
    //方块大小
    public int boxSize;
    //地图
    public boolean[][] maps;
    public int [][]color;
    //地图画笔
    Paint mapPaint;
    //辅助线画笔
    Paint linePaint;
    //状态画笔
    Paint statePaint;
    //高度
    int yHeight;
    //宽度
    int xWidth;

    public MapsModel(int boxSize, int xWidth, int yHeight){
        this.boxSize = boxSize;
        this.xWidth = xWidth;
        this.yHeight = yHeight;
        //初始化地图
        maps = new boolean[Config.MAPSX][Config.MAPSY];
        color=new int[Config.MAPSX][Config.MAPSY];
        //初始化地图画笔
        mapPaint = new Paint();

        mapPaint.setAntiAlias(true);
       //测试

        //初始化辅助线画笔
        linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#ffffff"));
        //抗锯齿
        linePaint.setAntiAlias(true);

        //初始化状态画笔
        statePaint = new Paint();
        statePaint.setColor(Color.parseColor("#EE0000"));
        statePaint.setAntiAlias(true);
        statePaint.setTextSize(100);
    }

    public void drawMaps(Canvas canvas) {

        RectF rel;
        for(int x=0; x<maps.length; x++) {
            for (int y = 0; y < maps[x].length; y++) {
                if (maps[x][y] == true) {
                    mapPaint.setColor(paintcolor[color[x][y]]);
//                    canvas.drawRect(x*boxSize+3,y*boxSize+3,x*boxSize+boxSize-3,y*boxSize+boxSize-3,mapPaint);
                    rel = new RectF(x * boxSize + 3, y * boxSize + 3, x * boxSize + boxSize - 3, y * boxSize + boxSize - 3);
                    canvas.drawRoundRect(rel, 15, 15, mapPaint);
                }
            }
        }
    }

    public void drawLines(Canvas canvas, boolean isAuxiliaryLines) {

        if(!isAuxiliaryLines)return;
        //纵向线
        for(int x=0;x<maps.length;x++){
            canvas.drawLine(x*boxSize,0,x*boxSize,yHeight,linePaint);
        }
        //横向线
        for (int y=0;y<maps[0].length;y++){
            canvas.drawLine(0,y*boxSize,xWidth,y*boxSize,linePaint);
        }
    }

    //画状态
    public void drawState(Canvas canvas, boolean isOver, boolean isPause) {

        //游戏结束状态
        if(isOver){
            canvas.drawText("游戏结束",xWidth/2-statePaint.measureText("游戏结束")/2,yHeight/2,statePaint);
        }
        //暂停状态
        if(isPause&&!isOver){
            canvas.drawText("暂停...",xWidth/2-statePaint.measureText("暂停...")/2,yHeight/2,statePaint);
        }
    }


    //清除地图
    public void cleanMaps() {

        for(int x=0; x<maps.length;x++){
            for(int y=0; y<maps[x].length;y++){
                maps[x][y] = false;
            }

        }
    }

    //消行处理
    public int cleanLine() {
        int line = 0;
        for(int y=maps[0].length-1; y > 0; y--){
            if(checkLine(y)) {
                //执行消行
                deleteLine(y);
                line++;
                y++;
            }

        }
        return line;
    }

    //消行判断
    public boolean checkLine(int y){
        for(int x=0; x < maps.length; x++){
            //如果有一个不为true,则该行不能消除
            if(!maps[x][y]){
                return false;
            }
        }
        return true;
    }

    //执行消行
    public void deleteLine(int dy){
        for(int y=dy; y>0;y--) {
            for (int x = 0; x < maps.length; x++) {
                maps[x][y] = maps[x][y - 1];
            }
        }
    }
}
