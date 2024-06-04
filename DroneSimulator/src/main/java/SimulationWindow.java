import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class SimulationWindow {

	private JFrame frame;
	public static JLabel info_label;
	public static JLabel info_label3, info_label4;
	public static boolean return_home = false;
	public static AutoAlgo1 algo1;
	public JLabel info_label2;
	public static boolean toogleRealMap = true;
	public static boolean toogleAI = false;
	boolean toogleStop = true;


	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				SimulationWindow window = new SimulationWindow();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public SimulationWindow() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setSize(1600, 900);
		frame.setTitle("Drone Simulator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

		// Creating buttons and other components
		JButton stopBtn = new JButton("Start/Pause");
		JButton toogleMapBtn = new JButton("Toggle Map");
		JButton toogleAIBtn = new JButton("Toggle AI");
		JButton graphBtn = new JButton("Open Graph");

		info_label = new JLabel();
		info_label2 = new JLabel();
		info_label3 = new JLabel();
		info_label4 = new JLabel();

		// Setting up action listeners for buttons
		stopBtn.addActionListener(e -> {
			if (toogleStop) {
				CPU.stopAllCPUS();
			} else {
				CPU.resumeAllCPUS();
			}
			toogleStop = !toogleStop;
		});

		toogleMapBtn.addActionListener(e -> toogleRealMap = !toogleRealMap);
		toogleAIBtn.addActionListener(e -> toogleAI = !toogleAI);
		graphBtn.addActionListener(e -> algo1.mGraph.drawGraph());

		// Setting the maximum size for all buttons to ensure they are the same size
		Dimension buttonSize = new Dimension(200, 30);
		stopBtn.setMaximumSize(buttonSize);
		toogleMapBtn.setMaximumSize(buttonSize);
		toogleAIBtn.setMaximumSize(buttonSize);
		graphBtn.setMaximumSize(buttonSize);

		// Adding buttons to the panel
		buttonPanel.add(stopBtn);
		buttonPanel.add(toogleMapBtn);
		buttonPanel.add(toogleAIBtn);
		buttonPanel.add(graphBtn);
		buttonPanel.add(info_label);
		buttonPanel.add(info_label2);
		buttonPanel.add(info_label3);
		buttonPanel.add(info_label4);

		// Adding button panel to the left side of the frame
		frame.getContentPane().add(buttonPanel, BorderLayout.WEST);

		// Main simulation initialization
		main();
	}


	public void main() {
		int map_num = 1;
		Point[] startPoints = {
				new Point(100,50),
				new Point(50,60),
				new Point(73,68),
				new Point(84,73),
				new Point(92,100)};

		Map map = new Map("C:\\Users\\user\\Desktop\\DroneSimulator\\Maps\\p1" + map_num + ".png",startPoints[map_num-1]);

		algo1 = new AutoAlgo1(map);


		Painter painter = new Painter(algo1);
		painter.setPreferredSize(new Dimension(2000, 2000));
		frame.getContentPane().add(painter, BorderLayout.CENTER);

		CPU painterCPU = new CPU(200,"painter"); // 60 FPS painter
		painterCPU.addFunction(frame::repaint);
		painterCPU.play();

		algo1.play();

		CPU updatesCPU = new CPU(60,"updates");
		updatesCPU.addFunction(algo1.drone::update);
		updatesCPU.play();

		CPU infoCPU = new CPU(6,"update_info");
		infoCPU.addFunction(this::updateInfo);
		infoCPU.play();
	}

	public void updateInfo(int deltaTime) {

		info_label.setText(algo1.drone.getInfoHTML());
		info_label3.setText(algo1.sensor_distance.getInfoSensorData());
		//info_label4.setText(algo1.getInfo());
		info_label2.setText("<html>" + " <BR><span style='color: red;'>isRisky:" + String.valueOf(algo1.is_risky) +
				"</span><BR>"  + "</html>");


	}

	public void stopCPUS() {
		CPU.stopAllCPUS();
	}

	public void resumseCPUS() {
		CPU.stopAllCPUS();
	}
}
