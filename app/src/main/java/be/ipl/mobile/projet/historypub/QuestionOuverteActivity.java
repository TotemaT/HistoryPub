package be.ipl.mobile.projet.historypub;

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
        Button repondre = (Button) findViewById(R.id.reponse_btn);
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
}
