package es.moncadaisla.eurideudas;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
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


public  class ProductosActivity extends Activity{

	private ProductosAdapter adapter;
	private GridView gridview;
	private int cid;
	private ArrayList<Producto> productosAll;
	
	@Override
	public void onCreate(Bundle state){
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		super.onCreate(state);
		Bundle bundle = getIntent().getExtras();
		
		cid = bundle.getInt("cid", 0) + 1;
		setTitle(bundle.getString("name","Productos"));
		
		setContentView(R.layout.categorias_fragment);
		loadContent();
		gridview = (GridView) findViewById(R.id.gridView1);		
		TextView emptyT = (TextView) findViewById(android.R.id.empty);
		gridview.setEmptyView(emptyT);
		
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				
				// 1. Instantiate an AlertDialog.Builder with its constructor
				AlertDialog.Builder builder = new AlertDialog.Builder(ProductosActivity.this);

				final Producto pr = productosAll.get(position);
				// 2. Chain together various setter methods to set the dialog characteristics
				builder.setMessage(pr.getNombre()+" "+pr.getPrecio()+" EUR")
				       .setTitle("Confirmar compra");
				
				builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			               ComprarTask task = new ComprarTask();
			               task.execute(pr);
			           }
			       });
				builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			               // User cancelled the dialog
			           }
			       });

				// 3. Get the AlertDialog from create()
				AlertDialog dialog = builder.create();
				dialog.show();
				
			}
		});

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void loadContent(){
		if(this.isNetworkAvailable()){
			GetProductosTask task = new GetProductosTask();
			task.execute();
		}else{
			Toast.makeText(this,
					"No hay conexión a internet", Toast.LENGTH_LONG).show();
		}
	}
	
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	
	
	public class ProductosAdapter extends ArrayAdapter<Producto> {
		private ArrayList<Producto> productos;
		
		public ProductosAdapter(Activity a, int textViewResourceId, ArrayList<Producto> productos){
			super(a,textViewResourceId, productos);
			this.productos = productos;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout l = new LinearLayout(ProductosActivity.this);
			GridView.LayoutParams params = new GridView.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			Producto producto = productos.get(position);
			l.setLayoutParams(params);
			l.setBackgroundColor(ColorList.getColor(producto.getCid()));
			TextView t = new TextView(ProductosActivity.this);
			t.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 300));
			t.setText(producto.getNombre());
			t.setTextColor(Color.WHITE);
			t.setTextSize(22);
			t.setGravity(Gravity.CENTER);
			l.addView(t);
			return l;
		}

	}
	
	private class GetProductosTask extends AsyncTask<Void, Void, ArrayList<Producto> > {

		protected void onPreExecute() {
		}

		@Override
		protected ArrayList<Producto> doInBackground(Void... voids) {
			
			Euri euri = new Euri();
			ArrayList<Producto> productos= new ArrayList<Producto>();
			try {
				JSONArray jsonArray = new JSONArray(euri.getProductos(cid));
				for(int i=0; i<jsonArray.length(); i++){					
					JSONObject json = jsonArray.getJSONObject(i);
					productos.add(new Producto(json.getString("name"),
							json.getString("price"),
							Integer.parseInt(json.getString("cid")) ) );
				}
			}
			catch(Exception e){
				Log.d("json", "Error parseando json: "+e);
			}
			Log.d("productos", "Tamaño: "+productos.size());
			return productos;
		}

		@Override
		protected void onPostExecute(ArrayList<Producto> productos){    	        	
			adapter = new ProductosAdapter( ProductosActivity.this, R.id.gridView1, productos);
			gridview.setAdapter(adapter);
			productosAll = productos;

		}
	}
	
	private class ComprarTask extends AsyncTask<Producto, Void, Void > {

		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(Producto... p) {			
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ProductosActivity.this);
			String username = settings.getString("username","");
			String password = settings.getString("password","");

			Euri euri = new Euri(username, password);
			euri.putDeuda(p[0].getNombre(), p[0].getPrecio());			
			return null;
			
		}

		@Override
		protected void onPostExecute(Void voids){    	        	
			Toast.makeText(ProductosActivity.this,
					"Añadido", Toast.LENGTH_SHORT).show();
		}
	}
	


	
}