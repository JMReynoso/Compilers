import java.io.*;
import java.util.*;
import java.util.regex.*;

public class compilers {

	public static void main(String args[]) throws IOException{
		
		
		try {
			//read file
			String filename=args[0];
			File file=new File(filename);
			Scanner scanFile = new Scanner(file);
			
			//lex analizer first
			linkedQ Qlink = new linkedQ();
			readFile(file, scanFile, Qlink);
			Qlink.insert("$", "$");
			Qlink.reset();
			
			p2 the = new p2(Qlink);
			
			System.out.println("\nEnd of Program");
		}
		
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
		
	}
	
	static void readFile(File file, Scanner scanFile, linkedQ Qlink) {
		
		
		
		boolean commentMode=false;
		StringBuilder wordBank = new StringBuilder();
		
		scanFile.useDelimiter("");

		
		while(scanFile.hasNext()) {
			//for comment case
			
			String character = scanFile.next();
			
			//skip \r and \n
			while(character.equals("\r") || character.equals("\n") || character.equals("\t")) {
				if(!scanFile.hasNext())	break;
				character = scanFile.next();
			}
			
			
			
			//if /* */comment
			if(character.equals("/") && scanFile.next().equals("*")) {
				commentMode=true;
				
				character=scanFile.next();
				while(commentMode==true) {
					
					if(character.equals("*")) {
						character=scanFile.next();
						if(character.equals("/")) {
							commentMode=false;
						}
					}
					else { character=scanFile.next();}
				}
			}
			
			//if // comment
			else if(character.equals("/") && scanFile.next().equals("/")){
				commentMode=true;
				
				character=scanFile.next();
				while(commentMode==true) {
					
					if(character.equals("\n") || character.equals("\r")) {
						commentMode=false;
					}
					
					else { character=scanFile.next();}
				}
			}
			
			//else, scan line
			else {
				
				//if white space, check to see if word bank is token
				if((character.equals(" ") && wordBank.length()!=0)) {
					makeToken(wordBank, Qlink);
				}
				
				//do nothing if you see a space lmao
				else if(character.equals(" ") || character.equals("\n") || character.equals("\r") || character.equals("\t")) { }
				
				//if character is operator
				//operations print word bank from above algorithm
				
				else if(character.equals(">") && scanFile.next().equals("="))	{
					if(wordBank.length()!=0) {makeToken(wordBank, Qlink);}
					Qlink.insert("compare", ">=");}
				else if(character.equals("=") && scanFile.next().equals("="))	{
					if(wordBank.length()!=0) {makeToken(wordBank, Qlink);}
					Qlink.insert("compare", "==");}
				else if(character.equals("!") && scanFile.next().equals("="))	{
					if(wordBank.length()!=0) {makeToken(wordBank, Qlink);}
					Qlink.insert("compare", "!=");}
				else if(character.equals("<") && scanFile.next().equals("="))	{
					if(wordBank.length()!=0) {makeToken(wordBank, Qlink);}
					Qlink.insert("compare", "<=");}
				
				
				else if(character.equals("+")){
					if(wordBank.length()!=0) {makeToken(wordBank, Qlink);}
					Qlink.insert("+", "+");}
				else if(character.equals("-")) 	{
					if(wordBank.length()!=0) {makeToken(wordBank, Qlink);} 
					Qlink.insert("-", "-");}
				else if(character.equals("*")) 	{
					if(wordBank.length()!=0) {makeToken(wordBank, Qlink);}
					Qlink.insert("*", "*");}
				else if(character.equals("/")) 	{
					if(wordBank.length()!=0) {makeToken(wordBank, Qlink);}
					Qlink.insert("/", "/");}
				else if(character.equals("<")) 	{
					if(wordBank.length()!=0) {makeToken(wordBank, Qlink);}
					Qlink.insert("compare", "<");}
				else if(character.equals(">"))	{
					if(wordBank.length()!=0) {makeToken(wordBank, Qlink);}
					Qlink.insert("compare", ">");}
				else if(character.equals("=")) 	{
					if(wordBank.length()!=0) {makeToken(wordBank, Qlink);}
					Qlink.insert("=", "=");}
				else if(character.equals(";")) 	{
					if(wordBank.length()!=0) {makeToken(wordBank, Qlink);}
					Qlink.insert(";", ";");}
				else if(character.equals(",")) 	{
					if(wordBank.length()!=0) {makeToken(wordBank, Qlink);}
					Qlink.insert(",", ",");}
				else if(character.equals("[")) 	{
					if(wordBank.length()!=0) {makeToken(wordBank, Qlink);}
					Qlink.insert("[", "[");}
				else if(character.equals("]")) 	{
					if(wordBank.length()!=0) {makeToken(wordBank, Qlink);}
					Qlink.insert("]", "]");}
				else if(character.equals("{")) 	{
					if(wordBank.length()!=0) {makeToken(wordBank, Qlink);}
					Qlink.insert("{", "{");}
				else if(character.equals("}")) 	{
					if(wordBank.length()!=0) {makeToken(wordBank, Qlink);}
					Qlink.insert("}", "}");}
				else if(character.equals("(")) 	{
					if(wordBank.length()!=0) {makeToken(wordBank, Qlink);}
					Qlink.insert("(", "(");}
				else if(character.equals(")")) 	{
					if(wordBank.length()!=0) {makeToken(wordBank, Qlink);}
					Qlink.insert(")", ")");}

				//else append to word bank
				else {
					wordBank.append(character);
					if(scanFile.hasNext()==false) {
						makeToken(wordBank, Qlink);
					}
				}
			}
		
		}
		
	}
	
