package com.belikeastamp.admin.util;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.belikeastamp.admin.R;
import com.belikeastamp.admin.model.Tutorial;

public class TutorialAdapter extends ArrayAdapter<Tutorial> {

	private final Context context;
	private final  List<Tutorial> values;

	static class ViewHolder {
		public ImageView icon;
		public TextView title;
		public TextView date;
		public TextView available;
		public TextView demand;
	}
	public TutorialAdapter(Context context, List<Tutorial> objects) {
		super(context,R.layout.tutorial_listview, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.values = objects;
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		//LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = convertView;//inflater.inflate(R.layout.workshop_listview, parent, false);
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.tutorial_listview, null);
			// configure view holder
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.icon = (ImageView) rowView.findViewById(R.id.icon);
			viewHolder.title= (TextView) rowView.findViewById(R.id.title);
			viewHolder.date= (TextView) rowView.findViewById(R.id.date);
			viewHolder.available= (TextView) rowView.findViewById(R.id.available);
			viewHolder.demand= (TextView) rowView.findViewById(R.id.demand);
			
			rowView.setTag(viewHolder);
		}

		// fill data
		ViewHolder holder = (ViewHolder) rowView.getTag();
		
		if (values.get(position).getOnDemand() == 0) {
			holder.icon.setImageResource(R.drawable.nobody);
		} else if (values.get(position).getOnDemand() >= 5 && values.get(position).getOnDemand() < 10) {
			holder.icon.setImageResource(R.drawable.tiers);
		}else if (values.get(position).getOnDemand() >= 10 && values.get(position).getOnDemand() < 15) {
			holder.icon.setImageResource(R.drawable.middle);
		} else if (values.get(position).getOnDemand() >= 15 && values.get(position).getOnDemand() < 20) {
			holder.icon.setImageResource(R.drawable.twotiers);
		} else {
			holder.icon.setImageResource(R.drawable.full);
		}
		
		holder.title.setText(context.getResources().getString(R.string.title)+" : "+values.get(position).getTitle());
		holder.date.setText(context.getResources().getString(R.string.date)+" : "+values.get(position).getDate());
		holder.available.setText(context.getResources().getString(R.string.availability)+" : "+values.get(position).getAvailable().toString());
		

		return rowView;
	}

}
