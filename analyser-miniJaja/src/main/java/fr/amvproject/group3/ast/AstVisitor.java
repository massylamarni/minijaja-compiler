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
    T visitProgram(ProgramNode node);
    T visitClasse(ClasseNode node);
    T visitDecls(DeclsNode node);
    T visitDecl(DeclNode node);
    T visitVars(VarsNode node);
    T visitVar(VarNode node);

    T visitMethode(MethodeNode node);
    T visitMethMain(MethMainNode node);
    T visitEntetes(EntetesNode node);
    T visitEntete(EnteteNode node);
    T visitInstrs(InstrsNode node);
    T visitInstr(InstrNode node);

    T visitListExp(ListExpNode node);
    T visitBinaryExp(BinaryExpNode node);
    T visitUnaryExp(UnaryExpNode node);
    T visitAssign(AssignNode node);
    T visitAddAssign(AddAssignNode node);
    T visitInc(IncNode node);
    T visitLength(LengthNode node);
    T visitIf(IfNode node);
    T visitWhile(WhileNode node);
    T visitWrite(WriteNode node);
    T visitRetour(RetourNode node);
    T visitIntLiteral(IntLiteralNode node);
    T visitBoolLiteral(BoolLiteralNode node);
    T visitStringLiteral(StringLiteralNode node);

    T visitVarRef(VarRefNode node);
    T visitArrayAccess(ArrayAccessNode node);
    T visitMethodCallExp(MethodCallExpNode node);
    T visitMethodCallInstr(MethodCallInstrNode node);

    T visitType(TypeNode node);
    T visitBinaryOp(BinaryOpNode node);
    T visitUnaryOp(UnaryOpNode node);
    T visitIdent(Ident node);


}
