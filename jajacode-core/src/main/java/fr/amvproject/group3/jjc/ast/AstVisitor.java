package fr.amvproject.group3.jjc.ast;

import fr.amvproject.group3.jjc.ast.nodes.finals.*;
import fr.amvproject.group3.jjc.ast.nodes.instrs.*;
import fr.amvproject.group3.jjc.ast.nodes.literals.InitLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.JcstopLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.NopLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.PopLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.ReturnLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.SwapLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.WriteLiteral;
import fr.amvproject.group3.jjc.ast.nodes.other.BinaryOpNode;
import fr.amvproject.group3.jjc.ast.nodes.other.ObjNode;
import fr.amvproject.group3.jjc.ast.nodes.other.TypeNode;
import fr.amvproject.group3.jjc.ast.nodes.other.UnaryOpNode;
import fr.amvproject.group3.jjc.ast.nodes.program.ClassNode;
import fr.amvproject.group3.jjc.ast.nodes.program.ProgramNode;

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
