package games.bad.taskcrawler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import Adapters.WeaponSelectListAdapter;
import Model.Weapon;

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

        List<Weapon> weapons = Weapon.getWeapons(this); //get all items from the db

        //fill the recyclerview with the items! using the default itemListAdapter.
        RecyclerView weaponRecyclerView = findViewById(R.id.weapon_recyclerview);
        WeaponSelectListAdapter weaponSelectListAdapter = new WeaponSelectListAdapter(weapons, this);
        weaponRecyclerView.setAdapter(weaponSelectListAdapter);
        weaponRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
