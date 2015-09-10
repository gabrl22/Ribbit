package com.example.gabriel.ribbit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gabriel.ribbit.ParseConstants;
import com.example.gabriel.ribbit.R;
import com.parse.ParseObject;

import java.util.Date;
import java.util.List;

/**
 * Created by Gabriel on 09/05/15.
 */
public class InboxAdapter extends ArrayAdapter<ParseObject> {

    protected Context mContext;
    protected List<ParseObject> mMessages;

    public InboxAdapter(Context context, List<ParseObject> messages) {
        super(context, R.layout.inbox_list_view, messages);
        mContext = context;
        mMessages = messages;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Patron utilizado pare reutilizar los items en el listview

        ViewHolder holder;
        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.inbox_list_view, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.message_icon);
            holder.nameLabel = (TextView) convertView.findViewById(R.id.sender_label);
            holder.timeLabel = (TextView) convertView.findViewById(R.id.time_label);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ParseObject message = mMessages.get(position);

        if (message.getString(ParseConstants.KEY_FILE_TYPE).equals(ParseConstants.FILE_TYPE_IMAGE)) {

            holder.iconImageView.setImageResource(R.drawable.ic_photo_black_24dp);
        } else {
            holder.iconImageView.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
        }


        Date date = message.getCreatedAt();
        String time = date.toString();

        holder.nameLabel.setText(message.getString(ParseConstants.KEY_SENDER_NAME));
        holder.timeLabel.setText(message.getString(ParseConstants.KEY_MESSAGE_CREATED));
        //holder.timeLabel.setText(time);
        return convertView;
    }

    private static class ViewHolder {
        ImageView iconImageView;
        TextView nameLabel;
        TextView timeLabel;
    }
    public void refill(List<ParseObject> messages){
        mMessages.clear();
        mMessages.addAll(messages);
        notifyDataSetChanged();
    }
}
