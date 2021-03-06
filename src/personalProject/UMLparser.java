package personalProject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.ModifierSet;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.body.VariableDeclaratorId;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.VoidType;




public class UMLparser {

	//set file path according to the args
		//private static String filePath = null;
		//private static File folder = null;
		private static File[] listOfFiles = null;
		private StringBuilder bodyURL = new StringBuilder();
		private ArrayList<String> interfaceList = new ArrayList<String>();
		private ArrayList<String> variableList = new ArrayList<String>();
		private ArrayList<String> methodParamList = new ArrayList<String>();
		private ArrayList<String> methodList = new ArrayList<String>();
		private ArrayList<String> constructList = new ArrayList<String>(); 
		private ArrayList<String> constructParamList = new ArrayList<String>();
		private HashMap<String,List<ClassOrInterfaceType>> classInterfaceMap = new HashMap<String,List<ClassOrInterfaceType>>();
		private HashMap<String,List<ClassOrInterfaceType>> classSuperClassMap = new HashMap<String,List<ClassOrInterfaceType>>();
		private String className = "";
		private String[] primitives = {"byte","short","int","long","float","double","boolean","char",
				"Byte","Short","Integer","Long","Float","Double","Boolean","Char"};
		private ConcurrentHashMap<String,String> multiplicityMap = new ConcurrentHashMap<String,String>();
		private ConcurrentHashMap<String,String> usesMap = new ConcurrentHashMap<String,String>();
		private ConcurrentHashMap<String,String> usessMap = new ConcurrentHashMap<String,String>();
		private String usesRelation="";
		private String usesInterface="";
		boolean flag=true;

