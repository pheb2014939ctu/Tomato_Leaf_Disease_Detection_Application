//package com.saurabh.homepage;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//
//import java.util.ArrayList;
//
//public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
//
//    private Context context;
//    Activity activity;
//    private ArrayList<String> note_id;
//    private ArrayList<String> note_title;
//    private ArrayList<String> note_desc;
//    private ArrayList<String> note_timestamp;
//
//    Animation tranlate_anim;
//
//    CustomAdapter(Activity activity, Context context, ArrayList<String> note_id, ArrayList<String> note_title, ArrayList<String> note_desc, ArrayList<String> note_timestamp) {
//        this.activity = activity;
//        this.context = context;
//        this.note_id = note_id;
//        this.note_title = note_title;
//        this.note_desc = note_desc;
//        this.note_timestamp = note_timestamp;
//    }
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.my_row, parent, false);
//        return new MyViewHolder(view);
//    }
//
//
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
//        holder.addNoteId_txt.setText(String.valueOf(note_id.get(position)));
//        holder.note_title_txt.setText(String.valueOf(note_title.get(position)));
//        holder.note_desc_txt.setText(String.valueOf(note_desc.get(position)));
//        // Set the timestamp to the corresponding TextView
//        holder.note_timestamp_txt.setText(String.valueOf(note_timestamp.get(position)));
//
//        //img
//        // Load the same image for all items
//        Glide.with(context)
//                .load(R.drawable.take_care_plant_icom)
//                .placeholder(R.drawable.take_care_plant_icom)
//                .into(holder.noteImage);
//
//        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, UpdateActivity.class);
//                intent.putExtra("id", String.valueOf(note_id.get(position)));
//                intent.putExtra("title", String.valueOf(note_title.get(position)));
//                intent.putExtra("desc", String.valueOf(note_desc.get(position)));
//                intent.putExtra("timestamp", String.valueOf(note_timestamp.get(position)));
//                activity.startActivityForResult(intent, 1);
//            }
//        });
//
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return note_id.size(); // Return the size of your data list
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//        ImageView noteImage;  // Add ImageView for image
//        TextView addNoteId_txt, note_title_txt, note_desc_txt, note_timestamp_txt;
//        LinearLayout mainLayout;
//
//        public MyViewHolder(View view) {
//
//            super(view);
//            noteImage = view.findViewById(R.id.img_take_care_plant);
//            addNoteId_txt = view.findViewById(R.id.addNoteId_txt);
//            note_title_txt = view.findViewById(R.id.note_title_txt);
//            note_desc_txt = view.findViewById(R.id.note_desc_txt);
//            note_timestamp_txt = view.findViewById(R.id.note_timestamp_txt);
//            mainLayout = itemView.findViewById(R.id.mainLayout);
//            //Animate recyclerview
//            tranlate_anim = AnimationUtils.loadAnimation(context, R.anim.tranlate_anim);
//            mainLayout.setAnimation(tranlate_anim);
//        }
//    }
//}


package com.saurabh.homepage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    Activity activity;
    private ArrayList<String> note_id;
    private ArrayList<String> note_title;
    private ArrayList<String> note_desc;
    private ArrayList<String> note_timestamp;

    Animation tranlate_anim;

    public interface OnDeleteButtonClickListener {
        void onDeleteButtonClick(int position);
    }
    private OnDeleteButtonClickListener onDeleteButtonClickListener;
    public void setOnDeleteButtonClickListener(OnDeleteButtonClickListener listener) {
        this.onDeleteButtonClickListener = listener;
    }

    CustomAdapter(Activity activity, Context context, ArrayList<String> note_id, ArrayList<String> note_title, ArrayList<String> note_desc, ArrayList<String> note_timestamp) {
        this.activity = activity;
        this.context = context;
        this.note_id = note_id;
        this.note_title = note_title;
        this.note_desc = note_desc;
        this.note_timestamp = note_timestamp;



    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.addNoteId_txt.setText(String.valueOf(note_id.get(position)));
        holder.note_title_txt.setText(String.valueOf(note_title.get(position)));
        holder.note_desc_txt.setText(String.valueOf(note_desc.get(position)));
        // Set the timestamp to the corresponding TextView
        holder.note_timestamp_txt.setText(String.valueOf(note_timestamp.get(position)));

        // Load the same image for all items
        Glide.with(context)
                .load(R.drawable.take_care_plant_icom)
                .placeholder(R.drawable.take_care_plant_icom)
                .into(holder.noteImage);

        //delete
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDeleteButtonClickListener != null) {
                    onDeleteButtonClickListener.onDeleteButtonClick(position);
                }
            }
        });


        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(note_id.get(position)));
                intent.putExtra("title", String.valueOf(note_title.get(position)));
                intent.putExtra("desc", String.valueOf(note_desc.get(position)));
                intent.putExtra("timestamp", String.valueOf(note_timestamp.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        }


        );

    }

//    public void deleteItem(int position) {
//        // Remove the item from your ArrayLists
//        note_id.remove(position);
//        note_title.remove(position);
//        note_desc.remove(position);
//        note_timestamp.remove(position);
//
//        // Notify the adapter that an item has been removed
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, getItemCount());
//    }

    public void deleteItem(int position) {
        MyDatabasesHelper myDB = new MyDatabasesHelper(context);

        // Get the ID of the item to be deleted
        String idToDelete = note_id.get(position);

        // Remove the item from your ArrayLists
        note_id.remove(position);
        note_title.remove(position);
        note_desc.remove(position);
        note_timestamp.remove(position);

        // Delete the item from the database using the ID
        myDB.deleteOneRow(idToDelete);

        // Notify the adapter that an item has been removed
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }




    @Override
    public int getItemCount() {
        return note_id.size(); // Return the size of your data list
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView noteImage;  // Add ImageView for image
        TextView addNoteId_txt, note_title_txt, note_desc_txt, note_timestamp_txt;
        LinearLayout mainLayout;
        //delete
        ImageView deleteButton;


        public MyViewHolder(View view) {

            super(view);
            noteImage = view.findViewById(R.id.img_take_care_plant);
            addNoteId_txt = view.findViewById(R.id.addNoteId_txt);
            note_title_txt = view.findViewById(R.id.note_title_txt);
            note_desc_txt = view.findViewById(R.id.note_desc_txt);
            note_timestamp_txt = view.findViewById(R.id.note_timestamp_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            //DELETE
            deleteButton = view.findViewById(R.id.deleteButton);

            //Animate recyclerview
            tranlate_anim = AnimationUtils.loadAnimation(context, R.anim.tranlate_anim);
            mainLayout.setAnimation(tranlate_anim);
        }
    }
}
