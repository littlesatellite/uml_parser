package UMLparser;

/**
 *  Program to parse the java source code to generate yUML url that produces class diagram 
 */




public class OneTest {

	public static void main(String[] args) 
//			throws URISyntaxException
	{
		if(args.length == 2)
		{
			String path = args[0];
			System.out.println("path:" + path);
			String newFileName = args[1];
			System.out.println("newFileName:" + newFileName);
		
		}
		else
		{
			System.out.println("Provide all required arguments.");
		}
	
	}
	
}
