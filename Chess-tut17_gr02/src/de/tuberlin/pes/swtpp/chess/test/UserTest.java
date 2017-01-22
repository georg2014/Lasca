package de.tuberlin.pes.swtpp.chess.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.tuberlin.pes.swtpp.chess.control.GameController;
import de.tuberlin.pes.swtpp.chess.control.UserController;
import de.tuberlin.pes.swtpp.chess.model.User;

public class UserTest {

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testUser() {
		User u = new User ("Test1", "ID1", "Pwd1");
		
		assertTrue(u.getName().equals("Test1"));
		assertTrue(u.getId().equals("ID1"));
		assertTrue(u.getPwd().equals("Pwd1"));
		
		u.setPwd("NewPwd");
		assertTrue(u.getPwd().equals("NewPwd"));
	}
}
