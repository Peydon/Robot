import javax.swing.plaf.synth.SynthLookAndFeel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class ImprovedGA {

    //栅格信息
    private int [][] map;//栅格化地图
    private int row;//行
    private int column;//列
    private int start;//起点坐标的方格编号
    private int goal;//终点坐标的方格编号

    //遗传算法参数
    private double cross_prob = 0.7;
    private double mutate_prob = 0.1;
    private double population_num = 30;
    private int epochs = 100;
    private int t = 1;
    public ArrayList<Integer> best_unit=new ArrayList<>();


    //种群信息
    private HashMap<ArrayList<Integer>,Integer> pl_info = new HashMap<>();

    public ImprovedGA(int row,int column, int [][]map)
    {
        this.row=row;
        this.column=column;
        this.map=map;
    }

    public void init(int sX,int sY ,int eX, int eY , int max_epochs)
    {
        this.epochs=max_epochs;
        start= sX+sY*this.row;
        goal = eX+eY*this.row;
    }
    //进化过程
    public void evolve()
    {

        if(start==goal){}
        //获得初始种群
        ArrayList<ArrayList<Integer>> old_population = getFirstPopulation();
        ArrayList<ArrayList<Integer>> new_population;
        //进化
        while(t<epochs)
        {
            new_population=select(old_population);
            new_population=cross(new_population);
            new_population=mutate(new_population);
            old_population=new_population;
            t++;
        }

    }

    public void deleteNode()
    {
        ArrayList<ArrayList<Integer>> s = new ArrayList<>();
        for(int i=0;i<best_unit.size();i++)
        {
            s.add(new ArrayList<>());
            for(int j=i+1;j<best_unit.size();j++)
            {
                if(isContinuous(best_unit.get(i),best_unit.get(j)))
                {
                    s.get(i).add(j);
                }

            }
        }
        int current = 0;
        do{
            drawMap(current);
            current = s.get(current).get(s.get(current).size()-1);
        }while(current==best_unit.size()-1);

    }

    //返回路径信息
    public ArrayList<Integer> getBest(){
        return best_unit;
    }
    private void drawMap(int idx)
    {
        int x=idx%row;
        int y=idx/row;
        map[x][y]=2;
    }


    //适应度函数
    private double F(ArrayList<Integer> unit)
    {
        //空路径代表是初始值
        if(unit.size()==0)return 0;
        double d=0.0;
        int sX=unit.get(0)%row;
        int sY = unit.get(0)/row;
        for(int i =1;i<unit.size();i++)
        {
            int eX = unit.get(i)%row;
            int eY = unit.get(i)/row;
            d+=utils.getDistance(sX,sY,eX,eY);
        }
        return 1/d;
    }
    //判断两个点是否连续
    private boolean isContinuous(int idx1,int idx2)
    {
        int x1 = idx1%row;
        int x2 = idx2%row;
        int y1 = idx1/row;
        int y2 = idx2%row;
        return (Math.abs(x1-x2)<=1&&Math.abs(y1-y2)<=1);
    }
    //查找交叉点
    private int[] findCrossPoint(ArrayList<Integer>p1,ArrayList<Integer>p2)
    {
        int[] result = new int[2];
        //默认相同点为起点，即不交叉
        result[0] =0;
        result[1] =0;
        for(int i =1;i<p1.size()-1;i++)
            for(int j=1;j<p2.size()-1;j++)
            {
                if(p1.get(i).intValue()==p2.get(j).intValue())
                {
                    result[0]=i;
                    result[1]=j;
                }
            }
        return result;
    }

    //生成一条两个点之间的路径，采用启发式函数优化
    private ArrayList<Integer> findPath(int start,int end)
    {
        ArrayList<Integer> path =new ArrayList<>();
        HashSet<Integer> banTable=new HashSet<>();
        int tX = start%row;
        int tY = start/row;
        int eX = end%row;
        int eY = end/row;
        int current_pos = start;
        while(current_pos!=goal)
        {
            path.add(current_pos);
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
            //八个点的概率
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
                            //分母为零时取一个很小的数
                            if(d[i]==0)d[i]=0.000000001;
                            p[i] = 1/d[i];
                            D_Sum += 1/d[i];
                        }
                    }
                }
            }
            //没有自由栅格，放弃这条路，回到起点再开始
            if(D_Sum==0.0)
            {
                current_pos = start;
                tX = start%row;
                tY = start/row;
                path.clear();
                banTable.clear();
                continue;
            }
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
                    break;
                }
            }
        }
        path.add(end);
        return path;
    }
    //选择操作，轮盘赌
    private ArrayList<ArrayList<Integer>> select(ArrayList<ArrayList<Integer>>old_pl)
    {
        ArrayList<ArrayList<Integer>> new_pl = new ArrayList<>();
        double F_sum=0;
        double p[]=new double[old_pl.size()];
        //累计概率
        for(int i=0;i<old_pl.size();i++)
        {
            p[i]=F(old_pl.get(i));
            //记录最佳适应度的个体
            if(p[i]>F(best_unit))best_unit= old_pl.get(i);
            F_sum+=p[i];
        }
        //计算每个的概率
        for(int i=0;i<old_pl.size();i++)
        {
            p[i]/=F_sum;
        }
        //选择
        double sum1,sum2=0.0;
        for(int i=0;i<old_pl.size();i++)
        {
            double r =Math.random();
            for(int j=0;j<old_pl.size();j++) {
                sum1 = sum2;
                sum2 += p[j];
                if (sum1 <= r && r < sum2) {
                    new_pl.add(old_pl.get(i));
                }
            }
        }
        return new_pl;
    }

    //交叉操作
    private ArrayList<ArrayList<Integer>> cross(ArrayList<ArrayList<Integer>>old_pl)
    {
        ArrayList<ArrayList<Integer>> new_pl = new ArrayList<>();
        int num=old_pl.size();
        for(int i=1;i<num;i+=2)
        {
            double r = Math.random();
            ArrayList<Integer> p1=old_pl.get(i-1);
            ArrayList<Integer> p2=old_pl.get(i);
            //小于交叉概率，不交叉直接加入
            if(r<cross_prob)
            {
                new_pl.add(p1);
                new_pl.add(p2);
                continue;
            }
            int[] result = findCrossPoint(p1,p2);
            ArrayList<Integer> child1 = new ArrayList<>();
            ArrayList<Integer> child2 = new ArrayList<>();
            child1.addAll(p1.subList(0,result[0]+1));
            child2.addAll(p2.subList(0,result[1]+1));
            child1.addAll(p2.subList(result[1]+1,p2.size()));
            child2.addAll(p1.subList(result[0]+1,p1.size()));
            new_pl.add(child1);
            new_pl.add(child2);
//            //若无相同序号，检查是否是连续路径，否则在断点处补充至连续
//            if(result[3]==1)
//            {
//                if(!isContinuous(child1.get(result[0]),child1.get(result[0]+1)))
//                {
//
//                }
//                if(!isContinuous(child1.get(result[1]),child1.get(result[1]+1)))
//                {
//
//                }
//            }
        }
        //剩下的直接加入
        if(num%2==1)new_pl.add(old_pl.get(num-1));
        return new_pl;
    }

    //变异操作
    private ArrayList<ArrayList<Integer>> mutate(ArrayList<ArrayList<Integer>>old_pl)
    {
        ArrayList<ArrayList<Integer>> new_pl = new ArrayList<>();
        for(int i=0;i<old_pl.size();i++)
        {
            double r = Math.random();
            ArrayList<Integer> unit = old_pl.get(i);
            int len = unit.size();
            if(len>2&&r>=mutate_prob){
                int pos =new Random().nextInt(unit.size()-2)+1;
                ArrayList<Integer> path = findPath(unit.get(pos-1),unit.get(pos+1));
                unit.remove(pos);
                if(path.size()>2){
                    unit.addAll(pos,path.subList(1,path.size()-1));
                }

            }
            new_pl.add(unit);
        }
        return new_pl;
    }
    //初代种群生成
    private ArrayList<ArrayList<Integer>> getFirstPopulation()
    {
        //初始种群
        ArrayList<ArrayList<Integer>> population=new ArrayList<>();
        //生成初始种群
        while(population.size()<this.population_num)
        {
            population.add(findPath(this.start,this.goal));
        }
        return population;
    }
