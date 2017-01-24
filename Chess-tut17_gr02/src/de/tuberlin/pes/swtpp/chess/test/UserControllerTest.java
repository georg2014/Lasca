package de.tuberlin.pes.swtpp.chess.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.tuberlin.pes.swtpp.chess.control.UserController;

public class UserControllerTest {

	UserController controller;
	
	@Before
	public void setUp() throws Exception {
		controller = UserController.getInstance();
		
		controller.createUser("Test1", "TestUser1", "Passw0rd");
	}

	@Test
	public void testCreateUser() {
		assertTrue(controller.createUser("Test1", "Jim", "1234") == null); // create should fail
		assertTrue(controller.createUser("", "Jack", "1234") == null); // create should fail
		assertTrue(controller.createUser("Test3", "", "1234") == null); // create should fail
		assertTrue(controller.createUser("Test3", "Jack", "") == null); // create should fail
		assertTrue(controller.createUser(null, "Jack", "1234") == null); // create should fail
		assertTrue(controller.createUser("Test3", null, "1234") == null); // create should fail
		assertTrue(controller.createUser("Test3", "Jack", null) == null); // create should fail
		assertTrue(controller.createUser("Test2", "Johnny", "1234").getId().equals("Test2")); // create should succeed
	}
	
	@Test
	public void testUserController() {
		assertTrue(controller != null); // successfully created by setup
	}

	@Test
	public void testCheckUserExists() {
		assertTrue(controller.checkUserExists("Test1"));
		assertFalse(controller.checkUserExists("JonDoe"));
	}

	@Test
	public void testCheckUserPwd() {
		assertFalse(controller.checkUserPwd("Test15", "abc,1234"));
		assertFalse(controller.checkUserPwd("Test1", "wrongpassword"));
		assertTrue(controller.checkUserPwd("Test1", "Passw0rd"));
	}

	@Test
	public void testFindUserByID() {
		assertTrue(controller.findUserByID("Test1").getId().equals("Test1"));
		assertTrue(controller.findUserByID("JonDoe") == null);
	}



}
