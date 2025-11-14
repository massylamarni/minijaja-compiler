package fr.amvproject.group3;

import fr.amvproject.group3.mjj.Quad;
import fr.amvproject.group3.mjj.Stack;
import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.InstrsNode;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.ListExpNode;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.ParamsNode;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.VarsNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.ParamNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeNode;
import fr.amvproject.group3.nodes.ObjNode;

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

public class StackTest {



    @Test
    public void givenStack__whenSorteOfLast__thenSameSizeQueueSameOrder() {
        // Given
        Stack stack = new Stack();
        stack.getQueue().push(new Quad(new IdentNode("Test",1), null, new ObjNode("var"), new TypeMethNode("void")));
        stack.getQueue().push(new Quad(new IdentNode("x",2), 2, new ObjNode("var"), new TypeMethNode("int")));
        stack.getQueue().push(new Quad(new IdentNode("y",3), 3, new ObjNode("var"), new TypeMethNode("int")));

        // When
        TypeMethNode res = (TypeMethNode) stack.sorte(new IdentNode("Test", 2), stack.pop());
        // Then
        assertTrue(res.isVoid());
        assertThat(stack.getQueue().size(), is(3));
        assertThat(stack.getQueue().pop().getIdent().getName(), is("y"));
        assertThat(stack.getQueue().pop().getIdent().getName(), is("x"));
        assertThat(stack.getQueue().pop().getIdent().getName(), is("Test"));
    }

    @Test
    public void givenStack__whenValOfX__thenSameSizeQueueSameOrder() {
        // Given
        Stack stack = new Stack();
        stack.getQueue().push(new Quad(new IdentNode("Test",1), null, new ObjNode("var"), new TypeMethNode("void")));
        stack.getQueue().push(new Quad(new IdentNode("x",2), 2, new ObjNode("var"), new TypeMethNode("int")));
        stack.getQueue().push(new Quad(new IdentNode("y",3), 3, new ObjNode("var"), new TypeMethNode("int")));

        // When
        Object res = stack.val(new IdentNode("x", 2), stack.pop());
        // Then
        assertThat(res instanceof Integer, is(true));
        assertThat(stack.getQueue().size(), is(3));
        assertThat(stack.getQueue().pop().getIdent().getName(), is("y"));
        assertThat(stack.getQueue().pop().getIdent().getName(), is("x"));
        assertThat(stack.getQueue().pop().getIdent().getName(), is("Test"));
    }

    @Test
    public void givenStack__whenMultipleSort__thenSameSizeQueueSameOrder() {
        // Given
        Stack stack = new Stack();
        stack.getQueue().push(new Quad(new IdentNode("Test",1), null, new ObjNode("var"), new TypeMethNode("void")));
        stack.getQueue().push(new Quad(new IdentNode("x",2), 2, new ObjNode("var"), new TypeMethNode("int")));
        stack.getQueue().push(new Quad(new IdentNode("y",3), 3, new ObjNode("var"), new TypeMethNode("int")));

        // When
        TypeMethNode resX = (TypeMethNode) stack.sorte(new IdentNode("x", 2), stack.pop());
        TypeMethNode resY = (TypeMethNode) stack.sorte(new IdentNode("y", 3), stack.pop());
        // Then

        assertTrue(resX.isInt());
        assertTrue(resY.isInt());
        assertThat(stack.getQueue().size(), is(3));
        assertThat(stack.getQueue().pop().getIdent().getName(), is("y"));
        assertThat(stack.getQueue().pop().getIdent().getName(), is("x"));
        assertThat(stack.getQueue().pop().getIdent().getName(), is("Test"));
    }


    @Test
    public void givenStack__whenDeclVar__thenVarIsDeclOnTop() {
        // Given
        Stack stack = new Stack();
        stack.getQueue().push(new Quad(new IdentNode("Test",1), null, new ObjNode("var"), new TypeMethNode("void")));
        IdentNode ident = new IdentNode("z", 4);
        // When
        stack.declVar(ident, 4, new TypeMethNode("int"));
        // Then
        assertThat( stack.getQueue().size(),is(2));
        assertThat(stack.getQueue().peek().getIdent().getName(),is("z"));
        assertTrue(stack.getQueue().peek().getObj().isVar());
        assertTrue(((TypeMethNode) stack.getQueue().peek().getType()).isInt());
    }

    @Test
    public void givenStack__whenDeclCst__thenVarIsDeclOnTop() {
        // Given
        Stack stack = new Stack();
        stack.getQueue().push(new Quad(new IdentNode("Test",1), null, new ObjNode("var"), new TypeMethNode("void")));
        IdentNode ident = new IdentNode("T", 5);
        // When
        stack.declCst(ident, 4, new TypeNode("int"));
        // Then
        assertThat( stack.getQueue().size(),is(2));
        assertThat(stack.getQueue().peek().getIdent().getName(),is("T"));
        assertTrue(stack.getQueue().peek().getObj().isCst());
    }

