package com.otl.tarangplus.fragments;


import androidx.lifecycle.MutableLiveData;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.otl.tarangplus.Analytics.WebEngageAnalytics;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.model.CatalogListItem;
import com.otl.tarangplus.model.PageEvents;
import com.otl.tarangplus.model.Thumbnails;
import com.otl.tarangplus.Analytics.AnalyticsProvider;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.SpacesItemDecoration;
import com.otl.tarangplus.Utils.ThumnailFetcher;
import com.otl.tarangplus.adapters.HomeEpgListRecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class EpgListFragment extends Fragment {

    public static final String TAG = EpgListFragment.class.getSimpleName();
    @BindView(R.id.image_view)
    ImageView imageView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.display_title)
    GradientTextView displayTitle;

    private WebEngageAnalytics webEngageAnalytics;
    private PageEvents pageEvents;

    private MutableLiveData<Object> selectedTitle;

    public EpgListFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_epg_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        selectedTitle = new MutableLiveData<>();
        webEngageAnalytics = new WebEngageAnalytics();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fireScreenView();
        updateUi();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void updateUi() {
        Bundle bundle = getArguments();
        CatalogListItem item = (CatalogListItem) bundle.getParcelable(Constants.ITEMS);
        if (!TextUtils.isEmpty(item.getDisplayTitle())) {
            displayTitle.setText(item.getDisplayTitle());
        }
        setDisplayTextGradientColor(item);
        displayTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.performClick();
            }
        });
        Thumbnails thumbnails = item.getCatalogListItems().get(0).getThumbnails();
        String imageUrl = ThumnailFetcher.fetchAppropriateThumbnail(thumbnails, Constants.T_1_1_ALBUM);
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.get().load(imageUrl).placeholder(R.drawable.place_holder_1x1).error(R.drawable.place_holder_1x1).into(this.imageView);
        } else {
            Picasso.get().load(R.drawable.place_holder_1x1).error(R.drawable.place_holder_1x1).into(this.imageView);
        }
        List<CatalogListItem> listItems = item.getCatalogListItems();
        if (listItems != null && listItems.size() > 0) {
            CatalogListItem listItem = listItems.get(0);
            setUpRecyclerView(listItem);
        }

    }

    private void setUpRecyclerView(CatalogListItem item) {
        HomeEpgListRecyclerAdapter recyclerViewAdapter = new HomeEpgListRecyclerAdapter(getActivity(), item);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, (int) getResources().getDimension(R.dimen.px_5)));
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerViewAdapter.notifyDataSetChanged();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LiveTvDetailsFragment fragment = new LiveTvDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.CONTENT_ID, item.getContentID());
                bundle.putString(Constants.CATALOG_ID, item.getCatalogID());
                bundle.putString(Constants.DISPLAY_TITLE, item.getDisplayTitle());
                bundle.putString(Constants.THEME, item.getTheme());
                String layoutScheme = item.getLayoutScheme();
                if (TextUtils.isEmpty(layoutScheme)) {
                    layoutScheme = item.getCatalogObject().getLayoutScheme();
                }
                bundle.putString(Constants.LAYOUT_SCHEME, item.getLayoutScheme());
                fragment.setArguments(bundle);
                Helper.addFragmentForDetailsScreen(getActivity(), fragment, LiveTvDetailsFragment.TAG);
            }
        });
    }

    private void setDisplayTextGradientColor(CatalogListItem item) {
        if (Constants.getSchemeColor(item.getLayoutScheme().replace('-', '_')) != null) {
            int startColor = Color.parseColor("#" + Constants.getSchemeColor(item.getLayoutScheme().replace('-', '_')).getStartColor());
            int endColor = Color.parseColor("#" + Constants.getSchemeColor(item.getLayoutScheme().replace('-', '_')).getEndColor());
            displayTitle.setStartAndEndColor(startColor, endColor);
            displayTitle.setText(item.getDisplayTitle());
        }
    }

    public void fireScreenView() {
        AnalyticsProvider mAnalytics = new AnalyticsProvider(getContext());
        mAnalytics.fireScreenView(AnalyticsProvider.Screens.TV);
        pageEvents = new PageEvents(Constants.TV);
        webEngageAnalytics.screenViewedEvent(pageEvents);
    }
}
