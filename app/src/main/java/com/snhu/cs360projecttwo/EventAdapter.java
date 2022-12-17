package com.snhu.cs360projecttwo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.snhu.cs360projecttwo.data.model.EventModel;

import java.text.DateFormat;
import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {


    private final Context context;
    private ArrayList<EventModel> eventModelList;

    public EventAdapter(Context context) {
        this.context = context;
    }

    public void setData(@NonNull ArrayList<EventModel> data) {
        this.eventModelList = data;
        this.notifyDataSetChanged();
    }

    public void replaceInsertItem(@NonNull EventModel model) {
        boolean found = false;

        for (EventModel item : this.eventModelList) {
            // if the ids match OR if the object instances are the same then replace
            // when a new object is saved for the first time its id will be changed but the instance will be the same
            // hence the OR clause
            if (item.getId().compareTo(model.getId()) == 0
                || item == model) {
                item.setDate(model.getDate());
                item.setName(model.getName());
                item.setLocation(model.getLocation());
                item.setUserId(model.getUserId());
                found = true;
                break;
            }
        }

        if (!found) {
            this.eventModelList.add(model);
        }

        this.notifyDataSetChanged();
    }

    public void deleteItem(@NonNull Long id) {
        boolean found = false;

        for (EventModel item : this.eventModelList) {
            if (item.getId().compareTo(id) == 0) {
                this.eventModelList.remove(item);
                found = true;
                break;
            }
        }

        if (found) {
            this.notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        EventModel model = eventModelList.get(position);
        holder.setData(model, context);
        holder.itemView.setTag(model);
        holder.itemView.setOnClickListener((View.OnClickListener) this.context);

        // TODO create custom drawable based on event name
//        ShapeDrawable shapeDrawable = new ShapeDrawable();
//        Shape shape = new Shape() {
//            @Override
//            public void draw(Canvas canvas, Paint paint) {
//                Path path = new Path();
//                path.moveTo(0, 80);
//                path.lineTo(100, 80);
//                String name = model.getName();
//                String firstLetter = name.substring(0, 1).toUpperCase();
//                canvas.drawTextOnPath(firstLetter, path, 0, 0, paint);
//            }
//        };
//        shapeDrawable.setShape(shape);
        //holder.eventIV.setImageDrawable(shapeDrawable);
        //holder.eventIV.setImageResource(R.drawable.ic_launcher_foreground);
    }

    @Override
    public int getItemCount() {
        return eventModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView eventIV;
        private final TextView eventNameTV;
        private final TextView eventDateTV;
        private final TextView eventTimeTV;
        private final TextView eventLocationTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventIV = itemView.findViewById(R.id.eventImage);
            eventNameTV = itemView.findViewById(R.id.eventName);
            eventDateTV = itemView.findViewById(R.id.eventDate);
            eventTimeTV = itemView.findViewById(R.id.eventTime);
            eventLocationTV = itemView.findViewById(R.id.eventLoc);

        }

        public void setData(EventModel data, Context context) {
            final DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
            final DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);

            eventLocationTV.setText(data.getLocation());
            eventDateTV.setText(dateFormat.format(data.getDate()));
            eventTimeTV.setText(timeFormat.format(data.getDate()));
            eventNameTV.setText(data.getName());
        }
    }
}
