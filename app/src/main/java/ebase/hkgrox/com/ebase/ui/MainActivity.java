package ebase.hkgrox.com.ebase.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ebase.hkgrox.com.ebase.Api.Addvender;
import ebase.hkgrox.com.ebase.Config;
import ebase.hkgrox.com.ebase.R;
import ebase.hkgrox.com.ebase.bean.USER;
import ebase.hkgrox.com.ebase.util.AppUtil;
import ebase.hkgrox.com.ebase.util.MUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etState;
    private EditText etPincode;
    private EditText etAddress;
    Config config;
    Spinner user_type;
    String login_url2 =config.ip_url;
    String name,address,mobile,state,city,pincode,password,email;
    List<USER> arraylist=new ArrayList<>();
    String[] usertype = { "Select user type","Distributor", "Retailer", "Premium Retailer", "Mechanic"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user_type=findViewById(R.id.user_type);
       /* try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception e) {

        }*/
       /*
        setSupportActionBar(toolbar);
        MUtil.setToolBarNew(this,"Registration",false);

*/
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,usertype);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        user_type.setAdapter(aa);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MUtil.setToolBarNew(this,"Registration",true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViews();
    }
    private EditText etName;
    private EditText etMobile;
    private EditText etEmail;
    private EditText etLocation;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private AppCompatButton btnSubmit;
    private TextView tvSignIn;

    private void findViews() {
        etName = (EditText)findViewById( R.id.et_name );
        etMobile = (EditText)findViewById( R.id.et_mobile );
        etEmail = (EditText)findViewById( R.id.et_email );
        etLocation = (EditText)findViewById( R.id.et_location );
        etPassword = (EditText)findViewById( R.id.etPassword );
        etState = (EditText)findViewById( R.id.et_state );
        etPincode = (EditText)findViewById( R.id.et_pincode );
        etAddress = (EditText)findViewById( R.id.et_address );

        etConfirmPassword = (EditText)findViewById( R.id.etConfirmPassword );
        btnSubmit = (AppCompatButton)findViewById( R.id.btnSubmit );
        tvSignIn = (TextView)findViewById( R.id.tvSignIn );



        btnSubmit.setOnClickListener( this );
        tvSignIn.setOnClickListener( this );
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-02-01 17:53:36 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btnSubmit ) {
            // Handle clicks for btnSubmit
            if (isValid()) {
                name=etName.getText().toString();
                address=etAddress.getText().toString();
                city=etLocation.getText().toString();
                mobile=etMobile.getText().toString();
                email=etEmail.getText().toString();
                state=etState.getText().toString();
                pincode=etPincode.getText().toString();
                password=etPassword.getText().toString();
                MUtil.showProgressDialog(MainActivity.this);
      /*        String method="insert";
                Backgroundtask backgroundtask=new Backgroundtask();
                backgroundtask.execute(method,name,address,email,mobile,state,city,pincode,password);
                //Toast.makeText(getApplicationContext(),name,Toast.LENGTH_SHORT).show();
*/


              Retrofit retrofit=new Retrofit.Builder().baseUrl(login_url2).addConverterFactory(GsonConverterFactory.create()).build();
                Addvender addvender=retrofit.create(Addvender.class);
                Call<List<USER>> call=addvender.inser(name,city,mobile,address,email,password,pincode,state,user_type.getSelectedItem().toString());
                call.enqueue(new Callback<List<USER>>() {
                    @Override
                    public void onResponse(Call<List<USER>> call, Response<List<USER>> response) {
                        //USER list=response.body();
                       // Toast.makeText(getApplicationContext(),"Data inserted",Toast.LENGTH_SHORT).show();
                        List<USER> list= response.body();
                       // Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_SHORT).show();
                        USER user=null;
                        //String code=list.get("Code");
                        for(int i=0;i<list.size();i++)
                        {
                            user=new USER();
                            String name=list.get(i).getNAME();
                            String address=list.get(i).getADDRESS();
                            String city=list.get(i).getCITY();
                            String designtion=list.get(i).getDEGINATION();
                            String mobile=list.get(i).getMOBILE();
                            String enable=list.get(i).getENABLE();
                            String password=list.get(i).getPASSWORD();
                            String pincode=list.get(i).getPINCODE();
                            String state=list.get(i).getSTATE();
                            String email=list.get(i).getEMAIL();

                            user.setNAME(name);
                            user.setADDRESS(address);
                            user.setCITY(city);
                            user.setDEGINATION(designtion);
                            user.setMOBILE(mobile);
                            user.setENABLE(enable);
                            user.setPASSWORD(password);
                            user.setPINCODE(pincode);
                            user.setSTATE(state);
                            user.setEMAIL(email);
                            if(user.getNAME()==null) {
                                Toast.makeText(getApplicationContext(),"Already exist",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if(user_type.getSelectedItem().toString().equals("Distributor"))
                                {
                                    startHomeActivity(user,"distributor");
                                }else{
                                    startHomeActivity(user,"user");

                                }
                            //arraylist.add(user);

                        }
                        }
                        MUtil.dismissProgressDialog();
                    }

                    @Override
                    public void onFailure(Call<List<USER>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"Data cannot be inserted!Please try again!!",Toast.LENGTH_SHORT).show();
                        MUtil.dismissProgressDialog();
                    }
                });




             /*   DatabaseReference databaseReferencenew = AppUtil.getUserReference(MainActivity.this).child(etMobile.getText().toString());
                databaseReferencenew.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        USER user = dataSnapshot.getValue(USER.class);
                       if(user==null){
                           registered();
                       }else{
                           MUtil.showInfoAlertDialog(MainActivity.this,"User already exist with this number try another no or login");
                           MUtil.dismissProgressDialog();
                       }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        MUtil.dismissProgressDialog();
                    }
                });*/

            }
        }

        if ( v == tvSignIn ) {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void registered() {

        DatabaseReference databaseReference = AppUtil.getUserReference(MainActivity.this);
        final USER user = new USER();
        user.setMOBILE(etMobile.getText().toString());
        user.setEMAIL(etEmail.getText().toString());
        user.setCITY(etLocation.getText().toString());
        user.setPASSWORD(etPassword.getText().toString());
        user.setNAME(etName.getText().toString());

        user.setSTATE(etState.getText().toString());
        user.setPINCODE(etPincode.getText().toString());
        user.setADDRESS(etAddress.getText().toString());

        user.setDEGINATION("USER");
        user.setENABLE("true");

        databaseReference.child(user.getMOBILE()).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                MUtil.dismissProgressDialog();
                if (databaseError != null) {
                    MUtil.showSnakbar(MainActivity.this, "User creation failed try again ");
                } else {
                    MUtil.showSnakbar(MainActivity.this, "User created successfully.");
//                    startHomeActivity(user,);
                }
            }
        });


    }
    private void startHomeActivity(USER user,String usertype) {
        Intent intent = new Intent(this,UserHome.class);
        intent.putExtra("Usertype",usertype);
        intent.putExtra("DATA",user);
        startActivity(intent);
        finish();

    }

    private boolean isValid() {
        if(etName.getText().toString().length()<2){
            MUtil.showSnakbar(this,"Name Not Valid");
            return false;
        }

        if(etMobile.getText().toString().length()<10){
            MUtil.showSnakbar(this,"Mobile No Not Valid");
            return false;
        }


        if(etPassword.getText().toString().length()<4){
            MUtil.showSnakbar(this,"Password Not Valid");
            return false;
        }
        return true;
    }

    class Backgroundtask extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... params) {
            String method = params[0];
           // Toast.makeText(getApplicationContext(),method,Toast.LENGTH_SHORT).show();
            if (method.equals("insert")) {
                String name = params[1];
                String address = params[2];
                String email = params[3];
                String mobile = params[4];
                String state = params[5];
                String city = params[6];
                String pincode = params[7];
                String password = params[8];

                try {
                    URL url = new URL(login_url2);
                    HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");

                    httpURLConnection.setDoOutput(true);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data= URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
                            URLEncoder.encode("address","UTF-8")+"="+URLEncoder.encode(address,"UTF-8")+"&"+
                            URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                            URLEncoder.encode("phn","UTF-8")+"="+URLEncoder.encode(mobile,"UTF-8")+"&"+
                            URLEncoder.encode("state","UTF-8")+"="+URLEncoder.encode(state,"UTF-8")+"&"+
                            URLEncoder.encode("city","UTF-8")+"="+URLEncoder.encode(city,"UTF-8")+"&"+
                            URLEncoder.encode("pincode","UTF-8")+"="+URLEncoder.encode(pincode,"UTF-8")+"&"+
                            URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");


                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();

                    InputStream inputStream=httpURLConnection.getInputStream();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result="";
                    String line="";
                    while ((line=bufferedReader.readLine())!=null){
                        result+=line;
                    }
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            MUtil.dismissProgressDialog();
            MUtil.showSnakbar(MainActivity.this, "User created successfully.");
            Intent intent = new Intent(MainActivity.this,HomePageActivity.class);
            startActivity(intent);
            finish();

        }
    }
}











