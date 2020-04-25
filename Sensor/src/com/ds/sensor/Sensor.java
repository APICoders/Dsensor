package com.ds.sensor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;
import org.json.simple.JSONObject;

public class Sensor {

	public static void main(String[] args) {
		Sensor sensor = new Sensor();
		Scanner sc= new Scanner(System.in);
		
		//Get sensor id from the user
		System.out.println("Enter sensor id:");
		int id = sc.nextInt();
		int smokeLevel = 0;
		int co2Level = 0;
		int command = 0;
		do {
		//Assign random values for smoke level and carbondioxide level readings
		Random randomGenerator=new Random();
		
		smokeLevel = randomGenerator.nextInt(10) + 1;
		co2Level = randomGenerator.nextInt(10) + 1;
		
		System.out.println("Co2 val:"+co2Level);
		System.out.println("smoke lavel:"+smokeLevel);
		try {
			
			sensor.sendSensorData(id,smokeLevel,co2Level);
			Thread.sleep(5000);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		}while(command ==0);
	}

	private void sendSensorData(int id,int smokeLevel, int co2Level) throws Exception{
		//URL for the updateFireAlarmRecords function call in the API
		String url = "http://localhost:8081/FireAlarmMonitor/rest/fireAlarms/updateRecords/"+id;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection)obj.openConnection();
		
		//Creating the put request
		con.setRequestMethod("PUT");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Content-Type","application/json");
		
		//creating a JSON object using json-simple library
		JSONObject jObject = new JSONObject();
		jObject.put("id",id);
		jObject.put("smokeLevel",smokeLevel);
		jObject.put("co2Level",co2Level);
		
		//converting the JSON object 
		String data = jObject.toString();
		
		System.out.println(data);
		
		 //Insert data to output stream
		con.setDoOutput(true);
		DataOutputStream stream = new DataOutputStream(con.getOutputStream());
		stream.writeBytes(data);
		System.out.println("Added successfully");
		stream.flush();
		stream.close();
		
		int responseCode = con.getResponseCode();
		  System.out.println("Sending 'PUT' request to URL : " + url);
		  System.out.println("Data sending : " + data);
		  System.out.println("Response Code : " + responseCode);
		 
		  BufferedReader reader = new BufferedReader(
		          new InputStreamReader(con.getInputStream()));
		  String output;
		  StringBuffer response = new StringBuffer();
		 
		  while ((output = reader.readLine()) != null) {
		   response.append(output);
		  }
		  
		  
		  reader.close();
		  
		  //printing result from response
		  System.out.println(response.toString());
	}
}
