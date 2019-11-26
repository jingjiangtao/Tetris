package com.awang.game.minetetris.bhtetris.model;

/**
 * Created by Administrator on 2017/11/30.
 */

public class SingleScoreModel {

    private String id;
    private String Score;

    public String getId() {
        return id;
    }

    public String getScore() {
        return Score;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setScore(String score) {
        Score = score;
    }

    public SingleScoreModel(String id, String score) {
        this.id = id;
        Score = score;
    }
}
