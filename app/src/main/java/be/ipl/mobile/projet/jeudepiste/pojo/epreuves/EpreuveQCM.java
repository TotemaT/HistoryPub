package be.ipl.mobile.projet.jeudepiste.pojo.epreuves;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matt on 10/11/15.
 */
public class EpreuveQCM extends Epreuve {

    public List<Reponse> reponses;

    public EpreuveQCM(int num, String question, String uri, int points) {
        super(num, question, uri, Type.QCM, points);
        reponses = new ArrayList<>();
    }

    public void addReponse(String reponse, boolean bonne) {
        reponses.add(new Reponse(reponse, bonne));
    }

    public boolean estReponseCorrecte(int numero) {
        return reponses.get(numero).estBonne();
    }


    class Reponse {
        public String reponse;
        public boolean bonne;

        public Reponse(String reponse, boolean bonne) {
            this.reponse = reponse;
            this.bonne = bonne;
        }

        public boolean estBonne() {
            return bonne;
        }
    }

}
