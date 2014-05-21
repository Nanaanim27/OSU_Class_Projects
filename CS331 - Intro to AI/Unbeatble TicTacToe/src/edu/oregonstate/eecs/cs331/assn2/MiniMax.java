package edu.oregonstate.eecs.cs331.assn2;

import java.util.ArrayList;

/**
 * This class represents the module for minimax.
 * 
 * @author Chris Ventura
 * 
 */
public class MiniMax implements Player {
	
	public static final int MOCK_INFINITY = 1000000;
	
	public static final int NEG_MOCK_INFINITY = -1000000;
	
	/**
	 * Constructor
	 * 
	 */
	public MiniMax() {

	}

	/**
	 * Returns the next move.
	 * 
	 * @param state
	 *            The current board state in the game
	 * @return The next move
	 */
	public Position getNextMove(TicTacToeBoard state) throws Exception {
		/*
		 * LastMoveHelper is a simple helper class that holds the position of the last
		 * move made and the utility value of the board at that configuration
		 */
		LastMoveHelper container;
		
		//The 'X' player will always try to maximize
		if(state.getTurn() == TicTacToeBoard.PLAYER_X){
			container = MAX(state);
			return container.position;
		//The 'O' player will always try to minimize
		}else if(state.getTurn() == TicTacToeBoard.PLAYER_O){
			container = MIN(state);
			return container.position;
		}else{
			//Only to satisfy the compiler.  Will never be reached.
			return null;
		}
		
	}
	
	//Minimizing functionality for Mini-Max
	public LastMoveHelper MIN(TicTacToeBoard state) {
		
		ArrayList<TicTacToeBoard> allSucc = new ArrayList<TicTacToeBoard>();
		
		try {
			//Check for winning board configuration, and Instatiate helper containers to store 
			//position and utility values at terminal configurations(node). Recursion ends. 
			if (state.isGameOver() == true) {
				if (state.isWin(state.PLAYER_X)) {
					LastMoveHelper container = new LastMoveHelper(state.lastMove, state.UTILITY_MAX);
					return container;

				} else if (state.isWin(state.PLAYER_O)) {
					LastMoveHelper container = new LastMoveHelper(state.lastMove, state.UTILITY_MIN);
					return container;

				} else {
					LastMoveHelper container = new LastMoveHelper(state.lastMove, 0);
					return container;
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		allSucc = state.getSuccessors(state);
		//Create a helper with current configuration and unbeatable positive utility.
		LastMoveHelper tempLMH = new LastMoveHelper(state.lastMove, MOCK_INFINITY);
		
		//loop through all successors of the current board state and recursively maximize
		for (int i = 0; i < allSucc.size(); i++) {
			TicTacToeBoard tempTTTB = allSucc.get(i);
			LastMoveHelper max = MAX(tempTTTB);
			if (max.utility <= tempLMH.utility) {
				tempLMH = max;
				tempLMH.position = tempTTTB.lastMove;
			}
		}
		return tempLMH; //Return helper ojb. containing position of best move
	}

	//Maximization functionality for Mini-max
	public LastMoveHelper MAX(TicTacToeBoard state) {
		
		ArrayList<TicTacToeBoard> allSucc = new ArrayList<TicTacToeBoard>();

		try {
			//Check for winning board configuration, and Instatiate helper containers to store 
			//position and utility values at terminal configuration(node). Recursion ends. 
			if (state.isGameOver() == true) {
				if (state.isWin(state.PLAYER_X)) {
					LastMoveHelper container = new LastMoveHelper(state.lastMove, state.UTILITY_MAX);
					return container;

				} else if (state.isWin(state.PLAYER_O)) {
					LastMoveHelper container = new LastMoveHelper(state.lastMove, state.UTILITY_MIN);
					return container;

				} else {
					LastMoveHelper container = new LastMoveHelper(state.lastMove, 0);
					return container;
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		allSucc = state.getSuccessors(state);
		//Create a helper with current configuration and unbeatable negative utility.
		LastMoveHelper tempLMH = new LastMoveHelper(state.lastMove, NEG_MOCK_INFINITY);
			
		////loop through all successors of the current board state and recursively minmize
		for (int i = 0; i < allSucc.size(); i++) {
			TicTacToeBoard tempTTTB = allSucc.get(i);
			LastMoveHelper min = MIN(tempTTTB);
			if (min.utility >= tempLMH.utility) {
				tempLMH = min;
				tempLMH.position = tempTTTB.lastMove;
			}
			
		}
		return tempLMH; //Return helper obj. containing position of best move

	}

	/**
	 * Returns the player type
	 */
	public int getPlayerType() {
		return MINIMAX_PLAYER;
	}

}










