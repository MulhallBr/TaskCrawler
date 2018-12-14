package Adapters;


import android.content.Context;
import android.graphics.PorterDuff;
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

import Interfaces.IconTapCallback;
import Model.Icon;
import Model.Player;
import games.bad.taskcrawler.R;

public class IconInventoryListAdapter extends RecyclerView.Adapter<IconInventoryListAdapter.ViewHolder> {

    private static final String TAG = "ICON INVENTORY LIST ADAPTER";

    private List<Icon> icons = new ArrayList<>();
    private Context context;

    private IconTapCallback listener; //tap callback listener.

    //tap callback setter.
    public void setIconTapCallback(IconTapCallback callback){
        this.listener = callback;
    }

    public IconInventoryListAdapter(List<Icon> icons, Context context) {
        this.icons = icons;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_itemlist_item, parent, false);
        IconInventoryListAdapter.ViewHolder viewHolder = new IconInventoryListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IconInventoryListAdapter.ViewHolder holder, int position) {

        holder.itemIcon.setImageResource(this.context.getResources().getIdentifier(this.icons.get(position).getIconFilename(), "drawable", this.context.getPackageName()));
        holder.itemName.setText(this.icons.get(position).getName());
        holder.itemDescription.setText(""); //nothing set because icons have no descriptions
        if(Player.getPlayer().getEquippedIconId(this.context) == this.icons.get(position).getId()) {
            //highlight it in
            holder.itemName.setTextColor(context.getResources().getColor(R.color.textInverse));
            holder.itemDescription.setTextColor(context.getResources().getColor(R.color.textInverse));
            holder.parentLayout.getBackground().setColorFilter(context.getResources().getColor(R.color.colorPrimaryLight), PorterDuff.Mode.MULTIPLY);
        }else{
            //clear the color highlighting
            holder.parentLayout.getBackground().clearColorFilter();
            holder.itemName.setTextColor(context.getResources().getColor(R.color.textPrimary));
            holder.itemDescription.setTextColor(context.getResources().getColor(R.color.textPrimary));
            //holder.parentLayout.getBackground().setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY); <- DEPRICAATED
        }
        final Icon icon = this.icons.get(position);
        //attach onclick listener....
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onIconTapped(icon);
            }
        });
    }

    public void updateList (List<Icon> icons) {
        if (icons != null) {
            this.icons.clear();
            this.icons.addAll(icons);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return this.icons.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemIcon;
        TextView itemName;
        TextView itemDescription;
        ConstraintLayout parentLayout;

        ViewHolder(View itemView) {
            super(itemView);
            itemIcon = itemView.findViewById( R.id.item_shop_icon);
            itemName = itemView.findViewById(R.id.item_shop_item_title);
            itemDescription = itemView.findViewById(R.id.itemdescription);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