	static void makeToken(StringBuilder wordBank, linkedQ Qlink) {
		String token;
		String wordBankString = wordBank.toString();
		token = findToken(wordBankString);
		Qlink.insert(token, wordBankString);
		//reset wordBank
		wordBank.setLength(0);
	
	}
	
	static String findToken(String find) {
		
		//keyword
		if(find.equals("else")) {return "else";}
		else if(find.equals("if")) {return "if";}
		else if(find.equals("int")) {return "int";}
		else if(find.equals("return")) {return "return";}
		else if(find.equals("void")) {return "void";}
		else if(find.equals("while")) {return "while";}
		
		//letter
		else if(Pattern.matches("[a-zA-Z]", find)) {return "ID";}
		
		//ID
		else if(Pattern.matches("[a-zA-Z]+[a-zA-Z]*", find)) {return "ID";}
		
		//digit
		else if(Pattern.matches("\\d", find)) {return "Number";}
		
		//num
		else if(Pattern.matches("\\d+\\d*", find)) {return "Number";}
		
		//return error
		else return "Error";
	}

}

class data{
	public String token;
	public String lex;
	public data next;
	public data prev;
	
	public data(String token, String lex) {
		this.token = token;
		this.lex = lex;
	}
}


class linkList{
	
	private data first;
	private data last;
	private data current = first;
	private data lookAhead;
	private data lookAheadMore;

	
	public void linkList(){
		first = null;
		last = null;
	}
	
	public void insertLast(String token, String lex) {
		data newData = new data(token, lex);
		if(isEmpty()) {
			first = newData;
		}
		else
			last.next=newData;
		last = newData;
	}
	
	public boolean isEmpty() {return first==null;}

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

class linkedQ{
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
}


class p2 {
	private linkedQ Qlink;
	
	
	p2(linkedQ Qlink){
		this.Qlink = Qlink;
		program();
	}
	
	void error() {
		System.out.println("REGECTED");
		System.exit(0);
	}
	
	String lookAhead(){
		return Qlink.getNextToken().token;
	}
	
	void accept(String token) {
		
		String theToken = currentToken();
		
		if(theToken.equals(token)) {
			Qlink.moveNext();
		}
		else error();
	}
	
	String currentToken() {
		return Qlink.getCurrentToken().token;
	}
	
	void program() {
		decList();
		String theToken = currentToken();
		if(theToken.equals("$"))
			System.out.println("ACCEPTED");
		else System.out.println("REJECTED");
	}
	
	void decList(){
		declaration();
		decListP();
	}
	
	void decListP() {
		//if not empty
		String theToken = currentToken();
		if(theToken.equals("$"))	return;
		
		else {
			declaration();
			decListP();
		}
		
	}
	
	void declaration() {
		
		//use lookAhead2
		
		//if int ID; or int ID [Number];
		String theToken = currentToken();		
		
		if(theToken.equals("int") || theToken.equals("void")) {
			typeSpeci();
			accept("ID");
			theToken = currentToken();
			if(theToken.equals(";") || theToken.equals("["))
					varDec();
				
			//if int ID ( param ) compoundStatment
			else if(theToken.equals("("))
				funDec();
			
		}
		
		else return;
	}
	
	void varDec() {
		String theToken = currentToken();
		
		if(theToken.equals("int") || theToken.equals("void")) {
			typeSpeci();
			accept("ID");
			
			theToken = currentToken();
			if(theToken.equals("[")) {
				accept("[");
				accept("Number");
				accept("]");
			}			
			
			accept(";");
		}
		
		else if(theToken.equals("[")) {
			accept("[");
			accept("Number");
			accept("]");
			accept(";");
		}			
		


	}
	
	void typeSpeci(){
		String theToken = currentToken();
		
		if(theToken.equals("int"))	accept("int");
		
		//or
		
		else	accept("void");
	}
	
	
	void funDec() {
		accept("(");
		params();
		accept(")");
		compoundStmnt();
	}
	
	void params() {
		String theToken = currentToken();
		if(theToken.equals("void")) {
			accept("void");
			return;
		}
		paramList();
	}
	
	void paramList() {
		param();
		paramListP();
		
	}
	
	void paramListP() {
		
		//if not empty
		String theToken = currentToken();
		if(theToken.equals(",")) {
			accept(",");
			param();
			paramListP();
		}
		
		else return;
	}
	
