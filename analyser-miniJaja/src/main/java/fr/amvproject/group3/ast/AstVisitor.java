package fr.amvproject.group3.ast;

import fr.amvproject.group3.ast.nodes.abstracts.*;
import fr.amvproject.group3.ast.nodes.decls.*;
import fr.amvproject.group3.ast.nodes.exprs.*;
import fr.amvproject.group3.ast.nodes.instrs.*;
import fr.amvproject.group3.ast.nodes.other.BinaryOpNode;
import fr.amvproject.group3.ast.nodes.other.TypeNode;
import fr.amvproject.group3.ast.nodes.other.UnaryOpNode;
import fr.amvproject.group3.ast.nodes.params.*;
import fr.amvproject.group3.ast.nodes.program.*;

public interface AstVisitor<T> {
    // --------- Abstracts ---------
    T visitIdent(Ident node);
    T visitIdentExp(IdentExp node);

    // --------- Decls ---------
    T visitMethMain(MethMainNode node);
    T visitMethode(MethodeNode node);
    T visitVar(VarNode node);
    T visitVars(VarsNode node);

    // --------- Exprs ---------
    T visitArrayAccess(ArrayAccessNode node);
    T visitBinaryExp(BinaryExpNode node);
    T visitBoolLiteral(BoolLiteralNode node);
    T visitIntLiteral(IntLiteralNode node);
    T visitLength(LengthNode node);
    T visitListExp(ListExpNode node);
    T visitMethodCallExp(MethodCallExpNode node);
    T visitStringLiteral(StringLiteralNode node);
    T visitUnaryExp(UnaryExpNode node);
    T visitVarRef(VarRefNode node);

    // --------- Instrs ---------
    T visitAddAssign(AddAssignNode node);
    T visitAssign(AssignNode node);
    T visitIf(IfNode node);
    T visitInc(IncNode node);
    T visitInstrs(InstrsNode node);
    T visitMethodCallInstr(MethodCallInstrNode node);
    T visitRetour(RetourNode node);
    T visitWhile(WhileNode node);
    T visitWrite(WriteNode node);

    // --------- Params ---------
    T visitEntete(EnteteNode node);
    T visitEntetes(EntetesNode node);

    // --------- Program ---------
    T visitClasse(ClasseNode node);
    T visitDecl(DeclNode node);
    T visitDecls(DeclsNode node);

    // --------- Other ---------
    T visitType(TypeNode node);
    T visitBinaryOp(BinaryOpNode node);
    T visitUnaryOp(UnaryOpNode node);

}
