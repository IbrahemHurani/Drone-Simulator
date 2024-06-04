import java.text.DecimalFormat;

public class DataSensor {
    private Drone drone;
    private  Map realmap;
    DataSensor(Drone drone1){
        drone=drone1;

        
    }
    public double Forward_distance(){
        return drone.lidars.get(0).current_distance;
    }
    public double Right_distance(){
        return drone.lidars.get(1).current_distance;
    }
    public double Left_distance(){
        return drone.lidars.get(2).current_distance;
    }
    public double Backward_distance(){
        return drone.lidars.get(3).current_distance;
    }
    public String getInfoSensorData() {
        DecimalFormat df = new DecimalFormat("#.####");
        String info = "<html>";
        info += "S-forward:" + df.format(Forward_distance()) + "<br>";
        info += "S-Right:" + df.format(Right_distance()) + "<br>";
        info += "S-Left:" + df.format(Left_distance()) + "<br>";
        info += "S-Back:" + df.format(Backward_distance()) + "<br>";
        info += "</html>";
        return info;
    }
}
