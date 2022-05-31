package com.otl.tarangplus.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.otl.tarangplus.R;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.interfaces.APICallValuesRetrurn;
import com.otl.tarangplus.model.Data;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Aditya on 8/9/2019.
 */
public class SeasonItemAdapter  extends RecyclerView.Adapter<SeasonItemAdapter.ViewHolder>  {

    private List<Data> datas;
    private Context context;
    private String posIsWhite = "";
    APICallValuesRetrurn.GetSeasonIDandItems videoListDetailScreenDataInterface;

   public SeasonItemAdapter(List<Data> homeContinueWatchingList,
                            Context context, APICallValuesRetrurn.GetSeasonIDandItems videoListDetailScreenDataInterface){
       this.datas = homeContinueWatchingList;
       this.context = context;
       this.videoListDetailScreenDataInterface = videoListDetailScreenDataInterface;

       /*temp*/
       posIsWhite = datas.get(0).getContentId();


   }

    @NonNull
    @Override
    public SeasonItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.season_item, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SeasonItemAdapter.ViewHolder holder, int position) {

        if (datas != null && datas.size() != 0) {
            try {

                holder.seasonName.setText(datas.get(position).getTitle());
                if (!posIsWhite.equals("") && posIsWhite.equals(datas.get(position).getContentId())) {
                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        holder.seasonName.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.roundcornerwhite));
                    } else {
                        holder.seasonName.setBackground(context.getResources().getDrawable(R.drawable.roundcornerwhite));
                    }
                    holder.seasonName.setTextColor(ContextCompat.getColor(context, R.color.white));
                } else {
                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        holder.seasonName.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounder_cornor));
                    } else {
                        holder.seasonName.setBackground(context.getResources().getDrawable(R.drawable.rounder_cornor));
                    }
                    holder.seasonName.setTextColor(ContextCompat.getColor(context, R.color.black));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        holder.seasonName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int posTemp = holder.getAdapterPosition();
                String seasonId = posIsWhite = datas.get(posTemp).getContentId();
                videoListDetailScreenDataInterface.OnseasonClick(seasonId);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != datas ? datas.size() : 0);
    }

    public final static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.season_name)
        MyTextView seasonName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
