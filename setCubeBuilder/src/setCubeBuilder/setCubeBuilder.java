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
	
	//pass the name of the text file, the three letter set code, and the amount of
	//each rarity of card desired
	public setCubeBuilder(String filename, String set, int mythics, int rares, int uncommons, int commons)
	{
		try 
		{
			//open a file to delete what is currently in it
			//and write the header to the file
			FileWriter writer = new FileWriter(filename);
			writer.write("Card List \r\n");
			writer.write("\r\n");
			
			//close the connection
			writer.close();
			
			//call fetch with each rarity and the amount of cards needed for each rarity
			//and the filename
			fetch(filename, set, mythics, "M");
			fetch(filename, set, rares, "R");
			fetch(filename, set, uncommons, "U");
			fetch(filename, set, commons, "C");
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//fetch data from a json file and print the card list to a specified text document
	//take the filename for the text file, the three letter setcode,
	//the amount of each rarity, and the letter for the rarity
	//return nothing, but print the results to the specified text document
	private void fetch(String filename, String set, int amount, String rarity) {
		
		String inline = "";
		try 
		{
			//Combine the strings and part of the api call to generate the full api call
			String website = "https://api.scryfall.com/cards/search?order=set&q=set%3A" + set + 
					"+rarity%3A" + rarity + "+not%3Apwdeck+-t%3Abasic";
			//System.out.println(website);
			
			//connect to the URL to fetch the JSON from a REST API
			URL url = new URL(website);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection(); 
			conn.setRequestMethod("GET");
			conn.connect(); 
			
			//test to see if we are able to connect
			int responsecode = conn.getResponseCode();
			System.out.println("Response code is: " +responsecode);
			
			//if we fail to connect throw an error or the JSON is read in incorrectly
			//throw an error
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
			
			
			//JSONParser reads the data from string object and break each data into key value pairs
			JSONParser parse = new JSONParser();
			//Type cast the parsed JSON data in JSON object
			JSONObject jobj = (JSONObject)parse.parse(inline);
			
			//Store the JSON object in JSON array as objects (For level 1 array element i.e Results)
			JSONArray jsonarr_1 = (JSONArray) jobj.get("data");
			
			//open the file that we will write to
			FileWriter writer = new FileWriter(filename, true);
			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			
			//print the number of each rarity before we print out the list of cards
			int total = jsonarr_1.size()*amount;
			bufferedWriter.write(String.valueOf(total));
			
			//choose between which rarity to write after the amount
			switch (rarity)
			{
			case "M":
				bufferedWriter.write(" Mythics");
				break;
			case "R":
				bufferedWriter.write(" Rares");
				break;
			case "U":
				bufferedWriter.write(" Uncommons");
				break;
			case "C":
				bufferedWriter.write(" Commons");
				break;
			}
			/*
			if (rarity == "M")
			else if (rarity == "R")
				bufferedWriter.write(" Rares");
			else if (rarity == "U")
				bufferedWriter.write(" Uncommons");
			else if (rarity == "C")
				bufferedWriter.write(" Commons");
				*/
			
			//look through the relevant portion of the json while
			//printing all of the cards and the amount of each of them
			for (int i=0; i<jsonarr_1.size(); i++)
			{
				//fetch the card name
				JSONObject jsonobj_1 = (JSONObject)jsonarr_1.get(i);
				bufferedWriter.newLine();
				//print the amount needed
				bufferedWriter.write(String.valueOf(amount));
				bufferedWriter.write(" ");
				//print the name of the card
				bufferedWriter.write((String)jsonobj_1.get("name"));
			}
			
			//space out the different rarities
			bufferedWriter.newLine();
			bufferedWriter.write("\n");
			
			//disconnect the connection to the rest API and to the the file
			conn.disconnect();
			bufferedWriter.close();
			writer.close();

		}
		
		//if we hit an error print it out
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		
	}

}
