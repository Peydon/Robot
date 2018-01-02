import simbad.sim.*;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

public class MyEnv extends EnvironmentDescription {
    public MyEnv(int column,int row,int[][] map,int startX,int startY,int endX,int endY){

        //运行遗传算法获得最优路径
        long startTime=System.currentTimeMillis();//获取开始时间
        ImprovedGA GA = new ImprovedGA(row,column,map);
        GA.init(startX,startY,endX,endY,100);
        GA.evolve();
        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");



        //自主机器人
        add(new AGV(new Vector3d(0, 0, 0),"my robot"));
        //动态障碍物
        //add()
        //边界
        Wall w1 = new Wall(new Vector3d(column/2+1, 0, 0), column+1, 1, this);
        Wall w2 = new Wall(new Vector3d(-column/2-1, 0, 0), column+1, 1, this);
        Wall w3 = new Wall(new Vector3d(0, 0, row/2+1), row+1, 1, this);
        Wall w4 = new Wall(new Vector3d(0, 0, -row/2-1), row+1, 1, this);
        w1.rotate90(1);
        w2.rotate90(1);
        add(w1);
        add(w2);
        add(w3);
        add(w4);

        Box b = null;
        //静态障碍物
        for(int i=0;i<row;i++)
            for (int j = 0; j < column; j++)
            {
                if (map[i][j] == 0)
                {
                    b = new Box(new Vector3d(j - column / 2, 0, i - row / 2), new Vector3f(1, 1, 1),
                            this);
                    add(b);
                }
            }
    }
}
