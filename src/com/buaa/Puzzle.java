package com.buaa;

import com.buaa.model.Node;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author Zheng.Fan
 * @date 2022/09/15
 */
public class Puzzle {
    Node[][] maze;
    Node startNode,endNode;
    int row;
    int col;
    Node[] result = null;

    public Puzzle(){
        // 设定好的演示参数
        maze = new Node[7][9];
        row = 7;
        col = 9;
        //在矩阵中初始化地图
        for(int i=0;i<row;i++) {
            for(int j=0;j<col;j++){
                maze[i][j] = new Node();
                maze[i][j].setAttainability(true);
                maze[i][j].setX(i);
                maze[i][j].setY(j);
            }
        }
        //设定起点和终点
        startNode = maze[3][2];
        startNode.setId("&");
        endNode = maze[3][6];
        endNode.setId("@");
        //边界不可达
        for(int i=0;i<row;i++){
            maze[i][0].setAttainability(false);
            maze[i][col-1].setAttainability(false);
        }
        for(int j=0;j<col;j++){
            maze[0][j].setAttainability(false);
            maze[row-1][j].setAttainability(false);
        }
        //设置禁飞区
        maze[4][3].setAttainability(false);
        maze[2][4].setAttainability(false);
        maze[3][4].setAttainability(false);
        maze[4][4].setAttainability(false);
    }

    /**
     * 调用者自定义参数
     * @param row
     * @param col
     * @param data
     * @param stRow
     * @param stCol
     * @param edRow
     * @param edCol
     */
    public Puzzle(int row, int col, int[][] data, int stRow, int stCol, int edRow, int edCol) {
    	this.row = row;
    	this.col = col;
    	maze = new Node[row][col];
        for(int i = 0; i < row; i++) {
            for(int j = 0; j < col; j++){
                maze[i][j] = new Node();
                maze[i][j].setAttainability(true);
                maze[i][j].setX(i);
                maze[i][j].setY(j);
            }
        }
        //设定起点和终点
        startNode = maze[stRow][stCol];
        startNode.setId("&");
        endNode = maze[edRow][edCol];
        endNode.setId("@");
        //根据data设置可达性
        for(int i = 0; i < row; i++) {
            for(int j = 0; j < col; j++){
            	if(1 == data[i][j]) {
                    maze[i][j].setAttainability(false);
                }
            }
        }       
    }

