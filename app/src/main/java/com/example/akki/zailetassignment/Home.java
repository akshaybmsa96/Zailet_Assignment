package com.example.akki.zailetassignment;

import android.accessibilityservice.AccessibilityService;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.attr.gravity;

public class Home extends AppCompatActivity {

    public ArrayList<String> title =new ArrayList<String>();     //suggestion lists
    public ArrayList<String> des=new ArrayList<String>();          //suggestion type
    AutoCompleteTextView actv;
    PreferenceData pf;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar tb=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        getSupportActionBar().setTitle("Feed");             // setting text to toolbar and color
        tb.setTitleTextColor(0XFFFFFFFF);
         actv=(AutoCompleteTextView)findViewById(R.id.at);

        actv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {    //when clicked on search button on keyboard
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    actv.setText("");
                    actv.setHint("");
                    actv.clearFocus();
                    actv.setCursorVisible(false);
                    actv.setBackgroundTintList(ColorStateList.valueOf( getResources().getColor(R.color.colorPrimary )));
                    return true;
                }
                return false;
            }
        });

        actv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                actv.setLayoutParams(new Toolbar.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT));  //expanding searchbar on click
                actv.setHint("Search");
                actv.setCursorVisible(true);
                actv.setBackgroundTintList(ColorStateList.valueOf( getResources().getColor(R.color.white )));
            }
        });

        actv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                actv.setLayoutParams(new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT,Gravity.RIGHT));  //shrinking searchbar
                actv.setCursorVisible(false);

            }
        });


        actv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {      //on text change in box

                if((s.length()>=2) )
                {
                    DbTaskSearchSuggestion db =new DbTaskSearchSuggestion(Home.this,Home.this,title,des,actv);   //search for the keyword in database
                    db.execute(s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {                                      //on suggestion click
                Toast.makeText(Home.this,title.get(i)+ " : " + des.get(i),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {   // To add items to the toolbar bar

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.logout_id) {                                        //logout

            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            builder.setTitle("Log Out");
            builder.setMessage("Are You Sure?");
            builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent i = new Intent(Home.this, LoginActivity.class);
                    pf.setUserLoggedInStatus(Home.this,false);                 // clearing user status and name
                    pf.clearLoggedInEmailAddress(Home.this);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);


                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();

        }
        return super.onOptionsItemSelected(item);
    }
}