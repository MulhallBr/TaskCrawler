package Adapters;


import android.content.Context;
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

public class WeaponShopListAdapter extends RecyclerView.Adapter<WeaponShopListAdapter.ViewHolder> {

    private static final String TAG = "WEAPON SHOP LIST ADAPTER";

    private List<Weapon> weapons = new ArrayList<>();
    private Context context;
    private WeaponTapCallback listener; //tap callback listener.

    //tap callback setter.
    public void setTapWeaponCallback(WeaponTapCallback callback){
        this.listener = callback;
    }

    public WeaponShopListAdapter(List<Weapon> weapons, Context context) {
        this.weapons = weapons;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_item, parent, false);
        WeaponShopListAdapter.ViewHolder viewHolder = new WeaponShopListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeaponShopListAdapter.ViewHolder holder, int position) {
        holder.itemIcon.setImageResource(this.context.getResources().getIdentifier(this.weapons.get(position).getIconFilename(), "drawable", this.context.getPackageName()));
        holder.itemName.setText(this.weapons.get(position).getName());
        holder.itemDescription.setText(this.weapons.get(position).getDescription());
        holder.itemCost.setText(String.format("%d", this.weapons.get(position).getCost()));

        final Weapon weapon = this.weapons.get(position);
        //attach onclick listener....
        if(this.weapons.get(position).getRequiredLevel() > Player.getPlayer().getLevel(this.context)) {
            //if this item is too powerful for this player....
            holder.contentHolderLayout.setAlpha(.25f);
            holder.unlockText.setVisibility(View.VISIBLE);
            holder.unlockText.setText(String.format("Unlocks at level %d", this.weapons.get(position).getRequiredLevel()));
        }else {
            //attach onclick listener....
            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onWeaponTapped(weapon);
                }
            });
        }
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
        TextView itemCost;
        TextView unlockText;
        ConstraintLayout contentHolderLayout;
        ConstraintLayout parentLayout;

        ViewHolder(View itemView) {
            super(itemView);
            itemIcon = itemView.findViewById( R.id.item_shop_icon);
            itemName = itemView.findViewById(R.id.item_shop_item_title);
            itemDescription = itemView.findViewById(R.id.item_shop_description);
            contentHolderLayout = itemView.findViewById(R.id.contentHolderLayout);
            itemCost = itemView.findViewById(R.id.item_shop_item_cost);
            unlockText = itemView.findViewById(R.id.unlockText);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
