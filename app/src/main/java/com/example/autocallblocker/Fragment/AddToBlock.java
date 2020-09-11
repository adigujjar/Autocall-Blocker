package com.example.autocallblocker.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.autocallblocker.Models.BlackList;
import com.example.autocallblocker.Models.DatabaseHelper;
import com.example.autocallblocker.R;


public class AddToBlock extends Fragment {
    private Button reset_button, submit_button;
    private EditText phone_edittxt;
    private DatabaseHelper blackListDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_to_block, container, false);
        reset_button = view.findViewById(R.id.reset_btn);
        submit_button = view.findViewById(R.id.submit_btn);
        phone_edittxt = view.findViewById(R.id.phone_et);

        blackListDao = new DatabaseHelper(getContext());

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetFields();
            }
        });
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blockNumber();
                submit_button.setBackgroundColor(Color.parseColor("#008000"));
            }
        });
        return view;
    }
    public void resetFields()
    {
        phone_edittxt.setText("");

    }
    public void blockNumber()
    {
        if(phone_edittxt.getText().toString().trim().length() > 0)
        {
             BlackList phone = new BlackList();
            phone.setPhoneNumber(phone_edittxt.getText().toString());
            blackListDao.addNumber(phone);
            showDialog();
        }
        else
        {
            showMessageDialog("All fields are mandatory !!");
        }
    }
    private void showDialog()
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Phone Number added to block list successfully !!").setCancelable(false);
        alertDialogBuilder.setPositiveButton("Add More",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        resetFields();
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                             if (getFragmentManager().getBackStackEntryCount() > 0)
                             {
                                 getFragmentManager().popBackStack();
                             }
                    }
                });
         AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void showMessageDialog(final String message)
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage(message);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Block Number");
        getActivity().findViewById(R.id.fab).setVisibility(View.GONE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Automatic Call BLocker");
        getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);
    }
}
