// Generated from C:/Users/Danila/IdeaProjects/safron++/src\safron.g4 by ANTLR 4.9.2
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class safronLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, INT=2, FLOAT=3, PRINT=4, WHILE=5, DO=6, IF=7, THEN=8, BEGIN=9, 
		END=10, CALL=11, CONST=12, VAR=13, PROCEDURE=14, FUNCTION=15, BREAK=16, 
		CONTINUE=17, RETURN=18, DEF=19, VOID=20, AND=21, OR=22, NOT=23, ASSIGN=24, 
		COMMA=25, SEMI=26, COLON=27, PLUS=28, MINUS=29, STAR=30, DIV=31, MOD=32, 
		EQUAL=33, NOT_EQUAL=34, LE=35, LT=36, GE=37, GT=38, LPAREN=39, RPAREN=40, 
		LCURLY=41, RCURLY=42, IDENT=43, NUMBER=44, NUM_FLOAT=45, STRING=46, COMMENT=47, 
		WS=48, ErrorChar=49;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "INT", "FLOAT", "PRINT", "WHILE", "DO", "IF", "THEN", "BEGIN", 
			"END", "CALL", "CONST", "VAR", "PROCEDURE", "FUNCTION", "BREAK", "CONTINUE", 
			"RETURN", "DEF", "VOID", "AND", "OR", "NOT", "ASSIGN", "COMMA", "SEMI", 
			"COLON", "PLUS", "MINUS", "STAR", "DIV", "MOD", "EQUAL", "NOT_EQUAL", 
			"LE", "LT", "GE", "GT", "LPAREN", "RPAREN", "LCURLY", "RCURLY", "IDENT", 
			"NUMBER", "NUM_FLOAT", "STRING", "COMMENT", "WS", "ErrorChar", "A", "B", 
			"C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", 
			"Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'.'", null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			"'='", "','", "';'", "':'", "'+'", "'-'", "'*'", "'/'", "'%'", "'=='", 
			"'!='", "'<='", "'<'", "'>='", "'>'", "'('", "')'", "'{'", "'}'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, "INT", "FLOAT", "PRINT", "WHILE", "DO", "IF", "THEN", "BEGIN", 
			"END", "CALL", "CONST", "VAR", "PROCEDURE", "FUNCTION", "BREAK", "CONTINUE", 
			"RETURN", "DEF", "VOID", "AND", "OR", "NOT", "ASSIGN", "COMMA", "SEMI", 
			"COLON", "PLUS", "MINUS", "STAR", "DIV", "MOD", "EQUAL", "NOT_EQUAL", 
			"LE", "LT", "GE", "GT", "LPAREN", "RPAREN", "LCURLY", "RCURLY", "IDENT", 
			"NUMBER", "NUM_FLOAT", "STRING", "COMMENT", "WS", "ErrorChar"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public safronLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "safron.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\63\u01ae\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64"+
		"\t\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t"+
		"=\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4"+
		"I\tI\4J\tJ\4K\tK\4L\tL\3\2\3\2\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3"+
		"\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\f"+
		"\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\17\3\17"+
		"\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24"+
		"\3\24\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\30"+
		"\3\30\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3\33\3\34\3\34\3\35\3\35\3\36"+
		"\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3\"\3#\3#\3#\3$\3$\3$\3%\3%\3&\3&"+
		"\3&\3\'\3\'\3(\3(\3)\3)\3*\3*\3+\3+\3,\3,\7,\u013f\n,\f,\16,\u0142\13"+
		",\3-\6-\u0145\n-\r-\16-\u0146\3.\6.\u014a\n.\r.\16.\u014b\3.\3.\7.\u0150"+
		"\n.\f.\16.\u0153\13.\3/\3/\7/\u0157\n/\f/\16/\u015a\13/\3/\3/\3/\7/\u015f"+
		"\n/\f/\16/\u0162\13/\3/\5/\u0165\n/\3\60\3\60\3\60\3\60\7\60\u016b\n\60"+
		"\f\60\16\60\u016e\13\60\3\60\3\60\3\60\3\60\3\60\3\61\3\61\3\61\3\61\3"+
		"\62\3\62\3\63\3\63\3\64\3\64\3\65\3\65\3\66\3\66\3\67\3\67\38\38\39\3"+
		"9\3:\3:\3;\3;\3<\3<\3=\3=\3>\3>\3?\3?\3@\3@\3A\3A\3B\3B\3C\3C\3D\3D\3"+
		"E\3E\3F\3F\3G\3G\3H\3H\3I\3I\3J\3J\3K\3K\3L\3L\5\u0158\u0160\u016c\2M"+
		"\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20"+
		"\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37"+
		"= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\2g\2i\2k\2m\2o\2q"+
		"\2s\2u\2w\2y\2{\2}\2\177\2\u0081\2\u0083\2\u0085\2\u0087\2\u0089\2\u008b"+
		"\2\u008d\2\u008f\2\u0091\2\u0093\2\u0095\2\u0097\2\3\2\5\4\2aac|\3\2\62"+
		";\5\2\13\f\17\17\"\"\2\u019b\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3"+
		"\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2"+
		"\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37"+
		"\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3"+
		"\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2"+
		"\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C"+
		"\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2"+
		"\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2"+
		"\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\3\u0099\3\2\2\2\5\u009b\3"+
		"\2\2\2\7\u009f\3\2\2\2\t\u00a5\3\2\2\2\13\u00ab\3\2\2\2\r\u00b1\3\2\2"+
		"\2\17\u00b4\3\2\2\2\21\u00b7\3\2\2\2\23\u00bc\3\2\2\2\25\u00c2\3\2\2\2"+
		"\27\u00c6\3\2\2\2\31\u00cb\3\2\2\2\33\u00d1\3\2\2\2\35\u00d5\3\2\2\2\37"+
		"\u00df\3\2\2\2!\u00e8\3\2\2\2#\u00ee\3\2\2\2%\u00f7\3\2\2\2\'\u00fe\3"+
		"\2\2\2)\u0102\3\2\2\2+\u0107\3\2\2\2-\u010b\3\2\2\2/\u010e\3\2\2\2\61"+
		"\u0112\3\2\2\2\63\u0114\3\2\2\2\65\u0116\3\2\2\2\67\u0118\3\2\2\29\u011a"+
		"\3\2\2\2;\u011c\3\2\2\2=\u011e\3\2\2\2?\u0120\3\2\2\2A\u0122\3\2\2\2C"+
		"\u0124\3\2\2\2E\u0127\3\2\2\2G\u012a\3\2\2\2I\u012d\3\2\2\2K\u012f\3\2"+
		"\2\2M\u0132\3\2\2\2O\u0134\3\2\2\2Q\u0136\3\2\2\2S\u0138\3\2\2\2U\u013a"+
		"\3\2\2\2W\u013c\3\2\2\2Y\u0144\3\2\2\2[\u0149\3\2\2\2]\u0164\3\2\2\2_"+
		"\u0166\3\2\2\2a\u0174\3\2\2\2c\u0178\3\2\2\2e\u017a\3\2\2\2g\u017c\3\2"+
		"\2\2i\u017e\3\2\2\2k\u0180\3\2\2\2m\u0182\3\2\2\2o\u0184\3\2\2\2q\u0186"+
		"\3\2\2\2s\u0188\3\2\2\2u\u018a\3\2\2\2w\u018c\3\2\2\2y\u018e\3\2\2\2{"+
		"\u0190\3\2\2\2}\u0192\3\2\2\2\177\u0194\3\2\2\2\u0081\u0196\3\2\2\2\u0083"+
		"\u0198\3\2\2\2\u0085\u019a\3\2\2\2\u0087\u019c\3\2\2\2\u0089\u019e\3\2"+
		"\2\2\u008b\u01a0\3\2\2\2\u008d\u01a2\3\2\2\2\u008f\u01a4\3\2\2\2\u0091"+
		"\u01a6\3\2\2\2\u0093\u01a8\3\2\2\2\u0095\u01aa\3\2\2\2\u0097\u01ac\3\2"+
		"\2\2\u0099\u009a\7\60\2\2\u009a\4\3\2\2\2\u009b\u009c\5u;\2\u009c\u009d"+
		"\5\177@\2\u009d\u009e\5\u008bF\2\u009e\6\3\2\2\2\u009f\u00a0\5o8\2\u00a0"+
		"\u00a1\5{>\2\u00a1\u00a2\5\u0081A\2\u00a2\u00a3\5e\63\2\u00a3\u00a4\5"+
		"\u008bF\2\u00a4\b\3\2\2\2\u00a5\u00a6\5\u0083B\2\u00a6\u00a7\5\u0087D"+
		"\2\u00a7\u00a8\5u;\2\u00a8\u00a9\5\177@\2\u00a9\u00aa\5\u008bF\2\u00aa"+
		"\n\3\2\2\2\u00ab\u00ac\5\u0091I\2\u00ac\u00ad\5s:\2\u00ad\u00ae\5u;\2"+
		"\u00ae\u00af\5{>\2\u00af\u00b0\5m\67\2\u00b0\f\3\2\2\2\u00b1\u00b2\5k"+
		"\66\2\u00b2\u00b3\5\u0081A\2\u00b3\16\3\2\2\2\u00b4\u00b5\5u;\2\u00b5"+
		"\u00b6\5o8\2\u00b6\20\3\2\2\2\u00b7\u00b8\5\u008bF\2\u00b8\u00b9\5s:\2"+
		"\u00b9\u00ba\5m\67\2\u00ba\u00bb\5\177@\2\u00bb\22\3\2\2\2\u00bc\u00bd"+
		"\5g\64\2\u00bd\u00be\5m\67\2\u00be\u00bf\5q9\2\u00bf\u00c0\5u;\2\u00c0"+
		"\u00c1\5\177@\2\u00c1\24\3\2\2\2\u00c2\u00c3\5m\67\2\u00c3\u00c4\5\177"+
		"@\2\u00c4\u00c5\5k\66\2\u00c5\26\3\2\2\2\u00c6\u00c7\5i\65\2\u00c7\u00c8"+
		"\5e\63\2\u00c8\u00c9\5{>\2\u00c9\u00ca\5{>\2\u00ca\30\3\2\2\2\u00cb\u00cc"+
		"\5i\65\2\u00cc\u00cd\5\u0081A\2\u00cd\u00ce\5\177@\2\u00ce\u00cf\5\u0089"+
		"E\2\u00cf\u00d0\5\u008bF\2\u00d0\32\3\2\2\2\u00d1\u00d2\5\u008fH\2\u00d2"+
		"\u00d3\5e\63\2\u00d3\u00d4\5\u0087D\2\u00d4\34\3\2\2\2\u00d5\u00d6\5\u0083"+
		"B\2\u00d6\u00d7\5\u0087D\2\u00d7\u00d8\5\u0081A\2\u00d8\u00d9\5i\65\2"+
		"\u00d9\u00da\5m\67\2\u00da\u00db\5k\66\2\u00db\u00dc\5\u008dG\2\u00dc"+
		"\u00dd\5\u0087D\2\u00dd\u00de\5m\67\2\u00de\36\3\2\2\2\u00df\u00e0\5o"+
		"8\2\u00e0\u00e1\5\u008dG\2\u00e1\u00e2\5\177@\2\u00e2\u00e3\5i\65\2\u00e3"+
		"\u00e4\5\u008bF\2\u00e4\u00e5\5u;\2\u00e5\u00e6\5\u0081A\2\u00e6\u00e7"+
		"\5\177@\2\u00e7 \3\2\2\2\u00e8\u00e9\5g\64\2\u00e9\u00ea\5\u0087D\2\u00ea"+
		"\u00eb\5m\67\2\u00eb\u00ec\5e\63\2\u00ec\u00ed\5y=\2\u00ed\"\3\2\2\2\u00ee"+
		"\u00ef\5i\65\2\u00ef\u00f0\5\u0081A\2\u00f0\u00f1\5\177@\2\u00f1\u00f2"+
		"\5\u008bF\2\u00f2\u00f3\5u;\2\u00f3\u00f4\5\177@\2\u00f4\u00f5\5\u008d"+
		"G\2\u00f5\u00f6\5m\67\2\u00f6$\3\2\2\2\u00f7\u00f8\5\u0087D\2\u00f8\u00f9"+
		"\5m\67\2\u00f9\u00fa\5\u008bF\2\u00fa\u00fb\5\u008dG\2\u00fb\u00fc\5\u0087"+
		"D\2\u00fc\u00fd\5\177@\2\u00fd&\3\2\2\2\u00fe\u00ff\5k\66\2\u00ff\u0100"+
		"\5m\67\2\u0100\u0101\5o8\2\u0101(\3\2\2\2\u0102\u0103\5\u008fH\2\u0103"+
		"\u0104\5\u0081A\2\u0104\u0105\5u;\2\u0105\u0106\5k\66\2\u0106*\3\2\2\2"+
		"\u0107\u0108\5e\63\2\u0108\u0109\5\177@\2\u0109\u010a\5k\66\2\u010a,\3"+
		"\2\2\2\u010b\u010c\5\u0081A\2\u010c\u010d\5\u0087D\2\u010d.\3\2\2\2\u010e"+
		"\u010f\5\177@\2\u010f\u0110\5\u0081A\2\u0110\u0111\5\u008bF\2\u0111\60"+
		"\3\2\2\2\u0112\u0113\7?\2\2\u0113\62\3\2\2\2\u0114\u0115\7.\2\2\u0115"+
		"\64\3\2\2\2\u0116\u0117\7=\2\2\u0117\66\3\2\2\2\u0118\u0119\7<\2\2\u0119"+
		"8\3\2\2\2\u011a\u011b\7-\2\2\u011b:\3\2\2\2\u011c\u011d\7/\2\2\u011d<"+
		"\3\2\2\2\u011e\u011f\7,\2\2\u011f>\3\2\2\2\u0120\u0121\7\61\2\2\u0121"+
		"@\3\2\2\2\u0122\u0123\7\'\2\2\u0123B\3\2\2\2\u0124\u0125\7?\2\2\u0125"+
		"\u0126\7?\2\2\u0126D\3\2\2\2\u0127\u0128\7#\2\2\u0128\u0129\7?\2\2\u0129"+
		"F\3\2\2\2\u012a\u012b\7>\2\2\u012b\u012c\7?\2\2\u012cH\3\2\2\2\u012d\u012e"+
		"\7>\2\2\u012eJ\3\2\2\2\u012f\u0130\7@\2\2\u0130\u0131\7?\2\2\u0131L\3"+
		"\2\2\2\u0132\u0133\7@\2\2\u0133N\3\2\2\2\u0134\u0135\7*\2\2\u0135P\3\2"+
		"\2\2\u0136\u0137\7+\2\2\u0137R\3\2\2\2\u0138\u0139\7}\2\2\u0139T\3\2\2"+
		"\2\u013a\u013b\7\177\2\2\u013bV\3\2\2\2\u013c\u0140\4c|\2\u013d\u013f"+
		"\t\2\2\2\u013e\u013d\3\2\2\2\u013f\u0142\3\2\2\2\u0140\u013e\3\2\2\2\u0140"+
		"\u0141\3\2\2\2\u0141X\3\2\2\2\u0142\u0140\3\2\2\2\u0143\u0145\t\3\2\2"+
		"\u0144\u0143\3\2\2\2\u0145\u0146\3\2\2\2\u0146\u0144\3\2\2\2\u0146\u0147"+
		"\3\2\2\2\u0147Z\3\2\2\2\u0148\u014a\4\62;\2\u0149\u0148\3\2\2\2\u014a"+
		"\u014b\3\2\2\2\u014b\u0149\3\2\2\2\u014b\u014c\3\2\2\2\u014c\u014d\3\2"+
		"\2\2\u014d\u0151\7\60\2\2\u014e\u0150\4\62;\2\u014f\u014e\3\2\2\2\u0150"+
		"\u0153\3\2\2\2\u0151\u014f\3\2\2\2\u0151\u0152\3\2\2\2\u0152\\\3\2\2\2"+
		"\u0153\u0151\3\2\2\2\u0154\u0158\7$\2\2\u0155\u0157\13\2\2\2\u0156\u0155"+
		"\3\2\2\2\u0157\u015a\3\2\2\2\u0158\u0159\3\2\2\2\u0158\u0156\3\2\2\2\u0159"+
		"\u015b\3\2\2\2\u015a\u0158\3\2\2\2\u015b\u0165\7$\2\2\u015c\u0160\7)\2"+
		"\2\u015d\u015f\13\2\2\2\u015e\u015d\3\2\2\2\u015f\u0162\3\2\2\2\u0160"+
		"\u0161\3\2\2\2\u0160\u015e\3\2\2\2\u0161\u0163\3\2\2\2\u0162\u0160\3\2"+
		"\2\2\u0163\u0165\7)\2\2\u0164\u0154\3\2\2\2\u0164\u015c\3\2\2\2\u0165"+
		"^\3\2\2\2\u0166\u0167\7\61\2\2\u0167\u0168\7,\2\2\u0168\u016c\3\2\2\2"+
		"\u0169\u016b\13\2\2\2\u016a\u0169\3\2\2\2\u016b\u016e\3\2\2\2\u016c\u016d"+
		"\3\2\2\2\u016c\u016a\3\2\2\2\u016d\u016f\3\2\2\2\u016e\u016c\3\2\2\2\u016f"+
		"\u0170\7,\2\2\u0170\u0171\7\61\2\2\u0171\u0172\3\2\2\2\u0172\u0173\b\60"+
		"\2\2\u0173`\3\2\2\2\u0174\u0175\t\4\2\2\u0175\u0176\3\2\2\2\u0176\u0177"+
		"\b\61\2\2\u0177b\3\2\2\2\u0178\u0179\13\2\2\2\u0179d\3\2\2\2\u017a\u017b"+
		"\7C\2\2\u017bf\3\2\2\2\u017c\u017d\7D\2\2\u017dh\3\2\2\2\u017e\u017f\7"+
		"E\2\2\u017fj\3\2\2\2\u0180\u0181\7F\2\2\u0181l\3\2\2\2\u0182\u0183\7G"+
		"\2\2\u0183n\3\2\2\2\u0184\u0185\7H\2\2\u0185p\3\2\2\2\u0186\u0187\7I\2"+
		"\2\u0187r\3\2\2\2\u0188\u0189\7J\2\2\u0189t\3\2\2\2\u018a\u018b\7K\2\2"+
		"\u018bv\3\2\2\2\u018c\u018d\7L\2\2\u018dx\3\2\2\2\u018e\u018f\7M\2\2\u018f"+
		"z\3\2\2\2\u0190\u0191\7N\2\2\u0191|\3\2\2\2\u0192\u0193\7O\2\2\u0193~"+
		"\3\2\2\2\u0194\u0195\7P\2\2\u0195\u0080\3\2\2\2\u0196\u0197\7Q\2\2\u0197"+
		"\u0082\3\2\2\2\u0198\u0199\7R\2\2\u0199\u0084\3\2\2\2\u019a\u019b\7S\2"+
		"\2\u019b\u0086\3\2\2\2\u019c\u019d\7T\2\2\u019d\u0088\3\2\2\2\u019e\u019f"+
		"\7U\2\2\u019f\u008a\3\2\2\2\u01a0\u01a1\7V\2\2\u01a1\u008c\3\2\2\2\u01a2"+
		"\u01a3\7W\2\2\u01a3\u008e\3\2\2\2\u01a4\u01a5\7X\2\2\u01a5\u0090\3\2\2"+
		"\2\u01a6\u01a7\7Y\2\2\u01a7\u0092\3\2\2\2\u01a8\u01a9\7Z\2\2\u01a9\u0094"+
		"\3\2\2\2\u01aa\u01ab\7[\2\2\u01ab\u0096\3\2\2\2\u01ac\u01ad\7\\\2\2\u01ad"+
		"\u0098\3\2\2\2\13\2\u0140\u0146\u014b\u0151\u0158\u0160\u0164\u016c\3"+
		"\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}