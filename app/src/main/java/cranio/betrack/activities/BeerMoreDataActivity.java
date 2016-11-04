package cranio.betrack.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

import cranio.betrack.R;
import cranio.betrack.pojos.BarrelInformationPojo;
import cranio.betrack.utils.AppPreferences;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BeerMoreDataActivity extends AppCompatActivity {

    private String jsonData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_more_data);

        jsonData= AppPreferences.instance(getApplication()).getBarrelInfo();
        showBeerData();
    }

    private void showBeerData() {
        Gson gson = new Gson();
        BarrelInformationPojo barrelInformationPojo = gson.fromJson(jsonData, BarrelInformationPojo.class);

        TextView beertype = (TextView)findViewById(R.id.beerTypeMoreData);
        assert beertype != null;
        beertype.setText(barrelInformationPojo.getBarreldata().getType());

    //    TextView rnpa = (TextView) findViewById(R.id.rnpa);
      //  assert rnpa != null;
      //  rnpa.setText(barrelInformationPojo.getBarreldata().getRnpa());

        TextView beerDescription= (TextView) findViewById(R.id.beerDescription);
        assert beerDescription != null;
        beerDescription.setText(barrelInformationPojo.getBarreldata().getDescription());

        TextView alcoholNumber = (TextView) findViewById(R.id.beerAlcoholNumber);
        assert alcoholNumber != null;
        alcoholNumber.setText(""+barrelInformationPojo.getBarreldata().getAlcohol()+"%");

        TextView beerIBUnumber= (TextView) findViewById(R.id.beerIBUNumber);
        assert beerIBUnumber != null;
        beerIBUnumber.setText(""+barrelInformationPojo.getBarreldata().getIbu());

        TextView elaborationDate = (TextView) findViewById(R.id.elaborationDate);
        assert elaborationDate != null;
        elaborationDate.setText(barrelInformationPojo.getBarreldata().getElaboration_date());

        TextView expirationDate = (TextView) findViewById(R.id.expirationDate);
        assert expirationDate != null;
        expirationDate.setText(barrelInformationPojo.getBarreldata().getExpiration_date());

        TextView lot = (TextView) findViewById(R.id.lotNumber);
        assert lot != null;
        lot.setText("Lote: "+barrelInformationPojo.getBarreldata().getLot());

        TextView comments =(TextView) findViewById(R.id.comments);
        assert comments != null;
        comments.setText(barrelInformationPojo.getBarreldata().getComments());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
