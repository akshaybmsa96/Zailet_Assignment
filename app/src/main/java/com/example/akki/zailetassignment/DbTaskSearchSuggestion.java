package com.example.akki.zailetassignment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Akki on 10-03-2017.
 */

public class DbTaskSearchSuggestion extends AsyncTask<String,Void,String> {

    Context ctx;
    String Jo,json_string;
    public ArrayList<String> title;
    public ArrayList<String> type;
    CustomAdapterSuggestion ca;
    AutoCompleteTextView actv;
    Activity a;
    DbTaskSearchSuggestion(Context ctx , Activity a , ArrayList<String> title, ArrayList<String> type ,AutoCompleteTextView actv)
    {
        this.ctx=ctx;
        this.a=a;
        this.actv=actv;
        this.title=title;
        this.type=type;
    }

    @Override
    public String doInBackground(String ...params) {

        String ip_url="http://ypoutlet.esy.es/zailet/getsuggestion.php";
       //  String ip_url="http://10.0.3.2/zailet/getsuggestion.php";

        String topic=params[0];


        try {

            URL url = new URL(ip_url);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream OS=httpURLConnection.getOutputStream();
            BufferedWriter br1=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
            String data= URLEncoder.encode("topic","UTF-8")+"="+URLEncoder.encode(topic,"UTF-8");   //post key word to php file
            br1.write(data);
            br1.flush();
            br1.close();
            OS.close();


            InputStream IS=httpURLConnection.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(IS));
            StringBuilder sb=new StringBuilder();

            while((Jo=br.readLine())!=null)                      //reading echo text
            {
                sb.append(Jo+"\n");

            }

            br.close();
            IS.close();
            httpURLConnection.disconnect();
            String s=sb.toString().trim();
            //  Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
            return s;

        }

        catch (Exception e) {
            e.printStackTrace();
        }


        return "Error";

    }

    @Override
    public void onPreExecute() {
        //  super.onPreExecute();

    }

    @Override
    public void onPostExecute(String result) {
        //   Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
        json_string=result;

        JSONObject j;
        JSONArray jsonArray;

        String t,n,id;
        int count=0;
        try {                                                           //json parsing

            j = new JSONObject(json_string);
            jsonArray=j.getJSONArray("response");
            ca= new CustomAdapterSuggestion(a,title,type);                //initializing adapter
            ca.clear();
            title.clear();
            type.clear();

            while (count<jsonArray.length())
            {
                JSONObject jo=jsonArray.getJSONObject(count);           //fetching suggestions
                t=jo.getString("title");
                n=jo.getString("type");

               //    Toast.makeText(ctx, t+" "+n , Toast.LENGTH_LONG).show();

               title.add(t);                                            // adding suggestions to arraylist
               type.add(n);

                count++;
            }

            actv.setThreshold(2);                                   //minimum char required to show suggestions
            actv.setAdapter(ca);                                    // setting suggestion to searchbar



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

