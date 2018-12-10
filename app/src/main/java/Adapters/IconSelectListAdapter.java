package Adapters;

import android.app.Activity;
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
        final int icon_id = this.icons.get(position).getId();

        //attach onclick listener....
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("icon_id",icon_id);
                ((Activity)context).setResult(Activity.RESULT_OK, data);
                ((Activity)context).finish();
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
            icon_imageview = itemView.findViewById( R.id.item_shop_icon);
            icon_textview = itemView.findViewById(R.id.item_shop_item_title);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }


}