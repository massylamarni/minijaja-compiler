package fr.amvproject.group3.ast;

import fr.amvproject.group3.ast.nodes.finals.*;
import fr.amvproject.group3.ast.nodes.instrs.*;
import fr.amvproject.group3.ast.nodes.literals.InitLiteral;
import fr.amvproject.group3.ast.nodes.literals.JcstopLiteral;
import fr.amvproject.group3.ast.nodes.literals.NopLiteral;
import fr.amvproject.group3.ast.nodes.literals.PopLiteral;
import fr.amvproject.group3.ast.nodes.literals.ReturnLiteral;
import fr.amvproject.group3.ast.nodes.literals.SwapLiteral;
import fr.amvproject.group3.ast.nodes.literals.WriteLiteral;
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

    T visitInit(InitLiteral literal);
    T visitJcstop(JcstopLiteral literal);
    T visitNop(NopLiteral literal);
    T visitPop(PopLiteral literal);
    T visitReturn(ReturnLiteral literal);
    T visitSwap(SwapLiteral literal);
    T visitWrite(WriteLiteral literal);

    T visitObj(ObjNode node);
    T visitType(TypeNode node);
    T visitUnaryOp(UnaryOpNode node);
    T visitBinaryOp(BinaryOpNode node);
}
