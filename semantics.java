import java.io.*;
import java.util.*;
import java.util.regex.*;

public class semantics {

	public static void main(String args[]) throws IOException {

		try {
			// read file
			String filename = args[0];
			File file = new File(filename);
			Scanner scanFile = new Scanner(file);
			Scanner scanFileNext = new Scanner(file);

			// lex analizer first
			linkedQ Qlink = new linkedQ();
			lexMe(scanFile, scanFileNext, Qlink);
			// Qlink.print();
			p2 the = new p2(Qlink);
		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	static void lexMe(Scanner scanFile, Scanner scanFileNext, linkedQ Qlink) {

		boolean commentMode = false;
		StringBuilder wordBank = new StringBuilder();

		scanFile.useDelimiter("");
		scanFileNext.useDelimiter("");

		String character = scanFile.next();
		String characterNext = scanFileNext.next();
		characterNext = scanFileNext.next();

		while (scanFile.hasNext()) {

			// skip \r and \n
			while (character.equals("\r") || character.equals("\n") || character.equals("\t")) {
				if (!scanFile.hasNext())
					break;
				if (scanFile.hasNext()) {
					character = scanFile.next();
				}
				if (scanFileNext.hasNext()) {
					characterNext = scanFileNext.next();
				}
			}

			// if /* */comment
			if (character.equals("/") && characterNext.equals("*")) {
				commentMode = true;

				character = scanFile.next();
				if (scanFileNext.hasNext()) {
					characterNext = scanFileNext.next();
				}
				while (commentMode == true) {

					if (character.equals("*")) {
						character = scanFile.next();
						if (scanFileNext.hasNext()) {
							characterNext = scanFileNext.next();
						}
						if (character.equals("/")) {
							commentMode = false;
						}
					} else {
						if (scanFile.hasNext()) {
							character = scanFile.next();
						}
						if (scanFileNext.hasNext()) {
							characterNext = scanFileNext.next();
						}
					}
				}
			}

			// if // comment
			else if (character.equals("/") && characterNext.equals("/")) {

				commentMode = true;

				character = scanFile.next();
				characterNext = scanFileNext.next();
				while (commentMode == true) {

					if (character.equals("\n") || character.equals("\r")) {
						commentMode = false;
					}

					else {
						if (scanFile.hasNext()) {
							character = scanFile.next();
						}
						if (scanFileNext.hasNext()) {
							characterNext = scanFileNext.next();
						}
					}
				}

			}

			// else, scan line
			else {

				// if white space, check to see if word bank is token
				if ((character.equals(" ") && wordBank.length() != 0)) {
					makeToken(wordBank, Qlink);
				}

				// do nothing if you see a space lmao
				else if (character.equals(" ") || character.equals("\n") || character.equals("\r")
						|| character.equals("\t")) {
				}

				// if character is operator
				// operations print word bank from above algorithm

				else if (character.equals(">") && characterNext.equals("=")) {
					if (wordBank.length() != 0) {
						makeToken(wordBank, Qlink);
					}
					Qlink.insert("compare", ">=");
					if (scanFile.hasNext()) {
						character = scanFile.next();
					}
					if (scanFileNext.hasNext()) {
						characterNext = scanFileNext.next();
					}
				} else if (character.equals("=") && characterNext.equals("=")) {
					if (wordBank.length() != 0) {
						makeToken(wordBank, Qlink);
					}
					Qlink.insert("compare", "==");
					if (scanFile.hasNext()) {
						character = scanFile.next();
					}
					if (scanFileNext.hasNext()) {
						characterNext = scanFileNext.next();
					}
				} else if (character.equals("!") && characterNext.equals("=")) {
					if (wordBank.length() != 0) {
						makeToken(wordBank, Qlink);
					}
					Qlink.insert("compare", "!=");
					if (scanFile.hasNext()) {
						character = scanFile.next();
					}
					if (scanFileNext.hasNext()) {
						characterNext = scanFileNext.next();
					}
				} else if (character.equals("<") && characterNext.equals("=")) {
					if (wordBank.length() != 0) {
						makeToken(wordBank, Qlink);
					}
					Qlink.insert("compare", "<=");
					if (scanFile.hasNext()) {
						character = scanFile.next();
					}
					if (scanFileNext.hasNext()) {
						characterNext = scanFileNext.next();
					}
				}

				else if (character.equals("+")) {
					if (wordBank.length() != 0) {
						makeToken(wordBank, Qlink);
					}
					Qlink.insert("+", "+");
				} else if (character.equals("-")) {
					if (wordBank.length() != 0) {
						makeToken(wordBank, Qlink);
					}
					Qlink.insert("-", "-");
				} else if (character.equals("*")) {
					if (wordBank.length() != 0) {
						makeToken(wordBank, Qlink);
					}
					Qlink.insert("*", "*");
				} else if (character.equals("/")) {
					if (wordBank.length() != 0) {
						makeToken(wordBank, Qlink);
					}
					Qlink.insert("/", "/");
				} else if (character.equals("<")) {
					if (wordBank.length() != 0) {
						makeToken(wordBank, Qlink);
					}
					Qlink.insert("compare", "<");
				} else if (character.equals(">")) {
					if (wordBank.length() != 0) {
						makeToken(wordBank, Qlink);
					}
					Qlink.insert("compare", ">");
				} else if (character.equals("=")) {
					if (wordBank.length() != 0) {
						makeToken(wordBank, Qlink);
					}
					Qlink.insert("=", "=");
				} else if (character.equals(";")) {
					if (wordBank.length() != 0) {
						makeToken(wordBank, Qlink);
					}
					Qlink.insert(";", ";");
				} else if (character.equals(",")) {
					if (wordBank.length() != 0) {
						makeToken(wordBank, Qlink);
					}
					Qlink.insert(",", ",");
				} else if (character.equals("[")) {
					if (wordBank.length() != 0) {
						makeToken(wordBank, Qlink);
					}
					Qlink.insert("[", "[");
				} else if (character.equals("]")) {
					if (wordBank.length() != 0) {
						makeToken(wordBank, Qlink);
					}
					Qlink.insert("]", "]");
				} else if (character.equals("{")) {
					if (wordBank.length() != 0) {
						makeToken(wordBank, Qlink);
					}
					Qlink.insert("{", "{");

					if (characterNext.equals("}") && !scanFileNext.hasNext()) {
						Qlink.insert("}", "}");
					}

				} else if (character.equals("}")) {

					if (wordBank.length() != 0) {
						makeToken(wordBank, Qlink);
					}

					if (character.equals("}") && characterNext.equals("}")) {
					}

					else
						Qlink.insert("}", "}");

				} else if (character.equals("(")) {
					if (wordBank.length() != 0) {
						makeToken(wordBank, Qlink);
					}
					Qlink.insert("(", "(");
				} else if (character.equals(")")) {
					if (wordBank.length() != 0) {
						makeToken(wordBank, Qlink);
					}
					Qlink.insert(")", ")");
				}

				// else append to word bank
				else {
					wordBank.append(character);
					if (scanFile.hasNext() == false) {
						makeToken(wordBank, Qlink);
					}
				}
			}

			if (scanFile.hasNext()) {
				character = scanFile.next();
			}
			if (scanFileNext.hasNext()) {
				characterNext = scanFileNext.next();
			}

			if (!scanFile.hasNext()) {
				
				if(character.equals(" ") || character.equals("\n") || character.equals("\r")
						|| character.equals("\t")) {}
				
				else {
					Qlink.insert(character, character);
				}
				
			}

		}

		if (wordBank.length() != 0) {
			wordBank.append(character);
			makeToken(wordBank, Qlink);
		}

		Qlink.insert("$", "$");
		Qlink.reset();

	}

	static void makeToken(StringBuilder wordBank, linkedQ Qlink) {
		String token;
		String wordBankString = wordBank.toString();
		token = findToken(wordBankString);
		Qlink.insert(token, wordBankString);
		// reset wordBank
		wordBank.setLength(0);

	}

	static String findToken(String find) {

		// keyword
		if (find.equals("else")) {
			return "else";
		} else if (find.equals("if")) {
			return "if";
		} else if (find.equals("int")) {
			return "int";
		} else if (find.equals("return")) {
			return "return";
		} else if (find.equals("void")) {
			return "void";
		} else if (find.equals("while")) {
			return "while";
		}

		// letter
		else if (Pattern.matches("[a-zA-Z]", find)) {
			return "ID";
		}

		// ID
		else if (Pattern.matches("[a-zA-Z]+[a-zA-Z]*", find)) {
			return "ID";
		}

		// digit
		else if (Pattern.matches("\\d", find)) {
			return "Number";
		}

		// num
		else if (Pattern.matches("\\d+\\d*", find)) {
			return "Number";
		}

		// return error
		else
			return "Error";
	}

}

class data {
	public String token;
	public String lex;
	public data next;
	public data prev;

	public data(String token, String lex) {
		this.token = token;
		this.lex = lex;
	}
}

class linkList {

	private data first;
	private data last;
	private data current = first;
	private data lookAhead;

	public linkList() {
		first = null;
		last = null;
	}

	public void insertLast(String token, String lex) {
		data newData = new data(token, lex);
		if (isEmpty()) {
			first = newData;
		} else
			last.next = newData;
		last = newData;
	}

	public boolean isEmpty() {
		return first == null;
	}

	public data getToken() {
		return current;
	}

	public void moveNext() {
		current = current.next;
	}

	public data dataAhead() {
		lookAhead = current.next;

		return lookAhead;
	}

	public void reset() {
		current = first;
	}

}

class linkedQ {
	private linkList Qlink;

	public linkedQ() {
		Qlink = new linkList();
	}

	public boolean isEmpty() {
		return Qlink.isEmpty();
	}

	public void insert(String token, String lex) {
		Qlink.insertLast(token, lex);
	}

	public data getCurrentToken() {
		return Qlink.getToken();
	}

	public void moveNext() {
		Qlink.moveNext();
	}

	public data getNextToken() {
		return Qlink.dataAhead();
	}

	public void reset() {
		Qlink.reset();
	}

	public void print() {
		while (Qlink != null) {
			System.out.println(Qlink.getToken().lex);
			Qlink.moveNext();
		}
	}
}

class p2 {
	private linkedQ Qlink;
	private HLink hashtl;
	private static int numArg = 0;
	private boolean hasMain = false;

	p2(linkedQ Qlink) {
		this.Qlink = Qlink;
		HLink hashtl = new HLink();
		this.hashtl = hashtl;
		hashtl.insertNewTable();
		program();
	}

	void error() {
		System.out.println("REGECT");
		System.exit(0);
	}

	String lookAhead() {
		return Qlink.getNextToken().token;
	}

	void accept(String token) {

		String theToken = currentToken();

		if (theToken.equals(token)) {
			Qlink.moveNext();
		} else
			error();
	}

	String currentToken() {
		return Qlink.getCurrentToken().token;
	}

	String currentLex() {
		return Qlink.getCurrentToken().lex;
	}

	void program() {
		decList();
		String theToken = currentToken();

		// if have at least 1 main function
		try {
			String type = hashtl.search("main").getType();
			if(type.equals("int")) {
				error();
			}
		} catch (Exception e) {
			error();
		}

		if (theToken.equals("$")) {
			System.out.println("ACCEPT");
			System.exit(0);
		}
		else {
			System.out.println("REJECT");
			System.exit(0);
		}
	}

	void decList() {
		declaration();
		decListP();
	}

	void decListP() {
		// if not empty
		String theToken = currentToken();
		if (theToken.equals("$"))
			return;

		else if (theToken.equals("ID")) {
			error();
		}

		else {
			declaration();
			decListP();
		}

	}

	void declaration() {

		// use lookAhead2

		// if int ID; or int ID [Number];
		String theToken = currentToken();

		if (theToken.equals("int") || theToken.equals("void")) {
			
			if(hasMain) {
				error();
			}

			// put ID into hash table

			String typeType = typeSpeci();

			String keepID = currentLex();

			hashtl.insertAtCurrentTable(typeType, keepID, "", 0);

			accept("ID");
			theToken = currentToken();

			if (theToken.equals(";") || theToken.equals("[")) {

				if (typeType.equals("void")) {
					error();
				}

				varDec(typeType, keepID);
			}

			// if int ID ( param ) compoundStatment
			else if (theToken.equals("(")) {
				hashtl.changeToFNT(keepID);
				String returnType = funDec(keepID);

				if (typeType.contentEquals("void") && returnType.equals("noReturn")) {
					if(keepID.contentEquals("main")) {
						hasMain=true;
					}
					return;
				} else if (typeType.equals("void") && returnType.equals("none")) {
					if(keepID.contentEquals("main")) {
						hasMain=true;
					}
					return;
				} else if (returnType.equals("array") && hashtl.search(keepID).isArray()) {
					if(keepID.contentEquals("main")) {
						hasMain=true;
					}
					return;
				} else if (typeType.contentEquals("int") && keepID.equals("main")) {
					if(keepID.contentEquals("main")) {
						hasMain=true;
					}
					return;
				} else if (!typeType.equals(returnType)) {
					error();
				} else {
					if(keepID.contentEquals("main")) {
						hasMain=true;
					}
					return;
				}
			}
		}

		else
			error();
	}

	void varDec(String typeType, String keepID) {
		String theToken = currentToken();

		if (theToken.equals("int") || theToken.equals("void")) {
			param();

			theToken = currentToken();

			if (theToken.equals("ID")) {
				while (theToken.equals("ID")) {
					accept("ID");
					theToken = currentToken();
				}
			}

		}

		else if (theToken.equals("[")) {
			accept("[");
			String val = currentLex();
			accept("Number");
			accept("]");
			hashtl.changeToArrayT(keepID);
		}

		accept(";");

	}

	void varDec() {
		String theToken = currentToken();

		if (theToken.equals("int") || theToken.equals("void")) {
			param();

			theToken = currentToken();

			if (theToken.equals("ID")) {
				while (theToken.equals("ID")) {
					accept("ID");
					theToken = currentToken();
				}
			}

		}

		else if (theToken.equals("[")) {
			accept("[");
			accept("Number");
			accept("]");
		}

		accept(";");

	}

	String typeSpeci() {
		String theToken = currentToken();

		if (theToken.equals("int")) {
			accept("int");
			return "int";
		}

		// or

		else {
			accept("void");
			return "void";
		}
	}

	String funDec() {
		accept("(");
		params();
		accept(")");
		return compoundStmnt();
	}

	String funDec(String ID) {
		accept("(");
		params(ID);
		accept(")");
		return compoundStmnt();
	}

	void params() {

		String theToken = currentToken();
		if (theToken.equals("void")) {
			accept("void");
			theToken = currentToken();
			if (theToken.equals("ID")) {
				accept("ID");
			}
		}

		theToken = currentToken();
		if (theToken.equals(",")) {
			paramListP();
		}

		theToken = currentToken();
		if (theToken.equals(")")) {
			return;
		}

		paramList();
	}

	void params(String ID) {

		String theToken = currentToken();
		if (theToken.equals("void")) {
			accept("void");
			theToken = currentToken();
			if (theToken.equals("ID")) {
				accept("ID");
				hashtl.search(ID).addParam();
			}
		}

		theToken = currentToken();
		if (theToken.equals(",")) {
			paramListP(ID);
		}

		theToken = currentToken();
		if (theToken.equals(")")) {
			return;
		}

		paramList(ID);
	}

	void paramList(String ID) {
		param(ID);
		paramListP(ID);

	}

	void paramList() {
		param();
		paramListP();

	}

	void paramListP() {

		// if not empty
		String theToken = currentToken();
		if (theToken.equals(",")) {
			accept(",");
			param();
			paramListP();
		}

		else
			return;
	}

	void paramListP(String ID) {

		// if not empty
		String theToken = currentToken();
		if (theToken.equals(",")) {
			accept(",");
			param(ID);
			paramListP(ID);
		}

		else
			return;
	}

	void param() {
		String type = typeSpeci();
		String keepID = currentLex();
		accept("ID");
		String theToken = currentToken();
		if (theToken.equals("[")) {
			accept("[");

			theToken = currentToken();
			String val = currentLex();
			if (theToken.equals("Number")) {
				accept("Number");
			}

			accept("]");

			// input into hash table
			hashtl.insertAtCurrentTable(type, keepID, val, 0);
			hashtl.changeToArrayT(keepID);
		}

		else {
			hashtl.insertAtCurrentTable(type, keepID, "e", 0);
		}

	}

	void param(String ID) {
		String type = typeSpeci();
		String keepID = currentLex();
		accept("ID");
		hashtl.search(ID).addParam();
		String theToken = currentToken();
		if (theToken.equals("[")) {
			accept("[");

			theToken = currentToken();
			String val = currentLex();
			if (theToken.equals("Number")) {
				accept("Number");
			}

			accept("]");

			// input into hash table
			hashtl.insertAtCurrentTable(type, keepID, val, 0);
			hashtl.changeToArrayT(keepID);
		}

		else {
			hashtl.insertAtCurrentTable(type, keepID, "e", 0);
		}

	}

	String compoundStmnt() {
		accept("{");
		hashtl.insertNewTable();
		localDec();
		String returnType = stmntList();
		accept("}");
		hashtl.moveBack();

		return returnType;
	}

	void localDec() {

		// if not empty
		String theToken = currentToken();
		if (theToken.equals("void") || theToken.equals("int")) {

			varDec();
			localDec();

		}

		else
			return;

	}

	String stmntList() {

		String leftSide = "";

		// if not empty
		String theToken = currentToken();
		if (theToken.equals("ID") || theToken.equals(";") || theToken.equals("Number") || theToken.equals("int")
				|| theToken.equals("void") || theToken.equals("{") || theToken.equals("if")
				|| theToken.equals("while")) {

			stmnt();
			leftSide = stmntList();
		}

		else if (theToken.equals("return")) {
			return returnStmnt();
		}

		if (!leftSide.equals("")) {
			return leftSide;
		}

		return "noReturn";
	}

	void stmnt() {

		String theToken = currentToken();

		// if ID
		if (theToken.equals("ID") || theToken.equals("Number") || theToken.equals(";"))
			expressionStmnt();

		// or

		// if {
		else if (theToken.equals("{"))
			compoundStmnt();

		// or

		// if if
		else if (theToken.equals("if"))
			selectionStmnt();

		// or

		// if while
		else if (theToken.equals("while"))
			iterationStmnt();

		// or

		// if return
		else if (theToken.equals("return"))
			returnStmnt();

		else if (theToken.equals("void") || theToken.equals("int")) {
			localDec();
		}
	}

	void expressionStmnt() {
		String theToken = currentToken();
		if (theToken.equals(";")) {
			accept(";");
			return;
		}

		else {
			expression();
			accept(";");
		}

	}

	void selectionStmnt() {
		accept("if");
		accept("(");
		expression();
		accept(")");
		stmnt();

		String theToken = currentToken();
		if (theToken.equals("else")) {
			accept("else");
			stmnt();
		}

	}

	void iterationStmnt() {
		accept("while");
		accept("(");
		expression();
		accept(")");
		stmnt();
	}

	String returnStmnt() {
		accept("return");
		String theToken = currentToken();
		String returnType = "";

		if (theToken.equals("ID") || theToken.equals("(") || theToken.equals("Number")) {
			returnType = expression();
		}

		else {
			returnType = "none";
		}

		accept(";");

		return returnType;
	}

	String expression() {

		String returnType = "", leftSide = "", saveID = "";

		// if ID
		String theToken = currentToken();

		// if var = expression
		if (theToken.equals("ID")) {
			String next = lookAhead();
			// returns int or void
			saveID = currentLex();
			leftSide = var();
			theToken = currentToken();
			next = lookAhead();

			// if var = expression
			if (theToken.equals("=")) {

				if (next.equals(";")) {
					error();
				}

				accept("=");
				returnType = expression();
			}

			// if ID ( args )
			else if (theToken.equals("(")) {
				returnType = call(saveID);
				theToken = currentToken();
				if (!theToken.equals(";")) {
					returnType = expression();
				}
				
				if (returnType.equals("void") && theToken.equals("+")) {
					error();
				}

				else if (returnType.equals("void") && theToken.equals("-")) {
					error();
				}

				else if (returnType.equals("void") && theToken.equals("*")) {
					error();
				}

				else if (returnType.equals("void") && theToken.equals("/")) {
					error();
				}

			}
			
			else if(theToken.equals("compare")) {
				returnType = simpleExpression();
			}

			// if 2+2 or (2+2) or (var1 + var2)
			else if (theToken.equals("+") || theToken.equals("-") || theToken.equals("/") || theToken.equals("*")){
				returnType = simpleExpression();
				theToken = currentToken();
				if (theToken.equals("=")) {
					returnType = expression();
				}
			}
			try {
				if (hashtl.search(saveID).isArray()) {
					returnType = "array";
				}
			}
			catch (Exception e){}
			
			if(returnType.equals("")) {
				returnType = leftSide;
			}
			
		}

		else if (theToken.equals("=")) {
			accept("=");
			returnType = expression();
		}

		else {
			returnType = simpleExpression();
		}
		
		theToken = currentToken();

		if (returnType.equals("void") && theToken.equals("+")) {
			error();
		}

		else if (returnType.equals("void") && theToken.equals("-")) {
			error();
		}

		else if (returnType.equals("void") && theToken.equals("*")) {
			error();
		}

		else if (returnType.equals("void") && theToken.equals("/")) {
			error();
		}

		return returnType;

	}

	String var() {

		String theToken = currentToken();
		String varType = "";

		// find ID in hash table
		String VarID = currentLex();

		try {
			varType = hashtl.search(VarID).getType();
		} catch (Exception e) {
			error();
		}

		// if cant find variable in hash table, throw error

		accept("ID");

		theToken = currentToken();

		if (theToken.equals("[")) {
			accept("[");
			expression();
			accept("]");
			hashtl.changeToArrayT(VarID);
		}

		return varType;

	}

	String simpleExpression() {
		String leftSide = additiveExpression();
		String rightSide = "";

		String theToken = currentToken();
		if (theToken.equals("compare")) {
			relOp();
			rightSide = additiveExpression();
		}
		// or

		else if (theToken.equals("=")) {
			rightSide = expression();
		}

		// additiveExpression();

		return leftSide;
	}

	String additiveExpression() {
		String leftSide = "";
		String theToken = currentToken();
		if (theToken.equals("+") || theToken.equals("-")) {
			leftSide = additiveExpressionP();
		}

		else {
			leftSide = term();
			additiveExpressionP();
		}

		return leftSide;
	}

	void relOp() {
		accept("compare");
	}

	String additiveExpressionP() {
		String leftSide = "";

		// if not empty
		String theToken = currentToken();
		if (theToken.equals("+") || theToken.equals("-")) {
			addOp();
			leftSide = term();
			additiveExpressionP();
		}

		return leftSide;
	}

	void addOp() {

		String theToken = currentToken();
		if (theToken.equals("+"))
			accept("+");

		// or

		else
			accept("-");

	}

	String term() {

		String leftSide = "", rightSide = "";

		leftSide = factor();
		rightSide = termP();

		return leftSide;
	}

	String termP() {
		String leftSide = "", rightSide = "";

		// if not empty
		String theToken = currentToken();
		if (theToken.equals("*") || theToken.equals("/")) {
			mulOp();
			leftSide = factor();
			termP();
		}

		else {
		}

		return leftSide;
	}

	void mulOp() {
		String theToken = currentToken();
		if (theToken.equals("*"))
			accept("*");

		// or

		else
			accept("/");
	}

	String factor() {
		// if (
		String theToken = currentToken();
		String next = lookAhead();
		String leftSide = "";

		if (theToken.equals("(")) {
			accept("(");
			leftSide = expression();
			accept(")");

			return leftSide;
		}
		// or

		else if (theToken.equals("ID") && next.equals("(")) {
			String test = currentLex();
			String varName = hashtl.search(test).getVar();
			accept("ID");
			call(varName);

			// if not a function
			if (!hashtl.search(varName).isFunc()) {
				error();
			}

			leftSide = hashtl.search(varName).getType();
		}

		// if ID

		else if (theToken.equals("ID")) {
			leftSide = var();
		}

		// or

		else if (theToken.equals("compare")) {
			if (next.equals("Number")) {
				accept("compare");
				accept("Number");
				leftSide = "int";
			}

			else if (next.equals("ID")) {
				accept("compare");
				leftSide = var();
			}
		}

		// if ID and (

		// or

		else if (theToken.equals("Number")) {
			accept("Number");
			leftSide = "int";
		}

		return leftSide;
	}

	String call(String ID) {
		numArg = 0;
		accept("(");
		args();
		accept(")");

		int numOfActualParams = (hashtl.search(ID).getNumParam());

		if (numArg == numOfActualParams) {
			return hashtl.search(ID).getType();
		}

		else {
			error();
		}
		
		return "";

	}

	void args() {

		// could be empty
		argsList();

	}

	void argsList() {

		String theToken = currentToken();
		if (theToken.equals("ID") || theToken.equals("(")) {
			expression();
			numArg++;
			argsListP();
		}

		else if (theToken.equals("Number")) {
			accept("Number");
			numArg++;
			argsListP();
		}

		else
			return;
	}

	void argsListP() {

		// if not empty
		String theToken = currentToken();
		if (theToken.equals(",")) {
			accept(",");
			numArg++;
			expression();
			argsListP();
		}

		else
			return;
	}

}

// to remember variable values
class hashItem {
	private String type, var, val;
	private int numParam;
	private boolean isArray, isFunc;

	public hashItem(String type, String var, String val, int numParam) {
		this.type = type;
		this.var = var;
		this.val = val;
		this.numParam = numParam;
	}

	public String getType() {
		return type;
	}

	public String getVar() {
		return var;
	}

	public String getVal() {
		return val;
	}

	public int getNumParam() {
		return numParam;
	}

	public boolean isArray() {
		return isArray;
	}

	public void setArrayTrue() {
		this.isArray = true;
	}

	public void setArrayFalse() {
		this.isArray = false;
	}

	public boolean isFunc() {
		return isFunc;
	}

	public void setFuncTrue() {
		this.isFunc = true;
	}

	public void setFuncFalse() {
		this.isFunc = false;
	}

	public void setValofVar(String newVal) {
		this.val = newVal;
	}

	public void setNumParam(int num) {
		numParam = num;
	}

	public void addParam() {
		numParam++;
	}
}

class HTable {
	private hashItem[] table;
	private int size;
	public HTable next;
	public HTable prev;

	public HTable() {
		size = getPrime(9999);
		table = new hashItem[size];
		next = null;
		prev = null;
	}

	// for hash array size
	private int getPrime(int min) {
		for (int j = min + 1; true; j++)
			if (isPrime(j))
				return j;
	}

	private boolean isPrime(int n) {
		for (int j = 2; (j * j <= n); j++)
			if (n % j == 0)
				return false;
		return true;
	}

	public int hashIndex(String word) {

		// convert string to ascii number
		int key = 0, i;
		for (i = 0; i < word.length(); i++) {
			key += word.charAt(i);
		}

		// return hash number

		return key % size;
	}

	public void insert(String type, String var, String val, int numParam) {

		// hash key
		int index = hashIndex(var);

		if (search(var) == null) {

			// make new data object
			hashItem stuff = new hashItem(type, var, val, numParam);

			// move in case of collision
			while (table[index] != null) {
				++index;
				index %= size;
			}

			// insert
			table[index] = stuff;
		}

		// if already exist
		else {
			System.out.println("REGECT");
			System.exit(0);
		}

	}

	public hashItem search(String word) {
		int index = hashIndex(word);

		// hash search
		while (table[index] != null) {

			// if found, return.
			String test = table[index].getVar();
			if (test.equals(word)) {
				return table[index];
			}

			// if not, move
			++index;
			index %= size;
		}

		// can't find
		return null;
	}
}

//get ID value: int value = hashLink.current.search("a").getVal();
//get ID data type: String type = hashLink.current.search("a").getType();

class HLink {

	public HTable first;
	public HTable last;
	public HTable current;
	public int currentTableIndex = 0;

	public HLink() {
		first = null;
		last = null;
	}

	public void insertNewTable() {
		HTable newTable = new HTable(); // make new table
		if (isEmpty()) { // if empty list,
			first = newTable; // first --> newTable
		} else {
			last.next = newTable; // old last --> newTable
			newTable.prev = last; // old last <-- newTable
		}

		last = newTable;
		current = last;
		
	}

	public boolean isEmpty() // true if no links
	{
		return first == null;
	}

	public void moveBack() {
		current = current.prev;
		currentTableIndex--;
	}

	public void moveForward() {
		current = current.next;
		currentTableIndex++;
	}

	public void insertAtCurrentTable(String type, String var, String val, int numParam) {
		current.insert(type, var, val, numParam);
	}

	public hashItem search(String search) {
		
		int numOfMoveBacks = 0;

		hashItem returnSearch = current.search(search);

		if (returnSearch == null) {
			moveBack();
			numOfMoveBacks++;
			while (current != null) {
				returnSearch = current.search(search);

				if (returnSearch != null) {
					
					//reset
					for(int i = 0; i < numOfMoveBacks; i++) {
						moveForward();
					}
					
					return returnSearch;
				}

				moveBack();
				numOfMoveBacks++;
			}
		}

		else if (returnSearch != null) {
			return returnSearch;
		}

		for(int i = 0; i < numOfMoveBacks; i++) {
			moveForward();
		}
		// cant find
		return null;
	}

	public void changeValue(String search, String newVal) {
		search(search).setValofVar(newVal);
	}

	public void changeToFNT(String search) {
		search(search).setFuncTrue();
	}

	public void changeToFNF(String search) {
		search(search).setFuncFalse();
	}

	public void changeToArrayT(String search) {
		search(search).setArrayTrue();
	}

	public void changeToArrayF(String search) {
		search(search).setArrayFalse();
	}

	public boolean isArray(String search) {
		return search(search).isArray();
	}

	public String getType(String search) {
		return search(search).getType();
	}

	public boolean isCurrentNull() {
		return current == null;
	}
}
