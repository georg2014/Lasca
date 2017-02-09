//editor: Georg Stahn
package de.tuberlin.sese.swtpp.gameserver.model.lasca;

import java.io.Serializable;

public class LascaBoard implements Serializable {
	private static final long serialVersionUID = 2467695635164175201L;
	
	private LascaField[][] gameboard = {{new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null)},
										{new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null)},
										{new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null)},
										{new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null)},
										{new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null)},
										{new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null)},
										{new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null),new LascaField(null)}};
	/*******************************BOARD***************************************
	 N	00	01					06
	 U	10
	 M
	 B
	 E
	 R
	 S	60						66
	 	L	E	T	T	E	R	S
	 This is how we designed the board ;)
	****************************************************************************/
	
	//constructor
	
	//TODO fen2array BIG
	public LascaBoard(String state){
		//editor: Jonas Franz Schicke, Georg Stahn
		int x=0;
		int y=0;
		
		while(state.length()>0){
			//System.err.println("state.length(): "+state.length());
			String start = state.substring(0,1);
			//System.err.println("start = state.substring(0,1) "+state.substring(0,1));
			if((x%2 == 1)&&y==0)
				y++;
			if(start.equals("b")||start.equals("w")||start.equals("B")||start.equals("W")){
				if(helpConstructor(x,y,start)){
					state = state.substring(1,state.length());
				}
			}else if(start.equals(",")){
				y=y+2;
				state = state.substring(1,state.length());
			}else if(start.equals("/")){
				x++;
				y=0;
				state = state.substring(1,state.length());
			}
		}
	}
	
	//helpConstructor
	public boolean helpConstructor(int x, int y, String stone){
		//editor: Georg Stahn
		if(x<7){
			gameboard[x][y].addStone(stone);
			return true;
		}
		return false;
	}
	
	
	//array2fen
	public  String array2fen(){
		//editor: Jonas Franz Schicke, Georg Stahn
		/** array2fen
		 * @return fenString array[x][y] x : numbers y : letters both from 0 to 6 
		 */
		String fenString = "";
		for(int m=0;m<7;m++){
			if(m%2==0){
				for(int i=0; i<7;i=i+2){
					fenString = helpArray2fen(i, m, fenString);
				}
			}
			if(m%2==1){
				for(int i=1; i<7;i=i+2){
					fenString = helpArray2fen(i, m, fenString);
				}
			}
			if(m!=6)fenString = fenString + "/";
		}
		return fenString;
	}
	//helpArray2fen
	public String helpArray2fen(int i, int m, String fenString){
		if(i<5){
			if(gameboard[m][i].getField()!=null) return fenString + gameboard[m][i].getField() + ",";
			else return fenString + ",";
		}else{
			if(gameboard[m][i].getField()!=null) return fenString + gameboard[m][i].getField();
		}
		return fenString;
	}
	
	
	//getGameboard
	public LascaField[][] getGameboard(){
		return gameboard;
	}
	
	//TODO isFinished BIG
	public  boolean isFinished(){
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
		//no stone there of the other colour
		if(noStoneOfOneColour())
			return true;
		//no opportunity left to move for one player
		int w=0;
		int b=0;
		for(int h=0; h<gameboard.length;h++){
			for(int j=0; j<gameboard[0].length;j++){
				//all valid moves
				String nb1=" ",nb2=" ",nw1=" ",nw2=" ",cb1=" ",cb2=" ",cw1=" ",cw2=" ";
				allValideMoves( nb1, nb2, nw1, nw2, cb1, cb2, cw1, cw2, h, j);
				//check moves 4 {"w","W","b","B"} x {nomalMoveLeft,nomalMoveRight,catchMoveLeft,catchMoveRight}
					//for B is also w possible etc.
				if(gameboard[h][j] != null && gameboard[h][j].getFirst()== "w"){
					if(nw1==null || nw2==null || (cw1==null && (nw1.substring(0, 1) == "b" || nw1.substring(0, 1) == "B")) || (cw2==null && (nw2.substring(0, 1) == "b" || nw2.substring(0, 1) == "B")))
						w++;//System.out.println("x:"+j+" y:"+h+" w1: "+w+" nw1:"+nw1+" nw2:"+nw2);
				}else if(gameboard[h][j] != null && gameboard[h][j].getFirst()== "b"){
					if(nb1==null || nb2==null || (cb1==null && (nb1.substring(0, 1) == "w" || nb1.substring(0, 1) == "W")) || (cb2==null && (nb2.substring(0, 1) == "w" || nb2.substring(0, 1) == "W")))
						b++;//System.out.println("x:"+j+" y:"+h+" b1: "+b);
				}else if(gameboard[h][j] != null && gameboard[h][j].getFirst()== "W"){
					if(nb1==null || nb2==null || (cb1==null && (nb1.substring(0, 1) == "w" || nb1.substring(0, 1) == "W")) || (cb2==null && (nb2.substring(0, 1) == "w" || nb2.substring(0, 1) == "W")))
						w++;//System.out.println("x:"+j+" y:"+h+" w2: "+w);
				}else if(gameboard[h][j] != null && gameboard[h][j].getFirst()== "B"){
					if(nw1==null || nw2==null || (cw1==null && (nw1.substring(0, 1) == "b" || nw1.substring(0, 1) == "B")) || (cw2==null && (nw2.substring(0, 1) == "b" || nw2.substring(0, 1) == "B")))
						b++;//System.out.println("x:"+j+" y:"+h+" b2: "+b);
				}
			}
		}
		if(w == 0 || b == 0)
			return true;
		return false;
	}
	//noStoneOfTheOtherColour()
	public boolean noStoneOfOneColour(){
		int w = 0, b=0;
		for(int h=0; h<gameboard.length;h++){
			for(int j=0; j<gameboard[0].length;j++){
				if(gameboard[h][j]!=null){
					if(gameboard[h][j].getFirst()== "w" || gameboard[h][j].getFirst()=="W"){
						w++;
					}else if(gameboard[h][j].getFirst()== "b" || gameboard[h][j].getFirst()=="B"){
						b++;
					}
				}
			}
		}
		if(b==0 || w==0)
			return true;
		return false;
	}
	//TODO allValideMoves() BIG
	public void allValideMoves(String nb1,String nb2,String nw1,String nw2,String cb1,String cb2,String cw1,String cw2,int h,int j){
		for(int i=2; i>0;i--){
			if(h+i <=6 && j+i <= 6){//right(1) down(b)
				if(i==1)
					nb1=gameboard[h+i][j+i].getField();
				else
					cb2=gameboard[h+i][j+i].getField();
			}
			if(h+i <=6 && j-i >= 0){//left down
				if(i==1)
					nb2=gameboard[h+i][j-i].getField();
				else
					cb2=gameboard[h+i][j-i].getField();
			}
			if(h-i >=0 && j+i <= 6){//right up
				if(i==1)
					nw1=gameboard[h-i][j+i].getField();
				else
					cw1=gameboard[h-i][j+i].getField();
			}
			if(h-i >=0 && j-i >= 0){//left up
				if(i==1){
					nw2=gameboard[h-i][j-i].getField();
				}else
					cw2=gameboard[h-i][j-i].getField();
			}
		}
	}
}
