/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import acm.graphics.*;

public class HangmanCanvas extends GCanvas {

  private int SCREEN_WIDTH = 300;
	private int SCREEN_HEIGHT = 380;
	private final int x = SCREEN_WIDTH / 4;
	private final int y = SCREEN_HEIGHT / 10;

	private int wordX = SCREEN_WIDTH / 3;
	private int wordY = SCREEN_HEIGHT + 40;

	private GLabel wordLabel = new GLabel("", wordX, wordY);

	private GLabel guessesLabel = new GLabel("", wordX, wordY + 20);
	private String incorrectGuesses = "";

	private GLabel youLoseLabel = new GLabel("You lose!", wordX, wordY + 40);
	private int numberOfGuesses = 0;

	/** Resets the display so that only the scaffold appears */
	public void reset() {
	
		SCREEN_WIDTH = getWidth();
		SCREEN_HEIGHT = getHeight();
		
		GLine scaffold = new GLine(x, y, x, y + SCAFFOLD_HEIGHT);
		add(scaffold);
		GLine beam = new GLine(x, y, x + BEAM_LENGTH, y);
		add(beam);

		GLine rope = new GLine(x + BEAM_LENGTH, y, x + BEAM_LENGTH, y
				+ ROPE_LENGTH);
		add(rope);

	}

	private void drawLeftFoot() {
		int x1 = x + BEAM_LENGTH + HIP_WIDTH;
		int y1 = y + ROPE_LENGTH + HEAD_RADIUS * 2 + BODY_LENGTH + LEG_LENGTH;

		GLine leftFoot = new GLine(x1, y1, x1 + FOOT_LENGTH, y1);
		add(leftFoot);

	}

	private void drawRightFoot() {
		int x1 = x + BEAM_LENGTH - HIP_WIDTH;
		int y1 = y + ROPE_LENGTH + HEAD_RADIUS * 2 + BODY_LENGTH + LEG_LENGTH;

		GLine rightFoot = new GLine(x1, y1, x1 - FOOT_LENGTH, y1);
		add(rightFoot);
	}

	private void drawLeftLeg() {
		int x1 = x + BEAM_LENGTH;
		int y1 = y + ROPE_LENGTH + HEAD_RADIUS * 2 + BODY_LENGTH;

		GLine leftLeg = new GLine(x1, y1, x1 + HIP_WIDTH, y1);
		add(leftLeg);
		GLine leftShin = new GLine(x1 + HIP_WIDTH, y1, x1 + HIP_WIDTH, y1
				+ LEG_LENGTH);
		add(leftShin);

	}

	private void drawRightLeg() {
		int x1 = x + BEAM_LENGTH;
		int y1 = y + ROPE_LENGTH + HEAD_RADIUS * 2 + BODY_LENGTH;

		GLine rightLeg = new GLine(x1, y1, x1 - HIP_WIDTH, y1);
		add(rightLeg);
		GLine rightShin = new GLine(x1 - HIP_WIDTH, y1, x1 - HIP_WIDTH, y1
				+ LEG_LENGTH);
		add(rightShin);

	}

	private void drawHead() {

		GOval head = new GOval(x + BEAM_LENGTH - HEAD_RADIUS, y + ROPE_LENGTH,
				HEAD_RADIUS * 2, HEAD_RADIUS * 2);
		add(head);
	}

	private void drawBody() {
		int x1 = x + BEAM_LENGTH;
		int y1 = y + ROPE_LENGTH + HEAD_RADIUS * 2;
		GLine body = new GLine(x1, y1, x1, y1 + BODY_LENGTH);
		add(body);

	}

	private void drawLeftArm() {
		int x1 = x + BEAM_LENGTH;
		int y1 = y + ROPE_LENGTH + HEAD_RADIUS * 2 + ARM_OFFSET_FROM_HEAD;

		GLine leftArm = new GLine(x1, y1, x1 + UPPER_ARM_LENGTH, y1);
		add(leftArm);
		GLine leftHand = new GLine(x1 + UPPER_ARM_LENGTH, y1, x1
				+ UPPER_ARM_LENGTH, y1 + LOWER_ARM_LENGTH);
		add(leftHand);

	}

