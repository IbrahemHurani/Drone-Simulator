import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class Drone {
	// Pitch control constants
	private static final double MAX_ANGLE = 10.0; // degrees for both pitch and roll

	// Existing fields
	private double gyroRotation;
	private Point sensorOpticalFlow;
	private Point pointFromStart;
	public Point startPoint;
	public List<Lidar> lidars;
	private String drone_img_path = "C:\\Users\\user\\Desktop\\DroneSimulator\\Maps\\drone_3_pixels.png";
	public Map realMap;
	private double rotation;
	private double speed;
	private CPU cpu;
	private long startTime;
	private static final int MAX_FLIGHT_TIME = 480;

	private double altitude;


	private double currentPitch = 0.0; // degrees
	private double currentRoll = 0.0; // degrees


	private VLD_PID pitchPID;
	private VLD_PID rollPID;


	public Drone(Map realMap) {
		this.realMap = realMap;
		this.startPoint = realMap.drone_start_point;
		pointFromStart = new Point();
		sensorOpticalFlow = new Point();
		lidars = new ArrayList<>();
		rotation = 0;
		gyroRotation = rotation;
		cpu = new CPU(100, "Drone");
		startTime = System.currentTimeMillis();

		// Initialize the pitch PID controller
		pitchPID = new VLD_PID(15.0, 0.04, 4.0, 0);
		rollPID = new VLD_PID(60.0, 60.0, 10.0, 0.5);

	}

	public void play() {
		cpu.play();
	}

	public void stop() {
		pitchPID.reset();
		rollPID.reset();
		cpu.stop();
	}

	public void addLidar(int degrees) {
		Lidar lidar = new Lidar(this, degrees);
		lidars.add(lidar);
		cpu.addFunction(lidar::getSimulationDistance);
	}

	public Point getPointOnMap() {
		double x = startPoint.x + pointFromStart.x;
		double y = startPoint.y + pointFromStart.y;
		return new Point(x, y);
	}

	public void update(int deltaTime) {
		double distancedMoved = (speed * 100) * ((double) deltaTime / 1000);
		pointFromStart = Tools.getPointByDistance(pointFromStart, rotation, distancedMoved);
		double noiseToDistance = Tools.noiseBetween(WorldParams.min_motion_accuracy, WorldParams.max_motion_accuracy, false);
		sensorOpticalFlow = Tools.getPointByDistance(sensorOpticalFlow, rotation, distancedMoved * noiseToDistance);
		double noiseToRotation = Tools.noiseBetween(WorldParams.min_rotation_accuracy, WorldParams.max_rotation_accuracy, false);
		double milli_per_minute = 60000;
		gyroRotation += (1 - noiseToRotation) * deltaTime / milli_per_minute;
		gyroRotation = formatRotation(gyroRotation);



	}

	public static double formatRotation(double rotationValue) {
		rotationValue %= 360;
		if (rotationValue < 0) {
			rotationValue = 360 - rotationValue;
		}
		return rotationValue;
	}

	public double getRotation() {
		return rotation;
	}

	public double getGyroRotation() {
		return gyroRotation;
	}

	public Point getOpticalSensorLocation() {
		return new Point(sensorOpticalFlow);
	}

	public void rotateLeft(int deltaTime) {
		double rotationChanged = WorldParams.rotation_per_second * deltaTime / 1000;
		rotation += rotationChanged;
		rotation = formatRotation(rotation);
		gyroRotation += rotationChanged;
		gyroRotation = formatRotation(gyroRotation);
		updateRoll(deltaTime);

	}

	public void rotateRight(int deltaTime) {
		double rotationChanged = -WorldParams.rotation_per_second * deltaTime / 1000;
		rotation += rotationChanged;
		rotation = formatRotation(rotation);
		gyroRotation += rotationChanged;
		gyroRotation = formatRotation(gyroRotation);
		updateRoll(deltaTime);

	}

	public void speedUp(int deltaTime) {
		updatePitch(deltaTime);
		speed += (WorldParams.accelerate_per_second * deltaTime / 1000);
		if (speed > WorldParams.max_speed) {
			speed = WorldParams.max_speed;
		}
	}

	public double getSpeed() {
		return speed;
	}

	public void slowDown(int deltaTime) {
		this.currentPitch=0.0;
		//Pitch_pid.reset();
		updatePitch(deltaTime);
		speed -= (WorldParams.accelerate_per_second * deltaTime / 1000);
		if (speed < 0) {
			speed = 0;
		}
	}

	boolean initPaint = false;
	BufferedImage mImage;
	int j = 0;

	public void paint(Graphics g) {
		if (!initPaint) {
			try {
				File f = new File(drone_img_path);
				mImage = ImageIO.read(f);
				initPaint = true;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		for (int i = 0; i < lidars.size(); i++) {
			Lidar lidar = lidars.get(i);
			lidar.paint(g);
		}
	}

	public int getElapsedTime() {
		long currentTime = System.currentTimeMillis();
		return (int) ((currentTime - startTime) / 1000);
	}

	public int getRemainingFlightTime() {
		int elapsedTime = getElapsedTime();
		return Math.max(MAX_FLIGHT_TIME - elapsedTime, 0);
	}

	public double getBatteryPercentage() {
		int remainingFlightTime = getRemainingFlightTime();
		return Math.max((remainingFlightTime / (double) MAX_FLIGHT_TIME) * 100.0, 0.0);
	}

	public boolean isBatteryEmpty() {
		int remainingFlightTime = getRemainingFlightTime();
		return remainingFlightTime == 0;
	}

	public boolean isBatteryAt50Percent() {
		int remainingFlightTime = getRemainingFlightTime();
		return remainingFlightTime <= (MAX_FLIGHT_TIME / 2);
	}


	public void updatePitch(int deltaTime) {
		double targetPitch = 10.0;
		double error = targetPitch - currentPitch;
		double dt = deltaTime / 1000.0;
		double pitchAdjustment = pitchPID.update(error, dt);

		currentPitch += pitchAdjustment * dt;
		currentPitch = constrain(currentPitch, MAX_ANGLE, -MAX_ANGLE);
	}
	public void updateRoll(int deltaTime) {
		double targetRoll = 10.0;
		double error = targetRoll - currentRoll;
		double dt = deltaTime / 1000.0;
		double rollAdjustment = rollPID.update(error, dt);

		currentRoll += rollAdjustment * dt;
		currentRoll = constrain(currentRoll, MAX_ANGLE, -MAX_ANGLE);
	}

	private double constrain(double val, double max, double min) {
		if (val > max) return max;
		if (val < min) return min;
		return val;
	}
	public void Takeoff(){
		this.altitude=1.0;
	}
	public void Land(){
		this.altitude=0.0;
	}
	public double getYaw(){
		return this.gyroRotation;
	}
	public String getInfoHTML() {
		DecimalFormat df = new DecimalFormat("#.####");
		String info = "<html>";
		//info += "Location: " + pointFromStart + "<br>";
		info += "<span style='color: blue;'>Pitch : " + this.currentPitch +"&deg;</span><br>";
		info += "<span style ='color: blue;'>Roll : " + this.currentRoll +"&deg;</span><br>";
		info += "<span style='color: blue;'>Yaw:" + df.format(gyroRotation) +"&deg;</span><br>";
		info+="<span style='color:blue;'> speed: "+df.format(speed)+"</span><br>";
		info+="<span style='color:blue;'> alttuide:"+df.format(altitude)+"</span><br>";
		info += "<span style='color: red;'>Battery: " + df.format(getBatteryPercentage()) + "%</span><br>";
		info += "</html>";
		return info;
	}
}
