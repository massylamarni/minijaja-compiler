package fr.amvproject.group3;

import java.util.HashMap;
import java.util.Map;

import fr.amvproject.group3.ast.nodes.other.TypeNode;

public class MiniJajaCompiler {
    private final Map<String, Object> variables = new HashMap<>();

    public void allocateVar(String ident, TypeNode type) {
        variables.put(ident, 0); // valeur par défaut
    }

    public void allocateArray(String ident, TypeNode type) {
        variables.put(ident, new Object[10]); // taille fixe pour test
    }

    public void loadVar(String ident) {
        System.out.println("LOAD " + ident + " (" + variables.get(ident) + ")");
    }

    public void storeVar(String ident) {
        System.out.println("STORE " + ident);
    }

    public void loadArrayElem(String ident) {
        System.out.println("LOAD " + ident + "[index]");
    }

    public void storeArrayElem(String ident) {
        System.out.println("STORE " + ident + "[index]");
    }

    public void pushInt(int value) {
        System.out.println("PUSH " + value);
    }

    public void pushBool(boolean value) {
        System.out.println("PUSH " + value);
    }

    public void writeTop() {
        System.out.println("WRITE <top>");
    }

    public void writelnTop() {
        System.out.println("WRITELN <top>");
    }

    public String createLabel(String prefix) {
        return prefix + "_" + System.nanoTime();
    }

    public void jumpIfFalse(String label) {
        System.out.println("JUMP_IF_FALSE " + label);
    }

    public void jump(String label) {
        System.out.println("JUMP " + label);
    }

    public void placeLabel(String label) {
        System.out.println(label + ":");
    }
}
