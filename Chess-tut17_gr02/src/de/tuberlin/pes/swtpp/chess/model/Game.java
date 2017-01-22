package de.tuberlin.pes.swtpp.chess.model;

import java.util.LinkedList;
import java.util.List;

public class Game {

	//Attribute
	private int id;
	private boolean started;
	private boolean finished;
	private Player white;
	private Player black;
	private Player winner;
	private Board board;
	private List<Move> moves = new LinkedList<Move>();
	private boolean drawWhite;
	private boolean drawBlack;
	private boolean giveupWhite;
	private boolean giveupBlack;
	//getter
	public int getId(){
		return id;
	}
	public boolean getStarted(){
		return started;
	}
	public boolean getFinished(){
		return finished;
	}
	public Player getWhite(){
		return white;
	}
	public Player getBlack(){
		return black;
	}
	public Player getWinner(){
		return winner;
	}
	public Board getBoard(){
		return board;
	}
	public List<Move> getMoves(){
		return moves;
	}
	public boolean drawWhite(){
		return drawWhite;
	}
	public boolean drawBlack(){
		return drawBlack;
	}
	public boolean giveupWhite(){
		return giveupWhite;
	}
	public boolean giveupBlack(){
		return giveupBlack;
	}
	//setter
	public void setId(int ide){
		id=ide;
	}
	public void setStarted(boolean star){
		started=star;
	}
	public void setFinished(boolean fine){
		finished=fine;
	}
	public void setWhite(Player p){
		white=p;
	}
	public void setBlack(Player p){
		black=p;
	}
	public void setWinner(Player p){
		winner=p;
	}
	public void setBoard(Board b){
		board=b;
	}
	public void setDrawWhite(boolean dw){
		drawWhite=dw;
	}
	public void setDrawBlack(boolean db){
		drawBlack=db;
	}
	public void setGiveupWhite(boolean gw){
		giveupWhite=gw;
	}
	public void setGiveupBlack(boolean gb){
		giveupBlack=gb;
	}
	
	//fügt ein zug ein
	public void addMove(Move m){
		moves.add(m);
	}
	
	//prüft ob ein spieler am zug ist
	public boolean isTurn(Player p){
		if(!finished && moves.size()%2==0 && p.getId().equals(white.getId())) return true;
		
		if(!finished && moves.size()%2==1 && p.getId().equals(black.getId())) return true;
		
		return false;
	}
}
