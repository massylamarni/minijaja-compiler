package fr.amvproject.group3.utils;

import java.util.List;

public class Lang {
  public static int getHashCodeFromList(List<?> list) {
    int hashSum = 0;
    for (Object object : list) {
      hashSum += object.hashCode();
    }
    return hashSum;
  }
}
