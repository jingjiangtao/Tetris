package com.awang.game.minetetris.bhtetris.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;


import java.util.Random;

/**
 * Created by jing on 17-10-27.
 */

public class BoxsModel {

    //颜色数组
    public int []color=new int[]{Color.RED,Color.parseColor("#663300"),Color.YELLOW,Color.GREEN,Color.parseColor("#FF6600"),Color.BLUE,Color.MAGENTA};

    //方块
    public Point[] boxs = new Point[]{};
    //方块的种类
    final int TYPE = 7;
    //方块的类型
    public int boxType;
    //方块大小
    public int boxSize;
    //方块画笔
    Paint boxPaint;
    //下一块方块
    public Point[] boxNext;
    //下一块方块的类型
    public int boxNextType;
    //下一块方块格子大小
    public int boxNextSize;

    public BoxsModel(int boxSize){
        this.boxSize = boxSize;
        //初始化方块画笔
        boxPaint = new Paint();
        //抗锯齿
        boxPaint.setAntiAlias(true);

    }


    //新的方块
    public void newBoxs() {

        if(boxNext==null){
            //生成下一块

            newBoxsNext();
        }

        //把下一块赋给当前块
        boxs = boxNext;
        boxType = boxNextType;
        //生成下一块
        newBoxsNext();
    }

    //生成下一块方块
    public void newBoxsNext(){

        //随机生成一个新的方块
        Random random = new Random();
        boxNextType = random.nextInt(TYPE);

        //生成下一块;
        switch (boxNextType){
            //O
            case 0:
                boxNext = new Point[]{new Point(4,0),new Point(5,0),new Point(4,1),new Point(5,1)};
                break;
            //L
            case 1:
                boxNext = new Point[]{new Point(4,1),new Point(5,0),new Point(3,1),new Point(5,1)};
                break;
            //J
            case 2:
                boxNext = new Point[]{new Point(4,1),new Point(3,0),new Point(3,1),new Point(5,1)};
                break;
            //I
            case 3:
                boxNext = new Point[]{new Point(4,0),new Point(3,0),new Point(5,0),new Point(6,0)};
                break;
            //S
            case 4:
                boxNext = new Point[]{new Point(4,1),new Point(4,0),new Point(3,1),new Point(5,0)};
                break;
            //Z
            case 5:
                boxNext = new Point[]{new Point(4,1),new Point(3,0),new Point(4,0),new Point(5,1)};
                break;
            //T
            case 6:
                boxNext = new Point[]{new Point(4,1),new Point(4,0),new Point(3,1),new Point(5,1)};
                break;
        }

    }

    //绘制方块
    public void drawBoxs(Canvas canvas){
        RectF rel;
        boxPaint.setColor(color[boxType]);

        for(int i=0;i< boxs.length; i++){
//            canvas.drawRect(boxs[i].x*boxSize+3,boxs[i].y*boxSize+3,boxs[i].x*boxSize+boxSize-3,boxs[i].y*boxSize+boxSize-3,boxPaint);//矩形绘制
            rel = new RectF(boxs[i].x*boxSize+3,boxs[i].y*boxSize+3,boxs[i].x*boxSize+boxSize-3,boxs[i].y*boxSize+boxSize-3);
            canvas.drawRoundRect(rel, 15, 15, boxPaint);
        }
    }

    //移动
    public boolean move(int x, int y, MapsModel mapsModel){

        for(int i=0; i<boxs.length; i++){
            //移动后如果出界,返回false
            if(checkBoundary(boxs[i].x + x,boxs[i].y + y,mapsModel)){
                return false;
            }
        }
        for(int i=0; i<boxs.length; i++){
            boxs[i].x += x;
            boxs[i].y += y;
        }
        return true;
    }

    public boolean rotate(MapsModel mapsModel){
        //田字型不可旋转
        if(boxType==0){
            return false;
        }
        //遍历方块数组，每一个都绕着中心点旋转
        for(int i=0; i<boxs.length; i++){
            //边界判断
            int checkX = -boxs[i].y + boxs[0].y + boxs[0].x;
            int checkY = boxs[i].x - boxs[0].x + boxs[0].y;
            if(checkBoundary(checkX,checkY,mapsModel)){
                return false;
            }
        }
        for(int i=0; i<boxs.length; i++){
            //旋转算法,笛卡尔公式,顺时针旋转90度
            int checkX = -boxs[i].y + boxs[0].y + boxs[0].x;
            int checkY = boxs[i].x - boxs[0].x + boxs[0].y;
            boxs[i].x = checkX;
            boxs[i].y = checkY;
        }
        return true;
    }

    /**
     * 边界判断
     * 传入x y,判断是否在边界外
     * @param x 方块x坐标
     * @param y 方块y坐标
     * @return true出界 false未出界
     */
    public boolean checkBoundary(int x, int y, MapsModel mapsModel){

        return (x<0||y<0||x>=mapsModel.maps.length||y>=mapsModel.maps[0].length||mapsModel.maps[x][y]);
    }


    //绘制下一块预览
    public void drawNext(Canvas canvas, int width) {
        RectF rel;
        boxPaint.setColor(color[boxNextType]);
        if(boxNextSize==0){
            boxNextSize = width/6;
        }
        if(boxNext != null){
            for(int i=0; i<boxNext.length; i++){
                rel = new RectF((boxNext[i].x-2)*boxNextSize+3,
                        (boxNext[i].y+1)*boxNextSize+3,
                        (boxNext[i].x-2)*boxNextSize+boxNextSize-3,
                        (boxNext[i].y+1)*boxNextSize+boxNextSize-3);
                canvas.drawRoundRect(rel, 15, 15, boxPaint);
//                canvas.drawRect((boxNext[i].x-2)*boxNextSize+3,
//                                (boxNext[i].y+1)*boxNextSize+3,
//                                (boxNext[i].x-2)*boxNextSize+boxNextSize-3,
//                                (boxNext[i].y+1)*boxNextSize+boxNextSize-3,
//                                boxPaint);

            }
        }

    }
}
