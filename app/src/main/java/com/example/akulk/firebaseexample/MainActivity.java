package com.example.akulk.firebaseexample;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity {

     Button B1,B2;

    TextView T1;
    EditText Ed1,Ed2,Ed3,Ed4,Ed5;
    private int year, month, day;
    List<userInformation> users;
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        T1 = (TextView)findViewById(R.id.textView3);
        Ed1= (EditText)findViewById(R.id.editText);
        Ed2= (EditText)findViewById(R.id.editText2);
        Ed3= (EditText)findViewById(R.id.editText3);
        Ed4= (EditText)findViewById(R.id.editText4);
        Ed5= (EditText)findViewById(R.id.editText5);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month, day);
        B1 = (Button)findViewById(R.id.b1);
        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("EXTRA_TRAVELLERS", Ed2.getText().toString()+" "+Ed3.getText().toString());
                intent.putExtra("EXTRA_DATE",Ed5.getText().toString());
                startActivity(intent);
            }
        });
        Ed5.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    showDialog(999);
            }
        });

        B2 = (Button)findViewById(R.id.b2);
        B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                basicWrite();
            }
        });
    }


    public void basicWrite() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Records");
        String name = Ed1.getText().toString();
        String Source = Ed2.getText().toString();
        String Destination = Ed3.getText().toString();
        String strDate = Ed5.getText().toString();
        String Phone = Ed4.getText().toString();
        myRef.push().setValue(new userInformation(name,Source,Destination,strDate,Long.parseLong(Phone)));
        Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_SHORT).show();
        Ed1.setText("");
        Ed2.setText("");
        Ed3.setText("");
        Ed4.setText("");

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1,arg2,arg3);
        }

    };
    public void showDate(int year, int month, int day) {
        Ed5.setText(new StringBuilder().append(month+1).append("/")
                .append(day).append("/").append(year));
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            Calendar c = Calendar.getInstance();
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

}
class userInformation {
    String strname,strSourceDestination,date;
    long Phone;

    public userInformation(String a_strname, String a_strSource, String a_strDestination, String a_dDate, long a_lPhone)
    {
        strname = a_strname;
        strSourceDestination = a_strSource+" "+ a_strDestination;
        date = a_dDate;
        Phone = a_lPhone;
    }
}

