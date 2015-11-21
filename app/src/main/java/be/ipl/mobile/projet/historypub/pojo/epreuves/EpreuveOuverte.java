package be.ipl.mobile.projet.historypub.pojo.epreuves;

import java.util.List;

/**
 * Created by nat on 19-11-15.
 */
public class EpreuveOuverte extends Epreuve{

    private final List<Reponse> reponses;

    public EpreuveOuverte(int num, String question, String uri, int points, List<Reponse> reponses) {
        super(num, question, uri, Type.OUVERTE, points);
        this.reponses = reponses;
    }

    public List<Reponse> getReponses() {
        return reponses;
    }

    public Reponse getReponse(){
        return reponses.get(0);
    }

    public boolean estReponseCorrecte(Reponse reponse){
        for(Reponse rep: reponses)
            if(rep.equals(reponse))
                return true;
        return false;
    }
}
