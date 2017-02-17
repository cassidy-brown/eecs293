package chemistry;

import org.junit.Test;

import static org.junit.Assert.*;

public class FormulaCheckerTest {
	
	/* There are 16 basic combos:
	 * 	cap-cap	 cap-low  cap-num  cap-par
	 *  low-cap	 low-low  low-num  low-par
	 *  num-cap	 num-low  num-num  num-par
	 *  par-cap  par-low  par-num  par-par
	 *  
	 *  low-low has multiple cases based on the number of lower case characters in a row (indicated with numbers)
	 *  par-* has different cases for open (indicated opar) and closed (indicated cpar) parentheses
	 *  
	 *  The first time each combo is tested, it is commented as such
	 */
	
	
	@Test
	public void testNoParenthesesFormulas() {
		FormulaChecker checker = new FormulaChecker();
		String[] formulas = {				
				"H2O", 							//Begin with cap, cap-num (H2), num-cap (2O)
				"HCL", 							//cap-cap (HC)
				"Hea7Ji3IJKl", 					//cap-low (He), low1-low2 (ea), low-num (a7)
				"O999999999999999", 			//cap-num (O9), num-num (99)
				"AgBrO3", 						//low-cap (gB)
				"AgCl3Cu2", 					
				"AgMnO4", 						
				"CH3CH2CH2CH2OH", 				//Lots of molecules (yay polymers)
				"ABCDEFGHIJKLMNOPQRSTUVWXYZ", 	//Lots of letters
				"H"};							//Minimalist test
		
		for(String s: formulas) {
			assertTrue("Checking " + s, checker.checkFormula(s));
		}
	}
	
	@Test
	public void testParenthesesFormulas() {
		FormulaChecker checker = new FormulaChecker();
		String[] formulas = {
				"H(CL)2", 								//cap-opar ("H("), opar-cap ("(C"), cap-cpar ("L)"), cpar-num (")2")
				"(Hea7Ji)3IJKl3", 						//Begin with paren, low-cpar ("i)")
				"(OjH)37", 								//note: number containing 0
				"(JfkC4)909(H2Ao)70", 					//num-cpar ("4)"), num-opar ("9(")		
				"(Hel2B3)3(Six6Fi4)22(Eye32Ps4)7", 		
				"Al(NO3)3",								
				"Au2(SeO4)3", 							
				"Ba(BrO3)2H20", 						
				"CH3COO(CH2)2CH(CH3)2", 				
				"(CH3)3COOC(CH3)3",
				"((ABc4)5F)17",							//opar-opar ("((")		note: number beginning with 1
				"(Qu3(Ki(RP2)2)4)2"};					//nominal nesting			
		
		for(String s: formulas) {
			assertTrue("Checking " + s, checker.checkFormula(s));
		}
	}
	
	@Test
	public void testNestedParentheses(){
		FormulaChecker checker = new FormulaChecker();
		assertTrue("Nested parentheses don't work yet", checker.checkFormula("(Qu3(KiP2)4)2"));
	}

	@Test
	public void testIncorrectFormulas() {
		FormulaChecker checker = new FormulaChecker();
		String[] formulas = {
				"H2 O", 	//space not allowed
				"eBBe3", 	//begins with lower case letter
				"x", 		
				"5", 		//begins with number
				"Wrong2", 	//too many lower case in a row low1-low2 ("ro"), low2-low3 ("on")
				"0"};		
		
		for(String s: formulas) {
			assertFalse("Checking " + s, checker.checkFormula(s));
		}
	}
	
	@Test
	public void testInvalidParentheses() {
		FormulaChecker checker = new FormulaChecker();
		String[] formulas = {
				"()", 				//Empty parentheses
				"H2O)3", 			//Missing open parenthesis
				"H(Io3", 			//Missing end parenthesis
				"(H20(I)4Nz2)2", 	//Only single element in parentheses
				"(Ab)",				//Still just single element in parentheses
				"H(Io4)3",			//only one net item inside parentheses
				"(H20)", 			//Not followed by number
				"(H2O)B4", 			//Not followed by number, cpar-cap
				"(H2O)b4",			//Not followed by number, cpar-low
				"H((H2O)7)6", 		//Unnecessary nesting -- only one net item inside outer parentheses
				"(H2(OH)3"};		//Nests two parentheses, doesn't close outer
		
		for(String s: formulas) {
			assertFalse("Checking " + s, checker.checkFormula(s));
		}
	}
	
	@Test
	public void testIncorrectMultiplier() {
		FormulaChecker checker = new FormulaChecker();
		String[] formulas = {"B0", "Ze098", "X9Je011", "A1"};		//Numbers begin with 0 or are 1
		
		for(String s: formulas) {
			assertFalse("Checking " + s, checker.checkFormula(s));
		}
		
		/* Unfortunately, we can't test the overflow check, 
		 * but given that I just ran out of heap space trying to, 
		 * I don't think it'll be much of a problem */
	}
	
	@Test
	public void testInvalidCharacters() {
		FormulaChecker checker = new FormulaChecker();
		String[] formulas = {"?3", "H20!", "K#9"};		//non-alphanumeric and non-parenthetical characters
		
		for(String s: formulas) {
			assertFalse("Checking " + s, checker.checkFormula(s));
		}
	}

}