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
		for(int y=0;y<jarr.length();y++){
			System.out.println(arr[y]);
		}
		
		//Post the token and needle
		dic.put("needle", Arrays.binarySearch(arr, needle));
		post("http://challenge.code2040.org/api/validateneedle",dic,false);
	}

}
