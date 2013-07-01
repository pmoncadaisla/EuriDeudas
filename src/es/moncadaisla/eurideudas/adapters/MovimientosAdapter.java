package es.moncadaisla.eurideudas.adapters;

import java.util.ArrayList;

import es.moncadaisla.eurideudas.Movimiento;
import es.moncadaisla.eurideudas.R;
import es.moncadaisla.eurideudas.R.id;
import es.moncadaisla.eurideudas.R.layout;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MovimientosAdapter extends ArrayAdapter<Movimiento>{
	private ArrayList<Movimiento> movimientos;
	private Activity activity;
	
	public MovimientosAdapter(Activity a, int textViewResourceId, ArrayList<Movimiento> movimientos){
		super(a,textViewResourceId, movimientos);
		this.activity = a;
		this.movimientos = movimientos;
	}
	
	public static class ViewHolder{
		public TextView item;
		public TextView precio;
		public TextView timestamp;
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View v = convertView;
		ViewHolder holder;
		
		if(v == null){
			LayoutInflater vi = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.listview_movimiento_row, null);
			holder = new ViewHolder();
			holder.item = (TextView)v.findViewById(R.id.movimiento_item);
			holder.precio = (TextView)v.findViewById(R.id.precio);
			holder.timestamp = (TextView)v.findViewById(R.id.timestamp);
			v.setTag(holder);
		}else{
			holder = (ViewHolder)v.getTag();
		}
		
		final Movimiento m = movimientos.get(position);
		if(m != null){
			holder.item.setText(m.getItem());
			holder.precio.setText(m.getPrecio());
			holder.timestamp.setText(m.getTimestamp());
		}
		return v;
	}
}