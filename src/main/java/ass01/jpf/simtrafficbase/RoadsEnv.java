package ass01.jpf.simtrafficbase;

import ass01.jpf.simengineseq.AbstractEnvironment;
import ass01.jpf.simengineseq.Action;
import ass01.jpf.simengineseq.Percept;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class RoadsEnv extends AbstractEnvironment {
		
	private static final int MIN_DIST_ALLOWED = 5;
	private static final int CAR_DETECTION_RANGE = 30;
	private static final int SEM_DETECTION_RANGE = 30;
	
	/* list of roads */
	private List<Road> roads;

	/* traffic lights */
	private List<TrafficLight> trafficLights;
	
	/* cars situated in the environment */	
	private HashMap<String, CarAgentInfo> registeredCars;

	private Lock mutex;

	public RoadsEnv() {
		super("traffic-env");
		registeredCars = new HashMap<>();	
		trafficLights = new ArrayList<>();
		roads = new ArrayList<>();
		mutex =  new ReentrantLock();
	}
	
	@Override
	public void init() {
		for (TrafficLight tl: trafficLights) {
			tl.init();
		}
	}
	
	@Override
	public void step(int dt) {
		for (TrafficLight tl: trafficLights) {
			tl.step(dt);
		}
	}
	
	public void registerNewCar(CarAgent car, Road road, double pos) {
		registeredCars.put(car.getId(), new CarAgentInfo(car, road, pos));
	}

	public Road createRoad(P2d p0, P2d p1) {
		Road r = new Road(p0, p1);
		this.roads.add(r);
		return r;
	}

	public TrafficLight createTrafficLight(P2d pos, TrafficLight.TrafficLightState initialState, int greenDuration, int yellowDuration, int redDuration) {
		TrafficLight tl = new TrafficLight(pos, initialState, greenDuration, yellowDuration, redDuration);
		this.trafficLights.add(tl);
		return tl;
	}

	@Override
	public Percept getCurrentPercepts(String agentId) {
		//try {
			//mutex.lock();
			CarAgentInfo carInfo = registeredCars.get(agentId);
			double pos = carInfo.getPos();
			Road road = carInfo.getRoad();
			Optional<CarAgentInfo> nearestCar = getNearestCarInFront(road,pos, CAR_DETECTION_RANGE);
			Optional<TrafficLightInfo> nearestSem = getNearestSemaphoreInFront(road,pos, SEM_DETECTION_RANGE);
			return new CarPercept(pos, nearestCar, nearestSem);
		//}
		//finally {
			//mutex.unlock();
		//}
	}

	private Optional<CarAgentInfo> getNearestCarInFront(Road road, double carPos, double range){
		return 
				registeredCars
				.entrySet()
				.stream()
				.map(el -> el.getValue())
				.filter((carInfo) -> carInfo.getRoad() == road)
				.filter((carInfo) -> {
					double dist = carInfo.getPos() - carPos;
					return dist > 0 && dist <= range;
				})
				.min((c1, c2) -> (int) Math.round(c1.getPos() - c2.getPos()));
	}

	private Optional<TrafficLightInfo> getNearestSemaphoreInFront(Road road, double carPos, double range){
		return 
				road.getTrafficLights()
				.stream()
				.filter((TrafficLightInfo tl) -> tl.getRoadPos() > carPos)
				.min((c1, c2) -> (int) Math.round(c1.getRoadPos() - c2.getRoadPos()));
	}
	
	
	@Override
	public void doAction(String agentId, Action act) {
		try {
			mutex.lock();
			if (act instanceof Action) {
				MoveForward mv = (MoveForward) act;
				CarAgentInfo info = registeredCars.get(agentId);
				Road road = info.getRoad();
				Optional<CarAgentInfo> nearestCar = getNearestCarInFront(road, info.getPos(), CAR_DETECTION_RANGE);

				if (nearestCar.isPresent()) {
					double dist = nearestCar.get().getPos() - info.getPos();
					if (dist > mv.getDistance() + MIN_DIST_ALLOWED) {
						info.updatePos(info.getPos() + mv.getDistance());
					}
				} else {
					info.updatePos(info.getPos() + mv.getDistance());
				}

				if (info.getPos() > road.getLen()) {
					info.updatePos(0);
				}

			}
		} finally {
			mutex.unlock();
		}
	}
	
	
	public List<CarAgentInfo> getAgentInfo(){
		return this.registeredCars.entrySet().stream().map(el -> el.getValue()).collect(Collectors.toList());
	}

	public List<Road> getRoads(){
		return roads;
	}
	
	public List<TrafficLight> getTrafficLights(){
		return trafficLights;
	}
}
