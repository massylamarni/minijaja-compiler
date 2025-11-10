package fr.amvproject.group3;

public class LabelManager {
    private int counter = 0;

    public String newLabel(String base) {
        return base + "_" + (counter++);
    }
}
