//editor:Georg Stahn
package de.tuberlin.sese.swtpp.gameserver.model.lasca;

import java.io.Serializable;

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
		boolean isPromoted=false;
		if(colour=="w"){
			try{
				for(int i = 0; i<7;i++){
					if(board[0][i].getField()!=null){
						if(board[0][i].getFirst().equals("w")){
							board[0][i].setFirst("W");
//							isPromoted = true;
							return true;
						}
					}
				}
			}catch(Exception exception){
				System.out.println("EXCEPTION in toOfficer w!");
//				isPromoted = false;
			}
		}
		if(colour=="b"){
			try{
				for(int i = 0; i<7;i++){
					if(board[6][i].getField()!=null){
						if(board[6][i].getFirst().equals("b")){
							board[6][i].setFirst("B");
//							isPromoted = true;
							return true;
						}
					}
				}
			}catch(Exception exception){
				System.out.println("EXCEPTION in toOfficer b!");
//				isPromoted = false;
			}
		}
		return isPromoted;
	}
	private void normalMove(){
		getD().setField(getL().getField());
		getL().delete();
	}
	//TODO 2 BIG problem is when the gameboard border is reached! need a good methode for that!
	//TODO 2 catchMove BIG
	private  void catchMove(){
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
		if(getB().equals(fieldEmpty) || getB().getField() == fieldEmpty){//is the between field free
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
		if((x%2 + y%2)!=1 && 0<=x && x<=6 && 0<=y && y<=6)
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
		if(getL()==null){
			System.err.println("(lm - validMove)location is empty");
			return false;
		}
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
		//6,5.darf nach hinten? 
		if(inReach()<0 && !getL().isOfficer()){
			System.err.println("(lm - validMove)move only possible for an officer(x)");
			return false;
		}
		//7.schlagen!
		if(Math.abs(inReach())==2){
			System.out.println("(lm - validMove)catchMove will preform");
			catchMove();
			return true;
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
		if(stone.equals(colour))
			return true;
		if(colour.equals("w") && stone.equals("W"))
			return true;
		if(colour.equals("b") && stone.equals("B"))
			return true;
		return false;
	}
	
	public boolean 	mustCatch(){
		//editor: Georg Stahn
		/** mustCatch()
		 * @return boolean:
		 * 		true: if this player has to do another catchMove(not backwards) after a catchMove or instead of a normalMove this has to be done if it is possible
		 * 		false: else
		 */
		if(mustCatchNormal() || mustCatchCatch())
			return true;
		return false;
	}
	
		
	private boolean mustCatchCatch() {
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
			LascaField[] possible = possibleMove(getD().isOfficer(), true);
			LascaField[] between = possibleMove(getD().isOfficer(), false);
			if(getXD()-getXL()==2){//TODO !!! just for white right?
				//moveString moves right
				if(getD().isOfficer()){
					possible[1]=null;
				}else{
					possible[3]=null;
				}
			}else{
				if(getD().isOfficer()){
					possible[0]=null;
				}else{
					possible[2]=null;
				}
			}
			for(int i=0;i<possible.length;i++){
				if(possible[i]!=null && possible[i].getField()==null && between[i]!=null && between[i].getFirst()!=null && !rightPlayer(between[i].getFirst())){
					return true;
				}
			}
		}
		return false;
	}


	private boolean mustCatchNormal() {
		if(Math.abs(inReach())==1){//TODO !!! her it needs to be tested for not only getD() rather then for all fields where the top stone is of the playing colour 
			LascaField[] possible = possibleMove(getD().isOfficer(), false);
			for(int i=0;i<possible.length;i++){
				if(possible[i]!=null){//possible moves
					if(possible[i].getField()==null){//new destination is free
						return false;
					}
				}
			}
		}
		return false;
	}
	
	
	private LascaField[] possibleMove(boolean isOfficer,boolean isCatch){
		int reach = 1; 
		if(isCatch)
			reach = 2;
		boolean[] possible4={false,false,false,false};
		LascaField[] fields = {null,null,null,null};
		for(int i = 0; i<possible4.length;i++){
			try{
				if(isOfficer && colour.equals("b") || colour.equals("w")){
					if(i==0)
						fields[i] = board[getYD()-reach][getXD()+reach];
					if(i==1)
						fields[i] = board[getYD()-reach][getXD()-reach];
				}
				if(isOfficer && colour.equals("w") || colour.equals("b")){
					if(i==2)
						fields[i] = board[getYD()+reach][getXD()+reach];
					if(i==3)
						fields[i] = board[getYD()+reach][getXD()-reach];
				}
				possible4[i] = true;
			}catch(ArrayIndexOutOfBoundsException exception){
				fields[i] = null;
//				System.err.println("in possibleMove "+i+" is not on board");
			}
		}
		return fields;
	}



	

	private int searchC(String coordinate){
		//editor: Georg Stahn
		/** searchC
		 * @parameter int coordinate
		 * @return coordinate as int
		 * 		fail : -1
		 * 		else : coordinate
		 */
		if(coordinate.length()==1){
			int c = java.lang.Character.getNumericValue(coordinate.charAt(0));
			if(c<10)
				return 7-c;//numbers stay numbers
			else
				return c-10;//little letters begin after 9
		}
		return -1;
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
				}else if(colour.equals("b")){
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
