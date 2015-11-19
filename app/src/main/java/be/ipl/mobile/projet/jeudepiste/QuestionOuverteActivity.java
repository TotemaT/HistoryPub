package be.ipl.mobile.projet.jeudepiste;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class QuestionOuverteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_ouverte);
        Button repondre = (Button) findViewById(R.id.boutton_question_ouverte);
        EditText edit = (EditText) findViewById(R.id.question_ouverte_edit);
        TextView question = (TextView) findViewById(R.id.question_ouverte);
        //Mettre le texte de la question ici
        repondre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Comparer la réponse donnée dans l'EditText avec celle de l'Epreuve
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question_ouverte, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
