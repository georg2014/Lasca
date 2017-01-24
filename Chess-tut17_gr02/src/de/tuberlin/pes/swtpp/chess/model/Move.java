package de.tuberlin.pes.swtpp.chess.model;

public class Move {
	
	private Player player;
	private Integer game;
	private String from;
	private String to;
	
	public Move(Player player, Integer game, String from, String to) {
		super();
		this.player = player;
		this.game = game;
		this.from = from;
		this.to = to;
	}

	public Player getPlayer() {
		return player;
	}

	public Integer getGame() {
		return game;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}
	
}
