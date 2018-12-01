package games.bad.taskcrawler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import Model.Item;
import Model.InventoryItemListAdapter;

public class InventoryActivity extends AppCompatActivity {

    private static final String TAG = "InventoryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "OnCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Item.initializeItems(this, this.getResources()); //init the database, if it is not already.

        List<Item> items = Item.getItems(this); //get all items from the db

        //fill the recyclerview with the items! using the default itemListAdapter.
        RecyclerView inventoryListRecyclerView = findViewById(R.id.item_recyclerview);
        InventoryItemListAdapter shopItemListRecyclerViewAdapter = new InventoryItemListAdapter(items, this);
        inventoryListRecyclerView.setAdapter(shopItemListRecyclerViewAdapter);
        inventoryListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
