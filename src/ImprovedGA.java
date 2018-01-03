import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ImprovedGA {

    //栅格信息
    private int [][] map;//栅格化地图
    private int row;//行
    private int column;//列
    private int sX;//起点坐标
    private int sY;
    private int eX;//终点坐标
    private int eY;
    private int goal;//终点坐标的方格编号

    //遗传算法参数
    private double cross_prob = 0.7;
    private double mutate_prob = 0.1;
    private double population_num = 30;
    private int epochs = 100;
    private int k = 1;

    //搜索禁忌表
    private HashSet<Integer> banTable=new HashSet<>();

    public ImprovedGA(int row,int column, int [][]map)
    {
        this.row=row;
        this.column=column;
        this.map=map;

    }

    public void init(int sX,int sY ,int eX, int eY , int max_epochs)
    {
        this.sX=sX;
        this.sY=sY;
        this.eX=eX;
        this.eY=eY;
        this.epochs=max_epochs;
        goal = eX+eY*this.row;

    }
    //进化过程
    public void evolve()
    {
        //获得初始种群
        ArrayList<ArrayList<Integer>> populations = getFirstPopulation();
        //进化
        while(k<epochs)
        {
            k++;
        }

    }

    public void getRouteLength()
    {

    }

    public void getBestRouteMap()
    {

    }

    private ArrayList<ArrayList<Integer>> getFirstPopulation()
    {
        //初始种群
        ArrayList<ArrayList<Integer>> populations=new ArrayList<>();
        //生成初始种群个数的序列
        while(populations.size()<this.population_num)
        {
            int tX = this.sX;
            int tY = this.sY;
            ArrayList<Integer> population = new ArrayList<>();
            int current_pos =utils.getIdx(tX,tY,row);
            population.add(current_pos);
            while(current_pos!=goal)
            {
                //8个方向的栅格xy坐标
                int xarray[] = new int[]{tX-1,tX-1,tX-1,tX,tX+1,tX+1,tX+1,tX};
                int yarray[] = new int[]{tY-1,tY,tY+1,tY+1,tY+1,tY,tY-1,tY-1};
                //8个栅格和目标点的距离
                double d[]= new double[]{
                        utils.getDistance(xarray[0],yarray[0],eX,eY),
                        utils.getDistance(xarray[1],yarray[1],eX,eY),
                        utils.getDistance(xarray[2],yarray[2],eX,eY),
                        utils.getDistance(xarray[3],yarray[3],eX,eY),
                        utils.getDistance(xarray[4],yarray[4],eX,eY),
                        utils.getDistance(xarray[5],yarray[5],eX,eY),
                        utils.getDistance(xarray[6],yarray[6],eX,eY),
                        utils.getDistance(xarray[7],yarray[7],eX,eY)
                };
                //五个点的概率
                double p[]= new double[]{-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0};
                //概率分母
                double D_Sum =0.0;
                //坐标不越界而且不是障碍物而且没被搜过的称为自由栅格
                for(int i=0;i<8;i++)
                {
                    int x=xarray[i];
                    int y=yarray[i];
                    if(x>=0&&x<row&&y>=0&&y<row) {
                        if (map[x][y] == 1) {
                            if (!banTable.contains(utils.getIdx(x, y, row))){
                                if(d[i]==0)d[i]=0.000000001;
                                p[i] = 1/d[i];
                                D_Sum += 1/d[i];
                            }
                        }
                    }
                }
                //没有自由栅格，放弃生成该个体
                if(D_Sum==0.0)break;
                //概率归一化
                for(int i=0;i<8;i++)
                {
                    if(p[i]>=0)
                    {
                       p[i]/=D_Sum;
                    }
                }
                double r =Math.random();
                double sum1,sum2=0.0;
                for(int i =0;i<8;i++)
                {
                    if(p[i]<0)continue;
                    sum1=sum2;
                    sum2+=p[i];
                    if(sum1<=r&&r<sum2)
                    {
                        banTable.add(utils.getIdx(tX,tY,row));
                        tX=xarray[i];
                        tY=yarray[i];
                        current_pos =utils.getIdx(tX,tY,row);
                        population.add(current_pos);
                        break;
                    }
                }
            }
            //到达目标点，加入该个体
            if(current_pos==goal) {
                populations.add(population);
                banTable.clear();
            }
        }
        return populations;

    }
    public static void main(String argsp[])
    {
        int[][] map = {
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,0,0,1,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1},
                {1,0,0,0,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1},
                {1,0,0,0,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1},
                {1,0,0,0,1,1,0,0,0,1,0,0,0,0,1,1,1,1,1,1},
                {1,0,0,0,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,0,0,1,0,0,0,0,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,0,0,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,1},
                {1,1,1,1,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,1},
                {1,1,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,1},
                {1,1,0,0,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,0,0,0,1,0,0,1,1,1,1,1,0,0,1},
                {1,1,1,1,1,1,1,1,1,1,0,0,1,1,0,1,1,0,0,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}};
        ImprovedGA ga = new ImprovedGA(20,20,map);
        ga.init(0,0,19,19,100);
        ArrayList<ArrayList<Integer>> l =ga.getFirstPopulation();
        for(int i=0;i<l.size();i++)
        {
            ArrayList<Integer>a = l.get(i);
            for(int j=0;j<a.size();j++)
            {

                System.out.print(a.get(j));
                System.out.print(" ");
            }
            System.out.println();
        }


    }

}
