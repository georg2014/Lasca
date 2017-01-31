//editor: Georg Stahn
package de.tuberlin.sese.swtpp.gameserver.model.lasca;

import java.io.Serializable;

public class LascaBoardControl implements Serializable{
	private static final long serialVersionUID = 2467695635164175201L;
	
	/***************************************************************
						     *METHODES*
	***************************************************************/
	
/*******************************BOARD***************************************
 L	00	10					60
 E	01
 T
 T
 E
 R
 S	06						66
 	N	U	M	B	E	R	S
 This is how we designed the board ;)
****************************************************************************/
	
	public  String[][] fen2array(String state){
		//editor: Jonas Franz Schicke
		/** fen2array
		 * 2D array (chessboard) takes fen (state) !without last bit " b" or " w"
		 * @param fenString array[x][y] x : numbers y : letters both from 0 to 6 
		 * @return 2D-array
		 */
		String[][] gameboard = new String[7][7];
		
		int x=0;
		int y=0;
		
		while(state.length()>0){
			//System.err.println("state.length(): "+state.length());
			String start = state.substring(0,1);
			//System.err.println("start = state.substring(0,1) "+state.substring(0,1));
			if((x==1||x==3||x==5)&&y==0)y++;
			switch(start){
			case "b": 	if(x<7){
						if(gameboard[x][y]==null)gameboard [x][y] = "b";
						else gameboard[x][y] = gameboard[x][y] + "b";
						state = state.substring(1,state.length());
						break;
						}
			case "B": 	if(x<7){
						if(gameboard[x][y]==null)gameboard [x][y] = "B";
						else gameboard[x][y] = gameboard[x][y] + "B";
						state = state.substring(1,state.length());
						break;
						}
			case "w": 	if(x<7){
						if(gameboard[x][y]==null)gameboard [x][y] = "w";
						else gameboard[x][y] = gameboard[x][y] + "w";
						state = state.substring(1,state.length());
						break;
					}
			case "W": 	if(x<7){
						if(gameboard[x][y]==null)gameboard [x][y] = "W";
						else gameboard[x][y] = gameboard[x][y] + "W";
						state = state.substring(1,state.length());
						break;
						}
			case ",": 	y=y+2;
						state = state.substring(1,state.length());;break;
			case "/": 	x++;
						y=0;
						state = state.substring(1,state.length());
			}
		}
		return gameboard;
	}
	public  String array2fen(String[][] board){
		//editor: Jonas Franz Schicke
		/** array2fen
		 * 2D array (chessboard) takes fen (state) !without last bit " b" or " w"
		 * @param fenString array[x][y] x : numbers y : letters both from 0 to 6 
		 * @return 2D-array
		 */
		String fenString = "";
		for(int m=0;m<7;m++){
			if(m==0||m==2||m==4||m==6){
				if(board[m][0]!=null) fenString = fenString + board[m][0] + ",";
				else fenString = fenString + ",";
				if(board[m][2]!=null) fenString = fenString + board[m][2] + ",";
				else fenString = fenString + ",";
				if(board[m][4]!=null) fenString = fenString + board[m][4] + ",";
				else fenString = fenString + ",";
				if(board[m][6]!=null) fenString = fenString + board[m][6];
			}
			if(m==1||m==3||m==5){
				if(board[m][1]!=null) fenString = fenString + board[m][1] + ",";
				else fenString = fenString + ",";
				if(board[m][3]!=null) fenString = fenString + board[m][3] + ",";
				else fenString = fenString + ",";
				if(board[m][5]!=null) fenString = fenString + board[m][5];
			}
			if(m!=6)fenString = fenString + "/";
		}
		return fenString;
	}
	
	public  int[] searchC(String moveString){
		//editor: Georg Stahn
		/** searchC
		 * @parameter moveString (a1-b2) 
		 * @return coordinate for the 2D array which presents the board
		 * 		fail : return : -1
		 * 		else : coordinate={start y coordinate, start x coordinate, end y coordinate, end x coordinate}
		 * 			for the board[][] just board[coordinate[0]][coordinate[1]]
		 * 			e.g.:	searchC(a1-b2) returns {6,0,5,1}
		 */
		int[] coordinate={-1,-1,-1,-1};
		coordinate[0]=6-(moveString.charAt(1)-49);
		coordinate[1]=(java.lang.Character.getNumericValue(moveString.charAt(0))-10);
		coordinate[2]=6-(moveString.charAt(4)-49);
		coordinate[3]=(java.lang.Character.getNumericValue(moveString.charAt(3))-10);
		return coordinate;
	}

/***********************************MOVES*********************************************/
	
