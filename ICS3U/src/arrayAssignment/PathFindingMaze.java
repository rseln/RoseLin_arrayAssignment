package arrayAssignment;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sun.nio.cs.ext.ISCII91;

import org.omg.CORBA.DoubleSeqHelper;

import javafx.animation.AnimationTimer;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PathFindingMaze extends Application {

	// Rose Lin
	// Array Assignment
	// Mr. Radulovic
	// ICS 3U

	private Robot robot;
	private long oldTime;
	private double elapsedTime;
	private AnimationTimer timer;
	private String[][] mazeArray;
	private boolean[][] visitedArray;
	private int[][][] parentArray;
	private int mazeX, mazeY;
	private int size = 30;
	private int moves = 1;
	private int robotX, robotY;
	private int startX, startY;
	private int endX, endY;
	private Text text;
	private Pane root;

	public static void main(String args[]) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		// get array using FileReader
		FileReader fReader = new FileReader();
		mazeArray = fReader.getArray();

		// initialize maze values
		int numRows = mazeArray.length;
		int numCol = mazeArray[0].length;

		parentArray = new int[numCol][numRows][2]; //3D array that stores the last visited locations (allows for backtracking in dead ends) 
		//different from visited array because it is used for backtracking and does not restrict any movement when doing so

		// initialize stage values
		root = new Pane();
		Scene scene = new Scene(root);

		// set window size
		root.setPrefHeight(numRows * size);
		root.setPrefWidth(numCol * size);

		// create the window
		primaryStage.setTitle("Array Assignment");
		primaryStage.setScene(scene);
		primaryStage.show();

		// initialize Robot
		robot = new Robot();

		// setup a timer to be used for repeatedly redrawing the scene
		oldTime = System.nanoTime();
		timer = new AnimationTimer() {
			public void handle(long now) {
				double deltaT = (now - oldTime) / 1000000000.0;
				onUpdate(deltaT);
				oldTime = now;
			}
		};

		// initialize the visited array (makes sure that the robot can't go back onto
		// its trail)
		visitedArray = new boolean[numRows][numCol];

		// building the maze
		for (int row = 0; row < numRows; row++) { // rows
			for (int col = 0; col < numCol; col++) { // columns

				visitedArray[row][col] = false; // starts as a false output (does not mark anything)

				mazeX = col * size; // y position of the individual squares
				mazeY = row * size; // x position of the individual squares

				Rectangle r = new Rectangle(mazeX, mazeY, size, size);

				// set up text for moves
				text = new Text();
				text.setX(size - 20);
				text.setY(size - 10);
				text.setFont(Font.font("Arial", 15));
				text.setFill(Color.BLACK);

				// create border
				r.setStroke(Color.BLACK);
				r.setStrokeType(StrokeType.INSIDE);
				r.setStrokeWidth(0.3);

				if (mazeArray[row][col].equals("0")) {
					r.setFill(Color.BLUE);
				}

				if (mazeArray[row][col].equals("1")) {
					r.setFill(Color.WHITE);

				}

				if (mazeArray[row][col].equals("G")) {
					r.setFill(Color.GREEN);

					// starting position
					robot.setXPos(mazeX);
					robot.setYPos(mazeY);

					// starting position (rows and col); is fixed number so is not altered
					startX = col;
					startY = row;

					// initializing values for the updated movements; to be altered
					robotX = col;
					robotY = row;

				}

				if (mazeArray[row][col].equals("R")) {
					r.setFill(Color.RED);

					endX = col;
					endY = row;

				}

				root.getChildren().add(r); // add rectangles
				root.getChildren().add(text); // add text for moves to the root

			}

		}

		root.getChildren().add(robot.getRobotImage()); // add robot
		timer.start(); // start animation
	}

	private void pathFinding() { // pathfinding algorithm

		// checking position of the goals; prioritizing moves based on the positions
		// eg: if goal were to be in up and right from the starting position, the robot
		// would first move up then right
		// based on placement of if statements
		// parentArray stores the values

		int prevX = robotX; // records previously visited locations
		int prevY = robotY; // records previously visited locations
		boolean deadend = false; // allows for backtracking in the instance of a dead end

		if (startX <= endX && startY >= endY) { // if the goal is up and right; up, right, down, left

			// if wall is not up; move up
			if (robotY >= 1 && !mazeArray[robotY - 1][robotX].equals("0") && !visitedArray[robotY - 1][robotX]) {
				robotY--;
			}

			// if wall is not right; move right
			else if (robotX <= mazeArray[0].length - 1 && !mazeArray[robotY][robotX + 1].equals("0")
					&& !visitedArray[robotY][robotX + 1]) {
				robotX++;
			}

			// if wall is not down; move down
			else if (robotY < mazeArray.length - 1 && !mazeArray[robotY + 1][robotX].equals("0")
					&& !visitedArray[robotY + 1][robotX]) {
				robotY++;
			}

			// if wall is not left; move left
			else if (robotX >= 1 && !mazeArray[robotY][robotX - 1].equals("0") && !visitedArray[robotY][robotX - 1]) {
				robotX--;
			}

			else { // if surrounded by walls or visited cells, backtrack one square

				robotX = parentArray[robotX][robotY][0]; // if it can't go anywhere else, it backtracks into its
															// previous location (0 stores x, 1 stores y)
				robotY = parentArray[robotX][robotY][1];
				deadend = true; // only updates parentArray if this wasn't a dead end (prevents robot from going back and forth between 2 squares)
			}
		}

		else if (startX >= endX && startY >= endY) { // if the goal is up and left; up, left, down, right

			// if wall is not up; move up
			if (robotY >= 1 && !mazeArray[robotY - 1][robotX].equals("0") && !visitedArray[robotY - 1][robotX]) {
				robotY--;
			}
			// if wall is not left; move left
			else if (robotX >= 1 && !mazeArray[robotY][robotX - 1].equals("0") && !visitedArray[robotY][robotX - 1]) {
				robotX--;
			}

			// if wall is not down; move down
			else if (robotY < mazeArray.length - 1 && !mazeArray[robotY + 1][robotX].equals("0")
					&& !visitedArray[robotY + 1][robotX]) {
				robotY++;
			}

			// if wall is not right; move right
			else if (robotX <= mazeArray[0].length - 1 && !mazeArray[robotY][robotX + 1].equals("0")
					&& !visitedArray[robotY][robotX + 1]) {
				robotX++;
			}

			else { // if surrounded by walls or visited cells, backtrack one square

				robotX = parentArray[robotX][robotY][0]; // if it can't go anywhere else, it backtracks into its
															// previous location (0 stores x, 1 stores y)
				robotY = parentArray[robotX][robotY][1];
				deadend = true; // only updates parentArray if this wasn't a dead end (prevents robot from going back and forth between 2 squares)
			}

		}

		else if (startX <= endX && startY <= endY) { // if the goal is down and right; down, right, up, left

			// if wall is not down; move down
			if (robotY < mazeArray.length - 1 && !mazeArray[robotY + 1][robotX].equals("0")
					&& !visitedArray[robotY + 1][robotX]) {
				robotY++;
			}

			// if wall is not right; move right
			else if (robotX <= mazeArray[0].length - 1 && !mazeArray[robotY][robotX + 1].equals("0")
					&& !visitedArray[robotY][robotX + 1]) {
				robotX++;
			}

			// if wall is not up; move up
			else if (robotY >= 1 && !mazeArray[robotY - 1][robotX].equals("0") && !visitedArray[robotY - 1][robotX]) {
				robotY--;
			}
			// if wall is not left; move left
			else if (robotX >= 1 && !mazeArray[robotY][robotX - 1].equals("0") && !visitedArray[robotY][robotX - 1]) {
				robotX--;
			}

			else { // if surrounded by walls or visited cells, backtrack one square

				robotX = parentArray[robotX][robotY][0]; // if it can't go anywhere else, it backtracks into its
															// previous location (0 stores x, 1 stores y)
				robotY = parentArray[robotX][robotY][1];
				deadend = true; // only updates parentArray if this wasn't a dead end (prevents robot from going back and forth between 2 squares)
			}

		}

		else if (startX >= endX && startY <= endY) { // if the goal is down and left; down, left, up, right

			// if wall is not down; move down
			if (robotY < mazeArray.length - 1 && !mazeArray[robotY + 1][robotX].equals("0")
					&& !visitedArray[robotY + 1][robotX]) {
				robotY++;
			}

			// if wall is not left; move left
			else if (robotX >= 1 && !mazeArray[robotY][robotX - 1].equals("0") && !visitedArray[robotY][robotX - 1]) {
				robotX--;
			}
			// if wall is not up; move up
			else if (robotY >= 1 && !mazeArray[robotY - 1][robotX].equals("0") && !visitedArray[robotY - 1][robotX]) {
				robotY--;
			}

			// if wall is not right; move right
			else if (robotX < mazeArray[0].length - 1 && !mazeArray[robotY][robotX + 1].equals("0")
					&& !visitedArray[robotY][robotX + 1]) {
				robotX++;
			}

			else { // if surrounded by walls or visited cells, backtrack one square

				robotX = parentArray[robotX][robotY][0]; // if it can't go anywhere else, it backtracks into its
															// previous location (0 stores x, 1 stores y)
				robotY = parentArray[robotX][robotY][1];
				deadend = true; // only updates parentArray if this wasn't a dead end (prevents robot from going back and forth between 2 squares)
			}

		}

		visitedArray[robotY][robotX] = true; // marks what positions where already visited; makes sure backtracking
												// doesn't happen unless robot is trying to get our of a dead end

		if (!deadend) { // if robot does not encounter a dead end (false), it updates the array
			parentArray[robotX][robotY][0] = prevX; // set parent array value back to prevX
			parentArray[robotX][robotY][1] = prevY; // set parent array value back to prevY
		}

		robot.getRobotImage().toFront();

		robot.setXPos(robotX * size); // moving the robot (columns)
		robot.setYPos(robotY * size); // moving the robot (rows)

	}

	private void onUpdate(double deltaT) {
		elapsedTime += deltaT;

		if (elapsedTime > 0.5) { // half a second for every movement

			// set text to print onto screen
			text.setText("Moves: " + moves);

			// add trail
			Rectangle rec = new Rectangle(robotX * size, robotY * size, size, size);
			rec.setFill(Color.YELLOW);
			root.getChildren().add(rec);

			// create border for trail
			rec.setStroke(Color.BLACK);
			rec.setStrokeType(StrokeType.INSIDE);
			rec.setStrokeWidth(0.3);

			moves++; // counter for # of moves

			pathFinding();

			if (robot.getPosX() == endX * size && robot.getPosY() == endY * size) { // if the robot reaches the goal
				timer.stop(); // stop the animation!
			}

			elapsedTime = 0;

		}
	}

}
