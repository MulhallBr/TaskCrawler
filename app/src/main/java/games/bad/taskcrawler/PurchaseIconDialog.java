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

import Model.Icon;
import Model.Player;
import Model.Weapon;

public class PurchaseIconDialog extends Dialog {
    private Activity activity;
    private Icon icon;

    private TextView itemTitle;
    private TextView itemDescription;
    private ImageView itemIcon;
    private TextView itemCost;
    private Button purchaseButton;
    private Button cancelButton;

    public PurchaseIconDialog(Activity activity, Icon icon) {
        super(activity);
        this.activity = activity;
        this.icon = icon;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.buy_item_dialog);

        cancelButton = findViewById(R.id.cancelButton);

        itemTitle = findViewById(R.id.itemTitle);
        itemDescription = findViewById(R.id.itemDescription);
        itemIcon = findViewById(R.id.itemIcon);

        itemCost = findViewById(R.id.itemCost);

        itemCost.setText(String.format("%d",icon.getCost()));
        itemTitle.setText(icon.getName());
        itemDescription.setText("");
        itemIcon.setImageResource(icon.getIconResourceId(activity));

        purchaseButton  = findViewById(R.id.purchaseButton);
        cancelButton = findViewById(R.id.backButton);

        //if the user doesnt have enough money, dont let them buy it.
        if(Player.getPlayer().getGold(activity) < icon.getCost()) {
            purchaseButton.setEnabled(false);
            purchaseButton.setText("NOT ENOUGH GOLD");
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(icon.purchase(activity) == false) {
                    Snackbar.make(v, "Not enough gold to purchase item", Snackbar.LENGTH_LONG).show();
                    return;
                }
                dismiss();
            }
        });
    }

}
