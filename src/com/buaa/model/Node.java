package com.buaa.model;

/**
 * @author Zheng.Fan
 * @date 2022/09/15
 **/
public class Node {
    /**
     * 是否可达
     */
    private boolean attainability;

    /**
     * 节点坐标
     */
    private int x,y;

    /**
     * 路径走向
     */
    private int direction = 1;

    private int score;

    private String id = " ";

    public boolean isAttainability() {
        return attainability;
    }

    public void setAttainability(boolean attainability) {
        this.attainability = attainability;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
