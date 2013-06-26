package es.moncadaisla.eurideudas;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.protocol.HTTP;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;


/**
 * Clase para obtener una pagina de la Politecnica Virtual (UPM)
 * @author Pablo Moncada
 * @version 11/06/2013
 *
 */
public class Euri {

    private String username;
    private String password;
    private String endpoint = "http://www.eurielec.etsit.upm.es/helpers/json.php";
    private String productosEndPoint = "http://www.eurielec.etsit.upm.es/helpers/productos.php";
    private String neveraEndPoint = "http://www.eurielec.etsit.upm.es/helpers/socios.php";
    CookieStore cookies;

    /**
     * Constructor de la clase
     * @param username (con @alumnos.upm.es)
     * @param password (La contraseña del usuario)
     */
    public Euri(String username, String password){
        this.username = username;
        this.password = password;
        getCookie();
        
    }
    
    public Euri(){
    	
    }

    /**
     * Se encarga de obtener la cookie de la sesión
     * @return false si no consigue obtener la cookie
     */
    private boolean getCookie() {
        DefaultHttpClient httpclient = new DefaultHttpClient();

        try{
            HttpPost httpost = new HttpPost("http://www.eurielec.etsit.upm.es/login.php");

            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            nvps.add(new BasicNameValuePair("login", this.username));
            nvps.add(new BasicNameValuePair("password", this.password));

            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            HttpResponse response = httpclient.execute(httpost);
            HttpEntity entity = response.getEntity();
            entity.consumeContent();

            List<Cookie> cookies = httpclient.getCookieStore().getCookies();
            if (cookies.isEmpty()) {
                return false;
            }else{
                this.cookies = httpclient.getCookieStore();
            }
        } catch (ClientProtocolException e) {
        } catch (Exception e) {
        }finally{
            httpclient.getConnectionManager().shutdown();
        }

        return true;
    }


    /**
     * Pagina solicitada
     * @param accion Ejemplo: "userinfo"
     * @return
     */
    public String get(String action){

    	this.getCookie();
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.setCookieStore(this.cookies);
        

        try{
            HttpPost httpost = new HttpPost(endpoint);

            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            nvps.add(new BasicNameValuePair("action", action));

            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            HttpResponse response = httpclient.execute(httpost);            
            
            HttpEntity entity = response.getEntity();
            entity = response.getEntity();
            
            

            return  EntityUtils.toString(entity);

        } catch (ClientProtocolException e) {
        	Log.d("exception", e.toString());
        } catch (IOException e){
        	Log.d("exception", e.toString());
        } finally{
            httpclient.getConnectionManager().shutdown();
        }
        return "error";

    }
    
    /**
     * Pagina solicitada
     * @param accion Ejemplo: "userinfo"
     * @return
     */
    public String putDeuda(String item, String precio){

    	this.getCookie();
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.setCookieStore(this.cookies);
        

        try{
            HttpPost httpost = new HttpPost(this.neveraEndPoint);

            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            nvps.add(new BasicNameValuePair("item", item));
            nvps.add(new BasicNameValuePair("precio", precio));
            nvps.add(new BasicNameValuePair("tipo", "plus"));
            nvps.add(new BasicNameValuePair("action", "nevera"));

            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            HttpResponse response = httpclient.execute(httpost);            
            
            HttpEntity entity = response.getEntity();
            entity = response.getEntity();          
            

            return  EntityUtils.toString(entity);

        } catch (ClientProtocolException e) {
        	Log.d("exception", e.toString());
        } catch (IOException e){
        	Log.d("exception", e.toString());
        } finally{
            httpclient.getConnectionManager().shutdown();
        }
        return "error";

    }
    
    /**
     * Para devolver las categorias
     * @param accion Ejemplo: "userinfo"
     * @return
     */
    public String getCategorias(){
    	
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try{
            HttpPost httpost = new HttpPost(productosEndPoint);
            HttpResponse response = httpclient.execute(httpost);
            HttpEntity entity = response.getEntity();
            entity = response.getEntity();

            return  EntityUtils.toString(entity);

        } catch (ClientProtocolException e) {
        	Log.d("exception", e.toString());
        } catch (IOException e){
        	Log.d("exception", e.toString());
        } finally{
            httpclient.getConnectionManager().shutdown();
        }
        return "error";
    }
    
    /**
     * Para devolver las categorias
     * @param accion Ejemplo: "userinfo"
     * @return
     */
    public String getProductos(int cid){
    	
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String url = productosEndPoint + "?cid=" + cid;
        Log.d("getProductos", url);
        try{
            HttpPost httpost = new HttpPost(url);
            HttpResponse response = httpclient.execute(httpost);
            HttpEntity entity = response.getEntity();
            entity = response.getEntity();
            
            return  EntityUtils.toString(entity);
            
        } catch (ClientProtocolException e) {
        	Log.d("exception", e.toString());
        } catch (IOException e){
        	Log.d("exception", e.toString());
        } finally{
            httpclient.getConnectionManager().shutdown();
        }
        return "error";
    }



}