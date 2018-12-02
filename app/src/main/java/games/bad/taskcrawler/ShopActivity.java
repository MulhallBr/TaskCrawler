package games.bad.taskcrawler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import Model.Weapon;

public class ShopActivity extends AppCompatActivity {

    private static final String TAG = "ShopActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "OnCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Item.initializeItems(this, this.getResources()); //fill the database with initial data if the db is not already full.
        List<Weapon> weapons = Weapon.getWeapons(this); //get all the items from the database.

        //fill the recycler view with the data from the Items.
        /*RecyclerView weaponShopListRecyclerView = findViewById(R.id.item_recyclerview);
        ShopItemListAdapter shopItemListRecyclerViewAdapter = new ShopItemListAdapter(items, this);
        shopListRecyclerView.setAdapter(shopItemListRecyclerViewAdapter);
        shopListRecyclerView.setLayoutManager(new LinearLayoutManager(this));*/

    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


}
