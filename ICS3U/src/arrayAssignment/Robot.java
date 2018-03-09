package arrayAssignment;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Robot {
	
	//Rose Lin 
	//Array Assignment
	//Mr. Radulovic 
	//ICS 3U

	private double xPos, yPos;
	private ImageView robot;

	public Robot() {

		xPos = 0;
		yPos = 0;

		// setting all the image values for the robot
		Image robotImage = new Image("file:src/arrayAssignment/robot.png"); // loading image
		robot = new ImageView();
		robot.setImage(robotImage);
		robot.setFitWidth(30); // adjusting the size
		robot.setPreserveRatio(true); // size ratio
		robot.setX(xPos); // establishing x position
		robot.setY(yPos); // establishing y position

	}

	public ImageView getRobotImage() {
		return robot;
	}

	public double getPosX() {
		return robot.getX();
	}

	public double getPosY() {
		return robot.getY();
	}

	public void setXPos(double x) {
		robot.setX(x);
	}

	public void setYPos(double y) {
		robot.setY(y);
	}


}
