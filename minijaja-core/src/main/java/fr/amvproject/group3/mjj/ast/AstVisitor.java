package fr.amvproject.group3.mjj.ast;

import fr.amvproject.group3.mjj.ast.nodes.abstracts.*;
import fr.amvproject.group3.mjj.ast.nodes.decls.*;
import fr.amvproject.group3.mjj.ast.nodes.exprs.BinaryExpNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.CallExpNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.LengthNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.UnaryExpNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.*;
import fr.amvproject.group3.mjj.ast.nodes.instrs.*;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.DeclNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.VarDeclNode;
import fr.amvproject.group3.mjj.ast.nodes.other.BinaryOpNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeMethNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeNode;
import fr.amvproject.group3.mjj.ast.nodes.other.UnaryOpNode;
import fr.amvproject.group3.mjj.ast.nodes.program.ClassNode;
import fr.amvproject.group3.mjj.ast.nodes.program.ProgramNode;

public interface AstVisitor<T> {
    T visitProgram(ProgramNode node);
    T visitClass(ClassNode node);

    T visitDecls(DeclsNode node);
    T visitInstrs(InstrsNode node);
    T visitListExp(ListExpNode node);
    T visitParams(ParamsNode node);
    T visitVars(VarsNode node);

    T visitCst(CstNode node);
    T visitMain(MainNode node);
    T visitMethod(MethodNode node);
    T visitTable(TableNode node);
    T visitVar(VarNode node);

    T visitDecl(DeclNode node);
    T visitVarDecl(VarDeclNode node);

    T visitBinaryExp(BinaryExpNode node);
    T visitCallExp(CallExpNode node);
    T visitLength(LengthNode node);
    T visitUnaryExp(UnaryExpNode node);

    T visitAddAssign(AddAssignNode node);
    T visitAssign(AssignNode node);
    T visitCallInstr(CallInstrNode node);
    T visitIf(IfNode node);
    T visitInc(IncNode node);
    T visitReturn(ReturnNode node);
    T visitWhile(WhileNode node);
    T visitWrite(WriteNode node);

    T visitIdent(IdentNode node);
    T visitArrayAccess(ArrayAccessNode node);
    T visitParam(ParamNode node);
    T visitIntLiteral(IntLiteralNode node);
    T visitBoolLiteral(BoolLiteralNode node);
    T visitStringLiteral(StringLiteralNode node);

    T visitBinaryOp(BinaryOpNode node);
    T visitType(TypeNode node);
    T visitTypeMeth(TypeMethNode node);
    T visitUnaryOp(UnaryOpNode node);
}
