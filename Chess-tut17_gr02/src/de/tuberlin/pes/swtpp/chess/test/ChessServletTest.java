package de.tuberlin.pes.swtpp.chess.test;

import static org.junit.Assert.*;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;

import org.junit.Before;
import org.junit.Test;

import de.tuberlin.pes.swtpp.chess.control.GameController;
import de.tuberlin.pes.swtpp.chess.control.UserController;
import de.tuberlin.pes.swtpp.chess.model.Board;
import de.tuberlin.pes.swtpp.chess.model.Bot;
import de.tuberlin.pes.swtpp.chess.model.Game;
import de.tuberlin.pes.swtpp.chess.model.User;
import de.tuberlin.pes.swtpp.chess.web.ChessServlet;

public class ChessServletTest {

	// We test with valid users
	ChessServlet servlet = new ChessServlet();
	UserController userController = servlet.getUserController();
	GameController gameController =  servlet.getGameController();
	
	@Before
	public void setUp() throws Exception {
		userController.reset();
		gameController.reset();
		userController.createUser("user1","User 1","1234");
		userController.createUser("user2","User 2","1234");
		userController.createUser("user3","User 3","1234");
		userController.createUser("user4","User 4","1234");
	}

	@Test
	public void testStartGame() {
		User u1 = userController.findUserByID("user1");//get user
		int games1 = u1.getGames().size();
		int gameID1 = gameController.totalGames()+1;//next gameID
		String gameData1 = servlet.startGame(u1, "random");//game data
		Game game1 = gameController.findGameByID(gameID1);//find game
		
		assertTrue(game1!=null);//game exists
		assertEquals(u1.getGames().size(),games1+1);// user u is in one more game
		assertEquals(game1.getWhite().getId(),u1.getId());// user is the white player
		assertTrue(game1.getBlack() instanceof Bot);// black player is bot
		assertTrue(game1.getStarted());// game started
		assertTrue(!game1.getFinished());// game not finished
		
		User u2 = userController.findUserByID("user2");//get user
		int games2 = u2.getGames().size();
		int gameID2 = gameController.totalGames()+1;//next gameID
		String gameData2 = servlet.startGame(u2, "");//game data
		Game game2 = gameController.findGameByID(gameID2);//find game
		
		assertTrue(game2!=null);//game exists
		assertEquals(u2.getGames().size(),games2+1);// user u is in one more game
		assertEquals(game2.getWhite().getId(),u2.getId());// user is the white player
		assertTrue(game2.getBlack() == null);// black player is null
		assertTrue(!game2.getStarted());// game not started cause black player not set
		assertTrue(!game2.getFinished());// game not finished
		
		User u3 = userController.findUserByID("user3");//get user
		int games3 = u3.getGames().size();
		String gameData3 = servlet.startGame(u3, null);//game data
		
		assertTrue(!gameData3.equals(""));//game data exists
		assertEquals(u3.getGames().size(),games3+1);// user u is in one more game
		assertEquals(game2.getBlack().getId(),u3.getId());// user is the black player of game 2
		assertTrue(game2.getStarted());// game started
		assertTrue(!game2.getFinished());// game not finished
	}

