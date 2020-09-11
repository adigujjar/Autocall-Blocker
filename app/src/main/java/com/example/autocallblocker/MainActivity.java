package com.example.autocallblocker;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.autocallblocker.Fragment.AddToBlock;
import com.example.autocallblocker.Models.BlackList;
import com.example.autocallblocker.Models.CustomAdapter;
import com.example.autocallblocker.Models.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public FloatingActionButton add_number;
    public static RadioButton radioButton;
    private RadioGroup radioGroup;
    public static TextView numbertxt;
    public static ArrayList<BlackList> blocklists;
    DatabaseHelper databaseHelper;
    CustomAdapter customAdapter;
    ListView listView;
    final int REQUEST_READ= 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add_number = findViewById(R.id.fab);
        listView = findViewById(R.id.listview);
        numbertxt = findViewById(R.id.textnum);
        SharedPreferences sharedPreferences = getSharedPreferences("radiocheck", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("unknowncallkey", true))
        {
            radioButton = findViewById(R.id.radio_Reject_unknown_Calls);
            radioButton.setChecked(true);
        }else {
            radioButton = findViewById(R.id.radio_Reject_Calls_On_List);
            radioButton.setChecked(true);
            databaseHelper = new DatabaseHelper(getApplicationContext());
            blocklists = databaseHelper.getAllContacts();
            customAdapter = new CustomAdapter(blocklists, getApplicationContext());
            listView.setAdapter(customAdapter);
        }
        add_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNumberForBlock();
            }
        });
        radioGroup = findViewById(R.id.radio_radioGroup);

        checkAnswerCallRequest();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                        databaseHelper.deleteContact(String.valueOf(blocklists.get(i).getPhoneNumber()));
                        deleted();

                return true;
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButton = findViewById(i);
                SharedPreferences sharedPreferences = getSharedPreferences("radiocheck", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                switch (radioButton.getText().toString())
                {
                    case "Reject Calls on List":
                    {
                        editor.putBoolean("unknowncallkey", false);
                        editor.apply();
                        editor.commit();
                        dbnumbers();
                         break;
                    }
                    case "Reject unknown Calls":
                    {
                        editor.putBoolean("unknowncallkey", true);
                        editor.apply();
                        editor.commit();
                        blockFromUnknownNumber();
                        break;
                    }
                }
            }
        });
    }
    public void dbnumbers()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Click yes to stop recieving calls from numbers that are you Added in blocklist")
                .setTitle("Block Calls From List").setCancelable(false);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseHelper = new DatabaseHelper(getApplicationContext());
                blocklists = databaseHelper.getAllContacts();
                customAdapter = new CustomAdapter(blocklists, getApplicationContext());

                listView.setAdapter(customAdapter);

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
    public void blockFromUnknownNumber()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("If you click yes the reject calls from unknown unmber you will never receive a call from any unknown number.")
                .setTitle("Reject Unknown Calls").setCancelable(false);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
       }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               onAgree();
            }
        });
        AlertDialog dialog = builder.create();
       dialog.show();
    }

    protected void checkAnswerCallRequest() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE, Manifest.permission.ANSWER_PHONE_CALLS, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS},
                    REQUEST_READ);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    checkAnswerCallRequest();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public void onAgree()
    {

    }
    public void deleted()
    {
        AlertDialog.Builder delete = new AlertDialog.Builder(this);
        delete.setMessage("Deleted");
        AlertDialog alertDialog = delete.create();
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
           super.onBackPressed();
    }

    public void addNumberForBlock()
    {
        Fragment fragment = new AddToBlock();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.addToBackStack("BlockNumber");
        fragmentTransaction.commit();
    }

//    private ArrayList<BlackList> getContactList() {
//        ArrayList<BlackList> arrayList = new ArrayList<BlackList>();
//        ContentResolver cr = getContentResolver();
//        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
//                null, null, null, null);
//
//        if ((cur != null ? cur.getCount() : 0) > 0) {
//            while (cur != null && cur.moveToNext()) {
//                String id = cur.getString(
//                        cur.getColumnIndex(ContactsContract.Contacts._ID));
//                String name = cur.getString(cur.getColumnIndex(
//                        ContactsContract.Contacts.DISPLAY_NAME));
//
//                if (cur.getInt(cur.getColumnIndex(
//                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
//                    Cursor pCur = cr.query(
//                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                            null,
//                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
//                            new String[]{id}, null);
//                    while (pCur.moveToNext()) {
//                       String phoneNo = pCur.getString(pCur.getColumnIndex(
//                                ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        BlackList  blackList = new BlackList();
//                        blackList.setPhoneNumber(phoneNo);
//                        arrayList.add(blackList);
//                    }
//                    pCur.close();
//                }
//            }
//        }
//        if(cur!=null){
//            cur.close();
//        }
//        return arrayList;
//    }
//



}
