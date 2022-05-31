package com.otl.tarangplus.viewModel;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.otl.tarangplus.R;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.PlayListItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaylistMePageViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.deleteplaylist)
    ImageView deleteplaylist;
    @BindView(R.id.listname)
    MyTextView listname;
    @BindView(R.id.item)
    LinearLayout item;
    private PlaylistMePageViewHolder.ItemClickListener listener;

    public PlaylistMePageViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.listname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayListItem clickedItem = (PlayListItem) itemView.getTag();
                listener.onItemClick(clickedItem);
            //    Log.e("ONCLICK OF ITEM","CLICKED DUDE");
            }
        });

        this.deleteplaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayListItem clickedItem = (PlayListItem) itemView.getTag();
                listener.onItemdeleteClick(clickedItem);

            }
        });

    }


    public void updateUI(PlayListItem item) {
        if (item != null) {
            this.listname.setText(item.getName());
        }
    }
    public void setOnItemClickListener(PlaylistMePageViewHolder.ItemClickListener listener) {
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onItemdeleteClick(PlayListItem item);
        void onItemClick(PlayListItem item);
    }
}
