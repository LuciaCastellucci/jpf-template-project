package ass01.jpf.simtrafficbase;

public class TrafficLightInfo {

    TrafficLight sem;
    Road road;
    double roadPos;
    public TrafficLightInfo(TrafficLight sem, Road road, double roadPos) {
        this.sem = sem;
        this.road = road;
        this.roadPos = roadPos;
    }

    public TrafficLight getSem() {
        return sem;
    }

    public void setSem(TrafficLight sem) {
        this.sem = sem;
    }

    public Road getRoad() {
        return road;
    }

    public void setRoad(Road road) {
        this.road = road;
    }

    public double getRoadPos() {
        return roadPos;
    }

    public void setRoadPos(double roadPos) {
        this.roadPos = roadPos;
    }
}
