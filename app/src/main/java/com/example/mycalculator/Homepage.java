package com.example.mycalculator;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class Homepage extends AppCompatActivity  {

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(requestCode==1)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(Homepage.this,"Permission granted",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        getSupportActionBar().hide();




        if(getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
        {

        }
        else
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }






        ImageButton CreateNoteBtn=findViewById(R.id.CreateNoteBtn);
        CreateNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Homepage.this, NotesEditingPage.class);
                intent.putExtra("filename","0");
                startActivity(intent);
            }
        });

        ImageButton AllNotesBtn=findViewById(R.id.AllNotesBtn);
        AllNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Homepage.this,NotesListPage.class);
                startActivity(intent);


            }
        });




        //To get No  Recent files

        File Maindir= Environment.getExternalStorageDirectory();
        File dir= new File(Maindir.getAbsolutePath()+"/MyNotes");
        dir.mkdir();

          File fileList[]=  dir.listFiles();

        File []sortedFiles=getSortedFiles(fileList);
        if(fileList.length>0)
        {
            ConstraintLayout RecentNotesTempImageLayo =findViewById(R.id.RecentNotesTempImageLayout);
            RecentNotesTempImageLayo.setVisibility(View.INVISIBLE);
        }


         DisplayRecentNotesCircles(sortedFiles);









    }


    public void  DisplayRecentNotesCircles( File []sortedFiles)
    {


        //To get Screen Display
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrix = new DisplayMetrics();
        display.getMetrics(outMetrix);
        float Density = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrix.heightPixels / Density;
        float dpwidth = outMetrix.widthPixels / Density;



        ArrayList<String> arrayList = new ArrayList<>();



        for(int i=0;i<sortedFiles.length;i++)
        {
            if(i==5)
                break;
            arrayList.add(sortedFiles[i].getName());

        }

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        CardView HistoryHolderlayout = findViewById(R.id.NotesHistorylayout);


        for (int i = 0; i < arrayList.size(); i++) {

            View view = inflater.inflate(R.layout.history_view, null);
            TextView HistoryText = view.findViewById(R.id.HistoryCardText);
            CardView HistoryCard = view.findViewById(R.id.HistoryCard);
            HistoryCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Intent intent=new Intent(Homepage.this, NotesEditingPage.class);
                   intent.putExtra("filename",HistoryText.getText());
                   startActivity(intent);
                }
            });


            if (i == 0) {

                HistoryCard.animate().translationX(20).alpha(1).setDuration(1000);
                HistoryCard.animate().translationY(50).alpha(1).setDuration(1000);
                HistoryCard.setCardBackgroundColor(Color.parseColor("#F0D9FF"));
            }

            if (i == 1) {
                ViewGroup.LayoutParams card = HistoryCard.getLayoutParams();
                card.height = 200;
                card.width = 200;
                HistoryCard.setRadius(100);
                HistoryCard.animate().translationX(outMetrix.widthPixels-250).alpha(1).setDuration(1000);
                HistoryCard.animate().translationY(100).alpha(1).setDuration(1000);
            }


            if (i == 2) {
                ViewGroup.LayoutParams card = HistoryCard.getLayoutParams();
                card.height = 350;
                card.width = 350;
                HistoryCard.setRadius(175);
                HistoryCard.setCardBackgroundColor(Color.parseColor("#FFB344"));
                HistoryCard.animate().translationX(outMetrix.widthPixels-400).alpha(1).setDuration(1000);
                HistoryCard.animate().translationY(460).alpha(1).setDuration(1000);
            }

            if (i == 3) {
                ViewGroup.LayoutParams card = HistoryCard.getLayoutParams();

                card.height = 250;
                card.width = 250;
                HistoryCard.setRadius(125);
                HistoryCard.setCardBackgroundColor(Color.parseColor("#00A19D"));
                HistoryCard.animate().translationX(20).alpha(1).setDuration(1000);
                HistoryCard.animate().translationY(540).alpha(1).setDuration(1000);
            }

            if (i == 4) {
                ViewGroup.LayoutParams card = HistoryCard.getLayoutParams();
                HistoryCard.setCardBackgroundColor(Color.parseColor("#57CC99"));
                HistoryCard.animate().translationX(70).alpha(1).setDuration(1000);
                HistoryCard.animate().translationY(800).alpha(1).setDuration(1000);
            }


            HistoryText.setText(arrayList.get(i));
            HistoryHolderlayout.addView(view, i);
        }


    }



    public File[] getSortedFiles(File[] files)
    {

        String []Dates=new String[files.length];


        String []Temp;
        int index=0;

        for(int i=0;i<files.length;i++)
        {
        Dates[i]=new Date(files[i].lastModified()).toString();
        }


        //Sort By Month
        for(int j=0;j<files.length-1;j++)
        for(int i=0;i<files.length-1;i++)
        {
         if(getMonthNumber(Dates[i].split(" ")[1])>getMonthNumber(Dates[i+1].split(" ")[1]))
         {
            File temp=files[i];
            files[i]=files[i+1];
            files[i+1]=temp;





         }
         else if(getMonthNumber(Dates[i].split(" ")[1])==getMonthNumber(Dates[i+1].split(" ")[1]))
         {
             if(Integer.parseInt(Dates[i].split(" ")[2])>Integer.parseInt(Dates[i+1].split(" ")[2]))
             {
                 File temp=files[i];
                 files[i]=files[i+1];
                 files[i+1]=temp;
             }
             else if(Integer.parseInt(Dates[i].split(" ")[2])==Integer.parseInt(Dates[i+1].split(" ")[2]))
             {
                 if(Integer.parseInt(Dates[i].split(" ")[3].split(":")[0])>Integer.parseInt(Dates[i+1].split(" ")[3].split(":")[0]))
                 {
                     File temp=files[i];
                     files[i]=files[i+1];
                     files[i+1]=temp;
                 }
                 else if (Integer.parseInt(Dates[i].split(" ")[3].split(":")[0])==Integer.parseInt(Dates[i+1].split(" ")[3].split(":")[0]))
                 {
                     if(Integer.parseInt(Dates[i].split(" ")[3].split(":")[1])>Integer.parseInt(Dates[i+1].split(" ")[3].split(":")[1]));
                     {
                         File temp=files[i];
                         files[i]=files[i+1];
                         files[i+1]=temp;
                     }
                 }


             }

         }



        }







        return files;
    }



    public int getMonthNumber(String Month)
    {
        String [][]Months={
                {"Jan","1"},
                {"Feb","2"},
                {"Mar","3"},
                {"Apr","4"},
                {"May","5"},
                {"Jun","6"},
                {"Jul","7"},
                {"Aug","8"},
                {"Sep","9"},
                {"Oct","10"},
                {"Nov","11"},
                {"Dec","12"},
        };


        for(int i=0;i<Months.length;i++)
        {
            if(Months[i][0]==Month);
            {
                return Integer.parseInt(Months[i][1]);
            }
        }


            return 0;
    }

}