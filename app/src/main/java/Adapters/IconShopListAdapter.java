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

import Interfaces.IconTapCallback;
import Interfaces.WeaponTapCallback;
import Model.Icon;
import Model.Player;
import Model.Weapon;
import games.bad.taskcrawler.R;

public class IconShopListAdapter extends RecyclerView.Adapter<IconShopListAdapter.ViewHolder> {

    private static final String TAG = "ICON SHOP LIST ADAPTER";

    private List<Icon> icons = new ArrayList<>();
    private Context context;
    private IconTapCallback listener; //tap callback listener.

    //tap callback setter.
    public void setTapIconCallback(IconTapCallback callback){
        this.listener = callback;
    }

    public IconShopListAdapter(List<Icon> icons, Context context) {
        this.icons = icons;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_item, parent, false);
        IconShopListAdapter.ViewHolder viewHolder = new IconShopListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IconShopListAdapter.ViewHolder holder, int position) {
        holder.itemIcon.setImageResource(this.context.getResources().getIdentifier(this.icons.get(position).getIconFilename(), "drawable", this.context.getPackageName()));
        holder.itemName.setText(this.icons.get(position).getName());
        holder.itemDescription.setText("");
        holder.itemCost.setText(String.format("%d", this.icons.get(position).getCost()));
        final Icon icon = this.icons.get(position);
        if(this.icons.get(position).getRequiredLevel() > Player.getPlayer().getLevel(this.context)) {
            //if this item is too powerful for this player....
            holder.contentHolderLayout.setAlpha(.25f);
            holder.unlockText.setVisibility(View.VISIBLE);
            holder.unlockText.setText(String.format("Unlocks at level %d", this.icons.get(position).getRequiredLevel()));
        }else {
            //attach onclick listener....
            holder.contentHolderLayout.setAlpha(1f);
            holder.unlockText.setVisibility(View.GONE);
            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onIconTapped(icon);
                }
            });
        }
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
        TextView itemCost;
        ConstraintLayout contentHolderLayout;
        TextView unlockText;
        ConstraintLayout parentLayout;

        ViewHolder(View itemView) {
            super(itemView);
            itemIcon = itemView.findViewById( R.id.item_shop_icon);
            itemName = itemView.findViewById(R.id.item_shop_item_title);
            itemDescription = itemView.findViewById(R.id.item_shop_description);
            itemCost = itemView.findViewById(R.id.item_shop_item_cost);
            contentHolderLayout = itemView.findViewById(R.id.contentHolderLayout);
            unlockText = itemView.findViewById(R.id.unlockText);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
