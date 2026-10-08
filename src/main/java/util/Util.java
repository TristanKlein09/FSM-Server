package util;

import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Util {
    private static final int HASHING_COST = 12; //Cost/work factor for bcrypt hashing

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

    //Spins up a new thread to hash the plain text and returns it
    public static String hashPlainText(String plainText) throws ExecutionException, InterruptedException {
        //Since we need to return a value from the thread, we must use CompleteableFuture as opposed to new Thread().start()
        CompletableFuture<String> future =  CompletableFuture.supplyAsync(() -> {
            String salt = BCrypt.gensalt(HASHING_COST); //Salt is name for the random string that is added to the plain text before hashing
            return BCrypt.hashpw(plainText, salt);
        });

        String hashedText = future.get();
        return hashedText;
    }

}
