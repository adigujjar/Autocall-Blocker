package com.example.autocallblocker.Models;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.autocallblocker.MainActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class CallBarring extends BroadcastReceiver {
    public String number;
    DatabaseHelper databaseHelper;
    SharedPreferences sharedPreferences;
    ContentResolver resolver;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        sharedPreferences = context.getSharedPreferences("radiocheck", Context.MODE_PRIVATE);
        resolver = context.getContentResolver();
        if (intent.getAction().equals("android.intent.action.PHONE_STATE"))
        {
        final TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            telephony.listen(new PhoneStateListener(){
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onCallStateChanged(int state, String incomingNumber) {
                    super.onCallStateChanged(state, incomingNumber);

                    MainActivity.numbertxt.setText(incomingNumber);

                    databaseHelper = new DatabaseHelper(context);
                    String statecall = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                    if (statecall.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                        if (sharedPreferences.getBoolean("unknowncallkey", true)) {

                            try {
                                //block number from phone contacts
                                Log.e("resolver", String.valueOf(resolver));
                                Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(incomingNumber));
                                Cursor c = resolver.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
                                String name = null;
                                if (c != null && c.getCount() != 0) { // cursor not null means number is found contactsTable
                                } else {
                                    endCall(context);
                                }
                            } catch (Exception ex) {
                                /* Ignore */
                                Log.e("exception", ex.getMessage());
                            }
                        } else {
                            //block number in  block list
                            Log.e("Call Barring", "If called");
                            ArrayList<BlackList> arrayList = MainActivity.blocklists;
                            Log.e("thisislistdata", String.valueOf(arrayList.size()));
                            for (int i = 0; i < arrayList.size(); i++) {
                                if (arrayList.get(i).getPhoneNumber().equals(incomingNumber)) {
                                    Log.e("Call Barring", "If called block");
                                    endCall(context);
                                }
                            }
                        }
                    }
                }
            } ,PhoneStateListener.LISTEN_CALL_STATE);
          }
        }

        public void endCall(Context context) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            try {
                //block incoming calls
                Class<?> telephonyManagerClass = Class.forName(telephonyManager.getClass().getName());
                Method getITelephonyMethod = telephonyManagerClass.getDeclaredMethod("getITelephony");
                getITelephonyMethod.setAccessible(true);
                Object iTelephony = getITelephonyMethod.invoke(telephonyManager);
                Class<?> iTelephonyClass = Class.forName(iTelephony.getClass().getName());
                Method endCallMethod = iTelephonyClass.getDeclaredMethod("endCall");
                endCallMethod.setAccessible(true);
                endCallMethod.invoke(iTelephony);
            } catch (ClassNotFoundException e) {
                // ClassNotFoundException
            } catch (NoSuchMethodException e) {
                // NoSuchMethodException
            } catch (IllegalAccessException e) {
                // IllegalAccessException
            } catch (InvocationTargetException e) {
                // InvocationTargetException
            } catch (Exception e) {
                // Some other exception
            }
        }
    }