package de.tuberlin.pes.swtpp.chess.model;

/**
 * This class represents a human user. Every user is also registered as a player.
 * 
 * A user object stores name and password of a user. It also inherits the ID attribute of the player. 
 * 
 *
 */
public class User extends Player{
private String pwd = "";
	
	public User(String name, String id, String pwd) {
		this.name = name;
		this.id = id;
		this.pwd = pwd;
	}
	
	public String getName() {
		return name;
	}

	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}
