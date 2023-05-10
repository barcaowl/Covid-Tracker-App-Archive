package com.example.covidtrackerjava;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final String NAME = "com.example.covidtrackerjava.example.NAME";
    public static final String LAT = "com.example.covidtrackerjava.example.LAT";
    public static final String LONGI = "com.example.covidtrackerjava.example.LONGI";
    public static final String CONFIRMED = "com.example.covidtrackerjava.example.CONFIRMED";
    public static final String DEATHS = "com.example.covidtrackerjava.example.DEATHS";
    public static final String AMOUNTTESTED = "com.example.covidtrackerjava.example.AMOUNTTESTED";
    public static final String AMOUNTHOSPITALIZED = "com.example.covidtrackerjava.example.AMOUNTHOSPITALIZED";
    public static final String RECOVERED = "com.example.covidtrackerjava.example.RECOVERED";
    public static final String ACTIVE = "com.example.covidtrackerjava.example.ACTIVE";
    public static final String DARKON = "com.example.covidtrackerjava.example.DARKON";
    public static final String DARKSTATUS = "com.example.covidtrackerjava.example.DARKSTATUS";
    public static final String NAMEPAST = "com.example.covidtrackerjava.example.NAMEPAST";
    public static final String LATPAST = "com.example.covidtrackerjava.example.LATPAST";
    public static final String LONGIPAST = "com.example.covidtrackerjava.example.LONGIPAST";
    public static final String CONFIRMEDPAST = "com.example.covidtrackerjava.example.CONFIRMEDPAST";
    public static final String DEATHSPAST = "com.example.covidtrackerjava.example.DEATHSPAST";
    public static final String AMOUNTTESTEDPAST = "com.example.covidtrackerjava.example.AMOUNTTESTEDPAST";
    public static final String AMOUNTHOSPITALIZEDPAST = "com.example.covidtrackerjava.example.AMOUNTHOSPITALIZEDPAST";
    public static final String RECOVEREDPAST = "com.example.covidtrackerjava.example.RECOVEREDPAST";
    public static final String ACTIVEPAST = "com.example.covidtrackerjava.example.ACTIVEPAST";

    public static final int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    TextView stateTextView;

    private static final String TAG = MainActivity.class.getSimpleName();


    public Boolean darkstatus;

    public SharedPreferences mPreferences;
    public String sharedPrefFile =
            "com.example.android.hellosharedprefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);


        darkstatus = mPreferences.getBoolean(DARKSTATUS, true);

        System.out.println("MAIN " + darkstatus);
        SharedPreferences dark = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = dark.edit();
        Boolean darkon = darkstatus;
        editor.putBoolean(DARKON, darkon);
        editor.commit();

        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES || darkstatus) {
            setTheme(R.style.darktheme);
        }
        else setTheme(R.style.AppTheme);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadFilesTaskUSA downloadFilesTaskUSA = new DownloadFilesTaskUSA();
        downloadFilesTaskUSA.execute();





       Switch myswitch = (Switch)findViewById(R.id.myswitch);
       if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES || darkstatus){
           myswitch.setChecked(true);
       }
        myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    restartApp();
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    restartApp();
                }
            }
        });





        stateTextView = findViewById(R.id.stateTextView);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Button moredata;
        moredata = (Button) findViewById(R.id.moredata);
        moredata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriURL = Uri.parse("https://www.arcgis.com/apps/opsdashboard/index.html#/85320e2ea5424dfaaa75ae62e5c06e61");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriURL);
                startActivity(launchBrowser);
            }
        });
       // fileContent = (TextView)findViewById(R.id.content_from_server);
       // Button loadTextButton = (Button)findViewById(R.id.load_file_from_server);
