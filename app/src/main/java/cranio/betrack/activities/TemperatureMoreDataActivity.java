package cranio.betrack.activities;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cranio.betrack.R;
import cranio.betrack.pojos.BarrelInformationPojo;
import cranio.betrack.pojos.BarrelStatusPojo;
import cranio.betrack.utils.AppPreferences;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TemperatureMoreDataActivity extends AppCompatActivity {

    private String jsonData;
    private BarrelInformationPojo barrelInformationPojo;
    private ArrayList<String> sentAtValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature_more_data);


        jsonData = AppPreferences.instance(getApplication()).getBarrelInfo();

        Gson gson = new Gson();
        barrelInformationPojo = gson.fromJson(jsonData, BarrelInformationPojo.class);

        LineChartView chart = (LineChartView) findViewById(R.id.chart);
        assert chart != null;
        chart.setInteractive(true);

        List<Float> temperatureValues = new ArrayList<>();
        sentAtValues = new ArrayList<>();

        for (BarrelStatusPojo status : barrelInformationPojo.getBarrelStatus()
                ) {
            temperatureValues.add(status.getTemperature());
            String parts[]=status.getSent_at().split("T");
            sentAtValues.add(parts[0]);
        }

        List<Float> axisValues = new ArrayList<>();
        for (int i = 0; i < sentAtValues.size(); i++) {
            axisValues.add((float) i);
        }

        List<PointValue> values = new ArrayList<PointValue>();

        for (int i = 0; i < temperatureValues.size(); i++) {
            values.add(new PointValue(axisValues.get(i), temperatureValues.get(i)));
        }

        //In most cased you can call data model methods in builder-pattern-like manner.
        Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        data.setAxisXBottom(Axis.generateAxisFromCollection(axisValues, sentAtValues));
        data.setAxisYLeft(Axis.generateAxisFromCollection(temperatureValues));

        chart.setLineChartData(data);

        showTemperatureData();

    }

    private void showTemperatureData() {
        TextView beerType = (TextView) findViewById(R.id.beerTypeTemperatureMoreData);
        assert beerType != null;
        beerType.setText(barrelInformationPojo.getBarreldata().getType());

        TextView beerTemperature = (TextView) findViewById(R.id.beerTemperatureMoreData);
        assert beerTemperature != null;
        Float temperature = barrelInformationPojo.getBarreldata().getLast_temperature();
        beerTemperature.setText(temperature+"º");
        ImageView temperatureCircle = (ImageView)findViewById(R.id.temperatureCircle);
        if(temperature<=5){
            assert temperatureCircle != null;
            temperatureCircle.setImageResource(R.drawable.ic_temperature_green);
        }else if(temperature>5&&temperature<12){
            assert temperatureCircle != null;
            temperatureCircle.setImageResource(R.drawable.ic_temperature_yellow);
        }else{
            assert temperatureCircle != null;
            temperatureCircle.setImageResource(R.drawable.ic_temperature_red);
        }


        TextView lot = (TextView) findViewById(R.id.lotNumberTemperatureData);
        assert lot != null;
        lot.setText("Lote: "+barrelInformationPojo.getBarreldata().getLot());

        TextView barrelNumber = (TextView) findViewById(R.id.barrelnumberTemperatureData);
        assert barrelNumber != null;
        barrelNumber.setText("Barril: "+barrelInformationPojo.getBarreldata().getNumber() );

        TextView temperatureLastUpdate = (TextView) findViewById(R.id.lastUpdateTemperatureMoreData);
        assert temperatureLastUpdate != null;
        temperatureLastUpdate.setText(sentAtValues.get(sentAtValues.size()-1));


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
