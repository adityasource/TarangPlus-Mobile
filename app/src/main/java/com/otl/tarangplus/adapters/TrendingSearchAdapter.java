package com.otl.tarangplus.adapters;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.ThumnailFetcher;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.CatalogListItem;
import com.otl.tarangplus.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrendingSearchAdapter extends RecyclerView.Adapter {
    Activity mContext;
    List<CatalogListItem> mListItems;


    public TrendingSearchAdapter(Activity activity, List<CatalogListItem> listItems) {
        this.mContext = activity;
        this.mListItems = listItems;
    }

    public void updateSearchItems(List<CatalogListItem> listItems) {
//        this.mListItems.clear();
        this.mListItems = listItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.searc_result_item_page, parent, false);
        SearchItemViewHolder searchItemViewHolder = new SearchItemViewHolder(view);
        return searchItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchItemViewHolder mSearchItemViewHolder = (SearchItemViewHolder) holder;
        final CatalogListItem item = mListItems.get(position);
        if (item != null) {
            if (!TextUtils.isEmpty(item.getItemCaption())) {
                mSearchItemViewHolder.mGeneres.setText(item.getItemCaption());
            }
            mSearchItemViewHolder.mName.setText(item.getTitle());
            Picasso.get().load(ThumnailFetcher.fetchAppropriateThumbnail(item.getThumbnails(), Constants.T_16_9_SMALL)).placeholder(R.drawable.place_holder_16x9).into(mSearchItemViewHolder.mThumbnail);
            mSearchItemViewHolder.mParentItem.setTag(item);

            //        Setting Premium,Live Tag's Here
            if (item.getAccessControl() != null && item.getAccessControl().getIsFree()) {
                if (mSearchItemViewHolder.premium != null)
                    mSearchItemViewHolder.premium.setVisibility(View.GONE);
            } else {
                if (item.getAccessControl() != null) {
                    boolean premiumTag = item.getAccessControl().isPremiumTag();
                    if (mSearchItemViewHolder.premium != null)
                        if (premiumTag) {
                            mSearchItemViewHolder.premium.setVisibility(View.VISIBLE);
                            Picasso.get().load(R.drawable.premium_rec).into(mSearchItemViewHolder.premium);

                        } else {
                            mSearchItemViewHolder.premium.setVisibility(View.GONE);
                        }
                }
            }

            if (mSearchItemViewHolder.liveTag != null)
                if (!TextUtils.isEmpty(item.getTheme()) && item.getTheme().equalsIgnoreCase("live"))
                    mSearchItemViewHolder.liveTag.setVisibility(View.VISIBLE);
                else
                    mSearchItemViewHolder.liveTag.setVisibility(View.GONE);

        }

    }

    public interface SearchItemClickListener {
        void onClickSearchItem(CatalogListItem item);
    }

    private SearchItemClickListener listener;

    public void setOnSearchItemClickListner(SearchItemClickListener listener) {
        this.listener = listener;
    }


    class SearchItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.dispay_name)
        MyTextView mName;
        @BindView(R.id.geners)
        MyTextView mGeneres;
        @BindView(R.id.thumbnail)
        ImageView mThumbnail;
        @BindView(R.id.parent_view)
        RelativeLayout mParentItem;
        @BindView(R.id.premium)
        ImageView premium;
        @BindView(R.id.live_tag)
        ImageView liveTag;

        public SearchItemViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mParentItem.setOnClickListener(view -> {

                if (listener != null) {
                    listener.onClickSearchItem((CatalogListItem) itemView.getTag());
                }
            });
        }


    }

    @Override
    public int getItemCount() {
//        Log.e(">>>>", "getItemCount: " );
        if (mListItems != null)
            return mListItems.size();
        else {
            return 0;
        }
    }


}
