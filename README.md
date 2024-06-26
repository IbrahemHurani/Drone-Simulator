<h1 align="center"> Drone Simulator </h1>

<p align="center">
  <img src="drone.png" alt="Drone Image" />
</p>

## Overview

The main goal of this project is to develop a solution for a small drone to navigate indoor environments autonomously without collisions. The project involves a fully autonomous 2D drone simulator, aiming for realistic behavior using lidar sensors, a gyroscope(Yaw),Pitch,Roll,altitude, an optical flow sensor, and a speed sensor. Noise is added to sensor samples to enhance realism. The simulator includes a basic API for real-time information and manual control, as well as an area mapping feature.

**The project is classified into two parts:**

- **Part 1:** _Handling drone's sensor data._
  
  _Essentially in `DataSensor.java`._
  
- **Part 2:** _Implementing P.I.D controller for the drone._
  
  _Essentially in `VLD_PID.java`._
  
> The project is coded in **Java**.

## Classes - Brief & Insight

<h3 align="center"> DataSensor.java </h3>
<p align="center"> Handles the drone's sensor data by calculating its distance from a wall. </p>

------------------------------------------------------------------------------------------------------------------------------------------------

- **Attributes**: 
  - _`Drone drone`_
  - _`Map realmap`_
- **Functions**: 
  - _`DataSensor(Drone drone1)`_
  - _`double Forward_distance()`_
  - _`double Right_distance()`_
  - _`double Left_distance()`_
  - _`double Backward_distance()`_
  - _`String getInfoSensorData()`_

<h3 align="center"> VLD_PID.java </h3>
<p align="center"> Implements a drone's P.I.D Controller by calculating its roll and pitch. </p>

------------------------------------------------------------------------------------------------------------------------------------------------

- **Attributes**:
  - _`double kp`_
  - _`double ki`_
  - _`double kd`_
  - _`double d`_
  - _`double integral`_
  - _`double last_error`_
  - _`boolean first_run`_
- **Functions**:
  - _`VLD_PID(double p, double i, double d, double max_i)`_
  - _`double update(double error, double dt)`_
  - _`double constrain(double val, double max, double min)`_
  - _`void reset()`_

## Getting Started

When all files located inside eclipse or any other explorer we have the Maps folder which contains couple of maps with route and obstacles.
- Inside "SimulationWindow" in main we have map object with the path to any map you want to test.
- Inside "Drone" we have path to our image represents the drone itself.
After setting this up it is ready to launch.

## Sensors
- Lidar - check the distance between his spot forward and return the distance if hit, if not return 300 as max sample enabled.
In our project we set 4 lidars - one in front, second 90 degrees, third -90 degrees,180 degrees(Backward).
- Gyroscpoe(Yaw) - check the rotation of the drone. (0-360)
- Optical flow - check his location on map.
- Speed - max speed is 2m per second.
- Pitch
- Roll
- Altitude 

## Symbols 
- Yellow mark - mapped area.
- Black circle - his purpose to get some idea from where drone came and simply make some route that his passed.(for navigation)
- Red points - represents the wall point.
- Blue line - his whole route.

## API Description
The simple API includes buttons for:
- Start/Pause
- open Graph: To see the graph 
- Toggle Map: Hides the real map to enter "real-time" vision
- Toggle AI: Enables/Disables AI

## Map Rule
If you wish to add custom map it has to be black/white pixels- black is wall/obstacle, white is safe pass.

## update
- if the battery is empty the drone do landing
- add to class Drone two method Takeoff and Landing
- Directed Graph feature added. (JGrapht library required)

## Known bugs
- API might be in different place depends on the map.
- Sometimes drone might crash(hit the black pixels) specially in difficult obstacles.
- Sometimes may be indifferent parameters which causing some pixels override - solution is to re-run project.

## How Does The Simulator Look Like:
![Ex2 screenshot 1](https://github.com/IbrahemHurani/Drone-Simulator/assets/86603326/f60f0cfd-f30d-47df-9c5b-153bb726f2d6)
![screenshot 2 eX2](https://github.com/IbrahemHurani/Drone-Simulator/assets/86603326/104ba326-40f1-45bc-9a4a-91e1add7b80b)
![Ex2 screenshot 3](https://github.com/IbrahemHurani/Drone-Simulator/assets/86603326/f4ba2801-3550-46d6-8831-c8724f3ddc95)
![screenshot 4](https://github.com/IbrahemHurani/Drone-Simulator/assets/86603326/ff329a4e-9646-4279-adeb-c9dc767f4d25)



## How To Run
- install all the code from github
- open the code in platform like IntelliJ or any platform work with java
- put your path for the map photo in class SimulationWindow for Example my path:
```java
Map map = new Map("C:\\Users\\user\\Desktop\\DroneSimulator\\Maps\\p1" + map_num + ".png",startPoints[map_num-1]);
```
- put your path for the drone photo in class Drone for example my path:
 ```java
 private String drone_img_path = "C:\\Users\\user\\Desktop\\DroneSimulator\\Maps\\drone_3_pixels.png";
```
- Run class SimulationWindow
- for autonomous drone just click on button Toggle AI.
  