    @Test
    public void givenStack__whenDeclTab__thenTabIsDeclOnTop() {
        // Given
        Stack stack = new Stack();
        stack.getQueue().push(new Quad(new IdentNode("Test",1), null, new ObjNode("var"), new TypeMethNode("void")));

        IdentNode ident = new IdentNode("arr", 18);
        // When
        stack.declTab(ident, 5, new TypeMethNode("int"));
        // Then
        assertThat( stack.getQueue().size(),is(2));
        assertThat(stack.getQueue().peek().getIdent().getName(),is("arr"));
        assertTrue(stack.getQueue().peek().getObj().isTab());
        assertTrue(((TypeMethNode) stack.getQueue().peek().getType()).isInt());
        assertThat(stack.getQueue().peek().getValue(),is(1));

    }

    @Test
    public void givenStack__whenAffecterVal__thenValueIsUpdated() {
        // Given
        Stack stack = new Stack();
        IdentNode ident = new IdentNode("x", 2);
        stack.getQueue().push(new Quad(new IdentNode("x",2), 2, new ObjNode("var"), new TypeMethNode("int")));
        stack.getQueue().push(new Quad(new IdentNode("x",2), 3, new ObjNode("var"), new TypeMethNode("int")));

        // When
        stack.affecterVal(ident, 5, stack.pop());
        // Then
        assertThat(stack.getQueue().pop().getValue(),is(5));
    }

    @Test
    public void givenStack__whenValT__thenArrayValueIsReturned() {
        // Given
        Stack stack = new Stack();
        IdentNode ident = new IdentNode("arr", 6);
        stack.declTab(ident, 5, new TypeMethNode("int"));
        stack.affecterValT(ident, 2, 10, stack.getQueue().peek());
        // When
        Object val = stack.valT(ident, 2, stack.getQueue().peek());
        // Then
        assertThat(val,is(10));
    }

    @Test
    public void givenStack__whenAffecterValT__thenArrayValueIsUpdated() {
        // Given
        Stack stack = new Stack();
        IdentNode ident = new IdentNode("arr", 6);
        stack.declTab(ident, 5, new TypeMethNode("int"));
        // When
        stack.affecterValT(ident, 2, 10, stack.getQueue().peek());
        assertThat(stack.valT(ident,2,stack.getQueue().peek()),is(10));
    }


    @Test
    public void givenStack__whenLongT__thenArrayLengthIsReturned() {
        // Given
        Stack stack = new Stack();
        IdentNode ident = new IdentNode("arr", 6);
        stack.declTab(ident, 5, new TypeMethNode("int"));
        // When
        Integer length = stack.longT(ident, stack.getQueue().peek());
        // Then
        assertEquals(5, length.intValue());
    }

    @Test
    public void givenStack__whenRetirerDecl__thenDeclarationIsRemoved() {
        // Given
        Stack stack = new Stack();
        IdentNode ident = new IdentNode("x", 2);
        stack.declVar(ident, 2, new TypeMethNode("int"));
        // When
        stack.retirerDecl(ident, stack.getQueue().peek());
        // Then
        assertThat( stack.getQueue().size(),is(0));
    }

    @Test
    public void givenStack__whenSwap__thenTopTwoElementsAreSwapped() {
        // Given
        Stack stack = new Stack();
        stack.getQueue().push(new Quad(new IdentNode("x", 2), 2, new ObjNode("var"), new TypeMethNode("int")));
        stack.getQueue().push(new Quad(new IdentNode("y", 3), 3, new ObjNode("var"), new TypeMethNode("int")));
        // When
        stack.swap();
        // Then
        assertThat(stack.getQueue().pop().getIdent().getName(),is("x"));
        assertThat(stack.getQueue().pop().getIdent().getName(),is("y"));
    }

    public void setUpMethod() {

    }

    @Test
    public void givenStack__whenDeclMeth__thenMethIsOnTop() {
        // Given
        Stack stack = new Stack();
        IdentNode identMeth = new IdentNode("maMethode", 1);
        TypeMethNode returnType = new TypeMethNode("int");

        List<ParamNode> paramList = new ArrayList<>();
        paramList.add(new ParamNode(new TypeNode("int"),new IdentNode("param1", 2)));
        paramList.add(new ParamNode( new TypeNode("boolean"),new IdentNode("param2", 3)));
        ParamsNode params = new ParamsNode(paramList);
        // When
        stack.declMeth(identMeth, params, returnType);
        // Then
        assertThat(stack.getQueue().size(),is(1));
        assertThat(stack.getQueue().peek().getIdent().getName(),is("maMethode"));
        assertTrue(stack.getQueue().peek().getObj().isMeth());
        assertTrue(((TypeMethNode) stack.getQueue().peek().getType()).isInt());
    }

    @Test
    public void givenStack__whenIdentVal__thenValueIsReplacedAtDepth() {
        // Given
        Stack stack = new Stack();
        stack.declVar(new IdentNode("x", 2), 2, new TypeMethNode("int"));
        stack.declVar(new IdentNode("y", 3), 3, new TypeMethNode("int"));
        // When
        stack.identVal(new IdentNode("x", 2), new TypeMethNode("int"), stack.getQueue().peek(), 1);
        // Then
        assertThat(stack.getQueue().size(), is(2));
        assertThat(stack.pop().getIdent().getName(), is("y"));
        assertThat(stack.pop().getIdent().getName(), is("x"));
    }



