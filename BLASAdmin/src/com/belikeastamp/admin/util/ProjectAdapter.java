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
import com.belikeastamp.admin.model.Project;

public class ProjectAdapter extends ArrayAdapter<Project> {

	private final Context context;
	private final  List<Project> values;
	private final String[] status;
	
	static class ViewHolder {
		public TextView name;
		public TextView submitdate;
		public TextView type;
		public TextView nbr_cards;
		public TextView orderdate;
		public TextView status;
	}
	public ProjectAdapter(Context context, List<Project> objects, String[] status) {
		super(context,R.layout.project_listview, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.values = objects;
		this.status = status;
	}


	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.project_listview, null);
			// configure view holder
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.name =  (TextView) rowView.findViewById(R.id.name);
			viewHolder.submitdate = (TextView) rowView.findViewById(R.id.submitdate);
			viewHolder.type = (TextView) rowView.findViewById(R.id.type);
			viewHolder.nbr_cards = (TextView) rowView.findViewById(R.id.nbr_cards);
			viewHolder.orderdate = (TextView) rowView.findViewById(R.id.orderdate);
			viewHolder.status = (TextView) rowView.findViewById(R.id.status);
			
			rowView.setTag(viewHolder);
		}

		// fill data
		ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.name.setText(context.getResources().getString(R.string.project_name)+" : "+values.get(position).getName());
		holder.submitdate.setText(context.getResources().getString(R.string.submit_date)+" : "+values.get(position).getSubDate());
		holder.submitdate.setTag(values.get(position));
		holder.type.setText(context.getResources().getString(R.string.project_type)+" : "+values.get(position).getType());
		holder.nbr_cards.setText(context.getResources().getString(R.string.how_many_cards)+" : "+values.get(position).getQuantity());
		holder.orderdate.setText(context.getResources().getString(R.string.order_date)+" : "+values.get(position).getOrderDate());
		holder.status.setText(status[values.get(position).getStatus()]);
		
		return rowView;
	}

}
