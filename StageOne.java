

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

public class StageOne {
	
	
	
	private static String register() throws Exception{
		String mygit = "https://github.com/msimonds/API-Challenge-CODE2040";
		String myemail = "msimonds@oberlin.edu";
		//create dictionary and json object
		Map<String, String> dictionary = new HashMap<String,String>();
		dictionary.put("email",myemail);
		dictionary.put("github",mygit);
		JSONObject json = new JSONObject(dictionary);
		return post("http://challenge.code2040.org/api/register",json,true);
		
				
	}
	
	private static String post(String urlname, JSONObject json, boolean gettoken) throws Exception{ 
		//setup connection
		URL url = new URL(urlname);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setDoOutput(true);
		connection.connect();
		//POST to server
		DataOutputStream out = new DataOutputStream(connection.getOutputStream());
		out.writeBytes(json.toString());
		out.close();
		
		if(gettoken){
			//store token
			InputStream in = connection.getInputStream();		
			Scanner scan = new Scanner(in);
			JSONObject jtoke = new JSONObject(scan.next());		
			scan.close();
			in.close();
			connection.disconnect();

			return jtoke.getString("result");
		}
		else{
			return null;
		}		
	}

	public static void main(String[] args) throws Exception {
		String toke= register();		
		//make json dictionary 
		JSONObject dic = new JSONObject();
		dic.put("token", toke);
		//post jsonobject to getstring url
		String getstr = post("http://challenge.code2040.org/api/getstring",dic,true);
		dic.put("string", new StringBuilder(getstr).reverse().toString());
		post("http://challenge.code2040.org/api/validatestring",dic,false);
		
		
	}

}
