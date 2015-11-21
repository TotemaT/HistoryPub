package be.ipl.mobile.projet.historypub.pojo.epreuves;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matt on 10/11/15.
 */
public class EpreuveATrou extends Epreuve {

    private final List<String> mots;

    public EpreuveATrou(int num, String question, String uri, int points) {
        super(num, question, uri, Type.ATROU, points);
        mots = new ArrayList<>();
    }

    public void addMot(String mot) {
        mots.add(mot);
    }

    public boolean estRéponseCorrecte(List<String> reponses) {
        for (int i = 0; i < mots.size(); i++) {
            if (!mots.get(i).equals(reponses.get(i))) {
                return false;
            }
        }
        return true;
    }
}
