package es.moncadaisla.eurideudas.fragments;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import es.moncadaisla.eurideudas.Categoria;
import es.moncadaisla.eurideudas.R;
import es.moncadaisla.eurideudas.R.id;
import es.moncadaisla.eurideudas.R.layout;
import es.moncadaisla.eurideudas.activities.ProductosActivity;
import es.moncadaisla.eurideudas.util.ColorList;
import es.moncadaisla.eurideudas.util.Euri;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;


public  class CategoriasFragment extends Fragment{

	private CategoryAdapter adapter;
	private Context context;
	private GridView gridview;
	ArrayList<Categoria> categoriasAll;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.categorias_fragment, container, false);
		gridview = (GridView) v.findViewById(R.id.gridView1);		
		TextView emptyT = (TextView) v.findViewById(android.R.id.empty);
		gridview.setEmptyView(emptyT);
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle state){
		super.onActivityCreated(state);
		context = this.getActivity();    	
		loadContent();
		
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Intent i = new Intent(context, ProductosActivity.class);
		        i.putExtra("cid", position);
		        i.putExtra("name", categoriasAll.get(position).getNombre());
		        startActivity(i);				
			}
		});

	}
	
	private void loadContent(){
		if(this.isNetworkAvailable()){
			GetCategoriasTask task = new GetCategoriasTask();
			task.execute();
		}else{
			Toast.makeText(context,
					"No hay conexión a internet", Toast.LENGTH_LONG).show();
		}
	}
	
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public class CategoryAdapter extends ArrayAdapter<Categoria> {
		private ArrayList<Categoria> categorias;
		
		public CategoryAdapter(Activity a, int textViewResourceId, ArrayList<Categoria> categorias){
			super(a,textViewResourceId, categorias);
			this.categorias = categorias;
		}


		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout l = new LinearLayout(context);
			GridView.LayoutParams params = new GridView.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			Categoria categoria = categorias.get(position);
			l.setLayoutParams(params);
			l.setBackgroundColor(ColorList.getColor(categoria.getId()));
			TextView t = new TextView(context);
			t.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 300));
			t.setText(categoria.getNombre());
			t.setTextColor(Color.WHITE);
			t.setTextSize(22);
			t.setGravity(Gravity.CENTER);
			l.addView(t);
			return l;
		}

	}	
	
	private class GetCategoriasTask extends AsyncTask<Void, Void, ArrayList<Categoria> > {

		protected void onPreExecute() {
		}

		@Override
		protected ArrayList<Categoria> doInBackground(Void... voids) {
			
			Euri euri = new Euri();
			ArrayList<Categoria> categorias= new ArrayList<Categoria>();
			try {
				JSONArray jsonArray = new JSONArray(euri.getCategorias());
				for(int i=0; i<jsonArray.length(); i++){					
					JSONObject json = jsonArray.getJSONObject(i);
					categorias.add(new Categoria(Integer.parseInt(json.getString("id")),json.getString("name")));
				}
			}
			catch(Exception e){
				Log.d("json", "Error parseando json: "+e);
			}
			Log.d("movimientos", "Tamaño: "+categorias.size());
			return categorias;
		}

		@Override
		protected void onPostExecute(ArrayList<Categoria> categorias){    	        	
			adapter = new CategoryAdapter( (Activity)context, R.id.gridView1, categorias);
			gridview.setAdapter(adapter);
			categoriasAll = categorias;

		}
	}
	


	
}