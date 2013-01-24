/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;

public class Hangman extends ConsoleProgram {

  private static final HangmanLexicon hangmanDictionary = new HangmanLexicon();
	private static HangmanCanvas canvas = new HangmanCanvas();
	private static String hangmanWord;
	public int lettersLeft = 0;
	public int numberOfTries = 8;

	public void run() {
		drawCanvasBeams();
		promptForHangman();

	}

	private void promptForHangman() {
		String guessedWord = setupNewWord();

		println("Welcome to Hangman!");
		println("The world now looks like this");
		printWordSoFar(guessedWord);
		canvas.displayWord(guessedWord);

		String line = guessedLetter();
		guessedWord = fillInMissingLetters(line.charAt(0), guessedWord,
				hangmanWord);
		printWordSoFar(guessedWord);
		canvas.displayWord(guessedWord);

		// let's keep prompting to enter more letters
		while (numberOfTries > 0) {
			line = guessedLetter();
			guessedWord = fillInMissingLetters(line.charAt(0), guessedWord,
					hangmanWord);
			printWordSoFar(guessedWord);
			canvas.displayWord(guessedWord);

			if (lettersLeft == 0) {
				println("You win!");
				break;
			}
		}

		if (numberOfTries == 0) {
			println("Game Over, you lose!");
		}

	}

	private void drawCanvasBeams() {
		canvas.reset();
		add(canvas);
	}

	private String guessedLetter() {

		println("You have " + numberOfTries + " guesses left.");
		String line1 = readLine("your guess: ");
		char[] ch = line1.trim().toUpperCase().toCharArray();

		while (!Character.isLetter(ch[0])) {
			line1 = readLine("Can you please enter a letter ?  ");
			ch = line1.trim().toUpperCase().toCharArray();
		}
		String line2 = new String(ch);
		return line2;
	}

	// logic for filling in all the letters
	private String fillInMissingLetters(char letter, String myWordSoFar,
			String theHangmanWord) {
		int index = 0;
		char[] myWordSoFarArray = myWordSoFar.toCharArray();
		index = theHangmanWord.indexOf(letter, index);

		// check if I don't find the letter at all on the first time around
		if (index == -1) {
			canvas.noteIncorrectGuess(letter);
			numberOfTries--;
		}

		// keep filling in the letters of that index
		while (index != -1) {
			index = theHangmanWord.indexOf(letter, index);
			if (index != -1) {
				myWordSoFarArray[index] = letter;
				// to tell the index to look at the next position
				index++;
				lettersLeft--;
			}
		}

		myWordSoFar = new String(myWordSoFarArray);
		return myWordSoFar;
	}

	// get my new word
	private String setupNewWord() {
		// generate a random word from my dictionary
		RandomGenerator randGen = RandomGenerator.getInstance();
		hangmanWord = hangmanDictionary.getWord(randGen.nextInt(0,
				hangmanDictionary.getWordCount() - 1));
		char[] wordSoFar = new char[hangmanWord.length()];

		for (int i = 0; i < hangmanWord.length(); i++) {
			wordSoFar[i] = '-';
		}
		// returning a string
		String wordSoFarAsString = new String(wordSoFar);
		lettersLeft = hangmanWord.length();
		return wordSoFarAsString;
	}

	private void printWordSoFar(String word) {
		println(word);
	}

}
