package com.belikeastamp.admin.util;

import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.belikeastamp.admin.R;
import com.belikeastamp.admin.model.Inscription;
import com.belikeastamp.admin.model.Workshop;

public class InscriptionAdapter extends ArrayAdapter<Inscription> {

	private final Context context;
	private final  List<Inscription> values;

	static class ViewHolder {
		public TextView date;
		public TextView workshop;
		public TextView participants;
		public TextView status;
	}

	public InscriptionAdapter(Context context, List<Inscription> objects) {
		super(context,R.layout.tutorial_listview, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.values = objects;
	}


	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView = convertView;

		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.inscription_listview, null);
			// configure view holder
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.date= (TextView) rowView.findViewById(R.id.date);
			viewHolder.workshop= (TextView) rowView.findViewById(R.id.workshop);
			viewHolder.participants = (TextView) rowView.findViewById(R.id.participants);
			viewHolder.status= (TextView) rowView.findViewById(R.id.status);
			
			rowView.setTag(viewHolder);
		}

		// fill data
		ViewHolder holder = (ViewHolder) rowView.getTag();
		Workshop selectedWorkshop = null;
		try {
			selectedWorkshop = new GetWorkShopTask().execute(values.get(position).getWorkshopId()).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		holder.status.setText(context.getResources().getString(R.string.insc_status)+" : "+values.get(position).getInscriptionStatus());
		holder.participants.setText(context.getResources().getString(R.string.nbr_participant)+" : "+(values.get(position).getPartcipants()+1));
		holder.date.setText(context.getResources().getString(R.string.inscription_date)+" : "+values.get(position).getInscriptionDate());
		holder.date.setTag(values.get(position));
		holder.workshop.setText(context.getResources().getString(R.string.workshop)+" : "+selectedWorkshop.getTheme());
		holder.workshop.setTag(selectedWorkshop);
		return rowView;
	}
	
	
	class GetWorkShopTask extends AsyncTask<Long, Void, Workshop> {

		@Override
		protected Workshop doInBackground(Long... params) {
			
			Long workshopId = params[0];
			WorkshopController c = new WorkshopController();
			Workshop w = c.getWorkshops(workshopId);
			
			return w;
		}
	
		
	}
}