package de.tuberlin.pes.swtpp.chess.control;

import java.util.HashMap;
import java.util.Map;

import de.tuberlin.pes.swtpp.chess.model.Board;
import de.tuberlin.pes.swtpp.chess.model.Game;
import de.tuberlin.pes.swtpp.chess.model.Player;

public class GameController {

	private static GameController instance = null;

	private static HashMap<Integer, Game> games;

	private GameController() {
		games = new HashMap<Integer, Game>();
	}

	public static synchronized GameController getInstance() {
		if (instance == null)
			instance = new GameController();

		return instance;
	}

	// finde ein game mit der id
	public Game findGameByID(Integer id) {
		return games.get(id);
	}

	// versuche ein spiel zu starten
	public synchronized Game startGame(Player p) {
		if (p == null)
			return null;

		Integer id = lookForGame(p);

		if (id == -1) {// kein vorhandenes spiel gefunden
			return createGame(p);
		} else {
			assignGame(p, id);

			return games.get(id);
		}
	}

	// versuche ein spiel zu starten
	public synchronized Game startBotGame(Player p, Player b) {
		if (p == null)
			return null;

		Game g = createGame(p);
		assignGame(b, g.getId());

		return g;
	}

	// suche ein vorhandenes spiel, wo ein spieler(schwarz) fehlt und gebe die id zurück
	private synchronized Integer lookForGame(Player p) {
		HashMap<Integer, Game> pGames = getPlayerGames(p);

		for (Map.Entry<Integer, Game> e1 : games.entrySet()) {
			Integer id1 = e1.getKey();
			Game game1 = e1.getValue();

			if (!game1.getStarted() && !game1.getFinished()/*!game1.getWhite().getId().equals(p.getId()) && game1.getBlack() == null*/) {// es ex. ein spiel, wo ein spieler fehlt
				if(pGames.size()==0){
					return id1;
				}
				
				boolean vorhanden=false;
				
				for (Map.Entry<Integer, Game> e2 : pGames.entrySet()) {
					Game game2 = e2.getValue();
					
					if(game2.getStarted() && !game2.getFinished() && (game2.getWhite().getId().equals(game1.getWhite().getId()) || game2.getBlack().getId().equals(game1.getWhite().getId()))){
						vorhanden=true;
						break;
					}
				}
				
				if(!vorhanden){
					return id1;
				}
			}
		}

		return -1;
	}

	// erstelle ein spiel mit dem spieler(weiss)
	private synchronized Game createGame(Player p) {
		if (p == null)
			return null;

		Integer i = totalGames() + 1;
		Game g = new Game();
		g.setId(i);
		g.setFinished(false);
		g.setWhite(p);

		Board b = new Board();
		g.setBoard(b);

		p.addGame(i);
		games.put(i, g);

		return g;
	}

	private synchronized void assignGame(Player p, Integer id) {
		if (p == null || id == null)
			return;

		Game g = games.get(id);
		g.setBlack(p);
		g.setStarted(true);

		p.addGame(id);
	}

	private HashMap<Integer, Game> getPlayerGames(Player p) {
		HashMap<Integer, Game> map = new HashMap<Integer, Game>();

		for (Integer g : p.getGames()) {
			map.put(g, games.get(g));
		}

		return map;
	}
	
	public int totalGames(){
		return games.size();
	}
	
	public void reset(){
		games = new HashMap<Integer, Game>();
	}
}
