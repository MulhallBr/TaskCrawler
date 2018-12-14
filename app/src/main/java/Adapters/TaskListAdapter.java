package Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Interfaces.TaskTapCallback;
import Model.Task;
import games.bad.taskcrawler.NotificationService;
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
        holder.taskLength.setText(this.tasks.get(position).getLengthAsString(true));
        holder.taskDueDate.setText(String.format("Due %s", this.tasks.get(position).getTimeUntilDueAsString()));

        if(this.tasks.get(position).getTimeUntilDueInSeconds() < 0) {
            // The task is overdue!
            // Sets the Task to a red background.
            holder.parentLayout.getBackground().setColorFilter(context.getResources().getColor(R.color.overdue), PorterDuff.Mode.MULTIPLY);
        }else{
            holder.parentLayout.getBackground().clearColorFilter();
        }

        holder.taskIcon.setImageResource(this.tasks.get(position).getIconResourceId(context, context.getResources()));

        final Task task = this.tasks.get(position);

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
        TextView taskLength;
        TextView taskDueDate;
        ConstraintLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            taskIcon = itemView.findViewById( R.id.taskicon);
            taskTitle = itemView.findViewById(R.id.tasktitle);
            taskLength = itemView.findViewById(R.id.tasklength);
            taskDueDate = itemView.findViewById(R.id.taskdatedue);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

}
