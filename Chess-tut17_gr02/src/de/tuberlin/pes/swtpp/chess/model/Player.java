package de.tuberlin.pes.swtpp.chess.model;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * The abstract player class represents every object that can be assigned as an opponent in a chess match.
 * 
 *  It therefore holds a unique ID and ..........
 *  
 * 
 */
public abstract class Player {
	
	protected String name = "";
	protected String id = "";
	protected List<Integer> games = new LinkedList<Integer>();
	
	/**
	 * 
	 * @return a display name for the chess game GUI
	 */
	public abstract String getName();
	
	public String getId() {
		return id;
	}
	
	//TODO: Da fehlt bestimmt noch was 
	public List<Integer> getGames() {
		return games;
	}
	
	public void addGame(Integer g){
		games.add(g);
	}
	
}
