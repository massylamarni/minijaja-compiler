package fr.amvproject.group3.ast.nodes.other;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;

public class TypeNode extends AstNode {

    public enum TypeName {
        INT("int"),
        BOOLEAN("boolean"),
        VOID("void");

        private final String name;

        TypeName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static TypeName fromString(String name) {
            for (TypeName type : values()) {
                if (type.name.equals(name)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Type non valide : " + name);
        }
    }

    private final TypeName type;

    public TypeNode(String name) {
        this.type = TypeName.fromString(name);
    }

    public TypeName getType() {
        return type;
    }

    public boolean isInt() {
        return type == TypeName.INT;
    }

    public boolean isBoolean() {
        return type == TypeName.BOOLEAN;
    }

    public boolean isVoid() {
        return type == TypeName.VOID;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitType(this);
    }
}
