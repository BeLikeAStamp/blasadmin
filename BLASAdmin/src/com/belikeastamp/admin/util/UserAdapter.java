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
import com.belikeastamp.admin.model.User;

public class UserAdapter extends ArrayAdapter<User> {

	private final Context context;
	private final  List<User> values;

	static class ViewHolder {
		public TextView data;
		public ImageView partener;
		public ImageView host;
	}
	public UserAdapter(Context context, List<User> objects) {
		super(context,R.layout.user_listview, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.values = objects;
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.user_listview, null);
			// configure view holder
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.data= (TextView) rowView.findViewById(R.id.data);
			viewHolder.partener = (ImageView) rowView.findViewById(R.id.partener);
			viewHolder.host = (ImageView) rowView.findViewById(R.id.host);
			rowView.setTag(viewHolder);
		}

		// fill data
		ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.data.setText(values.get(position).getFirstname() +" "+values.get(position).getName()+" ("+values.get(position).getEmail()+")");
		holder.data.setTag(values.get(position));

		if(values.get(position).getIsHost()) 
			holder.host.setImageResource(R.drawable.host);
		if(values.get(position).getIsPartener()) 
			holder.partener.setImageResource(R.drawable.partener);
		
		// change the icon for Windows and iPhone
		/*int ratio = 0;
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
		}*/

		return rowView;
	}

}
