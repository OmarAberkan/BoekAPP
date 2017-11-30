package com.example.omar.apvalley;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Omar on 18/10/2017.
 */


class GetLevDataTask extends AsyncTask<String, String, String> {
    String data;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }
    public GetLevDataTask(String data){
        this.data = data;
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            return postData(params);
        } catch (IOException ex) {
            return "Network error !";
        } catch (JSONException ex) {
            return "Data Invalid !";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

    }

    private String postData(String urlPath[]) throws IOException, JSONException {

        Response response;

        try {

            OkHttpClient client = new OkHttpClient();
//we hebben dit geprobeerd met dummi data en da werkt ma k moet u json data is zien oke wacht
            Request request = new Request.Builder()
                    .url("https://www.googleapis.com/books/v1/volumes?q=isbn:"+data)
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "f54b7bf2-1ba5-0d57-a00f-c36c8f757564")
                    .build();

           response = client.newCall(request).execute();
        } finally {

        }
        return response.body().string();
    }
}