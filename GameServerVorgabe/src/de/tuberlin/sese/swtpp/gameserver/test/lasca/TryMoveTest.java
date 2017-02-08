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
		startGame("b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w", true);
		assertMove("a3-b4", true, true);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/w,,/,w,w,w/w,w,w/w,w,w,w", false, false, false);
	}
/***TEST******************************************BEGIN****************************************************TEST****/
	//TODO: implement test cases of same kind as example here
//WHITE
	//FINISH
	//TRUE
		//normale Move
			//promotion
		//catch Move
			//promotion
			//double catch Move
				//promotion
	//FALSE
		//
//BLACK
	//FINISH
	//TRUE
			//normale Move
				//promotion
			//catch Move
				//promotion
				//double catch Move
					//promotion
	//FALSE
		//
/***TEST*******************************************END*****************************************************TEST***/
/*W*TEST*******************************************BEGIN***************************************************TEST*W*/
	//FINISH
	/*W*TEST*TRUE**************************************BEGIN**********************************************TEST*TRUE*W*/
		/*W*TEST*TRUE*NORMAL*MOVE**************************BEGIN**********************************TEST*TRUE*NORMAL*MOVE*W*/
	@Test
	public void testExample1() {//normal move to the left w
		System.out.println("test1:");
		startGame("b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w", true);
		assertMove("c3-b4", true, true);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/w,,/w,,w,w/w,w,w/w,w,w,w", false, false, false);
	}
		/*W*TEST*TRUE*NORMAL*MOVE**************************END************************************TEST*TRUE*NORMAL*MOVE*W*/
		/*W*TEST*TRUE*NORMAL*MOVE*PROMOTION****************BEGIN************************TEST*TRUE*NORMAL*MOVE*PROMOTION*W*/
	@Test
	public void testExample10() {//10 officer normal move to the left w
		System.out.println("test10:");
		startGame("b,b,b,b/b,b,b/b,b,b,W/,,/b,w,w,w/w,w,w/w,w,w,w", true);
		assertMove("g5-f4", true, true);
		assertGameState("b,b,b,b/b,b,b/b,b,b,/,,W/b,w,w,w/w,w,w/w,w,w,w", false, false, false);
	}
	@Test
	public void testExample11() {//11 officer normal move to the right w
		System.out.println("test11:");
		startGame("b,b,b,b/b,b,b/b,b,W,b/,,/b,w,w,w/w,w,w/w,w,w,w", true);
		assertMove("e5-f4", true, true);
		assertGameState("b,b,b,b/b,b,b/b,b,,b/,,W/b,w,w,w/w,w,w/w,w,w,w", false, false, false);
	}
	@Test
	public void testExample12() {//12 normal move to officer
		System.out.println("test12:");
		startGame("b,b,,b/,wbb,/,bw,b,b/,bw,/w,w,wb,/w,,w/w,,w,Bw", true);
		assertMove("d6-e7", true, true);
		assertGameState("b,b,Wbb,b/,,/,bw,b,b/,bw,/w,w,wb,/w,,w/w,,w,Bw", false, false, false);
	}
		/*W*TEST*TRUE*NORMAL*MOVE*PROMOTION****************END************************************TEST*TRUE*NORMAL*MOVE*W*/
		/*W*TEST*TRUE*CATCH*MOVE***************************BEGIN*************************TEST*TRUE*CATCH*MOVE***********W*/
		/*W*TEST*TRUE*CATCH*MOVE***************************END***************************TEST*TRUE*CATCH*MOVE***********W*/
		/*W*TEST*TRUE*CATCH*MOVE*PROMOTION*****************BEGIN*************************TEST*TRUE*CATCH*MOVE*PROMOTION*W*/
		/*W*TEST*TRUE*CATCH*MOVE*PROMOTION*****************END***************************TEST*TRUE*CATCH*MOVE*PROMOTION*W*/
		/*W*TEST*TRUE*DOUBLE*CATCH*MOVE***************************BEGIN***********TEST*TRUE*DOUBLE*CATCH*MOVE***********W*/
	@Test
	public void testExample30_0() {//30 catch move w 0
		System.out.println("test30,0:");
		startGame("b,b,b,b/b,b,/b,b,b,/wb,,/w,w,bw,w/w,,w/w,w,w,w", true);
		assertMove("f2-d4", true, true);
		assertGameState("b,b,b,b/b,b,/b,b,b,/wb,wb,/w,w,w,w/w,,/w,w,w,w", true, false, false);
	}
	@Test
	public void testExample30_1() {//30 catch move w 1
		System.out.println("test30,1:");
		startGame("b,b,b,b/b,b,/b,b,b,/wb,wb,/w,w,w,w/w,,/w,w,w,w", true);
		assertMove("d4-f6", true, true);
		assertGameState("b,b,b,b/b,b,wbb/b,b,,/wb,,/w,w,w,w/w,,/w,w,w,w", false, false, false);
	}
	@Test
	public void testExample31_0() {//31 officer catch move w 0
		System.out.println("test31,0:");
		startGame("b,b,,/b,b,bb/,,bw,b/b,,wb/w,wb,w,w/ww,,w/,w,,w", true);
		assertMove("a3-c5", true, true);
		assertGameState("b,b,,/b,b,bb/,wb,bw,b/,,wb/,wb,w,w/ww,,w/,w,,w", true, false, false);
	}
	@Test
	public void testExample31_1() {//31 officer catch move w 1
		System.out.println("test31,1:");
		startGame("b,b,,/b,b,bb/,wb,bw,b/,,wb/,wb,w,w/ww,,w/,w,,w", true);
		assertMove("c5-e7", true, true);
		assertGameState("b,b,Wbb,/b,,bb/,,bw,b/,,wb/,wb,w,w/ww,,w/,w,,w", false, false, false);
	}
		/*W*TEST*TRUE*DOUBLE*CATCH*MOVE***************************END*************TEST*TRUE*DOUBLE*CATCH*MOVE***********W*/
		/*W*TEST*TRUE*DOUBLE*CATCH*MOVE*PROMOTION*****************BEGIN***********TEST*TRUE*DOUBLE*CATCH*MOVE*PROMOTION*W*/
		/*W*TEST*TRUE*DOUBLE*CATCH*MOVE*PROMOTION*****************END*************TEST*TRUE*DOUBLE*CATCH*MOVE*PROMOTION*W*/
	/*W*TEST*TRUE**************************************END************************************************TEST*TURE*W*/
	/*W*TEST*FALSE**************************************BEGIN**********************************************TEST*TURE*W*/
		/*W*TEST*FALSE**************************************BEGIN**********************************************TEST*FALSE*W*/
		/*W*TEST*FALSE*NORMAL*MOVE**************************BEGIN**********************************TEST*FALSE*NORMAL*MOVE*W*/
		/*W*TEST*FALSE*NORMAL*MOVE**************************END************************************TEST*FALSE*NORMAL*MOVE*W*/
		/*W*TEST*FALSE*NORMAL*MOVE*PROMOTION****************BEGIN************************TEST*FALSE*NORMAL*MOVE*PROMOTION*W*/
		/*W*TEST*FALSE*NORMAL*MOVE*PROMOTION****************END************************************TEST*FALSE*NORMAL*MOVE*W*/
		/*W*TEST*FALSE*CATCH*MOVE***************************BEGIN*************************TEST*FALSE*CATCH*MOVE***********W*/
		/*W*TEST*FALSE*CATCH*MOVE***************************END***************************TEST*FALSE*CATCH*MOVE***********W*/
		/*W*TEST*FALSE*CATCH*MOVE*PROMOTION*****************BEGIN*************************TEST*FALSE*CATCH*MOVE*PROMOTION*W*/
		/*W*TEST*FALSE*CATCH*MOVE*PROMOTION*****************END***************************TEST*FALSE*CATCH*MOVE*PROMOTION*W*/
		/*W*TEST*FALSE*DOUBLE*CATCH*MOVE***************************BEGIN***********TEST*FALSE*DOUBLE*CATCH*MOVE***********W*/
		/*W*TEST*FALSE*DOUBLE*CATCH*MOVE***************************END*************TEST*FALSE*DOUBLE*CATCH*MOVE***********W*/
		/*W*TEST*FALSE*DOUBLE*CATCH*MOVE*PROMOTION*****************BEGIN***********TEST*FALSE*DOUBLE*CATCH*MOVE*PROMOTION*W*/
		/*W*TEST*FALSE*DOUBLE*CATCH*MOVE*PROMOTION*****************END*************TEST*FALSE*DOUBLE*CATCH*MOVE*PROMOTION*W*/
	/*W*TEST*FALSE**************************************END************************************************TEST*TURE*W*/
