package com.elanelango.poparazzi;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by eelango on 2/4/16.
 */
public class PhotosAdapter extends ArrayAdapter<Photo> {

    public PhotosAdapter(Context context, List<Photo> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Photo photo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        ImageView ivProfilePic = (ImageView) convertView.findViewById(R.id.ivProfilePic);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);

        tvUsername.setText(photo.username);
        tvTime.setText(photo.getTime());
        SpannableString styledCaption = new SpannableString(photo.username + " " + photo.caption);
        styledCaption.setSpan(new StyleSpan(Typeface.BOLD), 0, photo.username.length(), 0);
        tvCaption.setText(styledCaption);
        tvLikes.setText("\u2665 " + Integer.toString(photo.likesCount) + " likes");

        ivPhoto.setImageResource(0);

        Picasso.with(getContext())
                .load(photo.imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(ivPhoto);

        Picasso.with(getContext())
                .load(photo.profilePicURL)
                .into(ivProfilePic);

        return convertView;
    }
}
