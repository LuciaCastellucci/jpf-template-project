package ass01.jpf.simtrafficbase;

import ass01.jpf.simengineseq.Percept;

import java.util.Optional;

/**
 * 
 * Percept for Car Agents
 * 
 * - position on the road
 * - nearest car, if present (distance)
 * - nearest semaphore, if presente (distance)
 * 
 */
public class CarPercept implements Percept {
    double roadPos;
    Optional<CarAgentInfo> nearestCarInFront;
    Optional<TrafficLightInfo> nearestSem;

    public CarPercept (double roadPos, Optional<CarAgentInfo> nearestCarInFront, Optional<TrafficLightInfo> nearestSem) {
        this.roadPos = roadPos;
        this.nearestCarInFront = nearestCarInFront;
        this.nearestSem = nearestSem;
    }

    public double getRoadPos() {
        return roadPos;
    }

    public void setRoadPos(double roadPos) {
        this.roadPos = roadPos;
    }

    public Optional<CarAgentInfo> getNearestCarInFront() {
        return nearestCarInFront;
    }

    public void setNearestCarInFront(Optional<CarAgentInfo> nearestCarInFront) {
        this.nearestCarInFront = nearestCarInFront;
    }

    public Optional<TrafficLightInfo> getNearestSem() {
        return nearestSem;
    }

    public void setNearestSem(Optional<TrafficLightInfo> nearestSem) {
        this.nearestSem = nearestSem;
    }
}