package com.example.mycalculator;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AllNotesAdapter extends RecyclerView.Adapter<AllNotesAdapter.ViewHolder> {


    Context context;
    ArrayList<NotesListData> arrayList;


    public  AllNotesAdapter(Context context,ArrayList<NotesListData>arrayList)
    {


        this.context=context;
        this.arrayList=arrayList;


    }


    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.notes_list_template,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AllNotesAdapter.ViewHolder holder, int position) {

        NotesListData data=arrayList.get(position);

        holder.Filename.setText(data.FileName);
        holder.DateTime.setText(data.Date);
        holder.Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,NotesEditingPage.class);
                intent.putExtra("filename",data.FileName);
                context.startActivity(intent);


            }
        });



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView Filename;
        TextView DateTime;
        CardView Card;

        public ViewHolder( View itemView) {
            super(itemView);
            Filename=itemView.findViewById(R.id.NoteNameTextView);
            DateTime=itemView.findViewById(R.id.NoteDateTimeTextView);
            Card=itemView.findViewById(R.id.NoteListCard);
        }
    }
}
