package fr.amvproject.group3.memory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fr.amvproject.group3.ast.nodes.abstracts.Ident;
import fr.amvproject.group3.ast.nodes.other.TypeNode;
import fr.amvproject.group3.ast.nodes.params.EnteteNode;
import fr.amvproject.group3.ast.nodes.params.EntetesNode;

public class SymbolTable {
    public static class Symbol {
        private final Ident ident;
        private final TypeNode type;
        private final String dimension; // Table size can be defined by an expression
        private final EntetesNode entetes;
        private final int lineOfDeclaration;
        private List<Integer> linesOfUsage;

        public Symbol(Ident ident, TypeNode type, String dimension, EntetesNode entetes, int lineOfDeclaration) {
            this.ident = ident;
            this.type = type;
            this.dimension = dimension;
            this.entetes = entetes;
            this.lineOfDeclaration = lineOfDeclaration;
            this.linesOfUsage = new ArrayList<>();
        }

        public Ident getIdent() {
            return ident;
        }

        public TypeNode getType() {
            return type;
        }

        public String getDimension() {
            return dimension;
        }

        public EntetesNode getEntetes() {
            return entetes;
        }

        public int getLineOfDeclaration() {
            return lineOfDeclaration;
        }

        public List<Integer> getLinesOfUsage() {
            return linesOfUsage;
        }

        public void addLineOfUsage(int lineOfUsage) {
            linesOfUsage.add(lineOfUsage);
        }

        @Override
        public String toString() {
            StringBuilder params = new StringBuilder();

            if (entetes != null && !entetes.getList().isEmpty()) {
                for (EnteteNode e : entetes.getList()) {
                    TypeNode t = e.getType();
                    Ident i = e.getIdent();
                    params.append(String.format("%s %s, ", 
                        t != null ? t.getName() : "<?>", 
                        i != null ? i.getName() : "<?>"));
                }
                // remove trailing comma and space
                if (params.length() > 2)
                    params.setLength(params.length() - 2);
            } else {
                params.append("None");
            }

            return String.format(
                "Symbol(Name=%s, Type=%s, Dimension=%s, Params=[%s], DeclLine=%d, UsageLine=%d)",
                ident != null ? ident.getName() : "<?>",
                type != null ? type.getName() : "<?>",
                dimension,
                params,
                lineOfDeclaration,
                linesOfUsage
            );
        }
    }

    private final Map<String, Symbol> symbols = new LinkedHashMap<>();

    public boolean addSymbol(Ident ident, TypeNode type, EntetesNode entetes, String dimension, int lineOfDeclaration) {
        if (symbols.containsKey(ident.getName())) {
            // TODO: redeclaration error handling
            return false;
        }
        symbols.put(ident.getName(), new Symbol(ident, type, dimension, entetes, lineOfDeclaration));
        return true;
    }

    public Symbol lookup(Ident ident) {
        return symbols.get(ident.getName());
    }

    public boolean recordUsage(Ident ident, int lineOfUsage) {
        Symbol sym = symbols.get(ident.getName());
        if (sym == null) {
            // TODO: undeclared usage
            return false; 
        }
        sym.addLineOfUsage(lineOfUsage);
        return true;
    }

    public void printTable() {
        System.out.println("\n===== SYMBOL TABLE =====");
        System.out.printf("%-10s %-10s %-5s %-35s %-10s %-10s\n",
                "Name", "Type", "Dim", "Params", "Decl Line", "Usage Line");
        System.out.println("--------------------------------------------------------------------------------------");

        for (Symbol sym : symbols.values()) {
            String name = sym.getIdent() != null ? sym.getIdent().getName() : "<?>";

            String type = sym.getType() != null ? sym.getType().getName() : "<?>";

            String dim = sym.getDimension();

            // --- Format parameters (EntetesNode) ---
            StringBuilder params = new StringBuilder();
            if (sym.getEntetes() != null && !sym.getEntetes().getList().isEmpty()) {
                for (var e : sym.getEntetes().getList()) {
                    String pType = e.getType() != null ? e.getType().getName() : "<?>"; 
                    String pName = e.getIdent() != null ? e.getIdent().getName() : "<?>";
                    params.append(pType).append(" ").append(pName).append(", ");
                }
                if (params.length() > 2) params.setLength(params.length() - 2); // remove trailing comma
            } else {
                params.append("None");
            }

            int declLine = sym.getLineOfDeclaration();
            String sLinesOfUsage = sym.getLinesOfUsage().isEmpty() ? "None" : sym.getLinesOfUsage().toString();
            System.out.printf("%-10s %-10s %-5s %-35s %-10d %-10s\n",
                    name, type, dim, params, declLine, sLinesOfUsage);
        }
    }

}