//        loadTextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DownloadFilesTask downloadFilesTask = new DownloadFilesTask();
//                downloadFilesTask.execute();
//            }
//
//
//        });
        ImageView day = (ImageView) findViewById(R.id.day);
        ImageView night = (ImageView) findViewById(R.id.night);

        ImageButton currentlocation = (ImageButton)findViewById(R.id.currentlocation);
        currentlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });
        if (darkstatus) {
            currentlocation.setColorFilter(Color.WHITE);
            day.setColorFilter(Color.WHITE);
            night.setColorFilter(Color.WHITE);
        } else
        {
            currentlocation.setColorFilter(Color.BLACK);
            day.setColorFilter(Color.BLACK);
            night.setColorFilter(Color.BLACK);
        }
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String[] states = new String[]{
                "Select a State", "USA", "Alabama", "Alaska", "American Samoa", "Arizona","Arkansas","California","Colorado","Connecticut","Delaware",
                "Washington DC","Florida","Georgia", "Guam","Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas","Kentucky",
                "Louisiana","Maine","Maryland","Massachusetts", "Michigan","Minnesota","Mississippi","Missouri","Montana","Nebraska","Nevada",
                "New Hampshire","New Jersey","New Mexico", "New York","North Carolina","North Dakota","Northern Marina Island","Ohio","Oklahoma",
                "Oregon","Pennsylvania","Puerto Rico", "Rhode Island","South Carolina","South Dakota", "Tennessee","Texas","Utah","Vermont",
                "Virgin Islands","Virgina","Washington","West Virginia","Wisconsin","Wyoming"
        };

        final List<String> stateList = new ArrayList<>(Arrays.asList(states));
        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,stateList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                    if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES) {
                        tv.setBackgroundColor(Color.BLACK);
                    }
                    else   {
                        tv.setBackgroundColor(Color.WHITE);
                    }
                    //
                }
                else {
                    if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES) {
                        tv.setTextColor(Color.WHITE);
                        tv.setBackgroundColor(Color.BLACK);
                    }
                    else {
                        tv.setTextColor(Color.BLACK);
                        tv.setBackgroundColor(Color.WHITE);
                    }
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                    DownloadFilesTaskPAST downloadFilesTaskPAST = new DownloadFilesTaskPAST();
                    downloadFilesTaskPAST.execute();
                    DownloadFilesTask downloadFilesTask = new DownloadFilesTask();
                    downloadFilesTask.execute();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText
                        (getApplicationContext(), "Please Select State", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }



    @Override
    protected void onPause(){
        super.onPause();
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        darkstatus = AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES;
        preferencesEditor.putBoolean(DARKSTATUS, darkstatus);
        preferencesEditor.apply();
    }

    public void restartApp(){
        Intent iii = new Intent(getApplicationContext(), MainActivity.class);
        overridePendingTransition(0, 0);
        startActivity(iii);
        overridePendingTransition(0, 0);
        finish();
    }

    private boolean checkPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }
    private void requestPermissions(){
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
        }
    }
    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }
    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    displayState(location.getLongitude(), location.getLatitude());
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            displayState(mLastLocation.getLongitude(), mLastLocation.getLatitude());
        }
    };

    public void displayState(double longitude, double latitude){
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String state = addresses.get(0).getAdminArea();
        stateTextView.setText(state + "");
        DownloadFilesTaskPAST downloadFilesTaskPAST = new DownloadFilesTaskPAST();
        downloadFilesTaskPAST.execute();
        DownloadFilesTask downloadFilesTask = new DownloadFilesTask();
        downloadFilesTask.execute();
    }

    public String WorkingLink(int currentorpast) {
        String linkbare = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports_us/";
        String csv = ".csv";

        String yesterday = GetYesterdayDate();
        String today = GetTodayDate();
        String twodaydate = GetTwoDaysDate();

        String Initialtest = linkbare + today + csv;
        String yesterdaylink = linkbare + yesterday + csv;
        String twodaydatelink = linkbare + twodaydate + csv;

        String correctdate = ResponseCode(Initialtest);
        String pastdatalink = null;

        if (currentorpast == 2) {
            if (correctdate == Initialtest){
                pastdatalink = yesterday;
                return pastdatalink;
            }
            else {
                pastdatalink = twodaydatelink;
                return pastdatalink;
            }
        }
        else {
            String correctlink = linkbare + correctdate + csv;
            return correctlink;
        }
    }

    public String GetTodayDate() {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String today = dateFormat.format(cal.getTime());
        return today.toString();
    }

    public String GetYesterdayDate()
    {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        cal.add(Calendar.DATE, -1);
        String yesterday = dateFormat.format(cal.getTime());
        return yesterday.toString();
    }

    public String GetTwoDaysDate()
    {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        cal.add(Calendar.DATE, -2);
        String yesterday = dateFormat.format(cal.getTime());
        return yesterday.toString();
    }

    public String ResponseCode(String testurl) {
        URL url = null;
        int responseCode = -1;
        try {
            url = new URL(testurl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            responseCode = huc.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (responseCode == 404) {
            return GetYesterdayDate();
        }
        else {
            return GetTodayDate();
        }
    }
    public int openRow() {
        String statecurrent = (String) stateTextView.getText();
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String selectedItemText = (String) spinner.getSelectedItem();
//      EditText editText2 = (EditText) findViewById(R.id.edittext2);
        String state = (selectedItemText.toLowerCase().trim());
        if (selectedItemText == "Select a State" && statecurrent !="State"){
            state = (statecurrent.toLowerCase().trim());
        }

        int number2 = figurestate(state);
        //int number2 = Integer.parseInt(editText2.getText().toString());

        return number2;
    }

    private int figurestate(String state)
    {
        int statenumber=0;
        if (state.equals("")){statenumber=0;}
        if (state.equals("alabama")){statenumber=1;}
        if (state.equals("alaska")){statenumber=2;}
        if (state.equals("american samoa")){statenumber=3;}
        if (state.equals("arizona")){statenumber=4;}
        if (state.equals("arkansas")){statenumber=5;}
        if (state.equals("california")){statenumber=6;}
        if (state.equals("colorado")){statenumber=7;}
        if (state.equals("connecticut")){statenumber=8;}
        if (state.equals("delaware")){statenumber=9;}
        if (state.equals("usa")){statenumber=10;}
        if (state.equals("district of colombia")){statenumber=11;}
        if (state.equals("dc")){statenumber=11;}
        if (state.equals("washington dc")){statenumber=11;}
        if (state.equals("florida")){statenumber=12;}
        if (state.equals("georgia")){statenumber=13;}
        if (state.equals("grand princess")){statenumber=14;}
        if (state.equals("guam")){statenumber=15;}
        if (state.equals("hawaii")){statenumber=16;}
        if (state.equals("idaho")){statenumber=17;}
        if (state.equals("illinois")){statenumber=18;}
        if (state.equals("indiana")){statenumber=19;}
        if (state.equals("iowa")){statenumber=20;}
        if (state.equals("kansas")){statenumber=21;}
        if (state.equals("kentucky")){statenumber=22;}
        if (state.equals("louisiana")){statenumber=23;}
        if (state.equals("maine")){statenumber=24;}
        if (state.equals("maryland")){statenumber=25;}
        if (state.equals("massachusetts")){statenumber=26;}
        if (state.equals("michigan")){statenumber=27;}
        if (state.equals("minnesota")){statenumber=28;}
        if (state.equals("mississippi")){statenumber=29;}
        if (state.equals("missouri")){statenumber=30;}
        if (state.equals("montana")){statenumber=31;}
        if (state.equals("nebraska")){statenumber=32;}
        if (state.equals("nevada")){statenumber=33;}
        if (state.equals("new hampshire")){statenumber=34;}
        if (state.equals("new jersey")){statenumber=35;}
        if (state.equals("new mexico")){statenumber=36;}
        if (state.equals("new york")){statenumber=37;}
        if (state.equals("north carolina")){statenumber=38;}
        if (state.equals("north dakota")){statenumber=39;}
        if (state.equals("northern marina island")){statenumber=40;}
        if (state.equals("ohio")){statenumber=41;}
        if (state.equals("oklahoma")){statenumber=42;}
        if (state.equals("oregon")){statenumber=43;}
        if (state.equals("pennsylvania")){statenumber=44;}
        if (state.equals("puerto rico")){statenumber=45;}
        if (state.equals("rhode island")){statenumber=46;}
        if (state.equals("south carolina")){statenumber=47;}
        if (state.equals("south dakota")){statenumber=48;}
        if (state.equals("tennessee")){statenumber=49;}
        if (state.equals("texas")){statenumber=50;}
        if (state.equals("utah")){statenumber=51;}
        if (state.equals("vermont")){statenumber=52;}
        if (state.equals("virgin islands")){statenumber=53;}
        if (state.equals("virgina")){statenumber=54;}
        if (state.equals("washington")){statenumber=55;}
        if (state.equals("west virginia")){statenumber=56;}
        if (state.equals("wisconsin")){statenumber=57;}
        if (state.equals("wyoming")){statenumber=58;}
        return statenumber;
    }
    private class DownloadFilesTask extends AsyncTask<URL, Void, List<String[]>> {
        protected List<String[]> doInBackground(URL... urls) {
            return downloadRemoteTextFileContent();
        }
        protected void onPostExecute(List<String[]> result) {
            if(result != null){
                //Getting Country Totals
                int totalconfirmed = 0;
                int totaldeaths = 0;
                int totalrecovered = 0;
                int totalactive = 0;
                int totaltested = 0;
                int totalhospitalized = 0;
                for (int i = 1; i<=58; i++)
                {
                    totaldeaths = totaldeaths + Integer.parseInt(result.get(i)[6]);
                    totalconfirmed = totalconfirmed + Integer.parseInt(result.get(i)[5]);
                    if ("".equals(result.get(i)[7])!=true){
                    totalrecovered = totalrecovered + Integer.parseInt(result.get(i)[7]);}
                    String activesplit[] = result.get(i)[8].split("\\r?\\.");
                    int activeint = Integer.parseInt(activesplit[0]);
                    totalactive = totalactive + activeint;
                    if("".equals(result.get(i)[11])!=true){
                    totaltested = totaltested + Integer.parseInt(result.get(i)[11]);}
                    if("".equals(result.get(i)[12])!=true){
                    totalhospitalized = totalhospitalized + Integer.parseInt(result.get(i)[12]);}
                }

                result.get(10)[0] = "USA";
                result.get(10)[5] = Integer.toString(totalconfirmed);
                result.get(10)[6] = Integer.toString(totaldeaths);
                result.get(10)[7] = Integer.toString(totalrecovered);
                result.get(10)[8] = Integer.toString(totalactive);
                result.get(10)[11] = Integer.toString(totaltested);
                result.get(10)[12] = Integer.toString(totalhospitalized);
                String senddisplay = result.get(openRow())[0] + "," + result.get(openRow())[3]+ "," + result.get(openRow())[4]+ "," + result.get(openRow())[5]+ "," + result.get(openRow())[6] + "," + result.get(openRow())[7]+ ","
                 + result.get(openRow())[8]+ "," + result.get(openRow())[11]+ "," + result.get(openRow())[12]+ "," + result.get(openRow())[13]+ "," + result.get(openRow())[14]+ "," + result.get(openRow())[15];
                String datafilter[] = senddisplay.split("\\r?\\,");
                String name = datafilter[0];
                String lat = datafilter[1];
                String longi = datafilter[2];
                String confirmed = datafilter[3];
                if (confirmed.equals("")){confirmed = "No value provided";}
                String deaths = datafilter[4];
                String recovered = datafilter[5];
                if (recovered.equals("")){recovered = "No value provided";}
                String active = datafilter[6];
                if (active.equals("")){active = "No value provided";}
                String amounttested = datafilter[7];
                if (amounttested.equals("")){amounttested = "No value provided";}
                String amounthospitalized = datafilter[8];
                if (amounthospitalized.equals("")){amounthospitalized = "No value provided";}
                Intent intent = new Intent(getApplicationContext(), DataDisplayOmegalul.class);
                intent.putExtra(NAME, name);
                intent.putExtra(LAT,lat);
                intent.putExtra(LONGI,longi);
                intent.putExtra(CONFIRMED,confirmed);
                intent.putExtra(DEATHS,deaths);
                intent.putExtra(AMOUNTTESTED,amounttested);
                intent.putExtra(AMOUNTHOSPITALIZED,amounthospitalized);
                intent.putExtra(RECOVERED,recovered);
                intent.putExtra(ACTIVE,active);
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                spinner.setSelection(0);
                startActivity(intent);
            }
        }
    }

    private List<String[]> downloadRemoteTextFileContent(){
        URL mUrl = null;
        List<String[]> csvLine = new ArrayList<>();
        String[] content = null;
        try {
            mUrl = new URL(WorkingLink(1));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            assert mUrl != null;
            URLConnection connection = mUrl.openConnection();
            BufferedReader br = new BufferedReader(new
                    InputStreamReader(connection.getInputStream()));
            String line = "";
            while((line = br.readLine()) != null){
                content = line.split(",");
                csvLine.add(content);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvLine;
    }



    //
    //
    //
    //
    private class DownloadFilesTaskPAST extends AsyncTask<URL, Void, List<String[]>> {
        protected List<String[]> doInBackground(URL... urls) {
            return downloadRemoteTextFileContentPAST();
        }
        protected void onPostExecute(List<String[]> result) {
            if(result != null){
                //Getting Country Totals
                int totalconfirmed = 0;
                int totaldeaths = 0;
                int totalrecovered = 0;
                int totalactive = 0;
                int totaltested = 0;
                int totalhospitalized = 0;
                for (int i = 1; i<=58; i++)
                {
                    totaldeaths = totaldeaths + Integer.parseInt(result.get(i)[6]);
                    totalconfirmed = totalconfirmed + Integer.parseInt(result.get(i)[5]);
                    if ("".equals(result.get(i)[7])!=true){
                        totalrecovered = totalrecovered + Integer.parseInt(result.get(i)[7]);}
                    String activesplit[] = result.get(i)[8].split("\\r?\\.");
                    int activeint = Integer.parseInt(activesplit[0]);
                    totalactive = totalactive + activeint;
                    if("".equals(result.get(i)[11])!=true){
                        totaltested = totaltested + Integer.parseInt(result.get(i)[11]);}
                    if("".equals(result.get(i)[12])!=true){
                        totalhospitalized = totalhospitalized + Integer.parseInt(result.get(i)[12]);}
                }

                result.get(10)[0] = "USA";
                result.get(10)[5] = Integer.toString(totalconfirmed);
                result.get(10)[6] = Integer.toString(totaldeaths);
                result.get(10)[7] = Integer.toString(totalrecovered);
                result.get(10)[8] = Integer.toString(totalactive);
                result.get(10)[11] = Integer.toString(totaltested);
                result.get(10)[12] = Integer.toString(totalhospitalized);
                String senddisplay = result.get(openRow())[0] + "," + result.get(openRow())[3]+ "," + result.get(openRow())[4]+ "," + result.get(openRow())[5]+ "," + result.get(openRow())[6] + "," + result.get(openRow())[7]+ ","
                        + result.get(openRow())[8]+ "," + result.get(openRow())[11]+ "," + result.get(openRow())[12]+ "," + result.get(openRow())[13]+ "," + result.get(openRow())[14]+ "," + result.get(openRow())[15];
                String datafilter[] = senddisplay.split("\\r?\\,");
                String name = datafilter[0];
                String lat = datafilter[1];
                String longi = datafilter[2];
                String confirmed = datafilter[3];
                if (confirmed.equals("")){confirmed = "No value provided";}
                String deaths = datafilter[4];
                String recovered = datafilter[5];
                if (recovered.equals("")){recovered = "No value provided";}
                String active = datafilter[6];
                if (active.equals("")){active = "No value provided";}
                String amounttested = datafilter[7];
                if (amounttested.equals("")){amounttested = "No value provided";}
                String amounthospitalized = datafilter[8];
                if (amounthospitalized.equals("")){amounthospitalized = "No value provided";}
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(NAMEPAST, name);
                editor.putString(LATPAST,lat);
                editor.putString(LONGIPAST,longi);
                editor.putString(CONFIRMEDPAST,confirmed);
                editor.putString(DEATHSPAST,deaths);
                editor.putString(AMOUNTTESTEDPAST,amounttested);
                editor.putString(AMOUNTHOSPITALIZEDPAST,amounthospitalized);
                editor.putString(RECOVEREDPAST,recovered);
                editor.putString(ACTIVEPAST,active);
                editor.commit();

            }
        }
    }





    private List<String[]> downloadRemoteTextFileContentPAST(){
        URL mUrl = null;
        List<String[]> csvLine = new ArrayList<>();
        String[] content = null;
        try {
            mUrl = new URL(WorkingLink(2));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            assert mUrl != null;
            URLConnection connection = mUrl.openConnection();
            BufferedReader br = new BufferedReader(new
                    InputStreamReader(connection.getInputStream()));
            String line = "";
            while((line = br.readLine()) != null){
                content = line.split(",");
                csvLine.add(content);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvLine;
    }

//
    //
    //
    //
    //

    private class DownloadFilesTaskUSA extends AsyncTask<URL, Void, List<String[]>> {
        protected List<String[]> doInBackground(URL... urls) {
            return downloadRemoteTextFileContentUSA();
        }
        protected void onPostExecute(List<String[]> result) {
            if(result != null){
                //Getting Country Totals
                int totalconfirmed = 0;
                int totaldeaths = 0;
                int totalrecovered = 0;
                int totalactive = 0;
                int totaltested = 0;
                int totalhospitalized = 0;
                for (int i = 1; i<=58; i++)
                {
                    totaldeaths = totaldeaths + Integer.parseInt(result.get(i)[6]);
                    totalconfirmed = totalconfirmed + Integer.parseInt(result.get(i)[5]);
                    if ("".equals(result.get(i)[7])!=true){
                        totalrecovered = totalrecovered + Integer.parseInt(result.get(i)[7]);}
                    String activesplit[] = result.get(i)[8].split("\\r?\\.");
                    int activeint = Integer.parseInt(activesplit[0]);
                    totalactive = totalactive + activeint;
                    if("".equals(result.get(i)[11])!=true){
                        totaltested = totaltested + Integer.parseInt(result.get(i)[11]);}
                    if("".equals(result.get(i)[12])!=true){
                        totalhospitalized = totalhospitalized + Integer.parseInt(result.get(i)[12]);}
                }


                String confirmedintstring = NumberFormat.getNumberInstance(Locale.US).format(totalconfirmed);

                //PIECHART STUFF
                PieChart pieChart;
                pieChart = (PieChart)findViewById(R.id.statepie);
                pieChart.setUsePercentValues(false);
                pieChart.getDescription().setEnabled(false);
                pieChart.setExtraOffsets(5, 10, 5, 5);
                pieChart.setDrawHoleEnabled(true);
                pieChart.setHoleColor(Color.WHITE);
                pieChart.setTransparentCircleRadius(55f);



                ArrayList<PieEntry> yValues = new ArrayList<>();
                yValues.add(new PieEntry(totaldeaths, "Deaths"));
                yValues.add(new PieEntry(totalactive, "Alive Cases"));
                yValues.add(new PieEntry(totalrecovered, "Minimum Recovered"));
                pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
                PieDataSet dataSet = new PieDataSet(yValues, "");
                dataSet.setSliceSpace(3f);
                dataSet.setSelectionShift(5f);
                final int[] MY_COLORS = {Color.RED, Color.rgb(255,165,0), Color.GREEN};
                ArrayList<Integer> colors = new ArrayList<Integer>();
                for(int c: MY_COLORS) colors.add(c);
                dataSet.setColors(colors);
                pieChart.setCenterText("Confirmed Cases: "+confirmedintstring);
                pieChart.setCenterTextSize(16f);
                PieData data = new PieData(dataSet);
                data.setValueTextSize(15f);
                data.setValueTextColor(Color.BLACK);
                pieChart.setData(data);
                Legend piel = pieChart.getLegend();
                pieChart.setDragDecelerationFrictionCoef(.1f);
                piel.setEnabled(false);
                pieChart.setRotationAngle(340f);
                pieChart.setTouchEnabled(false);


                //TextView USATitle = (TextView)findViewById(R.id.USA);


                if (darkstatus) {
                    pieChart.setHoleColor(Color.rgb(48, 48, 48));
                    pieChart.setCenterTextColor(Color.WHITE);
                    pieChart.setEntryLabelColor(Color.WHITE);
                 //   USATitle.setTextColor(Color.WHITE);
                }
                else {
                    pieChart.setHoleColor(Color.WHITE);
                    pieChart.setCenterTextColor(Color.BLACK);
                    pieChart.setEntryLabelColor(Color.BLACK);
                 //   USATitle.setTextColor(Color.BLACK);
                }
            }
        }
    }





    private List<String[]> downloadRemoteTextFileContentUSA(){
        URL mUrl = null;
        List<String[]> csvLine = new ArrayList<>();
        String[] content = null;
        try {
            mUrl = new URL(WorkingLink(1));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            assert mUrl != null;
            URLConnection connection = mUrl.openConnection();
            BufferedReader br = new BufferedReader(new
                    InputStreamReader(connection.getInputStream()));
            String line = "";
            while((line = br.readLine()) != null){
                content = line.split(",");
                csvLine.add(content);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvLine;
    }

}