	void param() {
		typeSpeci();
		accept("ID");
		
		String theToken = currentToken();
		if(theToken.equals("[")) {
			accept("[");
			accept("]");
		}


	}
	
	void compoundStmnt() {
		accept("{");
		localDec();
		stmntList();
		accept("}");
	}
	
	void localDec() {
		
		//if not empty
		String theToken = currentToken();
		if(theToken.equals("void") || theToken.equals("int")) {
			
			varDec();
			localDec();
			
		}
		
		else return;

	}
	
	void stmntList() {
		
		//if not empty
		String theToken = currentToken();
		if(theToken.equals("ID") || theToken.equals("{") || theToken.equals("if") || 
				theToken.equals("while") || theToken.equals("return")) {
		
				stmnt();
				stmntList();
		}
		
		else return;
	}
	
	void stmnt() {
		
		String theToken = currentToken();
		
		//if ID
		if(theToken.equals("ID"))			expressionStmnt();
		
		//or
		
		//if {
		else if(theToken.equals("{"))		compoundStmnt();
		
		//or
		
		//if if
		else if(theToken.equals("if"))		selectionStmnt();
		
		//or
		
		//if while
		else if(theToken.equals("while"))	iterationStmnt();
		
		//or
		
		//if return
		else if(theToken.equals("return"))	returnStmnt();
	}
	
	void expressionStmnt() {
		String theToken = currentToken();
		if(theToken.equals(";")) {
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
		if(theToken.equals("else")) {
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
	
	void returnStmnt() {
		accept("return");
		String theToken = currentToken();
		
		if(theToken.equals("ID") || theToken.equals("(") || theToken.equals("Number")) {
			expression();
		}
		accept(";");
	}
	
	void expression() {
		//if ID
		String theToken = currentToken();
		if(theToken.equals("ID")) {
			String next = lookAhead();
			if(next.equals("(")) {
				call();
				return;
			}
			
			
			var();
			theToken = currentToken();
			if(theToken.equals("=")) {
				accept("=");
				expression();				
			}
			
			//if 2+2 or (2+2) or (var1 + var2)
			else simpleExpression();	
		}
		
		else simpleExpression();

	}
	
	void var() {
		accept("ID");
		
		String theToken = currentToken();
		
		if(theToken.equals("[")) {
			accept("[");
			expression();
			accept("]");
		}

	}
	
	void simpleExpression() {
		additiveExpression();
		String theToken = currentToken();
		if(theToken.equals("comapre")) {
			relOp();
			additiveExpression();
		}
		//or
		
		//additiveExpression();
	}
	
	void additiveExpression() {
		
		String theToken = currentToken();
		if(theToken.equals("+") || theToken.equals("-")) {
			additiveExpressionP();
		}
		
		else {
			term();
			additiveExpressionP();
		}
	}
	
	
	void relOp() {
		accept("compare");
	}
	
	void additiveExpressionP() {
		
		//if not empty
		String theToken = currentToken();
		if(theToken.equals("+") || theToken.equals("-")) {
			addOp();
			term();
			additiveExpressionP();
		}
	}
	
	
	void addOp() {
		
		String theToken = currentToken();
		if(theToken.equals("+"))	accept("+");
		
		//or
		
		else accept("-");
		
	}
	
	void term() {
		factor();
		termP();
	}
	
	void termP() {
		
		//if not empty
		String theToken = currentToken();
		if(theToken.equals("*") || theToken.equals("/")) {
			mulOp();
			factor();
			termP();
		}
		
		else return;
	}
	
	void mulOp() {
		String theToken = currentToken();
		if(theToken.equals("*"))	accept("*");
		
		//or
		
		else	accept("/");
	}
	
	void factor() {
		//if (
		String theToken = currentToken();
		String next = lookAhead();
		if(theToken.equals("(")) {
			accept("(");
			expression();
			accept(")");
		}
		//or
		
		else if(theToken.equals("ID") && next.equals("("))	call();
		
		
		// if ID
		
		else if(theToken.equals("ID"))	var();
		
		//or
		
		else if(theToken.equals("compare")) {
			if(next.equals("Number")) {
				accept("compare");
				accept("Number");
				return;
			}
			
			else if(next.equals("ID")) {
				accept("compare");
				accept("ID");
			}
		}
		
		
		//if ID and (

		
		//or
		
		
		else if(theToken.equals("Number"))
			accept("Number");
	}
	
	void call() {
		accept("ID");
		accept("(");
		args();
		accept(")");
	}
	
	void args() {
		
		//could be empty
		argsList();
		
	}
	
	void argsList() {
		
		String theToken = currentToken();
		if(theToken.equals("ID") || theToken.equals("(")) {
			expression();
			argsListP();
		}
		
		else if(theToken.equals("Number")) {
			accept("Number");
			argsListP();
		}
		
		else return;
	}
	
	void argsListP() {
		
		//if not empty
		String theToken = currentToken();
		if(theToken.equals(",")) {
			accept(",");
			expression();
			argsListP();
		}
		
		else return;
	}
	
}


