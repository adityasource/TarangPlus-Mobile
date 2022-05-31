package com.otl.tarangplus.viewholders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.SpacesItemDecoration;
import com.otl.tarangplus.model.CatalogListItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubscriptionViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.bag_image)
    ImageView image;
    @BindView(R.id.title)
    MyTextView titleText;
    @BindView(R.id.parentPanel)
    CardView parentPanel;

    public SubscriptionViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        parentPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemView != null) {
                    CatalogListItem clickedItem = (CatalogListItem) itemView.getTag();
                    Helper.moveToPageBasedOnTheme((AppCompatActivity) itemView.getContext(), clickedItem);
                }
            }
        });
    }

    public void updateUI(CatalogListItem listItem, String layoutType) {
        if (listItem != null) {
            this.titleText.setText(listItem.getTitle());
        }
    }

    public void setInitialPadding(int padding) {
        parentPanel.setLayoutParams(SpacesItemDecoration.addLeftMargin(padding));
    }

}
