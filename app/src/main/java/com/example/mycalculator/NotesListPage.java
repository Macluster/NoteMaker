package com.example.mycalculator;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class NotesListPage extends AppCompatActivity {

    public int SwipeDirection=0;
    public  int currentListViewPos=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_list_page);
        getSupportActionBar().hide();




        File Maindir= Environment.getExternalStorageDirectory();
        File dir= new File(Maindir.getAbsolutePath()+"/MyNotes");
        dir.mkdir();

        File fileList[]=  dir.listFiles();

        File []sortedFiles=getSortedFiles(fileList);
        ArrayList<NotesListData> arrayList=new ArrayList<>();


        for(int i=0;i<sortedFiles.length;i++)
        {
            String []datesplitter=String.valueOf(new Date(sortedFiles[i].lastModified())).split(" ");
             String CurrIterationDate=datesplitter[1]+" "+datesplitter[2]+" "+datesplitter[5]+" "+datesplitter[3];

            NotesListData data=new NotesListData(sortedFiles[i].getName(),sortedFiles[i].getAbsolutePath(),CurrIterationDate);
            arrayList.add(data);
        }





        AllNotesAdapter adapter=new AllNotesAdapter(NotesListPage.this,arrayList);
        RecyclerView listView=findViewById(R.id.AllNotesListView);



        LinearLayoutManager layoutManager=new LinearLayoutManager(NotesListPage.this,LinearLayoutManager.VERTICAL,false);
        listView.setLayoutManager(layoutManager);

        listView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove( RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder,RecyclerView.ViewHolder target) {


                return false;

            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {


             File path=new File(Environment.getExternalStorageDirectory(),"MyNotes");
             File file=new File(path,arrayList.get(viewHolder.getAdapterPosition()).FileName);
             file.delete();
             arrayList.remove(viewHolder.getAdapterPosition());





            }

             Canvas canva;

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @SuppressLint("ResourceAsColor")
            @Override
            public void onChildDraw( Canvas c,  RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


                canva=c;

                   Paint p = new Paint();
                   //to reset canvas to white ,Inorder to handle next swipe
                   p.setColor(Color.parseColor("#FFFFFF"));
                   c.drawPaint(p);

                   //Drawing Rectangle
                   p.setColor(Color.parseColor("#6D8299"));
                   int recttop = viewHolder.getAdapterPosition() * 200 + viewHolder.getAdapterPosition() * 20;
                   c.drawRect(c.getWidth() - 400, recttop, c.getWidth(), recttop + 200, p);


                   //Drawing Delete Image
                   Bitmap B = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
                   int top = recttop + 50;
                   Rect rect = new Rect(c.getWidth() - 150, top, c.getWidth() - 50, recttop + 150);
                   c.drawBitmap(B, null, rect, null);




                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);


            }


            @Override
            public void clearView( RecyclerView recyclerView,  RecyclerView.ViewHolder viewHolder) {

                Paint p = new Paint();
                //to reset canvas to white ,Inorder to handle next swipe
                p.setColor(Color.parseColor("#FFFFFF"));
                canva.drawPaint(p);
                super.onChildDraw(canva,recyclerView,viewHolder,0,0,1,true);


                super.clearView(recyclerView, viewHolder);
            }
        };


        ItemTouchHelper helper=new ItemTouchHelper(simpleCallback);
        helper.attachToRecyclerView(listView);


        ImageButton SerachBtn=findViewById(R.id.AllFilesSearchBtn);
        SerachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText SearcheditText=findViewById(R.id.AllFilesSerachEditText);
                SearcheditText.setVisibility(View.VISIBLE);

                SearcheditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {



                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        arrayList.clear();
                        for(int i=0;i<fileList.length;i++)
                        {
                            if(fileList[i].getName().toUpperCase().startsWith(SearcheditText.getText().toString().toUpperCase()))
                            {


                                    String []datesplitter=String.valueOf(new Date(fileList[i].lastModified())).split(" ");
                                    String CurrIterationDate=datesplitter[1]+" "+datesplitter[2]+" "+datesplitter[5]+" "+datesplitter[3];

                                    NotesListData data=new NotesListData(fileList[i].getName(),fileList[i].getAbsolutePath(),CurrIterationDate);
                                    arrayList.add(data);


                            }

                        }

                        if(SearcheditText.getText().toString()=="")
                        {

                            for(int i=0;i<sortedFiles.length;i++)
                            {
                                String []datesplitter=String.valueOf(new Date(sortedFiles[i].lastModified())).split(" ");
                                String CurrIterationDate=datesplitter[1]+" "+datesplitter[2]+" "+datesplitter[5]+" "+datesplitter[3];

                                NotesListData data=new NotesListData(sortedFiles[i].getName(),sortedFiles[i].getAbsolutePath(),CurrIterationDate);
                                arrayList.add(data);
                            }


                        }

                        adapter.notifyDataSetChanged();



                    }
                });


            }
        });












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