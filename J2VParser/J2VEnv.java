package J2VParser;

import visitor.*;
import syntaxtree.*;
import java.util.*;

public class J2VEnv {
    
  HashMap<String, J2VClassLayout> layout;
  Vector<String> list_classes;
  J2VClassLayout cur_class; 
  
  public J2VEnv() {
    layout = new HashMap<String, J2VClassLayout>();
    list_classes = new Vector<String>();
    cur_class = null;
  }

  // Class Layout Stuff
  ///////////////////////////
  void pushClass(String class_name, String parent_name) {
    cur_class = new J2VClassLayout();
    layout.put(class_name, cur_class);

    cur_class.id = class_name;
    cur_class.size = 4;

    cur_class.member_offsets = new HashMap<String, Integer>();
    cur_class.virtual_table = new Vector<String>();

    list_classes.add(class_name);
    cur_class.parent = parent_name;
  }
  
  void popClass() {
    if (cur_class == null) {
      J2VError.throwError("Did not previously initialize class before popping"); 
    }
    cur_class = null;
  }

  void addMember(String member_name) {
    if (cur_class == null) {
      J2VError.throwError("Did not previously initialize class before adding member"); 
    }
    cur_class.member_offsets.put(member_name, cur_class.size);
    cur_class.size += 4;
  }

  void addMethod(String method_name) {
    if (cur_class == null) {
      J2VError.throwError("Did not previously initialize class before adding method"); 
    }
    cur_class.virtual_table.add(method_name);
  }

  void createVirtualTable(String method_name, HashMap<String, String> function_list) {
    J2VClassLayout cur = layout.get(method_name);
    String cur_parent = cur.parent;
    String cur_class = cur.id;
    Iterator itr = cur.virtual_table.iterator();

    for (String cur_function : cur.virtual_table) {
      if (!function_list.containsKey(cur_function)) { 
        function_list.put(cur_function, cur_class); 
      }
    }
    if (cur.parent != null) {
      createVirtualTable(cur_parent, function_list);
    }

    for (String cur_function : cur.virtual_table) {
      cur_class = function_list.get(cur_function); 
      System.out.println("  :" + cur_class + "." + cur_function);
    }
  }

  void createAllVirtualTables() {
    System.out.println("");
    System.out.println("");

    HashMap<String, String> function_list = null;

    for (String cur_class : list_classes) {
      System.out.println("const vmt_" + cur_class);
      function_list = new HashMap<String, String>();
      createVirtualTable(cur_class, function_list);
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

  Vector<String> virtual_table;
  HashMap<String, Integer> member_offsets;
}
