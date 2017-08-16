package pe.edu.upc.parknina.networks;

/**
 * Created by hugo_ on 27/07/2017.
 */

public class ParkninaApi {
    private static String api_url = "http://parkcityperu.herokuapp.com";

    public static String getApi_url() {
        return api_url;
    }

    public static void setApi_url(String api_url) {
        ParkninaApi.api_url = api_url;
    }
}
