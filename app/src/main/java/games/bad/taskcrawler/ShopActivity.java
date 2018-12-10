package games.bad.taskcrawler;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import Adapters.WeaponShopListAdapter;
import Interfaces.WeaponTapCallback;
import Model.Weapon;

public class ShopActivity extends AppCompatActivity implements WeaponTapCallback {

    private static final String TAG = "ShopActivity";
    private RecyclerView weapon_recyclerview;
    WeaponShopListAdapter weaponShopListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        // Enable the back button in the action bar.
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        weapon_recyclerview = findViewById(R.id.weapon_recyclerview);
        
        //Item.initializeItems(this, this.getResources());  // Fill the database with initial data if the db is not already full.
        List<Weapon> weapons = Weapon.getAllUnpurchasedWeapons(this); // Grab all of the weapons from the database.

        // Fill the RecyclerView with the data from the Items.
        weaponShopListAdapter = new WeaponShopListAdapter(weapons, this);
        weapon_recyclerview.setAdapter(weaponShopListAdapter);
        weapon_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        weaponShopListAdapter.setTapWeaponCallback(this);
    }

    //the task tap callback callback method
    @Override
    public void onWeaponTapped(Weapon weapon){
        PurchaseWeaponDialog pwd = new PurchaseWeaponDialog(this, weapon);
        pwd.show();
        pwd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                updateAdapters();
            }
        });
    }

    public void updateAdapters() {
        List<Weapon> weapons = Weapon.getAllUnpurchasedWeapons(this);
        this.weaponShopListAdapter.updateList(weapons);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


}
