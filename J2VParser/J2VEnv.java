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
  int counter_var;
  int counter_label;
  
  public J2VEnv() {
    layout = new HashMap<String, J2VClassLayout>();
    list_classes = new Vector<String>();
    cur_class = null;

    indentation_level = 0;
    counter_var = 0;
    counter_label = 0;
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
}



class J2VClassLayout {
  String id;
  String parent;
  int size;

  Vector<String> function_list;
  HashMap<String, Integer> virtual_table;
  HashMap<String, Integer> member_offsets;
}
