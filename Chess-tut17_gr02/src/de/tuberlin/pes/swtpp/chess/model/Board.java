package de.tuberlin.pes.swtpp.chess.model;

import de.tuberlin.pes.swtpp.lib.ChessLib;

public class Board {
	private Figure[][] gameboard;

	public Board() {
		this.gameboard = new Figure[8][8];
		this.startBoard();
	}

	public Board(Figure[][] gameboard) {
		super();
		this.gameboard = gameboard;
	}

	public Figure[][] getGameboard() {
		return gameboard;
	}

	public void setGameboard(Figure[][] gameboard) {
		this.gameboard = gameboard;
	}
	
	//setzt die figuren für die ausgangsposition
	public void startBoard(){
		String start="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
		String[] rows=start.split("/");
		int r=0;
		int c=0;
		
		for(int i=0;i<rows.length;i++){
			char[] chars=rows[i].toCharArray();
			for(int j=0;j<chars.length;j++){
				if(Character.isDigit(chars[j])){
					c+=Character.getNumericValue(chars[j]);
				}else{
					addFigure(r,c,chars[j]);
					c++;
				}
			}
			r++;
			c=0;
		}
	}

	//fügt eine figur im brett hinzu
	public void addFigure(int x, int y, char f) {
		String figuren = "rnbqkpRNBQKP";
		String figur = String.valueOf(f);
		if (x >= 0 && x < 8 && y >= 0 && y < 8 && figuren.contains(figur)) {
			Figure fig = null;

			switch (figur.toLowerCase()) {
				case "r":
					fig = new Rook();
					break;
				case "n":
					fig = new Knight();
					break;
				case "b":
					fig = new Bishop();
					break;
				case "q":
					fig = new Queen();
					break;
				case "k":
					fig = new King();
					break;
				case "p":
					fig = new Pawn();
					break;
			}

			if (!Character.isUpperCase(figur.charAt(0))) {
				fig.setWhite(false);
			} else {
				fig.setWhite(true);
			}

			gameboard[x][y] = fig;
		} else {
			System.out.println("Figur kann nicht hinzugefügt werden.");
		}
	}
	
	//fügt eine figur hinzu
	public void addFigure(int x, int y, Figure f){
		gameboard[x][y] = f;
	}
	
	//löscht eine figur
	public void deleteFigure(int x, int y){
		gameboard[x][y] = null;
	}
	
