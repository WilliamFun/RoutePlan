package com.buaa.tools;

import com.buaa.model.Point;

/**
 * @author Zheng.Fan
 * @date 2022/09/15
 **/
public class TransTool {
    static public Point lonLat2WebMercator(Point lonLat) {
        Point mercator = new Point();
        double x = lonLat.getLng() * 20037508.34 / 180;
        double y = Math.log(Math.tan((90 + lonLat.getLat()) * Math.PI / 360)) / (Math.PI / 180);
        y = y * 20037508.34 / 180;
        mercator.setLat(y);
        mercator.setLng(x);
        return mercator;
    }

    static public Point webMercator2lonLat(Point mercator) {
        Point lonLat = new Point();
        double x = mercator.getLng() / 20037508.34 * 180;
        double y = mercator.getLat() / 20037508.34 * 180;
        y = 180 / Math.PI * (2 * Math.atan(Math.exp(y * Math.PI / 180)) - Math.PI / 2);
        lonLat.setLat(y);
        lonLat.setLng(x);
        return lonLat;
    }

    static public boolean crossObstacle(int stX, int stY, int edX, int edY, int[][] range) {
        for (int i = 0; i < range.length; i++) {
            for (int j = 0; j < range[i].length; j++) {
                if (1 == range[i][j]) {
                    double space = 0;
                    double a, b, c;
                    a = Math.sqrt((stX - edX) * (stX - edX) + (stY - edY) * (stY - edY));
                    b = Math.sqrt((i - edX) * (i - edX) + (j - edY) * (j - edY));
                    c = Math.sqrt((stX - i) * (stX - i) + (stY - j) * (stY - j));

                    if (c < 1 || b < 1) {
                        return true;
                    }
//				    if(a < 1 && (b < 1 || c<1)) {
//				        return true;
//				    }
                    boolean isC = (c * c >= a * a + b * b);
                    boolean isB = (b * b >= a * a + c * c);
                    if (isC || isB) {
                        break;
                    }
                    double p = (a + b + c) / 2;
                    double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));
                    space = 2 * s / a;
                    if (space < Math.sqrt(2.0) / 2) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
