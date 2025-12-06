package fr.amvproject.group3.utils;

import java.util.Collections;

import org.fxmisc.richtext.CodeArea;

import fr.amvproject.group3.model.LangWrapper;

public class EditorUtils {
  public static void highlightLine(LangWrapper langWrapper, CodeArea area) {
    if (langWrapper.getPreviousBreakpointHitLine() != -1) {
      area.setParagraphStyle(langWrapper.getPreviousBreakpointHitLine() - 1, Collections.emptyList());
    }

    area.setParagraphStyle(langWrapper.getBreakpointHitLine() - 1, Collections.singleton("line-highlight"));
  }

  public static void resetLineHighlighting(CodeArea area) {
    int total = area.getParagraphs().size();

    for (int i = 0; i < total; i++) {
      area.setParagraphStyle(i, Collections.emptyList());
    }
  }
}
