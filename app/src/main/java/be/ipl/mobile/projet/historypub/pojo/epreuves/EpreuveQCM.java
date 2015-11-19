package be.ipl.mobile.projet.historypub.pojo.epreuves;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matt on 10/11/15.
 */
public class EpreuveQCM extends Epreuve {

    public List<ReponseQCM> reponses;

    public EpreuveQCM(int num, String question, String uri, int points) {
        super(num, question, uri, Type.QCM, points);
        reponses = new ArrayList<>();
    }

    public void addReponse(String reponse, boolean bonne) {
        reponses.add(new ReponseQCM(reponse, bonne));
    }

    public void addReponse(ReponseQCM reponse){
        reponses.add(reponse);
    }

    public boolean estReponseCorrecte(int numero) {
        return reponses.get(numero).estBonne();
    }
}
