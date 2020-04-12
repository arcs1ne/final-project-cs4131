package com.example.project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LessonsViewAdapter extends RecyclerView.Adapter<LessonsViewAdapter.ViewHolder>{
    private ArrayList<Lesson> items = new ArrayList<>();
    private Context mcontext;
    private static final String TAG = "tag";
    public LessonsViewAdapter(Context context, ArrayList<Lesson> RatingItems){
        mcontext = context;
        items = RatingItems;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_item_layout, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }
    @NonNull
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindVH");
        // SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        holder.num.setText("Lesson " + items.get(position).getNumber());
        holder.title.setText(items.get(position).getTitle());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(position);
            }
        });
// title
        // recorded on date
        // duration
        // transcript
    }

    private void openDialog(int position){
        AppCompatActivity a = (AppCompatActivity)mcontext;
        LessonDialog.display(a.getSupportFragmentManager(), items.get(position));
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, num;
        LinearLayout root;
        public ViewHolder(@NonNull View view){
            super(view);
            // initialize all the items in the layout_listitem
            title = view.findViewById(R.id.title);
            num = view.findViewById(R.id.num);
            root = view.findViewById(R.id.parent);
        }
    }
}
