package de.tuberlin.sese.swtpp.gameserver.model.lasca;

public class NewMethods2 {
	//test fuer k´jonas

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
	String d = board[dx][dy];
	//b.substring(0, 1);	//first stone of alpa beta
		///Delete the "catched" stone from board[bx][by]
	board[bx][by] = b.substring(1, b.length()); 
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

public static String[][] normalMove(){
	return null;
}

}