/*W*TEST*******************************************END*****************************************************TEST*W*/
	
/*B*TEST*******************************************BEGIN***************************************************TEST*B*/
	//FINISH
	/*B*TEST*TRUE**************************************BEGIN**********************************************TEST*TRUE*B*/
		/*B*TEST*TRUE*NORMAL*MOVE**************************BEGIN**********************************TEST*TRUE*NORMAL*MOVE*B*/
		/*B*TEST*TRUE*NORMAL*MOVE**************************END************************************TEST*TRUE*NORMAL*MOVE*B*/
		/*B*TEST*TRUE*NORMAL*MOVE*PROMOTION****************BEGIN************************TEST*TRUE*NORMAL*MOVE*PROMOTION*B*/
		/*B*TEST*TRUE*NORMAL*MOVE*PROMOTION****************END************************************TEST*TRUE*NORMAL*MOVE*B*/
		/*B*TEST*TRUE*CATCH*MOVE***************************BEGIN*************************TEST*TRUE*CATCH*MOVE***********B*/
		/*B*TEST*TRUE*CATCH*MOVE***************************END***************************TEST*TRUE*CATCH*MOVE***********B*/
		/*B*TEST*TRUE*CATCH*MOVE*PROMOTION*****************BEGIN*************************TEST*TRUE*CATCH*MOVE*PROMOTION*B*/
		/*B*TEST*TRUE*CATCH*MOVE*PROMOTION*****************END***************************TEST*TRUE*CATCH*MOVE*PROMOTION*B*/
		/*B*TEST*TRUE*DOUBLE*CATCH*MOVE***************************BEGIN***********TEST*TRUE*DOUBLE*CATCH*MOVE***********B*/
		/*B*TEST*TRUE*DOUBLE*CATCH*MOVE***************************END*************TEST*TRUE*DOUBLE*CATCH*MOVE***********B*/
		/*B*TEST*TRUE*DOUBLE*CATCH*MOVE*PROMOTION*****************BEGIN***********TEST*TRUE*DOUBLE*CATCH*MOVE*PROMOTION*B*/
		/*B*TEST*TRUE*DOUBLE*CATCH*MOVE*PROMOTION*****************END*************TEST*TRUE*DOUBLE*CATCH*MOVE*PROMOTION*B*/
	/*B*TEST*TRUE**************************************END************************************************TEST*TURE*B*/
	/*B*TEST*FALSE**************************************BEGIN**********************************************TEST*FALSE*B*/
		/*B*TEST*FALSE*NORMAL*MOVE**************************BEGIN**********************************TEST*FALSE*NORMAL*MOVE*B*/
		/*B*TEST*FALSE*NORMAL*MOVE**************************END************************************TEST*FALSE*NORMAL*MOVE*B*/
		/*B*TEST*FALSE*NORMAL*MOVE*PROMOTION****************BEGIN************************TEST*FALSE*NORMAL*MOVE*PROMOTION*B*/
		/*B*TEST*FALSE*NORMAL*MOVE*PROMOTION****************END************************************TEST*FALSE*NORMAL*MOVE*B*/
		/*B*TEST*FALSE*CATCH*MOVE***************************BEGIN*************************TEST*FALSE*CATCH*MOVE***********B*/
		/*B*TEST*FALSE*CATCH*MOVE***************************END***************************TEST*FALSE*CATCH*MOVE***********B*/
		/*B*TEST*FALSE*CATCH*MOVE*PROMOTION*****************BEGIN*************************TEST*FALSE*CATCH*MOVE*PROMOTION*B*/
		/*B*TEST*FALSE*CATCH*MOVE*PROMOTION*****************END***************************TEST*FALSE*CATCH*MOVE*PROMOTION*B*/
		/*B*TEST*FALSE*DOUBLE*CATCH*MOVE***************************BEGIN***********TEST*FALSE*DOUBLE*CATCH*MOVE***********B*/
		/*B*TEST*FALSE*DOUBLE*CATCH*MOVE***************************END*************TEST*FALSE*DOUBLE*CATCH*MOVE***********B*/
		/*B*TEST*FALSE*DOUBLE*CATCH*MOVE*PROMOTION*****************BEGIN***********TEST*FALSE*DOUBLE*CATCH*MOVE*PROMOTION*B*/
		/*B*TEST*FALSE*DOUBLE*CATCH*MOVE*PROMOTION*****************END*************TEST*FALSE*DOUBLE*CATCH*MOVE*PROMOTION*B*/
	/*B*TEST*FALSE**************************************END************************************************TEST*FALSE*B*/
/*B*TEST*******************************************END*****************************************************TEST*B*/

}
