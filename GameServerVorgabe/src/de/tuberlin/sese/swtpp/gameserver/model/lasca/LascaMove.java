//editor:Georg Stahn
package de.tuberlin.sese.swtpp.gameserver.model.lasca;

import java.io.Serializable;
import java.util.LinkedList;

import de.tuberlin.sese.swtpp.gameserver.model.Player;
import de.tuberlin.sese.swtpp.gameserver.model.lasca.LascaField;

public class LascaMove implements Serializable {
	private static final long serialVersionUID = 2467695635164175201L;
	
	private String moveString;
	private LascaField[][] board;
	private String colour;
	
	//constructor +  moveString
	public LascaMove(String moveString, LascaField[][] board, Player player, Player whitePlayer){
		this.moveString = moveString;
		this.board = board;
		if(player == whitePlayer)
			this.colour = "w";
		else
			this.colour = "b";
	}
	

	/*START*********************************GETTER**************************************START*/

	public String getColour(){
		return colour;
	}
	
	public  int getXL(){
		//editor: Georg Stahn
		/** getXL
		 * @return coordinate as int
		 * 		fail : -1
		 * 		else : coordinate
		 */
		return searchC(moveString.substring(0, 1));
	}
	
	public  int getYL(){
		//editor: Georg Stahn
		/** getYL
		 * @return coordinate as int
		 * 		fail : -1
		 * 		else : coordinate
		 */
		return searchC(moveString.substring(1, 2));
	}
	
	public  int getXD(){
		//editor: Georg Stahn
		/** getXD
		 * @return coordinate as int
		 * 		fail : -1
		 * 		else : coordinate
		 */
		return searchC(moveString.substring(3, 4));
	}
	
	public  int getYD(){
		//editor: Georg Stahn
		/** getYD
		 * @return coordinate as int
		 * 		fail : -1
		 * 		else : coordinate
		 */
		return searchC(moveString.substring(4, 5));
	}
	
	//getLocation
	public LascaField getL(){
		return board[getYL()][getXL()];
	}
	
	//getDestination
	public LascaField getD(){
		return board[getYD()][getXD()];
	}
	
	//getBetween = field between location and destination
	public LascaField getB(){
		
		return board[(getYD()+getYL())/2][(getXD()+getXL())/2];
		//between the location and the destination is a field (alpa|beta) where a chip of the other colour lays
		//int bx = (getXL()+getXD())/2;//x coordinate of this field
		//int by = (getYL()+getYD())/2;//y coordinate of this field
		//System.err.println("bx:"+bx+" by:"+by);
		
	}

	/*END************************************GETTER**********************************END*/
	
