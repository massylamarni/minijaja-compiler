package fr.amvproject.group3.ui.workbench.nodes;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import fr.amvproject.group3.model.InteractiveWrapper;
import fr.amvproject.group3.ui.icons.SvgIcons;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class EditorView extends CodeArea {
  public EditorView(InteractiveWrapper langWrapper) {
    langWrapper.getViewsWrapper().setEditorView(this);
    
    this.getStyleClass().add("editor");
    VBox.setVgrow(this, Priority.ALWAYS);
    HBox.setHgrow(this, Priority.ALWAYS);

    this.replaceText(langWrapper.getProgram());
    this.setStyleSpans(0, SyntaxHighlighting.highlightText(langWrapper.getProgram(), true));
    this.setParagraphGraphicFactory(LineNumberFactory.get(this));
    this.textProperty().addListener((obs, oldText, newText) -> {
      this.setStyleSpans(0, SyntaxHighlighting.highlightText(newText, true));
      langWrapper.updateProgram(newText);
    });

    setBreakpointHandler(this, langWrapper);
  }

  private void setBreakpointHandler(CodeArea codeArea, InteractiveWrapper langWrapper) {
    codeArea.setParagraphGraphicFactory(line -> {
      HBox h = new HBox();
      h.setSpacing(5);
      h.setCursor(Cursor.HAND);
      h.setAlignment(Pos.CENTER_LEFT);

      int relevantLine = line + 1;
      Label lineNumberView = new Label(String.valueOf(relevantLine));
      lineNumberView.getStyleClass().add("editor-line-number");

      SVGPath breakpointView = SvgIcons.breakPointIcon();
      breakpointView.setVisible(langWrapper.getInterpreterWrapper().getBreakpoints().contains(relevantLine));
      breakpointView.setFill(Color.RED);

      h.setOnMouseClicked(e -> {
        if (e.getButton() == MouseButton.PRIMARY) {
          if (langWrapper.getInterpreterWrapper().getBreakpoints().contains(relevantLine)) {
            langWrapper.getInterpreterWrapper().removeBreakpoint(relevantLine);
            breakpointView.setVisible(false);
          } else {
            langWrapper.getInterpreterWrapper().addBreakPoint(relevantLine);
            breakpointView.setVisible(true);
          }
        }
      });

      h.getChildren().addAll(breakpointView, lineNumberView);
      return h;
    });
  }
}
