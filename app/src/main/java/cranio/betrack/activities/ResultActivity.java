package cranio.betrack.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;

import cranio.betrack.R;
import cranio.betrack.pojos.BarrelInformationPojo;
import cranio.betrack.utils.AppPreferences;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    private Button emptyFillBtn;
    private String jsonStatus;
    private int id;
    private String jsonData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        emptyFillBtn = (Button) findViewById(R.id.emptyFillBtn);
        TextView showMoreBeerData =(TextView) findViewById(R.id.beerMoreData);
        TextView showMoreOwnerData = (TextView) findViewById(R.id.ownerMoreData);
        if (getIntent().getExtras() != null) {
            showAssets(getIntent().getBooleanExtra("Found", false));
            id = getIntent().getIntExtra("BarrelId", 1);
            jsonData = getIntent().getStringExtra("BarrelData");

        }

        showBarrelData();
        assert emptyFillBtn != null;
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/betrackfont.ttf");
        emptyFillBtn.setTypeface(typeface);
        if (AppPreferences.instance(getApplication()).getUsername().equals("Bar")) {
            emptyFillBtn.setText("Vaciar Barril");
            jsonStatus = "{barrel_status:{state:'empty'}}";
        } else {
            emptyFillBtn.setText("Llenar Barril");
            jsonStatus = "{barrel_status:{state:'full'}}";
        }

        emptyFillBtn.setOnClickListener(this);
        assert showMoreBeerData != null;
        showMoreBeerData.setOnClickListener(this);
        assert showMoreOwnerData != null;
        showMoreOwnerData.setOnClickListener(this);


    }

    private void showBarrelData() {
        Gson gson = new Gson();

        BarrelInformationPojo barrelInformationPojo = gson.fromJson(jsonData, BarrelInformationPojo.class);

        TextView beertype = (TextView) findViewById(R.id.beerType);
        assert beertype != null;
        beertype.setText(barrelInformationPojo.getBarreldata().getType());

        TextView beerTemperature = (TextView) findViewById(R.id.beerTemperature);
        assert beerTemperature != null;
        if (barrelInformationPojo.getBarrelStatus().size() > 0) {
            beerTemperature.setText(barrelInformationPojo.getBarrelStatus().get(barrelInformationPojo.getBarrelStatus().size()).getTemperature() + "º");
        }

        TextView temperatureUpdate = (TextView) findViewById(R.id.lastUpdate);
        assert temperatureUpdate != null;
        if (barrelInformationPojo.getBarrelStatus().size() > 0) {
        }

        TextView ownerName = (TextView) findViewById(R.id.ownerName);
        assert ownerName != null;
        ownerName.setText(barrelInformationPojo.getOwner().getOwnerData().getName());

        TextView beerSize = (TextView) findViewById(R.id.beerSize);
        assert beerSize != null;
        beerSize.setText(barrelInformationPojo.getBarreldata().getContent()+"lts");

        TextView barrelNumber = (TextView) findViewById(R.id.barrelnumber);
        assert barrelNumber != null;
        barrelNumber.setText("Barril: "+barrelInformationPojo.getBarreldata().getNumber());


    }


    private void postNewStatus() {
        try {

            JSONObject obj = new JSONObject(jsonStatus);

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            String url = "http://betrack.herokuapp.com/barrels/" + id + "/barrel_statuses.json";
            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(JSON, obj.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.code() >= 400 && response.code() <= 500) {
                        Toast.makeText(ResultActivity.this,"No se pudo realizar la acción. Inténtelo nuevamente.",Toast.LENGTH_LONG).show();
                        Log.e("Response code: ", response.code() + " " + response.body().toString());
                        throw new IOException("Unexpected code " + response);

                    } else {
                        Toast.makeText(ResultActivity.this,"Acción realizada exitosamente",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(ResultActivity.this, NfcActivity.class);
                        startActivity(i);
                        Log.e("Response code: ", response.code() + " " + response.body().toString());

                    }
                }
            });

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + jsonStatus + "\"");
        }
    }

    private void showAssets(boolean found) {
        TextView weAreSorry = (TextView) findViewById(R.id.weAreSorryTV);
        TextView noDataFound = (TextView) findViewById(R.id.noDataAvailableTV);
        ImageView noFoundPic = (ImageView) findViewById(R.id.noFoundPic);

        assert weAreSorry != null;
        weAreSorry.setVisibility(found ? View.INVISIBLE : View.VISIBLE);
        assert noDataFound != null;
        noDataFound.setVisibility(found ? View.INVISIBLE : View.VISIBLE);
        assert noFoundPic != null;
        noFoundPic.setVisibility(found ? View.INVISIBLE : View.VISIBLE);
        emptyFillBtn.setVisibility(found ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.emptyFillBtn:
                postNewStatus();
                break;
            case R.id.beerMoreData:
                Intent i = new Intent(ResultActivity.this,BeerMoreDataActivity.class);
                startActivity(i);
                break;
            case R.id.ownerMoreData:
                Intent in = new Intent(ResultActivity.this,OwnerMoreData.class);
                startActivity(in);
                break;
        }

    }
}
