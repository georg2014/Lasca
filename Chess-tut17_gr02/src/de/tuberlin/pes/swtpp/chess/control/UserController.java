package de.tuberlin.pes.swtpp.chess.control;

import java.util.HashMap;

import de.tuberlin.pes.swtpp.chess.model.User;

/**
 * This class implements all general user-related use cases.
 */
public class UserController {

	private static UserController instance = null; 
	
	private HashMap<String, User> users;
	
	/**
	 * This controller needs access the global list of users. The list must not be null. 
	 * @param users
	 */
	private UserController() {
		this.users = new HashMap<String,User>();
	}
	
	public static synchronized UserController getInstance() {
		if (instance == null) 
			instance = new UserController();
		
		return instance;
	}
	
	/**
	 * Returns true if a user with the given ID exists in the list of users.
	 * @param id
	 * @return
	 */
	public boolean checkUserExists(String id) {
		
		return users.containsKey(id);
	}
	
	/**
	 * Checks user credentials.
	 * 
	 * @param id
	 * @param pwd
	 * @return true if User/Pwd exist
	 */
	public boolean checkUserPwd(String id, String pwd) {
		if (checkUserExists(id)) {
			User u = users.get(id);
			return u.getPwd().equals(pwd); 
		}
		
		return false;
	}
	
	/**
	 * Returns user object with given ID or null if it does not exist.
	 * 
	 * @param id
	 * @return
	 */
	public User findUserByID(String id) {
		if (checkUserExists(id)) {
			return users.get(id);
		}
		
		return null;
	}
	
	/**
	 * Use Case create user. All data supplied must not be empty. The id is a unique key. The user can not be created if the ID exists already. The created user is returned. 
	 * 
	 * @param id
	 * @param name
	 * @param pwd
	 * @param contactInfo
	 * @return
	 */
	public synchronized User createUser(String id, String name, String pwd) {
		
		if (id == null || name == null || pwd == null || id == "" || name == "" || pwd == "") return null;
		
		if (checkUserExists(id)) return null;
			
		User u = new User(name, id, pwd);
		
		users.put(id, u);
		
		return u;
	}
	
	public void reset(){
		this.users = new HashMap<String,User>();
	}
	
}