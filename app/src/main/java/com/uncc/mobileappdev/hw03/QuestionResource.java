package com.uncc.mobileappdev.hw03;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.uncc.mobileappdev.hw03.DataHelpers.formatQuestionDetailsString;

/**
 * Created by Stephen Weber on 2/18/2018.
 */

public class QuestionResource extends AsyncTask<String, Void, ArrayList<Question>> {

    public interface AsyncResponse{
        void processFinish(ArrayList<Question> questionDetails);
    }

    public AsyncResponse delegate = null;
    private Activity activity;
    private ArrayList<Question> questionData = new ArrayList<>();

    public QuestionResource(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected ArrayList<Question> doInBackground(String... params) {
        StringBuilder sb = new StringBuilder();
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        String result = null;
        try {

            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            result = sb.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (connection != null) {
                connection.disconnect();
            }

            if (bufferedReader != null) {
                try {
                    bufferedReader.close();

                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }

        questionData = formatQuestionDetailsString(result);
        return questionData;
    }

    @Override
    protected  void onPostExecute(ArrayList<Question> questionData){
        if(questionData != null && !questionData.isEmpty()){
            Log.d("Demo", "Number of Question objects in list: " + questionData.size());
            delegate.processFinish(questionData);
        } else {
            Log.d("Demo", "NO DATA!");
        }
    }
}
