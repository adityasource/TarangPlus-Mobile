package com.otl.tarangplus.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.otl.tarangplus.R;
import com.otl.tarangplus.customeUI.MyTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComboOfferDialog extends DialogFragment {

    public static ComboOfferDialog newInstance() {
        ComboOfferDialog f = new ComboOfferDialog();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.combo_offer_dialog, container, false);
        TransactionFailedDialog.ViewHolder holder = new TransactionFailedDialog.ViewHolder(v);
        updateUI(holder);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    private void updateUI(TransactionFailedDialog.ViewHolder holder) {

        holder.cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

    }

    static class ViewHolder {
        @BindView(R.id.message)
        MyTextView message;
        @BindView(R.id.try_again_button)
        MyTextView try_again_button;
        @BindView(R.id.cancel_button)
        MyTextView cancel_button;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
