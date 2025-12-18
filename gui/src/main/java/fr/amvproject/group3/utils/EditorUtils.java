package fr.amvproject.group3.utils;

import java.util.Collections;

import org.fxmisc.richtext.CodeArea;

import fr.amvproject.group3.mjj.ast.PrintVisitor;
import fr.amvproject.group3.mjj.utils.AstOperations;
import fr.amvproject.group3.mjj.utils.AstOperations.AstWrapper;
import fr.amvproject.group3.model.wrappers.InterpreterWrapper;

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

  public static String getFormattedProgram(String program, boolean isMjj) {
    try {
      if (isMjj) {
        AstWrapper astWrapper = AstOperations.getAstFromString(program);
        if (astWrapper.lexicalErrorsListener.hasErrors() || astWrapper.syntaxicErrorsListener.hasErrors()) {
          return null;
        } else {
          return astWrapper.ast.accept(new PrintVisitor());
        }
      } else {
        fr.amvproject.group3.jjc.utils.AstOperations.AstWrapper astWrapper = fr.amvproject.group3.jjc.utils.AstOperations.getAstFromString(program);
        if (astWrapper.lexicalErrorsListener.hasErrors() || astWrapper.syntaxicErrorsListener.hasErrors()) {
          return null;
        } else {
          return astWrapper.ast.accept(new fr.amvproject.group3.jjc.ast.PrintVisitor());
        }
      }
    } catch (Exception e) {
      return null; // TODO: handle exception
    }
  } 
}
