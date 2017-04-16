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
      String result;
      ArrayList<CompilationUnit> CuArray;
      
      String toUML;
      
     uml_parser(String inPath, String outFile){
           result = "";
   	  this.inPath = inPath;
   	  this.outPath = inPath + "\\" + outFile + ".png";
   	  map = new HashMap<String, Boolean>();
        mapClass =  new HashMap<String,String>();
     }
      
     public void start() throws Exception {
           cuArray = getCuArray(inpath);
           yumlCode(cuArray);
           for(CompilationUnit cu : cuArray)
                 result +
     }
      
      private String parser(CompilationUnit current){
            String result = "";
            String className = "";
            String classNameWithoutNotaion = "";
            String methods = "";
            String fields = "";
            String add = ",";
            
           ClassOrInterfaceDeclaration cl = (ClassOrInterfaceDeclaration) node;
             
            if (cl.isInterface()) {
            
            className = "[" + "<<interface>>;";
          
            }
            else{
            
            className = "[";
            
            }
            className += cl.getName();
            
         
            boolean next = false;
            for(BodyDeclaration bd : ((TypeDeclaration) node).getMembers()){
                  if(bd instanceof ConstructorDeclaration){
                        ConstructorDeclaration cd = ((ConstructorDeclaration) bd);
                        if(cd.getDeclarationAsString().startWith("public")&& !cl.isInterface()){
                              if(next){
                              methods += ";";
                              }
                              methods += "+ " + cd.getName() + "(";
                              for(Object gcn : cd.getChildrenNodes()){
                                    if(gcn instanceif Parameter){
                                          Parameter para = (Parameter) gcn;
                                          String PClass = para.getType().toString();
                                          String PName = Para.getChildrenNodes().get(0).toString();
                                          methods += PName + " : " + PClass;
                                          if(map.containsKey(PClass) && !map.get(classNameWithoutNotation))
                                          { add += "[" + classShortName + "] uses -.->";
                                           if(map.get(PClass))
                                                 add += "[<<interface>>;" + PClass + "]";
                                           else
                                                 add += "[" + PClass + "]";
                                          }
                                          add += ",";
                                    }
                              }
                              methods += ")";
                              next = true;
                        }
                  }
            }
            for(BodyDeclaration bd : ((TypeDeclaration) node).getMembers()){
                  if(bd instanceof MethodDeclaration){
                        MethodDeclaration md = ((MethodDeclaration) bd);
                        if(md.getDeclarationAsString().startWith("public")&&!cl.isInterface()){
                              if(md.getName().startWith("set")|| md.getName().startWith("get")){
                                    String varName = md.getName().substring(3);
                                    
             
             
            
            
      }
      
      @SuppressWarnings("unused")//keep silence of some actions of the compiler
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
    
    