    /**
     * 画图
     */
    public void printMaze(){
        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                if(maze[i][j].isAttainability()) {
                    System.out.print(maze[i][j].getId() + " ");
                } else if(!maze[i][j].isAttainability())
                    //i==0 || i==6 || j==0 ||j==8 || (i==4&&j==3) ||(i==2&&j==4) ||(i==3&&j==4) ||(i==4&&j==4)
                    //if(!maze[i][j].attainability) //i==0 || i==6 || j==0 ||j==8 || (i==4&&j==3) ||(i==2&&j==4) ||(i==3&&j==4) ||(i==4&&j==4)
                {
                    System.out.print("# ");
                } else {
                    // 未设定的点
                    System.out.print(maze[i][j].getId() + " ");
                }
            }
            System.out.println();
        }
    }

    /**
     * 计算路径
     */
    public void getPath(){
        // 初始化一个栈，将路径中的节点压入栈中
        Deque<Node> s = new LinkedList<>();
        Node curpos = startNode;
        //int curstep = 1;
        do{
            if(curpos.isAttainability()){
                curpos.setAttainability(false);
                if(curpos!=startNode && curpos!=endNode) {
                    curpos.setId("%");
                }
                s.push(curpos);
                if(curpos == endNode) {
                    printMaze();
                }
                //计算下一个节点位置
                Node tmp = nextPos(curpos,1);
                curpos = maze[tmp.getX()][tmp.getY()];
                //curpos.id++;
                //curstep++;
            } else {
                Node e = s.pop();
                while(e.getDirection() == 4 && !s.isEmpty()){
                    e.setAttainability(false);
                    e = s.pop();
                }
                if(e.getDirection() < 4){
                    e.setDirection(e.getDirection() + 1);
                    s.push(e);
                    Node tmp = nextPos(e,e.getDirection());
                    curpos = maze[tmp.getX()][tmp.getY()];
                }
            }
        }while(!s.isEmpty());
    }

    private Node nextPos(Node pos, int i){
        Node tmp = new Node();
        tmp.setDirection(pos.getDirection());
        tmp.setAttainability(pos.isAttainability());
        int x = pos.getX();
        int y = pos.getY();

        if(i == 2 && x<maze.length-1){
            //向x正方向走
            x = x+1;
            tmp.setY(pos.getY());
            tmp.setX(x);
        } else if(i == 4 && x != 0){
            //向x负方向走
            x = x-1;
            tmp.setY(pos.getY());
            tmp.setX(x);
        } else if(i == 1 && y<maze[0].length-1){
            //向y正方向
            y = y+1;
            tmp.setX(pos.getX());
            tmp.setY(y);
        } else if(i == 3 && y != 0){
            //向y负方向
            y = y - 1;
            tmp.setX(pos.getX());
            tmp.setY(y);
        }
        return tmp;
    }

    /**
     * Astar算法
     */
    public void Astar(){
        getScore();

        Deque<Node> open = new LinkedList<>();
        open.push(startNode);

        Deque<Node> close = new LinkedList<>();
        while(open.size()!=0){
            Node x = open.pop();
            if(x == endNode) {
                break;
            }
            Node[] t;
            if(x.isAttainability()){
                t = getNear(x);
                for (Node node : t) {
                    if (node != null) {
                        if (!open.contains(node) && !close.contains(node)) {
                            open.push(node);
                        } else if (open.contains(node)) {
                            sort(open);
                        } else if (!close.isEmpty() && node.getScore() < close.peek().getScore()) {
                            sort(close);
                            open.push(close.pop());
                        }
                    }
                }
                close.push(x);
                sort(open);
            }
        }
        int a = close.size();
        if(a>1) {
        	result = new Node[a];
        }
        for(int i=0;i<a;i++){
            Node tmp = close.pop();
            if (tmp == startNode) {
                tmp.setId("&");
            } else {
                tmp.setId("%");
            }
            maze[tmp.getX()][tmp.getY()] = tmp;
            result[i] = tmp;
        }
        printMaze();
    }


    private void sort(Deque<Node> s) {
        Node[] t = new Node[s.size()];
        int x = s.size();
        //将栈中的元素按出栈顺序放到数组
        for(int i=0;i<x;i++){
            t[i] = s.pop();
        }
        //选择排序:双重循环遍历数组，每经过一轮比较，找到评分最大元素的下标，将其交换至首位
        for(int i=0;i<t.length-1;i++) {
            int maxindex = i;
            for(int j=i+1;j<t.length;j++){
                if(t[j].getScore() > t[maxindex].getScore()){
                    maxindex = j;
                }
            }
            Node tmp = t[i];
            t[i] = t[maxindex];
            t[maxindex] = tmp;
        }
        //按顺序压栈，将评分最低(距离最小)的放在栈顶
        for (Node node : t) {
            s.push(node);
        }
    }

    /**
     * 计算得分，与终点的欧几里得距离or曼哈顿距离
     */
    private void getScore(){
        for (Node[] nodes : maze) {
            for (Node node : nodes) {
                //maze[i][j].score = Math.abs(endNode.x - maze[i][j].x) + Math.abs(endNode.y - maze[i][j].y);
                node.setScore((int) Math.sqrt((endNode.getX() - node.getX()) * (endNode.getX() - node.getX()) +
                        (endNode.getY() - node.getY()) * (endNode.getY() - node.getY())));
            }
        }
    }

    /**
     * 获取点周围的节点
     * @param e
     * @return
     */
    private Node[] getNear(Node e){

        Node leftNode = (e.getY()!=0 ? maze[e.getX()][e.getY()-1] : null);
        Node rightNode = (e.getY()!=maze[0].length-1 ? maze[e.getX()][e.getY()+1] : null);
        Node upNode = (e.getX()!=0 ? maze[e.getX()-1][e.getY()] : null);
        Node downNode = (e.getX()!=maze.length-1 ? maze[e.getX()+1][e.getY()] : null);
        Node leftUpNode = ((e.getY()!=0&&e.getX()!=0) ? maze[e.getX()-1][e.getY()-1] : null);
        Node rightUpNode = ((e.getY()!=maze[0].length-1&&e.getX()!=0) ? maze[e.getX()-1][e.getY()+1] : null);
        Node leftDownNode = ((e.getY()!=0&&e.getX()!=maze.length-1) ? maze[e.getX()+1][e.getY()-1] : null);
        Node rightDownNode = ((e.getY()!=maze[0].length-1&&e.getX()!=maze.length-1) ? maze[e.getX()+1][e.getY()+1] : null);

        return new Node[]{leftNode,rightNode,upNode,downNode,leftUpNode,rightUpNode,leftDownNode,rightDownNode};
    }

    public Node[] getResult(){
    	return result;
    }

    public static void main(String[] args){
    	
        Puzzle p = new Puzzle();
        System.out.println("初始化地图");
        p.printMaze();
        System.out.println();
        System.out.println("计算路径，显示遍历过的点");
        p.getPath();

        p = new Puzzle();
        System.out.println();
        System.out.println("规划结果如下");
        p.Astar();


    }
}
