// Generated from fr/amvproject/group3/JajaCodeParser.g4 by ANTLR 4.13.2
package fr.amvproject.group3;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link JajaCodeParser}.
 */
public interface JajaCodeParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link JajaCodeParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(JajaCodeParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link JajaCodeParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(JajaCodeParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link JajaCodeParser#classe}.
	 * @param ctx the parse tree
	 */
	void enterClasse(JajaCodeParser.ClasseContext ctx);
	/**
	 * Exit a parse tree produced by {@link JajaCodeParser#classe}.
	 * @param ctx the parse tree
	 */
	void exitClasse(JajaCodeParser.ClasseContext ctx);
	/**
	 * Enter a parse tree produced by {@link JajaCodeParser#instr}.
	 * @param ctx the parse tree
	 */
	void enterInstr(JajaCodeParser.InstrContext ctx);
	/**
	 * Exit a parse tree produced by {@link JajaCodeParser#instr}.
	 * @param ctx the parse tree
	 */
	void exitInstr(JajaCodeParser.InstrContext ctx);
	/**
	 * Enter a parse tree produced by {@link JajaCodeParser#ident}.
	 * @param ctx the parse tree
	 */
	void enterIdent(JajaCodeParser.IdentContext ctx);
	/**
	 * Exit a parse tree produced by {@link JajaCodeParser#ident}.
	 * @param ctx the parse tree
	 */
	void exitIdent(JajaCodeParser.IdentContext ctx);
	/**
	 * Enter a parse tree produced by {@link JajaCodeParser#valeur}.
	 * @param ctx the parse tree
	 */
	void enterValeur(JajaCodeParser.ValeurContext ctx);
	/**
	 * Exit a parse tree produced by {@link JajaCodeParser#valeur}.
	 * @param ctx the parse tree
	 */
	void exitValeur(JajaCodeParser.ValeurContext ctx);
	/**
	 * Enter a parse tree produced by {@link JajaCodeParser#adresse}.
	 * @param ctx the parse tree
	 */
	void enterAdresse(JajaCodeParser.AdresseContext ctx);
	/**
	 * Exit a parse tree produced by {@link JajaCodeParser#adresse}.
	 * @param ctx the parse tree
	 */
	void exitAdresse(JajaCodeParser.AdresseContext ctx);
	/**
	 * Enter a parse tree produced by {@link JajaCodeParser#oper}.
	 * @param ctx the parse tree
	 */
	void enterOper(JajaCodeParser.OperContext ctx);
	/**
	 * Exit a parse tree produced by {@link JajaCodeParser#oper}.
	 * @param ctx the parse tree
	 */
	void exitOper(JajaCodeParser.OperContext ctx);
	/**
	 * Enter a parse tree produced by {@link JajaCodeParser#oper1}.
	 * @param ctx the parse tree
	 */
	void enterOper1(JajaCodeParser.Oper1Context ctx);
	/**
	 * Exit a parse tree produced by {@link JajaCodeParser#oper1}.
	 * @param ctx the parse tree
	 */
	void exitOper1(JajaCodeParser.Oper1Context ctx);
	/**
	 * Enter a parse tree produced by {@link JajaCodeParser#oper2}.
	 * @param ctx the parse tree
	 */
	void enterOper2(JajaCodeParser.Oper2Context ctx);
	/**
	 * Exit a parse tree produced by {@link JajaCodeParser#oper2}.
	 * @param ctx the parse tree
	 */
	void exitOper2(JajaCodeParser.Oper2Context ctx);
	/**
	 * Enter a parse tree produced by {@link JajaCodeParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(JajaCodeParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JajaCodeParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(JajaCodeParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JajaCodeParser#sorte}.
	 * @param ctx the parse tree
	 */
	void enterSorte(JajaCodeParser.SorteContext ctx);
	/**
	 * Exit a parse tree produced by {@link JajaCodeParser#sorte}.
	 * @param ctx the parse tree
	 */
	void exitSorte(JajaCodeParser.SorteContext ctx);
}