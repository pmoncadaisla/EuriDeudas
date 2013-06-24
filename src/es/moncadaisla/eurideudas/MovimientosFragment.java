package es.moncadaisla.eurideudas;



import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import eu.erikw.PullToRefreshListView.OnRefreshListener;
import eu.erikw.PullToRefreshListView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public  class MovimientosFragment extends Fragment{
	
		private Context context;
		private DatosDBHelper dbHelper;
		private SQLiteDatabase database;
		private String[] allColumns = {dbHelper.COLUMN_ID, dbHelper.COLUMN_NAME, dbHelper.COLUMN_SURNAME, dbHelper.COLUMN_DEUDA };
		private ArrayList<Movimiento> movimientos = new ArrayList<Movimiento>();
		private PullToRefreshListView lv;
		private MovimientosAdapter adapter;
		private Button refrescar;
		private GetInfoTask task;
    	
    	public MovimientosFragment(){
    		
    	}
    	
    	 @Override
         public View onCreateView(LayoutInflater inflater, ViewGroup container,
                 Bundle savedInstanceState) {
             return inflater.inflate(R.layout.movimientos_fragment, container, false);
         }
    	 

    	    
    	 
    	   	 
    	 @Override
    	 public void onActivityCreated(Bundle state){
    		 super.onActivityCreated(state);
    		 context = this.getActivity();
    		 
    		// refrescar = (Button) ((Activity) context).findViewById(R.id.menu_refresh);
    		 
    		 lv = (PullToRefreshListView) ((Activity) context).findViewById(R.id.pull_to_refresh_listview);
    		 dbHelper = new DatosDBHelper(context);
    		 database = dbHelper.getWritableDatabase();
    		 Datos datos = this.getDatosDB();
    		 
    		 
    		 if(this.isNetworkAvailable()){	
    			 task = new GetInfoTask();
	    		 task.execute();
    		 }else{
    			 Toast.makeText(context,
    	                    "No hay conexi�n a internet", Toast.LENGTH_LONG).show();
    		 }
    		 
    		 lv.setOnRefreshListener(new OnRefreshListener() {
 	            @Override
 	            public void onRefresh() {
 	            	task = new GetInfoTask();
 	                task.execute(); 	                
 	            }
    		 });
    		 
    		 
    		 
    		 
    	 }
    	 
    	 private class GetInfoTask extends AsyncTask<Void, Void, ArrayList<Movimiento> > {

    	        protected void onPreExecute() {
    	        }

    	        
    	        @Override
    	        protected ArrayList<Movimiento> doInBackground(Void... voids) {
    	        	
    	        	
    	        	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
    	        	String username = settings.getString("username","");
    	        	String password = settings.getString("password","");

    	        	Euri euri = new Euri(username, password);
    	        	ArrayList<Movimiento> movimientos= new ArrayList<Movimiento>();
    	        	
    	        	try {
    	            	JSONArray jsonArray = new JSONArray(euri.get("movimientos"));
    	            	for(int i=0; i<jsonArray.length(); i++){
    	            		Movimiento m = new Movimiento();
    	            		JSONObject json = jsonArray.getJSONObject(i);
    	            		m.setItem(json.getString("item"));
    	            		m.setPrecio(json.getString("precio"));
    	            		m.setTimestamp(json.getString("timestamp"));
    	            		movimientos.add(m);
    	            	}
    	            	


    	            }
    	            catch(Exception e){
    	            	Log.d("json", "Error parseando json: "+e);
    	            }
    	        	Log.d("movimientos", "Tama�o: "+movimientos.size());
 	        	
    	        	return movimientos;
    	        }

    	        @Override
    	        protected void onPostExecute(ArrayList<Movimiento> movimientos){    	        	
    	        	adapter = new MovimientosAdapter((Activity) context, R.id.pull_to_refresh_listview, movimientos);
    	        	lv.setAdapter(adapter);
    	        	lv.onRefreshComplete();
    	        	   	        
    	            
    	            
    	        }
    	        
    	 
    	 }
    	 
    	 private Datos getDatosDB(){
    		 
    		 
    		 Log.d("db","Recuperando datos de BD");
    		 
    		 Cursor cursor = database.query(DatosDBHelper.TABLE_DATOS,
    			        allColumns, null, null, null, null, null);
    		 
    		 Datos datos = new Datos();
    		 if(cursor.getCount() > 0){	   
    			 cursor.moveToFirst();    			 
	    		 datos.setName(cursor.getString(1));
	    		 datos.setSurname(cursor.getString(2));
	    		 datos.setDeuda(cursor.getString(3));
    		 }else{
    			 Log.d("db","Nada que recuperar :(");
    		 }
    		 
    		cursor.close();
			return datos;
    		 
    	 }
    	 
    	 private void setDatosDB(Datos datos){
    		 Log.d("db","Guardando datos en base de datos");
    		 try{
		    	 database.execSQL("DELETE FROM "+DatosDBHelper.TABLE_DATOS );
		    	 String query = "INSERT INTO "+DatosDBHelper.TABLE_DATOS+" ("+DatosDBHelper.COLUMN_NAME+","+DatosDBHelper.COLUMN_SURNAME+","+DatosDBHelper.COLUMN_DEUDA+")" +
		    	 		"VALUES ('"+datos.getName()+"', '"+datos.getSurname()+"', '"+datos.getDeuda()+"');";
		    	 database.execSQL(query);
    		 }
	    	 catch(SQLException e){
	    		 e.printStackTrace();
	    	 }
	    	 database.close();
    	 
    	 }
    	 
    	 private boolean isNetworkAvailable() {
    		    ConnectivityManager connectivityManager 
    		          = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    		    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    		    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    		}
    	 
    	 
    	 
    	 private class DatosUsuario{
    		 private String datos;
    		 private Bitmap foto;
    		 private String deuda;
			public String getDeuda() {
				return deuda;
			}
			public void setDeuda(String deuda) {
				this.deuda = deuda;
			}
			public String getDatos() {
				return datos;
			}
			public void setDatos(String datos) {
				this.datos = datos;
			}
			public Bitmap getFoto() {
				return foto;
			}
			public void setFoto(Bitmap foto) {
				this.foto = foto;
			}
    		 
    		 
    	 }
}