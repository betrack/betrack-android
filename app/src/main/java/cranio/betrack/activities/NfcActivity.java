package cranio.betrack.activities;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import cranio.betrack.R;
import cranio.betrack.utils.AppPreferences;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NfcActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        AppPreferences.instance(getApplication()).saveSessionOpen(true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        TextView acerqueDisp = (TextView) findViewById(R.id.putDeviceNearTV);
        assert acerqueDisp != null;
        acerqueDisp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sinNfc();
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_nfc);
        navigationView.setNavigationItemSelectedListener(this);

        TextView username = (TextView) headerView.findViewById(R.id.bussinessName);
        username.setText(AppPreferences.instance(getApplication()).getUsername());
        TextView usermail = (TextView) headerView.findViewById(R.id.bussinessEmail);
        usermail.setText(AppPreferences.instance(getApplication()).getUserEmail());

    }

    private void sinNfc() {
        showTagReadImage(true);

        OkHttpClient client = new OkHttpClient();

        String url = "http://betrack.herokuapp.com/barrels/" + 2 + ".json";

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
                    Intent i = new Intent(NfcActivity.this, ResultActivity.class);
                    AppPreferences.instance(getApplication()).saveBarrelFound(false);
                    startActivity(i);
                    Log.e("Response code: ", response.code() + " " + response.body().toString());
                    throw new IOException("Unexpected code " + response);

                } else {
                    String jsonData = response.body().string();
                    AppPreferences.instance(getApplication()).saveBarrelInfo(jsonData);
                    Intent i = new Intent(NfcActivity.this, ResultActivity.class);
                    i.putExtra("BarrelId", 2);
                    AppPreferences.instance(getApplication()).saveBarrelFound(true);
                    startActivity(i);

                }
            }

        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_closesession) {
            AppPreferences.instance(getApplication()).saveSessionOpen(false);
            Intent i = new Intent(NfcActivity.this, LoginActivity.class);
            startActivity(i);

            // Handle the camera action
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppPreferences.instance(getApplication()).getBarrelFound()) {
            showTagReadImage(false);
        }
        enableForegroundDispatchSystem();
    }

    @Override
    protected void onPause() {
        super.onPause();

        disableForegroundDispatchSystem();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            showTagReadImage(true);

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
                                    Intent i = new Intent(NfcActivity.this, ResultActivity.class);
                                    AppPreferences.instance(getApplication()).saveBarrelFound(false);
                                    startActivity(i);
                                    Log.e("Response code: ", response.code() + " " + response.body().toString());
                                    throw new IOException("Unexpected code " + response);

                                } else {
                                    String jsonData = response.body().string();
                                    AppPreferences.instance(getApplication()).saveBarrelInfo(jsonData);
                                    Intent i = new Intent(NfcActivity.this, ResultActivity.class);
                                    AppPreferences.instance(getApplication()).saveBarrelFound(true);
                                    i.putExtra("BarrelId", id);
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
        ImageView tagTick = (ImageView) findViewById(R.id.tickFound);
        ImageView searchTag = (ImageView) findViewById(R.id.searchTag);
        TextView putCloserTv = (TextView) findViewById(R.id.putDeviceNearTV);
        TextView detectedTv = (TextView) findViewById(R.id.detectedBarrilTV);
        TextView searchingDataTv = (TextView) findViewById(R.id.searchingForDataTV);

        assert tagTick != null;
        tagTick.setVisibility(show ? View.VISIBLE : View.GONE);
        assert detectedTv != null;
        detectedTv.setVisibility(show ? View.VISIBLE : View.GONE);
        assert searchingDataTv != null;
        searchingDataTv.setVisibility(show ? View.VISIBLE : View.GONE);
        assert searchTag != null;
        searchTag.setVisibility(show ? View.GONE : View.VISIBLE);
        assert putCloserTv != null;
        putCloserTv.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private String ByteArrayToHexString(byte[] byteArrayExtra) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";

        for (j = 0; j < byteArrayExtra.length; ++j) {
            in = (int) byteArrayExtra[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }


    private void enableForegroundDispatchSystem() {

        Intent intent = new Intent(this, NfcActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
