package de.tuberlin.pes.swtpp.chess.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.json.*;

import de.tuberlin.pes.swtpp.chess.control.GameController;
import de.tuberlin.pes.swtpp.chess.control.UserController;
import de.tuberlin.pes.swtpp.chess.model.Bot;
import de.tuberlin.pes.swtpp.chess.model.Game;
import de.tuberlin.pes.swtpp.chess.model.Move;
import de.tuberlin.pes.swtpp.chess.model.User;
import de.tuberlin.pes.swtpp.lib.ChessLib;

/**
 * Servlet implementation class ChessServlet
 */
@WebServlet("/ChessServlet")
public class ChessServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
/////////////////////////////////////////////////////////////////////
//        GLOBAL DATA                                              //
/////////////////////////////////////////////////////////////////////
																   								   

	// controller classes
	private static UserController userController;
	private static GameController gameController;
	
/////////////////////////////////////////////////////////////////////
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChessServlet() {
        super();
        
        ///// initialization of data model //////
        
        // create controllers
        userController = UserController.getInstance();
                
        gameController = GameController.getInstance();
        
        // Nur fürs testen
        addDummyData();
    }

	/**
	 * 
	 * The doGet method of the servlet represents the interface of our java classes to the HTTP requests and responses of the application server 
	 * (servlet framework). See README for more info.
	 *  
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		// get a writer for our response data out of the request
	    PrintWriter out = response.getWriter();
	    response.setContentType("text/plain");
		
	    // extract use case parameter out of the request
		String usecase = request.getParameter("usecase");
				
		if (usecase == null || usecase == "" || usecase.equals("checkuser")) {
			// if there is no use-case selected:
			// redirect to main html. If there is no valid user session that script will further redirect to login page
			ServletContext sc = getServletContext();
			RequestDispatcher rd;
			
		    rd = sc.getRequestDispatcher("/main.html");
		    rd.forward(request, response);
		} else if (usecase.equals("login")) {
	    	// normal log in request : check user ID and password
	    	// if ok remember user and forward to main page, else try again
	    	if (userController.checkUserPwd(request.getParameter("id"), request.getParameter("password"))) {
	    		request.getSession().setAttribute("currentUser", userController.findUserByID(request.getParameter("id")));
	    		return;
	 	    } else {
	    		out.write("User/Pwd unbekannt.");
	    		return;
	    	}
	    } else if (usecase.equals("logout")) {
	    	// log of just disconnects the current user and goes back to start
	    	request.getSession().removeAttribute("currentUser");
	    	// go to index page
	    } else if (usecase.equals("register")) {
	    	// register new user use case. 

	    	// this checks if the user name in the request is secure to display in HTML 
	    	String name = request.getParameter("name");
	    	Pattern p = Pattern.compile("[a-zA-Z\\s,]+");
	    	Matcher m = p.matcher(name);
	    	
	    	// reject if input is not okay to process
	    	if (!m.matches()) {
	    		out.write("badinput");
	    		return;
	    	}
	    	
	    	// register a new user with the selected data. the createUser method returns error messages when the data given was not correct
	    	User u = userController.createUser(request.getParameter("id"), request.getParameter("name"), request.getParameter("password"));
	    	
	    	// check if new user was successfully created. 
	    	if (u != null) {
	    		//Start session
	    		request.getSession().setAttribute("currentUser", u);
	    	} else {
	    		// error message 
	    		out.write("exists");
	    	}
	    	
	    } else { 
	    	// now we really start processing the use cases that are related to the game. 
	    	
	    	User u = (User) request.getSession().getAttribute("currentUser");
	    	
	    	// answer with empty string when user is not logged in
	    	if (u == null) return;
	    	
	    	// from here on:
	    	// game-related and data-related use cases.
	    	// servlet's job here is only to extract the correct parameters and to pass them on to the use case methods (see below)
	    	// the result is passed back to javascript as a string (if complex: JSON)
	    	
	    	// start new game against human opponent (user)
	    	if (usecase.equals("startgame")) { 
	    		out.write(startGame(u, ""));
	    		
	    	// start new game againtst bot
			} else if (usecase.equals("startbotgame")) { 
		    	out.write(startGame(u, request.getParameter("bot")));
		    	
		    // give up game 
			} if (usecase.equals("giveUp")) { 
		    	try { out.write(giveUp(u,Integer.parseInt(request.getParameter("gameID")))); 		
		    	} catch (Exception e) { System.out.println("Illegal Input: " + request.toString());}

			// call draw (remis) on a game
			} if (usecase.equals("callDraw")) { 
			   	try { out.write(callDraw(u,Integer.parseInt(request.getParameter("gameID")))); 		
			   	} catch (Exception e) { System.out.println("Illegal Input: " + request.toString());}
			   	
			// retrieve all data of a game
			// used for polling
		    } else if (usecase.equals("getgamedata")) {
		    	try { out.write(getGameData(u,Integer.parseInt(request.getParameter("gameID")))); 		
		    	} catch (Exception e) { System.out.println("Illegal Input: " + request.toString());}
	
		    // try move: submit coordinates for a move (source->target) and perform move if it is possible 
		    } else if (usecase.equals("trymove")) {
		    	int gameID = Integer.parseInt(request.getParameter("gameID"));
		    	String source = request.getParameter("source");
		    	String target = request.getParameter("target");
		    	
		    	out.write(tryMove(u,gameID,source, target));

		    // retrieve data of a user
		    } else if (usecase.equals("getuserdata")) {
		    	out.write(getUserDataJSON(u));
			    
		    // retrieve statistics data of a user
		    } else if (usecase.equals("getstatistics")) {
		    	out.write(getStatisticsJSON(u));
		    }
			
	    }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("post");
		
		doGet(request, response);
	}
	
	
/////////////////////////////////////////////////////////////
///      WEB INTERFACE: USE CASE FUNCTIONS                 //
/////////////////////////////////////////////////////////////
	
// Use Cases login, logoff, register: done
	

	/**
	 * This method gets called by the servlet to start a new game for a  
	 * 
	 * @param u - User object: valid, logged in user
	 * @param bot - Parameter is empty string for game against human opponent. Otherwise - bot="bot" (Parameter is String in case different bots will be tested.)
	 * @return gameData as JSON (see getGameData)
	 */
	public String startGame(User u, String bot) {
		Game g=null;
		
		if(bot==null || bot.equals("")){
			g=gameController.startGame(u);
		} else{
			Bot b=new Bot(bot,bot);
			
			g=gameController.startBotGame(u,b);
		}
		
		return getGameData(u, g.getId());
	}
	
	/**
	 * Servlet calls this method for a move request of the User u. Figure moves from position source to position target in Game with id=gameID.  
	 * 
	 * @param u - User object: valid, logged in user
	 * @param gameID
	 * @param source (Format: "a1" to "h8")
	 * @param target (Format: "at" to "h8")
	 * @return gameData as JSON (see getGameData)
	 */
	public String tryMove(User u, int gameID, String source, String target) {
		
		// Hint:
		// conversion of coordinates:
		// sourceRow = source.charAt(0) - 96; // ASCII 'a' - 96 = 1
		// sourceCol = Integer.parseInt(source.charAt(1) + ""); // parse single digit character
		
		if(gameController.findGameByID(gameID)==null){
			return "";
		}
		
		Game g=gameController.findGameByID(gameID);
		
		if(g.getStarted() && !g.getFinished() && g.isTurn(u)){
			boolean moved=g.getBoard().moveFigure(g.getWhite().getId().equals(u.getId()), source, target);
			
			if(moved){
				g.addMove(new Move(u,gameID,source,target));
				
				if(ChessLib.checkCheckMate(g.getBoard().getFEN(), !g.getWhite().getId().equals(u.getId()))){
					System.out.println("Matt");
					if(g.getWhite().getId().equals(u.getId())){
						g.setWinner(g.getWhite());
					} else {
						g.setWinner(g.getBlack());
					}
					
					g.setFinished(true);
				}
			}
		}
		
		return getGameData(u, gameID);
	}
	
	/**
	 * Servlet calls this method when the user u wants to give up game with id='gameID'  
	 * 
	 * @param u - User object: valid, logged in user
	 * @param gameID
	 * @return gameData as JSON (see getGameData)
	 */
	public String giveUp(User u, int gameID) {
		if(gameController.findGameByID(gameID)==null){
			return "";
		}
		
		Game g=gameController.findGameByID(gameID);
		
		if(g.getStarted() && !g.getFinished()){
			if(g.getWhite().getId().equals(u.getId())){
				g.setGiveupWhite(true);
				g.setWinner(g.getBlack());
			} else {
				g.setGiveupBlack(true);
				g.setWinner(g.getWhite());
			}
			
			g.setFinished(true);
		}
		
		return getGameData(u, gameID);
	}
	
	/**
	 * Servlet calls this method when the user u wants to call draw (remis) on game with id='gameID'  
	 * 
	 * @param u - User object: valid, logged in user
	 * @param gameID
	 * @return gameData as JSON (see getGameData)
	 */
	public String callDraw(User u, int gameID) {
		if(gameController.findGameByID(gameID)==null){
			return "";
		}
		
		Game g=gameController.findGameByID(gameID);
		
		if(g.getStarted() && !g.getFinished()){
			if(g.getWhite().getId().equals(u.getId())){
				g.setDrawWhite(true);
			} else{
				g.setDrawBlack(true);
			}
			
			if(g.drawWhite() && g.drawBlack()){
				g.setFinished(true);
			}
		}
		
		return getGameData(u, gameID);
	}

	
	/**
	 * Creates a JSON object string with all data relevant for the user's main page. Format is JSON (variable values depicted as __VALUE__):
	 * 
	 *  {"userID":__USER_ID__,
	 *   "userFirstName":__USER_FIRST_NAME__,
	 *   "games":[
	 *   	[__GAME_ID1__ ,__OPPONENT1__],
	 *   	[__GAME_ID2__,__OPPONENT2__],
	 *   	..... more games......
	 *   ]
	 *  }
	 *  
	 *  User ID and user first name are Strings,
	 *  games is an Array where each element consists of the following entries:
	 *  gameID and opponent name (For display. If there is no opponent, another string is possible, for example: "waiting....")
	 * 
	 * @param u
	 * @return User data JSON string
	 */
	public String getUserDataJSON(User u) {
		
		
////////////////////////////////////////////////////////////////////////////////////
		 // Hint: The following code would create valid user data 
	 	 JsonBuilderFactory factory = Json.createBuilderFactory(null);
   	 
	   	 JsonArrayBuilder games = factory.createArrayBuilder();
	   	 
	   	 for(Integer i:u.getGames()){
	   		Game g=gameController.findGameByID(i);
	   		String o="";
	   		if(!g.getFinished()){
	   			if(g.getWhite()!=null && g.getBlack()!=null && u.getId().equals(g.getWhite().getId())){
		   			o=g.getBlack().getName();
		   		} else if(g.getWhite()!=null && g.getBlack()!=null && u.getId().equals(g.getBlack().getId())){
		   			o=g.getWhite().getName();
		   		} else if(g.getWhite()!=null && g.getBlack()==null){
		   			o="Waiting for player";
		   		}
		   		games.add(factory.createArrayBuilder().add(i).add(o));
	   		}   
	   	 }
	   	 
	   	 JsonObject value = factory.createObjectBuilder()
		   	     .add("userID", u.getId())
		   	     .add("userFirstName", u.getName())
		   	     .add("games", games)
		   	     .build();
	   	 
	   	 /*games.add(factory.createArrayBuilder().add(16).add("Waiting for Player."));
		 games.add(factory.createArrayBuilder().add(37).add("John"));   		 
	   	 	    	 
	   	 JsonObject value = factory.createObjectBuilder()
	   	     .add("userID", u.getId())
	   	     .add("userFirstName", u.getName())
	   	     .add("games", games)
	   	     .build();*/
		
	   	 // result: {"userID":u.getId(),"userFirstName":u.getName(),"games":[[16,"Waiting for Player."],[37,"John"]]}		
	   	
	   	return value.toString();
////////////////////////////////////////////////////////////////////////////////////
	}
	
	/**
	 * Returns the game data of the game with id="gameID". The exchange format is JSON (variable values depicted as __VALUE__):
	 * 
	 * {"gameID"  :__GAMEID__,
	 *  "white"   :__WHITE_PLAYER_NAME__,
	 *  "black"   :__BLACK_PLAYER_NAME__,
	 *  "status"  :__STATUS_OF_GAME__,
	 *  "info"    :__ADDITIONAL_INFO__,
	 *  "yourturn":__true_or_false__,
	 *  "board":__FEN_BOARD_STRING__}
	 * 
	 * Where status is a game status (String) such as "started" or "over" and info is an additional field such as "black gave up" or "white called draw". 
	 * Both are displayed on the game screen. yourturn should be set true if user u can make the next move. For the format of
	 * board see the FEN String documentation
	 *  
	 * @param u - User object: valid, logged in user
	 * @param gameID
	 * @return gameData as JSON or "" if user is not player in game 
	 */
	public String getGameData(User u, int gameID) {
		
//////////////////////////////////////////////////////////////////////////////////
     //  Hint: The following code would create a valid example of game data 
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		
		if(gameController.findGameByID(gameID)==null){
			return "";
		}
		
		Game g=gameController.findGameByID(gameID);
		int id=0;
		String white="";
		String black="";
		String status="";
		String info="";
		boolean turn=false;
		String board="";
		
		if(g!=null){
			id=g.getId();
			
			if(g.getWhite()!=null){
				white=g.getWhite().getName();
			}
			if(g.getBlack()!=null){
				black=g.getBlack().getName();
			}
			if(g.getFinished()){
				status="Finished";
				if(g.drawWhite() && g.drawBlack()){
					info="Draw";
				} else if(g.getWinner().getId().equals(u.getId())){
					info="Won.";
					if(g.giveupWhite()){
						info+="\n"+g.getWhite().getName()+" gived up.";
					} else if(g.giveupBlack()){
						info+="\n"+g.getBlack().getName()+" gived up.";
					}
				} else{
					info="Lost.";
					if(g.giveupWhite() && g.getWhite().getId().equals(u.getId())){
						info+="\nYou gived up.";
					} else if(g.giveupBlack() && g.getBlack().getId().equals(u.getId())){
						info+="\nYou gived up.";
					}
				}
			} else if(g.getWhite()!=null && g.getBlack()!=null){
				status="Started";
				if(g.drawWhite()){
					info=g.getWhite().getName()+" called draw.";
				}
				if(g.drawBlack()){
					info=g.getBlack().getName()+" called draw.";
				}
			} else if(g.getWhite()!=null && g.getBlack()==null){
				status="Wait";
				info="Waiting for opponent.";
			}
			if(g.isTurn(u)){
				turn=true;
			}
			if(g.getBoard()!=null){
				board=g.getBoard().getFEN();
			}
		}
		
		JsonObject value = factory.createObjectBuilder()
		   	     .add("gameID", id)
		   	     .add("white", white)
		   	     .add("black", black)
		   	     .add("status", status)
		   	     .add("info", info)
		   	     .add("yourturn", turn)
		   	     .add("board", board)
		   	     .build();
	 	 
	   	 /*JsonObject value = factory.createObjectBuilder()
	   	     .add("gameID", 5)
	   	     .add("white", "Jim")
	   	     .add("black", "Jack")
	   	     .add("status", "started")
	   	     .add("info", "Jim called draw")
	   	     .add("yourturn", true)
	   	     .add("board", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR")
	   	     .build();*/
	   	
	   	return value.toString();
///////////////////////////////////////////////////////////////////////////////////
		
		
	}
	
