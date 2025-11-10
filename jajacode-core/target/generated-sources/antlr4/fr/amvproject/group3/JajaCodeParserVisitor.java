// Generated from fr/amvproject/group3/JajaCodeParser.g4 by ANTLR 4.13.2
package fr.amvproject.group3;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link JajaCodeParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface JajaCodeParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link JajaCodeParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(JajaCodeParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link JajaCodeParser#classe}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClasse(JajaCodeParser.ClasseContext ctx);
	/**
	 * Visit a parse tree produced by {@link JajaCodeParser#instr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstr(JajaCodeParser.InstrContext ctx);
	/**
	 * Visit a parse tree produced by {@link JajaCodeParser#ident}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdent(JajaCodeParser.IdentContext ctx);
	/**
	 * Visit a parse tree produced by {@link JajaCodeParser#valeur}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValeur(JajaCodeParser.ValeurContext ctx);
	/**
	 * Visit a parse tree produced by {@link JajaCodeParser#adresse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdresse(JajaCodeParser.AdresseContext ctx);
	/**
	 * Visit a parse tree produced by {@link JajaCodeParser#oper}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOper(JajaCodeParser.OperContext ctx);
	/**
	 * Visit a parse tree produced by {@link JajaCodeParser#oper1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOper1(JajaCodeParser.Oper1Context ctx);
	/**
	 * Visit a parse tree produced by {@link JajaCodeParser#oper2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOper2(JajaCodeParser.Oper2Context ctx);
	/**
	 * Visit a parse tree produced by {@link JajaCodeParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(JajaCodeParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JajaCodeParser#sorte}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSorte(JajaCodeParser.SorteContext ctx);
}