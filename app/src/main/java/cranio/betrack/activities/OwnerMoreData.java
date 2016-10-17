package cranio.betrack.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import cranio.betrack.R;
import cranio.betrack.pojos.BarrelInformationPojo;
import cranio.betrack.utils.AppPreferences;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OwnerMoreData extends AppCompatActivity implements View.OnClickListener {

    private String jsonData;
    private BarrelInformationPojo barrelInformationPojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_more_data);

        jsonData= AppPreferences.instance(getApplication()).getBarrelInfo();

        Gson gson = new Gson();
        barrelInformationPojo = gson.fromJson(jsonData, BarrelInformationPojo.class);

        Button sendEmail = (Button) findViewById(R.id.ownerMail);
        assert sendEmail != null;
        sendEmail.setOnClickListener(this);

        Button callOwner = (Button) findViewById(R.id.ownerPhone);
        assert callOwner != null;
        callOwner.setOnClickListener(this);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/betrackfont.ttf");
        sendEmail.setTypeface(typeface);
        sendEmail.setTypeface(typeface);

        showOwnerData();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void showOwnerData() {

        TextView ownerName = (TextView) findViewById(R.id.ownerName);
        assert ownerName != null;
        ownerName.setText(barrelInformationPojo.getOwner().getOwnerData().getName());

        ImageView ownerLogo = (ImageView) findViewById(R.id.ownerlogo);

        switch (barrelInformationPojo.getOwner().getOwnerData().getStoreType()) {
            case "transport":
                assert ownerLogo != null;
                ownerLogo.setImageResource(R.drawable.transport);
                break;
            case "bar":
                assert ownerLogo != null;
                ownerLogo.setImageResource(R.drawable.bar);
                break;
            case "brewery":
                assert ownerLogo != null;
                ownerLogo.setImageResource(R.drawable.bewery);
                break;
        }

        TextView companyName = (TextView) findViewById(R.id.companyName);
        assert companyName != null;
        companyName.setText(barrelInformationPojo.getOwner().getOwnerData().getSocialReason());

        TextView cuit = (TextView) findViewById(R.id.cuit);
        assert cuit != null;
        cuit.setText("CUIT " + barrelInformationPojo.getOwner().getOwnerData().getCUIT());

        TextView ownerAddress = (TextView) findViewById(R.id.ownerAddress);
        assert ownerAddress != null;
        ownerAddress.setText(barrelInformationPojo.getOwner().getOwnerData().getAddress());

        Button ownerMail = (Button) findViewById(R.id.ownerMail);
        assert ownerMail != null;
        ownerMail.setText(barrelInformationPojo.getOwner().getOwnerData().getEmail());

        Button ownerPhone = (Button) findViewById(R.id.ownerPhone);
        assert ownerPhone != null;
        ownerPhone.setText(barrelInformationPojo.getOwner().getOwnerData().getPhoneNumber());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ownerMail:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{barrelInformationPojo.getOwner().getOwnerData().getEmail()});
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(OwnerMoreData.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.ownerPhone:
                String patientTelephone = barrelInformationPojo.getOwner().getOwnerData().getPhoneNumber();
                String uri = "tel:" + patientTelephone.trim();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
                startActivity(intent);
                break;

        }
    }
}
