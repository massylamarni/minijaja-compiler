package fr.amvproject.group3.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import fr.amvproject.group3.settings.Settings;

public class CssManager {

  public static void generate(String outputPath) {
    String cssString = StyleSheet.CSS_TEMPLATE
        .replace("${BACKGROUND_DARK}", Settings.BACKGROUND_DARK)
        .replace("${BACKGROUND_DARKER}", Settings.BACKGROUND_DARKER)
        .replace("${BORDER_COLOR}", Settings.BORDER_COLOR)
        .replace("${ICON_COLOR}", Settings.ICON_COLOR)
        .replace("${LIGHT_TEXT_COLOR}", Settings.LIGHT_TEXT_COLOR)
        .replace("${DIMM_TEXT_COLOR}", Settings.DIMM_TEXT_COLOR)
        .replace("${PURPLE_TEXT_COLOR}", Settings.PURPLE_TEXT_COLOR)
        .replace("${SIDEBAR_HEIGHT}", Settings.SIDEBAR_HEIGHT)
        .replace("${STATUS_BAR_HEIGHT}", Settings.STATUS_BAR_HEIGHT)
        .replace("${LEFT_SIDEBAR_WIDTH}", Settings.LEFT_SIDEBAR_WIDTH)
        .replace("${TERMINAL_HEIGHT}", Settings.TERMINAL_HEIGHT)
        .replace("${SM_PADDING}", Settings.SM_PADDING)
        .replace("${MD_PADDING}", Settings.MD_PADDING)
        .replace("${LG_PADDING}", Settings.LG_PADDING)
        .replace("${SM_FONT_SIZE}", Settings.SM_FONT_SIZE)
        .replace("${ACTION_BUTTON_BORDER_RADIUS}", Settings.ACTION_BUTTON_BORDER_RADIUS)
        .replace("${SH_BROWN}", Settings.SH_BROWN)
        .replace("${SH_DARK_BLUE}", Settings.SH_DARK_BLUE)
        .replace("${SH_DARKER_BLUE}", Settings.SH_DARKER_BLUE)
        .replace("${SH_LIGHT_BLUE}", Settings.SH_LIGHT_BLUE)
        .replace("${SH_YELLOW}", Settings.SH_YELLOW)
        .replace("${SH_DARKER_YELLOW}", Settings.SH_DARKER_YELLOW)
        .replace("${SH_GREEN}", Settings.SH_GREEN)
        .replace("${SH_DARK_GREEN}", Settings.SH_DARK_GREEN)
        .replace("${SH_GREEN_YELLOW}", Settings.SH_GREEN_YELLOW)
        .replace("${SH_PURPLE}", Settings.SH_PURPLE)
        .replace("${SH_DARKER_PURPLE}", Settings.SH_DARKER_PURPLE)
        .replace("${SH_WHITE}", Settings.SH_WHITE)
        .replace("${SH_GREY}", Settings.SH_GREY);

    try {
      Files.writeString(Path.of(outputPath), cssString);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
