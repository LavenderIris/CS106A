/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

  /** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	// 30 pixels padding for menu
	public static final int APPLICATION_HEIGHT = 600 + 30;

	private static final int WIDTH = 400;
	private static final int HEIGHT = 600;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH = (WIDTH - (NBRICKS_PER_ROW - 1)
			* BRICK_SEP)
			/ NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** diameter of the ball in pixels to deal with GOval's way of drawing ovals **/
	private static final int BALL_DIAMETER = 2 * BALL_RADIUS;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;
	private int lifeLeft = NTURNS;

	/** An array of colors for my initial grid **/
	private static final Color COLOR_GRIDLIST[] = { Color.RED, Color.RED,
			Color.ORANGE, Color.ORANGE, Color.YELLOW, Color.YELLOW,
			Color.GREEN, Color.GREEN, Color.CYAN, Color.CYAN };

	/** my paddle being used globally */
	GRect paddle = new GRect(BRICK_SEP / 2, initialPaddle, PADDLE_WIDTH,
			PADDLE_HEIGHT);

	private static final double initialPaddle = HEIGHT
			- (PADDLE_Y_OFFSET + PADDLE_HEIGHT);

	private int gameScore = 0;

	private GLabel scoreLabel = new GLabel("Score: 0");

	// my ball
	GOval ball = new GOval(WIDTH / 2 - BALL_RADIUS, HEIGHT / 2 - BALL_RADIUS,
			BALL_DIAMETER, BALL_DIAMETER);
	private static final Color BALL_COLOR = Color.PINK;

	// used for animation
	private static int PAUSE_TIME = 10;
	double vx, vy;
	private RandomGenerator rgen = RandomGenerator.getInstance();

	// location of the ball previously to calculate side collisions
	private double ball_prevX, ball_prevY;

	public void run() {
		drawMyInitialGrid();
		createPaddle();
		drawScoreLabel();
		setGameBallMovement();
	}

	private void drawScoreLabel() {
		scoreLabel.setFont("SansSerif-14");
		add(scoreLabel, 300, 30);
	}

	public void init() {
		addMouseListeners();
	}

	// mouse move event for my paddle
	public void mouseMoved(MouseEvent e) {
		// I have to put an if statement so my paddle doesn't go offscreen
		if (e.getX() - (PADDLE_WIDTH / 2) < 0) {
			paddle.setLocation(0, initialPaddle);
		} else if (e.getX() + (PADDLE_WIDTH / 2) > WIDTH) {
			paddle.setLocation(WIDTH - (PADDLE_WIDTH), initialPaddle);
		} else {
			paddle.setLocation(e.getX() - (PADDLE_WIDTH / 2), initialPaddle);
		}
	}

	private void setGameBallMovement() {
		GObject collider1 = null;
		boolean collidedWithPaddle = false;

		// initial ball position
		ball.setLocation(APPLICATION_WIDTH / 2, APPLICATION_HEIGHT / 2);
		ball.setFilled(true);
		ball.setColor(BALL_COLOR);
		ball.setFillColor(BALL_COLOR);
		add(ball);

		//generate a random speed for my ball
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5))
			vx = -vx;
		vy = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5))
			vy = -vy;

		while (true) {
			if (ball.getX() >= (WIDTH - BALL_DIAMETER))
				vx = -vx;
			else if (ball.getX() <= 0)
				vx = -vx;
			else if (ball.getY() <= 0)
				vy = -vy;
			else if (ball.getY() >= (HEIGHT - BALL_DIAMETER))
				vy = -vy;

			ball_prevX = ball.getX();
			ball_prevY = ball.getY();

			ball.move(vx, vy);

			collider1 = checkForCollisions();
			collidedWithPaddle = checkIfPaddle(collider1);

			// if I collided with my paddle, move in the opposite direction
			if (collidedWithPaddle) {
				setCollisionMovement(collider1);
			} else if ((collider1 != null) && (collider1 != scoreLabel)) {
				setCollisionMovement(collider1);
				updateScore(collider1);
				remove(collider1);
			}

			pause(PAUSE_TIME);

		} // end while statement

	}

	// sets if I move sideways or not
	private void setCollisionMovement(GObject collider) {
		if (calculateSideOfCollision(collider) == 0) {
			vx = -vx;
		} else {
			vy = -vy;
		}
	}

	private int calculateSideOfCollision(GObject rect) {

		// I want to check the side collision from my ball's position in the
		// previous frame
		double ballCenterX = ball_prevX + BALL_RADIUS;
		double ballCenterY = ball_prevY + BALL_RADIUS;
		double paddleXCoord = rect.getX();
		double paddleYCoord = rect.getY();
		double paddleXEnd = rect.getX() + rect.getWidth();
		double paddleYEnd = rect.getY() + rect.getHeight();

		// i only care about side of collision . 1 = top or bottom collision, 0
		// = side collisions

		// cuts out the easy four regions
		// this is for the top or bottom regions
		if ((ballCenterX < paddleXCoord || ballCenterX > paddleXEnd)
				&& ballCenterY > paddleYCoord && (ballCenterY < paddleYEnd))
			return 0;
		// this is for the side regions
		else if ((ballCenterY < paddleYCoord || ballCenterY > paddleYEnd)
				&& ballCenterX < paddleXEnd && ballCenterX > paddleXCoord)
			return 1;

		// now this is the harder stuff for each of the sides and checks the 4
		// corners
		if (ballCenterX < paddleXCoord && ballCenterY < paddleYCoord) {
			if (Math.abs(ballCenterX - paddleXCoord) < Math.abs(ballCenterY
					- paddleYCoord))
				return 1;
			else {
				return 0;
			}
		} else if (ballCenterX > paddleXEnd && ballCenterY < paddleYCoord) {
			if (Math.abs(ballCenterX - paddleXEnd) < Math.abs(ballCenterY
					- paddleYCoord)) {
				return 1;
			} else {
				return 0;
			}
		} else if (ballCenterX > paddleXEnd && ballCenterY > paddleYEnd) {
			if (Math.abs(ballCenterX - paddleXEnd) < Math.abs(ballCenterY
					- paddleYEnd)) {
				return 1;
			} else {
				return 0;
			}
		} else if (ballCenterX < paddleXCoord && ballCenterY > paddleYEnd) {
			if (Math.abs(ballCenterX - paddleXCoord) < Math.abs(ballCenterY
					- paddleYEnd)) {
				return 1;
			} else {
				return 0;
			}
		}

		return 3;
	}

	private void updateScore(GObject anObject) {
		if (anObject.getColor() == Color.CYAN)
			gameScore += 10;
		else if (anObject.getColor() == Color.GREEN)
			gameScore += 20;
		else if (anObject.getColor() == Color.YELLOW)
			gameScore += 30;
		else if (anObject.getColor() == Color.ORANGE)
			gameScore += 40;
		else if (anObject.getColor() == Color.RED)
			gameScore += 50;

		scoreLabel.setLabel("Score: " + gameScore);
	}

	private boolean checkIfPaddle(GObject collider) {
		if (collider == paddle)
			return true;
		else
			return false;
	}

	private void resetBallAndSubtractLife() {
		ball.setLocation(APPLICATION_WIDTH / 2, APPLICATION_HEIGHT / 2);
		lifeLeft--;

		if (lifeLeft == 0) {
			setGameOver();
		}

	}

	private void setGameOver() {
		GLabel gameOverLabel = new GLabel("You lose! The game is over. ");
		gameOverLabel.setLocation(
				APPLICATION_WIDTH / 2 - gameOverLabel.getWidth() / 2,
				APPLICATION_HEIGHT / 2);
		add(gameOverLabel);
		remove(ball);

		while (true) {
			pause(1000000);
		}
	}

	private GObject checkForCollisions() {
		double ballX, ballY;
		GObject collidedObject = null;

		// get the x and y coordinates.
		ballX = ball.getX();
		ballY = ball.getY();

		// gets rid of the life counter
		if ((ballY + BALL_DIAMETER + 30) >= APPLICATION_HEIGHT) {
			resetBallAndSubtractLife();

		}

		// in this object, do I collide? I will check every corner and if it's
		// nothing, do nothing
		if (this.getElementAt(ballX, ballY) == null) {
			if (this.getElementAt(ballX + BALL_DIAMETER, ballY) == null) {
				if (this.getElementAt(ballX + BALL_DIAMETER, ballY
						+ BALL_DIAMETER) == null) {
					if (this.getElementAt(ballX, ballY + BALL_DIAMETER) == null) {
					} else {
						collidedObject = this.getElementAt(ballX, ballY
								+ BALL_DIAMETER);
					}
				} else {
					collidedObject = this.getElementAt(ballX + BALL_DIAMETER,
							ballY + BALL_DIAMETER);
				}
			} else {
				collidedObject = this
						.getElementAt(ballX + BALL_DIAMETER, ballY);
			}
		} else {
			collidedObject = this.getElementAt(ballX, ballY);
		}
		// code to play sound - if there is an object that is collided
		if ((collidedObject != null) && (collidedObject != scoreLabel))
			playBounceSound();
		return collidedObject;
	}

	// plays sound for every bounce
	private void playBounceSound() {
		AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
		bounceClip.play();
	}

	// creates my paddle
	private void createPaddle() {
		paddle.setLocation(BRICK_SEP / 2.0, initialPaddle);
		paddle.setFilled(true);
		paddle.setFillColor(Color.BLACK);
		add(paddle);
	}

	private void drawMyInitialGrid() {
		int initial_x = (WIDTH - (NBRICKS_PER_ROW * (BRICK_WIDTH + BRICK_SEP)))
				/ 2 + (BRICK_SEP / 2);
		int initial_y = BRICK_Y_OFFSET;

		// draw my grid
		for (int j = 0; j < NBRICK_ROWS; j++) {
			drawARowOfBricks(initial_x, initial_y, COLOR_GRIDLIST[j]);
			initial_y += BRICK_HEIGHT + BRICK_SEP;
		}
	}

	// function to draw a row of bricks
	private void drawARowOfBricks(int x, int y, Color brickcolor) {
		// draw the row of bricks.
		for (int i = 0; i < NBRICKS_PER_ROW; i++) {
			GRect brick = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
			brick.setFilled(true);
			brick.setFillColor(brickcolor);
			brick.setColor(brickcolor);
			add(brick);
			x += BRICK_WIDTH + BRICK_SEP;
		}
	}

}
