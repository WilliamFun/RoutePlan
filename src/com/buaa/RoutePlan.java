package com.buaa;

import com.buaa.model.*;
import com.buaa.tools.TransTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.lang.Math;
import java.util.Arrays;


/**
 * @author asus
 */
public class RoutePlan {
	static public Route getRoute(Point startPt, Point endPt, List<Obstacle> Obtls, Params para) {
		Route result = new Route();
		Point startPt_xy = TransTool.lonLat2WebMercator(startPt);
		Point endPt_xy = TransTool.lonLat2WebMercator(endPt);
		System.out.printf("�������Ϊ��%f * %f\n",startPt_xy.getLng(), startPt_xy.getLat());
		System.out.printf("�յ�����Ϊ��%f * %f\n",endPt_xy.getLng(), endPt_xy.getLat());
		List<Obstacle> Obtls_xy = new ArrayList<>();
		for (Obstacle obstl : Obtls) {
			Obstacle obstl_xy = new Obstacle();
			List<Point> pts = new ArrayList<>();
			for (Point pt : obstl.getPts()) {
				Point pt_xy = TransTool.lonLat2WebMercator(pt);
				pts.add(pt_xy);
			}
			obstl_xy.setPts(pts);
			Obtls_xy.add(obstl_xy);	
		}
        for(Obstacle obtltmp : Obtls_xy) {
        	System.out.printf("ת���� �ϰ��� �߽������ %d\n", obtltmp.getPts().size());
        	for(Point ptmp:obtltmp.getPts()) {
        		System.out.printf("ת���� �ϰ��� �߽��λ�� %f %f\n", ptmp.getLng(),ptmp.getLat());
        	}
        }
		System.out.println("0. ����׼����������ͳһ����XYZֱ������ϵ�Ŀռ� ���");
		double midX = (startPt_xy.getLng() + endPt_xy.getLng())/2;
		double midY = (startPt_xy.getLat() + endPt_xy.getLat())/2;
		double dis = Math.sqrt((startPt_xy.getLng() - endPt_xy.getLng())*(startPt_xy.getLng() - endPt_xy.getLng()) +
				(startPt_xy.getLat() - endPt_xy.getLat())*(startPt_xy.getLat() - endPt_xy.getLat()));
		dis = 1.4*dis/2;
		double minX = midX - dis;
		double minY = midY - dis;
		double maxX = midX + dis;
		double maxY = midY + dis;
		int gridSize = Math.max(para.getHorizontal(), 10);
		int horn = (int)(2*dis/gridSize);
		System.out.printf("�����ĵ�ͼ�ߴ�Ϊ��%d*%d\n", horn, horn);
		System.out.printf("�����ĵ�ͼ��׼������Ϊ��%f*%f\n", minX, maxY);
		int[][] map = new int[horn][horn];
		int st_x = (int)(startPt_xy.getLng() - minX)/gridSize;
		int st_y = (int)(maxY - startPt_xy.getLat())/gridSize;
		int ed_x = (int)(endPt_xy.getLng() - minX)/gridSize;
		int ed_y = (int)(maxY - endPt_xy.getLat())/gridSize;
		System.out.printf("��� �յ�����Ϊ��%d %d -> %d %d\n",st_y, st_x, ed_y, ed_x);
		map[st_y -1][st_x - 1] = -1;
		map[ed_y - 1][ed_x - 1] = -9;
		for (int i=0;i<horn;i++) {
			System.out.println (Arrays.toString (map[i]));
		}
		System.out.println("1��  ��������յ㣬�����㷨������Χ����ʼ�������ռ� ���");
		for (Obstacle obstl : Obtls_xy) {
			for(Point pt1 : obstl.getPts()) {
				System.out.printf("�ϰ����� �߽����Ϣ%f %f\n", pt1.getLng(), pt1.getLat());
			}
            for(int m = 0; m < horn; m++) {
                for(int n = 0; n < horn; n++) {
                	double m_y = maxY - m*gridSize; // 
                	double n_x = minX + n*gridSize; // 
                	if(m<5&&n<5) {
						System.out.printf("��ͼλ��%d��%d�� ->��Ӧ���� %f %f\n", m, n, n_x, m_y);
					}
                	boolean inPolygon = obstl.containPoint(n_x, m_y);
                    if(inPolygon) {
                    	//mapObtl[m][n]= 1;
                    	map[m][n]= 1;
                    }
                }
            }   
		}
		for (int i=0;i<horn;i++) {
			System.out.println (Arrays.toString (map[i]));
		}
		System.out.println("2. �����ϰ����������һ�������ռ� ���");
		System.out.println("3. ���ø��ʵ�ͼ�����������ռ��Ż� ���������ƣ���ǰ�汾δʵ��");
    	Puzzle aStar = new Puzzle(horn, horn, map, st_y,st_x,ed_y,ed_x);
    	System.out.println("��ʼ���Թ����£�������&������㣬@�����յ㣬#�����ϰ���");
    	aStar.printMaze();    //�����ʼ�����Թ�
    	System.out.println();
    	System.out.println("��A*�㷨Ѱ··�����£�");
    	aStar.Astar();
		System.out.println("4. ����A*�����㷨��������·�� ��ϣ����ۺ�������δϸ������������");
		Node[] resultRoute = aStar.getResult();
		System.out.printf("��A*�㷨Ѱ··��������Ϊ��%d\n", resultRoute.length);
		for(int i = 0; i < resultRoute.length; i++) {
			System.out.printf("��A*�㷨Ѱ··��������Ϊ��%d:%d %d\n", i,resultRoute[i].getX(), resultRoute[i].getY());
		}
		Vector<Node > smoothResult = new Vector<Node>();
		int target_x = resultRoute[resultRoute.length - 1].getX();
		int target_y = resultRoute[resultRoute.length - 1].getY();
		int target_id = resultRoute.length - 1;
		smoothResult.add(resultRoute[resultRoute.length - 1]);
		
		for(int i = 0; i < resultRoute.length - 1; i++) {
			int[][] stopMap = new int[horn][horn];
			stopMap[2][2] = 1;
			stopMap[3][3] = 1;
			stopMap[4][6] = 1;
			if(i == target_id - 1) {
				smoothResult.add(resultRoute[i]);
				target_x = resultRoute[i].getX();
				target_y = resultRoute[i].getY();
				target_id = i;
				if(0 == i) {
					break;
				}
				//
				i = -1;			
			} else {
				boolean isCross = TransTool.crossObstacle(resultRoute[i].getX(), resultRoute[i].getY(), target_x, target_y, map);
				if(!isCross) {
					smoothResult.add(resultRoute[i]);
					target_x = resultRoute[i].getX();
					target_y = resultRoute[i].getY();
					target_id = i;
					if(0 == i) {
						break;
					} else {
						i = -1;
					}
				}
			}
		}
		System.out.printf("ƽ��·���󣬺�������Ϊ��%d\n", smoothResult.size());
		System.out.println("5. ����A*�㷨�ٴ�ƽ�������� ��� �����Ƿ�Խ�����������в����ƣ����ܴ���BUG");
		List<Point> pts = new ArrayList<>();
		pts.add(startPt);
		for(Node nd:smoothResult) {
			double x = nd.getY()*gridSize + minX;
			double y = maxY - nd.getX()*gridSize;
			Point pt_tmp = new Point(x, y);
			Point pt_rst = TransTool.webMercator2lonLat(pt_tmp);
			pts.add(pt_rst);
		}
		pts.add(endPt);
		result.setPts(pts);
		//
		System.out.printf("6. ת��Ϊ���������ʽ������ϵ������Ϊ%d\n",pts.size());
		for(Point pt: pts) {
			System.out.printf("����·����� ����%f γ��%f\n", pt.getLng(), pt.getLat());
		}
		return result;	
	}
}
