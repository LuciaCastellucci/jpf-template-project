package ass01.jpf.simtrafficexamples;

import ass01.jpf.simengineseq.AbstractAgent;
import ass01.jpf.simengineseq.AbstractEnvironment;
import ass01.jpf.simtrafficbase.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.Semaphore;

public class RoadSimView extends JFrame implements ActionListener {

	private RoadSimViewPanel panel;
	private JButton startButton;
	private JButton stopButton;
	private JTextField steps;
	private JTextField state;

	private ViewListener listener;

	private static final int CAR_DRAW_SIZE = 10;
	
	public RoadSimView() {
		super("RoadSim View");
		setSize(1500,600);

		steps = new JTextField(5);
		steps.setText("0");

		startButton = new JButton("start");
		stopButton = new JButton("stop");
		stopButton.setEnabled(false);
			
		panel = new RoadSimViewPanel(1500,600); 
		panel.setSize(1500, 600);

		JPanel cp = new JPanel();

		JPanel controlPanel = new JPanel();
		controlPanel.add(new JLabel("steps: "));
		controlPanel.add(steps);
		controlPanel.add(startButton);
		controlPanel.add(stopButton);

		JPanel infoPanel = new JPanel();
		state = new JTextField(20);
		state.setText("Ready to start");
		state.setEditable(false);
		infoPanel.add(new JLabel("State"));
		infoPanel.add(state);

		LayoutManager layout = new BorderLayout();
		cp.setLayout(layout);
		cp.add(BorderLayout.NORTH,controlPanel);
		cp.add(BorderLayout.CENTER,panel);
		cp.add(BorderLayout.SOUTH, infoPanel);
		setContentPane(cp);

		startButton.addActionListener(this);
		stopButton.addActionListener(this);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void display() {
		SwingUtilities.invokeLater(() -> {
			this.setVisible(true);
		});
	}

	public void notifyStepDone(int t, List<AbstractAgent> agents, AbstractEnvironment env) {
		RoadsEnv e = ((RoadsEnv) env);
		panel.update(e.getRoads(), e.getAgentInfo(), e.getTrafficLights());
	}

	public void setViewListener(ViewListener l){
		listener = l;
	}

	private void notifyStarted(int nSteps){
		stopButton.setEnabled(true);
		startButton.setEnabled(false);
		listener.started(nSteps);
	}

	private void notifyStopped(){
		stopButton.setEnabled(false);
		listener.stopped();
	}

	public void actionPerformed(ActionEvent ev){
		String cmd = ev.getActionCommand();
		if (cmd.equals("start")){
			int nSteps = Integer.parseInt(steps.getText());
			notifyStarted(nSteps);
		} else if (cmd.equals("stop")){
			notifyStopped();
		}
	}

	public void changeState(final String s){
		SwingUtilities.invokeLater(() -> {
			state.setText(s);
		});;
	}
	
	class RoadSimViewPanel extends JPanel {
		
		List<CarAgentInfo> cars;
		List<Road> roads;
		List<TrafficLight> sems;
		
		public RoadSimViewPanel(int w, int h){
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);   
	        Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2.clearRect(0,0,this.getWidth(),this.getHeight());
			
			if (roads != null) {
				for (Road r: roads) {
					g2.drawLine((int)r.getFrom().getX(), (int)r.getFrom().getY(), (int)r.getTo().getX(), (int)r.getTo().getY());
				}
			}
			
			if (sems != null) {
				for (TrafficLight s: sems) {
					if (s.isGreen()) {
						g.setColor(new Color(0, 255, 0, 255));
					} else if (s.isRed()) {
						g.setColor(new Color(255, 0, 0, 255));
					} else {
						g.setColor(new Color(255, 255, 0, 255));
					}
					g2.fillRect((int)(s.getPos().getX()-5), (int)(s.getPos().getY()-5), 10, 10);
				}
			}
			
			g.setColor(new Color(0, 0, 0, 255));

			if (cars != null) {
				for (CarAgentInfo c: cars) {
					double pos = c.getPos();
					Road r = c.getRoad();
					V2d dir = V2d.makeV2d(r.getFrom(), r.getTo()).getNormalized().mul(pos);
					g2.drawOval((int)(r.getFrom().getX() + dir.getX() - CAR_DRAW_SIZE/2), (int)(r.getFrom().getY() + dir.getY() - CAR_DRAW_SIZE/2), CAR_DRAW_SIZE , CAR_DRAW_SIZE);
				}
			}
  	   }
	
	   public void update(List<Road> roads, 
			   			  List<CarAgentInfo> cars,
			   			List<TrafficLight> sems) {
		   this.roads = roads;
		   this.cars = cars;
		   this.sems = sems;
		   repaint();
	   }
	}
}
