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
            
            ArrayList<String> 
            
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
                                    makeFieldPublic.add(varName.toLowerCase());
                              }else{
                                    if(next)
                                          methods += ";";
                                    methods += "+" + md.getName() + "(";
                                    for(Object gcn : md.getChildrenNode()){
                                          if(gcn instanceof Parameter){
                                                Parameter param = (Parameter) gcn;
                                                String PClass = paramCast.getType().toString();
                                                String PName = paramCast.getTChildrenNodes().get(0).toString();
                                                methods += PName + " : " PClass;
                                                if(map.containsKey(PClass) && !map.get(classShortName)){
                                                      add += "[" + classShortName + "] uses -.->";
                                                      if(map.get(PClass))
                                                            add += "[<<interface>>;" + PClass + "]";
                                                      else 
                                                            add += "[" + PClass + "]";
                                                }
                                                add += ",";
                      
                                          } else {
                                                String methodBody[] = gcn.toString().split(" ");
                                                for(String foo : methodBody){
                                                      if(map.containskey( body : methodBody){
                                                            if(map.containskey(foo) && !map.get(classShortName)){
                                                                  add += "[" + classShortName + "] uses -.->";
                                                                  if(mao.get(foo))
                                                                        add += "[<<interface>>;" + body + "]";
                                                                  else
                                                                        add += "[" + body + "]";
                                                                  add += ",";
                                                            }
                                                      }
                                                         }
                                                         }
                                                         methods += " ) : " + md.getType();
                                                         next = true;
                                                         }
                                                         }
                                                         }
                                                         }
                                                         
                                                       boolean nextField = false;
                                                         for(BodyDeclaration bd : ((TyoeDeclaration) node).getMembers()){
                                                               if(bd instanceof FieldDeclaration){
                                                                     FieldDeclaration fd = ((Fielddeclaration) bd);
                                                                     String fieldScope = aToSymScope(
                                                                           bd.toStringWithoutComments().substring(0,bd.toStringWithoutComments()indexOf(" ")));
                                                                     String fieldClass = changeBrackets(fd.getType().toString());
                                                                     String fieldName = fd.getChildrenNodes().get(1).toString();
                                                                  if(fieldScope.equals("-") && makeFieldPublic.contains(fieldName.toLowerCase())){
                                                                      fiedScope = "+";
                                                                  }
                                                                     String getDepen = "";
                                                                     boolean getDepenMultiple = "false";
                                                                     if(fieldClass.contains("(")){
                                                                           getDepen = fieldClass.substring(fieldclass.indexOf("(") + 1,fieldClass.indexOf(")"));
                                                                           getDepenMultiple = true;
                                                                     }
                                                                     else if (map.containsKey(fieldClass)){
                                                                           getDepen = fieldClass;
                                                                     }
                                                                     if(getDepen.length() > 0 && map.containsKey(getDepen)){
                                                                           String connection = "-";
                                                                           
                                                                           if(mapClassConn.containsKey(getDepen + "-" + classShortName)){
                                                                                 connection = mapClassConn.get(
                                                     
                                                
                    
                                    
             
             
            
            
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
    
    
