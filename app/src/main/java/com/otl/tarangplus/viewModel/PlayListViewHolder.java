package com.otl.tarangplus.viewModel;

import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.otl.tarangplus.R;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.Item;
import com.otl.tarangplus.model.PlayListItem;
import com.otl.tarangplus.viewholders.EpisodeItemViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayListViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.selectplaylist)
    AppCompatCheckBox selectplaylist;
    @BindView(R.id.listname)
    MyTextView listname;
    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;
    private PlayListViewHolder.ItemClickListener listener;

    public PlayListViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayListItem clickedItem = (PlayListItem) itemView.getTag();

                if(!selectplaylist.isChecked()){
                    selectplaylist.setChecked(true);
                    clickedItem.setSelected(true);
                }else{
                    selectplaylist.setChecked(false);
                    clickedItem.setSelected(false);
                }
                listener.onItemClick(clickedItem);
            }
        });



    }

    public void updateUI(PlayListItem item) {
        if (item != null) {
            this.listname.setText(item.getName());
            if(item.isSelected()){
                this.selectplaylist.setChecked(true);
            }else {
                this.selectplaylist.setChecked(false);
            }

            if(item.isPreviouslySelected()){
                this.selectplaylist.setChecked(true);
                this.linearlayout.setClickable(false);
            }
        }




    }
    public void setOnItemClickListener(PlayListViewHolder.ItemClickListener listener) {
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(PlayListItem item);
    }
}