	@Test
	public void testTryMove() {
		User u1 = userController.findUserByID("user1");//get user
		User u2 = userController.findUserByID("user2");//get user
		int gameID = gameController.totalGames()+1;//next gameID
		
		servlet.startGame(u1, "");//start game
		servlet.startGame(u2, "");//join game
		
		Game game = gameController.findGameByID(gameID);//find game
		Board board =  game.getBoard();//gameboard
		
		
		//player white: invalid move
		servlet.tryMove(u1, gameID, "a1", "a2");
		assertTrue(board.getFEN().equals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"));
		
		//player black: not in turn
		servlet.tryMove(u2, gameID, "a7", "a6");
		assertTrue(board.getFEN().equals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"));
		
		//player white: valid move
		servlet.tryMove(u1, gameID, "a2", "a3");
		assertTrue(board.getFEN().equals("rnbqkbnr/pppppppp/8/8/8/P7/1PPPPPPP/RNBQKBNR"));
		
		//player white: not in turn
		servlet.tryMove(u1, gameID, "b2", "b4");
		assertTrue(board.getFEN().equals("rnbqkbnr/pppppppp/8/8/8/P7/1PPPPPPP/RNBQKBNR"));
		
		//player black: invalid move
		servlet.tryMove(u2, gameID, "a7", "b6");
		assertTrue(board.getFEN().equals("rnbqkbnr/pppppppp/8/8/8/P7/1PPPPPPP/RNBQKBNR"));
		
		//player black: valid move
		servlet.tryMove(u2, gameID, "b7", "b5");
		assertTrue(board.getFEN().equals("rnbqkbnr/p1pppppp/8/1p6/8/P7/1PPPPPPP/RNBQKBNR"));
		
		/**
		 * W:weiss
		 * S:schwarz*/
		/*BAUERN*/
		
		//1
		//W:B2-A3:eigenen Bauer schlagen links:fail
		servlet.tryMove(u1, gameID, "b2", "a3");
		assertTrue(board.getFEN().equals("rnbqkbnr/p1pppppp/8/1p6/8/P7/1PPPPPPP/RNBQKBNR"));
		//2
		//W:B2-B4:Bauer zwei vor
		servlet.tryMove(u1, gameID, "b2", "b4");
		assertTrue(board.getFEN().equals("rnbqkbnr/p1pppppp/8/1p6/1P6/P7/2PPPPPP/RNBQKBNR"));
		//3
		//S:D7-D6:bauer eins vor
		servlet.tryMove(u2, gameID, "d7", "d6");
		assertTrue(board.getFEN().equals("rnbqkbnr/p1p1pppp/3p4/1p6/1P6/P7/2PPPPPP/RNBQKBNR"));
		//4
		//W:A3-B4:eigenen Bauer schlagen rechts:fail
		servlet.tryMove(u1, gameID, "a3", "b4");
		assertTrue(board.getFEN().equals("rnbqkbnr/p1p1pppp/3p4/1p6/1P6/P7/2PPPPPP/RNBQKBNR"));
		//5
		//W:A3-A5:bauer nur bei ersten mal zwei vor:fail
		servlet.tryMove(u1, gameID, "a3", "a5");
		assertTrue(board.getFEN().equals("rnbqkbnr/p1p1pppp/3p4/1p6/1P6/P7/2PPPPPP/RNBQKBNR"));
		//6
		//W:A3-A4:beuer eins vor
		servlet.tryMove(u1, gameID, "a3", "a4");
		assertTrue(board.getFEN().equals("rnbqkbnr/p1p1pppp/3p4/1p6/PP6/8/2PPPPPP/RNBQKBNR"));
		//7
		//S:D6-C5:eigenen bauer schlagen links:fail
		servlet.tryMove(u2, gameID, "d6", "c5");
		assertTrue(board.getFEN().equals("rnbqkbnr/p1p1pppp/3p4/1p6/PP6/8/2PPPPPP/RNBQKBNR"));
		//8
		//S:D6-D4:bauer zwei vor:fail
		servlet.tryMove(u2, gameID, "d6", "d4");
		assertTrue(board.getFEN().equals("rnbqkbnr/p1p1pppp/3p4/1p6/PP6/8/2PPPPPP/RNBQKBNR"));
		//9
		//S:F7-F8
		servlet.tryMove(u2, gameID, "f7", "f6");
		assertTrue(board.getFEN().equals("rnbqkbnr/p1p1p1pp/3p1p2/1p6/PP6/8/2PPPPPP/RNBQKBNR"));
		//10
		//W:C2-C4:bauer zwei vor
		servlet.tryMove(u1, gameID, "c2", "c4");
		assertTrue(board.getFEN().equals("rnbqkbnr/p1p1p1pp/3p1p2/1p6/PPP5/8/3PPPPP/RNBQKBNR"));
		//11
		//S:E7-E5:bauer zwei vor
		servlet.tryMove(u2, gameID, "e7", "e5");
		assertTrue(board.getFEN().equals("rnbqkbnr/p1p3pp/3p1p2/1p2p3/PPP5/8/3PPPPP/RNBQKBNR"));

		//springer
		//12
		//W:G1-F5:springer a(in bauer):fail
		servlet.tryMove(u1, gameID, "g1", "f5");
		assertTrue(board.getFEN().equals("rnbqkbnr/p1p3pp/3p1p2/1p2p3/PPP5/8/3PPPPP/RNBQKBNR"));
		//13
		//W:G1-E2:springer b(in bauer):fail
		servlet.tryMove(u1, gameID, "g1", "e2");
		assertTrue(board.getFEN().equals("rnbqkbnr/p1p3pp/3p1p2/1p2p3/PPP5/8/3PPPPP/RNBQKBNR"));
		//14 - geht nicht das außerhalb des feldes
		//W:G1-I2:Springer c(aus spielfeld):fail
		//servlet.tryMove(u1, gameID, "g1", "");
		//System.out.println(board.getFEN()); // selftest
		//assertTrue(board.getFEN().equals("rnbqkbnr/p1p3pp/3p1p2/1p2p3/PPP5/8/3PPPPP/RNBQKBNR"));
		//15
		//W:G1-F3:springer d(über bauer)
		servlet.tryMove(u1, gameID, "g1", "f3");
		assertTrue(board.getFEN().equals("rnbqkbnr/p1p3pp/3p1p2/1p2p3/PPP5/5N2/3PPPPP/RNBQKB1R"));
		//16
		//S:B8-B6:springer e(nicht in reichweite):fail
		servlet.tryMove(u2, gameID, "b8", "b6");
		assertTrue(board.getFEN().equals("rnbqkbnr/p1p3pp/3p1p2/1p2p3/PPP5/5N2/3PPPPP/RNBQKB1R"));
		//17
		//S:G8-F6:springer f(auf bauer):fail
		servlet.tryMove(u2, gameID, "g8", "f6");
		assertTrue(board.getFEN().equals("rnbqkbnr/p1p3pp/3p1p2/1p2p3/PPP5/5N2/3PPPPP/RNBQKB1R"));
		//18
		//S:B8-D7:springer g(zwischen die figuren)
		servlet.tryMove(u2, gameID, "b8", "d7");
		assertTrue(board.getFEN().equals("r1bqkbnr/p1pn2pp/3p1p2/1p2p3/PPP5/5N2/3PPPPP/RNBQKB1R"));
		//19
		//W:F3-D4:springer h(in geschlagen werden pos)
		servlet.tryMove(u1, gameID, "f3", "d4");
		assertTrue(board.getFEN().equals("r1bqkbnr/p1pn2pp/3p1p2/1p2p3/PPPN4/8/3PPPPP/RNBQKB1R"));
		//20
		//S:G8-E7:springer i(zwischen figuren)
		servlet.tryMove(u2, gameID, "g8", "e7");
		assertTrue(board.getFEN().equals("r1bqkb1r/p1pnn1pp/3p1p2/1p2p3/PPPN4/8/3PPPPP/RNBQKB1R"));
		//21
		//W:B1-C3:springer j(bewegen)
		servlet.tryMove(u1, gameID, "b1", "c3");
		assertTrue(board.getFEN().equals("r1bqkb1r/p1pnn1pp/3p1p2/1p2p3/PPPN4/2N5/3PPPPP/R1BQKB1R"));
		//22
		//S:E7-F5:springer k(durch bauern)
		servlet.tryMove(u2, gameID, "e7", "f5");
		assertTrue(board.getFEN().equals("r1bqkb1r/p1pn2pp/3p1p2/1p2pn2/PPPN4/2N5/3PPPPP/R1BQKB1R"));
		//23
		//W:C3-E4:springer l
		servlet.tryMove(u1, gameID, "c3", "e4");
		assertTrue(board.getFEN().equals("r1bqkb1r/p1pn2pp/3p1p2/1p2pn2/PPPNN3/8/3PPPPP/R1BQKB1R"));
		//24
		//S:F5-E3:springer m(in geschlagen werden pos)
		servlet.tryMove(u2, gameID, "f5", "e3");
		assertTrue(board.getFEN().equals("r1bqkb1r/p1pn2pp/3p1p2/1p2p3/PPPNN3/4n3/3PPPPP/R1BQKB1R"));
		
		//schlagen
		//25
		//W:A4-B5: bauer
		servlet.tryMove(u1, gameID, "a4", "b5");
		assertTrue(board.getFEN().equals("r1bqkb1r/p1pn2pp/3p1p2/1P2p3/1PPNN3/4n3/3PPPPP/R1BQKB1R"));
		//26
		//S:D6-C5: bauer move
		servlet.tryMove(u2, gameID, "c7", "c5");
		assertTrue(board.getFEN().equals("r1bqkb1r/p2n2pp/3p1p2/1Pp1p3/1PPNN3/4n3/3PPPPP/R1BQKB1R"));
		//27
		//W:E4-C5: pferd
		servlet.tryMove(u1, gameID, "e4", "c5");
		assertTrue(board.getFEN().equals("r1bqkb1r/p2n2pp/3p1p2/1PN1p3/1PPN4/4n3/3PPPPP/R1BQKB1R"));
		//28
		//S:E3-G2: pferd
		servlet.tryMove(u2, gameID, "e3", "g2");
		assertTrue(board.getFEN().equals("r1bqkb1r/p2n2pp/3p1p2/1PN1p3/1PPN4/8/3PPPnP/R1BQKB1R"));
		//29
		//W:F1-G2: läufer w
		servlet.tryMove(u1, gameID, "f1", "g2");
		assertTrue(board.getFEN().equals("r1bqkb1r/p2n2pp/3p1p2/1PN1p3/1PPN4/8/3PPPBP/R1BQK2R"));
		//30
		//S:F8-C5: bauer move
		servlet.tryMove(u2, gameID, "d6", "d5");
		assertTrue(board.getFEN().equals("r1bqkb1r/p2n2pp/5p2/1PNpp3/1PPN4/8/3PPPBP/R1BQK2R"));	
		//31
		//W:läufer w
		servlet.tryMove(u1, gameID, "g2", "d5");
		assertTrue(board.getFEN().equals("r1bqkb1r/p2n2pp/5p2/1PNBp3/1PPN4/8/3PPP1P/R1BQK2R"));
		//32
		//S:läufer move
		servlet.tryMove(u2, gameID, "c8", "b7");
		System.out.println(board.getFEN());
		assertTrue(board.getFEN().equals("r2qkb1r/pb1n2pp/5p2/1PNBp3/1PPN4/8/3PPP1P/R1BQK2R"));
		//33
		//W:trum in schlag pos bringen
		servlet.tryMove(u1, gameID, "h1", "g1");
		assertTrue(board.getFEN().equals("r2qkb1r/pb1n2pp/5p2/1PNBp3/1PPN4/8/3PPP1P/R1BQK1R1"));
		//34
		//S:läufer w 
		servlet.tryMove(u2, gameID, "b7", "d5");
		assertTrue(board.getFEN().equals("r2qkb1r/p2n2pp/5p2/1PNbp3/1PPN4/8/3PPP1P/R1BQK1R1"));
		//35
		//W:trum
		servlet.tryMove(u1, gameID, "a1", "a7");
		assertTrue(board.getFEN().equals("r2qkb1r/R2n2pp/5p2/1PNbp3/1PPN4/8/3PPP1P/2BQK1R1"));
		//36
		//S:turm
		servlet.tryMove(u2, gameID, "a8", "a7");
		assertTrue(board.getFEN().equals("3qkb1r/r2n2pp/5p2/1PNbp3/1PPN4/8/3PPP1P/2BQK1R1"));
		//37
		//W:trum
		servlet.tryMove(u1, gameID, "g1", "g7");
		assertTrue(board.getFEN().equals("3qkb1r/r2n2Rp/5p2/1PNbp3/1PPN4/8/3PPP1P/2BQK3"));
		//38
		//S:dame move
		servlet.tryMove(u2, gameID, "d8", "a5");
		assertTrue(board.getFEN().equals("4kb1r/r2n2Rp/5p2/qPNbp3/1PPN4/8/3PPP1P/2BQK3"));
		//39
		//W:dame in pos bringen
		servlet.tryMove(u1, gameID, "d1", "c2");
		assertTrue(board.getFEN().equals("4kb1r/r2n2Rp/5p2/qPNbp3/1PPN4/8/2QPPP1P/2B1K3"));
		//40
		//S:trum in schlag pos bringen
		servlet.tryMove(u2, gameID, "a7", "b7");
		assertTrue(board.getFEN().equals("4kb1r/1r1n2Rp/5p2/qPNbp3/1PPN4/8/2QPPP1P/2B1K3"));
		//41
		//W:dame
		servlet.tryMove(u1, gameID, "c2", "h7");
		assertTrue(board.getFEN().equals("4kb1r/1r1n2RQ/5p2/qPNbp3/1PPN4/8/3PPP1P/2B1K3"));
		//42
		//S:trum
		servlet.tryMove(u2, gameID, "b7", "b5");
		assertTrue(board.getFEN().equals("4kb1r/3n2RQ/5p2/qrNbp3/1PPN4/8/3PPP1P/2B1K3"));
		//43
		//W:könig in schlag pos bringen:fail
		servlet.tryMove(u1, gameID, "e1", "e2");
		assertTrue(board.getFEN().equals("4kb1r/3n2RQ/5p2/qrNbp3/1PPN4/8/3PPP1P/2B1K3"));
		//33
		//W:trum
		servlet.tryMove(u1, gameID, "g7", "d7");
		assertTrue(board.getFEN().equals("4kb1r/3R3Q/5p2/qrNbp3/1PPN4/8/3PPP1P/2B1K3"));
		//44
		//S:trum
		servlet.tryMove(u2, gameID, "b5", "c5");
		assertTrue(board.getFEN().equals("4kb1r/3R3Q/5p2/q1rbp3/1PPN4/8/3PPP1P/2B1K3"));
		//45
		//W:könig move
		servlet.tryMove(u1, gameID, "e1", "d1");
		assertTrue(board.getFEN().equals("4kb1r/3R3Q/5p2/q1rbp3/1PPN4/8/3PPP1P/2BK4"));
		//46
		//S:dame
		servlet.tryMove(u2, gameID, "a5", "b4");
		assertTrue(board.getFEN().equals("4kb1r/3R3Q/5p2/2rbp3/1qPN4/8/3PPP1P/2BK4"));
		//47
		//W:könig bewegen auf schlap pos
		servlet.tryMove(u1, gameID, "d1", "c2");
		assertTrue(board.getFEN().equals("4kb1r/3R3Q/5p2/2rbp3/1qPN4/8/2KPPP1P/2B5"));
		//48
		//S:dame weg bewegen + schach
		servlet.tryMove(u2, gameID, "b4", "d2");
		assertTrue(board.getFEN().equals("4kb1r/3R3Q/5p2/2rbp3/2PN4/8/2KqPP1P/2B5"));
		//49
		//W:könig bewegen
		servlet.tryMove(u1, gameID, "c2", "b3");
		assertTrue(board.getFEN().equals("4kb1r/3R3Q/5p2/2rbp3/2PN4/1K6/3qPP1P/2B5"));
		//50
		//S:läufer move
		servlet.tryMove(u2, gameID, "f8", "e7");
		assertTrue(board.getFEN().equals("4k2r/3Rb2Q/5p2/2rbp3/2PN4/1K6/3qPP1P/2B5"));
		//51
		//W:könig move
		servlet.tryMove(u1, gameID, "b3", "a4");
		assertTrue(board.getFEN().equals("4k2r/3Rb2Q/5p2/2rbp3/K1PN4/8/3qPP1P/2B5"));
		//52
		//S:könig
		servlet.tryMove(u2, gameID, "e8", "d7");
		assertTrue(board.getFEN().equals("7r/3kb2Q/5p2/2rbp3/K1PN4/8/3qPP1P/2B5"));
		//53
		//W:dame bewegen+schach
		servlet.tryMove(u1, gameID, "h7", "e7");
		assertTrue(board.getFEN().equals("7r/3kQ3/5p2/2rbp3/K1PN4/8/3qPP1P/2B5"));
		//54
		//S:könig
		servlet.tryMove(u2, gameID, "d7", "e7");
		assertTrue(board.getFEN().equals("7r/4k3/5p2/2rbp3/K1PN4/8/3qPP1P/2B5"));
		//55
		//W:könig move ins schach:fail
		servlet.tryMove(u1, gameID, "a4", "b4");
		assertTrue(board.getFEN().equals("7r/4k3/5p2/2rbp3/K1PN4/8/3qPP1P/2B5"));
		//56
		//W:pferd in geschlagen werden pos
		servlet.tryMove(u1, gameID, "d4", "e6");
		assertTrue(board.getFEN().equals("7r/4k3/4Np2/2rbp3/K1P5/8/3qPP1P/2B5"));
		//57
		//S:köonig in geschlagen werden pos
		servlet.tryMove(u2, gameID, "c5", "b5");
		assertTrue(board.getFEN().equals("7r/4k3/4Np2/1r1bp3/K1P5/8/3qPP1P/2B5"));
		//58
		//W:könig
		servlet.tryMove(u1, gameID, "a4", "b5");
		assertTrue(board.getFEN().equals("7r/4k3/4Np2/1K1bp3/2P5/8/3qPP1P/2B5"));
		//59
		//S:läufer in geschlagen werden pos + schach
		servlet.tryMove(u2, gameID, "d5", "c6");
		assertTrue(board.getFEN().equals("7r/4k3/2b1Np2/1K2p3/2P5/8/3qPP1P/2B5"));
		//60
		//W:könig
		servlet.tryMove(u1, gameID, "b5", "c6");
		assertTrue(board.getFEN().equals("7r/4k3/2K1Np2/4p3/2P5/8/3qPP1P/2B5"));
		//61
		//S:könig
		servlet.tryMove(u2, gameID, "e7", "e6");
		assertTrue(board.getFEN().equals("7r/8/2K1kp2/4p3/2P5/8/3qPP1P/2B5"));

		//checke bauer zu dame wechsel
		/*dazu bewege schwarzen bauer in e so schnell wie möglich nach e1
		 * und bewege den weissn bauer in c so schnell wie möglich nach c8*/
		//62
		//weg frei machen
		//W
		servlet.tryMove(u1, gameID, "c6", "b6");
		assertTrue(board.getFEN().equals("7r/8/1K2kp2/4p3/2P5/8/3qPP1P/2B5"));
		//63
		//S
		servlet.tryMove(u2, gameID, "d2", "e2");
		assertTrue(board.getFEN().equals("7r/8/1K2kp2/4p3/2P5/8/4qP1P/2B5"));
		//64
		//W
		servlet.tryMove(u1, gameID, "c4", "c5");
		assertTrue(board.getFEN().equals("7r/8/1K2kp2/2P1p3/8/8/4qP1P/2B5"));
		//65
		//S
		servlet.tryMove(u2, gameID, "e2", "f1");
		assertTrue(board.getFEN().equals("7r/8/1K2kp2/2P1p3/8/8/5P1P/2B2q2"));
		//66
		//W
		servlet.tryMove(u1, gameID, "c5", "c6");
		assertTrue(board.getFEN().equals("7r/8/1KP1kp2/4p3/8/8/5P1P/2B2q2"));
		//67
		//S
		servlet.tryMove(u2, gameID, "e5", "e4");
		assertTrue(board.getFEN().equals("7r/8/1KP1kp2/8/4p3/8/5P1P/2B2q2"));
		//68
		//W
		servlet.tryMove(u1, gameID, "c6", "c7");
		assertTrue(board.getFEN().equals("7r/2P5/1K2kp2/8/4p3/8/5P1P/2B2q2"));
		//69
		//S
		servlet.tryMove(u2, gameID, "e4", "e3");
		assertTrue(board.getFEN().equals("7r/2P5/1K2kp2/8/8/4p3/5P1P/2B2q2"));
		//70
		//W führt zu schach
		servlet.tryMove(u1, gameID, "c7", "c8");
		assertTrue(board.getFEN().equals("2Q4r/8/1K2kp2/8/8/4p3/5P1P/2B2q2"));
		//71
		//S
		servlet.tryMove(u2, gameID, "e6", "e7");
		assertTrue(board.getFEN().equals("2Q4r/4k3/1K3p2/8/8/4p3/5P1P/2B2q2"));
		//72
		//W
		servlet.tryMove(u1, gameID, "c8", "h3");
		assertTrue(board.getFEN().equals("7r/4k3/1K3p2/8/8/4p2Q/5P1P/2B2q2"));
		//73
		//S
		servlet.tryMove(u2, gameID, "e3", "e2");
		assertTrue(board.getFEN().equals("7r/4k3/1K3p2/8/8/7Q/4pP1P/2B2q2"));
		//74
		//W
		servlet.tryMove(u1, gameID, "c1", "h6");
		assertTrue(board.getFEN().equals("7r/4k3/1K3p1B/8/8/7Q/4pP1P/5q2"));
		//75
		//S
		servlet.tryMove(u2, gameID, "e2", "e1");
		assertTrue(board.getFEN().equals("7r/4k3/1K3p1B/8/8/7Q/5P1P/4qq2"));
		//76
		//W
		servlet.tryMove(u1, gameID, "b6", "a7");
		assertTrue(board.getFEN().equals("7r/K3k3/5p1B/8/8/7Q/5P1P/4qq2"));
		//77
		//S
		servlet.tryMove(u2, gameID, "e1", "b4");
		assertTrue(board.getFEN().equals("7r/K3k3/5p1B/8/1q6/7Q/5P1P/5q2"));
		//78
		//W
		servlet.tryMove(u1, gameID, "f2", "f4");
		assertTrue(board.getFEN().equals("7r/K3k3/5p1B/8/1q3P2/7Q/7P/5q2"));
		//79
		//S
		servlet.tryMove(u2, gameID, "f1", "a1");
		assertTrue(board.getFEN().equals("7r/K3k3/5p1B/8/1q3P2/7Q/7P/q7"));
		//80
		//W:fail
		servlet.tryMove(u1, gameID, "a6", "a7");
		assertTrue(board.getFEN().equals("7r/K3k3/5p1B/8/1q3P2/7Q/7P/q7"));
		//W:fail
		servlet.tryMove(u1, gameID, "a7", "a1");
		assertTrue(board.getFEN().equals("7r/K3k3/5p1B/8/1q3P2/7Q/7P/q7"));
		//W:fail
		servlet.tryMove(u1, gameID, "a7", "b4");
		assertTrue(board.getFEN().equals("7r/K3k3/5p1B/8/1q3P2/7Q/7P/q7"));
		//W:fail
		servlet.tryMove(u1, gameID, "a7", "h8");
		assertTrue(board.getFEN().equals("7r/K3k3/5p1B/8/1q3P2/7Q/7P/q7"));
		//jetzt müsste spieler weiss verloren haben ...
	}

	@Test
	public void testGiveUp() {
		User u1 = userController.findUserByID("user1");//get user
		int games1 = u1.getGames().size();
		int gameID1 = gameController.totalGames()+1;//next gameID
		String gameData1 = servlet.startGame(u1, "random");//game data
		Game game1 = gameController.findGameByID(gameID1);//find game
		Board board1 =  game1.getBoard();//gameboard
		
		servlet.giveUp(u1, gameID1);//white player give up
		
		assertTrue(game1.getFinished());//game finished
		assertTrue(game1.giveupWhite());//white player gived up
		assertTrue(!game1.giveupBlack());//bot dont gived up
		assertTrue(!game1.getWinner().getId().equals(u1.getId()));//user is not the winner
	
		//player white: valid move but doesnt have any effect because game finished
		servlet.tryMove(u1, gameID1, "A2", "A3");
		assertTrue(board1.getFEN().equals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"));
		
		User u2 = userController.findUserByID("user1");//get user
		User u3 = userController.findUserByID("user3");//get user
		int gameID2 = gameController.totalGames()+1;//next gameID
		
		servlet.startGame(u2, "");//start game
		servlet.startGame(u3, "");//join game
		
		Game game2 = gameController.findGameByID(gameID2);//find game
		
		servlet.giveUp(u3, gameID2);//black player give up
		
		assertTrue(game2.getFinished());//game finished
		assertTrue(!game2.giveupWhite());//white player dont gived up
		assertTrue(game2.giveupBlack());//black player gived up
		assertTrue(game2.getWinner().getId().equals(u2.getId()));// white player is the winner
	
		servlet.giveUp(u2, gameID2);//white player give up after black, but it doesnt have any effects
		
		assertTrue(game2.getFinished());//game finished
		assertTrue(!game2.giveupWhite());//white player dont gived up
		assertTrue(game2.giveupBlack());//black player gived up
		assertTrue(game2.getWinner().getId().equals(u2.getId()));// white player is the winner
	}

	@Test
	public void testCallDraw() {
		User u1 = userController.findUserByID("user1");//get user
		int games1 = u1.getGames().size();
		int gameID1 = gameController.totalGames()+1;//next gameID
		String gameData1 = servlet.startGame(u1, "random");//game data
		Game game1 = gameController.findGameByID(gameID1);//find game
		Board board1 =  game1.getBoard();//gameboard
		
		servlet.callDraw(u1, gameID1);//white player call draw
		
		assertTrue(!game1.getFinished());//game not finished
		assertTrue(game1.drawWhite());//white player called draw
		assertTrue(!game1.drawBlack());//bot dont called draw
		
		//player white: valid move can do because game not finished
		servlet.tryMove(u1, gameID1, "a2", "a3");
		assertTrue(board1.getFEN().equals("rnbqkbnr/pppppppp/8/8/8/P7/1PPPPPPP/RNBQKBNR"));
				
		
		User u2 = userController.findUserByID("user2");//get user
		User u3 = userController.findUserByID("user4");//get user
		int gameID2 = gameController.totalGames()+1;//next gameID
		
		servlet.startGame(u2, "");//start game
		servlet.startGame(u3, "");//join game
		
		Game game2 = gameController.findGameByID(gameID2);//find game
		Board board2 =  game2.getBoard();//gameboard
		
		servlet.callDraw(u2, gameID2);//white player call draw
		
		assertTrue(!game2.getFinished());//game not finished
		assertTrue(game2.drawWhite());//white player called draw
		assertTrue(!game2.drawBlack());//bot dont called draw
		
		//player white: valid move can do because game not finished
		servlet.tryMove(u2, gameID2, "a2", "a3");
		assertTrue(board2.getFEN().equals("rnbqkbnr/pppppppp/8/8/8/P7/1PPPPPPP/RNBQKBNR"));
	
		servlet.callDraw(u3, gameID2);//black player call draw
		
		assertTrue(game2.getFinished());//game finished
		assertTrue(game2.drawWhite());//white player called draw
		assertTrue(game2.drawBlack());//black player called draw
		
		//player black: valid move dont have any effect because game finished
		servlet.tryMove(u3, gameID2, "b7", "b5");
		assertTrue(board2.getFEN().equals("rnbqkbnr/pppppppp/8/8/8/P7/1PPPPPPP/RNBQKBNR"));
	}

	@Test
	public void testGetUserDataJSON() {
		
		String uid = "uid";
		String uname = "uname";
		String upassword = "upassword";
		
		User u = new User(uname, uid, upassword);
		
		User u1 = userController.findUserByID("user1");//get user
		
		//add games
		servlet.startGame(u, "random");
		servlet.startGame(u, "");
		servlet.startGame(u, null);
		servlet.startGame(u1, "");
		
		String result = servlet.getUserDataJSON(u);
		JsonObject json = Json.createReader(new StringReader(result)).readObject();
		
		String userIDJson = json.getJsonString("userID").toString(); 
		String userNameJson = json.getJsonString("userFirstName").toString();
		
		assertEquals(uname,userNameJson);  
		assertEquals(uid,userIDJson);
		
		//test game data of user as well
		JsonArray userGamesJson = json.getJsonArray("games");
		
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
	   	JsonArrayBuilder games = factory.createArrayBuilder();
	   	games.add(factory.createArrayBuilder().add(1).add("random"));
	   	games.add(factory.createArrayBuilder().add(2).add("User 1"));
	   	games.add(factory.createArrayBuilder().add(3).add("Waiting for player"));
	   	
	   	JsonObject value = factory.createObjectBuilder().add("games", games).build();
	   	String result2 = value.toString();
		JsonObject json2 = Json.createReader(new StringReader(result2)).readObject();
		JsonArray userGamesJson2 = json2.getJsonArray("games");
		
		assertEquals(userGamesJson2.get(0),userGamesJson.get(0));
		assertEquals(userGamesJson2.get(1),userGamesJson.get(1));
		assertEquals(userGamesJson2.get(2),userGamesJson.get(2));
	}

	@Test
	public void testGetGameData() {
		User u1 = userController.findUserByID("user1");//get user
		User u2 = userController.findUserByID("user2");//get user
		int gameID = gameController.totalGames()+1;//next gameID
		
		servlet.startGame(u1, "");//start game
		
		Game game = gameController.findGameByID(gameID);//find game
		Board board =  game.getBoard();//gameboard
		
		String result = servlet.getGameData(u1, gameID);
		JsonObject json = Json.createReader(new StringReader(result)).readObject();
		
		assertEquals(gameID,json.getInt("gameID"));//game id is equal
		assertEquals(u1.getName(),json.getJsonString("white").toString());//white player name is ok
		assertEquals("Wait",json.getJsonString("status").toString());//status is ok
		assertEquals("Waiting for opponent.",json.getJsonString("info").toString());//info is ok
		assertTrue(true==json.getBoolean("yourturn"));//turn is ok
		assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR",json.getString("board").toString());//fen is ok
		
		servlet.startGame(u2, "");//join game
		
		result = servlet.getGameData(u2, gameID);
		json = Json.createReader(new StringReader(result)).readObject();
		
		assertEquals(gameID,json.getInt("gameID"));//game id is equal
		assertEquals(u2.getName(),json.getJsonString("black").toString());//black player name is ok
		assertEquals("Started",json.getJsonString("status").toString());//status is ok
		assertEquals("",json.getJsonString("info").toString());//info is ok
		assertTrue(false==json.getBoolean("yourturn"));//turn is ok
		assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR",json.getString("board").toString());//fen is ok
		
		servlet.tryMove(u1, gameID, "a2", "a3");//player white: valid move
		servlet.callDraw(u2, gameID);//black player call draw
		
		result = servlet.getGameData(u1, gameID);
		json = Json.createReader(new StringReader(result)).readObject();
		
		assertTrue(false==json.getBoolean("yourturn"));//turn is ok
		assertEquals("rnbqkbnr/pppppppp/8/8/8/P7/1PPPPPPP/RNBQKBNR",json.getString("board").toString());//fen is ok
		assertEquals(u2.getName()+" called draw.",json.getJsonString("info").toString());//info is ok
	
		servlet.tryMove(u2, gameID, "b7", "b5");//player black: valid move
		servlet.giveUp(u1, gameID);//white gived up
		
		result = servlet.getGameData(u1, gameID);
		json = Json.createReader(new StringReader(result)).readObject();
		
		assertEquals("Finished",json.getJsonString("status").toString());//status is ok
		assertEquals("Lost.\nYou gived up.",json.getJsonString("info").toString());//info is ok
		assertTrue(false==json.getBoolean("yourturn"));//turn is ok
		assertEquals("rnbqkbnr/p1pppppp/8/1p6/8/P7/1PPPPPPP/RNBQKBNR",json.getString("board").toString());//fen is ok
	
		servlet.tryMove(u1, gameID, "a3", "a4");//player white: valid move but has no effect because game finished
		
		result = servlet.getGameData(u2, gameID);
		json = Json.createReader(new StringReader(result)).readObject();
		
		assertEquals("Finished",json.getJsonString("status").toString());//status is ok
		assertEquals("Won.\n"+u1.getName()+" gived up.",json.getJsonString("info").toString());//info is ok
		assertTrue(false==json.getBoolean("yourturn"));//turn is ok
		assertEquals("rnbqkbnr/p1pppppp/8/1p6/8/P7/1PPPPPPP/RNBQKBNR",json.getString("board").toString());//fen is ok
	}

	@Test
	public void testGetStatisticsJSON() {
		User u1 = userController.findUserByID("user1");//get user
		User u2 = userController.findUserByID("user2");//get user
		User u3 = userController.findUserByID("user3");//get user
		User u4 = userController.findUserByID("user4");//get user
		
		int gameID1 = gameController.totalGames()+1;//next gameID
		
		servlet.startGame(u1, "");//start game
		servlet.startGame(u2, "");//join game
		servlet.giveUp(u1, gameID1);
		
		Game game1 = gameController.findGameByID(gameID1);//find game
		
		String result = servlet.getStatisticsJSON(u1);
		JsonObject json = Json.createReader(new StringReader(result)).readObject();
	
		assertEquals(u1.getName(),json.getString("userFirstName").toString());
		assertEquals(0,json.getInt("nbWon"));
		assertEquals(0,json.getInt("nbDraw"));
		assertEquals(1,json.getInt("nbLost"));
		assertEquals(0,json.getInt("avgMoves"));
		assertEquals(0,json.getInt("quotWon"));
		
		result = servlet.getStatisticsJSON(u2);
		json = Json.createReader(new StringReader(result)).readObject();
		
		assertEquals(u2.getName(),json.getString("userFirstName").toString());
		assertEquals(1,json.getInt("nbWon"));
		assertEquals(0,json.getInt("nbDraw"));
		assertEquals(0,json.getInt("nbLost"));
		assertEquals(0,json.getInt("avgMoves"));
		assertEquals(1,json.getInt("quotWon"));
		
		int gameID2 = gameController.totalGames()+1;//next gameID
		
		servlet.startGame(u3, "");//start game
		servlet.startGame(u1, "");//join game
		
		servlet.tryMove(u3, gameID2, "a2", "a3");//player white: valid move
		servlet.tryMove(u1, gameID2, "b7", "b5");//player black: valid move
		servlet.tryMove(u3, gameID2, "c2", "c3");//player white: valid move
		servlet.tryMove(u1, gameID2, "d7", "d5");//player black: valid move
		
		servlet.callDraw(u3, gameID2);//white player call draw
		servlet.callDraw(u1, gameID2);//black player call draw
		
		result = servlet.getStatisticsJSON(u1);
		json = Json.createReader(new StringReader(result)).readObject();
		
		assertEquals(u1.getName(),json.getString("userFirstName").toString());
		assertEquals(0,json.getInt("nbWon"));
		assertEquals(1,json.getInt("nbDraw"));
		assertEquals(1,json.getInt("nbLost"));
		assertEquals(1,json.getInt("avgMoves"));
		assertEquals(0.0,json.getJsonNumber("quotWon").doubleValue(),0.01);
		
		result = servlet.getStatisticsJSON(u3);
		json = Json.createReader(new StringReader(result)).readObject();
		
		assertEquals(u3.getName(),json.getString("userFirstName").toString());
		assertEquals(0,json.getInt("nbWon"));
		assertEquals(1,json.getInt("nbDraw"));
		assertEquals(0,json.getInt("nbLost"));
		assertEquals(2,json.getInt("avgMoves"));
		assertEquals(0.0,json.getJsonNumber("quotWon").doubleValue(),0.01);
		
		int gameID3 = gameController.totalGames()+1;//next gameID
		
		servlet.startGame(u1, "");//start game
		servlet.startGame(u4, "");//join game
		
		servlet.tryMove(u1, gameID3, "c2", "c3");//player white: valid move
		servlet.tryMove(u4, gameID3, "d7", "d5");//player black: valid move
		
		servlet.giveUp(u4, gameID3);
		
		result = servlet.getStatisticsJSON(u1);
		json = Json.createReader(new StringReader(result)).readObject();
		assertEquals(u1.getName(),json.getString("userFirstName").toString());
		assertEquals(1,json.getInt("nbWon"));
		assertEquals(1,json.getInt("nbDraw"));
		assertEquals(1,json.getInt("nbLost"));
		assertEquals(1,json.getInt("avgMoves"));
		assertEquals(0.33,json.getJsonNumber("quotWon").doubleValue(),0.01);
		
		result = servlet.getStatisticsJSON(u4);
		json = Json.createReader(new StringReader(result)).readObject();
		
		assertEquals(u4.getName(),json.getString("userFirstName").toString());
		assertEquals(0,json.getInt("nbWon"));
		assertEquals(0,json.getInt("nbDraw"));
		assertEquals(1,json.getInt("nbLost"));
		assertEquals(1,json.getInt("avgMoves"));
		assertEquals(0.0,json.getJsonNumber("quotWon").doubleValue(),0.01);
	}

}