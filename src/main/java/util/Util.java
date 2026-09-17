package util;

import java.io.BufferedReader;
import java.io.IOException;

public class Util {
    //body.length is quicker, but making a function allows for better error handling
    public static int contentLength(byte[] body) {
        int contentLength = 0; //Must initialise with a value

        try {
            contentLength = body.length;
        } catch (Exception e) {
            System.out.println("Error finding the content length: " + e);
        }

        return contentLength;
    }
}
