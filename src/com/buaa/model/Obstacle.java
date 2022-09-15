package com.buaa.model;

import java.util.List;
import java.util.Vector;

/**
 * @author Zheng.Fan
 * @date 2022/09/15
 **/
public class Obstacle {
    private int type;
    private List<Point> pts;
    private double radius;
    private static final double DIFF = 1e-6D;

    public boolean containPoint(double x, double y) {
        int nCross = 0;
        if (pts.size() < 3) {
            return false;
        }
        for (int i = 0; i < pts.size(); i++) {
            System.out.print(pts.get(i));
            double p1X = pts.get(i).lng;
            double p1Y = pts.get(i).lat;
            double p2X = 0;
            double p2Y = 0;
            if (i == pts.size() - 1) {
                p2X = pts.get(0).lng;
                p2Y = pts.get(0).lat;
            } else {
                p2X = pts.get(i + 1).lng;
                p2Y = pts.get(i + 1).lat;
            }
            if (Math.abs(p1Y - p2X) < DIFF) {
                continue;
            }
            if (y < Math.min(p1Y, p2Y)) {
                continue;
            }
            if (y >= Math.max(p1Y, p2Y)) {
                continue;
            }
            double isx = (y - p1Y) * (p2X - p1X) / (p2Y - p1Y) + p1X;
            if (isx > x) {
                nCross++;
            }
        }
        return (nCross % 2 == 1);
    }

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

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

}
