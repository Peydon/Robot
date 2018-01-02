import simbad.sim.Agent;
import simbad.sim.LampActuator;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;

import javax.vecmath.Vector3d;

public class DynamicObstacle extends Agent {

    RangeSensorBelt sonars,bumpers;
    LampActuator lamp;
    double speed = 0.5;

    public DynamicObstacle(Vector3d position, String name) {
        super(position,name);
        bumpers = RobotFactory.addBumperBeltSensor(this);
        sonars = RobotFactory.addSonarBeltSensor(this,24);
        lamp = RobotFactory.addLamp(this);
    }

    public void initBehavior() {
        setTranslationalVelocity(speed);
    }

    public void performBehavior() {

        if (bumpers.oneHasHit()) {
            lamp.setBlink(true);
        }else
            lamp.setBlink(false);

        if(getCounter()%70 == 0){
            speed = -speed;
            setTranslationalVelocity(speed);
        }
    }
}