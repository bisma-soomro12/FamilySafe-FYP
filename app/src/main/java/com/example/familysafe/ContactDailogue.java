package com.example.familysafe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ContactDailogue extends AppCompatDialogFragment {
        EditText nameEdt;
        EditText noEdt;
    ContactDialogueListner listner;
    @Override
    public Dialog onCreateDialog(@Nullable  Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.contact_dialogue,null);
        builder.setView(view)
                .setTitle("Add Contacts")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String  contact_name=nameEdt.getText().toString();
                        String contact_no=noEdt.getText().toString();
                        listner.saveContact(contact_name,contact_no);
                    }
                });
        nameEdt=view.findViewById(R.id.name_dail);
         noEdt=view.findViewById(R.id.no_dail);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
        listner=(ContactDialogueListner)context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString()+ " must implement contact dialogue listiner");
        }
    }

    public interface ContactDialogueListner{
        void saveContact(String name,String no);
    }
}

