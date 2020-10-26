package org.alessios18.jserversmanager.launcher;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.alessios18.jserversmanager.launcher.baseobjects.Release;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class JServersManagerLauncher {

	 public JServersManagerLauncher() {
	 }

	 public static void main(String[] args) {
		  getReleases();
	 }

	 public static void getReleases() {
		  try {
				URL url = new URL(" https://api.github.com/repos/alessios18/JServersManager/releases");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/vnd.github.v3+json");

				if (conn.getResponseCode() != 200) {
					 throw new RuntimeException("Failed : HTTP error code : "
								+ conn.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(
						  (conn.getInputStream())));
				StringBuilder sb = new StringBuilder();
				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					 System.out.println(output);
					 sb.append(output);
				}

				Gson gson = new Gson();
				Type listType = new TypeToken<ArrayList<Release>>() {
				}.getType();
				ArrayList<Release> readValue = gson.fromJson(sb.toString(), listType);

				System.out.println(readValue.toString());
				conn.disconnect();
		  } catch (MalformedURLException e) {
				e.printStackTrace();
		  } catch (IOException e) {
				e.printStackTrace();
		  }

	 }
}
