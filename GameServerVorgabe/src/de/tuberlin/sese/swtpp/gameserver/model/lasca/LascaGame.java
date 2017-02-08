package de.tuberlin.sese.swtpp.gameserver.model.lasca;

import java.io.Serializable;

import de.tuberlin.sese.swtpp.gameserver.model.Game;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

/**

    Class LascaGame extends the abstract class Game as a concrete game instance that allows to play
    Lasca (http://www.lasca.org/).

*/
public class LascaGame extends Game implements Serializable{
//TEST

private static final long serialVersionUID = 8461983069685628324L;

/************************
 * member
 ***********************/

// just for better comprehensibility of the code: assign white and black player
private Player blackPlayer;
private Player whitePlayer;

// internal representation of the game state
// TODO: insert additional game data here

// String state = "b,b,b,b/,,/w,w,w/,/,,/,,,/w,w,w/w,w,w,w";
String state = ",,,/b,b,b/b,b,b,b/b,b,b/,,,/w,w,w/w,w,w,w";
// String state = ",,,/w,wb,w/,,,/b,b,/b/,,,/b,bw,b/,,,";
String playersTurn = "w";
int x = 0;

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
	// TODO: implement

// if(x==1)state = "b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w";
this.state = state;
}

@Override
public String getState() {
	// TODO: implement
	return state;
}

@Override
public boolean tryMove(String moveString, Player player) {
	String[][] board = NewMethods2.fenToArray(state);
	if(player.equals(whitePlayer))this.playersTurn="w";
	if(player.equals(blackPlayer))this.playersTurn="b";
	
	if(NewMethods2.schlagenMuss(board,this.playersTurn)){//if player has to strike/catch
		board = NewMethods2.catchMove(board, moveString, this.playersTurn);//catchMove
 		String fenString = NewMethods2.arrayToString(board);
		if(this.state.equals(fenString))			
			return false;//if board has not changed
		else{ //if board has changed
			if(this.playersTurn=="w"){//if white must play
		 		if(!NewMethods2.schlagenMuss(board,"w")){//if white player can´t strike/catch
		 			System.out.println("IN W");
					board = NewMethods2.toOfficer(board, this.playersTurn);
			 		fenString = NewMethods2.arrayToString(board);
			 		setState(fenString);
		 			this.setNextPlayer(blackPlayer);//change to next player
		 			this.playersTurn="b";
		 			}
		 			return true;//if board has changed
			}
			else{//if black must play
		 		if(!NewMethods2.schlagenMuss(board,"b")){//if black player can´t strike/catch
		 			System.out.println("IN B");
					board = NewMethods2.toOfficer(board, this.playersTurn);
			 		fenString = NewMethods2.arrayToString(board);
			 		setState(fenString);
		 			this.setNextPlayer(whitePlayer);//change to next player
		 			this.playersTurn="w";
		 			}
		 			return true;//if board has changed
			}
				
			}
	}
	else
	{

board = NewMethods2.normalMove(moveString, board,this.playersTurn);//normalMove
board = NewMethods2.toOfficer(board, this.playersTurn);
String fenString = NewMethods2.arrayToString(board);
if(this.state.equals(fenString))return false;//if board has not changed
else{
if(this.playersTurn=="w"){
setState(fenString);
this.setNextPlayer(blackPlayer);
this.playersTurn="b";
// }
return true;//if board has changed
}
else{
setState(fenString);
this.setNextPlayer(whitePlayer);
this.playersTurn="w";
// }
return true;//if board has changed
}
}
}

}

}