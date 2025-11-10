package fr.amvproject.group3;

import java.util.ArrayList;
import java.util.List;

public class CodeEmitter {
    private final List<String> code = new ArrayList<>();

    // Ajoute une ligne de code Jaja
    public void emit(String line) {
        code.add(line);
    }

    // Retourne la liste complète (utile pour tests)
    public List<String> getCode() {
        return code;
    }

    // Affiche le code à la console
    public void print() {
        for (String line : code) {
            System.out.println(line);
        }
    }
}