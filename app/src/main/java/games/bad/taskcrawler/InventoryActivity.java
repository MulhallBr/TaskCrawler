package games.bad.taskcrawler;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;

import Adapters.IconInventoryListAdapter;
import Adapters.WeaponInventoryListAdapter;
import Interfaces.IconTapCallback;
import Interfaces.WeaponTapCallback;
import Model.Icon;
import Model.Player;
import Model.Weapon;

public class InventoryActivity extends AppCompatActivity implements WeaponTapCallback, IconTapCallback {

    private static final String TAG = "InventoryActivity";
    private WeaponInventoryListAdapter weaponInventoryListAdapter;
    private IconInventoryListAdapter iconInventoryListAdapter;
    private TabLayout inventoryTabLayout;
    private RecyclerView weaponRecyclerView;
    private RecyclerView iconRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "IconOnCreate"); // Logcat print for debugging.

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        inventoryTabLayout = findViewById(R.id.inventoryTabLayout);
        weaponRecyclerView = findViewById(R.id.weapon_recyclerview);
        iconRecyclerView = findViewById(R.id.icon_recyclerView);

        inventoryTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabSelected(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Enable the back button in the action bar.
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Fill the RecyclerView with all of the items, using the default itemListAdapter.
        //RecyclerView weaponRecyclerView = findViewById(R.id.weapon_recyclerview);
        
        List<Weapon> weapons = Weapon.getAllPurchasedWeapons(this); //get all purchased weapons
        List<Icon> icons = Icon.getAllPurchasedPlayerIcons(this); //get all purchased icons

        weaponInventoryListAdapter = new WeaponInventoryListAdapter(weapons, this);
        weaponRecyclerView.setAdapter(weaponInventoryListAdapter);
        weaponRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        weaponInventoryListAdapter.setTapWeaponCallback(this);

        iconInventoryListAdapter = new IconInventoryListAdapter(icons, this);
        iconRecyclerView.setAdapter(iconInventoryListAdapter);
        iconRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        iconInventoryListAdapter.setIconTapCallback(this);

        tabSelected(0);
    }

    public void updateAdapters() {
        List<Weapon> weapons = Weapon.getAllPurchasedWeapons(this);
        this.weaponInventoryListAdapter.updateList(weapons);

        List<Icon> icons = Icon.getAllPurchasedPlayerIcons(this);
        this.iconInventoryListAdapter.updateList(icons);
    }

    private void tabSelected(int position) {
        if(position == 0) {
            weaponRecyclerView.setVisibility(View.VISIBLE);
            iconRecyclerView.setVisibility(View.GONE);

        }else if(position == 1) {
            weaponRecyclerView.setVisibility(View.GONE);
            iconRecyclerView.setVisibility(View.VISIBLE);

        }else if(position == 2) {
            weaponRecyclerView.setVisibility(View.GONE);
            iconRecyclerView.setVisibility(View.GONE);

        }
    }

    //the task tap callback callback method
    @Override
    public void onWeaponTapped(Weapon weapon){
        //Equip the damn weapon:

        Player.getPlayer().setEquippedWeapon(this, weapon);
        //Snackbar.make(findViewById(android.R.id.content), String.format("FUCK: %d", Player.getPlayer().getEquippedWeaponId(this)), Snackbar.LENGTH_LONG).show();

        updateAdapters();

    }

    @Override
    public void onIconTapped(Icon icon) {
        Player.getPlayer().setEquippedIcon(this, icon);
        updateAdapters();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("current_tab", inventoryTabLayout.getSelectedTabPosition());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        int currentTab = savedInstanceState.getInt("current_tab", 0);

        TabLayout.Tab tab = inventoryTabLayout.getTabAt(currentTab);
        tab.select();
        super.onRestoreInstanceState(savedInstanceState);
    }
}
