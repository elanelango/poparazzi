package com.elanelango.poparazzi;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;

/**
 * Created by eelango on 2/4/16.
 */
public class PhotosAdapter extends ArrayAdapter<Photo> {

    public PhotosAdapter(Context context, List<Photo> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    private SpannedString getStyledComment(String username, String text) {
        SpannableString styledComment = new SpannableString(username + " " + text);
        styledComment.setSpan(new StyleSpan(Typeface.BOLD), 0, username.length(), 0);
        return (SpannedString) TextUtils.concat(styledComment, "\n");
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
        SpannedString styledComments = getStyledComment(photo.username, photo.caption);

        int commentsLimit = 2;
        for (int i = 0, index = photo.comments.size() - 1; index >= 0 && i < commentsLimit; i++, index--) {
            Comment commentObj = photo.comments.get(index);
            SpannedString commentStr = getStyledComment(commentObj.username, commentObj.text);
            styledComments = (SpannedString) TextUtils.concat(styledComments, commentStr);
        }

        tvCaption.setText(styledComments);
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
