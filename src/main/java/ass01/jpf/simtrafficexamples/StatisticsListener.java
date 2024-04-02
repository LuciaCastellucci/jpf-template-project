package ass01.jpf.simtrafficexamples;

import ass01.jpf.simengineseq.AbstractAgent;
import ass01.jpf.simengineseq.AbstractEnvironment;
import ass01.jpf.simengineseq.SimulationListener;
import ass01.jpf.simtrafficbase.CarAgent;

import java.util.List;

/**
 * Simple class keeping track of some statistics about a traffic simulation
 * - average number of cars
 * - min speed
 * - max speed
 */
public class StatisticsListener implements SimulationListener {

	
	private double averageSpeed;
	private double minSpeed;
	private double maxSpeed;
	
	public StatisticsListener() {
	}
	
	@Override
	public void notifyInit(int t, List<AbstractAgent> agents, AbstractEnvironment env) {
		// TODO Auto-generated method stub
		// log("reset: " + t);
		averageSpeed = 0;
	}

	@Override
	public void notifyStepDone(int t, List<AbstractAgent> agents, AbstractEnvironment env) {
		double avSpeed = 0;
		
		maxSpeed = -1;
		minSpeed = Double.MAX_VALUE;
		for (AbstractAgent agent: agents) {
			CarAgent car = (CarAgent) agent;
			double currSpeed = car.getCurrentSpeed();
			avSpeed += currSpeed;			
			if (currSpeed > maxSpeed) {
				maxSpeed = currSpeed;
			} else if (currSpeed < minSpeed) {
				minSpeed = currSpeed;
			}
		}
		
		if (!agents.isEmpty()) {
			avSpeed /= agents.size();
		}
		log("average speed: " + avSpeed);
	}

	@Override
	public void notifyStateChanged(String message) {
		log("interrupted simulation");
	}

	public double getAverageSpeed() {
		return this.averageSpeed;
	}

	public double getMinSpeed() {
		return this.minSpeed;
	}
	
	public double getMaxSpeed() {
		return this.maxSpeed;
	}
	
	
	private void log(String msg) {
		System.out.println("[STAT] " + msg);
	}

}