	public  int inReach(String[][] board, String moveString, String colour){
		//editor: Georg Stahn
		/** inReach - TODO return 0 when it should not
		 * @parameter 2D array 
		 * @parameter move String 
		 * @parameter colour of player
		 * 1.normal move 
		 * 		1.1 change itself for 1 
		 * 2.catch move
		 * 		2.1 change itself for 2 
		 * @return 	0 for nothing right 
		 * 			1 for 1.forward 
		 * 			-1 for 1.backwards 
		 * 			2 for 2.forward
		 * 			-2 for 2.backwards
		 */
			/*
			System.out.println(colour);
			System.out.println(moveString);
			*/
		int[] coordinates = searchC(moveString);
		for(int i=1; i<=2; i++){
			if(Math.abs(coordinates[0]-coordinates[2])==i && Math.abs(coordinates[i]-coordinates[3])==i){
				if(colour.equals("w")){
					if((coordinates[0]-coordinates[2])==i){
						return i;
					}else{
						return -i;
					}
				}else if(colour.equals("b")){
					if((coordinates[0]-coordinates[2])==-i){
						return i;
					}else{
						return -i;
					}
				}
			}
		}
		//TODO inReach returns 0
		System.err.println("error: inReach (return 0)");
		return 0;
	}
	public  String[][] catchMove(String[][] board, String moveString, String colour){
		//edtor:Georg Stahn
		/**
		 * @param 2d array
		 * @param moveString (a1-c3)
		 * @param colour "w":white, "b":black of the playing player
		 * 		between the location and the destination is a field where a chip of the other colour lays 
		 * 			-calls catchMust (takes that catched chip with him)
		 * @return updated array
		 */
		String fieldEmpty = null;
		int[] coordinates = searchC(moveString);
		//colour of the not playing player - enemies
		String enemies = null;
		if(colour == "w"){
			enemies = "b";
		}else{
			enemies = "w";
		}
		//System.err.println("enmie 100");
		//between the location and the destination is a field (alpa|beta) where a chip of the other colour lays
		int bx = (coordinates[0]+coordinates[2])/2;//x coordinate of this field
		int by = (coordinates[1]+coordinates[3])/2;//y coordinate of this field
		//System.err.println("bx:"+bx+" by:"+by);
		if(board[bx][by]==fieldEmpty){//is the between field free

			//System.err.println("empty:"+board[bx][by]);
			return board;
		}else if(board[bx][by].charAt(0)=='w' || board[bx][by].charAt(0)=='W'){
			if(enemies != "w"){

				//System.err.println("no enemie1");
				return board;//same colour
			}
			//System.err.println("the between field is topped by a WHITE stone(ENEMIE)");
		}else if(board[bx][by].charAt(0)=='b' || board[bx][by].charAt(0)=='B'){
			if(enemies != "b"){

				//System.err.println("no enemie2");
				return board;//same colour
			}
			//System.err.println("the between field is topped by a BLACK stone(ENEMIE)");
		}

		/**
		 * catch:
		 * l - location value/stones
		 * lx - x coordinate of location
		 * ly - y coordinates of location
		 * b - between value/stones
		 * bx - x coordinate of between
		 * by - y coordinates of between
		 * d - destination
		 * dx - x coordinate of destination
		 * dy - y coordinates of destination
		 * 
		 * //take the stones from board[bx][by] => b
		 * 		//Delete the "catched" stone from board[bx][by]
		 * //put it(b.sub) on(d) the destination field board[dx][dy]
		 * //put the stones(l) from the location field board[lx][ly] on(d) the destination field board[dx][dy]
		 * 		//Delete the "old" stones from board[lx][ly]
		 */
		int dx = coordinates[2];
		int dy = coordinates[3];
		int lx = coordinates[0];
		int ly = coordinates[1];
		//take the stones from board[bx][by] => b (with the frist)
		String l = board[lx][ly];
		String b = board[bx][by];
		//String d = board[dx][dy];
		//b.substring(0, 1);	//first stone of alpa beta
			///Delete the "catched" stone from board[bx][by]
		board[bx][by] = b.substring(1, b.length()); 
		//put it(b.sub) on(d) the destination field board[dx][dy]
		//put the stones(l) from the location field board[lx][ly] on(d) the destination field board[dx][dy]
		board[dx][dy] = l.concat(b.substring(0, 1));
		//Delete the "old" stones from board[lx][ly]board[coordinates[0]][coordinates[1]] = null;
		board[lx][ly] = null;
		//System.err.println("all good");
		//TODO schlagen muss?
		return board;
	}
	public boolean schlagenMuss(String[][]spielfeld, String spieler){
		//editor: Jonas Franz Schicke
		/**
		 * @param spielfeld
		 * @param spieler String! - colour
		 * @return  boolean: true - es bleibt der spieler an der reihe, da noch geschlagen werden kann/muss
		 * 					 false - rest 
		 */
		if(spieler.equals("w")){
			for(int m=0;m<7;m++){
				if(m==0||m==2||m==4||m==6){//jede zweite Reiche durchgehen
					if(spielfeld[m][0]!=null){
						String farbe = spielfeld[m][0].substring(0, 1);//oberste farbe
						try{
							if(spielfeld[m-2][2]==null&&spielfeld[m-1][1]!=null){//auf rechtem Platz steht einer und übernächster ist null
								if(!farbe.equalsIgnoreCase(spielfeld[m-1][1].substring(0,1)))return true;}//farbe und farbe des nächsten Feldes(rechts runter) sind nicht gleich
						}catch(ArrayIndexOutOfBoundsException exception){
	
						}
					};
					if(spielfeld[m][2]!=null){
						String farbe = spielfeld[m][2].substring(0, 1);//oberste farbe
						try{
							if(spielfeld[m-2][4]==null&&spielfeld[m-1][3]!=null){//auf rechtem Platz steht einer und übernächster ist null
								if(!farbe.equalsIgnoreCase(spielfeld[m-1][3].substring(0,1)))return true;}//farbe und farbe des nächsten Feldes(rechts runter) sind nicht gleich
							if(spielfeld[m-2][0]==null&&spielfeld[m-1][1]!=null){//auf rechtem Platz steht einer und übernächster ist null
	
								if(!farbe.equalsIgnoreCase(spielfeld[m-1][1].substring(0,1)))return true;}
						}catch(ArrayIndexOutOfBoundsException exception){
							
						}
					};
					if(spielfeld[m][4]!=null){
						String farbe = spielfeld[m][4].substring(0, 1);//oberste farbe
						try{
							if(spielfeld[m-2][6]==null&&spielfeld[m-1][5]!=null){//auf rechtem Platz steht einer und übernächster ist null
								if(!farbe.equalsIgnoreCase(spielfeld[m-1][5].substring(0,1)))return true;}
							if(spielfeld[m-2][2]==null&&spielfeld[m-1][3]!=null){//auf rechtem Platz steht einer und übernächster ist null
								if(!farbe.equalsIgnoreCase(spielfeld[m-1][3].substring(0,1)))return true;}
						}catch(ArrayIndexOutOfBoundsException exception){						
						}
					};
					if(spielfeld[m][6]!=null);
				}
				if(m==1||m==3||m==5){
					if(spielfeld[m][1]!=null){
						String farbe = spielfeld[m][1].substring(0, 1);//oberste farbe
						try{
							if(spielfeld[m-2][3]==null&&spielfeld[m-1][2]!=null){//auf rechtem Platz steht einer und übernächster ist null
								if(!farbe.equalsIgnoreCase(spielfeld[m-1][2].substring(0,1)))return true;}
						}catch(ArrayIndexOutOfBoundsException exception){						
						}
					};
					if(spielfeld[m][3]!=null){
						String farbe = spielfeld[m][3].substring(0, 1);//oberste farbe
						try{
							if(spielfeld[m-2][5]==null&&spielfeld[m-1][4]!=null){//auf rechtem Platz steht einer und übernächster ist null
								if(!farbe.equalsIgnoreCase(spielfeld[m-1][4].substring(0,1)))return true;}
							if(spielfeld[m-2][1]==null&&spielfeld[m-1][2]!=null){//auf rechtem Platz steht einer und übernächster ist null
								if(!farbe.equalsIgnoreCase(spielfeld[m-1][2].substring(0,1)))return true;}
						}catch(ArrayIndexOutOfBoundsException exception){						
						}
					};
					if(spielfeld[m][5]!=null){
						String farbe = spielfeld[m][5].substring(0, 1);//oberste farbe
						try{
							if(spielfeld[m-2][3]==null&&spielfeld[m-1][4]!=null){//auf rechtem Platz steht einer und übernächster ist null
								if(!farbe.equalsIgnoreCase(spielfeld[m-1][4].substring(0,1)))return true;}
						}catch(ArrayIndexOutOfBoundsException exception){						
						}
					};
				}
			}
		return false;
		}
		if(spieler.equals("b")){
			for(int m=0;m<7;m++){
				if(m==0||m==2||m==4||m==6){//jede zweite Reiche durchgehen
					if(spielfeld[m][0]!=null){
						String farbe = spielfeld[m][0].substring(0, 1);//oberste farbe
						try{
							if(spielfeld[m+2][2]==null&&spielfeld[m+1][1]!=null){//auf rechtem Platz steht einer und übernächster ist null
								if(!farbe.equalsIgnoreCase(spielfeld[m+1][1].substring(0,1)))return true;}
						}catch(ArrayIndexOutOfBoundsException exception){
							
						}
					};
					if(spielfeld[m][2]!=null){
						String farbe = spielfeld[m][2].substring(0, 1);//oberste farbe
						try{
							if(spielfeld[m+2][4]==null&&spielfeld[m+1][3]!=null){//auf rechtem Platz steht einer und übernächster ist null
								if(!farbe.equalsIgnoreCase(spielfeld[m+1][3].substring(0,1)))return true;}
							if(spielfeld[m+2][0]==null&&spielfeld[m+1][1]!=null){//auf rechtem Platz steht einer und übernächster ist null
								if(!farbe.equalsIgnoreCase(spielfeld[m+1][1].substring(0,1)))return true;}
						}catch(ArrayIndexOutOfBoundsException exception){
							
						}
					};
					if(spielfeld[m][4]!=null){
						String farbe = spielfeld[m][4].substring(0, 1);//oberste farbe
						try{
							if(spielfeld[m+2][6]==null&&spielfeld[m+1][5]!=null){//auf rechtem Platz steht einer und übernächster ist null
								if(!farbe.equalsIgnoreCase(spielfeld[m+1][5].substring(0,1)))return true;}
							if(spielfeld[m+2][2]==null&&spielfeld[m+1][3]!=null){//auf rechtem Platz steht einer und übernächster ist null
								if(!farbe.equalsIgnoreCase(spielfeld[m+1][3].substring(0,1)))return true;}
						}catch(ArrayIndexOutOfBoundsException exception){						
						}
					};
					if(spielfeld[m][6]!=null){
						String farbe = spielfeld[m][6].substring(0, 1);//oberste farbe
						try{
							if(spielfeld[m+2][4]==null&&spielfeld[m+1][5]!=null){//auf rechtem Platz steht einer und übernächster ist null
								if(!farbe.equalsIgnoreCase(spielfeld[m+1][5].substring(0,1)))return true;}
						}catch(ArrayIndexOutOfBoundsException exception){						
						}
					};
				}
				if(m==1||m==3||m==5){
					if(spielfeld[m][1]!=null){
						String farbe = spielfeld[m][1].substring(0, 1);//oberste farbe
						try{
							if(spielfeld[m+2][3]==null&&spielfeld[m+1][2]!=null){//auf rechtem Platz steht einer und übernächster ist null
								if(!farbe.equalsIgnoreCase(spielfeld[m+1][2].substring(0,1)))return true;}
						}catch(ArrayIndexOutOfBoundsException exception){						
						}
					};
					if(spielfeld[m][3]!=null){
						String farbe = spielfeld[m][3].substring(0, 1);//oberste farbe
						try{
							if(spielfeld[m+2][5]==null&&spielfeld[m+1][4]!=null){//auf rechtem Platz steht einer und übernächster ist null
								if(!farbe.equalsIgnoreCase(spielfeld[m+1][4].substring(0,1)))return true;}
							if(spielfeld[m+2][1]==null&&spielfeld[m+1][2]!=null){//auf rechtem Platz steht einer und übernächster ist null
								if(!farbe.equalsIgnoreCase(spielfeld[m+1][2].substring(0,1)))return true;}
						}catch(ArrayIndexOutOfBoundsException exception){						
						}
					};
					if(spielfeld[m][5]!=null){
						String farbe = spielfeld[m][5].substring(0, 1);//oberste farbe
						try{
							if(spielfeld[m+2][3]==null&&spielfeld[m+1][4]!=null){//auf rechtem Platz steht einer und übernächster ist null
								if(!farbe.equalsIgnoreCase(spielfeld[m+1][4].substring(0,1)))return true;}
						}catch(ArrayIndexOutOfBoundsException exception){						
						}
					};
				}
			}
			return false;
	}
		return false;
	}
	public  boolean isFinished(String[][] board /*TODO (in isFinished) ,Player p*/){
		//editor: Georg Stahn
		/**
		 * @param 2D array
		 * @return boolean - true:game is finished; false:game is still on the go
		 * //no stone there of the other colour
		 * 	- maybe count the number of stones one player has got -> attribute?
		 * //give up
		 * //draw
		 * //no opportunity left to move for one player
		 */
			//TODO give up
			/*
			if(p.giveUp)
				return true;
			//TODO draw
			if(p.game.players.value.draw && p.game.players.next.value.draw)
				return true;
			*/
			//no stone there of the other colour
			int w = 0, b=0;
			for(int h=0; h<board.length;h++){
				for(int j=0; j<board[0].length;j++){
					if(board[h][j]!=null){
						if(board[h][j].substring(0, 1)== "w" || board[h][j].substring(0, 1)=="W"){
							w++;
						}else if(board[h][j].substring(0, 1)== "b" || board[h][j].substring(0, 1)=="B"){
							b++;
						}
					}
				}
			}
			if(w == 0 || b == 0)
				return true;
			//no opportunity left to move for one player
			w=0;
			b=0;
			for(int h=0; h<board.length;h++){
				for(int j=0; j<board[0].length;j++){
					String nb1=" ",nb2=" ",nw1=" ",nw2=" ",cb1=" ",cb2=" ",cw1=" ",cw2=" ";
					for(int i=2; i>0;i--){
						if(h+i <=6 && j+i <= 6){//right(1) down(b)
							if(i==1)
								nb1=board[h+i][j+i];
							else
								cb2=board[h+i][j+i];
						}
						if(h+i <=6 && j-i >= 0){//left down
							if(i==1)
								nb2=board[h+i][j-i];
							else
								cb2=board[h+i][j-i];
						}
						if(h-i >=0 && j+i <= 6){//right up
							if(i==1)
								nw1=board[h-i][j+i];
							else
								cw1=board[h-i][j+i];
						}
						if(h-i >=0 && j-i >= 0){//left up
							if(i==1){
								nw2=board[h-i][j-i];
								//System.err.println("  nw2"+nw2+"*  ");
							}else
								cw2=board[h-i][j-i];
						}
					}
					if(board[h][j] != null && board[h][j].substring(0, 1)== "w"){
						if(nw1==null || nw2==null || (cw1==null && (nw1.substring(0, 1) == "b" || nw1.substring(0, 1) == "B")) || (cw2==null && (nw2.substring(0, 1) == "b" || nw2.substring(0, 1) == "B")))
							w++;//System.out.println("x:"+j+" y:"+h+" w1: "+w+" nw1:"+nw1+" nw2:"+nw2);
					}else if(board[h][j] != null && board[h][j].substring(0, 1)== "b"){
						if(nb1==null || nb2==null || (cb1==null && (nb1.substring(0, 1) == "w" || nb1.substring(0, 1) == "W")) || (cb2==null && (nb2.substring(0, 1) == "w" || nb2.substring(0, 1) == "W")))
							b++;//System.out.println("x:"+j+" y:"+h+" b1: "+b);
					}else if(board[h][j] != null && board[h][j].substring(0, 1)== "W"){
						if(nb1==null || nb2==null || (cb1==null && (nb1.substring(0, 1) == "w" || nb1.substring(0, 1) == "W")) || (cb2==null && (nb2.substring(0, 1) == "w" || nb2.substring(0, 1) == "W")))
							w++;//System.out.println("x:"+j+" y:"+h+" w2: "+w);
					}else if(board[h][j] != null && board[h][j].substring(0, 1)== "B"){
						if(nw1==null || nw2==null || (cw1==null && (nw1.substring(0, 1) == "b" || nw1.substring(0, 1) == "B")) || (cw2==null && (nw2.substring(0, 1) == "b" || nw2.substring(0, 1) == "B")))
							b++;//System.out.println("x:"+j+" y:"+h+" b2: "+b);
					}
				}
			}
			if(w == 0 || b == 0)
				return true;
			return false;
		}
}
