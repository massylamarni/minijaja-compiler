package fr.amvproject.group3.utils;

public class StyleSheet {
  public static final String CSS_TEMPLATE = """
    .ide {
      -fx-background-color: ${BACKGROUND_DARK};
    }

    .ide-title-bar {
      -fx-background-color: ${BACKGROUND_DARKER};
      -fx-pref-height: ${SIDEBAR_HEIGHT};
      -fx-border-color: ${BORDER_COLOR};
      -fx-border-width: 1px 1px 1px 1px;
    }

    .ide-body {
      -fx-background-color: ${BACKGROUND_DARK};
    }

    .ide-status-bar {
      -fx-background-color: ${BACKGROUND_DARKER};
      -fx-pref-height: ${STATUS_BAR_HEIGHT};
      -fx-border-color: ${BORDER_COLOR};
      -fx-border-width: 1px 1px 1px 1px;
    }

    .left-sidebar {
      -fx-background-color: ${BACKGROUND_DARKER};
      -fx-pref-width: ${LEFT_SIDEBAR_WIDTH};
      -fx-border-color: ${BORDER_COLOR};
      -fx-border-width: 0 1px 0 0;
      -fx-padding: 0 ${SM_PADDING};
    }

    .left-sidebar-title {
      -fx-text-fill: ${LIGHT_TEXT_COLOR};
      -fx-padding: ${SM_PADDING} ${MD_PADDING} 0 ${MD_PADDING};
    }

    .left-sidebar-body {
      -fx-padding: ${MD_PADDING};
    }

    .stack-item {
      -fx-spacing: ${SM_PADDING};
    }

    .stack-item-key {
      -fx-text-fill: ${PURPLE_TEXT_COLOR};
      -fx-font-family: "Droid Sans Mono";
      -fx-font-size: ${SM_FONT_SIZE};
    }

    .stack-item-value {
      -fx-text-fill: ${ICON_COLOR};
      -fx-font-family: "Droid Sans Mono";
      -fx-font-size: ${SM_FONT_SIZE};
    }

    .workbench {
      -fx-background-color: ${BACKGROUND_DARK};
    }

    .workbench-title-bar {
      -fx-background-color: ${BACKGROUND_DARKER};
      -fx-pref-height: ${SIDEBAR_HEIGHT};
      -fx-border-color: ${BORDER_COLOR};
      -fx-border-width: 0 0 1px 0;
    }

    .editor {
      -fx-background-color: ${BACKGROUND_DARK};
      -fx-padding: 0 ${SM_PADDING};
    }

    .editor-line-number {
      -fx-text-fill: ${ICON_COLOR};
      -fx-padding: 0 ${SM_PADDING};
    }

    .editor .caret {
      -fx-stroke: ${LIGHT_TEXT_COLOR};
    }

    .terminal {
      -fx-background-color: ${BACKGROUND_DARKER};
      -fx-border-color: ${BORDER_COLOR};
      -fx-border-width: 1px 0 0 0;
      -fx-pref-height: ${TERMINAL_HEIGHT};
    }

    .terminal-title-bar {
      -fx-background-color: ${BACKGROUND_DARKER};
      -fx-pref-height: ${SIDEBAR_HEIGHT};
    }

    .action-bar {
      -fx-spacing: 7px;
      -fx-alignment: center-right;
      -fx-padding: ${SM_PADDING};
    }

    .action-button {
      -fx-background-color: transparent;
      -fx-pref-width: 22px;
      -fx-pref-height: 22px;
      -fx-alignment: center;
      -fx-background-radius: ${ACTION_BUTTON_BORDER_RADIUS};
      -fx-cursor: hand;
    }

    .action-button:hover {
      -fx-background-color: ${BORDER_COLOR};
    }

    /* Defaults */

    .menu-bar {
      -fx-background-color: ${BACKGROUND_DARKER};
    }

    .menu-bar .label {
      -fx-text-fill: ${ICON_COLOR};
    }

    .split-pane {
      -fx-background-color: transparent;
      -fx-border-color: transparent;
    }

    .split-pane .split-pane-divider {
      -fx-background-color: transparent;
      -fx-padding: 0;
    }

    /* Highlightings */

    .line-highlight {
      -fx-background-color: #ffff0030;
    }

    .sh-brown {
      -fx-fill: ${SH_BROWN};
    }

    .sh-dark-blue {
      -fx-fill: ${SH_DARK_BLUE};
    }

    .sh-light-blue {
      -fx-fill: ${SH_LIGHT_BLUE};
    }

    .sh-yellow {
      -fx-fill: ${SH_YELLOW};
    }

    .sh-green {
      -fx-fill: ${SH_GREEN};
    }

    .sh-dark-green {
      -fx-fill: ${SH_DARK_GREEN};
    }

    .sh-green-yellow {
      -fx-fill: ${SH_GREEN_YELLOW};
    }

    .sh-purple {
      -fx-fill: ${SH_PURPLE};
    }

    .sh-white {
      -fx-fill: ${SH_WHITE};
    }

    .sh-grey {
      -fx-fill: ${SH_GREY};
    }
    

    """;
}
