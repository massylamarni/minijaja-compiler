package fr.amvproject.group3.ast.nodes.other;

import fr.amvproject.group3.ast.AstNode;

public class TypeNode implements AstNode {

    private String name;  // "int", "boolean" ou "void"

    public TypeNode(String name) {
        if (!name.equals("int") && !name.equals("boolean") && !name.equals("void")) {
            throw new IllegalArgumentException("Type non valide : " + name);
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Vérifie si le type est int
    public boolean isInt() {
        return "int".equals(name);
    }

    // Vérifie si le type est boolean
    public boolean isBoolean() {
        return "boolean".equals(name);
    }

    // Vérifie si le type est void
    public boolean isVoid() {
        return "void".equals(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