    @Test
    public void givenStack__whenAffecterType__thenTypeIsUpdated() {
        Stack stack = new Stack();
        // Given
        stack.declVar(new IdentNode("x", 2), 2, new TypeMethNode("int"));
        // When
        stack.affecterType(new IdentNode("x", 2), new TypeMethNode("boolean"), stack.getQueue().peek());
        // Then
        assertTrue(((TypeMethNode) stack.getQueue().peek().getType()).isBoolean());
    }

    @Test
    public void givenStack__whenExpParam__thenParamsAreDeclared() {
        // Given
        Stack stack = new Stack();

        List<ParamNode> paramList = new ArrayList<>();
        paramList.add(new ParamNode(new TypeNode("int"), new IdentNode("param1", 2)));
        paramList.add(new ParamNode(new TypeNode("boolean"), new IdentNode("param2", 3)));
        ParamsNode params = new ParamsNode(paramList);

        List<ExpNode> expList = new ArrayList<>();
        // Implémentation minimale de ExpNode pour le test
        expList.add(new ExpNode() {
            @Override
            public <T> T accept(AstVisitor<T> visitor) {
                return null;
            }

            @Override
            public boolean sameAs(AstNode other) {
                return false;
            }

            @Override
            public String evaluate() {
                return "42"; // Valeur simulée pour le test
            }
        });
        expList.add(new ExpNode() {
            @Override
            public <T> T accept(AstVisitor<T> visitor) {
                return null;
            }

            @Override
            public boolean sameAs(AstNode other) {
                return false;
            }

            @Override
            public String evaluate() {
                return "true"; // Valeur simulée pour le test
            }
        });

        ListExpNode listExpNode = new ListExpNode(expList);

        // When
        stack.expParam(listExpNode, params);

        // Then
        assertThat(stack.getQueue().size(), is(2));
        assertThat(stack.getQueue().peek().getIdent().getName(), is("param2"));
    }


    @Test
    public void givenStack__whenObjet__thenObjTypeIsReturned() {
        // Given
        Stack stack = new Stack();
        stack.declVar(new IdentNode("x", 2), 2, new TypeMethNode("int"));
        // When
        ObjNode obj = stack.objet(new IdentNode("x", 2), stack.getQueue().peek());
        // Then
        assertTrue(obj.isVar());
    }

    @Test
    public void givenStack__whenParametre__thenParamsAreReturned() {
        // Given
        Stack stack = new Stack();
        IdentNode identMeth = new IdentNode("maMethode", 1);
        TypeMethNode returnType = new TypeMethNode("int");

        List<ParamNode> paramList = new ArrayList<>();
        paramList.add(new ParamNode(new TypeNode("int"),new IdentNode("param1", 2)));
        paramList.add(new ParamNode( new TypeNode("boolean"),new IdentNode("param2", 3)));
        ParamsNode params = new ParamsNode(paramList);
        stack.declMeth(identMeth, params, returnType);
        // When
        ParamsNode resultParams = stack.parametre(identMeth, stack.getQueue().peek());
        // Then
        assertThat(resultParams.getList().size(),is(2));
    }

    @Test
    public void givenStack__whenDeclaration__thenVarsAreReturned() {
        // Given
        Stack stack = new Stack();
        IdentNode identMeth = new IdentNode("maMethode", 1);
        TypeMethNode returnType = new TypeMethNode("int");

        List<ParamNode> paramList = new ArrayList<>();
        paramList.add(new ParamNode(new TypeNode("int"),new IdentNode("param1", 2)));
        paramList.add(new ParamNode( new TypeNode("boolean"),new IdentNode("param2", 3)));
        ParamsNode params = new ParamsNode(paramList);
        stack.declMeth(identMeth, params, returnType);
        // When
        VarsNode resultVars = stack.declaration(identMeth, stack.getQueue().peek());
        // Then
        assertThat(resultVars,notNullValue());
    }

    @Test
    public void givenStack__whenCorps__thenInstrsAreReturned() {
        // Given
        Stack stack = new Stack();
        IdentNode identMeth = new IdentNode("maMethode", 1);
        TypeMethNode returnType = new TypeMethNode("int");

        List<ParamNode> paramList = new ArrayList<>();
        paramList.add(new ParamNode(new TypeNode("int"),new IdentNode("param1", 2)));
        paramList.add(new ParamNode( new TypeNode("boolean"),new IdentNode("param2", 3)));
        ParamsNode params = new ParamsNode(paramList);

        stack.declMeth(identMeth, params, returnType);
        // When
        InstrsNode resultInstrs = stack.corps(identMeth, stack.getQueue().peek());
        // Then
        assertThat(resultInstrs,is(notNullValue()));
    }





}
