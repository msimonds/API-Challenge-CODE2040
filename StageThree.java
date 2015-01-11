/**
* This program completes Stage Three of the CODE2040 API Challenge.
* The code takes a prefix and searches an array for strings that have
* a matching prefix and removes them from the array. 
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

import org.json.JSONArray;
import org.json.JSONObject;

public class StageThree {
	
	/**
	 * This method establishes a connection and calls the post() method in 
	 * order to recieve the unique identifying token used throughout the 
	 * API Challenge.
	 * 
	 * @return JSONObject This returns the JSONObject containing the token.
	 */
	private static JSONObject register() throws Exception{
		String mygit = "https://github.com/msimonds/API-Challenge-CODE2040";
		String myemail = "msimonds@oberlin.edu";
		//Create dictionary and json object
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
	 * @param gettoken If True, the method will return output from the server. If False, the method will return null.
	 * @return JSONObject This returns JSONObject containing the token or a null value.
	 */
	private static JSONObject post(String urlname, JSONObject json, boolean gettoken) throws Exception{ 
		//Setup connection
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
			//Store token
			InputStream in = connection.getInputStream();		
			Scanner scan = new Scanner(in);
			JSONObject jtoke = new JSONObject(scan.next());		
			scan.close();
			in.close();
			connection.disconnect();

			return jtoke;
		}
		else{
			return null;
		}		
	}

	public static void main(String[] args) throws Exception {
		//Grabs the "prefix" and "array"
		JSONObject dic = new JSONObject();
		dic.put("token",register().getString("result"));		
		JSONObject nh = post("http://challenge.code2040.org/api/prefix",dic,true).getJSONObject("result");
		JSONArray jarr = nh.getJSONArray("array");		
		String prefix = nh.getString("prefix");
		//For loop to compare beginning of strings to prefix and remove from JSONArray
		int size = jarr.length();
		for(int x=0; x<size; x++){
			if(prefix.equals(jarr.get(x).toString().substring(0, prefix.length()))){
				System.out.println(jarr.get(x).toString().substring(0,prefix.length()));
				jarr.remove(x);
				x = x-1;
				size = size-1;				
			}
		}
		//Update dictionary and POST result
		dic.put("array", jarr.toString());
		post("http://challenge.code2040.org/api/validateprefix",dic, false);
	}
}