//	{"userFirstName":"a","nbWon":0,"nbLost":0,"nbDraw":0,"avgMoves":0.0,"quotWon":0.0}
	
	/**
	 * Returns statistics data for the user u. The exchange format is JSON (variable values depicted as __VALUE__):
	 * 
	 * {"userFirstName"  :__USER_FIRST_NAME__,
	 *  "nbWon"   :__NB_GAMES_WON__,
	 *  "nbLost"   :__NB_GAMES_LOST__,
	 *  "nbDraw"  :__NB_GAMES_DRAW__,
	 *  "avgMoves"    :__AVG_GAME_MOVES__,
	 *  "quotWon":__PERCENT_GAMES_WON__
	 *  }
	 *
	 * where first name is the user first name, quotWon is a double 
	 * and all the other values are integers that represent the statistics of the player
	 *  
	 * @param u - User object: valid, logged in user
	 * @return statics data  as JSON 
	 */
	public String getStatisticsJSON(User u) {
//////////////////////////////////////////////////////////////////////////////////
		// Hint: The following code would create a valid example of statistics data
	
	 	 JsonBuilderFactory factory = Json.createBuilderFactory(null);
	 	 
	 	 int won=0;
	 	 int lost=0;
	 	 int draw=0;
	 	 double move=0;
	 	 
	 	 List<Integer> games = u.getGames();
	 	 
	 	 for(Integer i:games){
	 		 Game g = gameController.findGameByID(i);
	 		 
	 		 if(g.getFinished()){
	 			 if(g.drawWhite() && g.drawBlack()){
	 				 draw++;
	 			 } else if(g.getWinner().getId().equals(u.getId())){
	 				 won++;
	 			 } else{
	 				 lost++;
	 			 }
	 			 
	 			List<Move> moves =  g.getMoves();
	 			
	 			for(Move m:moves){
	 				if(m.getPlayer().getId().equals(u.getId())){
	 					move++;
	 				}
	 			}
	 		 }
	 	 }
	 	 
	 	 int avg=0;
	 	 double quote=0;
	 	 
	 	 if(won+lost+draw>0){
	 		 avg=(int)(move/(won+lost+draw));
	 		 double tmpquote=(double)won/(won+lost+draw);
	 		 quote=(int) (tmpquote*100);
	 		 quote=quote/100;
	 	 }
	   	 	 	    	 
	   	 JsonObject value = factory.createObjectBuilder()
	   	     .add("userFirstName", u.getName())
	   	     .add("nbWon", won)
	   	     .add("nbLost", lost)
	   	     .add("nbDraw", draw)
	   	     .add("avgMoves", avg)
	   	     .add("quotWon", quote)
	   	     .build();
	   	
	   	return value.toString();
////////////////////////////////////////////////////////////////////////////////
	   	
	}
	
/////////////////////////////////////////////////////////////
///      WEB INTERFACE: HELPING FUNCTIONS                  //
/////////////////////////////////////////////////////////////

	/**
	 * This method creates DEBUG data for faster manual testing. To be deleted before shipping. 
	 */
	private void addDummyData()
	{
		userController.createUser("a", "a", "a");
		userController.createUser("b", "b", "b");
		
	}

	public static UserController getUserController() {
		return userController;
	}

	public static GameController getGameController() {
		return gameController;
	}

	public static void setUserController(UserController userController) {
		ChessServlet.userController = userController;
	}

	public static void setGameController(GameController gameController) {
		ChessServlet.gameController = gameController;
	}

}