	private void drawRightArm() {
		int x1 = x + BEAM_LENGTH;
		int y1 = y + ROPE_LENGTH + HEAD_RADIUS * 2 + ARM_OFFSET_FROM_HEAD;

		GLine rightArm = new GLine(x1, y1, x1 - UPPER_ARM_LENGTH, y1);
		add(rightArm);
		GLine rightHand = new GLine(x1 - UPPER_ARM_LENGTH, y1, x1
				- UPPER_ARM_LENGTH, y1 + LOWER_ARM_LENGTH);
		add(rightHand);

	}

	/**
	 * Updates the word on the screen to correspond to the current state of the
	 * game. The argument string shows what letters have been guessed so far;
	 * unguessed letters are indicated by hyphens.
	 */
	public void displayWord(String word) {
		wordLabel.setLabel(word);
		wordLabel.setFont("Helvetica-25");
		add(wordLabel);
	}

	/**
	 * Updates the display to correspond to an incorrect guess by the user.
	 * Calling this method causes the next body part to appear on the scaffold
	 * and adds the letter to the list of incorrect guesses that appears at the
	 * bottom of the window.
	 */
	public void noteIncorrectGuess(char letter) {
		// add my to my incorrect guesses at bottom

		if (numberOfGuesses < 7) {
			String str = Character.toString(letter);
			incorrectGuesses = incorrectGuesses.concat(str);
			guessesLabel.setLabel(incorrectGuesses);
			guessesLabel.setFont("TimesNewRoman-25");
			add(guessesLabel);
			numberOfGuesses++;
			drawBodyPart(numberOfGuesses);
		} else {
			String str = Character.toString(letter);
			incorrectGuesses = incorrectGuesses.concat(str);
			guessesLabel.setLabel(incorrectGuesses);
			guessesLabel.setFont("TimesNewRoman-25");
			add(guessesLabel);
					
			numberOfGuesses++;
			drawBodyPart(numberOfGuesses);
			displayYouLoseLabel();

		}
	}

	private void drawBodyPart(int i) {
		switch (i) {
		case 1:
			drawHead();
			
			break;
		case 2:
			drawBody();
			break;
		case 3:
			drawLeftArm();
			break;
		case 4:
			drawRightArm();
			break;
		case 5:
			drawLeftLeg();
			break;
		case 6:
			drawRightLeg();
			break;
		case 7:
			drawLeftFoot();
			break;
		case 8:
			drawRightFoot();
			break;
		}
	}

	public void displayYouLoseLabel() {
		youLoseLabel.setLabel("You Lose!");
		youLoseLabel.setFont("Helvetica-25");
		add(youLoseLabel);
		drawDeadFace();
	}

	public void drawDeadFace(){
		GLine line1 = new GLine(x + BEAM_LENGTH - HEAD_RADIUS+ 10,y + ROPE_LENGTH + 20,
				x + BEAM_LENGTH - HEAD_RADIUS + 30 ,y + ROPE_LENGTH +40 );
		add(line1);
		GLine line2 = new GLine(x + BEAM_LENGTH - HEAD_RADIUS+ 30,y + ROPE_LENGTH + 20,
				x + BEAM_LENGTH - HEAD_RADIUS + 10 ,y + ROPE_LENGTH +40 );
		add(line2);
		GLine line3 = new GLine(x + BEAM_LENGTH - HEAD_RADIUS+ 40,y + ROPE_LENGTH + 20,
				x + BEAM_LENGTH - HEAD_RADIUS + 60 ,y + ROPE_LENGTH +40 );
		add(line3);
		GLine line4 = new GLine(x + BEAM_LENGTH - HEAD_RADIUS+ 60,y + ROPE_LENGTH + 20,
				x + BEAM_LENGTH - HEAD_RADIUS + 40 ,y + ROPE_LENGTH +40 );
		add(line4);
		
		GLabel deadLabel = new GLabel( "BLAHHH!", x + BEAM_LENGTH - HEAD_RADIUS+ 90,y + ROPE_LENGTH + 40);
		deadLabel.setFont("Helvetica-20");
		add(deadLabel);
		
	}
	
	
	
	/* Constants for the simple version of the picture (in pixels) */
	private static final int SCAFFOLD_HEIGHT = 360;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 18;
	private static final int HEAD_RADIUS = 36;
	private static final int BODY_LENGTH = 144;
	private static final int ARM_OFFSET_FROM_HEAD = 28;
	private static final int UPPER_ARM_LENGTH = 72;
	private static final int LOWER_ARM_LENGTH = 44;
	private static final int HIP_WIDTH = 36;
	private static final int LEG_LENGTH = 108;
	private static final int FOOT_LENGTH = 28;

}
