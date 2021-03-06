package com.example.rohit.moviesnow;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ReviewAdapter extends ArrayAdapter<Review> {
	public ReviewAdapter(Activity context, List<Review> reviews) {
		super(context,0, reviews);
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {

		if (convertView == null) {
			convertView =
					LayoutInflater.from(getContext()).inflate(R.layout.custom_list_item, null);
		}
		TextView author = (TextView) convertView.findViewById(R.id.author);
		TextView content = (TextView) convertView.findViewById(R.id.content);
		Review review = getItem(position);
		author.setText("author: " + review.author);
		content.setText(review.content);

			return convertView;
		}
	}

