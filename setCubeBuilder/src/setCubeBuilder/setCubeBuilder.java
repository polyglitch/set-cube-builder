package setCubeBuilder;

import java.net.HttpURLConnection;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class setCubeBuilder {
	public setCubeBuilder(String filename, String set, int mythics, int rares, int uncommons, int commons)
	{
		try 
		{
			FileWriter writer = new FileWriter(filename);
			fetch(filename, set, "M");
			fetch(filename, set, "R");
			fetch(filename, set, "U");
			fetch(filename, set, "C");
			
			//this will probably cause weird errors
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private static void fetch(String filename, String set, String rarity) {
		// TODO Auto-generated method stub
		
		String inline = "";
		try 
		{
			
			String website = "https://api.scryfall.com/cards/search?order=set&q=set%3A" + set + 
					"+rarity%3A" + rarity + "+not%3Apwdeck";
			
			URL url = new URL(website);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection(); 
			conn.setRequestMethod("GET");
			conn.connect(); 
			
			int responsecode = conn.getResponseCode();
			System.out.println("Response code is: " +responsecode);
			
			if(responsecode != 200)
				throw new RuntimeException("HttpResponseCode: " +responsecode);
			else
			{
				//Scanner functionality will read the JSON data from the stream
				Scanner sc = new Scanner(url.openStream());
				while(sc.hasNext())
				{
					inline+=sc.nextLine();
				}
				//Close the stream when reading the data has been finished
				sc.close();
			}
			
			
			System.out.println();
			//JSONParser reads the data from string object and break each data into key value pairs
			JSONParser parse = new JSONParser();
			//Type caste the parsed json data in json object
			JSONObject jobj = (JSONObject)parse.parse(inline);
			
			//Store the JSON object in JSON array as objects (For level 1 array element i.e Results)
			JSONArray jsonarr_1 = (JSONArray) jobj.get("data");
			//Get data for Results array
			//System.out.println(jsonarr_1.size());
			
			
			FileWriter writer = new FileWriter("MyFile.txt", true);
			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			
			System.out.println("total cards " + jsonarr_1.size());
			
			bufferedWriter.newLine();
			bufferedWriter.write("\ntotal cards " + jsonarr_1.size());
			bufferedWriter.newLine();
			
			for (int i=0; i<jsonarr_1.size(); i++)
			{
				JSONObject jsonobj_1 = (JSONObject)jsonarr_1.get(i);
				bufferedWriter.newLine();
				bufferedWriter.write((String)jsonobj_1.get("name"));
				System.out.println(jsonobj_1.get("name"));
			}
			
			conn.disconnect();
			bufferedWriter.close();
			writer.close();

		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		
	}

}
