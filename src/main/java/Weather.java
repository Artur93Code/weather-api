import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


//This app base on OpenWeatherMap API - https://openweathermap.org/
public class Weather {
    public static void main(String[] args) throws IOException {

        Scanner scan = new Scanner(System.in);
        System.out.println("Which city do you want to check the weather for (use -all postfix to see details about the weather):");
        String city = scan.nextLine();
        boolean detailedWeather = false;

        //check if user want detalis about weather
        if(city.endsWith("-all"))
        {
            city = city.substring(0,city.length()-4);
            detailedWeather = true;
        }

        URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q="+city+"&units=metric&appid=7fb4cc27190f5f8d4777f821b6d9063e");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        //Getting the response code
        int responsecode = conn.getResponseCode();


        if (responsecode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responsecode);
        } else {
            String weatherInfo = "";
            Scanner scanner = new Scanner(url.openStream());

            //Write all the JSON data into a string using a scanner
            while (scanner.hasNext()) {
                weatherInfo += scanner.nextLine();
            }

            //Close the scanner
            scanner.close();

            //Using the JSON simple library parse the string into a json object
            JsonObject jo = new Gson().fromJson(weatherInfo, JsonObject.class);
            //JsonArray weatherInJSON = new Gson().fromJson(weatherInfo, JsonArray.class);

            /*JSONParser parse = new JSONParser();
            JSONObject data_obj = (JSONObject) parse.parse(inline);

            //Get the required object from the above created object
            JSONObject obj = (JSONObject) data_obj.get("Global");*/
            if (!detailedWeather)
            {
                showBasicInfo(jo);
            }
            else
                showDetailsInfo(jo);
            //Get the required data using its key
            //System.out.println(obj.get("TotalRecovered"));

        }
    }

    public static void showBasicInfo(JsonObject weatherInJSON)
    {
        System.out.println("Latitude: "+ weatherInJSON.getAsJsonObject("coord").get("lat"));
        System.out.println("Longitude: "+ weatherInJSON.getAsJsonObject("coord").get("lon"));
        System.out.println("Country: "+ weatherInJSON.getAsJsonObject("sys").get("country"));
        System.out.println();
        System.out.println("Temperature");
        System.out.println("Temp: "+ weatherInJSON.getAsJsonObject("main").get("temp"));
        System.out.println("Feels like: "+ weatherInJSON.getAsJsonObject("main").get("feels_like"));
        System.out.println("Min: "+ weatherInJSON.getAsJsonObject("main").get("temp_min"));
        System.out.println("Max: "+ weatherInJSON.getAsJsonObject("main").get("temp_max"));
    }

    public static void showDetailsInfo(JsonObject weatherInJSON)
    {
        System.out.println("Details about the weather: "+ weatherInJSON);
    }
}
