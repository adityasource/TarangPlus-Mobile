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

public class TransactionFailedDialog extends DialogFragment {

    private static final String TAG = TransactionFailedDialog.class.getSimpleName();

    public static TransactionFailedDialog newInstance() {
        TransactionFailedDialog f = new TransactionFailedDialog();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.transaction_failed_item, container, false);
        ViewHolder holder = new ViewHolder(v);
        updateUI(holder);
        return v;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    private void updateUI(ViewHolder holder) {

        holder.cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
    }

    static class ViewHolder {
        @BindView(R.id.failed_message)
        MyTextView failed_message;
        @BindView(R.id.reason_message)
        MyTextView reason_message;
        @BindView(R.id.transaction_id)
        MyTextView transaction_id;
        @BindView(R.id.try_again_button)
        MyTextView try_again_button;
        @BindView(R.id.cancel_button)
        MyTextView cancel_button;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
