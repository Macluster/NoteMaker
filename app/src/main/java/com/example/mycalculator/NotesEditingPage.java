package com.example.mycalculator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;

public class NotesEditingPage extends AppCompatActivity  {


   public int FilebtnFlag=0;

    int sheetIndex=0;
    int CurrentSheet=1;
    int RecentSheet=1;
    String SheetsData[]=new String[10];

    Button sheets[]=new Button[10];
    Button AddsheetBtnn;

    EditText TitleEditText;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_editing_page);
        getSupportActionBar().hide();

        TitleEditText=findViewById(R.id.TitleEditText);

        //Adding First sheet

        Intent intent=getIntent();

        if(!intent.getStringExtra("filename").equals("0"))
        {
            try {


                GetNotes(intent.getStringExtra("filename"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {

            AddSheetFun();
        }






        Button Filebtn=findViewById(R.id.FileBtn);
        Filebtn.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                AddFileButtons();
            }
        });


        EditText MainEditor =findViewById(R.id.MainEditText);
        MainEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable s) {
                SheetsData[CurrentSheet-1]=MainEditor.getText().toString();
            }
        });


        ImageButton DeleteBtn=findViewById(R.id.deleteBtn);
        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i=CurrentSheet-1;i<sheetIndex-1;i++)
                {
                    SheetsData[i]=SheetsData[i+1];
                    sheets[i+1].setId(sheets[i].getId());
                    sheets[i+1].setText(sheets[i].getText());
                }
                 LinearLayout scrolLinearlayo=findViewById(R.id.ScrollLinearLayout);
                SheetsData[sheetIndex-1]="";
                scrolLinearlayo.removeView(sheets[sheetIndex-1]);
                sheetIndex--;

                MainEditor.setText(SheetsData[CurrentSheet-1]);




            }
        });





    }


    public  void GetNotes(String filename) throws IOException {

        //For getting File Path
       File dir=new File(Environment.getExternalStorageDirectory(),"MyNotes");
       File path=new File(dir,filename);


      //Tile Edit Text
      TitleEditText.setText(filename.split("[.]")[0]);



      BufferedReader br=new BufferedReader(new FileReader(path));
      String st="",temp;
      while ((temp=br.readLine())!=null)
        {
            st+=temp;
        }






      //Adding Data To sheets Array
      for(int i=1;i<st.split("~").length;i++)
      {
          SheetsData[i-1]=st.split("~")[i];
          AddSheetFun();
      }
      EditText MainEdiText=findViewById(R.id.MainEditText);
      MainEdiText.setText(SheetsData[0]);



        //Tile Edit Text
        TitleEditText.setText(st.split("~")[0]);




        


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void SaveData(String Filename)
    {



        File Maindir= Environment.getExternalStorageDirectory();
        File dir= new File(Maindir.getAbsolutePath()+"/MyNotes");

        if(!Files.exists(dir.toPath()))
        {
            dir.mkdirs();
        }

        File file=new File(dir,Filename+".txt");
        try (FileOutputStream stream = new FileOutputStream(file))
        {
             String data="";
             data+=TitleEditText.getText()+"~"; //Adding Title At the  Start of File to Read Title Easily

            //Adding Real Contents
             for(int i=0;i<sheetIndex;i++)
             data +=SheetsData[i]+"~";

            stream.write(data.getBytes());
            file.setLastModified(new Date().getTime());
            stream.close();



        }
        catch(Exception e)
        {

        }
        finally {

        }



    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void AddFileButtons()
    {
        ConstraintLayout layo = findViewById(R.id.RootLayout);
        ConstraintLayout.LayoutParams layoPara = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        ListView st ;

        if(FilebtnFlag==0) {



            st = new ListView(this);
            st.setBackgroundColor(Color.parseColor("#5b39c6"));
            st.setTranslationZ(2);
             st.setOnItemClickListener(new AdapterView.OnItemClickListener()
             {
                 @RequiresApi(api = Build.VERSION_CODES.O)
                 @Override
                 public  void onItemClick(AdapterView<?> parent, View view, int position, long id)
                 {

                         EditText editText=findViewById(R.id.MainEditText);
                         SaveDialouge(layo);
                      // NotesEditingPage.this.SaveData(editText);


                 }

             });
            layoPara.topToBottom = R.id.FileBtn;
            layoPara.rightToRight = R.id.RootLayout;
            layoPara.leftMargin = 5;
            layoPara.width = 250;
            layoPara.leftToLeft = R.id.RootLayout;
            layoPara.rightMargin = 450;






            ArrayList<SheetListData> list = new ArrayList<>();

            SheetListData filelist = new SheetListData("Save");
            list.add(filelist);
            filelist = new SheetListData("Save As");
            list.add(filelist);

            ListAdapter listAdapter = new ListAdapter(NotesEditingPage.this, list);
            st.setAdapter(listAdapter);


            layo.addView(st, 1,layoPara);

            FilebtnFlag=1;

        }
        else
        {
            layo.removeView(layo.getChildAt(1));
            FilebtnFlag=0;

        }









    }




    public void SaveDialouge(ConstraintLayout layo)
    {




        //save dialouge Box using Linear layout
        LinearLayout ll=new LinearLayout(this);
        ll.setBackgroundColor(Color.parseColor("#F6C6EA"));
        ll.setOrientation(LinearLayout.VERTICAL);

        ConstraintLayout.LayoutParams ConstParaForll=new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
        ConstParaForll.leftToLeft=R.id.RootLayout;
        ConstParaForll.rightToRight=R.id.RootLayout;
        ConstParaForll.topToTop=R.id.RootLayout;
        ConstParaForll.rightMargin=50;
        ConstParaForll.leftMargin=50;
        ConstParaForll.topMargin=400;
        ConstParaForll.width=400;
        ConstParaForll.height=200;


        layo.addView(ll,ConstParaForll);


        //Editor for entering File name

        EditText FilenameEditor=new EditText(this);
        FilenameEditor.setHint("Enter File Name");
        FilenameEditor.setText(TitleEditText.getText());
        ll.addView(FilenameEditor);


        // linear layout for adding two buttons horizontally
        LinearLayout Horll=new LinearLayout(this);
        LinearLayout.LayoutParams HorllPara=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        Horll.setOrientation(LinearLayout.HORIZONTAL);
        ll.addView(Horll);


        //SaveButton
        Button savbtn=new Button(this);
        savbtn.setText("Save");
        LinearLayout.LayoutParams LinearParaForSaveButton=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);
        LinearParaForSaveButton.topMargin=30;
        LinearParaForSaveButton.leftMargin=20;
        savbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                SaveData(FilenameEditor.getText().toString());
                ll.removeAllViews();
                layo.removeView(ll);

            }
        });


        //CancelButton
        Button Cancelbtn=new Button(this);
        Cancelbtn.setText("Cancel");
        LinearLayout.LayoutParams LinearParaForCancelButton=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);
        LinearParaForCancelButton.topMargin=30;
        LinearParaForCancelButton.rightMargin=20;
        Cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll.removeAllViews();
                layo.removeView(ll);

            }
        });



        //Adding save and cancel Buttons
        Horll.addView(savbtn ,LinearParaForSaveButton);
        Horll.addView(Cancelbtn,LinearParaForCancelButton);




    }


    public void AddSheetFun()
    {

        EditText MainEditor=findViewById(R.id.MainEditText);


      LinearLayout linearLayout=findViewById(R.id.ScrollLinearLayout);



        sheets[sheetIndex]=new Button(this);
        sheets[sheetIndex].setId(sheetIndex);
        sheets[sheetIndex].setText("Sheet"+String.valueOf(sheetIndex+1) );
        sheets[sheetIndex].setBackgroundColor(Color.parseColor("#5b39c6"));
        sheets[sheetIndex].setTextSize(10);
        sheets[sheetIndex].setTextColor(Color.parseColor("#FFFFFF"));
        CurrentSheet=sheetIndex+1;
        MainEditor.setText(SheetsData[sheetIndex]);

        sheets[sheetIndex].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentSheet=  (((Button)v).getId())+1;
                EditText MainEditor=findViewById(R.id.MainEditText);
                MainEditor.setText(SheetsData[CurrentSheet-1]);

            }
        });
        LinearLayout.LayoutParams LinearParamsForsheetbtn=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearParamsForsheetbtn.width=120;
        LinearParamsForsheetbtn.height=70;
        LinearParamsForsheetbtn.topMargin=10;
        LinearParamsForsheetbtn.leftMargin=5;



        linearLayout.addView(sheets[sheetIndex++],LinearParamsForsheetbtn);

        linearLayout.removeView(AddsheetBtnn);
        AddsheetBtnn=new Button(this);
        AddsheetBtnn.setBackgroundResource(R.drawable.layout_bg);
        AddsheetBtnn.setText("+");
        AddsheetBtnn.setWidth(10);
        AddsheetBtnn.setHeight(30);
        AddsheetBtnn.setTextColor(Color.parseColor("#FFFFFF"));
        AddsheetBtnn.setBackgroundColor(Color.parseColor("#5b39c6"));

        LinearLayout.LayoutParams LinearParamsForAddsheetbtn=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearParamsForAddsheetbtn.width=50;
        LinearParamsForAddsheetbtn.height=50;
        LinearParamsForAddsheetbtn.topMargin=11;
        LinearParamsForAddsheetbtn.leftMargin=10;

        AddsheetBtnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddSheetFun();

            }
        });

        linearLayout.addView(AddsheetBtnn,LinearParamsForAddsheetbtn);



    }








}