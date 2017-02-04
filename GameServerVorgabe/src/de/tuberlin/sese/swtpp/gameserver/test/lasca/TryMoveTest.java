package de.tuberlin.sese.swtpp.gameserver.test.lasca;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.tuberlin.sese.swtpp.gameserver.control.GameController;
import de.tuberlin.sese.swtpp.gameserver.model.Player;
import de.tuberlin.sese.swtpp.gameserver.model.User;
import de.tuberlin.sese.swtpp.gameserver.model.lasca.LascaGame;

public class TryMoveTest {

	User user1 = new User("Alice", "alice");
	User user2 = new User("Bob", "bob");
	
	Player whitePlayer = null;
	Player blackPlayer = null;
	LascaGame game = null;
	GameController controller;
	
	@Before
	public void setUp() throws Exception {
		controller = GameController.getInstance();
		controller.clear();
		
		int gameID = controller.startGame(user1, "");
		
		game = (LascaGame) controller.getGame(gameID);
		whitePlayer = game.getPlayer(user1);

	}
	
	
	public void startGame(String initialBoard, boolean whiteNext) {
		controller.joinGame(user2);		
		blackPlayer = game.getPlayer(user2);
		
		game.setState(initialBoard);
		game.setNextPlayer(whiteNext? whitePlayer:blackPlayer);
	}
	
	public void assertMove(String move, boolean white, boolean expectedResult) {
		if (white)
			assertEquals(game.tryMove(move, whitePlayer), expectedResult);
		else 
			assertEquals(game.tryMove(move, blackPlayer), expectedResult);
	}

	public void assertGameState(String expectedBoard, boolean whiteNext, boolean finished, boolean whiteWon) {
		assertEquals(game.getState(), expectedBoard);
		assertEquals(game.isWhiteNext(), whiteNext);

		assertEquals(game.isFinished(), finished);
		if (game.isFinished()) {
			assertEquals(whitePlayer.isWinner(), whiteWon);
			assertEquals(blackPlayer.isWinner(), !whiteWon);
		}
	}
	
	/*******************************************
	 * !!!!!!!!! To be implemented !!!!!!!!!!!!
	 *******************************************/
	
	@Test
	public void exampleTest() {//normal move to the right w
		startGame("b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w w", true);
		assertMove("a3-b4", true, true);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/w,,/,w,w,w/w,w,w/w,w,w,w b", false, false, false);
	}

	//TODO: implement test cases of same kind as example here
/*************************************************TRUE**********************************************************/
	///*
	@Test
	public void testExample1() {//normal move to the left w
		//startGame: String move, boolean white, boolean expectedResult
		//assertMove: String move, boolean white, boolean expectedResult
		//assertGameState: String expectedBoard, boolean whiteNext, boolean finished, boolean whiteWon
		System.err.println("test1:");
		startGame("b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w w", true);
		assertMove("c3-b4", true, true);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/w,,/w,,w,w/w,w,w/w,w,w,w b", false, false, false);
	}
	@Test
	public void testExample2_0() {//TODO 3 catch move w 0
		//startGame: String move, boolean white, boolean expectedResult
		//assertMove: String move, boolean white, boolean expectedResult
		//assertGameState: String expectedBoard, boolean whiteNext, boolean finished, boolean whiteWon
		startGame("b,b,,/b,b,bb/,,bw,b/b,,wb/w,wb,w,w/ww,,w/,w,,w w", true);
		assertMove("a3-c5", true, true);
		assertGameState("b,b,,/b,b,bb/,wb,bw,b/,,wb/,wb,w,w/ww,,w/,w,,w w", true, false, false);
	}
	@Test
	public void testExample2_1() {//TODO 3 catch move w 1
		//startGame: String move, boolean white, boolean expectedResult
		//assertMove: String move, boolean white, boolean expectedResult
		//assertGameState: String expectedBoard, boolean whiteNext, boolean finished, boolean whiteWon
		startGame("b,b,,/b,b,bb/,wb,bw,b/,,wb/,wb,w,w/ww,,w/,w,,w w", true);
		assertMove("c5-e7", true, true);
		assertGameState("b,b,Wbb,/b,,bb/,,bw,b/,,wb/,wb,w,w/ww,,w/,w,,w b", false, false, false);
	}
	@Test
	public void testExample3() {//TODO 3 officer normal move to the left w
		//startGame: String move, boolean white, boolean expectedResult
		//assertMove: String move, boolean white, boolean expectedResult
		//assertGameState: String expectedBoard, boolean whiteNext, boolean finished, boolean whiteWon
		startGame("b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w w", true);
		assertMove("c3-b4", true, true);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/w,,/w,,w,w/w,w,w/w,w,w,w b", false, false, false);
	}
	@Test
	public void testExample4() {//TODO 3 officer normal move to the right w
		//startGame: String move, boolean white, boolean expectedResult
		//assertMove: String move, boolean white, boolean expectedResult
		//assertGameState: String expectedBoard, boolean whiteNext, boolean finished, boolean whiteWon
		startGame("b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w w", true);
		assertMove("c3-b4", true, true);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/w,,/w,,w,w/w,w,w/w,w,w,w b", false, false, false);
	}
	@Test
	public void testExample5_0() {//TODO 3 officer catch move w 0
		//startGame: String move, boolean white, boolean expectedResult
		//assertMove: String move, boolean white, boolean expectedResult
		//assertGameState: String expectedBoard, boolean whiteNext, boolean finished, boolean whiteWon
		startGame("b,b,,/b,b,bb/,,bw,b/b,,wb/w,wb,w,w/ww,,w/,w,,w w", true);
		assertMove("a3-c5", true, true);
		assertGameState("b,b,,/b,b,bb/,wb,bw,b/,,wb/,wb,w,w/ww,,w/,w,,w w", true, false, false);
	}
	@Test
	public void testExample5_1() {//TODO 3 officer catch move w 1
		//startGame: String move, boolean white, boolean expectedResult
		//assertMove: String move, boolean white, boolean expectedResult
		//assertGameState: String expectedBoard, boolean whiteNext, boolean finished, boolean whiteWon
		startGame("b,b,,/b,b,bb/,wb,bw,b/,,wb/,wb,w,w/ww,,w/,w,,w w", true);
		assertMove("c5-e7", true, true);
		assertGameState("b,b,Wbb,/b,,bb/,,bw,b/,,wb/,wb,w,w/ww,,w/,w,,w b", false, false, false);
	}
	@Test
	public void testExample6() {//TODO 3 normal move to officer
		//startGame: String move, boolean white, boolean expectedResult
		//assertMove: String move, boolean white, boolean expectedResult
		//assertGameState: String expectedBoard, boolean whiteNext, boolean finished, boolean whiteWon
		startGame("b,b,,b/,wbb,/,bw,b,b/,bw,/w,w,wb,/w,,w/w,,w,Bw w", true);
		assertMove("d6-e7", true, true);
		assertGameState("b,b,Wbb,b/,,/,bw,b,b/,bw,/w,w,wb,/w,,w/w,,w,Bw b", false, false, false);
	}
	//*/
}
