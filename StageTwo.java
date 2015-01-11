/**
* This program completes Stage Two of the CODE2040 API Challenge.
* Given a string, "needle", the program Uses a binary search method 
* on a sorted array of strings in order to find the index of a string 
* that matches the "needle". 
*
* @author  Michael Simonds
* @since   01/11/2015
*/ 
package code2040;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class StageTwo {
	
	
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
		//Grabs the "haystack" dictionary 
		JSONObject dic = new JSONObject();
		dic.put("token",register().getString("result"));		
		JSONObject nh = post("http://challenge.code2040.org/api/haystack",dic,true).getJSONObject("result");
		JSONArray jarr = nh.getJSONArray("haystack");
		String needle = nh.getString("needle");
		
		//Create an array to sort the haystack
		String[] arr = new String[jarr.length()];
		for(int x=0; x<jarr.length();x++){
			arr[x]=(String) jarr.get(x);
		}		
		Arrays.sort(arr);
		
		
		//Post the token and needle
		dic.put("needle", Arrays.binarySearch(arr, needle));
		post("http://challenge.code2040.org/api/validateneedle",dic,false);
	}

}
