import  simbad.sim.*;
import javax.vecmath.Vector3d;

public class AGV extends Agent {

    //传感器
    RangeSensorBelt sonars,bumpers;
    LampActuator lamp;
    //起始位置
    private Vector3d origin = null;

    public AGV(Vector3d position, String name) {
        super(position,name);
        bumpers = RobotFactory.addBumperBeltSensor(this);
        sonars = RobotFactory.addSonarBeltSensor(this);
        lamp = RobotFactory.addLamp(this);
        origin = position;// 起点位置
    }
    public void initBehavior() {}

    public void performBehavior() {
        if (collisionDetected()) {
            // stop the robot
            setTranslationalVelocity(0.0);
            setRotationalVelocity(0);
        } else {
            // progress at 0.5 m/s
            setTranslationalVelocity(0.5);
            // frequently change orientation 
            if ((getCounter() % 100)==0)
                setRotationalVelocity(Math.PI/2 * (0.5 - Math.random()));
        }
    }


}