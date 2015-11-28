package be.ipl.mobile.projet.historypub;

import android.view.View;
import android.widget.Button;

/**
 * Created by nat on 28-11-15.
 */
public abstract class EpreuveActivity extends BasicActivity{

    protected void initCheatButton(){
        mCheatButton = (Button) findViewById(R.id.cheat_btn);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelpButton.setEnabled(false);
                mCheatButton.setEnabled(false);
                doCheat();
                pointsAEnlever=mEpreuve.getPoints();
            }
        });
    }

    protected Button initgetHelpButton(){
        mHelpButton = (Button) findViewById(R.id.help_btn);
        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelpButton.setEnabled(false);
                doHelp();
                pointsAEnlever=(mEpreuve.getPoints()/2);
            }
        });
        return mHelpButton;
    }

    public abstract void doHelp();

    public abstract void doCheat();
}
