package com.example.mycalculator;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotesListData {


    String FileName;
    String Path;
    String Date;



    public  NotesListData(String FileName ,String Path,String Date)
    {

        this.FileName=FileName;
        this.Path=Path;
        this.Date=Date;




    }



}
