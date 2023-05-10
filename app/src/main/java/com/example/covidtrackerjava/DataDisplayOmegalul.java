package com.example.covidtrackerjava;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import static com.example.covidtrackerjava.MainActivity.ACTIVEPAST;
import static com.example.covidtrackerjava.MainActivity.AMOUNTHOSPITALIZEDPAST;
import static com.example.covidtrackerjava.MainActivity.AMOUNTTESTEDPAST;
import static com.example.covidtrackerjava.MainActivity.CONFIRMEDPAST;
import static com.example.covidtrackerjava.MainActivity.DARKON;
import static com.example.covidtrackerjava.MainActivity.DARKSTATUS;
import static com.example.covidtrackerjava.MainActivity.DEATHSPAST;
import static com.example.covidtrackerjava.MainActivity.RECOVEREDPAST;



public class DataDisplayOmegalul extends AppCompatActivity {
    public Boolean darkon;
    public Boolean darkstatus;

    public SharedPreferences mPreferences;
    public String sharedPrefFile =
            "com.example.android.hellosharedprefs";

    private TextView namesdiplay;
    PieChart pieChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        SharedPreferences dark = PreferenceManager.getDefaultSharedPreferences(this);
        darkon = dark.getBoolean(DARKON, true);

        System.out.println("DATA " + darkon);

        if(darkon) {
            setTheme(R.style.darktheme);

        }
        else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.datadisplayomegalul);
        namesdiplay = (TextView) findViewById(R.id.namesdisplay);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        ImageButton backbutton = (ImageButton)findViewById(R.id.back);
        //

        //
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });






        Intent intent = getIntent();
        String name = intent.getStringExtra(MainActivity.NAME);
        if(name.equals("Province_State")){ name = "Invalid Name, Go Back";}

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String confirmedPAST = prefs.getString(CONFIRMEDPAST, "No value provided"); //no id: default value
        String deathsPAST = prefs.getString(DEATHSPAST, "No value provided");
        String amounttestedPAST = prefs.getString(AMOUNTTESTEDPAST, "No value provided");
        String amounthospitalizedPAST = prefs.getString(AMOUNTHOSPITALIZEDPAST, "No value provided");
        String recoveredPAST = prefs.getString(RECOVEREDPAST, "No value provided");
        String activePAST = prefs.getString(ACTIVEPAST, "No value provided");

        int confirmedp = Integer.parseInt(confirmedPAST);
        int deathsp = Integer.parseInt(deathsPAST);


        String lat = intent.getStringExtra(MainActivity.LAT);
        String longi = intent.getStringExtra(MainActivity.LONGI);


        String confirmed = intent.getStringExtra(MainActivity.CONFIRMED);
        if(confirmed.equals("No value provided")){confirmed="0";}
        int confirmedint = Integer.parseInt(confirmed);
        String confirmedintstring = NumberFormat.getNumberInstance(Locale.US).format(confirmedint);


        String deaths = intent.getStringExtra(MainActivity.DEATHS);
        int deathsint = Integer.parseInt(deaths);

        String amounttested = intent.getStringExtra(MainActivity.AMOUNTTESTED);
        int amounttestedint = 0;
        if(amounttested.equals("No value provided")){amounttested="0";}
        amounttestedint = Integer.parseInt(amounttested);

        String amounthospitalized = intent.getStringExtra(MainActivity.AMOUNTHOSPITALIZED);
        if (amounthospitalized.equals("No value provided")) {amounthospitalized="0"; }
        int amounthospitalizedint = Integer.parseInt(amounthospitalized);

        String recovered = intent.getStringExtra(MainActivity.RECOVERED);
        if (recovered.equals("No value provided")){recovered = "0";}
        int recoveredint = Integer.parseInt(recovered);

        String active = intent.getStringExtra(MainActivity.ACTIVE);
        int activeint = 0;
        if(active.equals("No value provided")!=true){String activesplit[] = active.split("\\r?\\.");
            activeint = Integer.parseInt(activesplit[0]);}


        namesdiplay.setText(name);

        //PIECHART STUFF
        pieChart = (PieChart)findViewById(R.id.statepie);
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(55f);
        pieChart.setRotationAngle(340f);
        pieChart.setTouchEnabled(false);

        ArrayList<PieEntry> yValues = new ArrayList<>();

        yValues.add(new PieEntry(deathsint, "Deaths"));
        yValues.add(new PieEntry(activeint, "Alive Cases"));
        if(name.equals("USA"))
        {
            yValues.add(new PieEntry(recoveredint, "Minimum Recovered"));
        }
        else{
            if(recoveredint!=0) {
                yValues.add(new PieEntry(recoveredint, "Recovered Cases"));
            }
        }

        if (recoveredint==0){
            Description description = new Description();
            description.setText("No recovery data available.");
            description.setTextSize(15f);
            description.setTextAlign(Paint.Align.RIGHT);
            pieChart.setDescription(description);
            if (darkon){
                description.setTextColor(Color.WHITE);
            }
            else {
                description.setTextColor(Color.BLACK);
            }
        }


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



        HorizontalBarChart barChart;
        barChart= (HorizontalBarChart) findViewById(R.id.statebar);
        ArrayList<BarEntry> bVals = new ArrayList<>();
        float spaceForBar = 5f;
        float floatconverter = 1000;
        if(amounttestedint>900000){
            float amounttestedfloat = amounttestedint/(floatconverter*floatconverter);
            float amountconfirmedfloat = confirmedint/(floatconverter*floatconverter);
            float amounthospitalizedfloat = amounthospitalizedint/(floatconverter*floatconverter);
            bVals.add(new BarEntry(0,new float[]{amounthospitalizedfloat,amountconfirmedfloat,amounttestedfloat}));}
        else{
            float amounttestedfloat = amounttestedint/(floatconverter);
            float amountconfirmedfloat = confirmedint/(floatconverter);
            float amounthospitalizedfloat = amounthospitalizedint/(floatconverter);
            bVals.add(new BarEntry(0,new float[]{amounthospitalizedfloat,amountconfirmedfloat,amounttestedfloat}));}
        //bVals.add(new BarEntry(1*spaceForBar, amounttestedint));
        // bVals.add(new BarEntry(2*spaceForBar, confirmedint));
        //if(amounthospitalizedint>0){bVals.add(new BarEntry(3*spaceForBar, amounthospitalizedint));}
        BarDataSet set1;
        set1 = new BarDataSet(bVals, "");
        final int[] colorClassArrayy = {Color.RED,Color.rgb(255,165,0),Color.YELLOW};
        ArrayList<Integer> colorClassArray = new ArrayList<>();
        BarData dataa = new BarData(set1);
        barChart.setData(dataa);
        for(int d: colorClassArrayy) colorClassArray.add(d);
        set1.setColors(colorClassArray);
        //barChart.getAxisLeft().setEnabled(false);

        barChart.getAxisLeft().setTextSize(14f);
        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setTextSize(15f);
        dataa.setBarWidth(.5f);
        set1.setBarBorderWidth(.5f);
        barChart.getXAxis().setDrawLabels(false);
        Legend barL = barChart.getLegend();
        LegendEntry legendea = new LegendEntry();
        legendea.label = "Hospitalized";
        legendea.formColor=Color.RED;
        LegendEntry legendeb = new LegendEntry();
        legendeb.label = "Confirmed";
        legendeb.formColor=Color.rgb(255,165,0);
        LegendEntry legendec = new LegendEntry();
        legendec.label= "Tested";
        legendec.formColor=Color.YELLOW;
        barL.setCustom(Arrays.asList(legendea, legendeb,legendec));
        set1.setValueTextSize(0f);
        barChart.animateX(10000, Easing.EasingOption.EaseInOutCubic);
        barChart.setScaleEnabled(false);
