package games.bad.taskcrawler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import Model.Item;
import Model.ItemListAdapter;

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

        Item.initializeItems(this, this.getResources());

        List<Item> items = Item.getItems(this);

        RecyclerView inventoryListRecyclerView = findViewById(R.id.item_recyclerview);

        ItemListAdapter shopItemListRecyclerViewAdapter = new ItemListAdapter(items, this);

        inventoryListRecyclerView.setAdapter(shopItemListRecyclerViewAdapter);

        inventoryListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
