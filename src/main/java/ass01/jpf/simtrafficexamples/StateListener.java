package ass01.jpf.simtrafficexamples;

import ass01.jpf.simengineseq.AbstractAgent;
import ass01.jpf.simengineseq.AbstractEnvironment;
import ass01.jpf.simengineseq.SimulationListener;

import java.util.List;

public class StateListener implements SimulationListener {


    RoadSimView view;

    public StateListener(RoadSimView view) {
        super();
        this.view = view;
    }

    @Override
    public void notifyInit(int t, List<AbstractAgent> agents, AbstractEnvironment env) { }

    @Override
    public void notifyStepDone(int t, List<AbstractAgent> agents, AbstractEnvironment env) {
        view.notifyStepDone(t, agents, env);
    }

    public void notifyStateChanged(String message) {
        view.changeState(message);
    }
}
