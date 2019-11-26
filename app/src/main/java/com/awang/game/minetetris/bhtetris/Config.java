package com.awang.game.minetetris.bhtetris;

/**
 * Created by jing on 17-10-27.
 */

public class Config {

    public static final int MAPSX = 10;
    public static final int MAPSY = 20;

    //地图x方向的宽度
    public static int XWIDTH;
    //地图y方向的高度
    public static int YHEIGH;

    //间距
    public static int PADDING;
    public static final int SPLIT_PADDING = 25;

    //游戏区域背景颜色
    public static final int GAME_BG = 0x10000000;
    //信息区域背景颜色
    public static final int INFO_BG = 0x10000000;
    //辅助线颜色
    public static final int AUXILIARY_LINES_COLOR = 0xffffff;
    //游戏状态颜色
    public static final int STATE_COLOR = 0xff000000;
    //地图方块颜色
    public static final int MAP_BOX_COLOR = 0x50000000;
    //下落方块颜色
    public static final int BOX_COLOR = 0xff000000;
}