	/****************************************METHODES************************************/
	public boolean toOfficer(){
		try{
			for(int i = 0; i<7;i++){
				if(board[officerY(colour)][i].getField()!=null && board[officerY(colour)][i].getFirst().equals(colour)){
					if("w".equals(colour))
						board[officerY(colour)][i].setFirst("W");
					else
						board[officerY(colour)][i].setFirst("B");
					return true;
				}
			}
		}catch(Exception exception){
			System.out.println("EXCEPTION in toOfficer w!");
		}
		return false;
	}
	private int officerY(String colour){
		if("w".equals(colour))
			return 0;
		return 6;
//		if("b".equals(colour))
//			return 6;
//		return -1;
	}
	private void normalMove(){
		getD().setField(getL().getField());
		getL().delete();
	}
	private void catchMove(){
		//edtor:Georg Stahn
		/**
		 * @param colour "w":white, "b":black of the playing player
		 * 		between the location and the destination is a field where a chip of the other colour lays 
		 * 			-calls catchMust (takes that catched chip with him)
		 * @return updated array
		 */
		String fieldEmpty = null;
		if(/*getB().equals(fieldEmpty) ||*/ getB().getField() == fieldEmpty){//is the between field free
			return;
		}else if(getB().getField().charAt(0)=='w' || getB().getField().charAt(0)=='W'){
			if(colour.equals("w")){
				return;//same colour
			}
			//System.err.println("the between field is topped by a WHITE stone(ENEMIE)");
		}else if(getB().getField().charAt(0)=='b' || getB().getField().charAt(0)=='B'){
			if(colour.equals("b")){
				return;//same colour
			}
		}
		getD().setField(getL().getField());
		getD().addStone(getB().doCatch());
		getL().delete();
	}
	
	
	public boolean validFields(){//of the Move
		return valid(getXL(), getYL()) && valid(getXD(), getYD());
	}
	public boolean valid(int x, int y){
		if((x%2 + y%2)!=1 /*&& 0<=x && x<=6 && 0<=y && y<=6*/)
			return true;
		return false;
	}
	public boolean validMove(){
		/**editor: Georg Stahn
		 * @param bekommt moveString = b1-c2 
		 * -1. fen zu 2D array (chessboard) 
		 * -1,5. 2D zu fen 
		 * -2.suchKoordinaten 
		 * 3.frei 
		 * 5.valides Feld(nicht von b1-b2) 
		 * 6.inreichweite 
		 * /\ 
		 * 6,5.darf nach hinten? 
		 * /\ 
		 * 7.schlagen! 
		 * 8.schlagen muss methode
		 * @return boolean //wenn einfacher zug
		 */
//		if(getL()==null){
//			System.err.println("(lm - validMove)location is empty1");
//			return false;
//		}
//		if(getL().getField()==null){
//			System.err.println("(lm - validMove)location is empty2");
//			return false;
//		}
		//3.frei
		if(getD().getField()!=null){
			System.err.println("(lm - validMove)destination is not empty "+getD().getField());
			return false;
		}
		//5.valides Feld(nicht von b1-b2)
		if(!validFields()){
			System.err.println("(lm - validMove)no valid field");
			return false;
		}
		//6.inreichweite
		if(inReach()==0){
			System.err.println("(lm - validMove)destination not in reach");
			return false;
		}
//		//6,5.darf nach hinten? 
//		if(inReach()<0 && !getL().isOfficer()){
//			System.err.println("(lm - validMove)move only possible for an officer(x)");
//			return false;
//		}
		//7.schlagen!
		if(Math.abs(inReach())==2){
			System.out.println("(lm - validMove)catchMove will preform");
			catchMove();
			return true;
		}
		if(mustCatchNormal()){
			System.out.println("(lm - validMove)it is possible to do a catch move");
			return false;
		}
		System.out.println("(lm - validMove)normalMove will preform");
		normalMove();
		return true;
	}
	public boolean rightPlayer(){
		String first = getL().getFirst();
		if(first == null)
			return false;
		return rightPlayer(first);
	}
	private boolean rightPlayer(String stone){
		if(colour.equals(stone))
			return true;
		if("w".equals(colour) && "W".equals(stone))
			return true;
		if("b".equals(colour) && "B".equals(stone))
			return true;
		return false;
	}
	
//	public boolean 	mustCatch(){
//		//editor: Georg Stahn
//		/** mustCatch()
//		 * @return boolean:
//		 * 		true: if this player has to do another catchMove(not backwards) after a catchMove or instead of a normalMove this has to be done if it is possible
//		 * 		false: else
//		 */
//		if(mustCatchNormal() || mustCatchCatch())
//			return true;
//		return false;
//	}
	public boolean mustCatchCatch() {
		/** mustCatchCatch()
		 * @return boolean
		 * 		true: the tryed move is a catch Move
		 * 				take destination and new board
		 * 					look if any of the 2 or 4 catchMoves are possible
		 * 						new destination free and valid
		 * 							between field is the other colour
		 * 		false: else
		 */
		if(Math.abs(inReach())==2){
			LascaField[] possible = possibleMovesOf(getYD() ,getXD() ,getD().isOfficer(), true);
			LascaField[] between = possibleMovesOf(getYD() ,getXD() ,getD().isOfficer(), false);
			for(int i=0;i<possible.length;i++){
				if(possible[i]!=null
						&& possible[i]!=getL()
						&& possible[i].getField()==null
						&& between[i]!=null
						&& between[i].getFirst()!=null 
						&& !rightPlayer(between[i].getFirst())){
					return true;
				}
			}
		}
		return false;
	}
	private boolean mustCatchNormal() {
		if(numberOfVM(colour)>=10000)
			return true;
		return false;
	}

	public  boolean isFinished(){
		//editor: Georg Stahn
		/** isFinished
		 * @return boolean - true:game is finished; false:game is still on the go
		 * //no stone there of the other colour
		 * 	- maybe count the number of stones one player has got -> attribute?
		 * //give up
		 * //draw
		 * //no opportunity left to move for one player
		 */
		//no stone there of the other colour
		if(noStoneOfOneColour())
			return true;
		//no opportunity left to move for one player
		if(noMovePossible())
			return true;
		return false;
	}
	//noStoneOfTheOtherColour()
	private boolean noStoneOfOneColour(){
		int w = 0, b=0;
		for(int h=0; h<board.length;h++){
			for(int j=0; j<board[0].length;j++){
				if(board[h][j]!=null){
					if("w".equals(board[h][j].getFirst()) || "W".equals(board[h][j].getFirst())){
						w++;
					}else if("b".equals(board[h][j].getFirst()) || "B".equals(board[h][j].getFirst())){
						b++;
					}
				}
			}
		}
		if(b==0 || w==0)
			return true;
		return false;
	}
	//no opportunity left to move for one player
	private boolean noMovePossible(){
		if("w".equals(colour))
			return numberOfVM("b")==0;
		else
			return numberOfVM("w")==0;
	}
	public int numberOfVM(String colour){
		/** numberOfVvalidMoves
		* @return
		*	0 noMovepossible
		*	>10000 catchMove is possible
		*	the 3 last numbers give me the number of moves which are possible  
		*/
		//all fields with same colour on the top
			//LascaField[] allFieldsWith(boolean white)
		LinkedList<LascaField> allFields = allFieldsWith(colour);
		int result=0;
		while(allFields.size()>0){
			LascaField field = allFields.pop();
			if(isPoMoOf(field, false)){
				result++;
			}else if(isPoMoOf(field, true)){
				if(result>10000)
					result++;
				else
					result += 10001;
			}
		}
		//go over all of them
			//is normal Move possible?
				//officer
			//is catch Move possible? -- same as mustCatchNormal
				//officer
		return result;
	}
	//is possible move of
	private boolean isPoMoOf(LascaField field, boolean isCatch){
		LascaField[] fields = possibleMovesOf(getCoordinates(field)[1], getCoordinates(field)[0], field.isOfficer(), isCatch);
		LascaField[] fieldsB = possibleMovesOf(getCoordinates(field)[1], getCoordinates(field)[0], field.isOfficer(), false);
		for(int i = 0;i<fields.length; i++){
			if(isCatch 
					&& fields[i]!=null 
					&& fields[i].getField()==null 
//					&& fieldsB[i]!=null 
					&& !rightPlayer(fieldsB[i].getFirst()))
				return true;
			if(!isCatch && fields[i]!=null && fields[i].getField()==null)
				return true;
		}
		return false;
	}