//


        if(amounttestedint>900000){
            Description des = barChart.getDescription();
            des.setText("Amount in millions.");
            des.setTextSize(18f); }

        if(amounttestedint>900000 && amounthospitalizedint==0){
            Description des = barChart.getDescription();
            des.setText("Amount in millions. No hospitalization data.");
            des.setTextSize(15f); }

        if(amounttestedint<900000){
            Description des = barChart.getDescription();
            des.setText("Amount in thousands.");
            des.setTextSize(18f); }
        if(amounttestedint<900000 && amounthospitalizedint==0){
            Description des = barChart.getDescription();
            des.setText("Amount in thousands. No hospitalization data.");
            des.setTextSize(15f); }


        if (darkon) {
            Description color = barChart.getDescription();
            color.setTextColor(Color.WHITE);
            barChart.getAxisLeft().setTextColor(Color.WHITE);
            barChart.getLegend().setTextColor(Color.WHITE);
            pieChart.setHoleColor(Color.rgb(48, 48, 48));
            pieChart.setCenterTextColor(Color.WHITE);
            backbutton.setColorFilter(Color.WHITE);
            pieChart.setEntryLabelColor(Color.WHITE);
        }
        else {
            Description color = barChart.getDescription();
            color.setTextColor(Color.BLACK);
            barChart.getAxisLeft().setTextColor(Color.BLACK);
            barChart.getLegend().setTextColor(Color.BLACK);
            pieChart.setHoleColor(Color.WHITE);
            pieChart.setCenterTextColor(Color.BLACK);
            backbutton.setColorFilter(Color.BLACK);
            pieChart.setEntryLabelColor(Color.WHITE);
        }

        TextView comparison1, comparison2;
        comparison1 = (TextView) findViewById(R.id.comparison1);
        comparison2 = (TextView) findViewById(R.id.comparison2);

        int todaycon = confirmedint - confirmedp;
        int todaydea = deathsint - deathsp;

        comparison1.setText(todaycon+" Confirmed");
        comparison1.setTextColor(Color.rgb(255,165,0));
        comparison2.setText(todaydea+ " Deaths");
        comparison2.setTextColor(Color.RED);
        barChart.setTouchEnabled(false);

    }
    @Override
    protected void onPause(){
        super.onPause();
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        darkstatus = darkon;
        preferencesEditor.putBoolean(DARKSTATUS, darkstatus);
        preferencesEditor.apply();
        System.out.println("sucess " + darkstatus);
    }
}