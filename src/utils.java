import java.util.ArrayList;

public class utils {

    public static double getDistance(int x1,int y1,int x2,int y2)
    {
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }
    public static int getIdx(int x,int y, int row)
    {
        return x+row*y;
    }
    //右 1 左 5 上3 下 7 右下 8  左下 6 左上 4 右上 2
    public static ArrayList<Integer> transform(ArrayList<Integer> route)
    {
        ArrayList<Integer> result = new ArrayList<>();
        int x1 = route.get(0)%20;
        int y1 = route.get(0)/20;
        for(int i =0;i<route.size();i++)
        {
            int x2 = route.get(i)%20;
            int y2 = route.get(i)/20;
            if(x2==x1&&y2==y1+1){
                result.add(1);
                System.out.print(1);
                System.out.print("右");
            }
            if(x2==x1&&y2==y1-1){result.add(5);System.out.print(5);System.out.print("左");}
            if(x2==x1-1&&y2==y1){result.add(3);System.out.print(3);System.out.print("上");}
            if(x2==x1+1&&y2==y1){result.add(7);System.out.print(7);System.out.print("下");}
            if(x2==x1+1&&y2==y1+1){result.add(8);System.out.print(8);System.out.print("右下");}
            if(x2==x1+1&&y2==y1-1){result.add(6);System.out.print(6);System.out.print("左下");}
            if(x2==x1-1&&y2==y1-1){result.add(4);System.out.print(4);System.out.print("左上");}
            if(x2==x1-1&&y2==y1+1){result.add(2);System.out.print(2);System.out.print("右上");}
            System.out.print(" ");
            x1=x2;
            y1=y2;
        }
        return result;
    }
}
