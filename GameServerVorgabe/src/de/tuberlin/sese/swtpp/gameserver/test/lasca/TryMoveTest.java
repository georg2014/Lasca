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
		assertMove("d4-f6", true, true);
		assertGameState("b,b,b,b/b,b,wbb/b,b,,/wb,,/w,w,w,w/w,,/w,w,w,w", false, false, false);
	}
	@Test
	public void testExample31_0() {//31 officer catch move w 0
		System.out.println("test31,0:");
		startGame("b,b,,/b,b,bb/,,bw,b/b,,wb/w,wb,w,w/ww,,w/,w,,w", true);
		assertMove("a3-c5", true, true);
		assertGameState("b,b,,/b,b,bb/,wb,bw,b/,,wb/,wb,w,w/ww,,w/,w,,w", true, false, false);
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
		/*B*TEST*FALSE**************************************END************************************************TEST*FALSE*B*/
/*B*TEST*******************************************END*****************************************************TEST*B*/
	/**
	 * SchlagenMussRauf mit w und W
	 */
	@Test
	public void testSchlagenMuss17(){
		System.out.println("testSchlagenMuss17");
		startGame(",,,b/b,,/w,,,/,,/,,,/,,/,,,",true);
		assertMove("a5-c7",true,true);
		assertGameState(",Wb,,b/,,/,,,/,,/,,,/,,/,,,",false,false,false);		
	}
	
	@Test
	public void testSchlagenMuss16(){
		startGame(",W,,/b,,/w,,,/,,/,,,/,,/,,,",true);
		assertMove("a5-c7",true,false);
		assertGameState(",W,,/b,,/w,,,/,,/,,,/,,/,,,",true,false,false);		
	}
	
	@Test
	public void testSchlagenMuss19(){
		startGame(",,,b/b,,/W,,,/,,/,,,/,,/,,,",true);
		assertMove("a5-c7",true,true);
		assertGameState(",Wb,,b/,,/,,,/,,/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss18(){
		System.out.println("18");
		startGame(",,,/,,/b,,,/,,/,,,/,,/,,,",true);
		assertMove("a3-c5",true,false);
		assertGameState(",,,/,,/b,,,/,,/,,,/,,/,,,",true,false,false);		
	}	
	@Test
	public void testSchlagenMuss20(){
		startGame("b,,,W/,,b/,,,/,,/,,,/,,/,,,",true);
		assertMove("g7-e5",true,true);
		assertGameState("b,,,/,,/,,Wb,/,,/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss21(){
		startGame(",,,/,,/,b,,/,w,/,,,/,,/,,,",true);
		assertMove("d4-b6",true,true);
		assertGameState(",,,/wb,,/,,,/,,/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss22(){
		startGame(",,,/,,/,b,,/w,,/,,,/,,/,,,",true);
		assertMove("b4-d6",true,true);
		assertGameState(",,,/,wb,/,,,/,,/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss23(){
		startGame(",,,/,,/,,b,/,w,/,,,/,,/,,,",true);
		assertMove("d4-f6",true,true);
		assertGameState(",,,/,,wb/,,,/,,/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss24(){
		startGame(",,,/,,/,,,/,,/,,b,/,,w/,,,",true);
		assertMove("f2-d4",true,true);
		assertGameState(",,,/,,/,,,/,wb,/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss25(){
		startGame(",,,/,,/,,,/b,,/,w,,/,,/,,,",true);
		assertMove("c3-a5",true,true);
		assertGameState(",,,/,,/wb,,,/,,/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss26(){
		startGame(",,,/,,/,,,/,b,/,w,,/,,/,,,",true);
		assertMove("c3-e5",true,true);
		assertGameState(",,,/,,/,,wb,/,,/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss27(){
		startGame("W,W,W,W/,,/,,,/,,b/,,w,/,,/,,,",true);
		assertMove("e3-g5",true,true);
		assertGameState("W,W,W,W/,,/,,,wb/,,/,,,/,,/,,,",false,false,false);		
	}

	/**
	 * SchlagenMussRunter mit W
	 */	
	@Test
	public void testSchlagenMuss28(){
		startGame(",,,/,,/W,,,/b,,/,,,/,,/,,,",true);
		assertMove("a5-c3",true,true);
		assertGameState(",,,/,,/,,,/,,/,Wb,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss29(){
		startGame(",,,/,,/,,,/,,/,W,,/b,,/,,,",true);
		assertMove("c3-a1",true,true);
		assertGameState(",,,/,,/,,,/,,/,,,/,,/Wb,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss30(){
		startGame(",,,/,,/,,,/,,/,W,,/,b,/,,,",true);
		assertMove("c3-e1",true,true);
		assertGameState(",,,/,,/,,,/,,/,,,/,,/,,Wb,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss31(){
		startGame(",,,/,,/,,W,/,b,/,,,/,,/,,,",true);
		assertMove("e5-c3",true,true);
		assertGameState(",,,/,,/,,,/,,/,Wb,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss32(){
		startGame(",,,/W,,/,b,,/,,/,,,/,,/,,,",true);
		assertMove("b6-d4",true,true);
		assertGameState(",,,/,,/,,,/,Wb,/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss33(){
		startGame(",,,/,W,/,b,,/,,/,,,/,,/,,,",true);
		assertMove("d6-b4",true,true);
		assertGameState(",,,/,,/,,,/Wb,,/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss34(){
		startGame(",,,/,W,/,,b,/,,/,,,/,,/,,,",true);
		assertMove("d6-f4",true,true);
		assertGameState(",,,/,,/,,,/,,Wb/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss35(){
		startGame(",,,/,,W/,,b,/,,/,,,/,,/,,,",true);
		assertMove("f6-d4",true,true);
		assertGameState(",,,/,,/,,,/,Wb,/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss36(){
		startGame(",,,/,W,/,,b,/,,/,,,/W,W,W/W,W,W,W",true);
		assertMove("d6-f4",true,true);
		assertGameState(",,,/,,/,,,/,,Wb/,,,/W,W,W/W,W,W,W",false,false,false);		
	}
	@Test
	public void testSchlagenMuss37(){
		startGame(",,,/,,/,,W,/,,b/,,,/,,/,,,",true);
		assertMove("e5-g3",true,true);
		assertGameState(",,,/,,/,,,/,,/,,,Wb/,,/,,,",false,false,false);		
	}

	/**
	 * SchlagenMussRunter mit b und B
	 */
	@Test
	public void testSchlagenMuss38(){
		startGame(",,,/,,/b,,,/w,,/,,,/,,/,,,",false);
		assertMove("a5-c3",false,true);
		assertGameState(",,,/,,/,,,/,,/,bw,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMuss39(){
		startGame(",,,/,,/,,,/,,/,b,,/w,,/,,,",false);
		assertMove("c3-a1",false,true);
		assertGameState(",,,/,,/,,,/,,/,,,/,,/Bw,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMuss40(){
		startGame(",,,/,,/,,,/,,/,b,,/,w,/,,,",false);
		assertMove("c3-e1",false,true);
		assertGameState(",,,/,,/,,,/,,/,,,/,,/,,Bw,",true,false,false);		
	}	
	@Test
	public void testSchlagenMuss41(){
		startGame(",,,/,,/,,b,/,w,/,,,/,,/,,,",false);
		assertMove("e5-c3",false,true);
		assertGameState(",,,/,,/,,,/,,/,bw,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMuss42(){
		startGame(",,,/b,,/,w,,/,,/,,,/,,/,,,",false);
		assertMove("b6-d4",false,true);
		assertGameState(",,,/,,/,,,/,bw,/,,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMuss43(){
		startGame(",,,/,b,/,w,,/,,/,,,/,,/,,,",false);
		assertMove("d6-b4",false,true);
		assertGameState(",,,/,,/,,,/bw,,/,,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMuss44(){
		startGame(",,,/,b,/,,w,/,,/,,,/,,/,,,",false);
		assertMove("d6-f4",false,true);
		assertGameState(",,,/,,/,,,/,,bw/,,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMuss45(){
		startGame(",,,/,,b/,,w,/,,/,,,/,,/,,,",false);
		assertMove("f6-d4",false,true);
		assertGameState(",,,/,,/,,,/,bw,/,,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMuss46(){
		startGame(",,,/,b,/,,w,/,,/,,,/b,b,b/B,B,B,B",false);
		assertMove("d6-f4",false,true);
		assertGameState(",,,/,,/,,,/,,bw/,,,/b,b,b/B,B,B,B",true,false,false);		
	}
	@Test
	public void testSchlagenMuss47(){
		startGame(",,,/,,/,,b,/,,w/,,,/,,/,,,",false);
		assertMove("e5-g3",false,true);
		assertGameState(",,,/,,/,,,/,,/,,,bw/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMuss48(){
		startGame(",,,/,,/,,,/,,/,,,b/,,w/,,,",false);
		assertMove("g3-e1",false,true);
		assertGameState(",,,/,,/,,,/,,/,,,/,,/,,Bw,",true,false,false);		
	}
	/**
	 * SchlagenMussHoch mit B
	 */
	@Test
	public void testSchlagenMuss50(){
		startGame(",,,/,,/,w,,/,B,/,,,/,,/,,,",false);
		assertMove("d4-b6",false,true);
		assertGameState(",,,/Bw,,/,,,/,,/,,,/,,/,,,",true,false,false);		
	}


	@Test
	public void testSchlagenMuss51(){
		startGame(",,,/,,/,w,,/B,,/,,,/,,/,,,",false);
		assertMove("b4-d6",false,true);
		assertGameState(",,,/,Bw,/,,,/,,/,,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMuss52(){
		startGame(",,,/,,/,,w,/,B,/,,,/,,/,,,",false);
		assertMove("d4-f6",false,true);
		assertGameState(",,,/,,Bw/,,,/,,/,,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMuss53(){
		startGame(",,,/,,/,,,/,,/,,w,/,,B/,,,",false);
		assertMove("f2-d4",false,true);
		assertGameState(",,,/,,/,,,/,Bw,/,,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMuss54(){
		startGame(",,,/,,/,,,/w,,/,B,,/,,/,,,",false);
		assertMove("c3-a5",false,true);
		assertGameState(",,,/,,/Bw,,,/,,/,,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMuss55(){
		startGame(",,,/,,/,,,/,w,/,B,,/,,/,,,",false);
		assertMove("c3-e5",false,true);
		assertGameState(",,,/,,/,,Bw,/,,/,,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMuss56(){
		startGame("B,B,B,B/,,/,,,/,,w/,,B,/,,/,,,",false);
		assertMove("e3-g5",false,true);
		assertGameState("B,B,B,B/,,/,,,Bw/,,/,,,/,,/,,,",true,false,false);		
	}

	/**
	 * Ziel beim Schlagen ist besetzt
	 */

	@Test
	public void testSchlagenMuss57(){
		startGame(",,,b/,,b/,,w,/,,/,,,/,,/,,,",true);
		assertMove("e5-g7",true,false);
		assertGameState(",,,b/,,b/,,w,/,,/,,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMuss58(){
		startGame(",b,,/,w,/,,w,/,,/,,,/,,/,,,",true);
		assertMove("e5-c7",true,false);
		assertGameState(",b,,/,w,/,,w,/,,/,,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMuss59(){
		startGame(",,,/,b,/,,w,/,,/,,,/,,/,,,",true);
		assertMove("e5-c7",true,true);
		assertGameState(",Wb,,/,,/,,,/,,/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss60(){
		startGame("b,,,/b,,/,w,,/,,/,,,/,,/,,,",true);
		assertMove("c5-a7",true,false);
		assertGameState("b,,,/b,,/,w,,/,,/,,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMuss61(){
		startGame(",,b,/,b,/,w,,/,,/,,,/,,/,,,",true);
		assertMove("c5-e7",true,false);
		assertGameState(",,b,/,b,/,w,,/,,/,,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMuss62(){
		startGame(",,,/,,b/,,,w/,,/,,,/,,/,,,",true);
		assertMove("g5-e7",true,true);
		assertGameState(",,Wb,/,,/,,,/,,/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss63(){
		startGame(",,,/,,/,,,/,b,/,b,,/w,,/,,,",true);
		assertMove("b2-d4",true,false);
		assertGameState(",,,/,,/,,,/,b,/,b,,/w,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMuss64(){
		startGame(",,,/,,/,,,/,,/,w,,/w,,/,,,",true);//white next - white is on its turn
		assertMove("b2-d4",true,false);
		assertGameState(",,,/,,/,,,/,,/,w,,/w,,/,,,",true,false,false);
	}
	@Test
	public void testSchlagenMuss65(){
		startGame("b,,,/,,/,w,,/,w,/,,,/,,/,,,",true);
		assertMove("d4-b6",true,false);
		assertGameState("b,,,/,,/,w,,/,w,/,,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMuss66(){
		startGame(",,,/w,,/,w,,/,w,/,,,/,,/,,,",true);
		assertMove("d4-b6",true,false);
		assertGameState(",,,/w,,/,w,,/,w,/,,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMuss67(){
		startGame(",,,/,,/,,,/,b,/,,b,/,,w/,,,",true);
		assertMove("f2-d4",true,false);
		assertGameState(",,,/,,/,,,/,b,/,,b,/,,w/,,,",true,false,false);		
	}

	/**
	 * SchlagenMussRunter mit W -> Zusatz
	 */
	@Test
	public void testSchlagenMuss68(){
		startGame("W,,,/b,,/,b,,/,,/,,,/,,/,,,",true);
		assertMove("a7-c5",true,false);
		assertGameState("W,,,/b,,/,b,,/,,/,,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMuss69(){
		startGame("W,,,b/,,/,,,/,,/,,,/,,/,,,",true);
		assertMove("a7-c5",true,false);
		assertGameState("W,,,b/,,/,,,/,,/,,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMuss78(){
		startGame("W,,,/w,,/,,,/,,b/,,,/,,/,,,",true);
		assertMove("a7-c5",true,false);
		assertGameState("W,,,/w,,/,,,/,,b/,,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMus70(){
		startGame(",,,/,,/,W,,/b,,/b,,,/,,/,,,",true);
		assertMove("c5-a3",true,false);
		assertGameState(",,,/,,/,W,,/b,,/b,,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMus71(){
		startGame(",,,/,,/,W,,/w,,/,,,/,,/,,,",true);
		assertMove("c5-a3",true,false);
		assertGameState(",,,/,,/,W,,/w,,/,,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMus72(){
		startGame(",,,/,,/,W,,/,b,/,,b,/,,/,,,",true);
		assertMove("c5-e3",true,false);
		assertGameState(",,,/,,/,W,,/,b,/,,b,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMus73(){
		startGame(",,,/,,/,W,,/,w,/,,,/,,/,,,",true);
		assertMove("c5-e3",true,false);
		assertGameState(",,,/,,/,W,,/,w,/,,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMus74(){
		startGame(",,,/,,/,,W,/,b,/,b,,/,,/,,,",true);
		assertMove("e5-c3",true,false);
		assertGameState(",,,/,,/,,W,/,b,/,b,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMus75(){
		startGame(",,,/,,/,,W,/,w,/,,,/,,/,,,",true);
		assertMove("e5-c3",true,false);
		assertGameState(",,,/,,/,,W,/,w,/,,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMus76(){
		startGame(",,,/,,/,,W,/,,b/,,,b/,,/,,,",true);
		assertMove("e5-g3",true,false);
		assertGameState(",,,/,,/,,W,/,,b/,,,b/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMus77(){
		startGame(",,,/,,/,,W,/,,w/,,,/,,/,,,",true);
		assertMove("e5-g3",true,false);
		assertGameState(",,,/,,/,,W,/,,w/,,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMus79(){
		startGame(",,,/,,/,,,W/,,b/,,b,/,,/,,,",true);
		assertMove("g5-e3",true,false);
		assertGameState(",,,/,,/,,,W/,,b/,,b,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMus80(){
		startGame(",,,/,,/,,,W/,,w/,,,/,,/,,,",true);
		assertMove("g5-e3",true,false);
		assertGameState(",,,/,,/,,,W/,,w/,,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMus81(){
		startGame(",,,/,,/,,,/W,,/,b,,/,b,/,,,",true);
		assertMove("b4-d2",true,false);
		assertGameState(",,,/,,/,,,/W,,/,b,,/,b,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMus82(){
		startGame(",,,/,,/,,,/W,,/,w,,/,,/,,,",true);
		assertMove("b4-d2",true,false);
		assertGameState(",,,/,,/,,,/W,,/,w,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMus83(){
		startGame(",,,/,,/,,,/,W,/,,b,/,,b/,,,",true);
		assertMove("d4-f2",true,false);
		assertGameState(",,,/,,/,,,/,W,/,,b,/,,b/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMus84(){
		startGame(",,,/,,/,,,/,W,/,,w,/,,/,,,",true);
		assertMove("d4-f2",true,false);
		assertGameState(",,,/,,/,,,/,W,/,,w,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMus85(){
		startGame(",,,/,,/,,,/,W,/,b,,/b,,/,,,",true);
		assertMove("d4-b2",true,false);
		assertGameState(",,,/,,/,,,/,W,/,b,,/b,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMus86(){
		startGame(",,,/,,/,,,/,W,/,w,,/,,/,,,",true);
		assertMove("d4-b2",true,false);
		assertGameState(",,,/,,/,,,/,W,/,w,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMus87(){
		startGame(",,,/,,/,,,/,,W/,,w,/,,/,,,",true);
		assertMove("f4-d2",true,false);
		assertGameState(",,,/,,/,,,/,,W/,,w,/,,/,,,",true,false,false);		
	}





	/**
	 * SchlagenMussRunter mit b und B -> Zusatz
	 */
	@Test
	public void testSchlagenMuss88(){
		startGame("B,,,/b,,/,b,,/,,/,,,/,,/,,,w",false);
		assertMove("a7-c5",false,false);
		assertGameState("B,,,/b,,/,b,,/,,/,,,/,,/,,,w",false,false,false);		
	}
	@Test
	public void testSchlagenMuss89(){
		startGame("B,,,/,,/,,,/,,/,,,/,,/,,,w",false);
		assertMove("a7-c5",false,false);
		assertGameState("B,,,/,,/,,,/,,/,,,/,,/,,,w",false,false,false);		
	}
	@Test
	public void testSchlagenMuss90(){
		startGame("B,,,/b,,/,,,/,,/,,,/,,/,,,w",false);
		assertMove("a7-c5",false,false);
		assertGameState("B,,,/b,,/,,,/,,/,,,/,,/,,,w",false,false,false);		
	}
	@Test
	public void testSchlagenMus91(){
		startGame(",,,/,,/,B,,/w,,/w,,,/,,/,,,",false);
		assertMove("c5-a3",false,false);
		assertGameState(",,,/,,/,B,,/w,,/w,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMus92(){
		startGame(",,,/,,/,B,,/b,,/,,,/,,/,,,",false);
		assertMove("c5-a3",false,false);
		assertGameState(",,,/,,/,B,,/b,,/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMus93(){
		startGame(",,,/,,/,B,,/,w,/,,w,/,,/,,,",false);
		assertMove("c5-e3",false,false);
		assertGameState(",,,/,,/,B,,/,w,/,,w,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMus94(){
		startGame(",,,/,,/,B,,/,b,/,,,/,,/,,,",false);
		assertMove("c5-e3",false,false);
		assertGameState(",,,/,,/,B,,/,b,/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMus95(){
		startGame(",,,/,,/,,B,/,w,/,w,,/,,/,,,",false);
		assertMove("e5-c3",false,false);
		assertGameState(",,,/,,/,,B,/,w,/,w,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMus96(){
		startGame(",,,/,,/,,B,/,b,/,,,/,,/,,,",false);
		assertMove("e5-c3",false,false);
		assertGameState(",,,/,,/,,B,/,b,/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMus97(){
		startGame(",,,/,,/,,B,/,,w/,,,w/,,/,,,",false);
		assertMove("e5-g3",false,false);
		assertGameState(",,,/,,/,,B,/,,w/,,,w/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMus98(){
		startGame(",,,/,,/,,B,/,,b/,,,/,,/,,,",false);
		assertMove("e5-g3",false,false);
		assertGameState(",,,/,,/,,B,/,,b/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMus99(){
		startGame(",,,/,,/,,,B/,,w/,,w,/,,/,,,",false);
		assertMove("g5-e3",false,false);
		assertGameState(",,,/,,/,,,B/,,w/,,w,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMus100(){
		startGame(",,,/,,/,,,B/,,b/,,,/,,/,,,",false);
		assertMove("g5-e3",false,false);
		assertGameState(",,,/,,/,,,B/,,b/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMus101(){
		startGame(",,,/,,/,,,/B,,/,w,,/,w,/,,,",false);
		assertMove("b4-d2",false,false);
		assertGameState(",,,/,,/,,,/B,,/,w,,/,w,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMus102(){
		startGame(",,,/,,/,,,/B,,/,b,,/,,/,,,",false);
		assertMove("b4-d2",false,false);
		assertGameState(",,,/,,/,,,/B,,/,b,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMus103(){
		startGame(",,,/,,/,,,/,B,/,,w,/,,w/,,,",false);
		assertMove("d4-f2",false,false);
		assertGameState(",,,/,,/,,,/,B,/,,w,/,,w/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMus104(){
		startGame(",,,/,,/,,,/,B,/,,b,/,,/,,,",false);
		assertMove("d4-f2",false,false);
		assertGameState(",,,/,,/,,,/,B,/,,b,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMus105(){
		startGame(",,,/,,/,,,/,B,/,w,,/w,,/,,,",false);
		assertMove("d4-b2",false,false);
		assertGameState(",,,/,,/,,,/,B,/,w,,/w,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMus106(){
		startGame(",,,/,,/,,,/,B,/,b,,/,,/,,,",false);
		assertMove("d4-b2",false,false);
		assertGameState(",,,/,,/,,,/,B,/,b,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMus107(){
		startGame(",,,/,,/,,,/,,B/,,b,/,,/,,,",false);
		assertMove("f4-d2",false,false);
		assertGameState(",,,/,,/,,,/,,B/,,b,/,,/,,,",false,false,false);		
	}
	/**
	 * SchlagenMussHoch mit B -> Zusatz
	 */
	@Test
	public void testSchlagenMuss108(){
		startGame(",,,w/,,w/,,B,/,,/,,,/,,/,,,",false);
		assertMove("e5-g7",false,false);
		assertGameState(",,,w/,,w/,,B,/,,/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss110(){
		startGame(",,,/,w,/,,B,/,,/,,,/,,/,,,",false);
		assertMove("e5-c7",false,true);
		assertGameState(",Bw,,/,,/,,,/,,/,,,/,,/,,,",true,false,false);		
	}
	@Test
	public void testSchlagenMuss111(){
		startGame("w,,,/w,,/,B,,/,,/,,,/,,/,,,",false);
		assertMove("c5-a7",false,false);
		assertGameState("w,,,/w,,/,B,,/,,/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss112(){
		startGame(",,w,/,w,/,B,,/,,/,,,/,,/,,,",false);
		assertMove("c5-e7",false,false);
		assertGameState(",,w,/,w,/,B,,/,,/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss113(){
		startGame(",,,/,,w/,,,B/,,/,,,/,,/,,,w",false);
		assertMove("g5-e7",false,true);
		assertGameState(",,Bw,/,,/,,,/,,/,,,/,,/,,,w",true,false,false);		
	}
	@Test
	public void testSchlagenMuss114(){
		startGame(",,,/,,/,,,/,w,/,w,,/B,,/,,,",false);
		assertMove("b2-d4",false,false);
		assertGameState(",,,/,,/,,,/,w,/,w,,/B,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss115(){
		startGame(",,,/,,/,,,/,,/,b,,/B,,/,,,w",false);
		assertMove("b2-d4",false,false);
		assertGameState(",,,/,,/,,,/,,/,b,,/B,,/,,,w",false,false,false);		
	}
	@Test
	public void testSchlagenMuss116(){
		startGame(",,,/,,/,b,,/,B,/,,,/,,/,,,",false);
		assertMove("d4-b6",false,false);
		assertGameState(",,,/,,/,b,,/,B,/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss117(){
		startGame(",,,/b,,/,b,,/,B,/,,,/,,/,,,",false);
		assertMove("d4-b6",false,false);
		assertGameState(",,,/b,,/,b,,/,B,/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss118(){
		startGame(",,,/,,/,,,/,w,/,,w,/,,B/,,,",false);
		assertMove("f2-d4",false,false);
		assertGameState(",,,/,,/,,,/,w,/,,w,/,,B/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss119(){
		startGame(",b,,/b,,/B,,,/,,/,,,/,,/,,,",false);
		assertMove("a5-c7",false,false);
		assertGameState(",b,,/b,,/B,,,/,,/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss120(){
		startGame(",,,/w,,/B,,,/,,/,,,/,,/,,,w",false);
		assertMove("a5-c7",false,true);
		assertGameState(",Bw,,/,,/,,,/,,/,,,/,,/,,,w",true,false,false);
	}
	@Test
	public void testSchlagenMuss121(){
		startGame(",b,,/,b,/,,B,/,,/,,,/,,/,,,",false);
		assertMove("e5-c7",false,false);
		assertGameState(",b,,/,b,/,,B,/,,/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss122(){
		startGame(",,,/,,b/,,b,/,B,/,,,/,,/,,,",false);
		assertMove("d4-f6",false,false);
		assertGameState(",,,/,,b/,,b,/,B,/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss123(){
		startGame(",,,/,,/,,b,/,B,/,,,/,,/,,,",false);
		assertMove("d4-f6",false,false);
		assertGameState(",,,/,,/,,b,/,B,/,,,/,,/,,,",false,false,false);		
	}
	@Test
	public void testSchlagenMuss124(){
		startGame(",,,/,,/,,b,/,,B/,,,/,,/,,,w",false);
		assertMove("f4-d6",false,false);
		assertGameState(",,,/,,/,,b,/,,B/,,,/,,/,,,w",false,false,false);		
	}
	@Test
	public void testLagestEndMove(){
		//http://www.lasca.org/show?6JS
		startGame(",,,BBww/WB,WB,WB/,,,/WB,WB,WB/,,,/WB,WB,WB/,,,",false);
		assertMove("g7-e5",false,true);
		assertGameState(",,,/WB,WB,B/,,BBwwW,/WB,WB,WB/,,,/WB,WB,WB/,,,",false,false,false);
		assertMove("e5-c7",false,true);
		assertGameState(",BBwwWW,,/WB,B,B/,,,/WB,WB,WB/,,,/WB,WB,WB/,,,",false,false,false);
		assertMove("c7-a5",false,true);
		assertGameState(",,,/B,B,B/BBwwWWW,,,/WB,WB,WB/,,,/WB,WB,WB/,,,",false,false,false);
		assertMove("a5-c3",false,true);
		assertGameState(",,,/B,B,B/,,,/B,WB,WB/,BBwwWWWW,,/WB,WB,WB/,,,",false,false,false);
		assertMove("c3-e5",false,true);
		assertGameState(",,,/B,B,B/,,BBwwWWWWW,/B,B,WB/,,,/WB,WB,WB/,,,",false,false,false);
		assertMove("e5-g3",false,true);
		assertGameState(",,,/B,B,B/,,,/B,B,B/,,,BBwwWWWWWW/WB,WB,WB/,,,",false,false,false);
		assertMove("g3-e1",false,true);
		assertGameState(",,,/B,B,B/,,,/B,B,B/,,,/WB,WB,B/,,BBwwWWWWWWW,",false,false,false);
		assertMove("e1-c3",false,true);
		assertGameState(",,,/B,B,B/,,,/B,B,B/,BBwwWWWWWWWW,,/WB,B,B/,,,",false,false,false);
		assertMove("c3-a1",false,true);
		assertGameState(",,,/B,B,B/,,,/B,B,B/,,,/B,B,B/BBwwWWWWWWWWW,,,",true,true,false);
	}
	@Test
	public void testEndMoveW(){
		System.out.println("testEndMoveW");
		startGame(",,,/W,W,W/,,,/W,W,W/,WWbbBBBBBBBB,,/BW,W,W/,,,",true);
		assertMove("c3-a1",true,true);
		assertGameState(",,,/W,W,W/,,,/W,W,W/,,,/W,W,W/WWbbBBBBBBBBB,,,",false,true,true);
	}
}
