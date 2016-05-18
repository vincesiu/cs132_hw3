import java.lang.*;
import java.util.*;
import syntaxtree.*;
import visitor.*;
import J2VParser.*;


public class J2V {
  public static void main(String[] args) {
    MiniJavaParser parser = new MiniJavaParser(System.in);

    try {
      Node root = parser.Goal();
      J2VEnv env = new J2VEnv();
      root.accept(new J2VVisitor(env));
      root.accept(new J2VParser(env));
      //System.err.println("Vapor compilation successful");
    }
    catch (Exception e) {
      //System.err.println(e.toString());
      //System.err.println(Arrays.toString(e.getStackTrace()));
      System.err.println("Vapor compilation failed");
    }
  }
}
