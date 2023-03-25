package com.sirajsaleem.mathmanica;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class DbGetLeaderBoards {
    public String sendData(String username, String type, String rank, String userOrScore, String difficulty, String getUserScore) {
        String getData_url = "https://sirajsaleem.com/secrets/mathmanica/get-leader-boards.php";
        try {
            URL url = new URL(getData_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                    + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8") + "&"
                    + URLEncoder.encode("rank", "UTF-8") + "=" + URLEncoder.encode(rank, "UTF-8") + "&"
                    + URLEncoder.encode("userOrScore", "UTF-8") + "=" + URLEncoder.encode(userOrScore, "UTF-8") + "&"
                    + URLEncoder.encode("difficulty", "UTF-8") + "=" + URLEncoder.encode(difficulty, "UTF-8") + "&"
                    + URLEncoder.encode("getUserScore", "UTF-8") + "=" + URLEncoder.encode(getUserScore, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.ISO_8859_1));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            result = new StringBuilder(result.toString().trim());
            return result.toString();
        } catch (Exception urlEx) {
            Log.d("exception-url", urlEx.getMessage());
        }
        return null;
    }
}
