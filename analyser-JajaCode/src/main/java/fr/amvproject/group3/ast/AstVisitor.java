package fr.amvproject.group3.ast;

import fr.amvproject.group3.ast.nodes.finals.*;
import fr.amvproject.group3.ast.nodes.instrs.*;
import fr.amvproject.group3.ast.nodes.other.*;
import fr.amvproject.group3.ast.nodes.program.*;

public interface AstVisitor<T> {
    T visitProgram(ProgramNode node);
    T visitClass(ClassNode node);

    T visitGoto(GotoNode node);
    T visitIf(IfNode node);
    T visitInc(IncNode node);
    T visitInvoke(InvokeNode node);
    T visitLength(LengthNode node);
    T visitLoad(LoadNode node);
    T visitNewArray(NewArrayNode node);
    T visitNew(NewNode node);
    T visitPush(PushNode node);
    T visitStore(StoreNode node);

    T visitAddress(AddressNode node);
    T visitIdent(IdentNode node);
    T visitIntLiteral(IntLiteralNode node);
    T visitBoolLiteral(BoolLiteralNode node);
    T visitStringLiteral(StringLiteralNode node);

    T visitObj(ObjNode node);
    T visitType(TypeNode node);
    T visitUnaryOp(UnaryOpNode node);
    T visitBinaryOp(BinaryOpNode node);
}
