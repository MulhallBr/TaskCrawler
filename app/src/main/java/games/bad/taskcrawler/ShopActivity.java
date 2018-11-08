package games.bad.taskcrawler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import Model.Item;
import Model.ShopItemListRecyclerViewAdapter;
import Model.Task;
import Model.TaskListRecyclerViewAdapter;

public class ShopActivity extends AppCompatActivity {
    private List<Item> items;
    private static final String TAG = "ShopActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Item.initializeItems(this, this.getResources());

        items = Item.getItems(this);

        RecyclerView shopListRecyclerView = findViewById(R.id.shop_item_recyclerview);
        ShopItemListRecyclerViewAdapter shopItemListRecyclerViewAdapter = new ShopItemListRecyclerViewAdapter(items, this);
        shopListRecyclerView.setAdapter(shopItemListRecyclerViewAdapter);
        shopListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


}
