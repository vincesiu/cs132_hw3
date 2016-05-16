package J2VParser;

import syntaxtree.*;
import visitor.*;
import java.util.*;

public class J2VParser extends GJNoArguDepthFirst<String> {

  J2VEnv env;

  public J2VParser(J2VEnv input) {
    env = input;
  }
  //
  // Auto class visitors--probably don't need to be overridden.
  //
  public String visit(NodeList n) {
    String _ret=null;
    int _count=0;
    for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
      e.nextElement().accept(this);
      _count++;
    }
    return _ret;
  }

  public String visit(NodeListOptional n) {
    if ( n.present() ) {
      String _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
        e.nextElement().accept(this);
        _count++;
      }
      return _ret;
    }
    else
      return null;
  }

  public String visit(NodeOptional n) {
    if ( n.present() )
      return n.node.accept(this);
    else
      return null;
  }

  public String visit(NodeSequence n) {
    String _ret=null;
    int _count=0;
    for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
      e.nextElement().accept(this);
      _count++;
    }
    return _ret;
  }

  public String visit(NodeToken n) { return null; }

  //
  // User-generated visitor methods below
  //

  /**
   * f0 -> MainClass()
   * f1 -> ( TypeDeclaration() )*
   * f2 -> <EOF>
   */
  public String visit(Goal n) {
    String _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return _ret;
  }

  /**
   * f0 -> "class"
   * f1 -> Identifier()
   * f2 -> "{"
   * f3 -> "public"
   * f4 -> "static"
   * f5 -> "void"
   * f6 -> "main"
   * f7 -> "("
   * f8 -> "String"
   * f9 -> "["
   * f10 -> "]"
   * f11 -> Identifier()
   * f12 -> ")"
   * f13 -> "{"
   * f14 -> ( VarDeclaration() )*
   * f15 -> ( Statement() )*
   * f16 -> "}"
   * f17 -> "}"
   */
  public String visit(MainClass n) {
    String _ret=null;
    
    stmtMethodParamStart("main");
    stmtMethodParamEnd(); 
    pushIndentation();
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    n.f5.accept(this);
    n.f6.accept(this);
    n.f7.accept(this);
    n.f8.accept(this);
    n.f9.accept(this);
    n.f10.accept(this);
    n.f11.accept(this);
    n.f12.accept(this);
    n.f13.accept(this);
    n.f14.accept(this);
    n.f15.accept(this);
    n.f16.accept(this);
    n.f17.accept(this);
    popIndentation();
    return _ret;
  }

  /**
   * f0 -> ClassDeclaration()
   *       | ClassExtendsDeclaration()
   */
  public String visit(TypeDeclaration n) {
    String _ret=null;
    n.f0.accept(this);
    return _ret;
  }

  /**
   * f0 -> "class"
   * f1 -> Identifier()
   * f2 -> "{"
   * f3 -> ( VarDeclaration() )*
   * f4 -> ( MethodDeclaration() )*
   * f5 -> "}"
   */
  public String visit(ClassDeclaration n) {
    String _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    n.f5.accept(this);
    return _ret;
  }

  /**
   * f0 -> "class"
   * f1 -> Identifier()
   * f2 -> "extends"
   * f3 -> Identifier()
   * f4 -> "{"
   * f5 -> ( VarDeclaration() )*
   * f6 -> ( MethodDeclaration() )*
   * f7 -> "}"
   */
  public String visit(ClassExtendsDeclaration n) {
    String _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    n.f5.accept(this);
    n.f6.accept(this);
    n.f7.accept(this);
    return _ret;
  }

  /**
   * f0 -> Type()
   * f1 -> Identifier()
   * f2 -> ";"
   */
  public String visit(VarDeclaration n) {
    String _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return _ret;
  }

  /**
   * f0 -> "public"
   * f1 -> Type()
   * f2 -> Identifier()
   * f3 -> "("
   * f4 -> ( FormalParameterList() )?
   * f5 -> ")"
   * f6 -> "{"
   * f7 -> ( VarDeclaration() )*
   * f8 -> ( Statement() )*
   * f9 -> "return"
   * f10 -> Expression()
   * f11 -> ";"
   * f12 -> "}"
   */
  public String visit(MethodDeclaration n) {
    String _ret=null;

    System.out.println("");
    //String method_name = n.f2.accept(this);
    String method_name = n.f2.f0.toString();
    stmtMethodParamStart(method_name);
    n.f4.accept(this);
    stmtMethodParamEnd();

    pushIndentation();
    n.f7.accept(this);
    n.f8.accept(this);
    n.f10.accept(this);
    popIndentation();

    return _ret;
  }

  /**
   * f0 -> FormalParameter()
   * f1 -> ( FormalParameterRest() )*
   */
  public String visit(FormalParameterList n) {
    String _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    return _ret;
  }

  /**
   * f0 -> Type()
   * f1 -> Identifier()
   */
  public String visit(FormalParameter n) {
    String _ret=null;
    //String parameter_name = n.f1.accept(this);
    String parameter_name = n.f1.f0.toString();

    stmtMethodParamParameter(parameter_name);

    return _ret;
  }

  /**
   * f0 -> ","
   * f1 -> FormalParameter()
   */
  public String visit(FormalParameterRest n) {
    String _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    return _ret;
  }

  /**
   * f0 -> ArrayType()
   *       | BooleanType()
   *       | IntegerType()
   *       | Identifier()
   */
  public String visit(Type n) {
    String _ret=null;
    n.f0.accept(this);
    return _ret;
  }

  /**
   * f0 -> "int"
   * f1 -> "["
   * f2 -> "]"
   */
  public String visit(ArrayType n) {
    String _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return _ret;
  }

  /**
   * f0 -> "boolean"
   */
  public String visit(BooleanType n) {
    String _ret=null;
    n.f0.accept(this);
    return _ret;
  }

  /**
   * f0 -> "int"
   */
  public String visit(IntegerType n) {
    String _ret=null;
    n.f0.accept(this);
    return _ret;
  }

  /**
   * f0 -> Block()
   *       | AssignmentStatement()
   *       | ArrayAssignmentStatement()
   *       | IfStatement()
   *       | WhileStatement()
   *       | PrintStatement()
   */
  public String visit(Statement n) {
    String _ret=null;
    n.f0.accept(this);
    return _ret;
  }

  /**
   * f0 -> "{"
   * f1 -> ( Statement() )*
   * f2 -> "}"
   */
  public String visit(Block n) {
    String _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return _ret;
  }

  /**
   * f0 -> Identifier()
   * f1 -> "="
   * f2 -> Expression()
   * f3 -> ";"
   */
  public String visit(AssignmentStatement n) {
    String _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    return _ret;
  }

  /**
   * f0 -> Identifier()
   * f1 -> "["
   * f2 -> Expression()
   * f3 -> "]"
   * f4 -> "="
   * f5 -> Expression()
   * f6 -> ";"
   */
  public String visit(ArrayAssignmentStatement n) {
    String _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    n.f5.accept(this);
    n.f6.accept(this);
    return _ret;
  }

  /**
   * f0 -> "if"
   * f1 -> "("
   * f2 -> Expression()
   * f3 -> ")"
   * f4 -> Statement()
   * f5 -> "else"
   * f6 -> Statement()
   */
  public String visit(IfStatement n) {
    String _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    n.f5.accept(this);
    n.f6.accept(this);
    return _ret;
  }

  /**
   * f0 -> "while"
   * f1 -> "("
   * f2 -> Expression()
   * f3 -> ")"
   * f4 -> Statement()
   */
  public String visit(WhileStatement n) {
    String _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    return _ret;
  }

  /**
   * f0 -> "System.out.println"
   * f1 -> "("
   * f2 -> Expression()
   * f3 -> ")"
   * f4 -> ";"
   */
  public String visit(PrintStatement n) {
    String _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    return _ret;
  }

  /**
   * f0 -> AndExpression()
   *       | CompareExpression()
   *       | PlusExpression()
   *       | MinusExpression()
   *       | TimesExpression()
   *       | ArrayLookup()
   *       | ArrayLength()
   *       | MessageSend()
   *       | PrimaryExpression()
   */
  public String visit(Expression n) {
    String _ret=null;
    n.f0.accept(this);
    return _ret;
  }

  /**
   * f0 -> PrimaryExpression()
   * f1 -> "&&"
   * f2 -> PrimaryExpression()
   */
  public String visit(AndExpression n) {
    String _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return _ret;
  }

  /**
   * f0 -> PrimaryExpression()
   * f1 -> "<"
   * f2 -> PrimaryExpression()
   */
  public String visit(CompareExpression n) {
    String _ret=null;
    n.f0.accept(this);
    n.f2.accept(this);

    return _ret;
  }

  /**
   * f0 -> PrimaryExpression()
   * f1 -> "+"
   * f2 -> PrimaryExpression()
   */
  public String visit(PlusExpression n) {
    String _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return _ret;
  }

  /**
   * f0 -> PrimaryExpression()
   * f1 -> "-"
   * f2 -> PrimaryExpression()
   */
  public String visit(MinusExpression n) {
    String _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return _ret;
  }

  /**
   * f0 -> PrimaryExpression()
   * f1 -> "*"
   * f2 -> PrimaryExpression()
   */
  public String visit(TimesExpression n) {
    String _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return _ret;
  }

  /**
   * f0 -> PrimaryExpression()
   * f1 -> "["
   * f2 -> PrimaryExpression()
   * f3 -> "]"
   */
  public String visit(ArrayLookup n) {
    String _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    return _ret;
  }

  /**
   * f0 -> PrimaryExpression()
   * f1 -> "."
   * f2 -> "length"
   */
  public String visit(ArrayLength n) {
    String _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return _ret;
  }

  /**
   * f0 -> PrimaryExpression()
   * f1 -> "."
   * f2 -> Identifier()
   * f3 -> "("
   * f4 -> ( ExpressionList() )?
   * f5 -> ")"
   */
  public String visit(MessageSend n) {
    String _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    n.f5.accept(this);
    return _ret;
  }

  /**
   * f0 -> Expression()
   * f1 -> ( ExpressionRest() )*
   */
  public String visit(ExpressionList n) {
    String _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    return _ret;
  }

  /**
   * f0 -> ","
   * f1 -> Expression()
   */
  public String visit(ExpressionRest n) {
    String _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    return _ret;
  }

  /**
   * f0 -> IntegerLiteral()
   *       | TrueLiteral()
   *       | FalseLiteral()
   *       | Identifier()
   *       | ThisExpression()
   *       | ArrayAllocationExpression()
   *       | AllocationExpression()
   *       | NotExpression()
   *       | BracketExpression()
   */
  public String visit(PrimaryExpression n) {
    String _ret=null;
    n.f0.accept(this);
    return _ret;
  }

  /**
   * f0 -> <INTEGER_LITERAL>
   */
  public String visit(IntegerLiteral n) {
    String _ret=null;
    n.f0.accept(this);
    return _ret;
  }

  /**
   * f0 -> "true"
   */
  public String visit(TrueLiteral n) {
    String _ret=null;
    n.f0.accept(this);
    return _ret;
  }

  /**
   * f0 -> "false"
   */
  public String visit(FalseLiteral n) {
    String _ret=null;
    n.f0.accept(this);
    return _ret;
  }

  /**
   * f0 -> <IDENTIFIER>
   */
  public String visit(Identifier n) {
    String _ret=null;
    return _ret;
  }

  /**
   * f0 -> "this"
   */
  public String visit(ThisExpression n) {
    String _ret=null;
    n.f0.accept(this);
    return _ret;
  }

  /**
   * f0 -> "new"
   * f1 -> "int"
   * f2 -> "["
   * f3 -> Expression()
   * f4 -> "]"
   */
  public String visit(ArrayAllocationExpression n) {
    String _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    return _ret;
  }

  /**
   * f0 -> "new"
   * f1 -> Identifier()
   * f2 -> "("
   * f3 -> ")"
   */
  public String visit(AllocationExpression n) {
    String _ret=null;
    int counter = 0;
    //String class_name = n.f1.accept(this);
    String class_name = n.f1.f0.toString();

    J2VClassLayout class_layout = env.layout.get(class_name);

    counter = obtainVarTicket();

    stmtAssignment(counter, "HeapAllocZ(" + class_layout.size + ")");
    stmtMemoryAccess(counter, ":vmt_" + class_layout.id);
        /*
    System.out.println(" = HeapAllocZ(" + class_layout.size + ")");
    System.out.println(" = :vmt_" + class_layout.id);
    */
    return _ret;
  }

  /**
   * f0 -> "!"
   * f1 -> Expression()
   */
  public String visit(NotExpression n) {
    String _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    return _ret;
  }

  /**
   * f0 -> "("
   * f1 -> Expression()
   * f2 -> ")"
   */
  public String visit(BracketExpression n) {
    String _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return _ret;
  }

  void stmtMethodParamStart(String function_name) {
    if (function_name.equals("main")) {
      System.out.printf("func Main(");
    } else {
      System.out.printf("func " + function_name + "(this");
    }
  }
  
  void stmtMethodParamParameter(String parameter_name) {
    System.out.printf(" " + parameter_name);
  }

  void stmtMethodParamEnd() {
    System.out.printf(")\n");
  }

  void pushIndentation() {
    env.indentation_level += 1;
  }

  void popIndentation() {
    env.indentation_level -= 1;
  }

  void stmtAssignment(int lhs, String rhs) {
    if (rhs == null) {
      J2VError.throwError("Null rhs given to stmtAssignment function");
    }
    for (int i = 0; i < env.indentation_level; i++) {
      System.out.printf("  ");
    }
    System.out.println("t." + String.valueOf(lhs) + " = " + rhs);
  }

  void stmtMemoryAccess(int lhs, String rhs) {
    if (rhs == null) {
      J2VError.throwError("Null rhs given to stmtMemoryAccess function");
    }
    for (int i = 0; i < env.indentation_level; i++) {
      System.out.printf("  ");
    }
    System.out.println("[t." + String.valueOf(lhs) + "] = " + rhs);
  }

  int obtainVarTicket() {
    env.counter_var += 1;
    return env.counter_var - 1;
  }

  int obtainLabelTicket() {
    env.counter_label += 1;
    return env.counter_label - 1;
  }

}