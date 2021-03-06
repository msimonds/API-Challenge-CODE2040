/**
* This program completes Stage One of the CODE2040 API Challenge.
* It uses the POST method to send a unique token to a server which returns a string
* which is then reversed and sent back to the server using standard Java libraries.
*
* @author  Michael Simonds
* @since   01/11/2015
*/

package code2040;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONObject;

public class StageOne {
	

	/**
	 * This method establishes a connection and calls the post() method in 
	 * order to recieve the unique identifying token used throughout the 
	 * API Challenge.
	 * 
	 * @return String This returns the token as a String
	 */
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
	
	/**
	 * This method uses the POST method to push a given JSONObject 
	 * to a specified url. If the output must be stored then a third
	 * parameter can be used to specify whether the output from the server
	 * is returned, or a null string. 
	 * @param urlname The url address of the server.
	 * @param json  The JSONObject to be POSTed to the server
	 * @param gettoken If True, the method will return output from the server. If False, the method will return the null string
	 * @return String This returns the token value associated with the key "result", or the null string
	 */
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
		//Create JSON dictionary 
		JSONObject dic = new JSONObject();
		dic.put("token", toke);
		//POST reversed string to getstring url
		String getstr = post("http://challenge.code2040.org/api/getstring",dic,true);
		dic.put("string", new StringBuilder(getstr).reverse().toString());
		post("http://challenge.code2040.org/api/validatestring",dic,false);	
		
	}

}
