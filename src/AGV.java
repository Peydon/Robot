import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import simbad.sim.Agent;
import simbad.sim.LampActuator;

import simbad.sim.RobotFactory;

import java.util.ArrayList;

public class AGV extends Agent {
    private ArrayList<Integer> d;
    private int x;
    private int y;
    private int eX;
    private int eY;
    private int row;
    private int column;
    private Vector3d p;   //初始位置
    private int direction = 1;  //移动方向  ,八个方向
    private int next;
    private int i =0;
    LampActuator lamp;

    public AGV(Vector3d position, String name, int column, int row, ArrayList<Integer>route, int eX, int eY) {
        super(position, name);
        this.d = utils.transform(route);
        this.row = row;
        this.column = column;
        //得到当前的坐标
        x = (int)position.z+column/2;
        y = (int)position.x+row/2;
        p = position;
        this.eX = eX;
        this.eY = eY;
        lamp = RobotFactory.addLamp(this);
    }



    //初始化
    public void initBehavior() {

        x = (int)p.z+column/2;
        y = (int)p.x+row/2;
        next=d.get(i);
        direction=1;
    }
    public boolean check(int x,int y){
        Point3d p= new Point3d();
        getCoords(p);
        double px= p.z+10;
        double py= p.x+10;

        if(Math.abs(px-x)<=0.001&&Math.abs(py-y)<=0.001){
            return true;
        }
        else{
            return false;
        }
    }


    public void performBehavior() {
        double angle = 0;

        if (getCounter() % 20 == 0){
            Point3d p= new Point3d();
            getCoords(p);
            double px=p.z+10.0;
            double py=p.x+10.0;


            if(Math.abs(px-eX)<=0.00001 && Math.abs(py-eY)<=0.00001){
                lamp.setOn(true);
                setRotationalVelocity(0);
                setTranslationalVelocity(0);
                System.out.println((p.z+10)+" "+(p.x+10)+ "    "+y+ " "+x);
                return;
            }
            lamp.setOn(false);


            //System.out.println((p.z+10)+" "+(p.x+10)+ "    "+y+ " "+x);
            //向右移动一个单位
            if(next==1){
                angle = getRotationalAngle(1);
                if(!check(x,y+1)){
                    if(angle==0){
                        //平移
                        setRotationalVelocity(0);
                        setTranslationalVelocity(1);

                    }else{
                        //转动指定角度
                        setTranslationalVelocity(0);
                        setRotationalVelocity(angle);
                    }
                    return;
                }
                else{
                    setTranslationalVelocity(0);
                    next=d.get(++i);
                    y++;
                }
            }
            //向左移动一个单位
            if(next==5){
                angle = getRotationalAngle(5);
                if(!check(x,y-1)){
                    if(angle==0){
                        //平移


                        setRotationalVelocity(0);
                        setTranslationalVelocity(1);

                    }else{
                        //转动指定角度
                        setTranslationalVelocity(0);
                        setRotationalVelocity(angle);
                    }
                    return;
                }
                else{
                    setTranslationalVelocity(0);
                    next=d.get(++i);
                    y--;
                }
            }
            //向上移动一个单位
            if(next==3){
                angle = getRotationalAngle(3);
                if(!check(x-1,y)){
                    if(angle==0){
                        //平移


                        setRotationalVelocity(0);
                        setTranslationalVelocity(1);

                    }else{
                        //转动指定角度
                        setTranslationalVelocity(0);
                        setRotationalVelocity(angle);
                    }
                    return;
                }
                else{
                    setTranslationalVelocity(0);
                    next=d.get(++i);
                    x--;
                }
            }
            //向下移动一个单位
            if(next==7){
                angle = getRotationalAngle(7);
                if(!check(x+1,y)){
                    if(angle==0){
                        //平移
                        setRotationalVelocity(0);
                        setTranslationalVelocity(1);

                    }else{
                        //转动指定角度
                        setTranslationalVelocity(0);
                        setRotationalVelocity(angle);
                    }
                    return;
                }
                else{
                    setTranslationalVelocity(0);
                    next=d.get(++i);
                    x++;
                }
            }


            //右下
            if(next==8){
                angle = getRotationalAngle(8);
                if(!check(x+1,y+1)){
                    if(angle==0){
                        //平移
                        setRotationalVelocity(0);
                        setTranslationalVelocity(Math.sqrt(2.0));

                    }else{
                        //转动指定角度
                        setTranslationalVelocity(0);
                        setRotationalVelocity(angle);
                    }
                    return;
                }

                else{
                    setTranslationalVelocity(0);
                    next=d.get(++i);
                    x++;
                    y++;
                }
            }
            //左下
            if(next==6){
                angle = getRotationalAngle(6);
                if(!check(x+1,y-1)){
                    if(angle==0){
                        //平移
                        setRotationalVelocity(0);
                        setTranslationalVelocity(Math.sqrt(2.0));

                    }else{
                        //转动指定角度
                        setTranslationalVelocity(0);
                        setRotationalVelocity(angle);
                    }
                    return;
                }
                else{
                    setTranslationalVelocity(0);
                    next=d.get(++i);
                    y--;
                    x++;
                }
            }

            //左上
            if(next==4){
                angle = getRotationalAngle(4);
                if(!check(x-1,y-1)){
                    if(angle==0){
                        //平移
                        setRotationalVelocity(0);
                        setTranslationalVelocity(Math.sqrt(2.0));

                    }else{
                        //转动指定角度
                        setTranslationalVelocity(0);
                        setRotationalVelocity(angle);
                    }
                    return;
                }
                else{
                    setTranslationalVelocity(0);
                    next=d.get(++i);
                    x--;
                    y--;
                }
            }
            //右上
            if(next==2){
                angle = getRotationalAngle(2);
                if(!check(x-1,y+1)){
                    if(angle==0){
                        //平移

                        setRotationalVelocity(0);
                        setTranslationalVelocity(Math.sqrt(2.0));

                    }else{
                        //转动指定角度
                        setTranslationalVelocity(0);
                        setRotationalVelocity(angle);
                    }
                    return;
                }
                else{
                    setTranslationalVelocity(0);
                    next=d.get(++i);
                    y++;
                    x--;
                }
            }


        }
    }

    private double getRotationalAngle(int d){
        int result = direction - d;
        direction=d;
        //左转90度
        if(result == -2 || result == 6){
            return Math.PI/2;
        }
        //右转90度
        if(result == 2 || result == -6){
            return -Math.PI/2;
        }
        //左转45度
        if(result == -1 || result == 7){
            return Math.PI/4;
        }
        //右转45度
        if(result == 1 || result == -7){
            return -Math.PI/4;
        }
        //左转135度
        if(result == -3 || result == 5){
            return 5*Math.PI/4;
        }
        //右转135度
        if(result == 3 || result ==-5){
            return 5*Math.PI/4;
        }

        if(result == 0){
            return 0;
        }
        //转180度
        return Math.PI;
    }
}
