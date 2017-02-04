package de.tuberlin.sese.swtpp.gameserver.model.lasca;

import java.io.Serializable;
import java.util.List;

import de.tuberlin.sese.swtpp.gameserver.model.Game;
import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

/**
 * Class LascaGame extends the abstract class Game as a concrete game instance that allows to play 
 * Lasca (http://www.lasca.org/).
 *
 */
public class LascaGame extends Game implements Serializable{

	private static final long serialVersionUID = 8461983069685628324L;
	
	/************************
	 * member
	 ***********************/
	
	// just for better comprehensibility of the code: assign white and black player
	private Player blackPlayer;
	private Player whitePlayer;

	// internal representation of the game state
	// TODO: insert additional game data here
	String state = "b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w w";
	String[][] board;
	
	/************************
	 * constructors
	 ***********************/
	
	public LascaGame() {
		super();
		
		// initialize internal game model (state/ board here)
	}
	
	/*******************************************
	 * Game class functions already implemented
	 ******************************************/
	
	@Override
	public boolean addPlayer(Player player) {
		if (!started) {
			players.add(player);
			
			if (players.size() == 2) {
				started = true;
				this.whitePlayer = players.get(0);
				this.blackPlayer = players.get(1);
				nextPlayer = this.whitePlayer;
			}
			return true;
		}
		
		return false;
	}

	@Override
	public String getStatus() {
		if (error) return "Error";
		if (!started) return "Wait";
		if (!finished) return "Started";
		if (surrendered) return "Surrendered";
		if (draw) return "Draw";
		
		return "Finished";
	}
	
	@Override
	public String gameInfo() {
		String gameInfo = "";
		
		if(started) {
			if(blackGaveUp()) gameInfo = "black gave up";
			else if(whiteGaveUp()) gameInfo = "white gave up";
			else if(didWhiteDraw() && !didBlackDraw()) gameInfo = "white called draw";
			else if(!didWhiteDraw() && didBlackDraw()) gameInfo = "black called draw";
			else if(draw) gameInfo = "draw game";
			else if(finished)  gameInfo = blackPlayer.isWinner()? "black won" : "white won";
		}
			
		return gameInfo;
	}	

	@Override
	public int getMinPlayers() {
		return 2;
	}

	@Override
	public int getMaxPlayers() {
		return 2;
	}
	
	@Override
	public boolean callDraw(Player player) {
		
		// save to status: player wants to call draw 
		if (this.started && ! this.finished) {
			player.requestDraw();
		} else {
			return false;
		}
	
		// if both agreed on draw:
		// game is over
		if(players.stream().allMatch(p -> p.requestedDraw())) {
			this.finished = true;
			this.draw = true;
			whitePlayer.finishGame();
			blackPlayer.finishGame();
		}	
		return true;
	}
	
	@Override
	public boolean giveUp(Player player) {
		if (started && !finished) {
			if (this.whitePlayer == player) { 
				whitePlayer.surrender();
				blackPlayer.setWinner();
			}
			if (this.blackPlayer == player) {
				blackPlayer.surrender();
				whitePlayer.setWinner();
			}
			finished = true;
			surrendered = true;
			whitePlayer.finishGame();
			blackPlayer.finishGame();
			
			return true;
		}
		
		return false;
	}

	/*******************************************
	 * Helpful stuff
	 ******************************************/
	
	/**
	 * 
	 * @return True if it's white player's turn
	 */
	public boolean isWhiteNext() {
		return nextPlayer == whitePlayer;
	}
	
	/**
	 * Finish game after regular move (save winner, move game to history etc.)
	 * 
	 * @param player
	 * @return
	 */
	public boolean finish(Player player) {
		// this method is public only for test coverage
		if (started && !finished) {
			player.setWinner();
			finished = true;
			whitePlayer.finishGame();
			blackPlayer.finishGame();
			
			return true;
		}
		return false;
	}

	public boolean didWhiteDraw() {
		return whitePlayer.requestedDraw();
	}

	public boolean didBlackDraw() {
		return blackPlayer.requestedDraw();
	}

	public boolean whiteGaveUp() {
		return whitePlayer.surrendered();
	}

	public boolean blackGaveUp() {
		return blackPlayer.surrendered();
	}

	/*******************************************
	 * !!!!!!!!! To be implemented !!!!!!!!!!!!
	 ******************************************/
	
	@Override
	public void setState(String state) {
		System.out.println("state: "+state);
		this.state = state;
	}
	
	@Override
	public String getState() {
		return state;
	}
	
	@Override
	public boolean tryMove(String moveString, Player player) {
		// TODO: implement
		//editor: Georg Stahn
		// hint: see javadoc comment in super class(Game):
			/**
			 * This method checks if the supplied move is possible to perform 
			 * in the current game state/status and, if so, does it.
			 * The following has to be checked/might be changed:
			 * 1- it has to be checked if the move can be performed
			 * 2---- it is a valid move
			 * 3---- it is done by the right player
			 * 4---- there is no other move that the player is forced to perform
			 * 5- if the move can be performed, the following has to be done:
			 * 6---- the board state has to be updated (e.g. figures moved/deleted)
			 * 7---- the board status has to be updated (check if game is finished)
			 * 8---- the next player has to be set (if move is over, it's next player's turn)
			 * 
			 * @param move String representation of move
			 * @param player The player that tries the move
			 * @return true if the move was performed
			 */
		System.out.println("Am Anfang");
		String[][] board = NewMethods2.fenToArray(state);
		System.out.println("Nach fenToArray");
		board = NewMethods2.catchMove(board, moveString, "w");
		System.out.println("nach catchMove");
		String fenString = NewMethods2.arrayToString(board);
		System.out.println("nach arraytotring");
		setState(fenString);
		System.out.println("neuer fenString: " + fenString);
		return true;
	}
}