package com.example.mycalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<SheetListData>  {


    public ListAdapter(Context context ,ArrayList<SheetListData> FileArraylist)
    {
        super(context,R.layout.buttonlist,FileArraylist);

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        SheetListData list=getItem(position);

        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.buttonlist,parent,false);
        }

        TextView button=convertView.findViewById(R.id.Button1);

        button.setText(list.ButtonText);

        return convertView;
    }
}
