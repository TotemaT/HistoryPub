package be.ipl.mobile.projet.historypub.activites;

import android.view.View;
import android.widget.Button;

import be.ipl.mobile.projet.historypub.R;

/**
 * Classe reprenant différentes méthodes utilisées dans les différentes activités épreuve uniquement.
 */
public abstract class EpreuveActivity extends BasicActivity {

    protected Button mCheatButton;
    protected Button mHelpButton;
    protected int mPointsAEnlever = 0;

    /**
     * Initialise le bouton de triche dans les différentes épreuves.
     */
    protected void initCheatButton() {
        mCheatButton = (Button) findViewById(R.id.cheat_btn);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelpButton.setEnabled(false);
                mCheatButton.setEnabled(false);
                doCheat();
                mPointsAEnlever = mEpreuve.getPoints();
            }
        });
    }

    /**
     * Initialise le bouton d'aide dans les différentes épreuves.
     */
    protected Button initgetHelpButton() {
        mHelpButton = (Button) findViewById(R.id.help_btn);
        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelpButton.setEnabled(false);
                doHelp();
                mPointsAEnlever = (mEpreuve.getPoints() / 2);
            }
        });
        return mHelpButton;
    }

    /**
     * Réalise l'action à effectuer lors de l'appui sur le bouton de triche.
     */
    public abstract void doHelp();

    /**
     * Réalise l'action à effectuer lors de l'appui sur le bouton d'aide.
     */
    public abstract void doCheat();
}
