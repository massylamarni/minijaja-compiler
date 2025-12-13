package fr.amvproject.group3.ui.workbench.nodes;

import java.util.Collection;
import java.util.Collections;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import fr.amvproject.group3.JajaCodeLexer;
import fr.amvproject.group3.MiniJajaLexer;

public class SyntaxHighlighting {
  private SyntaxHighlighting() {}

  public static StyleSpans<Collection<String>> highlightText(String text, boolean isMjj) {
    CommonTokenStream tokens;
    if (isMjj) {
      tokens = new CommonTokenStream(new MiniJajaLexer(CharStreams.fromString(text)));
    } else {
      tokens = new CommonTokenStream(new JajaCodeLexer(CharStreams.fromString(text)));
    }
    tokens.fill();

    StyleSpansBuilder<Collection<String>> spans = new StyleSpansBuilder<>();

    int lastIndex = 0;
    for (Token token : tokens.getTokens()) {
      int start = token.getStartIndex();
      int end = token.getStopIndex() + 1;
      if (start > lastIndex) spans.add(Collections.emptyList(), start - lastIndex);
      String tokenClass = isMjj ? getMjjTokenClass(token.getType()) : getJjcTokenClass(token.getType());
      spans.add(Collections.singleton(tokenClass), end - start);
      lastIndex = end;
    }

    // Remaining text after last token
    if (lastIndex < text.length()) spans.add(Collections.emptyList(), text.length() - lastIndex);

    return spans.create();
  }

  private static String getMjjTokenClass(int type) {
    switch (type) {
      case MiniJajaLexer.CLASS:
      case MiniJajaLexer.FINAL:
      case MiniJajaLexer.TRUE:
      case MiniJajaLexer.FALSE:
      case MiniJajaLexer.LBRACE:
      case MiniJajaLexer.RBRACE:
      case MiniJajaLexer.LPAREN:
      case MiniJajaLexer.RPAREN:
      case MiniJajaLexer.LBRACK:
      case MiniJajaLexer.RBRACK:
      case MiniJajaLexer.MAIN:
        return "sh-dark-blue";

      case MiniJajaLexer.WRITE:
      case MiniJajaLexer.WRITELN:
      case MiniJajaLexer.LENGTH:
        return "sh-yellow";

      case MiniJajaLexer.VOID:
      case MiniJajaLexer.INT:
      case MiniJajaLexer.BOOLEAN:
        return "sh-green";

      case MiniJajaLexer.RETURN:
      case MiniJajaLexer.IF:
      case MiniJajaLexer.ELSE:
      case MiniJajaLexer.WHILE:
        return "sh-purple";

      case MiniJajaLexer.ASSIGN:
      case MiniJajaLexer.ADDASSIGN:
      case MiniJajaLexer.INC:
      case MiniJajaLexer.ADD:
      case MiniJajaLexer.SUB:
      case MiniJajaLexer.MUL:
      case MiniJajaLexer.DIV:
      case MiniJajaLexer.NOT:
      case MiniJajaLexer.AND:
      case MiniJajaLexer.OR:
      case MiniJajaLexer.EQ:
      case MiniJajaLexer.GT:
      case MiniJajaLexer.COMMA:
      case MiniJajaLexer.SEMI:
        return "sh-white";

      case MiniJajaLexer.IDENTIFICATEUR:
        return "sh-light-blue";

      case MiniJajaLexer.STRING:
        return "sh-brown";

      case MiniJajaLexer.NOMBRE:
        return "sh-green-yellow";

      case MiniJajaLexer.COMMENT_LINE:
      case MiniJajaLexer.COMMENT_BLOCK:
        return "sh-dark-green";

      default:
        return "";
    }
  }

  private static String getJjcTokenClass(int type) {
    switch (type) {
      case JajaCodeLexer.INIT:
      case JajaCodeLexer.SWAP:
      case JajaCodeLexer.PUSH:
      case JajaCodeLexer.POP:
      case JajaCodeLexer.INC:
      case JajaCodeLexer.AINC:
      case JajaCodeLexer.NOP:
      case JajaCodeLexer.JCSTOP:
        return "sh-dark-blue";

      case JajaCodeLexer.INVOKE:
      case JajaCodeLexer.WRITE:
      case JajaCodeLexer.WRITELN:
      case JajaCodeLexer.GOTO:
      case JajaCodeLexer.LENGHT:
        return "sh-yellow";

      case JajaCodeLexer.VIDE:
      case JajaCodeLexer.INT:
      case JajaCodeLexer.BOOLEAN:
        return "sh-green";

      case JajaCodeLexer.VAR:
      case JajaCodeLexer.METH:
      case JajaCodeLexer.CST:
      case JajaCodeLexer.TAB:
        return "sh-grey";

      case JajaCodeLexer.RETURN:
      case JajaCodeLexer.IF:
        return "sh-purple";

      case JajaCodeLexer.ADD:
      case JajaCodeLexer.SUB:
      case JajaCodeLexer.MUL:
      case JajaCodeLexer.DIV:
      case JajaCodeLexer.NOT:
      case JajaCodeLexer.AND:
      case JajaCodeLexer.OR:
      case JajaCodeLexer.NEG:
      case JajaCodeLexer.CMP:
      case JajaCodeLexer.SUP:
      case JajaCodeLexer.COMMA:
        return "sh-white";

      case JajaCodeLexer.IDENTIFICATEUR:
      case JajaCodeLexer.NEW:
      case JajaCodeLexer.NEWARRAY:
      case JajaCodeLexer.LOAD:
      case JajaCodeLexer.ALOAD:
      case JajaCodeLexer.STORE:
      case JajaCodeLexer.ASTORE:
        return "sh-light-blue";

      case JajaCodeLexer.STRING:
        return "sh-brown";

      case JajaCodeLexer.NOMBRE:
      case JajaCodeLexer.TRUE:
      case JajaCodeLexer.FALSE:
        return "sh-green-yellow";

      case JajaCodeLexer.COMMENT_LINE:
      case JajaCodeLexer.COMMENT_BLOCK:
        return "sh-dark-green";

      default:
        return "";
    }
  }
}
