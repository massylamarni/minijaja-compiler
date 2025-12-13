package fr.amvproject.group3.utils;

import java.util.Collections;

import org.fxmisc.richtext.CodeArea;

import fr.amvproject.group3.model.InterpreterWrapper;

public class EditorUtils {
  public static void highlightLine(InterpreterWrapper interpreterWrapper, CodeArea area) {
    if (interpreterWrapper.getPreviousBreakpointHitLine() != -1) {
      area.setParagraphStyle(interpreterWrapper.getPreviousBreakpointHitLine() - 1, Collections.emptyList());
    }

    area.setParagraphStyle(interpreterWrapper.getBreakpointHitLine() - 1, Collections.singleton("line-highlight"));
  }

  public static void resetLineHighlighting(CodeArea area) {
    int total = area.getParagraphs().size();

    for (int i = 0; i < total; i++) {
      area.setParagraphStyle(i, Collections.emptyList());
    }
  }
}
