// Generated from /home/dhk/Desktop/UFC/Compilation/groupe-3/analyser-JajaCode/src/main/antlr4/fr/amvproject/group3/JajaCodeParser.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class JajaCodeParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		VIDE=1, INIT=2, SWAP=3, PUSH=4, NEW=5, NEWARRAY=6, INVOKE=7, GOTO=8, LOAD=9, 
		ALOAD=10, STORE=11, ASTORE=12, LENGHT=13, INC=14, AINC=15, IF=16, JCSTOP=17, 
		TRUE=18, FALSE=19, INT=20, BOOLEAN=21, POP=22, RETURN=23, WRITE=24, WRITELN=25, 
		VAR=26, METH=27, NEG=28, NOT=29, ADD=30, SUB=31, MUL=32, DIV=33, CMP=34, 
		SUP=35, OR=36, AND=37, NOP=38, COMMA=39, LPAREN=40, RPAREN=41, STRING=42, 
		IDENTIFICATEUR=43, NOMBRE=44, WS=45, COMMENT_LINE=46, COMMENT_BLOCK=47;
	public static final int
		RULE_program = 0, RULE_classe = 1, RULE_instr = 2, RULE_ident = 3, RULE_valeur = 4, 
		RULE_adresse = 5, RULE_oper = 6, RULE_oper1 = 7, RULE_oper2 = 8, RULE_type = 9, 
		RULE_sorte = 10;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "classe", "instr", "ident", "valeur", "adresse", "oper", "oper1", 
			"oper2", "type", "sorte"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'vide'", "'init'", "'swap'", "'push'", "'new'", "'newarray'", 
			"'invoke'", "'goto'", "'load'", "'aload'", "'store'", "'astore'", "'length'", 
			"'inc'", "'ainc'", "'if'", "'jcstop'", "'true'", "'false'", "'entier'", 
			"'boolean'", "'pop'", "'return'", "'write'", "'writeln'", "'var'", "'meth'", 
			"'neg'", "'not'", "'add'", "'sub'", "'mul'", "'div'", "'cmp'", "'sup'", 
			"'or'", "'and'", "'nop'", "','", "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "VIDE", "INIT", "SWAP", "PUSH", "NEW", "NEWARRAY", "INVOKE", "GOTO", 
			"LOAD", "ALOAD", "STORE", "ASTORE", "LENGHT", "INC", "AINC", "IF", "JCSTOP", 
			"TRUE", "FALSE", "INT", "BOOLEAN", "POP", "RETURN", "WRITE", "WRITELN", 
			"VAR", "METH", "NEG", "NOT", "ADD", "SUB", "MUL", "DIV", "CMP", "SUP", 
			"OR", "AND", "NOP", "COMMA", "LPAREN", "RPAREN", "STRING", "IDENTIFICATEUR", 
			"NOMBRE", "WS", "COMMENT_LINE", "COMMENT_BLOCK"
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

	@Override
	public String getGrammarFileName() { return "JajaCodeParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public JajaCodeParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(JajaCodeParser.EOF, 0); }
		public List<ClasseContext> classe() {
			return getRuleContexts(ClasseContext.class);
		}
		public ClasseContext classe(int i) {
			return getRuleContext(ClasseContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(23); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(22);
				classe();
				}
				}
				setState(25); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==NOMBRE );
			setState(27);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ClasseContext extends ParserRuleContext {
		public AdresseContext adresse() {
			return getRuleContext(AdresseContext.class,0);
		}
		public InstrContext instr() {
			return getRuleContext(InstrContext.class,0);
		}
		public ClasseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classe; }
	}

	public final ClasseContext classe() throws RecognitionException {
		ClasseContext _localctx = new ClasseContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_classe);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(29);
			adresse();
			setState(30);
			instr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InstrContext extends ParserRuleContext {
		public TerminalNode INIT() { return getToken(JajaCodeParser.INIT, 0); }
		public TerminalNode SWAP() { return getToken(JajaCodeParser.SWAP, 0); }
		public TerminalNode NEW() { return getToken(JajaCodeParser.NEW, 0); }
		public TerminalNode LPAREN() { return getToken(JajaCodeParser.LPAREN, 0); }
		public IdentContext ident() {
			return getRuleContext(IdentContext.class,0);
		}
		public List<TerminalNode> COMMA() { return getTokens(JajaCodeParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(JajaCodeParser.COMMA, i);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public SorteContext sorte() {
			return getRuleContext(SorteContext.class,0);
		}
		public ValeurContext valeur() {
			return getRuleContext(ValeurContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(JajaCodeParser.RPAREN, 0); }
		public TerminalNode NEWARRAY() { return getToken(JajaCodeParser.NEWARRAY, 0); }
		public TerminalNode INVOKE() { return getToken(JajaCodeParser.INVOKE, 0); }
		public TerminalNode RETURN() { return getToken(JajaCodeParser.RETURN, 0); }
		public TerminalNode PUSH() { return getToken(JajaCodeParser.PUSH, 0); }
		public TerminalNode POP() { return getToken(JajaCodeParser.POP, 0); }
		public TerminalNode LOAD() { return getToken(JajaCodeParser.LOAD, 0); }
		public TerminalNode ALOAD() { return getToken(JajaCodeParser.ALOAD, 0); }
		public TerminalNode STORE() { return getToken(JajaCodeParser.STORE, 0); }
		public TerminalNode ASTORE() { return getToken(JajaCodeParser.ASTORE, 0); }
		public TerminalNode WRITE() { return getToken(JajaCodeParser.WRITE, 0); }
		public TerminalNode WRITELN() { return getToken(JajaCodeParser.WRITELN, 0); }
		public TerminalNode LENGHT() { return getToken(JajaCodeParser.LENGHT, 0); }
		public TerminalNode IF() { return getToken(JajaCodeParser.IF, 0); }
		public AdresseContext adresse() {
			return getRuleContext(AdresseContext.class,0);
		}
		public TerminalNode GOTO() { return getToken(JajaCodeParser.GOTO, 0); }
		public TerminalNode INC() { return getToken(JajaCodeParser.INC, 0); }
		public TerminalNode AINC() { return getToken(JajaCodeParser.AINC, 0); }
		public OperContext oper() {
			return getRuleContext(OperContext.class,0);
		}
		public TerminalNode NOP() { return getToken(JajaCodeParser.NOP, 0); }
		public TerminalNode JCSTOP() { return getToken(JajaCodeParser.JCSTOP, 0); }
		public InstrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instr; }
	}

	public final InstrContext instr() throws RecognitionException {
		InstrContext _localctx = new InstrContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_instr);
		try {
			setState(114);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INIT:
				enterOuterAlt(_localctx, 1);
				{
				setState(32);
				match(INIT);
				}
				break;
			case SWAP:
				enterOuterAlt(_localctx, 2);
				{
				setState(33);
				match(SWAP);
				}
				break;
			case NEW:
				enterOuterAlt(_localctx, 3);
				{
				setState(34);
				match(NEW);
				setState(35);
				match(LPAREN);
				setState(36);
				ident();
				setState(37);
				match(COMMA);
				setState(38);
				type();
				setState(39);
				match(COMMA);
				setState(40);
				sorte();
				setState(41);
				match(COMMA);
				setState(42);
				valeur();
				setState(43);
				match(RPAREN);
				}
				break;
			case NEWARRAY:
				enterOuterAlt(_localctx, 4);
				{
				setState(45);
				match(NEWARRAY);
				setState(46);
				match(LPAREN);
				setState(47);
				ident();
				setState(48);
				match(COMMA);
				setState(49);
				type();
				setState(50);
				match(RPAREN);
				}
				break;
			case INVOKE:
				enterOuterAlt(_localctx, 5);
				{
				setState(52);
				match(INVOKE);
				setState(53);
				match(LPAREN);
				setState(54);
				ident();
				setState(55);
				match(RPAREN);
				}
				break;
			case RETURN:
				enterOuterAlt(_localctx, 6);
				{
				setState(57);
				match(RETURN);
				}
				break;
			case PUSH:
				enterOuterAlt(_localctx, 7);
				{
				setState(58);
				match(PUSH);
				setState(59);
				match(LPAREN);
				setState(60);
				valeur();
				setState(61);
				match(RPAREN);
				}
				break;
			case POP:
				enterOuterAlt(_localctx, 8);
				{
				setState(63);
				match(POP);
				}
				break;
			case LOAD:
				enterOuterAlt(_localctx, 9);
				{
				setState(64);
				match(LOAD);
				setState(65);
				match(LPAREN);
				setState(66);
				ident();
				setState(67);
				match(RPAREN);
				}
				break;
			case ALOAD:
				enterOuterAlt(_localctx, 10);
				{
				setState(69);
				match(ALOAD);
				setState(70);
				match(LPAREN);
				setState(71);
				ident();
				setState(72);
				match(RPAREN);
				}
				break;
			case STORE:
				enterOuterAlt(_localctx, 11);
				{
				setState(74);
				match(STORE);
				setState(75);
				match(LPAREN);
				setState(76);
				ident();
				setState(77);
				match(RPAREN);
				}
				break;
			case ASTORE:
				enterOuterAlt(_localctx, 12);
				{
				setState(79);
				match(ASTORE);
				setState(80);
				match(LPAREN);
				setState(81);
				ident();
				setState(82);
				match(RPAREN);
				}
				break;
			case WRITE:
				enterOuterAlt(_localctx, 13);
				{
				setState(84);
				match(WRITE);
				}
				break;
			case WRITELN:
				enterOuterAlt(_localctx, 14);
				{
				setState(85);
				match(WRITELN);
				}
				break;
			case LENGHT:
				enterOuterAlt(_localctx, 15);
				{
				setState(86);
				match(LENGHT);
				setState(87);
				match(LPAREN);
				setState(88);
				ident();
				setState(89);
				match(RPAREN);
				}
				break;
			case IF:
				enterOuterAlt(_localctx, 16);
				{
				setState(91);
				match(IF);
				setState(92);
				match(LPAREN);
				setState(93);
				adresse();
				setState(94);
				match(RPAREN);
				}
				break;
			case GOTO:
				enterOuterAlt(_localctx, 17);
				{
				setState(96);
				match(GOTO);
				setState(97);
				match(LPAREN);
				setState(98);
				adresse();
				setState(99);
				match(RPAREN);
				}
				break;
			case INC:
				enterOuterAlt(_localctx, 18);
				{
				setState(101);
				match(INC);
				setState(102);
				match(LPAREN);
				setState(103);
				ident();
				setState(104);
				match(RPAREN);
				}
				break;
			case AINC:
				enterOuterAlt(_localctx, 19);
				{
				setState(106);
				match(AINC);
				setState(107);
				match(LPAREN);
				setState(108);
				ident();
				setState(109);
				match(RPAREN);
				}
				break;
			case NEG:
			case NOT:
			case ADD:
			case SUB:
			case MUL:
			case DIV:
			case CMP:
			case SUP:
			case OR:
			case AND:
				enterOuterAlt(_localctx, 20);
				{
				setState(111);
				oper();
				}
				break;
			case NOP:
				enterOuterAlt(_localctx, 21);
				{
				setState(112);
				match(NOP);
				}
				break;
			case JCSTOP:
				enterOuterAlt(_localctx, 22);
				{
				setState(113);
				match(JCSTOP);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IdentContext extends ParserRuleContext {
		public TerminalNode IDENTIFICATEUR() { return getToken(JajaCodeParser.IDENTIFICATEUR, 0); }
		public IdentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ident; }
	}

	public final IdentContext ident() throws RecognitionException {
		IdentContext _localctx = new IdentContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_ident);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(116);
			match(IDENTIFICATEUR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValeurContext extends ParserRuleContext {
		public TerminalNode NOMBRE() { return getToken(JajaCodeParser.NOMBRE, 0); }
		public TerminalNode TRUE() { return getToken(JajaCodeParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(JajaCodeParser.FALSE, 0); }
		public TerminalNode STRING() { return getToken(JajaCodeParser.STRING, 0); }
		public ValeurContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valeur; }
	}

	public final ValeurContext valeur() throws RecognitionException {
		ValeurContext _localctx = new ValeurContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_valeur);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(119);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 21990233341952L) != 0)) {
				{
				setState(118);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 21990233341952L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AdresseContext extends ParserRuleContext {
		public TerminalNode NOMBRE() { return getToken(JajaCodeParser.NOMBRE, 0); }
		public AdresseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_adresse; }
	}

	public final AdresseContext adresse() throws RecognitionException {
		AdresseContext _localctx = new AdresseContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_adresse);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(121);
			match(NOMBRE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OperContext extends ParserRuleContext {
		public Oper2Context oper2() {
			return getRuleContext(Oper2Context.class,0);
		}
		public Oper1Context oper1() {
			return getRuleContext(Oper1Context.class,0);
		}
		public OperContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_oper; }
	}

	public final OperContext oper() throws RecognitionException {
		OperContext _localctx = new OperContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_oper);
		try {
			setState(125);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ADD:
			case SUB:
			case MUL:
			case DIV:
			case CMP:
			case SUP:
			case OR:
			case AND:
				enterOuterAlt(_localctx, 1);
				{
				setState(123);
				oper2();
				}
				break;
			case NEG:
			case NOT:
				enterOuterAlt(_localctx, 2);
				{
				setState(124);
				oper1();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Oper1Context extends ParserRuleContext {
		public TerminalNode NEG() { return getToken(JajaCodeParser.NEG, 0); }
		public TerminalNode NOT() { return getToken(JajaCodeParser.NOT, 0); }
		public Oper1Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_oper1; }
	}

	public final Oper1Context oper1() throws RecognitionException {
		Oper1Context _localctx = new Oper1Context(_ctx, getState());
		enterRule(_localctx, 14, RULE_oper1);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(127);
			_la = _input.LA(1);
			if ( !(_la==NEG || _la==NOT) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Oper2Context extends ParserRuleContext {
		public TerminalNode ADD() { return getToken(JajaCodeParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(JajaCodeParser.SUB, 0); }
		public TerminalNode MUL() { return getToken(JajaCodeParser.MUL, 0); }
		public TerminalNode DIV() { return getToken(JajaCodeParser.DIV, 0); }
		public TerminalNode CMP() { return getToken(JajaCodeParser.CMP, 0); }
		public TerminalNode SUP() { return getToken(JajaCodeParser.SUP, 0); }
		public TerminalNode OR() { return getToken(JajaCodeParser.OR, 0); }
		public TerminalNode AND() { return getToken(JajaCodeParser.AND, 0); }
		public Oper2Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_oper2; }
	}

	public final Oper2Context oper2() throws RecognitionException {
		Oper2Context _localctx = new Oper2Context(_ctx, getState());
		enterRule(_localctx, 16, RULE_oper2);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(129);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 273804165120L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(JajaCodeParser.INT, 0); }
		public TerminalNode BOOLEAN() { return getToken(JajaCodeParser.BOOLEAN, 0); }
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_type);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(131);
			_la = _input.LA(1);
			if ( !(_la==INT || _la==BOOLEAN) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SorteContext extends ParserRuleContext {
		public TerminalNode VAR() { return getToken(JajaCodeParser.VAR, 0); }
		public TerminalNode METH() { return getToken(JajaCodeParser.METH, 0); }
		public SorteContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sorte; }
	}

	public final SorteContext sorte() throws RecognitionException {
		SorteContext _localctx = new SorteContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_sorte);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(133);
			_la = _input.LA(1);
			if ( !(_la==VAR || _la==METH) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001/\u0088\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0001\u0000\u0004\u0000\u0018"+
		"\b\u0000\u000b\u0000\f\u0000\u0019\u0001\u0000\u0001\u0000\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0003\u0002s\b\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0003\u0004"+
		"x\b\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0003\u0006"+
		"~\b\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001"+
		"\n\u0001\n\u0001\n\u0000\u0000\u000b\u0000\u0002\u0004\u0006\b\n\f\u000e"+
		"\u0010\u0012\u0014\u0000\u0005\u0003\u0000\u0012\u0013**,,\u0001\u0000"+
		"\u001c\u001d\u0001\u0000\u001e%\u0001\u0000\u0014\u0015\u0001\u0000\u001a"+
		"\u001b\u0094\u0000\u0017\u0001\u0000\u0000\u0000\u0002\u001d\u0001\u0000"+
		"\u0000\u0000\u0004r\u0001\u0000\u0000\u0000\u0006t\u0001\u0000\u0000\u0000"+
		"\bw\u0001\u0000\u0000\u0000\ny\u0001\u0000\u0000\u0000\f}\u0001\u0000"+
		"\u0000\u0000\u000e\u007f\u0001\u0000\u0000\u0000\u0010\u0081\u0001\u0000"+
		"\u0000\u0000\u0012\u0083\u0001\u0000\u0000\u0000\u0014\u0085\u0001\u0000"+
		"\u0000\u0000\u0016\u0018\u0003\u0002\u0001\u0000\u0017\u0016\u0001\u0000"+
		"\u0000\u0000\u0018\u0019\u0001\u0000\u0000\u0000\u0019\u0017\u0001\u0000"+
		"\u0000\u0000\u0019\u001a\u0001\u0000\u0000\u0000\u001a\u001b\u0001\u0000"+
		"\u0000\u0000\u001b\u001c\u0005\u0000\u0000\u0001\u001c\u0001\u0001\u0000"+
		"\u0000\u0000\u001d\u001e\u0003\n\u0005\u0000\u001e\u001f\u0003\u0004\u0002"+
		"\u0000\u001f\u0003\u0001\u0000\u0000\u0000 s\u0005\u0002\u0000\u0000!"+
		"s\u0005\u0003\u0000\u0000\"#\u0005\u0005\u0000\u0000#$\u0005(\u0000\u0000"+
		"$%\u0003\u0006\u0003\u0000%&\u0005\'\u0000\u0000&\'\u0003\u0012\t\u0000"+
		"\'(\u0005\'\u0000\u0000()\u0003\u0014\n\u0000)*\u0005\'\u0000\u0000*+"+
		"\u0003\b\u0004\u0000+,\u0005)\u0000\u0000,s\u0001\u0000\u0000\u0000-."+
		"\u0005\u0006\u0000\u0000./\u0005(\u0000\u0000/0\u0003\u0006\u0003\u0000"+
		"01\u0005\'\u0000\u000012\u0003\u0012\t\u000023\u0005)\u0000\u00003s\u0001"+
		"\u0000\u0000\u000045\u0005\u0007\u0000\u000056\u0005(\u0000\u000067\u0003"+
		"\u0006\u0003\u000078\u0005)\u0000\u00008s\u0001\u0000\u0000\u00009s\u0005"+
		"\u0017\u0000\u0000:;\u0005\u0004\u0000\u0000;<\u0005(\u0000\u0000<=\u0003"+
		"\b\u0004\u0000=>\u0005)\u0000\u0000>s\u0001\u0000\u0000\u0000?s\u0005"+
		"\u0016\u0000\u0000@A\u0005\t\u0000\u0000AB\u0005(\u0000\u0000BC\u0003"+
		"\u0006\u0003\u0000CD\u0005)\u0000\u0000Ds\u0001\u0000\u0000\u0000EF\u0005"+
		"\n\u0000\u0000FG\u0005(\u0000\u0000GH\u0003\u0006\u0003\u0000HI\u0005"+
		")\u0000\u0000Is\u0001\u0000\u0000\u0000JK\u0005\u000b\u0000\u0000KL\u0005"+
		"(\u0000\u0000LM\u0003\u0006\u0003\u0000MN\u0005)\u0000\u0000Ns\u0001\u0000"+
		"\u0000\u0000OP\u0005\f\u0000\u0000PQ\u0005(\u0000\u0000QR\u0003\u0006"+
		"\u0003\u0000RS\u0005)\u0000\u0000Ss\u0001\u0000\u0000\u0000Ts\u0005\u0018"+
		"\u0000\u0000Us\u0005\u0019\u0000\u0000VW\u0005\r\u0000\u0000WX\u0005("+
		"\u0000\u0000XY\u0003\u0006\u0003\u0000YZ\u0005)\u0000\u0000Zs\u0001\u0000"+
		"\u0000\u0000[\\\u0005\u0010\u0000\u0000\\]\u0005(\u0000\u0000]^\u0003"+
		"\n\u0005\u0000^_\u0005)\u0000\u0000_s\u0001\u0000\u0000\u0000`a\u0005"+
		"\b\u0000\u0000ab\u0005(\u0000\u0000bc\u0003\n\u0005\u0000cd\u0005)\u0000"+
		"\u0000ds\u0001\u0000\u0000\u0000ef\u0005\u000e\u0000\u0000fg\u0005(\u0000"+
		"\u0000gh\u0003\u0006\u0003\u0000hi\u0005)\u0000\u0000is\u0001\u0000\u0000"+
		"\u0000jk\u0005\u000f\u0000\u0000kl\u0005(\u0000\u0000lm\u0003\u0006\u0003"+
		"\u0000mn\u0005)\u0000\u0000ns\u0001\u0000\u0000\u0000os\u0003\f\u0006"+
		"\u0000ps\u0005&\u0000\u0000qs\u0005\u0011\u0000\u0000r \u0001\u0000\u0000"+
		"\u0000r!\u0001\u0000\u0000\u0000r\"\u0001\u0000\u0000\u0000r-\u0001\u0000"+
		"\u0000\u0000r4\u0001\u0000\u0000\u0000r9\u0001\u0000\u0000\u0000r:\u0001"+
		"\u0000\u0000\u0000r?\u0001\u0000\u0000\u0000r@\u0001\u0000\u0000\u0000"+
		"rE\u0001\u0000\u0000\u0000rJ\u0001\u0000\u0000\u0000rO\u0001\u0000\u0000"+
		"\u0000rT\u0001\u0000\u0000\u0000rU\u0001\u0000\u0000\u0000rV\u0001\u0000"+
		"\u0000\u0000r[\u0001\u0000\u0000\u0000r`\u0001\u0000\u0000\u0000re\u0001"+
		"\u0000\u0000\u0000rj\u0001\u0000\u0000\u0000ro\u0001\u0000\u0000\u0000"+
		"rp\u0001\u0000\u0000\u0000rq\u0001\u0000\u0000\u0000s\u0005\u0001\u0000"+
		"\u0000\u0000tu\u0005+\u0000\u0000u\u0007\u0001\u0000\u0000\u0000vx\u0007"+
		"\u0000\u0000\u0000wv\u0001\u0000\u0000\u0000wx\u0001\u0000\u0000\u0000"+
		"x\t\u0001\u0000\u0000\u0000yz\u0005,\u0000\u0000z\u000b\u0001\u0000\u0000"+
		"\u0000{~\u0003\u0010\b\u0000|~\u0003\u000e\u0007\u0000}{\u0001\u0000\u0000"+
		"\u0000}|\u0001\u0000\u0000\u0000~\r\u0001\u0000\u0000\u0000\u007f\u0080"+
		"\u0007\u0001\u0000\u0000\u0080\u000f\u0001\u0000\u0000\u0000\u0081\u0082"+
		"\u0007\u0002\u0000\u0000\u0082\u0011\u0001\u0000\u0000\u0000\u0083\u0084"+
		"\u0007\u0003\u0000\u0000\u0084\u0013\u0001\u0000\u0000\u0000\u0085\u0086"+
		"\u0007\u0004\u0000\u0000\u0086\u0015\u0001\u0000\u0000\u0000\u0004\u0019"+
		"rw}";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}