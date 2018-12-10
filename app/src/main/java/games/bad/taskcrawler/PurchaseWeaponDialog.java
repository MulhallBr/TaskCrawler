package games.bad.taskcrawler;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import Model.Player;
import Model.Task;
import Model.Weapon;

public class PurchaseWeaponDialog extends Dialog {
    private Activity activity;
    private Weapon weapon;
    private TextView itemTitle;
    private TextView itemDescription;
    private ImageView itemIcon;
    private TextView itemCost;
    private Button purchaseButton;
    private Button cancelButton;

    public PurchaseWeaponDialog(Activity activity, Weapon weapon) {
        super(activity);
        this.activity = activity;
        this.weapon = weapon;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.buy_item_dialog);

        itemTitle = findViewById(R.id.itemTitle);
        itemDescription = findViewById(R.id.itemDescription);
        itemIcon = findViewById(R.id.itemIcon);

        itemCost = findViewById(R.id.itemCost);

        itemCost.setText(String.format("%d",weapon.getCost()));
        itemTitle.setText(weapon.getName());
        itemDescription.setText(weapon.getDescription());
        itemIcon.setImageResource(weapon.getIconResourceId(activity));

        purchaseButton  = findViewById(R.id.purchaseButton);
        cancelButton = findViewById(R.id.backButton);

        //if the user doesnt have enough money, dont let them buy it.
        if(Player.getPlayer().getGold(activity) < weapon.getCost()) {
            purchaseButton.setEnabled(false);
        }

        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(weapon.purchase(activity) == false) {
                    Snackbar.make(v, "Not enough gold to purchase item", Snackbar.LENGTH_LONG).show();
                    return;
                }
                dismiss();
            }
        });
    }
}
