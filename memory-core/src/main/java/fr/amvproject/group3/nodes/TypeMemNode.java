package fr.amvproject.group3.nodes;

public class TypeMemNode {
    private enum TypeMemName {
        INT("int"),
        BOOLEAN("boolean"),
        STRING("string"),
        VOID("void");

        private final String name;

        TypeMemName(String name) {
            this.name = name;
        }

        public static TypeMemName fromString(String name) {
            for (TypeMemName type : values()) {
                if (name.equals("entier")) {
                    return TypeMemName.INT;
                }
                if (type.name.equals(name)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Type non valide : " + name);
        }
    }

    private final TypeMemName typeMem;

    public TypeMemNode(String name) {
        this.typeMem = TypeMemName.fromString(name);
    }

    public String getName() {
        return typeMem.name;
    }

    public TypeMemName getType() {
        return typeMem;
    }

    public boolean isInt() {
        return typeMem == TypeMemName.INT;
    }

    public boolean isBoolean() {
        return typeMem == TypeMemName.BOOLEAN;
    }

    public boolean isString() {
        return typeMem == TypeMemName.STRING;
    }

    public boolean isVoid() {
        return typeMem == TypeMemName.VOID;
    }
}
