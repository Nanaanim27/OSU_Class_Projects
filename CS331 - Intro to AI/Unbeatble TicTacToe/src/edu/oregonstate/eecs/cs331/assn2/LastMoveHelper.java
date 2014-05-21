package edu.oregonstate.eecs.cs331.assn2;

public class LastMoveHelper {

	public Position position;
	public int utility;
	
	/*
	 * The purpose of LastMoveHelper is to essentially act as a container for
	 * the current state's position and its utility after each move) so each board state's 
	 * attributes can be stored at each recursion level. Makes MiniMax code more readable
	 */
	public LastMoveHelper(Position position, int utility){
		this.position = position;
		this.utility = utility;
		
	}
}
