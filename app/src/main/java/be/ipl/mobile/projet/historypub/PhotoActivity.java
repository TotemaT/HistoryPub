package be.ipl.mobile.projet.historypub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import be.ipl.mobile.projet.historypub.pojo.Etape;
import be.ipl.mobile.projet.historypub.pojo.epreuves.Epreuve;

public class PhotoActivity extends AppCompatActivity {
    private static final String TAG = "PhotoActivity";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    private Etape mEtape;
    private Epreuve mEpreuve;

    private Button mButton;
    private TextView mQuestion;
    private ImageView mPhoto;

    private Uri uri;
    private boolean photoPrise = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        mEtape = GestionEtapes.getInstance(this).getEtape(getIntent().getIntExtra(Config.EXTRA_ETAPE_COURANTE, 0));
        mEpreuve = mEtape.getEpreuve(getIntent().getStringExtra(Config.EXTRA_EPREUVE));

        /* Gestion de la prise de photo */
        final Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        uri = getOutputMediaFileUri();
        i.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        mButton = (Button) findViewById(R.id.picture_btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!photoPrise) {
                    startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                } else {
                    Utils utils = new Utils(PhotoActivity.this);
                    Toast.makeText(PhotoActivity.this, "Bonne réponse! +" + mEpreuve.getPoints() + " points.", Toast.LENGTH_LONG).show();
                    utils.augmenterPoints(mEpreuve.getPoints());
                    utils.chargerEpreuveOuEtapeSuivante(mEtape, mEpreuve);
                    finish();
                }
            }
        });

        mQuestion = (TextView) findViewById(R.id.question_textView);
        mQuestion.setText(mEpreuve.getQuestion());
        mPhoto = (ImageView) findViewById(R.id.photo_imageview);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_epreuve, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        (menu.findItem(R.id.score_menu)).setTitle("Score: " + new Utils(this).getPoints());
        (menu.findItem(R.id.reinit_menu)).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Utils util = new Utils(PhotoActivity.this);
                GestionEtapes g = GestionEtapes.getInstance(PhotoActivity.this);
                SharedPreferences.Editor edit = getSharedPreferences(Config.PREFERENCES,MODE_PRIVATE).edit();
                edit.putInt(Config.PREF_ETAPE_COURANTE,0);
                edit.putString(Config.PREF_EPREUVE_COURANTE, null);
                edit.putInt(Config.PREF_POINTS_TOTAUX, 0);
                edit.apply();
                util.chargerEpreuveOuEtapeSuivante(null, null);
                return false;
            }
        });
        return true;
    }


    private static Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "HistoryPub");
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d("MyCameraApp", "failed to create directory");
            return null;
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    mPhoto.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri));
                    mPhoto.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    Log.d(TAG, "Impossible d'afficher la photo");
                }
                photoPrise = true;
                mButton.setText(R.string.ok);
            } else if (resultCode == RESULT_CANCELED) {

            } else {
                // Image capture failed, advise user
            }
        }
    }

}