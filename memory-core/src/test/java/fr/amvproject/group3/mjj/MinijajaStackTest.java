package fr.amvproject.group3.mjj;

import fr.amvproject.group3.mjj.Quad;
import fr.amvproject.group3.mjj.Stack;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.InstrsNode;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.ListExpNode;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.ParamsNode;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.VarsNode;
import fr.amvproject.group3.mjj.ast.nodes.decls.MethodNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.ParamNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeNode;
import fr.amvproject.group3.mjj.utils.NodeOperations.MockToken;
import fr.amvproject.group3.nodes.ObjNode;

import org.junit.Ignore;
import org.junit.Test;

import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeMethNode;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MinijajaStackTest {

    @Test
    public void givenStack__whenSorteOfLast__thenSameSizeQueueSameOrder() {
        // Given
        MockToken identToken1 = new MockToken("Test", 1, 0);
        MockToken identToken2 = new MockToken("x", 2, 0);
        MockToken identToken3 = new MockToken("y", 3, 0);
        MockToken identToken4 = new MockToken("Test", 2, 0);
        MockToken typeMethToken1 = new MockToken("void", 0, 0);
        MockToken typeMethToken2 = new MockToken("int", 0, 0);

        Stack stack = new Stack();
        stack.push(new Quad(new IdentNode(identToken1), null, new ObjNode("var"), new TypeMethNode(typeMethToken1)));
        stack.push(new Quad(new IdentNode(identToken2), 2, new ObjNode("var"), new TypeMethNode(typeMethToken2)));
        stack.push(new Quad(new IdentNode(identToken3), 3, new ObjNode("var"), new TypeMethNode(typeMethToken2)));

        // When
        TypeMethNode res = (TypeMethNode) stack.sorte(new IdentNode(identToken4));
        // Then
        assertTrue(res.isVoid());
        assertThat(stack.getQueue().size(), is(3));
        assertThat(stack.pop().getIdent().getName(), is("y"));
        assertThat(stack.pop().getIdent().getName(), is("x"));
        assertThat(stack.pop().getIdent().getName(), is("Test"));
    }

    @Test
    public void givenStack__whenValOfX__thenSameSizeQueueSameOrder() {
        // Given
        Stack stack = new Stack();
        stack.push(new Quad(new IdentNode(new MockToken("Test",1,0)), null, new ObjNode("var"), new TypeMethNode(new MockToken("void",1,0))));
        stack.push(new Quad(new IdentNode(new MockToken("x",2,0)), 2, new ObjNode("var"), new TypeMethNode(new MockToken("int",1,0))));
        stack.push(new Quad(new IdentNode(new MockToken("y",3,0)), 3, new ObjNode("var"), new TypeMethNode(new MockToken("int",1,0))));

        // When
        Object res = stack.val(new IdentNode(new MockToken("x", 2,0)));
        // Then
        assertThat(res instanceof Integer, is(true));
        assertThat(stack.getQueue().size(), is(3));
        assertThat(stack.pop().getIdent().getName(), is("y"));
        assertThat(stack.pop().getIdent().getName(), is("x"));
        assertThat(stack.pop().getIdent().getName(), is("Test"));
    }

    @Test
    public void givenStack__whenMultipleSort__thenSameSizeQueueSameOrder() {
        // Given
        Stack stack = new Stack();
        stack.push(new Quad(new IdentNode(new MockToken("Test",1,0)), null, new ObjNode("var"),  new TypeMethNode(new MockToken("void",1,0))));
        stack.push(new Quad(new IdentNode(new MockToken("x",2,0)), 2, new ObjNode("var"), new TypeMethNode(new MockToken("int",1,0))));
        stack.push(new Quad(new IdentNode(new MockToken("y",3,0)), 3, new ObjNode("var"), new TypeMethNode(new MockToken("int",1,0))));

        // When
        TypeMethNode resX = (TypeMethNode) stack.sorte(new IdentNode(new MockToken("x",2,0)));
        TypeMethNode resY = (TypeMethNode) stack.sorte(new IdentNode(new MockToken("y",3,0)));
        // Then

        assertTrue(resX.isInt());
        assertTrue(resY.isInt());
        assertThat(stack.getQueue().size(), is(3));
        assertThat(stack.pop().getIdent().getName(), is("y"));
        assertThat(stack.pop().getIdent().getName(), is("x"));
        assertThat(stack.pop().getIdent().getName(), is("Test"));
    }


    @Test
    public void givenStack__whenDeclVar__thenVarIsDeclOnTop() {
        // Given
        Stack stack = new Stack();
        stack.push(new Quad(new IdentNode(new MockToken("Test",1,0)), null, new ObjNode("var"),  new TypeMethNode(new MockToken("void",1,0))));
        IdentNode ident = new IdentNode(new MockToken("z", 4,0));
        // When
        stack.declVar(ident, 4, new TypeMethNode(new MockToken("int",1,0)));
        // Then
        assertThat( stack.getQueue().size(),is(2));
        assertThat(stack.peek().getIdent().getName(),is("z"));
        assertTrue(stack.peek().getObj().isVar());
        assertTrue(((TypeMethNode) stack.peek().getType()).isInt());
    }

    @Test
    public void givenStack__whenDeclCst__thenVarIsDeclOnTop() {
        // Given
        Stack stack = new Stack();
        stack.push(new Quad(new IdentNode(new MockToken("Test",1,0)), null, new ObjNode("var"),  new TypeMethNode(new MockToken("void",1,0))));
        IdentNode ident = new IdentNode(new MockToken("T", 5,0));
        // When
        stack.declCst(ident, 4, new TypeNode(new MockToken("int",1,0)));
        // Then
        assertThat( stack.getQueue().size(),is(2));
        assertThat(stack.peek().getIdent().getName(),is("T"));
        assertTrue(stack.peek().getObj().isCst());
    }

    @Test
    public void givenStack__whenDeclTab__thenTabIsDeclOnTop() {
        // Given
        Stack stack = new Stack();
        stack.push(new Quad(new IdentNode(new MockToken("Test",1,0)), null, new ObjNode("var"),  new TypeMethNode(new MockToken("void",1,0))));

        IdentNode ident = new IdentNode(new MockToken("arr", 18,0));
        // When
        stack.declTab(ident, 5, new TypeMethNode(new MockToken("int",1,0)));
        // Then
        assertThat( stack.getQueue().size(),is(2));
        assertThat(stack.peek().getIdent().getName(),is("arr"));
        assertTrue(stack.peek().getObj().isTab());
        assertTrue(((TypeMethNode) stack.peek().getType()).isInt());
        assertThat(stack.peek().getValue(),is(1));

    }

    @Test
    public void givenStack__whenAffecterVal__thenValueIsUpdated() {
        // Given
        Stack stack = new Stack();
        IdentNode ident = new IdentNode(new MockToken("x",2,0));
        stack.push(new Quad(new IdentNode(new MockToken("x",2,0)), 2, new ObjNode("var"), new TypeMethNode(new MockToken("int",1,0))));
        stack.push(new Quad(new IdentNode(new MockToken("x",2,0)), 3, new ObjNode("var"), new TypeMethNode(new MockToken("int",1,0))));

        // When
        stack.affecterVal(ident, 5);
        // Then
        assertThat(stack.pop().getValue(),is(5));
    }

    @Test
    public void givenStack__whenValT__thenArrayValueIsReturned() {
        // Given
        Stack stack = new Stack();
        IdentNode ident = new IdentNode(new MockToken("arr", 6,0));
        stack.declTab(ident, 5, new TypeMethNode(new MockToken("int",1,0)));
        stack.affecterValT(ident, 2, 10);
        // When
        Object val = stack.valT(ident, 2);
        // Then
        assertThat(val,is(10));
    }

    @Test
    public void givenStack__whenAffecterValT__thenArrayValueIsUpdated() {
        // Given
        Stack stack = new Stack();
        IdentNode ident = new IdentNode(new MockToken("arr", 6,0));
        stack.declTab(ident, 5, new TypeMethNode(new MockToken("int",1,0)));
        // When
        stack.affecterValT(ident, 2, 10);
        assertThat(stack.valT(ident,2),is(10));
    }


    @Test
    public void givenStack__whenLongT__thenArrayLengthIsReturned() {
        // Given
        Stack stack = new Stack();
        IdentNode ident = new IdentNode(new MockToken("arr", 6,0));
        stack.declTab(ident, 5, new TypeMethNode(new MockToken("int",1,0)));
        // When
        Integer length = stack.longT(ident);
        // Then
        assertEquals(5, length.intValue());
    }

    @Test
    public void givenStack__whenRetirerDecl__thenDeclarationIsRemoved() {
        // Given
        Stack stack = new Stack();
        IdentNode ident = new IdentNode(new MockToken("x",2,0));
        stack.declVar(ident, 2, new TypeMethNode(new MockToken("int",1,0)));
        // When
        stack.retirerDecl(ident);
        // Then
        assertThat( stack.getQueue().size(),is(0));
    }

    @Test
    public void givenStack__whenSwap__thenTopTwoElementsAreSwapped() {
        // Given
        Stack stack = new Stack();
        stack.push(new Quad(new IdentNode(new MockToken("x",2,0)), 2, new ObjNode("var"), new TypeMethNode(new MockToken("int",1,0))));
        stack.push(new Quad(new IdentNode(new MockToken("y",3,0)), 3, new ObjNode("var"), new TypeMethNode(new MockToken("int",1,0))));
        // When
        stack.swap();
        // Then
        assertThat(stack.pop().getIdent().getName(),is("x"));
        assertThat(stack.pop().getIdent().getName(),is("y"));
    }

    public void setUpMethod() {

    }

    @Test
    public void givenStack__whenDeclMeth__thenMethIsOnTop() {
        // Given
        Stack stack = new Stack();
        IdentNode identMeth = new IdentNode(new MockToken("maMethode", 1,0));
        TypeMethNode returnType = new TypeMethNode(new MockToken("int",1,0));

        List<ParamNode> paramList = new ArrayList<>();
        paramList.add(new ParamNode(new TypeNode(new MockToken("int",1,0)),new IdentNode(new MockToken("param1", 2,0))));
        paramList.add(new ParamNode( new TypeNode(new MockToken("boolean",1,0)),new IdentNode(new MockToken("param2", 3,0))));
        ParamsNode params = new ParamsNode(paramList);
        // When
        stack.declMeth(new MethodNode(returnType, identMeth, params, null, null));
        // Then
        assertThat(stack.getQueue().size(),is(1));
        assertThat(stack.peek().getIdent().getName(),is("maMethode"));
        assertTrue(stack.peek().getObj().isMeth());
        assertTrue(((TypeMethNode) stack.peek().getType()).isInt());
    }

    @Test
    public void givenStack__whenIdentVal__thenValueIsReplacedAtDepth() {
        // Given
        Stack stack = new Stack();
        stack.declVar(new IdentNode(new MockToken("x",2,0)), 2, new TypeMethNode(new MockToken("int",1,0)));
        stack.declVar(new IdentNode(new MockToken("y",3,0)), 3, new TypeMethNode(new MockToken("int",1,0)));
        // When
        stack.identVal(new IdentNode(new MockToken("x",2,0)), new TypeMethNode(new MockToken("int",1,0)), 1, 1);
        // Then
        assertThat(stack.getQueue().size(), is(2));
        assertThat(stack.pop().getIdent().getName(), is("y"));
        assertThat(stack.pop().getIdent().getName(), is("x"));
    }



    @Test
    public void givenStack__whenAffecterType__thenTypeIsUpdated() {
        Stack stack = new Stack();
        // Given
        stack.declVar(new IdentNode(new MockToken("x",2,0)), 2, new TypeMethNode(new MockToken("int",1,0)));
        // When
        stack.affecterType(new IdentNode(new MockToken("x",2,0)), new TypeMethNode(new MockToken("boolean",1,0)));
        // Then
        assertTrue(((TypeMethNode) stack.peek().getType()).isBoolean());
    }

    @Ignore // TODO
    public void givenStack__whenExpParam__thenParamsAreDeclared() {
        // Given
        Stack stack = new Stack();

        List<ParamNode> paramList = new ArrayList<>();
        paramList.add(new ParamNode(new TypeNode(new MockToken("int",1,0)), new IdentNode(new MockToken("param1", 2,0))));
        paramList.add(new ParamNode(new TypeNode(new MockToken("boolean",1,0)), new IdentNode(new MockToken("param2", 3,0))));
        ParamsNode params = new ParamsNode(paramList);

        List<ExpNode> expList = new ArrayList<>();

        ListExpNode listExpNode = new ListExpNode(expList);

        // When
        stack.expParam(listExpNode, params);

        // Then
        assertThat(stack.getQueue().size(), is(2));
        assertThat(stack.peek().getIdent().getName(), is("param2"));
    }


    @Test
    public void givenStack__whenObjet__thenObjTypeIsReturned() {
        // Given
        Stack stack = new Stack();
        stack.declVar(new IdentNode(new MockToken("x",2,0)), 2, new TypeMethNode(new MockToken("int",1,0)));
        // When
        ObjNode obj = stack.objet(new IdentNode(new MockToken("x",2,0)));
        // Then
        assertTrue(obj.isVar());
    }

    @Test
    public void givenStack__whenParametre__thenParamsAreReturned() {
        // Given
        Stack stack = new Stack();
        IdentNode identMeth = new IdentNode(new MockToken("maMethode", 1,0));
        TypeMethNode returnType = new TypeMethNode(new MockToken("int",1,0));

        List<ParamNode> paramList = new ArrayList<>();
        paramList.add(new ParamNode(new TypeNode(new MockToken("int",1,0)),new IdentNode(new MockToken("param1", 2,0))));
        paramList.add(new ParamNode( new TypeNode(new MockToken("boolean",1,0)),new IdentNode(new MockToken("param2", 3,0))));
        ParamsNode params = new ParamsNode(paramList);
        stack.declMeth(new MethodNode(returnType, identMeth, params, null, null));
        // When
        ParamsNode resultParams = stack.parametre(identMeth);
        // Then
        assertThat(resultParams.getList().size(),is(2));
    }

    @Test
    public void givenStack__whenDeclaration__thenVarsAreReturned() {
        // Given
        Stack stack = new Stack();
        IdentNode identMeth = new IdentNode(new MockToken("maMethode", 1,0));
        TypeMethNode returnType = new TypeMethNode(new MockToken("int",1,0));

        List<ParamNode> paramList = new ArrayList<>();
        paramList.add(new ParamNode(new TypeNode(new MockToken("int",1,0)),new IdentNode(new MockToken("param1", 2,0))));
        paramList.add(new ParamNode( new TypeNode(new MockToken("boolean",1,0)),new IdentNode(new MockToken("param2", 3,0))));
        ParamsNode params = new ParamsNode(paramList);
        stack.declMeth(new MethodNode(returnType, identMeth, params, new VarsNode(null), null));
        // When
        VarsNode resultVars = stack.declaration(identMeth);
        // Then
        assertThat(resultVars,notNullValue());
    }

    @Test
    public void givenStack__whenCorps__thenInstrsAreReturned() {
        // Given
        Stack stack = new Stack();
        IdentNode identMeth = new IdentNode(new MockToken("maMethode", 1,0));
        TypeMethNode returnType = new TypeMethNode(new MockToken("int",1,0));

        List<ParamNode> paramList = new ArrayList<>();
        paramList.add(new ParamNode(new TypeNode(new MockToken("int",1,0)),new IdentNode(new MockToken("param1", 2,0))));
        paramList.add(new ParamNode( new TypeNode(new MockToken("boolean",1,0)),new IdentNode(new MockToken("param2", 3,0))));
        ParamsNode params = new ParamsNode(paramList);

        stack.declMeth(new MethodNode(returnType, identMeth, params, null, new InstrsNode(null)));
        // When
        InstrsNode resultInstrs = stack.corps(identMeth);
        // Then
        assertThat(resultInstrs,is(notNullValue()));
    }





}
