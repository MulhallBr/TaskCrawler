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

import Model.Weapon;
import games.bad.taskcrawler.R;

public class WeaponSelectListAdapter extends RecyclerView.Adapter<WeaponSelectListAdapter.ViewHolder> {

    private static final String TAG = "WEAPON ITEM LIST ADAPTER";

    private List<Weapon> weapons = new ArrayList<>();
    private Context context;

    public WeaponSelectListAdapter(List<Weapon> weapons, Context context) {
        this.weapons = weapons;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_itemlist_item, parent, false);
        WeaponSelectListAdapter.ViewHolder viewHolder = new WeaponSelectListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeaponSelectListAdapter.ViewHolder holder, int position) {

        holder.itemIcon.setImageResource(this.context.getResources().getIdentifier(this.weapons.get(position).getIconFilename(), "drawable", this.context.getPackageName()));
        holder.itemName.setText(this.weapons.get(position).getName());
        holder.itemDescription.setText(this.weapons.get(position).getDescription());
        //attach onclick listener....
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "You clicked the thing!", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.weapons.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemIcon;
        TextView itemName;
        TextView itemDescription;
        ConstraintLayout parentLayout;

        ViewHolder(View itemView) {
            super(itemView);
            itemIcon = itemView.findViewById( R.id.icon_imageview);
            itemName = itemView.findViewById(R.id.icon_title_textview);
            itemDescription = itemView.findViewById(R.id.itemdescription);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
