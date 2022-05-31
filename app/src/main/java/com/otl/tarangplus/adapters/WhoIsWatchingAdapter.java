package com.otl.tarangplus.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.fragments.EditProfilesFragment;
import com.otl.tarangplus.model.Profile;
import com.otl.tarangplus.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WhoIsWatchingAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Profile> mListItems;
    private boolean isFromLogin;
    private boolean isFromMetab;
    private String TAG = WhoIsWatchingItemViewHolder.class.getSimpleName();
    private boolean IS_SELECTED;
    private int layoutWidth;
    private int layoutHeight;

    Typeface tFontBold, tFontReg;

    public WhoIsWatchingAdapter(Context context, List<Profile> listItems, boolean isFromLogin, boolean isFromMetab) {
        this.mContext = context;
        this.mListItems = listItems;
        this.isFromLogin = isFromLogin;
        this.isFromMetab = isFromMetab;
        IS_SELECTED = false;

        tFontBold = Typeface.createFromAsset(context.getAssets(),
                "fonts/" + context.getString(R.string.font_sans_extra_bold));

        tFontReg = Typeface.createFromAsset(context.getAssets(),
                "fonts/" + context.getString(R.string.font_sans_semi_bold));
    }

    public void updateItems(List<Profile> listItems) {
        this.mListItems = listItems;
        /*if (!isFromMetab) {
            Profile profile = new Profile();
            profile.setFirstname("Add Profile");
            if (!this.mListItems.contains(profile)) {
                this.mListItems.add(profile);
            }
        }*/
        notifyDataSetChanged();
    }

    private selectProfileClickListner listener;

    public void setOnProfileClickListner(selectProfileClickListner listener) {
        this.listener = listener;
    }

    public interface selectProfileClickListner {
        void onProfileSelected(Profile profile);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.who_is_watching_items, parent, false);
        WhoIsWatchingItemViewHolder catalogViewHolder = new WhoIsWatchingItemViewHolder(view);
        return catalogViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        WhoIsWatchingItemViewHolder mWhoIsWatchingItemViewHolder = (WhoIsWatchingItemViewHolder) holder;
        final Profile item = mListItems.get(position);
        if (mListItems != null && mListItems.size() > 0) {


            String currentProfileID = PreferenceHandler.getCurrentProfileID(mContext);
            ViewGroup.LayoutParams layoutParams = mWhoIsWatchingItemViewHolder.mImageText.getLayoutParams();
            boolean profileSelected = PreferenceHandler.getProfileSelected(mContext);

            if (!profileSelected) {
                if (item.isDefaultProfile()) {

                    if (!IS_SELECTED) {
                        layoutWidth = layoutParams.width = layoutParams.width + 60;
                        layoutHeight = layoutParams.height = layoutParams.height + 60;
                        IS_SELECTED = true;

                        mWhoIsWatchingItemViewHolder.mName.setVisibility(View.GONE);
                        mWhoIsWatchingItemViewHolder.mImageText.setVisibility(View.GONE);
                        mWhoIsWatchingItemViewHolder.mNameBold.setVisibility(View.VISIBLE);
                        mWhoIsWatchingItemViewHolder.mImageTextBold.setVisibility(View.VISIBLE);


                    } else {
                        layoutParams.width = layoutWidth;
                        layoutParams.height = layoutHeight;
                    }
                    mWhoIsWatchingItemViewHolder.mParentItem.setAlpha(1.0f);
                } else {
                    if (IS_SELECTED) {
                        layoutParams.width = layoutWidth - 60;
                        layoutParams.height = layoutHeight - 60;
                        mWhoIsWatchingItemViewHolder.mParentItem.setAlpha(1.0f);
                        mWhoIsWatchingItemViewHolder.mParentItem.setAlpha(0.5f);

                    }
                }
            } else {
                if (item != null && currentProfileID != null) {

                    if (currentProfileID.equals(item.getProfileId())) {
                        if (!IS_SELECTED) {
                            layoutWidth = layoutParams.width = layoutParams.width + 60;
                            layoutHeight = layoutParams.height = layoutParams.height + 60;
                            IS_SELECTED = true;

                            mWhoIsWatchingItemViewHolder.mName.setVisibility(View.GONE);
                            mWhoIsWatchingItemViewHolder.mImageText.setVisibility(View.GONE);
                            mWhoIsWatchingItemViewHolder.mNameBold.setVisibility(View.VISIBLE);
                            mWhoIsWatchingItemViewHolder.mImageTextBold.setVisibility(View.VISIBLE);
                        } else {
                            layoutParams.width = layoutWidth;
                            layoutParams.height = layoutHeight;
                        }
                        mWhoIsWatchingItemViewHolder.mParentItem.setAlpha(1.0f);


                    } else {
                        if (IS_SELECTED) {
                            layoutParams.width = layoutWidth - 60;
                            layoutParams.height = layoutHeight - 60;
                            mWhoIsWatchingItemViewHolder.mParentItem.setAlpha(1.0f);
                            mWhoIsWatchingItemViewHolder.mParentItem.setAlpha(0.5f);
                        }
                    }
                }
            }

            if (item != null) {
                if (item.isChild()) {
                    mWhoIsWatchingItemViewHolder.mImageText.setBackground(mContext.getDrawable(R.drawable.place_holder_kids_profile));
                    mWhoIsWatchingItemViewHolder.mImageTextBold.setBackground(mContext.getDrawable(R.drawable.place_holder_kids_profile));
                } else {
                    if (item.getFirstname().equalsIgnoreCase("ADD Profile")) {
                        mWhoIsWatchingItemViewHolder.mImageText.setVisibility(View.GONE);
                    } else {
                        mWhoIsWatchingItemViewHolder.mImageText.setBackground(mContext.getDrawable(R.drawable.place_holder_circle));
                        mWhoIsWatchingItemViewHolder.mImageTextBold.setBackground(mContext.getDrawable(R.drawable.place_holder_circle));
                    }
                }
                setTextAccordingToName(mWhoIsWatchingItemViewHolder, item);
            }
        }

        mWhoIsWatchingItemViewHolder.mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditProfilesFragment fragment = new EditProfilesFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PROFILE_NAME, item.getFirstname());
                bundle.putString(Constants.PROFILE_ID, item.getProfileId());
                bundle.putString(Constants.IS_KID_PROF, item.isChild() + "");
                bundle.putBoolean(Constants.IS_DEFAULT_PROFILE, item.isDefaultProfile());
                fragment.setArguments(bundle);
                Helper.addFragment(((Activity) mContext), fragment, EditProfilesFragment.TAG);
            }
        });

        mWhoIsWatchingItemViewHolder.mParentItem.setOnClickListener(view -> {

            if (item.getFirstname().equalsIgnoreCase("add profile")) {
                EditProfilesFragment fragment = new EditProfilesFragment();
                Helper.addFragment(((Activity) mContext), fragment, EditProfilesFragment.TAG);
            } else if (isFromMetab) {
                if (listener != null)
                    this.listener.onProfileSelected(item);

            }

        });

    }

    private void setTextAccordingToName(WhoIsWatchingItemViewHolder holder, Profile profile) {
        if (profile.getFirstname() != null && profile.getLastname() != null && profile.getFirstname().trim().length() > 0 && profile.getLastname().trim().length() > 0) {
            holder.mName.setText(profile.getFirstname() + " " + profile.getLastname());
            holder.mImageText.setText(profile.getFirstname().substring(0, 1) + profile.getLastname().substring(0, 1));
        } else if (!TextUtils.isEmpty(profile.getFirstname())) {
            if (!profile.getFirstname().equalsIgnoreCase("Add Profile")) {
                holder.mName.setText(profile.getFirstname());
                String firstName = Constants.removeExtraWhiteSpace(profile.getFirstname());
                if (firstName.split(" ").length > 1) {
                    String[] splitName = firstName.split(" ");
                    if (splitName.length > 1) {
                        String firstNAme = splitName[0].replace("  ", " ");
                        String secondName = splitName[1].replace("  ", " ");
                        try {
                            holder.mImageText.setText(firstNAme.substring(0, 1) + secondName.substring(0, 1));
                        } catch (Exception e) {
                            holder.mImageText.setText(firstNAme.substring(0, 1));
                        }
                    } else if (splitName.length < 2 && splitName.length > 0) {
                        holder.mImageText.setText(splitName[0].substring(0, 1));
                    }
                } else {
                    holder.mImageText.setText(profile.getFirstname().substring(0, 1));
                }
            }
        } else if (profile.getLastname() != null) {
            holder.mName.setText(profile.getLastname());
            holder.mImageText.setText(profile.getLastname().substring(0, 1));
        }

        if (!TextUtils.isEmpty(holder.mName.getText()))
            holder.mNameBold.setText(holder.mName.getText());

        if (!TextUtils.isEmpty(holder.mImageText.getText()))
            holder.mImageTextBold.setText(holder.mImageText.getText());
    }


    class WhoIsWatchingItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        MyTextView mName;
        @BindView(R.id.name_bold)
        MyTextView mNameBold;
        @BindView(R.id.name_image_text_bold)
        MyTextView mImageTextBold;
        @BindView(R.id.name_image_text)
        MyTextView mImageText;
        @BindView(R.id.edit)
        MyTextView mEdit;
        @BindView(R.id.parent_view)
        RelativeLayout mParentItem;

        public WhoIsWatchingItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemCount() {
        if (mListItems != null) {
            return mListItems.size();
        } else {
            return 0;
        }
    }
}

