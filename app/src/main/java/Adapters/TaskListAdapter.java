package Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Interfaces.TaskTapCallback;
import Model.Icon;
import Model.Task;
import games.bad.taskcrawler.EditTaskActivity;
import games.bad.taskcrawler.MainActivity;
import games.bad.taskcrawler.R;


public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private static final String TAG = "TASK LIST ADAPTER";
    private List<Task> tasks = new ArrayList<>();
    private Context context;
    private TaskTapCallback listener; //tap callback listener.

    //tap callback setter.
    public void setTapTaskCallback(TaskTapCallback callback){
        this.listener = callback;
    }

    public TaskListAdapter(List<Task> tasks, Context context) {
        Collections.sort(tasks);
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

        //how long until this is due:
        holder.taskTitle.setText(this.tasks.get(position).getTitle());

        holder.taskIcon.setImageResource(this.tasks.get(position).getIconResourceId(context, context.getResources()));

        long due_in_seconds = this.tasks.get(position).getTimeUntilDueInSeconds();
        long due_in_days;
        long due_in_hours = 0;
        long due_in_minutes = 0;

        due_in_days = due_in_seconds / (60 * 60 * 24); //set the value to the output of the division operation, without remainder.
        due_in_seconds = due_in_seconds % (60 * 60 * 24);

        due_in_hours = due_in_seconds / (60 * 60);
        due_in_seconds = due_in_seconds % (60 * 60);

        due_in_minutes = due_in_seconds / (60);
        due_in_seconds = due_in_seconds % (60);

        StringBuilder sb = new StringBuilder();

        if(due_in_days != 0) {
            sb.append("%d day");
            if(due_in_days != 1) {
                sb.append("s");
            }
        }
        String dayString = sb.toString();
        sb.setLength(0);

        if(due_in_hours != 0) {
            sb.append("%d hour");
            if(due_in_hours != 1) {
                sb.append("s");
            }
        }
        String hourString = sb.toString();
        sb.setLength(0);

        if(due_in_minutes != 0) {
            sb.append("%d minute");
            if(due_in_minutes != 1) {
                sb.append("s");
            }
        }
        String minuteString = sb.toString();
        sb.setLength(0);

        sb.append("Due in ");
        if(due_in_days > 3) { //days are greater than three
            sb.append(dayString);
            holder.taskInfo.setText(String.format(sb.toString(), due_in_days));
        }else if( due_in_days != 0) { //days are less than or equal to three, but not zero:
                sb.append(dayString);
                sb.append(", ");
                sb.append(hourString);
                holder.taskInfo.setText(String.format(sb.toString(), due_in_days, due_in_hours));
        }else{ //days are zero
            if(due_in_hours > 12) {
                sb.append(hourString);
                holder.taskInfo.setText(String.format(sb.toString(), due_in_hours));
            }else{
                sb.append(hourString);
                sb.append(", ");
                sb.append(minuteString);
                holder.taskInfo.setText(String.format(sb.toString(), due_in_hours, due_in_minutes));
            }
        }


        final Task task = this.tasks.get(position);
        final int taskID = task.getId();

        //attach onclick listener....
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the callback method from MainActivity.
                listener.onTaskTapped(task);
            }
        });
    }

    public void updateList (List<Task> items) {
        if (items != null) {
            tasks.clear();
            Collections.sort(items);
            tasks.addAll(items);
            notifyDataSetChanged();
        }
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
