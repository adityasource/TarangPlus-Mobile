package com.otl.tarangplus.adapters;

import android.content.Context;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.ThumnailFetcher;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.Item;
import com.otl.tarangplus.model.Thumbnails;
import com.otl.tarangplus.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NextProgramsAdapter extends RecyclerView.Adapter<NextProgramsAdapter.NextProgramViewHolder> {

    private List<Item> programList;
    private Context context;
    private CountDownTimer timer;
    private String TAG = NextProgramsAdapter.class.getSimpleName();

    public NextProgramsAdapter(Context context) {
        programList = new ArrayList<>();
        this.context = context;
    }

    public void updateNextProgramms(List<Item> programList) {
        this.programList = programList;
       /* Date time = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        int minutes = time.getMinutes();
        time.setMinutes(minutes-1);
        String changedTime = format.format(time);
        programList.get(0).setStopTime(changedTime);*/
        /* Handling time constraint lists */
        makeAlaramSet(programList.get(0));
        notifyDataSetChanged();
    }

    public void stopAlarmTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    @NonNull
    @Override
    public NextProgramViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.next_program_item, parent, false);
        return new NextProgramViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NextProgramViewHolder holder, int position) {

        if (position == 0)
            holder.mPremium.setVisibility(View.VISIBLE);
        else
            holder.mPremium.setVisibility(View.GONE);

        if (position == 1) {
            holder.mImageMask.setVisibility(View.VISIBLE);
            holder.mImageMask.setImageResource(R.drawable.playing_next_dots);
        } else if (position == 0) {
            holder.mImageMask.setVisibility(View.GONE);
        } else {
            holder.mImageMask.setVisibility(View.VISIBLE);
           // holder.mImageMask.setImageResource(R.drawable.upcoming_dots);
        }

        Item program = programList.get(position);

        if (!TextUtils.isEmpty(program.getTitle()))
            holder.titleText.setText(program.getTitle());

        String startTime = program.getStartTime();
        String stopTime = program.getStopTime();
        String estimatedTime = Constants.getEstimatedTime(startTime, stopTime);

        if (!TextUtils.isEmpty(estimatedTime))
            holder.episode.setText(estimatedTime);

        Thumbnails thumbnails = program.getThumbnails();
        String url = ThumnailFetcher.fetchAppropriateThumbnail(thumbnails, Constants.T_16_9_SMALL);
        Picasso.get().load(url).placeholder(R.drawable.place_holder_16x9).error(R.drawable.place_holder_16x9).into(holder.image);
    }

    private void makeAlaramSet(Item item) {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String currentTime = format.format(currentDate);
        try {
            String stopTime = item.getStopTime();
            Date date = format.parse(stopTime);
            Date currentUtc = format.parse(currentTime);

            boolean before = date.before(currentUtc);
            if (before) {
                programList.remove(item);
                notifyDataSetChanged();


            } else {
                long currentDateTime = currentDate.getTime();
                long time = date.getTime();
                long difference = time - currentDateTime;
                removeAlarmItem(difference, item);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void removeAlarmItem(long difference, Item item) {
        if (timer == null) {
            timer = new CountDownTimer(difference, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    programList.remove(item);
                    notifyDataSetChanged();
                    if (!programList.isEmpty())
                        makeAlaramSet(programList.get(0));
                }
            };
            timer.start();
        } else {
            timer.cancel();
            timer = null;
            removeAlarmItem(difference, item);
        }
    }

    @Override
    public int getItemCount() {
        return programList.size();
    }

    class NextProgramViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.titleText)
        MyTextView titleText;
        @BindView(R.id.episode_meta_data)
        MyTextView episode;
        @BindView(R.id.reminder)
        ImageView reminder;
        @BindView(R.id.parentPanel)
        CardView parentPanel;
        @BindView(R.id.premium)
        ImageView mPremium;
        @BindView(R.id.image_mask)
        ImageView mImageMask;

        public NextProgramViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            reminder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
