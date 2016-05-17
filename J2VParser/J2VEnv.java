package J2VParser;

import visitor.*;
import syntaxtree.*;
import java.util.*;

public class J2VEnv {
    
  //Used in first pass
  HashMap<String, J2VClassLayout> layout;
  Vector<String> list_classes;
  J2VClassLayout cur_class; 
  
  //Used only in second pass
  int indentation_level;
  int counter_id;
  int counter_label;
  int counter_temp;
  int counter_var;
  HashMap<Integer, VaporValue> variable_map;
  HashMap<String, Integer> identifier_map;
  
  public J2VEnv() {
    layout = new HashMap<String, J2VClassLayout>();
    list_classes = new Vector<String>();
    cur_class = null;

    indentation_level = 0;
    counter_id = 0;
    counter_label = 0;
    counter_temp = 0;
    counter_var = 0;
    variable_map = null;
    identifier_map = null;
  }

  // Class Layout Stuff
  ///////////////////////////
  void pushClass(String class_name, String parent_name) {
    cur_class = new J2VClassLayout();
    layout.put(class_name, cur_class);

    cur_class.id = class_name;
    cur_class.size = 4;

    cur_class.member_offsets = new HashMap<String, Integer>();
    cur_class.virtual_table = new HashMap<String, Integer>();
    cur_class.function_list = new Vector<String>();

    list_classes.add(class_name);
    cur_class.parent = parent_name;
  }
  
  void popClass() {
    if (cur_class == null) {
      J2VError.throwError("Did not previously initialize class before popping"); 
    }
    cur_class = null;
  }

  void pushMember(String member_name) {
    if (cur_class == null) {
      J2VError.throwError("Did not previously initialize class before adding member"); 
    }
    cur_class.member_offsets.put(member_name, cur_class.size);
    cur_class.size += 4;
  }

  void pushMethod(String method_name) {
    if (cur_class == null) {
      J2VError.throwError("Did not previously initialize class before adding method"); 
    }
    cur_class.function_list.add(method_name);
  }

  int createVirtualTable(String method_name, HashMap<String, String> function_list, HashMap<String, Integer> virtual_table) {
    J2VClassLayout cur = layout.get(method_name);
    String cur_parent = cur.parent;
    String cur_class = cur.id;
    int count_functions = 0;

    for (String cur_function : cur.function_list) {
      if (!function_list.containsKey(cur_function)) { 
        function_list.put(cur_function, cur_class); 
      }
    }
    if (cur.parent != null) {
      count_functions = createVirtualTable(cur_parent, function_list, virtual_table);
    }

    for (String cur_function : cur.function_list) {
      count_functions += 1;
      cur_class = function_list.get(cur_function); 
      virtual_table.put(cur_function, count_functions * 4); 
      System.out.println("  :" + cur_class + "." + cur_function);
    }

    return count_functions;
  }

  void createAllVirtualTables() {
    System.out.println("");
    System.out.println("");

    HashMap<String, String> function_list = null;
    HashMap<String, Integer> virtual_table = null;

    for (String cur_class : list_classes) {
      System.out.println("const vmt_" + cur_class);
      function_list = new HashMap<String, String>();
      virtual_table = layout.get(cur_class).virtual_table;
      createVirtualTable(cur_class, function_list, virtual_table);
      System.out.println("");
    }

    System.out.println("");
  }
  /////////////////////////////
  /////////////////////////////
  //Oh, how I do wish I could use my push pop notation for everything. ALAS!
  //The following will be used in J2VParser

  void startParseClass(String class_name) {
    cur_class = layout.get(class_name);
  }

  void endParseClass() {
    cur_class = null;
  }

  void startParseMethod() {
    variable_map = new HashMap<Integer, VaporValue>();
    identifier_map = new HashMap<String, Integer>();
    counter_var = 0;
    counter_temp = 0;
    counter_label = 0;
  }

  void endParseMethod() {
    variable_map = null;
    identifier_map = null;
    counter_var = 0;
    counter_temp = 0;
    counter_label = 0;
  }


  //Methods to support environment variable operations
  ///////////////////////

  int obtainVarNumber() {
    counter_var += 1;
    return counter_var - 1;
  }

  int obtainTempNumber() {
    counter_temp += 1;
    return counter_temp - 1;
  }

  int obtainLabelNumber() {
    counter_label += 1;
    return counter_label - 1;
  }

  //Methods to handle the adding of new identifiers
  ///////////////////
  int getIdentifier(String identifier) {

    Integer out = identifier_map.get(identifier);
    int _ret = -1;
    int ticket = 0;

    if (out == null) {
      ticket = obtainVarNumber(); 
      VaporValue v = new VaporValue(identifier);
      variable_map.put(ticket, v);
      identifier_map.put(identifier, ticket);
      _ret = ticket;
    } else {
      _ret = out;
    }
    return _ret;
  }
  
  int getTemporary() {
    int ticket = obtainVarNumber();
    int temp = obtainTempNumber();

    VaporValue v = new VaporValue("t." + String.valueOf(temp)); 
    variable_map.put(ticket, v);
    return ticket;
  }

  int getLabel() {
    int ticket = obtainVarNumber();
    int temp = obtainLabelNumber();

    VaporValue v = new VaporValue("control" + String.valueOf(temp));  
    variable_map.put(ticket, v);
    return ticket;
  }

  String findVariableEnv(int ticket) {
    return variable_map.get(ticket).identifier;
  }

  int findTicketNumEnv(String identifier) {
    int _ret = 0;;
    System.out.println(identifier);
    System.out.println(identifier_map);
    Integer out = identifier_map.get(identifier);
    if (out == null) {
      _ret = -1;
    } else {
      _ret = out;
    }
    return out;
  }


  //////////////////////
}



class J2VClassLayout {
  String id;
  String parent;
  int size;

  Vector<String> function_list;
  HashMap<String, Integer> virtual_table;
  HashMap<String, Integer> member_offsets;
}

class VaporValue {
  String identifier;
  VaporValue(String input) {
    identifier = input;
  }
}
