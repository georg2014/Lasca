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
	
	//TODO BIG fen2array
	public LascaBoard(String state){
		//editor: Jonas Franz Schicke, Georg Stahn
		int x=0;
		int y=0;
		
		while(state.length()>0){
			//System.err.println("state.length(): "+state.length());
			String start = state.substring(0,1);
			//System.err.println("start = state.substring(0,1) "+state.substring(0,1));
//			if((x%2 == 1)&&y==0)
//				y++;
			y = helpConstructor(x, y);
			if((start.equals("b")||start.equals("w")||start.equals("B")||start.equals("W")) && x<7){
				gameboard[x][y].addStone(start);
				state = state.substring(1,state.length());
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
	public int helpConstructor(int x, int y){
		//editor: Georg Stahn
		if((x%2 == 1)&&y==0)
			y++;
		return y;
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
}
