package com.example.akki.zailetassignment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

/**
 * Created by Akki on 17-04-2017.
 */

public class DbTaskLogin extends AsyncTask<String,Void,String> {

    Context ctx;
    String Jo,json_string;
    ProgressDialog mProgress;
    PreferenceData pf;
    String un;

    Activity a;
    DbTaskLogin(Context ctx , Activity a)
    {
        this.ctx=ctx;
        this.a=a;
    }

    @Override
    public String doInBackground(String ...params) {


       // String ip_url="http://10.0.3.2/zailet/login.php";
        String ip_url="http://ypoutlet.esy.es/zailet/login.php";

        un=params[0];
        String ps=params[1];


        try {

            URL url = new URL(ip_url);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream OS=httpURLConnection.getOutputStream();
            BufferedWriter br1=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
            String data= URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(un,"UTF-8")+"&"+          // post data to php file
                    URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(ps,"UTF-8");;
            br1.write(data);
            br1.flush();
            br1.close();
            OS.close();


            InputStream IS=httpURLConnection.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(IS));
            StringBuilder sb=new StringBuilder();

            while((Jo=br.readLine())!=null)                 //reading echo text
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

        mProgress = new ProgressDialog(ctx);                  // showing progress dialog
        mProgress.setMessage("Authenticating... ");
        mProgress.show();
        mProgress.setCancelable(false);
    }

    @Override
    public void onPostExecute(String result) {
          // Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
        json_string=result;

        JSONObject j;
        JSONArray jsonArray;

        String st,n;
        int count=0;
        try {                                                           //Json parsing

            j = new JSONObject(json_string);
            jsonArray=j.getJSONArray("response");

                JSONObject jo=jsonArray.getJSONObject(0);
                st=jo.getString("status");

            if(st.equals("true")){
                mProgress.dismiss();
                n=jo.getString("name");
                Intent i=new Intent(ctx,Home.class);
                pf.setUserLoggedInStatus(ctx,true);                   // setting shared preference data for login status and name of the logged in user
                pf.setLoggedInUserName(ctx,n);
                pf.setLoggedInUserId(ctx,un);
               // Toast.makeText(ctx,n, Toast.LENGTH_LONG).show();
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ctx.startActivity(i);

            }
                else {
                Toast.makeText(ctx,"Invalid Credentials",Toast.LENGTH_LONG).show();      //wrong used credentials
                mProgress.dismiss();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
