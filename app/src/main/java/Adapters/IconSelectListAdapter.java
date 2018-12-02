package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Model.Icon;
import games.bad.taskcrawler.R;

public class IconSelectListAdapter extends RecyclerView.Adapter<IconSelectListAdapter.ViewHolder> {
    private static final String TAG = "ICONSELECTITEMLISTADAPTER";

    private List<Icon> icons = new ArrayList<>();
    private Context context;

    public IconSelectListAdapter(List<Icon> icons, Context context) {
        this.icons = icons;
        this.context = context;
    }
    @NonNull
    @Override
    public IconSelectListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_select_item, parent, false);
        IconSelectListAdapter.ViewHolder viewHolder = new IconSelectListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IconSelectListAdapter.ViewHolder holder, int position) {
        holder.icon_imageview.setImageResource(this.context.getResources().getIdentifier(this.icons.get(position).getIconFilename(), "drawable", this.context.getPackageName()));
        holder.icon_textview.setText(this.icons.get(position).getName());

        //attach onclick listener....
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "You clicked the icon!!!!!!", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.icons.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon_imageview;
        TextView icon_textview;
        ConstraintLayout parentLayout;

        ViewHolder(View itemView) {
            super(itemView);
            icon_imageview = itemView.findViewById( R.id.icon_imageview);
            icon_textview = itemView.findViewById(R.id.icon_title_textview);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }


}