	private int[] getCoordinates(LascaField field){
		for(int y=0; y<board.length-1;y++){
			for(int x=0; x<board[0].length;x++){
				if(board[y][x] == field){
					int[] coordinates = {x,y};
					return coordinates;
				}
			}
		}
		System.err.println("(lm - getCoor.) last");
		int[] coordinates = {6,6};
		return coordinates;
	}


	//catchMove is possible 
	//all fields with same colour on the top
	private LinkedList<LascaField> allFieldsWith(String colour){
		LinkedList<LascaField> allFields = new  LinkedList<LascaField>();
		for(int y=0; y<board.length;y++){
			for(int x=0; x<board[0].length;x++){
				if(board[y][x] != null && board[y][x].stoneWith(colour)){
					allFields.add(board[y][x]);
				}
			}
		}
		return allFields;
	}
	private LascaField[] possibleMovesOf(int y, int x, boolean isOfficer,boolean isCatch){
		int reach = 1; 
		if(isCatch)
			reach = 2;
//		boolean[] possible4={false,false,false,false};
		LascaField[] fields = {null,null,null,null};
		String firstOfL = board[y][x].getFirst();
		for(int i = 0; i<fields.length;i++){
			try{
				fields[i] = possibleMoveOf(x,y,reach,i);
				if("w".equals(firstOfL) && i%2==1){
					fields[i] = null;
				}else if("b".equals(firstOfL) && i%2==0){
					fields[i] = null;
				}
//				if(isOfficer && colour.equals("B") || colour.equals("w")){
//					fields[i] = possibleMoveOf(x,y,reach,i);
//				}
//				if(isOfficer && colour.equals("w") || colour.equals("b")){
//					fields[i] = possibleMoveOf(x,y,reach,i);
//				}
//				possible4[i] = true;
			}catch(ArrayIndexOutOfBoundsException exception){
				fields[i] = null;
//				System.err.println("in possibleMove "+i+" is not on board");
			}
		}
		return fields;
	}
	private LascaField possibleMoveOf(int x, int y, int reach, int i){
		if(i==0)
			return board[y-reach][x+reach];
		if(i==1)
			return board[y+reach][x-reach];
		if(i==2)
			return board[y-reach][x-reach];
//		if(i==3)
			return board[y+reach][x+reach];
//		System.err.println("notBackwards(lm)");
//		return null;
	}
	

	private int searchC(String coordinate){
		//editor: Georg Stahn
		/** searchC
		 * @parameter int coordinate
		 * @return coordinate as int
		 * 		fail : -1
		 * 		else : coordinate
		 */
//		if(coordinate.length()==1){
			int c = java.lang.Character.getNumericValue(coordinate.charAt(0));
			if(c<10)
				return 7-c;//numbers stay numbers
			else
				return c-10;//little letters begin after 9
//		}
//		return -1;
	}
	public int inReach(){
		//editor: Georg Stahn
		/** inReach
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
		for(int i=1; i<=2; i++){
//			System.out.println("inReach: i="+i);
			if(Math.abs(this.getXL()-this.getXD())==i && Math.abs(this.getYL()-this.getYD())==i){
//				System.out.println("inReach: it is a diagonal move");
				if(colour.equals("w")){
//					System.out.println("inReach: colour is white");
					if((this.getYD()-this.getYL())==i){
//						System.out.println("inReach: "+i);
						return -i;
					}else{
//						System.out.println("inReach: -"+i);
						return i;
					}
				}else/* if(colour.equals("b"))*/{
					if((this.getYD()-this.getYL())==-i){
						return -i;
					}else{
						return i;
					}
				}
			}
		}
		System.err.println("error: inReach (return 0)");
		return 0;
	}
}
