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
import com.belikeastamp.admin.model.Workshop;

public class WorkshopAdapter extends ArrayAdapter<Workshop> {

	private final Context context;
	private final  List<Workshop> values;

	static class ViewHolder {
		public TextView data;
		public ImageView icon;
	}
	public WorkshopAdapter(Context context, List<Workshop> objects) {
		super(context,R.layout.workshop_listview, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.values = objects;
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		//LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = convertView;//inflater.inflate(R.layout.workshop_listview, parent, false);
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.workshop_listview, null);
			// configure view holder
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.data= (TextView) rowView.findViewById(R.id.data);
			viewHolder.icon = (ImageView) rowView.findViewById(R.id.icon);
			rowView.setTag(viewHolder);
		}

		// fill data
		ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.data.setText(values.get(position).getTheme() +" in "+values.get(position).getTown()+" ("+values.get(position).getDate()+")");
		//holder.data.setText(values.get(position).toString());
		holder.data.setTag(values.get(position));

		int ratio = 0;
		if(values.get(position).getRegistered() > 0 ) 
			ratio = values.get(position).getCapacity()/values.get(position).getRegistered();

		if (values.get(position).getRegistered() == 0) {
			holder.icon.setImageResource(R.drawable.nobody);
		} else if (values.get(position).getRegistered() > 0 && ratio >= 3){
			holder.icon.setImageResource(R.drawable.tiers);
		} else if (ratio < 3 && ratio >= 2){
			holder.icon.setImageResource(R.drawable.middle);
		} else if (ratio < 2 && values.get(position).getRegistered() < values.get(position).getCapacity()){
			holder.icon.setImageResource(R.drawable.twotiers);
		} else {
			holder.icon.setImageResource(R.drawable.full);
		}

		return rowView;
	}

}
