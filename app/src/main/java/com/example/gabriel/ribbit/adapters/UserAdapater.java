package com.example.gabriel.ribbit.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gabriel.ribbit.R;
import com.example.gabriel.ribbit.helper_methods.MD5Utils;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

/**
 * Created by Gabriel on 09/05/15.
 */
public class UserAdapater extends ArrayAdapter<ParseUser> {

    protected Context mContext;
    protected List<ParseUser> mUsers;

    public UserAdapater(Context context, List<ParseUser> users) {
        super(context, R.layout.inbox_list_view, users);
        mContext = context;
        mUsers = users;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Patron utilizado pare reutilizar los items en el listview

        ViewHolder holder;
        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.user_item, null);
            holder = new ViewHolder();
            holder.userImageView = (ImageView) convertView.findViewById(R.id.user_image_view);
            holder.nameLabel = (TextView) convertView.findViewById(R.id.name_label);
            holder.checkImageView = (ImageView)convertView.findViewById(R.id.check_image_view);


            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ParseUser user = mUsers.get(position);
        String email = user.getEmail().toLowerCase();
        if(email.equals("")){
            holder.userImageView.setImageResource(R.drawable.avatar_empty);
        }
        else{

            String hash = MD5Utils.md5Hex(email);
            String gravatarUrl = "http://www.gravatar.com/avatar/" + hash +
                    "?s=204&d=404";
            Log.d("Haha", gravatarUrl);
            Picasso.with(mContext).load(gravatarUrl).placeholder(R.drawable.avatar_empty)
                    .into(holder.userImageView);
        }
        Date createdAt = user.getCreatedAt();
        long now = new Date().getTime();
        String convertedDate = DateUtils.getRelativeTimeSpanString(createdAt.getTime(),
                now, DateUtils.SECOND_IN_MILLIS).toString();

        /*if (user.getString(ParseConstants.KEY_FILE_TYPE).equals(ParseConstants.FILE_TYPE_IMAGE)) {

            holder.iconImageView.setImageResource(R.drawable.ic_picture);
        } else {
            holder.iconImageView.setImageResource(R.drawable.ic_video);
        }*/


        Date date = user.getCreatedAt();
        String time = date.toString();

        holder.nameLabel.setText(user.getUsername());
        //holder.timeLabel.setText(message.getString(ParseConstants.KEY_MESSAGE_CREATED));

        GridView gridView = (GridView)parent;
        if(gridView.isItemChecked(position)){
            holder.checkImageView.setVisibility(View.VISIBLE);
        }
        else{
            holder.checkImageView.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView userImageView;
        TextView nameLabel;
        ImageView checkImageView;

    }
    public void refill(List<ParseUser> users){
        mUsers.clear();
        mUsers.addAll(users);
        notifyDataSetChanged();
    }
}
