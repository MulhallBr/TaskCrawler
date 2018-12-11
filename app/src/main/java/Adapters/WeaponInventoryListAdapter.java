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

import Interfaces.WeaponTapCallback;
import Model.Player;
import Model.Weapon;
import games.bad.taskcrawler.R;

public class WeaponInventoryListAdapter extends RecyclerView.Adapter<WeaponInventoryListAdapter.ViewHolder> {

    private static final String TAG = "WEAPON ITEM LIST ADAPTER";

    private List<Weapon> weapons = new ArrayList<>();
    private Context context;

    private WeaponTapCallback listener; //tap callback listener.

    //tap callback setter.
    public void setTapWeaponCallback(WeaponTapCallback callback){
        this.listener = callback;
    }

    public WeaponInventoryListAdapter(List<Weapon> weapons, Context context) {
        this.weapons = weapons;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_itemlist_item, parent, false);
        WeaponInventoryListAdapter.ViewHolder viewHolder = new WeaponInventoryListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeaponInventoryListAdapter.ViewHolder holder, int position) {

        holder.itemIcon.setImageResource(this.context.getResources().getIdentifier(this.weapons.get(position).getIconFilename(), "drawable", this.context.getPackageName()));
        holder.itemName.setText(this.weapons.get(position).getName());
        holder.itemDescription.setText(this.weapons.get(position).getDescription());
        if(Player.getPlayer().getEquippedWeaponId(this.context) == this.weapons.get(position).getId()) {
            //highlight it in
            holder.itemName.setTextColor(context.getResources().getColor(R.color.textInverse));
            holder.itemDescription.setTextColor(context.getResources().getColor(R.color.textInverse));
            holder.parentLayout.getBackground().setColorFilter(context.getResources().getColor(R.color.colorPrimaryLight), PorterDuff.Mode.MULTIPLY);
        }else{
            //clear the color highlighting
            holder.itemName.setTextColor(context.getResources().getColor(R.color.textPrimary));
            holder.itemDescription.setTextColor(context.getResources().getColor(R.color.textPrimary));
            holder.parentLayout.getBackground().clearColorFilter();
            //holder.parentLayout.getBackground().setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY); <- DEPRICAATED
        }
        final Weapon weapon = this.weapons.get(position);
        //attach onclick listener....
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onWeaponTapped(weapon);
            }
        });
    }

    public void updateList (List<Weapon> weapons) {
        if (weapons != null) {
            this.weapons.clear();
            //Collections.sort(weapons); sort by required level!!!
            this.weapons.addAll(weapons);
            notifyDataSetChanged();
        }
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
            itemIcon = itemView.findViewById( R.id.item_shop_icon);
            itemName = itemView.findViewById(R.id.item_shop_item_title);
            itemDescription = itemView.findViewById(R.id.itemdescription);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