	//bewegt eine figur
	public boolean moveFigure(boolean white, String from, String to){
		int x1=Integer.parseInt(from.charAt(1) + "") - 1;
		int y1=from.charAt(0) - 96 - 1;
		int x2=Integer.parseInt(to.charAt(1) + "") - 1;
		int y2=to.charAt(0) - 96 - 1;
		Figure f=gameboard[7-x1][y1];
		
		if(f!=null && white==f.getWhite()){//spieler darf nur seine figuren bewegen
			if((x1!=x2 || y1!=y2) && validMove(7-x1,y1,7-x2,y2,f)){//zug ausführbar
				deleteFigure(7-x1,y1);
				addFigure(7-x2,y2,f);
				
				if(ChessLib.checkCheck(getFEN(), f.getWhite())){//mache zug rückgängig bei schach
					System.out.println("Schach");
					deleteFigure(7-x2,y2);
					addFigure(7-x1,y1,f);
					
					return false;
				}
				
				if(f instanceof Pawn){//wandle bauer in dame um
					if(white==true && 7-x2==0){
						addFigure(7-x2,y2,'Q');
					} else if(white==false && 7-x2==7){
						addFigure(7-x2,y2,'q');
					}
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	//prüft ob zug durchführbar ist
	private boolean validMove(int x1, int y1, int x2, int y2, Figure f){
		if(f instanceof Bishop){
			return validMoveB(x1,y1,x2,y2,f);
		} else if(f instanceof King){
			return validMoveK(x1,y1,x2,y2,f);
		} else if(f instanceof Knight){
			return validMoveN(x1,y1,x2,y2,f);
		} else if(f instanceof Pawn){
			return validMoveP(x1,y1,x2,y2,f);
		} else if(f instanceof Queen){
			return validMoveQ(x1,y1,x2,y2,f);
		} else if(f instanceof Rook){
			return validMoveR(x1,y1,x2,y2,f);
		}
		
		return false;
	}
	
	//prüfe bewegung von läufer
	private boolean validMoveB(int x1, int y1, int x2, int y2, Figure f){
		boolean position=false;
		boolean jump=false;
		
		int x=x2-x1;
		int y=y2-y1;
		int z=Math.abs(x*y);
		
		if(Math.abs(x)==Math.abs(y)){//diagonal aus der position
			System.out.println("Läufer Pos:true");
			position=true;
		}
		
		if(gameboard[x2][y2]!=null && f.getWhite()==gameboard[x2][y2].getWhite()){//im zielposition darf kein eigene figur stehen
			System.out.println("Läufer Pos:false");
			position=false;
		}
		
		for(int r=0;r<8;r++){
			for(int c=0;c<8;c++){
				int u=r-x1;
				int v=c-y1;
				int w=Math.abs(u*v);
				
				if(r!=x1 && c!=y1 && Math.abs(u)==Math.abs(v) && equalSign(x,u) && equalSign(y,v) && w<z){
					if(gameboard[r][c]!=null){//auf dem weg darf keine figur übersprungen werden
						System.out.println("Läufer Jump:true");
						jump=true;
					}
				}
			}
		}
		
		return position && !jump;
	}
	
	//prüfe bewegung von könig
	private boolean validMoveK(int x1, int y1, int x2, int y2, Figure f){
		boolean position=false;
		boolean beat=false;
		
		int x=x2-x1;
		int y=y2-y1;
		
		if(x>=-1 && x<=1 && y>=-1 && y<=1){//nur benachbarte felder
			System.out.println("König Pos:true");
			position=true;
		}
		
		if(gameboard[x2][y2]!=null && f.getWhite()==gameboard[x2][y2].getWhite()){//im zielposition darf kein eigene figur stehen
			System.out.println("König Pos:false");
			position=false;
		}
		
		beat=false;//wird in moveFigure betrachtet
		
		return position && !beat;
	}
	
	//prüfe bewegung von pferd
	private boolean validMoveN(int x1, int y1, int x2, int y2, Figure f){
		boolean position=false;
		
		int x=Math.abs(x2-x1);
		int y=Math.abs(y2-y1);
		
		if((x==2 && y==1)||(x==1 && y==2)){//entweder 2 horizontal 1 vertikal oder 1 horizontal und 2 vertikal
			System.out.println("Pferd Pos:true");
			position=true;
		}
		
		if(gameboard[x2][y2]!=null && f.getWhite()==gameboard[x2][y2].getWhite()){//im zielposition darf kein eigene figur stehen
			System.out.println("Pferd Pos:false");
			position=false;
		}
		
		return position;
	}
	
	//prüfe bewegung von bauer
	private boolean validMoveP(int x1, int y1, int x2, int y2, Figure f){
		boolean position=false;
		boolean beat=false;
		
		int x=Math.abs(x2-x1);
		int y=Math.abs(y2-y1);
		
		if((f.getWhite() && x2<x1) || (!f.getWhite() && x1<x2)){
			if(x==1 && y==0){//nur 1 schritt vorwärts
				System.out.println("Bauer Pos:true");
				position=true;
			} else if(x==2 && y==0 && ((x1==6 && f.getWhite())||(x1==1 && !f.getWhite()))){//oder 2 schritte aus ausgangsposition
				System.out.println("Bauer Pos:true");
				position=true;
			}
			
			if(gameboard[x2][y2]!=null){//im zielposition darf keine figur stehen
				System.out.println("Bauer Pos:false");
				position=false;
			}
			
			if(x==1 && y==1){//oder schräg schlagen
				if(gameboard[x2][y2]!=null && gameboard[x2][y2].getWhite()!=f.getWhite()){//nur gegnersiche figur schlagen
					System.out.println("Bauer Beat:true");
					beat=true;
				}
			}
		}
		
		return position || beat;
	}
	
	//prüfe bewegung von dame
	private boolean validMoveQ(int x1, int y1, int x2, int y2, Figure f){
		return validMoveR(x1,y1,x2,y2,f) || validMoveB(x1,y1,x2,y2,f);//dame ist kombination aus läufer und turm	
	}
	
	//prüfe bewegung von turm
	private boolean validMoveR(int x1, int y1, int x2, int y2, Figure f){
		boolean position=false;
		boolean jump=false;
		
		if(x1==x2 || y1==y2){//eine koordinate muss konstant bleiben
			System.out.println("Turm Pos:true");
			position=true;
		}
		
		if(gameboard[x2][y2]!=null && f.getWhite()==gameboard[x2][y2].getWhite()){//im zielposition darf kein eigene figur stehen
			System.out.println("Turm Pos:false");
			position=false;
		}
		
		for(int r=0;r<8;r++){
			for(int c=0;c<8;c++){
				int u=r-x1;
				int v=c-y1;
				int p=x2-x1;
				int q=y2-y1;
				
				if((c!=y1 && u==0 && equalSign(v,q) && Math.abs(v)<Math.abs(q)) || (Math.abs(u)<Math.abs(p) && equalSign(u,p) && v==0 && r!=x1)){
					if(gameboard[r][c]!=null){//auf dem weg darf keine figur übersprungen werden
						System.out.println("Turm Jump:true");
						jump=true;
					}
				}
			}
		}
		
		return position && !jump;
	}
	
	//gibt den board als fen format zurück
	public String getFEN(){
		String[] fen=new String[8];
		int c=0;
		
		for(int i=0;i<gameboard.length;i++){
			fen[i]="";
			for(int j=0;j<gameboard[i].length;j++){
				if(gameboard[i][j]==null){
					c++;
				} else{
					if(c>0){
						fen[i]+=c+"";
					}
					c=0;
					String letter="";
					Figure f=gameboard[i][j];
					
					if(f instanceof Bishop){
						letter="b";
					} else if(f instanceof King){
						letter="k";
					} else if(f instanceof Knight){
						letter="n";
					} else if(f instanceof Pawn){
						letter="p";
					} else if(f instanceof Queen){
						letter="q";
					} else if(f instanceof Rook){
						letter="r";
					}
					
					if(f.getWhite()){
						letter=letter.toUpperCase();
					}
					
					fen[i]+=letter;
				}
			}
			if(c>0){
				fen[i]+=c+"";
			}
			c=0;
		}
		
		String str=fen[0];
		
		for(int i=1;i<8;i++){
			str+="/"+fen[i];
		}
		
		return str;
	}
	
	private boolean equalSign(int a, int b){
		if((a>=0 && b>=0) || (a<0 && b<0)){
			return true;
		}
		
		return false;
	}

}
