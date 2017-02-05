package de.tuberlin.sese.swtpp.gameserver.model.lasca;

public class NewMethods2 {

	public static String[][] fenToArray(String state){
		
		String[][] spielfeld = new String[7][7];
		int x=0;
		int y=0;
		
		while(state.length()>0){
			String start = state.substring(0,1);
			if((x==1||x==3||x==5)&&y==0)y++;
			switch(start){
			case "b": 	if(x<7){
						if(spielfeld[x][y]==null)spielfeld [x][y] = "b";
						else spielfeld[x][y] = spielfeld[x][y] + "b";
						state = state.substring(1,state.length());
						break;
						}
			case "B": 	if(x<7){
						if(spielfeld[x][y]==null)spielfeld [x][y] = "B";
						else spielfeld[x][y] = spielfeld[x][y] + "B";
						state = state.substring(1,state.length());
						break;
						}
			case "w": 	if(x<7){
						if(spielfeld[x][y]==null)spielfeld [x][y] = "w";
						else spielfeld[x][y] = spielfeld[x][y] + "w";
						state = state.substring(1,state.length());
						break;
					}
			case "W": 	if(x<7){
						if(spielfeld[x][y]==null)spielfeld [x][y] = "W";
						else spielfeld[x][y] = spielfeld[x][y] + "W";
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
		return spielfeld;
		
		
	}
	
	public static String[][] catchMove(String[][] board, String moveString, String colour){
	
	// return board;
	/**
	* @param 2d array
	* @param moveString (a1-c3)
	* @param colour "w":white, "b":black of the playing player
	* between the location and the destination is a field where a chip of the other colour lays
	* -calls catchMust (takes that catched chip with him)
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
	//between the location and the destination is a field (alpa|beta) where a chip of the other colour lays
	int bx = (coordinates[0]+coordinates[2])/2;//x coordinate of this field
	int by = (coordinates[1]+coordinates[3])/2;//y coordinate of this field
	if(board[bx][by]==fieldEmpty){//is the between field free
	return board;
	}else if(board[bx][by].charAt(0)=='w' || board[bx][by].charAt(0)=='W'){
	if(enemies != "w"){
	return board;//same colour
	}
	System.out.println("the between field is topped by a WHITE stone(ENEMIE)");
	}else if(board[bx][by].charAt(0)=='b' || board[bx][by].charAt(0)=='B'){
	if(enemies == "b"){
	return board;//same colour
	}
	System.out.println("the between field is topped by a BLACK stone(ENEMIE)");
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
		//b.substring(0, 1);	//first stone of alpa beta
			///Delete the "catched" stone from board[bx][by]
		if(b.length()>1)
			board[bx][by] = b.substring(1, b.length()); 
		else
			board[bx][by] = null;
		//put it(b.sub) on(d) the destination field board[dx][dy]
		//put the stones(l) from the location field board[lx][ly] on(d) the destination field board[dx][dy]
		board[dx][dy] = l.concat(b.substring(0, 1));
		//Delete the "old" stones from board[lx][ly]board[coordinates[0]][coordinates[1]] = null;
		board[lx][ly] = null;
		//TODO schlagen muss
		return board;
	}
	public static int[] searchC(String moveString){
		/**
		 * @parameter moveString (a1-b2) 
		 * @return coordinate for the 2D array which presents the board
		 * 		fail : return : -1
		 * 		else : coordinate={start character, start number, end character, end number}
		 * 			e.g.:	searchC(a1-b2) returns {6,0,5,1}
		 * 					to get the position a1 from board do easy : board[coordinate[0]][coordinate[1]] - location
		 * 					to get the position b2 from board do easy : board[coordinate[2]][coordinate[3]] - location
		 */
		int[] coordinate={-1,-1,-1,-1};
		
		coordinate[0]=6-(moveString.charAt(1)-49);
		coordinate[1]=(java.lang.Character.getNumericValue(moveString.charAt(0))-10);
		coordinate[2]=6-(moveString.charAt(4)-49);
		coordinate[3]=(java.lang.Character.getNumericValue(moveString.charAt(3))-10);
		
		return coordinate;
	}
	
	public static String arrayToString(String[][] spielfeld){
		String fenString = "";
		for(int m=0;m<7;m++){
			if(m==0||m==2||m==4||m==6){
				if(spielfeld[m][0]!=null) fenString = fenString + spielfeld[m][0] + ",";
				else fenString = fenString + ",";
				if(spielfeld[m][2]!=null) fenString = fenString + spielfeld[m][2] + ",";
				else fenString = fenString + ",";
				if(spielfeld[m][4]!=null) fenString = fenString + spielfeld[m][4] + ",";
				else fenString = fenString + ",";
				if(spielfeld[m][6]!=null) fenString = fenString + spielfeld[m][6];
			}
			if(m==1||m==3||m==5){
				if(spielfeld[m][1]!=null) fenString = fenString + spielfeld[m][1] + ",";
				else fenString = fenString + ",";
				if(spielfeld[m][3]!=null) fenString = fenString + spielfeld[m][3] + ",";
				else fenString = fenString + ",";
				if(spielfeld[m][5]!=null) fenString = fenString + spielfeld[m][5];
			}
			if(m!=6)fenString = fenString + "/";
		}
		return fenString;
	}
	
	public static String[][] normalMove(String moveString, String[][] board, String colour){
		/** normalMove
		 * @parameter moveString
		 * @parameter 2D array
		 * does the normal Move if it can be performed!
		 * 		checks:
		 * 			right player - see rightColour
		 * 			tries normal Move
		 * 			schlagenMuss
		 * 			officer Move
		 * 			destination is free
		 * 		normalMove:
		 * 			swap destination and location
		 * 	@return 2D array
		 */
		int dir = inReach(board, moveString, colour);
		if(rightColour(moveString, board, colour)){
			if(Math.abs(dir)==1){
				if(schlagenMuss(board, colour))
					return board;
				int[] coordinates = searchC(moveString);
				if(dir == 1 || (dir == -1 && board[coordinates[0]][coordinates[1]].charAt(0)>'A')){
					if(board[coordinates[2]][coordinates[3]]==null){
						board[coordinates[2]][coordinates[3]]=board[coordinates[0]][coordinates[1]];
						board[coordinates[0]][coordinates[1]]=null;
					}
				}
			}
		}
		return board;
	}
	
	private static boolean rightColour(String moveString, String[][] board, String colour){
		int[] coordinates = searchC(moveString);
		String bc;
		if(colour == "w"){
			bc = "W";
		}else{
			bc = "B";
		}
		return board[coordinates[0]][coordinates[1]] != null && (board[coordinates[0]][coordinates[1]].substring(0, 1).equals(colour) || board[coordinates[0]][coordinates[1]].substring(0, 1).equals(bc));
	}
	
	public static int inReach(String[][] board, String moveString, String colour){
		/**
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
				if(colour=="w"){
					if((coordinates[1]-coordinates[3])==i){
						return i;
					}else{
						return -i;
					}
				}else if(colour=="b"){
					if((coordinates[1]-coordinates[3])==-i){
						return i;
					}else{
						return -i;
					}
				}
			}
		}
		return 0;
	}
	
	public static boolean schlagenMuss(String[][]spielfeld, String spieler){
		/**
		 * @param spielfeld
		 * @param spieler
		 * @return  boolean: true - es bleibt der spieler an der reihe, da noch geschlagen werden kann/muss
		 * 					 false - rest 
		 */
		if(spieler=="w"){
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
		if(spieler=="b"){
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

}
