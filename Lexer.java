import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Lexer {

	public static enum TokenType {
		// Token types cannot have underscores
//		FLOAT("-?([0-9]+\\.[0-9]*)|([0-9]*\\.[0-9]+)"),
		INT("[0-9]+"),
		STRING("\"(.*?)\""),
//		CHAR("'(.*?)'"),
//		WHITESPACE("[ ]+"),
		LEFTTPARAN("\\("),
		RIGHTPARAN("\\)"),
		LEFTTCURR("\\{"),
		RIGHTCURR("\\}"),
		PLUS("\\+"),
		MINUS("\\-"),
		MUL("\\*"),
		DIV("\\/"),
		CONJ("\\&\\&"),
		DISJ("\\|\\|"),
		DEQUALITY("=="),
		NEQUALITY("!="),
		EQUALITY("\\="),
		LETHEN("<="),
		GETHEN(">="),
		LTHEN("<"),
		GTHEN(">"),
		PRINT("print"),
		ERRORSS(".*Error: .*\\n"),
		SEMIC(";"),
		TYPEINT("int"),
		TYPEVOID("void"),
		TYPESTRING("string"),
		TYPEBOOL("boolean"),
		TRUE("true"),
		FALSE("false"),
		IF("if"),
		ELSE("else"),
		WHILE("while"),
		FOR("for"),
		IDENT("[a-zA-Z0-9_]+"),
		EOF("\\Z");

		// add more

		public final String pattern;

		private TokenType(String pattern) {
			this.pattern = pattern;
		}

	}

	public static class Token {
		private TokenType type;
		private String data;
		private int position;

		public Token(TokenType type, String data, int position) {
			this.type = type;
			this.data = data;
			this.position = position;
		}

		public TokenType getType() {
			return type;
		}

		public String getData() {
			return data;
		}

		public int getPosition() {
			return position;
		}

		@Override
		public String toString() {
			return String.format("(%s %s)", type.name(), data);
		}
	}

	public LinkedList<Token> lex(String input) {
		// The tokens to return
		LinkedList<Token> tokens = new LinkedList<Token>();

		// Lexer logic begins here
		StringBuffer tokenPatternsBuffer = new StringBuffer();
		for (TokenType tokenType : TokenType.values())
			tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));

		Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));

		// Begin matching tokens
		Matcher matcher = tokenPatterns.matcher(input);
		while (matcher.find()) {
			for (TokenType tokenType : TokenType.values()) {
				String tokenName = tokenType.name();
				if (matcher.group(tokenName) != null) {
					tokens.add(new Token(tokenType, matcher.group(tokenName), matcher.start(tokenName)));
				}
			}
		}

		return tokens;
	}
}