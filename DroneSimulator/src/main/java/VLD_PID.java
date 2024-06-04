
public class VLD_PID {
    private double kp,ki ,kd,d,integral,last_error;
    private boolean first_run;
    public VLD_PID(double p, double i, double d, double max_i) {
        this.kp = p;
        this.ki= i;
        this.kd = d;
        this.d = max_i;
        first_run=true;
    }
    public double update(double error,double dt){
        if(first_run){
            last_error = error; first_run = false; }
        integral += ki*error*dt;
        double diff = (error-last_error)/dt;
        double const_integral = constrain(integral,d,-d);
        double control_out = kp*error + kd*diff + const_integral;
        last_error = error;
        return control_out;
    }

    private double constrain(double val, double max,double min){
        if(val > max) return max;
        if(val < min) return min;
        return val;
    }

    public void reset(){
        integral = 0;
        first_run = true;
    }
}
