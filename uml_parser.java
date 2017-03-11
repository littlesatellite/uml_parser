package parser;

import java.io.*;
import java.util.*;
import java.lang.*;

import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

public class uml_parser {
      static String inPath;
      static String outPath;
      HashMap<String, Boolean> map;
      HashMap<String, String> mapClass;
      
      //String toUML;
      
//      uml_parser(String inPath, String outFile){
//    	  this.inPath = inPath;
//    	  this.outPath = inPath + "\\" + outFile + ".png";
//    	  map = new HashMap<String,>
//    	  
//      }
      
      inPath = /Users/jinchengcheng/Desktop/umlparser/uml-parser-test-1;
      
      public static void main(String[] args){
    	 System.out.println(getCuArray(inPath));
      }

      private ArrayList<CompilationUnit> getCuArray(String inPath)
    	  throws Exception {
    	File folder = new File(inPath);
    	ArrayList<CompilationUnit> cuArray = new ArrayList<CompilationUnit>();
    	 for(final File f : folder.listFiles()){
    		 if(f.isFile() && f.getnName().endsWith(".java")){
    			 FileInputStream in = new FileInputStream(f);
    			 CompilationUnit cu;
    			 try{
    				 cu = JavaParser.parse(in);
    				 cuArray.add(cu);
    			 }finally{
    				 in.close();
    			 }
    		 }
    	 }
    	return cuArray;  
      }
}
    
    
