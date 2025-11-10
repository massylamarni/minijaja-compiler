// Generated from fr/amvproject/group3/MiniJajaParser.g4 by ANTLR 4.13.2
package fr.amvproject.group3;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MiniJajaParser}.
 */
public interface MiniJajaParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(MiniJajaParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(MiniJajaParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#classe}.
	 * @param ctx the parse tree
	 */
	void enterClasse(MiniJajaParser.ClasseContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#classe}.
	 * @param ctx the parse tree
	 */
	void exitClasse(MiniJajaParser.ClasseContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#ident}.
	 * @param ctx the parse tree
	 */
	void enterIdent(MiniJajaParser.IdentContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#ident}.
	 * @param ctx the parse tree
	 */
	void exitIdent(MiniJajaParser.IdentContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#decls}.
	 * @param ctx the parse tree
	 */
	void enterDecls(MiniJajaParser.DeclsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#decls}.
	 * @param ctx the parse tree
	 */
	void exitDecls(MiniJajaParser.DeclsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#decl}.
	 * @param ctx the parse tree
	 */
	void enterDecl(MiniJajaParser.DeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#decl}.
	 * @param ctx the parse tree
	 */
	void exitDecl(MiniJajaParser.DeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#vars}.
	 * @param ctx the parse tree
	 */
	void enterVars(MiniJajaParser.VarsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#vars}.
	 * @param ctx the parse tree
	 */
	void exitVars(MiniJajaParser.VarsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#var}.
	 * @param ctx the parse tree
	 */
	void enterVar(MiniJajaParser.VarContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#var}.
	 * @param ctx the parse tree
	 */
	void exitVar(MiniJajaParser.VarContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#vexp}.
	 * @param ctx the parse tree
	 */
	void enterVexp(MiniJajaParser.VexpContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#vexp}.
	 * @param ctx the parse tree
	 */
	void exitVexp(MiniJajaParser.VexpContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#methode}.
	 * @param ctx the parse tree
	 */
	void enterMethode(MiniJajaParser.MethodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#methode}.
	 * @param ctx the parse tree
	 */
	void exitMethode(MiniJajaParser.MethodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#methmain}.
	 * @param ctx the parse tree
	 */
	void enterMethmain(MiniJajaParser.MethmainContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#methmain}.
	 * @param ctx the parse tree
	 */
	void exitMethmain(MiniJajaParser.MethmainContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#entetes}.
	 * @param ctx the parse tree
	 */
	void enterEntetes(MiniJajaParser.EntetesContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#entetes}.
	 * @param ctx the parse tree
	 */
	void exitEntetes(MiniJajaParser.EntetesContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#entete}.
	 * @param ctx the parse tree
	 */
	void enterEntete(MiniJajaParser.EnteteContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#entete}.
	 * @param ctx the parse tree
	 */
	void exitEntete(MiniJajaParser.EnteteContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#instrs}.
	 * @param ctx the parse tree
	 */
	void enterInstrs(MiniJajaParser.InstrsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#instrs}.
	 * @param ctx the parse tree
	 */
	void exitInstrs(MiniJajaParser.InstrsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#instr}.
	 * @param ctx the parse tree
	 */
	void enterInstr(MiniJajaParser.InstrContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#instr}.
	 * @param ctx the parse tree
	 */
	void exitInstr(MiniJajaParser.InstrContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#listexp}.
	 * @param ctx the parse tree
	 */
	void enterListexp(MiniJajaParser.ListexpContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#listexp}.
	 * @param ctx the parse tree
	 */
	void exitListexp(MiniJajaParser.ListexpContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterExp(MiniJajaParser.ExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitExp(MiniJajaParser.ExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#expOr}.
	 * @param ctx the parse tree
	 */
	void enterExpOr(MiniJajaParser.ExpOrContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#expOr}.
	 * @param ctx the parse tree
	 */
	void exitExpOr(MiniJajaParser.ExpOrContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#expAnd}.
	 * @param ctx the parse tree
	 */
	void enterExpAnd(MiniJajaParser.ExpAndContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#expAnd}.
	 * @param ctx the parse tree
	 */
	void exitExpAnd(MiniJajaParser.ExpAndContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#expEq}.
	 * @param ctx the parse tree
	 */
	void enterExpEq(MiniJajaParser.ExpEqContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#expEq}.
	 * @param ctx the parse tree
	 */
	void exitExpEq(MiniJajaParser.ExpEqContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#expRel}.
	 * @param ctx the parse tree
	 */
	void enterExpRel(MiniJajaParser.ExpRelContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#expRel}.
	 * @param ctx the parse tree
	 */
	void exitExpRel(MiniJajaParser.ExpRelContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#expAdd}.
	 * @param ctx the parse tree
	 */
	void enterExpAdd(MiniJajaParser.ExpAddContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#expAdd}.
	 * @param ctx the parse tree
	 */
	void exitExpAdd(MiniJajaParser.ExpAddContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#expMul}.
	 * @param ctx the parse tree
	 */
	void enterExpMul(MiniJajaParser.ExpMulContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#expMul}.
	 * @param ctx the parse tree
	 */
	void exitExpMul(MiniJajaParser.ExpMulContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#expUnary}.
	 * @param ctx the parse tree
	 */
	void enterExpUnary(MiniJajaParser.ExpUnaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#expUnary}.
	 * @param ctx the parse tree
	 */
	void exitExpUnary(MiniJajaParser.ExpUnaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#fact}.
	 * @param ctx the parse tree
	 */
	void enterFact(MiniJajaParser.FactContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#fact}.
	 * @param ctx the parse tree
	 */
	void exitFact(MiniJajaParser.FactContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#ident1}.
	 * @param ctx the parse tree
	 */
	void enterIdent1(MiniJajaParser.Ident1Context ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#ident1}.
	 * @param ctx the parse tree
	 */
	void exitIdent1(MiniJajaParser.Ident1Context ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#typemeth}.
	 * @param ctx the parse tree
	 */
	void enterTypemeth(MiniJajaParser.TypemethContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#typemeth}.
	 * @param ctx the parse tree
	 */
	void exitTypemeth(MiniJajaParser.TypemethContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJajaParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(MiniJajaParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJajaParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(MiniJajaParser.TypeContext ctx);
}