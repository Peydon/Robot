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
    private HashSet<Integer> banTable;



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

    public void evolve()
    {
        List populations=new ArrayList<ArrayList<Integer>>();
        int tX = this.sX;
        int tY = this.sY;
        //生成初始种群个数的序列
        while(populations.size()<this.population_num)
        {

        }

    }

    public void getRouteLength()
    {

    }

    public void getBestRouteMap()
    {

    }

    public void getFirstPopulation()
    {

    }

}
