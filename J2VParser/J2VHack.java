package J2VParser;

import visitor.*;
import syntaxtree.*;
import java.util.*;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class J2VHack extends GJNoArguDepthFirst<String> {

   /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
   public String visit(Type n) {
      String _ret=null;
      _ret = n.f0.accept(this);
      return _ret;
   }


   /**
    * f0 -> <IDENTIFIER>
    */
   public String visit(Identifier n) {
      String _ret=null;
      _ret = n.f0.toString();
      return _ret;
   }


}
