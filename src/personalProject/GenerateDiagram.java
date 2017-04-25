package personalProject;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GenerateDiagram {

	public static Boolean generatePNG(String result,String outPath){
		result = "https://yuml.me/diagram/scruffy/class/" + result + ".png";
		try
		{
		URL url = new URL(result);
		
		  
           HttpURLConnection conn = (HttpURLConnection) url.openConnection();
           conn.setRequestMethod("GET");
           conn.setRequestProperty("Accept", "application/json");

           if (conn.getResponseCode() != 200) {
               throw new RuntimeException(
                       "Failed : HTTP error code : " + conn.getResponseCode());
           }
           
		InputStream in = new BufferedInputStream(url.openStream());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int n = 0;
		while (-1!=(n=in.read(buf)))
		{
		  out.write(buf, 0, n);
		}
		out.close();
		in.close();
		byte[] response = out.toByteArray();
		FileOutputStream fos = new FileOutputStream(outPath);
		fos.write(response);
		fos.close();
		}
		catch(Exception e)
		{
			System.out.println("Please provide correct outPath name.");
			e.printStackTrace();
		}
		return true;
	}
	}
