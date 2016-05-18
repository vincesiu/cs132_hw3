package J2VParser;

import syntaxtree.*;
import visitor.*;
import java.util.*;

public class J2VParser extends GJNoArguDepthFirst<Integer> {

  J2VEnv env;

  public J2VParser(J2VEnv input) {
    env = input;
  }

  /**
   * f0 -> MainClass()
   * f1 -> ( TypeDeclaration() )*
   * f2 -> <EOF>
   */
  public Integer visit(Goal n) {
    Integer _ret=null;
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
  public Integer visit(MainClass n) {
    Integer _ret=null;

    String class_name = n.f1.f0.toString();
    env.startParseClass(class_name);
    env.startParseMethod();

    stmtMethodParamStart(class_name, "main");
    stmtMethodParamEnd(); 

    pushIndentation();

    n.f14.accept(this);
    n.f15.accept(this);


    indentVapor();
    System.out.println("ret");

    popIndentation();
    env.endParseMethod();
    env.endParseClass();


    return _ret;
  }

  /**
   * f0 -> ClassDeclaration()
   *       | ClassExtendsDeclaration()
   */
  public Integer visit(TypeDeclaration n) {
    Integer _ret=null;
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
  public Integer visit(ClassDeclaration n) {
    Integer _ret=null;

    String class_name = n.f1.f0.toString();
    env.startParseClass(class_name);

    n.f4.accept(this);

    env.endParseClass();
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
  public Integer visit(ClassExtendsDeclaration n) {
    Integer _ret=null;

    String class_name = n.f1.f0.toString();
    env.startParseClass(class_name);

    n.f6.accept(this);

    env.endParseClass();

    return _ret;
  }

  /**
   * f0 -> Type()
   * f1 -> Identifier()
   * f2 -> ";"
   */
  public Integer visit(VarDeclaration n) {
    Integer _ret=null;
    String type = n.f0.accept(new J2VHack());
    int ticket = n.f1.accept(this);
    
    env.variable_map.get(ticket).class_name = type;
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
  public Integer visit(MethodDeclaration n) {
    Integer _ret=null;

    System.out.println("");
    String method_name = n.f2.f0.toString();
    String class_name = env.cur_class.id;

    env.startParseMethod();
    stmtMethodParamStart(class_name, method_name);
    n.f4.accept(this);
    stmtMethodParamEnd();


    pushIndentation();
    n.f7.accept(this);
    n.f8.accept(this);
    int a = n.f10.accept(this);
    indentVapor();
    System.out.println("ret " + env.findVariableEnv(a));

    popIndentation();
    env.endParseMethod();

    return _ret;
  }

  /**
   * f0 -> FormalParameter()
   * f1 -> ( FormalParameterRest() )*
   */
  public Integer visit(FormalParameterList n) {
    Integer _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    return _ret;
  }

  /**
   * f0 -> Type()
   * f1 -> Identifier()
   */
  public Integer visit(FormalParameter n) {
    Integer _ret=null;
    String type = n.f0.accept(new J2VHack());
    String parameter_name = n.f1.f0.toString();
    stmtMethodParamParameter(parameter_name, type);

    return _ret;
  }

  /**
   * f0 -> ","
   * f1 -> FormalParameter()
   */
  public Integer visit(FormalParameterRest n) {
    Integer _ret=null;
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
  public Integer visit(Type n) {
    Integer _ret=null;
    n.f0.accept(this);
    return _ret;
  }

  /**
   * f0 -> "int"
   * f1 -> "["
   * f2 -> "]"
   */
  public Integer visit(ArrayType n) {
    Integer _ret=null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return _ret;
  }

  /**
   * f0 -> "boolean"
   */
  public Integer visit(BooleanType n) {
    Integer _ret=null;
    n.f0.accept(this);
    return _ret;
  }

  /**
   * f0 -> "int"
   */
  public Integer visit(IntegerType n) {
    Integer _ret=null;
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
  public Integer visit(Statement n) {
    Integer _ret=null;
    n.f0.accept(this);
    return _ret;
  }

  /**
   * f0 -> "{"
   * f1 -> ( Statement() )*
   * f2 -> "}"
   */
  public Integer visit(Block n) {
    Integer _ret=null;
    n.f1.accept(this);
    return _ret;
  }

  /**
   * f0 -> Identifier()
   * f1 -> "="
   * f2 -> Expression()
   * f3 -> ";"
   */
  public Integer visit(AssignmentStatement n) {
    Integer _ret=null;
    String identifier = n.f0.f0.toString();
    Integer a = n.f2.accept(this);
    int ticket = env.getIdentifier(identifier);

    VaporValue v1 = env.variable_map.get(ticket);
    VaporValue v2 = env.variable_map.get(a);
    if (v1.class_name != null ) {
      v1.class_name = v2.class_name;
    }
    
    
    stmtAssignment(ticket, env.findVariableEnv(a)); 
    
    _ret = ticket;
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
  public Integer visit(ArrayAssignmentStatement n) {
    Integer _ret=null;
    int a = n.f0.accept(this);
    int b = n.f2.accept(this);
    int c = n.f5.accept(this);
    int ticket1 = env.getTemporary();
    int ticket2 = env.getTemporary();
    int ticket3 = env.getTemporary();
    int ticket4 = env.getTemporary();
    int ticket5 = env.getTemporary();
    int ticket6 = env.getTemporary();
    int control1 = env.getLabel();
    
    //ticket1 = [a]
    //ticket2 = LtS(b ticket1) //b < ticket1
    //if ticket2 goto: control1
    //  Error("Array out of bounds")
    //control1: 
    //ticket3 = b * 4
    //ticket4 = ticket3 + a
    //ticket5 = ticket4 + 4
    //[ticket5] = c
    stmtMemoryAccess(ticket1, env.findVariableEnv(a));
    stmtAssignment(ticket2, "LtS(" + env.findVariableEnv(b) + " " + env.findVariableEnv(ticket1) + ")");
    stmtIfGoto(ticket2, control1);
    pushIndentation();
    indentVapor();
    System.out.println("Error(\"Array out of bounds\")");
    popIndentation();
    stmtLabel(control1);
    stmtAssignment(ticket3, "MulS(" + env.findVariableEnv(b) + " 4)");
    stmtAssignment(ticket4, "Add(" + env.findVariableEnv(a) + " " + env.findVariableEnv(ticket3) + ")");
    stmtAssignment(ticket5, "Add(" + env.findVariableEnv(ticket4) + " 4)");
    stmtMemoryAssignment(ticket5, env.findVariableEnv(c));

    _ret = c;
  



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
  public Integer visit(IfStatement n) {
    Integer _ret=null;


    int a = n.f2.accept(this);
    int control1 = env.getLabel();
    int control2 = env.getLabel();

    stmtIf0Goto(a, control1);

    pushIndentation();
    n.f4.accept(this);
    stmtGoto(control2);

    popIndentation();

    stmtLabel(control1);
    pushIndentation();
    n.f6.accept(this);
    popIndentation();
    stmtLabel(control2);

    return _ret;
  }

  /**
   * f0 -> "while"
   * f1 -> "("
   * f2 -> Expression()
   * f3 -> ")"
   * f4 -> Statement()
   */
  public Integer visit(WhileStatement n) {
    Integer _ret=null;

    int control1 = env.getLabel();
    int control2 = env.getLabel();

    //Initial conditional tag
    stmtLabel(control1);
    //Jump to end
    int a = n.f2.accept(this);
    stmtIf0Goto(a, control2);
    //Main Loop code
    n.f4.accept(this);
    //Jump to conditional
    stmtGoto(control1);
    /*
    indentVapor();
    System.out.println("if0 " + env.findVariableEnv(a) + " goto :" + env.findVariableEnv(control2));
    n.f3.accept(this);

    indentVapor();
    System.out.println("goto :" + env.findVariableEnv(control1));
    */

    stmtLabel(control2);

    return _ret;
  }

  /**
   * f0 -> "System.out.println"
   * f1 -> "("
   * f2 -> Expression()
   * f3 -> ")"
   * f4 -> ";"
   */
  public Integer visit(PrintStatement n) {
    Integer _ret=null;
    int a = n.f2.accept(this);
    indentVapor();
    System.out.println("PrintIntS(" + env.findVariableEnv(a) + ")");
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
  public Integer visit(Expression n) {
    Integer _ret=null;
    _ret = n.f0.accept(this);
    return _ret;
  }

  /**
   * f0 -> PrimaryExpression()
   * f1 -> "&&"
   * f2 -> PrimaryExpression()
   */
  public Integer visit(AndExpression n) {
    Integer _ret=null;
    //Pseudocode
    //ticket1 = Eq(1 a)
    //ticket2 = Eq(1 a)
    //if0 ticket1 goto :control1
    //  if0 ticket2 goto :control1
    //    ticket3 = 1
    //goto: control2
    //control1: 
    //  ticket3 = 0
    //control2:
    int a = n.f0.accept(this);
    int b = n.f2.accept(this);
    int ticket1 = env.getTemporary();
    int ticket2 = env.getTemporary();
    int ticket3 = env.getTemporary();
    int control1 = env.getLabel();
    int control2 = env.getLabel();
    stmtAssignment(ticket1, "Eq(1 " + env.findVariableEnv(a) + ")");
    stmtAssignment(ticket2, "Eq(2 " + env.findVariableEnv(b) + ")");

    stmtIf0Goto(ticket1, control1);
    pushIndentation();
    stmtIf0Goto(ticket2, control1);
    pushIndentation();
    stmtAssignment(ticket3, "1");
    popIndentation();
    popIndentation();
    stmtGoto(control2);
    stmtLabel(control1);
    pushIndentation();
    stmtAssignment(ticket3, "0");
    popIndentation();
    stmtLabel(control2);

     
    _ret = ticket3;
    return _ret;
  }

  /**
   * f0 -> PrimaryExpression()
   * f1 -> "<"
   * f2 -> PrimaryExpression()
   */
  public Integer visit(CompareExpression n) {
    Integer _ret=null;
    int a = n.f0.accept(this);
    int b = n.f2.accept(this);
    int ticket = env.getTemporary();
    stmtAssignment(ticket, "LtS(" + env.findVariableEnv(a) + " " + env.findVariableEnv(b) + ")");

    _ret = ticket;

    


    return _ret;
  }

  /**
   * f0 -> PrimaryExpression()
   * f1 -> "+"
   * f2 -> PrimaryExpression()
   */
  public Integer visit(PlusExpression n) {
    Integer _ret=null;
    int a = n.f0.accept(this);
    int b = n.f2.accept(this);
    int ticket = env.getTemporary();

    stmtAssignment(ticket, "Add(" + env.findVariableEnv(a) + " " + env.findVariableEnv(b) + ")");
    _ret = ticket;
    return _ret;
  }

  /**
   * f0 -> PrimaryExpression()
   * f1 -> "-"
   * f2 -> PrimaryExpression()
   */
  public Integer visit(MinusExpression n) {
    Integer _ret=null;
    int a = n.f0.accept(this);
    int b = n.f2.accept(this);
    int ticket = env.getTemporary();

    stmtAssignment(ticket, "Sub(" + env.findVariableEnv(a) + " " + env.findVariableEnv(b) + ")");
    _ret = ticket;
    return _ret;
  }

  /**
   * f0 -> PrimaryExpression()
   * f1 -> "*"
   * f2 -> PrimaryExpression()
   */
  public Integer visit(TimesExpression n) {
    Integer _ret=null;
    
    int a = n.f0.accept(this);
    int b = n.f2.accept(this);
    int ticket = env.getTemporary();

    stmtAssignment(ticket, "MulS(" + env.findVariableEnv(a) + " " + env.findVariableEnv(b) + ")");
   
    _ret = ticket;
    return _ret;
  }

  /**
   * f0 -> PrimaryExpression()
   * f1 -> "["
   * f2 -> PrimaryExpression()
   * f3 -> "]"
   */
  public Integer visit(ArrayLookup n) {
    Integer _ret=null;
    int a = n.f0.accept(this);
    int b = n.f2.accept(this);
    int ticket1 = env.getTemporary();
    int ticket2 = env.getTemporary();
    int ticket3 = env.getTemporary();
    int ticket4 = env.getTemporary();
    int ticket5 = env.getTemporary();
    int control1 = env.getLabel();
       

    //pseudocode
    //ticket1 = [a]
    //ticket2 = LtS(b ticket1) // b < ticket1, this is true if it isssss out of bounds
    //if ticket2 goto: control1
    //  Error("Array out of bounds")
    //control1:
    //ticket3 = b * 4
    //ticket4 = a + ticket3
    //ticket5 = [ticket4+4]
    
    stmtMemoryAccess(ticket1, env.findVariableEnv(a));
    stmtAssignment(ticket2, "LtS(" + env.findVariableEnv(b) + " " + env.findVariableEnv(ticket1) + ")");
    stmtIfGoto(ticket2, control1);
    pushIndentation();
    stmtPrint("Error(\"Array out of bounds\")");
    popIndentation();
    stmtLabel(control1);
    stmtAssignment(ticket3, "MulS(" + env.findVariableEnv(b) + " 4)");
    stmtAssignment(ticket4, "Add(" + env.findVariableEnv(a) + " " + env.findVariableEnv(ticket3) + ")");
    stmtMemoryAccess(ticket5, env.findVariableEnv(ticket4) + "+4");

    _ret = ticket5;
    return _ret;
  }

  /**
   * f0 -> PrimaryExpression()
   * f1 -> "."
   * f2 -> "length"
   */
  public Integer visit(ArrayLength n) {
    Integer _ret=null;

    int a = n.f0.accept(this);
    int ticket = env.getTemporary();
    stmtMemoryAccess(ticket, env.findVariableEnv(a));

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
  public Integer visit(MessageSend n) {
    Integer _ret=null;

    int a = n.f0.accept(this);
    //Error Checking Pseudocode:
    ////////////////////
    //if a goto control1
    //  Error("null pointer")
    //control1:
    int control1 = env.getLabel();

    stmtIfGoto(a, control1);
    pushIndentation();
    stmtPrint("Error(\"null pointer\")");
    popIndentation();
    stmtLabel(control1);
    


    //Normal Code
    ////////////////////////
    int ticket1 = env.getTemporary();
    int ticket2 = env.getTemporary();

    String function_name = n.f2.f0.toString();
    String class_name = null;
    
    //to get the correct classname of the function
    if (a == 0) {
      class_name = env.cur_class.id; 
    } else {
      class_name = env.variable_map.get(a).class_name;
    }


   
    /*
    System.out.println(a);
    System.out.println(env.variable_map.keySet());
    for (Integer v : env.variable_map.keySet()) {
      String RAND = env.variable_map.get(v).identifier;
      
      System.out.printf(RAND + "   :   ");

      System.out.println(env.variable_map.get(v).class_name);

    }
    System.out.println(class_name);
    System.out.println(env.layout.get(class_name));
    System.out.println(env.layout.get(class_name).virtual_table);
    */
    


    J2VClassLayout class_layout = env.layout.get(class_name);
    int offset = class_layout.virtual_table.get(function_name);
    String method_type = class_layout.method_types.get(function_name);

    //to get the function name in ticket1
    stmtMemoryAccess(ticket1, env.findVariableEnv(a));
    stmtMemoryAccess(ticket1, env.findVariableEnv(ticket1) + "+" + String.valueOf(offset));

    
      
    env.call_list.push(env.call_parameters);
    env.call_parameters = new Vector<Integer>();
    n.f4.accept(this);

    String parameters = "";

    for (Integer ticket_param : env.call_parameters) {
      parameters += " ";
      parameters += env.findVariableEnv(ticket_param);
    }

    stmtAssignment(ticket2, "call " + env.findVariableEnv(ticket1) + "(" + env.findVariableEnv(a) + parameters + ")");

    env.call_parameters = env.call_list.pop();

    VaporValue v = env.variable_map.get(ticket2);
    v.class_name = method_type;

    _ret = ticket2;
    return _ret;
  }

  /**
   * f0 -> Expression()
   * f1 -> ( ExpressionRest() )*
   */
  public Integer visit(ExpressionList n) {
    Integer _ret=null;
    Integer a = n.f0.accept(this);
    env.call_parameters.add(a);
    n.f1.accept(this);
    return _ret;
  }

  /**
   * f0 -> ","
   * f1 -> Expression()
   */
  public Integer visit(ExpressionRest n) {
    Integer _ret=null;
    Integer a = n.f1.accept(this);
    env.call_parameters.add(a);
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
  public Integer visit(PrimaryExpression n) {
    Integer _ret=null;
    _ret = n.f0.accept(this);
    return _ret;
  }

  /**
   * f0 -> <INTEGER_LITERAL>
   */
  public Integer visit(IntegerLiteral n) {
    Integer _ret=null;

    int ticket = env.getTemporary(); 

    stmtAssignment(ticket, n.f0.toString());

    _ret = ticket;
    return _ret;
  }

  /**
   * f0 -> "true"
   */
  public Integer visit(TrueLiteral n) {
    Integer _ret=null;

    int ticket = env.getTemporary(); 
    stmtAssignment(ticket, "1");
    _ret = ticket;
    return _ret;
  }

  /**
   * f0 -> "false"
   */
  public Integer visit(FalseLiteral n) {
    Integer _ret=null;

    int ticket = env.getTemporary(); 
    stmtAssignment(ticket, "0");
    _ret = ticket;
    return _ret;
  }

  /**
   * f0 -> <IDENTIFIER>
   */
  public Integer visit(Identifier n) {
    Integer _ret = env.getIdentifier(n.f0.toString());
    return _ret;
  }

  /**
   * f0 -> "this"
   */
  public Integer visit(ThisExpression n) {
    Integer _ret=null;
    _ret = 0;
    return _ret;
  }

  /**
   * f0 -> "new"
   * f1 -> "int"
   * f2 -> "["
   * f3 -> Expression()
   * f4 -> "]"
   */
  public Integer visit(ArrayAllocationExpression n) {
    Integer _ret=null;
    int a = n.f3.accept(this);

    int ticket1 = env.getTemporary();
    int ticket2 = env.getTemporary();
    int ticket3 = env.getTemporary();

    //Assigning the size in position 1 as the size of only the data values, and not metadata
    stmtAssignment(ticket1, "MulS(" + env.findVariableEnv(a) + " 4)");
    stmtAssignment(ticket2, "Add(" + env.findVariableEnv(ticket1) + " 4)");
    stmtAssignment(ticket3, "HeapAllocZ(" + env.findVariableEnv(ticket2) + ")");
    stmtMemoryAssignment(ticket3, env.findVariableEnv(a));
     
    _ret = ticket3;

    return _ret;
  }

  /**
   * f0 -> "new"
   * f1 -> Identifier()
   * f2 -> "("
   * f3 -> ")"
   */
  public Integer visit(AllocationExpression n) {
    Integer _ret=null;
    int ticket = 0;
    String class_name = n.f1.f0.toString();

    J2VClassLayout class_layout = env.layout.get(class_name);

    ticket = env.getTemporary(); 

    VaporValue v = env.variable_map.get(ticket);
    v.class_name = class_name;

    stmtAssignment(ticket, "HeapAllocZ(" + class_layout.size + ")");
    stmtMemoryAssignment(ticket, ":vmt_" + class_layout.id);

    _ret = ticket;
    return _ret;
  }

  /**
   * f0 -> "!"
   * f1 -> Expression()
   */
  public Integer visit(NotExpression n) {
    Integer _ret=null;
    //TODO check this?
    int a = n.f1.accept(this);
    int ticket = env.getTemporary();
    stmtAssignment(ticket, "LtS(" + env.findVariableEnv(a) + " 1)");
    _ret = ticket;
    return _ret;
  }

  /**
   * f0 -> "("
   * f1 -> Expression()
   * f2 -> ")"
   */
  public Integer visit(BracketExpression n) {
    Integer _ret=null;
    _ret = n.f1.accept(this);
    return _ret;
  }

  //////////////////////////
  void stmtMethodParamStart(String class_name, String function_name) {
    if (function_name.equals("main")) {
      System.out.printf("func Main(");
    } else {
      System.out.printf("func " + class_name + "." + function_name + "(this");
    }
  }
  
  void stmtMethodParamParameter(String parameter_name, String parameter_type) {
    System.out.printf(" " + parameter_name);
    int ticket = env.getIdentifier(parameter_name);
    env.variable_map.get(ticket).class_name = parameter_type; 
  }

  void stmtMethodParamEnd() {
    System.out.printf(")\n");
  }

  //////////////////////////////
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
    indentVapor();
    System.out.println(env.findVariableEnv(lhs) +  " = " + rhs);
  }

  void stmtMemoryAssignment(int lhs, String rhs) {
    if (rhs == null) {
      J2VError.throwError("Null rhs given to stmtMemoryAssignment function");
    }
    indentVapor();
    System.out.println("[" + env.findVariableEnv(lhs) + "] = " + rhs);
  }

  void stmtMemoryAccess(int lhs, String rhs) {
    if (rhs == null) {
      J2VError.throwError("Null rhs given to stmtMemoryAccess function");
    }
    indentVapor();
    System.out.println(env.findVariableEnv(lhs) + " = [" + rhs + "]");
  }

  void stmtLabel(int label) {
    indentVapor();
    System.out.println(env.findVariableEnv(label) + ":");
  }

  void stmtIf0Goto(int ticket, int label) {
    indentVapor();
    System.out.println("if0 " + env.findVariableEnv(ticket) + " goto :" + env.findVariableEnv(label));
  }

  void stmtIfGoto(int ticket, int label) {
    indentVapor();
    System.out.println("if " + env.findVariableEnv(ticket) + " goto :" + env.findVariableEnv(label));
  }

  void stmtGoto(int label) {
    indentVapor();
    System.out.println("goto :" + env.findVariableEnv(label));
  }

  void stmtPrint(String input) {
    indentVapor();
    System.out.println(input);
  }

  void indentVapor() {
    for (int i = 0; i < env.indentation_level; i++) {
      System.out.printf("  ");
    }
  }
}
