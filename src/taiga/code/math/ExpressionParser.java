/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.math;

import java.util.Stack;
import java.util.logging.Logger;

public class ExpressionParser {

  public ExpressionParser() {
    this.stack = new Stack<>();
  }
  
  public void clear() {
    stack.clear();
  }
  
  public void parseExprestion(String exp) {
    Stack<Element> operators = new Stack<>();
    
    String token = null;
    for(index = 0; index < exp.length(); token = getNextToken(exp)) {
      //check for an error in the token.
      if(token == null) {
        throw new RuntimeException("Invalid expression: " + exp);
      } else if(token.isEmpty()) break;
      
      //sort the token into its correct lists
      //first check for numbers
      if(Character.isDigit(token.charAt(0))) {
        Element val = new Element(Integer.parseInt(token));
        
        stack.add(val);
      //then for functions
      } else if(token.length() > 1) {
        if(!isFunction(token)) throw new RuntimeException("Unknown function: " + token);
        
        operators.add(getFunction(token));
        
      //now for any variables
      } else if(Character.isLetter(token.charAt(0))) {
        Element ele = new Element(Type.variable);
        ele.value = token.charAt(0);
      //and parenthesis
      } else if(token.charAt(0) == '(') {
        operators.add(new Element(Type.parens));
      } else if(token.charAt(0) == ')') {
        //match the parenthesis
        for(Element e = operators.pop(); e.type != Type.parens; e = operators.pop()){
          if(operators.isEmpty()) throw new RuntimeException("Mismatched parenthesis in expression: " + exp);
          stack.add(e);
        }
      //what ever is left over must be an operator or invalid
      } else {
        stack.add(new Element(token.charAt(index)));
      }
    }
    
    condense();
  }
  
  public boolean isFunction(String func) {
    return getFunction(func) != null;
  }
  
  private Element getFunction(String func) {
    func = func.trim().toLowerCase();
    switch(func) {
      case "sin": return new Element(Type.sin);
      case "cos": return new Element(Type.sin);
      case "arcsin":
      case "asin": return new Element(Type.asin);
      case "arccos":
      case "acos": return new Element(Type.acos);
    }
    
    return null;
  }
  
  private void condense() {
    Stack<Element> nstack = new Stack<>();
    
    while(!stack.isEmpty()) {
      Element next = stack.pop();
      
      if(next.type == Type.number) nstack.add(next);
      else if(next.type == Type.variable) nstack.add(next);
      //binary operators are the first type to have non trivial processing
      else if(next.type == Type.operator) {
        Element right = nstack.pop();
        Element left = (nstack.isEmpty() ? null : nstack.pop());
        
        //only the - operator can be unary
        if(left == null && next.value != '-') throw new RuntimeException("Invalid operator in expression");
        
        
      }
    }
  }
  
  private String getNextToken(String str) {
    //get rid of any whitespace characters
    char s = str.charAt(index);
    while(Character.isWhitespace(s) && index < str.length()) {
      index++;
      s = str.charAt(index);
    }
    
    //check to see if this is the start of a variable or function.
    if(Character.isLetter(s)) {
      //check if this is the last character
      if(str.length() == index + 1) {
        index++;
        return Character.toString(s);
      } else if(Character.isLetterOrDigit(str.charAt(index + 1))) {
        //find the end of the word
        int end = index;
        for(;end < str.length() && Character.isWhitespace(str.charAt(end)); end++){}
        
        String output = str.substring(index, end);
        index = end;
        return output;
      } else {
        index++;
        return Character.toString(s);
      }
    //now check for numbers
    } else if(Character.isDigit(s)) {
      //find the end of the number
      int end = index;
      for(;end < str.length() && Character.isDigit(str.charAt(end)); end++){}
      
      String output = str.substring(index, end);
      index = end;
      return output;
    } else if(Character.isWhitespace(s)) {
      index++;
      return "";
    } else if(Character.isValidCodePoint(s)) {
      index++;
      return Character.toString(s);
    }
    
    return null;
  }

  private int index;
  private final Stack<Element> stack;
  
  private static class Element{
    public Type type;
    public int value;

    public Element() {
    }

    public Element(char c) {
      this.type = Type.operator;
      this.value = c;
    }
    
    public Element(int value) {
      type = Type.number;
      this.value = value;
    }
    
    public Element(Type t) {
      type = t;
    }
  }
  
  private static enum Type {
    number,
    operator,
    parens,
    variable,
    sin,
    cos,
    acos,
    asin
  }
  
  private static final String locprefix = ExpressionParser.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
