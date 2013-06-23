package es.moncadaisla.eurideudas;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public  class InfoFragment extends Fragment{
	
		private Context context;
		private TextView nombre;
		private TextView deuda;
		private ImageView foto;
		private PictUtil pu;
		private DatosDBHelper dbHelper;
		private SQLiteDatabase database;
		private String[] allColumns = {dbHelper.COLUMN_ID, dbHelper.COLUMN_NAME, dbHelper.COLUMN_SURNAME, dbHelper.COLUMN_DEUDA };
				
    	
    	public InfoFragment(){
    		
    	}
    	
    	 @Override
         public View onCreateView(LayoutInflater inflater, ViewGroup container,
                 Bundle savedInstanceState) {
             return inflater.inflate(R.layout.info_fragment, container, false);
         }
    	 
    	 @Override
    	 public void onActivityCreated(Bundle state){
    		 super.onActivityCreated(state);
    		 context = this.getActivity();
    		 nombre = (TextView)getView().findViewById(R.id.nombre);
    		 deuda = (TextView)getView().findViewById(R.id.euros);
    		 foto = (ImageView)getView().findViewById(R.id.foto);
    		 
    		 dbHelper = new DatosDBHelper(context);
    		 database = dbHelper.getWritableDatabase();
    		 Datos datos = this.getDatosDB();
    		 
    		 nombre.setText(datos.getName()+" "+datos.getSurname());
    		 deuda.setText(String.valueOf(datos.getDeuda()));
    		 foto.setImageBitmap(PictUtil.loadFromCacheFile());
    		 
    		 if(this.isNetworkAvailable()){
	    		 GetInfoTask task = new GetInfoTask();
	    		 task.execute();
    		 }else{
    			 Toast.makeText(context,
    	                    "No hay conexión a internet", Toast.LENGTH_LONG).show();
    		 }
    		 
    		 
    	 }
    	 
    	 private class GetInfoTask extends AsyncTask<Void, Void, DatosUsuario> {

    	        protected void onPreExecute() {
    	        }

    	        
    	        @Override
    	        protected DatosUsuario doInBackground(Void... voids) {
    	        	
    	        	DatosUsuario du = new DatosUsuario();
    	        	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
    	        	String username = settings.getString("username","");
    	        	String password = settings.getString("password","");

    	        	Euri euri = new Euri(username, password);
    	        	String imageUrl = "http://www.eurielec.etsit.upm.es/images/miembro.php?m="+username;
    	        	URL url;
					try {
						url = new URL(imageUrl);					 
	    	        	HttpURLConnection connection  = (HttpURLConnection) url.openConnection();	
	    	        	InputStream is = connection.getInputStream();
	    	        	Log.d("image",is.toString());
	    	        	Bitmap img = BitmapFactory.decodeStream(is); 
	    	        	du.setFoto(img);
	    	        	PictUtil.saveToCacheFile(img);
    	        	
					}catch (Exception e) {
						e.printStackTrace();
					}
    	        	
    	        	du.setDatos(euri.get("infouser"));
    	        	du.setDeuda(euri.get("deuda"));
    	      
    	        	

    	        	return du;
    	        }

    	        @Override
    	        protected void onPostExecute(DatosUsuario du){
    	        	
    	        	Datos datos = new Datos();
    	            
    	            try {
    	            	JSONObject json = new JSONObject(du.getDatos());	                
    	                String name = json.getString("name");
    	                String surname = json.getString("surname");
    	                
    	                nombre.setText(name+" "+surname);
    	                deuda.setText(String.valueOf(du.getDeuda()));
    	                foto.setImageBitmap(du.getFoto());
    	                
    	                datos.setName(name);
    	                datos.setSurname(surname);
    	                datos.setDeuda(du.getDeuda());
    	                Log.d("db","Preparando para guardar");
    	                setDatosDB(datos);

    	            }
    	            catch(Exception e){
    	            	Log.d("json", "Error parseando json: "+e);
    	            }
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