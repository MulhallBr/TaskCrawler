package Model;


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

import games.bad.taskcrawler.R;


public class ShopItemListRecyclerViewAdapter extends RecyclerView.Adapter<ShopItemListRecyclerViewAdapter.ViewHolder> {
    private List<Item> items = new ArrayList<>();
    private Context context;

    public ShopItemListRecyclerViewAdapter(List<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_itemlist_item, parent, false);
        ShopItemListRecyclerViewAdapter.ViewHolder viewHolder = new ShopItemListRecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShopItemListRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.itemName.setText(this.items.get(position).getName());
        holder.itemDescription.setText(this.items.get(position).getDescription());
        holder.itemCost.setText(Integer.toString(this.items.get(position).getCost()));
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
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemIcon;
        TextView itemName;
        TextView itemDescription;
        TextView itemCost;
        ConstraintLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            itemIcon = itemView.findViewById( R.id.itemicon);
            itemName = itemView.findViewById(R.id.itemname);
            itemDescription = itemView.findViewById(R.id.itemdescription);
            itemCost = itemView.findViewById(R.id.itemcost);

            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