//    public static void main(String argsp[])
//    {
//        int[][] map = {
//                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
//                {1,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
//                {1,0,0,1,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1},
//                {1,1,1,1,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1},
//                {1,1,1,1,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1},
//                {1,0,0,0,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1},
//                {1,0,0,0,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1},
//                {1,0,0,0,1,1,0,0,0,1,0,0,0,0,1,1,1,1,1,1},
//                {1,0,0,0,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1},
//                {1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1},
//                {1,1,1,1,1,1,1,0,0,1,0,0,0,0,1,1,1,1,1,1},
//                {1,1,1,1,1,1,1,0,0,1,1,1,1,1,1,1,1,1,1,1},
//                {1,1,1,1,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,1},
//                {1,1,1,1,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,1},
//                {1,1,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,1},
//                {1,1,0,0,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1},
//                {1,1,1,1,1,1,0,0,0,1,0,0,1,1,1,1,1,0,0,1},
//                {1,1,1,1,1,1,1,1,1,1,0,0,1,1,0,1,1,0,0,1},
//                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1},
//                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}};
//        ImprovedGA ga = new ImprovedGA(20,20,map);
//        ga.init(0,0,14,9,100);
//        ga.evolve();
//        ga.getBestRouteMap();
//        for(int i=0;i<ga.best_unit.size();i++)
//        {
//            System.out.print(ga.best_unit.get(i));
//            System.out.print(" ");
//        }
//
////        ArrayList<ArrayList<Integer>> l =ga.getFirstPopulation();
////        for(int i=0;i<l.size();i++)
////        {
////            ArrayList<Integer>a = l.get(i);
////            HashSet<Integer> table = new HashSet<>();
////            for(int j=0;j<a.size();j++)
////            {
////                if(table.contains(a.get(j)))System.out.print(1);
//////                System.out.print(a.get(j));
//////                System.out.print(" ");
////            }
//////            System.out.println();
////        }
////        ArrayList<Integer> a = new ArrayList<>();
////        ArrayList<Integer> b = new ArrayList<>();
////        b.add(1);
////        b.add(2);
////        b.add(3);
////        b.add(4);
////        b.add(5);
////        a.add(1);
////        a.add(6);
////        a.add(5);
////        a.remove(1);
////
////        a.addAll(1,b.subList(1,b.size()-1));
////        for(int i=0;i<a.size();i++)
////        {
////            System.out.println(a.get(i));
////        }
//
//
//   }

}
