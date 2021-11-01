package com.example.familysafe;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class ViewHolder extends RecyclerView.ViewHolder {

    LinearLayout root;
    TextView txt_name;
    TextView txt_no;

    public ViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        root= itemView.findViewById(R.id.root);
        txt_name=itemView.findViewById(R.id.txt_contact_name);
        txt_no=itemView.findViewById(R.id.txt_contact_no);

    }

    public void setName(String string){
        txt_name.setText(string);
    }
    public void setNumber(String string){
        txt_no.setText(string);
    }
}
