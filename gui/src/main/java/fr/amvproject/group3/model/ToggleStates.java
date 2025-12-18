package fr.amvproject.group3.model;

import fr.amvproject.group3.ui.ide.nodes.LeftSideBarView;
import fr.amvproject.group3.ui.terminal.TerminalView;
import fr.amvproject.group3.ui.workbench.nodes.DebugToolBarView;

public class ToggleStates {
  private boolean isLeftSideBarOpen = false;
  private boolean isTerminalOpen = false;
  private boolean isDebugOn = false;

  public LeftSideBarView leftSideBarView;
  public TerminalView terminalView;
  public DebugToolBarView debugToolBarView;

  public void holdLeftSideBarState() {
    if (isLeftSideBarOpen) {
      leftSideBarView.openLeftSideBar();
    } else {
      leftSideBarView.closeLeftSideBar();
    }
  }

  public void holdTerminalState() {
    if (isTerminalOpen) {
      terminalView.openTerminal();
    } else {
      terminalView.closeTerminal();
    }
  }

  public void holdDebugToolBarState() {
    if (isDebugOn) {
      debugToolBarView.showDebugToolBar();
    } else {
      debugToolBarView.hideDebugToolBar();
    }
  }

  public void closeLeftSideBar() {
    isLeftSideBarOpen = false;
    leftSideBarView.closeLeftSideBar();
  }

  public void closeTerminal() {
    isTerminalOpen = false;
    terminalView.closeTerminal();
  }

  public void hideDebugToolBar() {
    isDebugOn = false;
    debugToolBarView.hideDebugToolBar();
  }

  public void openLeftSideBar() {
    isLeftSideBarOpen = true;
    leftSideBarView.openLeftSideBar();
  }

  public void openTerminal() {
    isTerminalOpen = true;
    terminalView.openTerminal();
  }

  public void showDebugToolBar() {
    isDebugOn = true;
    debugToolBarView.showDebugToolBar();
  }

  public void reset() {
    closeLeftSideBar();
    closeTerminal();
    hideDebugToolBar();
  }
}
