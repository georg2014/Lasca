package de.tuberlin.pes.swtpp.chess.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ UserControllerTest.class, UserTest.class, ChessServletTest.class })
public class AllTests {
	UserTest ut = new UserTest();
	UserControllerTest uct = new UserControllerTest();
	ChessServletTest cst = new ChessServletTest();
}