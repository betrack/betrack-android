package cranio.betrack.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import cranio.betrack.R;
import cranio.betrack.activities.NfcActivity;

/**
 * Created by MariaSol on 18/10/2016.
 */
public class PuntuationDialog extends Dialog implements View.OnClickListener, View.OnTouchListener {

    public Activity c;
    public Dialog d;
    public Button sendBtn, cancelBtn;
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageView star4;
    private ImageView star5;

    public PuntuationDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        sendBtn = (Button) findViewById(R.id.sendBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        sendBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        star1 = (ImageView) findViewById(R.id.star1);
        star2 = (ImageView) findViewById(R.id.star2);
        star3 = (ImageView) findViewById(R.id.star3);
        star4 = (ImageView) findViewById(R.id.star4);
        star5 = (ImageView) findViewById(R.id.star5);

        star1.setOnTouchListener(this);
        star2.setOnTouchListener(this);
        star3.setOnTouchListener(this);
        star4.setOnTouchListener(this);
        star5.setOnTouchListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendBtn:
                dismiss();
                break;
            case R.id.cancelBtn:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.star1:
                star1.setImageResource(R.drawable.ic_toggle_star);
                star2.setImageResource(R.drawable.ic_toggle_star_outline);
                star3.setImageResource(R.drawable.ic_toggle_star_outline);
                star4.setImageResource(R.drawable.ic_toggle_star_outline);
                star5.setImageResource(R.drawable.ic_toggle_star_outline);
                break;
            case R.id.star2:
                star1.setImageResource(R.drawable.ic_toggle_star);
                star2.setImageResource(R.drawable.ic_toggle_star);
                star3.setImageResource(R.drawable.ic_toggle_star_outline);
                star4.setImageResource(R.drawable.ic_toggle_star_outline);
                star5.setImageResource(R.drawable.ic_toggle_star_outline);
                break;
            case R.id.star3:
                star1.setImageResource(R.drawable.ic_toggle_star);
                star2.setImageResource(R.drawable.ic_toggle_star);
                star3.setImageResource(R.drawable.ic_toggle_star);
                star4.setImageResource(R.drawable.ic_toggle_star_outline);
                star5.setImageResource(R.drawable.ic_toggle_star_outline);
                break;
            case R.id.star4:
                star1.setImageResource(R.drawable.ic_toggle_star);
                star2.setImageResource(R.drawable.ic_toggle_star);
                star3.setImageResource(R.drawable.ic_toggle_star);
                star4.setImageResource(R.drawable.ic_toggle_star);
                star5.setImageResource(R.drawable.ic_toggle_star_outline);
                break;
            case R.id.star5:
                star1.setImageResource(R.drawable.ic_toggle_star);
                star2.setImageResource(R.drawable.ic_toggle_star);
                star3.setImageResource(R.drawable.ic_toggle_star);
                star4.setImageResource(R.drawable.ic_toggle_star);
                star5.setImageResource(R.drawable.ic_toggle_star);
                break;
        }
        return false;
    }
}

