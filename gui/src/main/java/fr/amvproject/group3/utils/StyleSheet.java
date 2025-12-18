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
      -fx-pref-height: ${ACTION_BUTTON_ICON_HEIGHT};
      -fx-min-height: ${ACTION_BUTTON_ICON_HEIGHT};
      -fx-border-color: ${BORDER_COLOR};
      -fx-border-width: 1px 1px 1px 1px;
    }

    .left-sidebar {
      -fx-background-color: ${BACKGROUND_DARKER};
      -fx-pref-width: ${LEFT_SIDEBAR_WIDTH};
      -fx-border-color: ${BORDER_COLOR};
      -fx-border-width: 0 1px 0 0;
    }

    .left-sidebar-title {
      -fx-text-fill: ${LIGHT_TEXT_COLOR};
      -fx-padding: 0 ${MD_PADDING} 0 ${MD_PADDING};
    }

    .left-sidebar-body {
      -fx-padding: ${MD_PADDING};
      -fx-border-color: ${BORDER_COLOR};
      -fx-border-width: 0 0 1px 0;
    }

    .stack-item, .heap-item {
      -fx-spacing: ${SM_PADDING};
    }

    .stack-item-key, .heap-item-key {
      -fx-text-fill: ${PURPLE_TEXT_COLOR};
      -fx-font-size: ${SM_FONT_SIZE};
    }

    .stack-item-value, .heap-item-value {
      -fx-text-fill: ${ICON_COLOR};
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
      -fx-alignment: center;
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

    .terminal-body {
      -fx-padding: 0 ${LG_PADDING} ${SM_PADDING} ${LG_PADDING};
    }

    .terminal-body .terminal-user-output {
      -fx-text-fill: ${SH_GREEN};
    }

    .terminal-body .terminal-path-output {
      -fx-text-fill: ${SH_DARK_BLUE};
    }

    .terminal-body .terminal-output {
      -fx-text-fill: ${ICON_COLOR};
    }

    .action-bar {
      -fx-spacing: ${ACTION_BUTTON_CONTAINER_GAP};
      -fx-alignment: center-right;
      -fx-padding: ${SM_PADDING};
    }

    .action-button {
      -fx-padding: 0;
      -fx-background-color: transparent;
      -fx-pref-width: ${ACTION_BUTTON_ICON_HEIGHT};
      -fx-pref-height: ${ACTION_BUTTON_ICON_HEIGHT};
      -fx-max-width: ${ACTION_BUTTON_ICON_HEIGHT};
      -fx-max-height: ${ACTION_BUTTON_ICON_HEIGHT};
      -fx-alignment: center;
      -fx-background-radius: ${ACTION_BUTTON_BORDER_RADIUS};
      -fx-cursor: hand;
    }

    .action-button:hover {
      -fx-background-color: ${BORDER_COLOR};
    }

    .debug-toolbar {
      -fx-border-color: ${BORDER_COLOR};
      -fx-border-width: 1px;
      -fx-border-radius: ${ACTION_BUTTON_BORDER_RADIUS};
      -fx-max-height: ${SM_BAR_HEIGHT};
      -fx-padding: 3px;
    }

    /* Defaults */

    .menu-bar {
      -fx-background-color: ${BACKGROUND_DARKER};
    }

    .menu-bar .label {
      -fx-text-fill: ${ICON_COLOR};
    }

    .menu-bar .menu {
      -fx-background-color: ${BACKGROUND_DARKER};
    }

    .menu-bar .menu:hover, .menu-bar .menu:armed {
      -fx-background-color: #ffffff30;
    }

    .menu .menu-item:armed, .menu .menu-item:hover {
      -fx-background-color: #ffffff30;
    }

    .menu .menu-item {
      -fx-background-color: ${BACKGROUND_DARK};
    }

    .context-menu {
      -fx-background-color: ${BACKGROUND_DARK};
      -fx-border-color: ${BORDER_COLOR};
      -fx-border-width: 1px;
      -fx-border-radius: ${ACTION_BUTTON_BORDER_RADIUS};
      -fx-background-radius: ${ACTION_BUTTON_BORDER_RADIUS};
    }

    .context-menu .label {
      -fx-padding: 0 ${LG_PADDING} 0 ${LG_PADDING};
      -fx-text-fill: ${DIMM_TEXT_COLOR};
    }

    .context-menu > * > .separator > .line {
      -fx-border-color: ${BORDER_COLOR};
    }


    .split-pane {
      -fx-background-color: transparent;
      -fx-border-color: transparent;
    }

    .split-pane .split-pane-divider {
      -fx-background-color: transparent;
      -fx-padding: 0;
    }

    .scroll-pane {
      -fx-background-color: transparent;
    }

    .scroll-pane > .viewport {
      -fx-background-color: transparent;
    }

    .scroll-pane .scroll-bar {
      -fx-background-color: transparent;
    }
    .scroll-pane .thumb {
      -fx-background-color: ${ICON_COLOR};
    }

    .tab-pane {
      -fx-background-color: ${BACKGROUND_DARKER};
    }

    .tab-pane .tab-header-area .tab-header-background {
      -fx-background-color: ${BACKGROUND_DARKER};
    }

    .tab-pane .tab-header-area {
      -fx-background-color: ${BACKGROUND_DARKER};
      -fx-padding: 0 0 0 0;
    }

    .tab-pane .tab {
      -fx-background-color: ${BACKGROUND_DARKER};
      -fx-background-insets: 0;
      -fx-padding: ${SM_PADDING} ${MD_PADDING} ${SM_PADDING} ${MD_PADDING};
      -fx-border-color: ${BORDER_COLOR} ${BORDER_COLOR} transparent ${BORDER_COLOR};
      -fx-border-width: 1 1 0 1;
      -fx-cursor: hand;
    }

    .tab-pane .tab-label {
      -fx-text-fill: ${LIGHT_TEXT_COLOR};
    }

    .tab-pane .tab:hover {
      -fx-background-color: #ffffff10;
    }

    .tab-pane .tab:selected {
      -fx-background-color: ${BACKGROUND_DARK};
      -fx-border-color: ${BORDER_COLOR} ${BORDER_COLOR} transparent ${BORDER_COLOR};
      -fx-border-width: 1 1 0 1;
    }

    .tab-pane .tab:selected .tab-label {
      -fx-text-fill: ${LIGHT_TEXT_COLOR};
    }

    .styled-text-area:focused .selection {
      -fx-fill: #ffffff30;
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
