package com.example.familysafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolderClass> {
        String[] data1,data2;
        Context context;

    public ContactsAdapter(Context ct,String[] s1,String[] s2){
            data1=s1;
            data2=s2;
            context=ct;

    }

    @NonNull
    @Override
    public ContactsAdapter.MyViewHolderClass onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(context);
        View view =layoutInflater.inflate(R.layout.contacts_layout,parent,false);
        return new MyViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ContactsAdapter.MyViewHolderClass holder, int position) {
        holder.txt_cont_name.setText(data1[position]);
        holder.txt_cont_no.setText(data2[position]);
    }

    @Override
    public int getItemCount() {
        return data1.length;
    }

    public class MyViewHolderClass extends RecyclerView.ViewHolder{
            TextView txt_cont_name,txt_cont_no;
        public MyViewHolderClass(@NonNull  View itemView) {
            super(itemView);
            txt_cont_name=itemView.findViewById(R.id.txt_contact_name);
            txt_cont_no=itemView.findViewById(R.id.txt_contact_no);
        }
    }
}
