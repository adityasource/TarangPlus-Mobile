package com.otl.tarangplus.adapters;

import android.app.Activity;
import android.content.Context;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asksira.loopingviewpager.LoopingPagerAdapter;
import com.otl.tarangplus.Analytics.AnalyticsProvider;
import com.otl.tarangplus.Analytics.WebEngageAnalytics;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.ThumnailFetcher;
import com.otl.tarangplus.model.CatalogListItem;
import com.otl.tarangplus.R;
import com.otl.tarangplus.model.PromoEvents;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ankith on 1/12/15.
 */
public class HomeViewPagerItemAdapter extends LoopingPagerAdapter<CatalogListItem> {

    Activity activity;
    private List<CatalogListItem> catalogListItems;
    private String TAG = HomeViewPagerItemAdapter.class.getSimpleName();
    CatalogListItem g;
    AnalyticsProvider analyticsProvider;
    WebEngageAnalytics webEngageAnalytics;
    PromoEvents promoEvents;

    public HomeViewPagerItemAdapter(Context context, List<CatalogListItem> itemList, CatalogListItem g, boolean isInfinite) {
        super(context, itemList, isInfinite);
        this.g = g;
        this.catalogListItems = itemList;
        this.activity = (Activity) context;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    protected View inflateView(int viewType, ViewGroup container, int listPosition) {
        return LayoutInflater.from(context).inflate(R.layout.t_16_9_banner, container, false);

    }

    @Override
    protected void bindView(View convertView, int listPosition, int viewType) {
        final CatalogListItem object = catalogListItems.get(listPosition);

        // LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //final View layout = inflater.inflate(R.layout.t_16_9_banner, null);

        ImageView mCatalogImage = (ImageView) convertView.findViewById(R.id.image_catalog_item);
        ImageView mPremium = (ImageView) convertView.findViewById(R.id.premium);
        ImageView liveTag = (ImageView) convertView.findViewById(R.id.live_tag);


        if (object.getAccessControl() != null) {
            if (object.getAccessControl() != null && object.getAccessControl().getIsFree()) {
                mPremium.setVisibility(View.GONE);
            } else {
                boolean premiumTag = object.getAccessControl().isPremiumTag();
                if (premiumTag) {
                    mPremium.setVisibility(View.VISIBLE);
                } else {
                    mPremium.setVisibility(View.GONE);
                }
            }
        }


        if (object.getTheme().equalsIgnoreCase(Constants.LIVE))
            liveTag.setVisibility(View.VISIBLE);
        else
            liveTag.setVisibility(View.GONE);


        String imageUrl = ThumnailFetcher.fetchAppropriateThumbnailForCurosal(object.getThumbnails(), g.getLayoutType());
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.get().load(imageUrl).placeholder(R.drawable.place_holder_16x9).into(mCatalogImage);
        } else {
            Picasso.get().load(R.drawable.place_holder_16x9).into(mCatalogImage);
        }


        mCatalogImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //analyticsProvider = new AnalyticsProvider(activity);
                webEngageAnalytics = new WebEngageAnalytics();
                String bannerName = object.getCatalogName() != null ? object.getCatalogName() : "";
                String bannerId = object.getCatalogID() != null ?  object.getCatalogID() :"";
                promoEvents = new PromoEvents(bannerName,bannerId,listPosition);
                webEngageAnalytics.promoClickEvent(promoEvents);
                if (object.getListItemObject() != null
                        && object.getListItemObject().getBannerFlag().equalsIgnoreCase(Constants.NON_MEDIA)) {
                    Helper.moveToPageBasedOnListItemObject(activity, object);
                   // if (analyticsProvider != null)
                       // analyticsProvider.branchonClickOfBanner(object, activity,Constants.NON_MEDIA);
                } else {
                    Helper.moveToPageBasedOnTheme(activity, object);
                    //if (analyticsProvider != null)
                       // analyticsProvider.branchonClickOfBanner(object, activity,"media");
                }
            }
        });

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        ((ViewPager) container).removeView((View) view);
//       container.removeView((LinearLayout)object);
    }

}
