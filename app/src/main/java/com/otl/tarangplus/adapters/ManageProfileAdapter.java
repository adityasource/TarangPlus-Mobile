package com.otl.tarangplus.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
import com.otl.tarangplus.MyApplication;
import com.otl.tarangplus.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManageProfileAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Profile> mListItems;
    private boolean isFromLogin;
    private boolean isFromMetab;
    private String TAG = ManageProfileAdapter.class.getSimpleName();
    private boolean IS_SELECTED;

    public ManageProfileAdapter(Context context, List<Profile> listItems, boolean isFromLogin, boolean isFromMetab) {
        this.mContext = context;
        this.mListItems = listItems;
        this.isFromLogin = isFromLogin;
        this.isFromMetab = isFromMetab;
        IS_SELECTED = false;
    }

    public void updateItems(List<Profile> listItems) {
        if (this.mListItems != null) {
            this.mListItems.clear();
        }
        this.mListItems = listItems;

        int limit = PreferenceHandler.getUserProfileLimit(MyApplication.getInstance());
        if (limit > (mListItems.size())) {
            Profile profile = new Profile();
            profile.setFirstname("new profile");
            if (!this.mListItems.contains(profile)) {
                this.mListItems.add(profile);
            }
        }

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

        View view = LayoutInflater.from(mContext).inflate(R.layout.manage_profile_items, parent, false);
        ManageProfileViewHolder catalogViewHolder = new ManageProfileViewHolder(view);
        return catalogViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ManageProfileViewHolder mWhoIsWatchingItemViewHolder = (ManageProfileViewHolder) holder;
        final Profile item = mListItems.get(position);
        if (mListItems != null && mListItems.size() > 0) {

            String currentProfileID = PreferenceHandler.getCurrentProfileID(mContext);

            if (item != null) {
                if (item.getFirstname().equalsIgnoreCase("new profile")) {
                    mWhoIsWatchingItemViewHolder.mNameImageText.setText("");
                    mWhoIsWatchingItemViewHolder.mEdit.setVisibility(View.GONE);
                    mWhoIsWatchingItemViewHolder.mNameImageText.setBackground(mContext.getDrawable(R.drawable.new_profile));
                    mWhoIsWatchingItemViewHolder.mName.setText(item.getFirstname());
                } else {

                    if (item.isChild()) {
                        mWhoIsWatchingItemViewHolder.mNameImageText.setBackground(mContext.getDrawable(R.drawable.place_holder_kids_profile));
                    } else {
                        mWhoIsWatchingItemViewHolder.mNameImageText.setBackground(mContext.getDrawable(R.drawable.place_holder_circle));
                    }

                    mWhoIsWatchingItemViewHolder.mEdit.setVisibility(View.VISIBLE);

                    setTextAccordingToName(mWhoIsWatchingItemViewHolder, item);

                }
            }

        }


        mWhoIsWatchingItemViewHolder.mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditProfilesFragment fragment = new EditProfilesFragment();
                Bundle bundle = new Bundle();
                String firstName = Constants.removeExtraWhiteSpace(item.getFirstname());
                String secondNAme = Constants.removeExtraWhiteSpace(item.getLastname());
                String fullName = firstName + " " + secondNAme;
                bundle.putString(Constants.PROFILE_NAME, fullName);
                bundle.putString(Constants.PROFILE_ID, item.getProfileId());
                bundle.putString(Constants.IS_KID_PROF, item.isChild() + "");
                bundle.putBoolean(Constants.IS_DEFAULT_PROFILE, item.isDefaultProfile());
                fragment.setArguments(bundle);
                Helper.addFragment(((Activity) mContext), fragment, EditProfilesFragment.TAG);
            }
        });

        mWhoIsWatchingItemViewHolder.mParentView.setOnClickListener(view -> {

            if (item.getFirstname().equalsIgnoreCase("new profile")) {
                EditProfilesFragment fragment = new EditProfilesFragment();
                Helper.addFragment(((Activity) mContext), fragment, EditProfilesFragment.TAG);
            } else if (isFromMetab) {
                if (listener != null)
                    this.listener.onProfileSelected(item);

            }

        });

    }

    private void setTextAccordingToName(ManageProfileViewHolder holder, Profile profile) {
        String firstName = Constants.removeExtraWhiteSpace(profile.getFirstname());
        String lastName = Constants.removeExtraWhiteSpace(profile.getLastname());
        if (firstName != null && lastName != null && firstName.trim().length() > 0 && lastName.trim().length() > 0) {
            holder.mName.setText(firstName + " " + lastName);
            holder.mNameImageText.setText(firstName.substring(0, 1) + lastName.substring(0, 1));
        } else if (firstName != null) {
            holder.mName.setText(firstName);
            if (firstName.indexOf(" ") == 0 || firstName.indexOf(" ") == firstName.length() - 1) {
                firstName = firstName.trim();
                holder.mName.setText(firstName);
            } else if (firstName.split(" ").length > 1) {
                String[] splitName = firstName.split(" ");
                try {
                    holder.mNameImageText.setText(splitName[0].substring(0, 1) + splitName[1].substring(0, 1));
                } catch (Exception e) {
                    holder.mNameImageText.setText(splitName[0].substring(0, 1));
                }

            } else {
                holder.mNameImageText.setText(firstName.substring(0, 1));
            }
        } else if (lastName != null) {
            holder.mName.setText(lastName);
            holder.mNameImageText.setText(lastName.substring(0, 1));
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

    static class ManageProfileViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name_image_text)
        MyTextView mNameImageText;
        @BindView(R.id.name)
        MyTextView mName;
        @BindView(R.id.parent_view)
        RelativeLayout mParentView;
        @BindView(R.id.edit)
        MyTextView mEdit;


        ManageProfileViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

