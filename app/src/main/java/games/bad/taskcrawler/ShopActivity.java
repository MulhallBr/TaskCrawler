package games.bad.taskcrawler;

import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;

import Adapters.IconShopListAdapter;
import Adapters.WeaponShopListAdapter;
import Interfaces.IconTapCallback;
import Interfaces.WeaponTapCallback;
import Model.Icon;
import Model.Weapon;

public class ShopActivity extends AppCompatActivity implements WeaponTapCallback, IconTapCallback {

    private static final String TAG = "ShopActivity";
    private RecyclerView weapon_recyclerview;
    private RecyclerView icon_recyclerview;

    WeaponShopListAdapter weaponShopListAdapter;
    IconShopListAdapter iconShopListAdapter;

    private TabLayout shopTabLayout;

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
        icon_recyclerview = findViewById(R.id.shop_icon_recyclerview);

        shopTabLayout = findViewById(R.id.shopTabLayout);

        shopTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

          //Item.initializeItems(this, this.getResources());  // Fill the database with initial data if the db is not already full.
        List<Weapon> weapons = Weapon.getAllUnpurchasedWeapons(this); // Grab all of the weapons from the database.

        // Fill the RecyclerView with the data from the Items.
        weaponShopListAdapter = new WeaponShopListAdapter(weapons, this);
        weapon_recyclerview.setAdapter(weaponShopListAdapter);
        weapon_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        weaponShopListAdapter.setTapWeaponCallback(this);

        List<Icon> icons = Icon.getAllUnpurchasedIcons(this);

        iconShopListAdapter = new IconShopListAdapter(icons, this);
        icon_recyclerview.setAdapter(iconShopListAdapter);
        icon_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        iconShopListAdapter.setTapIconCallback(this);

        tabSelected(0);
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

    @Override
    public void onIconTapped(Icon icon) {
        //purchase icon thing
        PurchaseIconDialog pid = new PurchaseIconDialog(this, icon);
        pid.show();

        pid.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                updateAdapters();
            }
        });
    }

    public void tabSelected(int position) {
        if(position == 0) {
            weapon_recyclerview.setVisibility(View.VISIBLE);
            icon_recyclerview.setVisibility(View.GONE);

        }else if(position == 1) {
            weapon_recyclerview.setVisibility(View.GONE);
            icon_recyclerview.setVisibility(View.VISIBLE);
        }else if(position == 2){
            weapon_recyclerview.setVisibility(View.GONE);
            icon_recyclerview.setVisibility(View.GONE);
        }
    }

    public void updateAdapters() {
        List<Icon> icons = Icon.getAllUnpurchasedIcons(this);
        this.iconShopListAdapter.updateList(icons);

        List<Weapon> weapons = Weapon.getAllUnpurchasedWeapons(this);
        //weapon_recyclerview.setAdapter(new WeaponShopListAdapter(weapons, this));
        this.weaponShopListAdapter.updateList(weapons);
        this.weaponShopListAdapter.notifyDataSetChanged();


    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("current_tab", shopTabLayout.getSelectedTabPosition());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        int currentTab = savedInstanceState.getInt("current_tab", 0);

        TabLayout.Tab tab = shopTabLayout.getTabAt(currentTab);
        tab.select();
        super.onRestoreInstanceState(savedInstanceState);
    }
}
