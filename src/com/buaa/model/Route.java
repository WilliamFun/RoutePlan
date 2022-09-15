package com.buaa.model;

import java.util.List;
import java.util.Vector;

/**
 * @author Zheng.Fan
 * @date 2022/09/15
 **/
public class Route {

    int type;
    List<Point> pts;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Point> getPts() {
        return pts;
    }

    public void setPts(List<Point> pts) {
        this.pts = pts;
    }
}
