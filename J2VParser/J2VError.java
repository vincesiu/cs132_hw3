package J2VParser;

import syntaxtree.*;
import visitor.*;

public class J2VError {
  public static void throwError(String input) {
    System.err.println(input);
    System.exit(1);
  }
}
