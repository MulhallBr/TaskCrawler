package Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Model.Task;
import games.bad.taskcrawler.EditTaskActivity;
import games.bad.taskcrawler.R;


public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private static final String TAG = "TASKLISTRECYCLERVIEWADAPTER";
    private List<Task> tasks = new ArrayList<>();
    private Context context;

    public TaskListAdapter(List<Task> tasks, Context context) {
        this.tasks = tasks;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tasklist_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListAdapter.ViewHolder holder, final int position) {
        //put a log in here.
        context = holder.parentLayout.getContext();
        holder.taskTitle.setText(this.tasks.get(position).getTitle());
        final int taskID = this.tasks.get(position).getId();

        //attach onclick listener....
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, EditTaskActivity.class);
                myIntent.putExtra("task_id", taskID);
                context.startActivity(myIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.tasks.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView taskIcon;
        TextView taskTitle;
        TextView taskInfo;
        ConstraintLayout parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            taskIcon = itemView.findViewById( R.id.taskicon);
            taskTitle = itemView.findViewById(R.id.tasktitle);
            taskInfo = itemView.findViewById(R.id.taskinfo);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

        }
    }

}