		public static void main(String[] args) throws URISyntaxException
		{
		
			
			if(args.length == 2 )
			{
				String inPath = args[0];
				String outPath = args[1];
		
				File folder = new File(inPath);
			    listOfFiles = folder.listFiles();

		 		UMLparser obj = new UMLparser();
				String result = obj.umlParser(outPath,inPath);
				System.out.println(result);
				
				GenerateDiagram.generatePNG(result,outPath.toString());
				
			}
			else
			{
				System.out.println("Please provide required path.");
			}
		
		}
		
		
		public String umlParser(String outPath,String inPath)
		{
			File folder = new File(inPath);
		    listOfFiles = folder.listFiles();
			try
			{
				for(File file : listOfFiles)
				{
					if (file.isFile()&&file.getName().endsWith(".java")) 
					{
						if(bodyURL.length() > 0 && (bodyURL.charAt(bodyURL.length()-1) != ','))
						{
							bodyURL.append(",");
						}
						variableList = new ArrayList<String>();
						methodList = new ArrayList<String>();
						constructList = new ArrayList<String>();
						bodyURL.append("[");
						
						
				        FileInputStream inputStream = new FileInputStream(folder.toString()+"/" + file.getName());
						CompilationUnit cu = JavaParser.parse(inputStream);
						List<Node> childrenNodes = cu.getChildrenNodes();
						for(Node child : childrenNodes)
						{
							if(child instanceof ClassOrInterfaceDeclaration)
							{
								ClassOrInterfaceDeclaration classInterfaceDec = (ClassOrInterfaceDeclaration)child;
								
								// create a list with interfaces as entries
								
								if(classInterfaceDec.isInterface())
								{
									interfaceList.add(classInterfaceDec.getName());
									bodyURL.append("<<Interface>>;");
									bodyURL.append(classInterfaceDec.getName());
									bodyURL.append("");
									flag=false;
									continue;
								}
								flag=true;
								bodyURL.append(classInterfaceDec.getName());
								className = classInterfaceDec.getName();
								// create HashMaps to map classes with interfaces and parent classes for implements and extends relation
								
								List<ClassOrInterfaceType> implementsList = classInterfaceDec.getImplements();
								if(implementsList!=null)
								{
									classInterfaceMap.put(classInterfaceDec.getName(), implementsList);
								}
								
								List<ClassOrInterfaceType> extendsList = classInterfaceDec.getExtends();
								if(extendsList!=null)
								{
									classSuperClassMap.put(classInterfaceDec.getName(), extendsList);
								}
								
							}
						}// end of for loop for childrenNodes
						
						// check declaration
						
						List<TypeDeclaration> bodyTypes = cu.getTypes();
						for(TypeDeclaration bodyType : bodyTypes)
						{
							List<BodyDeclaration> bodyDec = bodyType.getMembers();
							
							if(!bodyDec.isEmpty() && bodyDec.size()>0)
							{
								String accessModifier = "";
								for(BodyDeclaration body : bodyDec)
								{
									if(body instanceof FieldDeclaration)
									{
										String primitiveType="";
										FieldDeclaration fieldDec = (FieldDeclaration)body;
										// checking access modifiers 
										int fieldDecModifiers = fieldDec.getModifiers();
										boolean success = false;
	
										switch(fieldDecModifiers)
										{
										case ModifierSet.PRIVATE:
											accessModifier = "-";
											success = true;
											break;
										case ModifierSet.PUBLIC:
											accessModifier = "+";
											success = true;
											break;
										
										}
										
										// end checking access modifiers
										if(success)
										{
											boolean enterVariable = true;
											
											List<Node> fieldChildNodes = fieldDec.getChildrenNodes();
											
											for(Node fieldNode :fieldChildNodes)
											{
												if(fieldNode instanceof ReferenceType)
												{
													String refType = ((ReferenceType) fieldNode).getType().toString();
													if(refType.equals("String"))
													{
														primitiveType += refType;
													}
													else
													{
														boolean foundPrimitive = false;
														boolean foundCollection = false;
	
														for(String primitiveRef : primitives)
														{
															if(refType.contains(primitiveRef))
															{
																primitiveType += refType+"(*)";
																foundPrimitive = true;
																break;
															}
														}
														if(!foundPrimitive)
														{
															// logic for checking multiplicity
															if(checkForMultiplicity(refType,className))
															{
																enterVariable = false;
																foundCollection = true;
																break;
															}
															
														}
													}
												}
												else if(fieldNode instanceof PrimitiveType)
												{
													PrimitiveType pType = (PrimitiveType)fieldNode;
													primitiveType = pType.toString();
												}
												if(fieldNode instanceof VariableDeclarator && enterVariable)
												{
													VariableDeclarator variableDec = (VariableDeclarator)fieldNode;
											
													variableList.add(accessModifier+variableDec.toString()+":"+" "+primitiveType);
												}
											}
										}
										
									}
									
									else if(body instanceof MethodDeclaration)
									{
										if(flag == true){
										String tempMethodParam = "";
										MethodDeclaration method = (MethodDeclaration)body;
										String methodAccessModifier = "";
										String methodReferenceType = "";
										String methodName = "";
										int methodModifier = method.getModifiers();
										boolean success = false;
	
										switch(methodModifier)
										{
										case ModifierSet.PUBLIC:
											methodAccessModifier = "+";
											success = true;
											break;
										case ModifierSet.PUBLIC+ModifierSet.STATIC:
											methodAccessModifier = "+";
											success = true;
											break;
										}
										
										if(success)
										{
											List<Node> methodChildNodes =   method.getChildrenNodes();
	
											for(Node methodChildNode : methodChildNodes)
											{
												if(methodChildNode instanceof ReferenceType)
												{
													ReferenceType referenceMethod = (ReferenceType)methodChildNode;
	
													methodReferenceType = referenceMethod.getType().toString();
												}
												else if(methodChildNode instanceof VoidType)
												{
													methodReferenceType = "void";
												}
											}
	
											methodName  = method.getName();
											
											List<Parameter> methodParams = method.getParameters();
	
											methodParamList = new ArrayList<String>();
											
											if(methodParams.size() > 0)
											{
												tempMethodParam += "(";
												for(Parameter param : methodParams)
												{
													List<Node> paramChildNodes = param.getChildrenNodes();
	
													String methodParamReferenceType="", variable="";
													for(Node paramChild : paramChildNodes)
													{
														if(paramChild instanceof ReferenceType)
														{
															ReferenceType r = (ReferenceType)paramChild;
															methodParamReferenceType = r.getType().toString();

															// do uses operation
															Dependency cd = new Dependency(className,method);
															usesMap=cd.checkDependency(interfaceList);
															
														}
														else if(paramChild instanceof VariableDeclaratorId)
														{
															VariableDeclaratorId v = (VariableDeclaratorId)paramChild;
															variable = v.getName().toString();
														}
														
													}
	
													methodParamList.add(variable+":"+methodParamReferenceType);
												}
												
												for(int i=0; i<methodParamList.size() ; i++)
												{
													if(i != methodParamList.size()-1)
													{
														tempMethodParam += methodParamList.get(i)+",";
													}
													else
													{
														tempMethodParam += methodParamList.get(i);
													}
												}
												tempMethodParam += ")";
											}
											else
											{
												tempMethodParam = "()";
											}
											if(methodName.startsWith("get")||methodName.startsWith("set"))
											{
												
											}
											else
											{
												methodList.add(methodAccessModifier+methodName+tempMethodParam+":"+methodReferenceType);
											}
										}
									}
									}
									else if(body instanceof ConstructorDeclaration)
									{
										String tempConstructParam="";
										ConstructorDeclaration construct = (ConstructorDeclaration)body;
										List<Node> constructNodes = construct.getChildrenNodes();
										
										String constructAccessModifier = "";
										String constrctName = "";
										
										int constructModifier = construct.getModifiers();
										boolean success = false;
	
										switch(constructModifier)
										{
										case ModifierSet.PUBLIC:
											constructAccessModifier = "+";
											success = true;
											break;
										}
										constrctName = construct.getName();
										List<Parameter> constructParams = construct.getParameters();
										constructParamList = new ArrayList<String>();
										if(constructParams.size()>0)
										{
											tempConstructParam+="(";
											for(Parameter param : constructParams)
											{
												List<Node> paramChildNodes = param.getChildrenNodes();
												String conParamReferenceType="", variable="";
												for(Node paramChild : paramChildNodes)
												{
													if(paramChild instanceof ReferenceType)
													{
														ReferenceType r = (ReferenceType)paramChild;
														conParamReferenceType = r.getType().toString();
											
														// do uses operation
														Dependency cd = new Dependency(className,construct);
														usessMap=cd.checkCDependency(interfaceList);
														
													}
													else if(paramChild instanceof VariableDeclaratorId)
													{
														VariableDeclaratorId v = (VariableDeclaratorId)paramChild;
														variable = v.getName().toString();
													}
													
												}
	
												constructParamList.add(variable+":"+conParamReferenceType);
											}
											
											for(int i=0; i<constructParamList.size() ; i++)
											{
												if(i != constructParamList.size()-1)
												{
													tempConstructParam += constructParamList.get(i)+",";
												}
												else
												{
													tempConstructParam += constructParamList.get(i);
												}
											}
											tempConstructParam += ")";
										}
										else
										{
											tempConstructParam = "()";
										}
										constructList.add(constructAccessModifier+constrctName+tempConstructParam);
									}
									
								}
							}
						}
						
						if(variableList.size() > 0)
						{
							bodyURL.append("|");
							for(int i=0 ; i<variableList.size() ; i++)
							{
								if(i != variableList.size()-1)
									bodyURL.append(variableList.get(i)+";");
								else
									bodyURL.append(variableList.get(i));
							}
	
						}
	
	
						if(methodList.size() > 0)
						{
							bodyURL.append("|");
							for(int i=0 ; i<methodList.size() ; i++)
							{
								if(i != methodList.size()-1)
									bodyURL.append(methodList.get(i)+";");
								else
									bodyURL.append(methodList.get(i)+";");
							}
						}
						if(constructList.size() > 0)
						{
							if(methodList.isEmpty() && methodList.size()==0)
							{
								bodyURL.append("|");
								for(int i=0 ; i<constructList.size() ; i++)
								{
									if(i != constructList.size()-1)
										bodyURL.append(constructList.get(i)+";");
									else
										bodyURL.append(constructList.get(i));
								}
							}
							else
							{
								for(int i=0 ; i<constructList.size() ; i++)
								{
									if(i != constructList.size()-1)
										bodyURL.append(constructList.get(i)+";");
									else
										bodyURL.append(constructList.get(i));
								}
							}
		
						}
						bodyURL.append("]");
						bodyURL.append(",");
				
				}
				}
				if(!usesMap.isEmpty() && usesMap.size()>0)
				{
					System.out.println(usesMap);
					for(String keys : usesMap.keySet())
					{
						String tempKey = keys;
						bodyURL.append("[");
						bodyURL.append(usesMap.get(tempKey));
						bodyURL.append("]uses -.->[<<interface>>;");
						bodyURL.append(tempKey);
						bodyURL.append("],");
					}
				}
				if(!usessMap.isEmpty() && usessMap.size()>0)
				{
					System.out.println(usessMap);
					for(String keys : usessMap.keySet())
					{
						String tempKey = keys;
						bodyURL.append("[");
						bodyURL.append(tempKey);
						bodyURL.append("]uses -.->[<<interface>>;");
						bodyURL.append(usessMap.get(tempKey));
						bodyURL.append("],");
					}
				}
				if(!multiplicityMap.isEmpty() && multiplicityMap.size()>0)
				{
					for(String keys : multiplicityMap.keySet())
					{
						String tempKey = keys;
						if(interfaceList.contains(tempKey.split("\\~")[1]))
						{
							usesRelation += "["+tempKey.split("\\~")[0]+"]"+multiplicityMap.get(tempKey)+"[<<interface>>;"+tempKey.split("\\~")[1]+"],";
						}
						else
						{
							usesRelation += "["+tempKey.split("\\~")[0]+"]"+multiplicityMap.get(tempKey)+"["+tempKey.split("\\~")[1]+"],";
						}
						System.out.println(" tempKey "+tempKey);
						
					}
				}
				for(String key : classInterfaceMap.keySet() )
				{
					String tempKey = key;
					List<ClassOrInterfaceType> tempList = classInterfaceMap.get(tempKey);
					if( tempList != null)
					{
					for(int i=0;i<tempList.size();i++)
					{
						if(!tempList.isEmpty())
						{
							if(tempList.get(i)!=null)
							{
								bodyURL = bodyURL.append("[<<interface>>;"+tempList.get(i)+"]^-.-["+tempKey+"],");
		
							}
							
						}
					}
					}
				}
				
			   // combine class inheritance
				for(String key : classSuperClassMap.keySet() )
				{
					String tempKey = key;
					List<ClassOrInterfaceType> tempList = classSuperClassMap.get(tempKey);
					
					if( tempList != null)
					{
					for(int i=0;i<tempList.size();i++)
					{
						if(!tempList.isEmpty())
						{
							if(tempList.get(i)!=null)
							{
								bodyURL = bodyURL.append("["+tempList.get(i)+"]^-["+tempKey+"],");
								
							}
							
						}
					}
				}
				}
				
				bodyURL.append(usesRelation);
				bodyURL.append(usesInterface);
				bodyURL.deleteCharAt(bodyURL.length()-1);
			}catch(Exception e)
			{
				System.out.println(e);
				e.printStackTrace();
			}
	
			return bodyURL.toString();
		}
		public boolean checkForMultiplicity(String referenceClass,String className)
		{
			String relationValue="";
			String relationKey="";
			String reverseRelationKey="";
			
			if(referenceClass.contains("Collection"))
			{
				referenceClass = referenceClass.toString().replace("Collection<","");
				referenceClass = referenceClass.replace(">", "");
				
				relationValue="-*";
				relationKey=className+"~"+referenceClass;
				reverseRelationKey=referenceClass+"~"+className;

				if(multiplicityMap.isEmpty())
				{
					multiplicityMap.put(relationKey, relationValue);
				}
				// check key already exists
				else if(!multiplicityMap.isEmpty() && multiplicityMap.size()>0)
				{
					if(!multiplicityMap.containsKey(relationKey) && !multiplicityMap.containsKey(reverseRelationKey))
					{
						for(String keys : multiplicityMap.keySet())
						{
							String tempKey = keys;
						
							if(tempKey.equals(reverseRelationKey)==false)
							{
								multiplicityMap.put(relationKey, relationValue);
								
							}
						}
					}
				}
				
				//System.out.println(multiplicityMap);
				return true;
				
			}
			else
			{
				relationValue="1-1";
				relationKey=className+"~"+referenceClass;
				reverseRelationKey=referenceClass+"~"+className;
				if(multiplicityMap.isEmpty())
				{
					multiplicityMap.put(relationKey, relationValue);
					
				}
				// check key already exists
				else if(!multiplicityMap.isEmpty() && multiplicityMap.size()>0)
				{
					if(!multiplicityMap.containsKey(relationKey) && !multiplicityMap.containsKey(reverseRelationKey))
					{
						for(String keys : multiplicityMap.keySet())
						{
							String tempKey = keys;
							
							if(tempKey.equals(reverseRelationKey)==false)
							{
								multiplicityMap.put(relationKey, relationValue);
								
							}
						}
					}
				}
				
				System.out.println(multiplicityMap);
				return true;
				
			}
			
		}
		
	}

