package ass01.jpf.simtrafficbase;

import ass01.jpf.simengineseq.Action;

/**
 * Car agent move forward action
 */
public class MoveForward implements Action {

    double distance;
    public MoveForward(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
