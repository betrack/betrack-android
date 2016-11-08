package cranio.betrack.activities;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.io.UnsupportedEncodingException;

import javax.xml.transform.Result;

import cranio.betrack.R;
import cranio.betrack.pojos.BarrelInformationPojo;
import cranio.betrack.utils.AppPreferences;
import cranio.betrack.utils.PuntuationDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    private Button emptyFillBtn;
    private String jsonStatus;
    private int id;
    private String jsonData;
    private TextView barrelStatus;
    private boolean found;
    private boolean update;
    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);


        emptyFillBtn = (Button) findViewById(R.id.emptyFillBtn);
        TextView showMoreBeerData = (TextView) findViewById(R.id.beerMoreData);
        TextView showMoreOwnerData = (TextView) findViewById(R.id.ownerMoreData);
        TextView showMoreTemperatureData = (TextView) findViewById(R.id.tempertureMoreData);
        if (getIntent().getExtras() != null) {
            id = getIntent().getIntExtra("BarrelId", 1);
            update = getIntent().getBooleanExtra("Update", false);
        }

        barrelStatus = (TextView) findViewById(R.id.barrelState);
        assert barrelStatus != null;

        if (update) {
            if (AppPreferences.instance(getApplication()).getUsername().equals("Bar")) {
                barrelStatus.setText("Estado Actual: Vacío");
            } else {
                barrelStatus.setText("Estado Actual: Lleno");
            }
        }

        found = AppPreferences.instance(getApplication()).getBarrelFound();
        showAssets(found);

        if (found) {
            jsonData = AppPreferences.instance(getApplication()).getBarrelInfo();
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
            assert showMoreTemperatureData != null;
            showMoreTemperatureData.setOnClickListener(this);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        enableForegroundDispatchSystem();
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableForegroundDispatchSystem();
    }

    private void enableForegroundDispatchSystem() {

        Intent intent = new Intent(this, ResultActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, null);
    }

    private void disableForegroundDispatchSystem() {
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            ResultActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    showTagReadImage(true);

                }
            });

            if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
                Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

                if (parcelables != null && parcelables.length > 0) {
                    final String id = readTextFromMessage((NdefMessage) parcelables[0]);
                    if (id != null) {

                        OkHttpClient client = new OkHttpClient();

                        String url = "http://betrack.herokuapp.com/barrels/" + id + ".json";

                        Request request = new Request.Builder()
                                .url(url)
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {
                                e.printStackTrace();

                            }

                            @Override
                            public void onResponse(Response response) throws IOException {
                                if (response.code() >= 400 && response.code() <= 500) {
                                    Intent i = new Intent(ResultActivity.this, ResultActivity.class);
                                    AppPreferences.instance(getApplication()).saveBarrelFound(false);
                                    startActivity(i);
                                    Log.e("Response code: ", response.code() + " " + response.body().toString());
                                    throw new IOException("Unexpected code " + response);

                                } else {
                                    String jsonData = response.body().string();
                                    AppPreferences.instance(getApplication()).saveBarrelInfo(jsonData);
                                    Intent i = new Intent(ResultActivity.this, ResultActivity.class);
                                    AppPreferences.instance(getApplication()).saveBarrelFound(true);
                                    i.putExtra("BarrelId", Integer.parseInt(id));
                                    startActivity(i);

                                }
                            }

                        });

                    }
                } else {
                    Toast.makeText(this, "No se encontró ningún dato. Inténtelo nuevamente", Toast.LENGTH_SHORT).show();
                    showTagReadImage(false);
                }


            }

        }
    }

    private void showTagReadImage(boolean show) {
        RelativeLayout resultLayout = (RelativeLayout) findViewById(R.id.resultActivityLayout);
        RelativeLayout searchingDataLayout = (RelativeLayout) findViewById(R.id.searchingDataLayout);

        assert searchingDataLayout != null;
        searchingDataLayout.setVisibility(show? View.VISIBLE: View.GONE);
        assert resultLayout != null;
        resultLayout.setVisibility(show? View.GONE: View.VISIBLE);

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
            beerTemperature.setText(barrelInformationPojo.getBarreldata().getLast_temperature() + "º");
        }

        Float temperature = barrelInformationPojo.getBarreldata().getLast_temperature();
        beerTemperature.setText(temperature + "º");
        ImageView temperatureCircle = (ImageView) findViewById(R.id.temperatureCircle);
        if (temperature <= 5) {
            assert temperatureCircle != null;
            temperatureCircle.setImageResource(R.drawable.ic_temperature_green);
        } else if (temperature > 5 && temperature < 12) {
            assert temperatureCircle != null;
            temperatureCircle.setImageResource(R.drawable.ic_temperature_yellow);
        } else {
            assert temperatureCircle != null;
            temperatureCircle.setImageResource(R.drawable.ic_temperature_red);
        }


        TextView temperatureUpdate = (TextView) findViewById(R.id.lastUpdate);
        assert temperatureUpdate != null;
        if (barrelInformationPojo.getBarrelStatus().size() > 0) {
            String lastUpdate = barrelInformationPojo.getBarrelStatus().get(barrelInformationPojo.getBarrelStatus().size() - 1).getSent_at();
            String parts[] = lastUpdate.split("T");
            temperatureUpdate.setText(parts[0]);
        }

        TextView ownerName = (TextView) findViewById(R.id.ownerName);
        assert ownerName != null;
        ownerName.setText(barrelInformationPojo.getOwner().getOwnerData().getName());

        TextView beerSize = (TextView) findViewById(R.id.beerSize);
        assert beerSize != null;
        beerSize.setText(barrelInformationPojo.getBarreldata().getContent() + "lts");

        TextView barrelNumber = (TextView) findViewById(R.id.barrelnumber);
        assert barrelNumber != null;
        barrelNumber.setText("Barril: " + barrelInformationPojo.getBarreldata().getNumber());

        if (barrelInformationPojo.getBarreldata().getLast_state() != null) {

            if (barrelInformationPojo.getBarreldata().getLast_state().equals("empty")) {
                barrelStatus.setText("Estado Actual: Vacio");
            } else {
                barrelStatus.setText("Estado Actual: Lleno");
            }
        }

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        navigateUpTo(getParentActivityIntent());
        super.onBackPressed();

    }

    private boolean postNewStatus() {
        final boolean[] succes = {false};

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
                    ResultActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ResultActivity.this, "No se pudo actualizar el estado del barril. Compruebe su conexión a Internet.", Toast.LENGTH_LONG).show();
                        }
                    });

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.code() >= 400 && response.code() <= 500) {
                        Log.e("Response code: ", response.code() + " " + response.body().toString());
                        ResultActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(ResultActivity.this, "No se pudo actualizar el estado del barril", Toast.LENGTH_SHORT).show();
                            }
                        });

                        throw new IOException("Unexpected code " + response);

                    } else if (response.isSuccessful()) {
                        Log.e("Response code: ", response.code() + " " + response.body().toString());
                        updateBarrelData();
                        ResultActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                PuntuationDialog puntuation = new PuntuationDialog(ResultActivity.this);
                                puntuation.show();
                            }
                        });


                    }else {
                        ResultActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(ResultActivity.this, "No se pudo actualizar el estado del barril. Compruebe su conexión a internet", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            });

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + jsonStatus + "\"");
        }
        return succes[0];
    }

    private void updateBarrelData() {
        OkHttpClient client = new OkHttpClient();

        String url = "http://betrack.herokuapp.com/barrels/" + id + ".json";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.code() >= 400 && response.code() <= 500) {
                    Log.e("Response code: ", response.code() + " " + response.body().toString());
                    throw new IOException("Unexpected code " + response);

                } else {
                    String jsonData = response.body().string();
                    AppPreferences.instance(getApplication()).saveBarrelInfo(jsonData);
                }
            }

        });


    }

    private void showAssets(boolean found) {
        TextView weAreSorry = (TextView) findViewById(R.id.weAreSorryTV);
        TextView noDataFound = (TextView) findViewById(R.id.noDataAvailableTV);
        ImageView noFoundPic = (ImageView) findViewById(R.id.noFoundPic);
        TextView beertype = (TextView) findViewById(R.id.beerType);
        TextView beerTemperature = (TextView) findViewById(R.id.beerTemperature);
        TextView temperatureUpdate = (TextView) findViewById(R.id.lastUpdate);
        TextView ownerName = (TextView) findViewById(R.id.ownerName);
        TextView beerSize = (TextView) findViewById(R.id.beerSize);
        TextView barrelNumber = (TextView) findViewById(R.id.barrelnumber);
        barrelStatus = (TextView) findViewById(R.id.barrelState);
        ImageView goldenAlePic = (ImageView) findViewById(R.id.goldenAlePic);
        TextView beerMoreData = (TextView) findViewById(R.id.beerMoreData);
        TextView updateTV = (TextView) findViewById(R.id.updateTV);
        TextView temperatureMoreData = (TextView) findViewById(R.id.tempertureMoreData);
        ImageView ownerPic = (ImageView) findViewById(R.id.ownerPic);
        TextView ownerMoreData = (TextView) findViewById(R.id.ownerMoreData);

        assert weAreSorry != null;
        weAreSorry.setVisibility(found ? View.INVISIBLE : View.VISIBLE);
        assert noDataFound != null;
        noDataFound.setVisibility(found ? View.INVISIBLE : View.VISIBLE);
        assert noFoundPic != null;
        noFoundPic.setVisibility(found ? View.INVISIBLE : View.VISIBLE);
        emptyFillBtn.setVisibility(found ? View.VISIBLE : View.INVISIBLE);
        assert beertype != null;
        beertype.setVisibility(found ? View.VISIBLE : View.INVISIBLE);
        assert beerTemperature != null;
        beerTemperature.setVisibility(found ? View.VISIBLE : View.INVISIBLE);
        assert temperatureUpdate != null;
        temperatureUpdate.setVisibility(found ? View.VISIBLE : View.INVISIBLE);
        assert ownerName != null;
        ownerName.setVisibility(found ? View.VISIBLE : View.INVISIBLE);
        assert beerSize != null;
        beerSize.setVisibility(found ? View.VISIBLE : View.INVISIBLE);
        assert barrelNumber != null;
        barrelNumber.setVisibility(found ? View.VISIBLE : View.INVISIBLE);
        assert goldenAlePic != null;
        goldenAlePic.setVisibility(found ? View.VISIBLE : View.INVISIBLE);
        assert beerMoreData != null;
        beerMoreData.setVisibility(found ? View.VISIBLE : View.INVISIBLE);
        assert updateTV != null;
        updateTV.setVisibility(found ? View.VISIBLE : View.INVISIBLE);
        assert temperatureMoreData != null;
        temperatureMoreData.setVisibility(found ? View.VISIBLE : View.INVISIBLE);
        assert ownerPic != null;
        ownerPic.setVisibility(found ? View.VISIBLE : View.INVISIBLE);
        assert ownerMoreData != null;
        ownerMoreData.setVisibility(found ? View.VISIBLE : View.INVISIBLE);


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private String readTextFromMessage(NdefMessage ndefMessage) {

        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if (ndefRecords != null && ndefRecords.length > 0) {

            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromNdefRecord(ndefRecord);

            return tagContent;

        }
        return null;
    }

    public String getTextFromNdefRecord(NdefRecord ndefRecord) {
        String tagContent = null;
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1,
                    payload.length - languageSize - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("getTextFromNdefRecord", e.getMessage(), e);
        }
        return tagContent;
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.emptyFillBtn:
                postNewStatus();

                break;
            case R.id.beerMoreData:
                i = new Intent(ResultActivity.this, BeerMoreDataActivity.class);
                startActivity(i);
                break;
            case R.id.ownerMoreData:
                i = new Intent(ResultActivity.this, OwnerMoreData.class);
                startActivity(i);
                break;

            case R.id.tempertureMoreData:
                i = new Intent(ResultActivity.this, TemperatureMoreDataActivity.class);
                startActivity(i);
        }

    }
}
