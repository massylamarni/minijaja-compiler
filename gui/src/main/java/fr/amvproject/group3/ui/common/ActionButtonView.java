package fr.amvproject.group3.ui.common;

import fr.amvproject.group3.settings.Settings;
import fr.amvproject.group3.ui.icons.IconType;
import fr.amvproject.group3.ui.icons.SvgIcons;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class ActionButtonView extends Button {
  public ActionButtonView(IconType iconName) {   
    this.getStyleClass().add("action-button");

    SVGPath icon = getIcon(iconName);
    switch (iconName) {
      case DEBUG_STOP:
        icon.setFill(Color.web("#d07766"));
        break;
      case DEBUG_RESTART:
        icon.setFill(Color.web("#80c080"));
        break;
      case DEBUG_CONTINUE, DEBUG_PAUSE, DEBUG_STEP_IN, DEBUG_STEP_OUT:
        icon.setFill(Color.web("#6699cc"));
        break;
      
    
      default:
        icon.setFill(Color.web(Settings.ICON_COLOR));
    }

    this.setGraphic(icon);
  }

  private SVGPath getIcon(IconType iconName) {
    switch (iconName) {
      case RUN_WITH_DEBUG: return SvgIcons.runWithDebugIcon();
      case RUN: return SvgIcons.runIcon();
      case COMPILE: return SvgIcons.compileIcon();
      case DEBUG_CONTINUE: return SvgIcons.debugContinueIcon();
      case DEBUG_PAUSE: return SvgIcons.debugPauseIcon();
      case DEBUG_RESTART: return SvgIcons.debugRestartIcon();
      case DEBUG_STEP_IN: return SvgIcons.debugStepInIcon();
      case DEBUG_STEP_OUT: return SvgIcons.debugStepOutIcon();
      case DEBUG_STOP: return SvgIcons.debugStopIcon();
      case SEARCH: return SvgIcons.searchIcon();
      case CLOSE: return SvgIcons.closeIcon();
      default: return SvgIcons.closeIcon();
    }
  }
}
