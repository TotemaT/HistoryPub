package be.ipl.mobile.projet.historypub.pojo.epreuves;

/**
 * Created by nat on 19-11-15.
 */
public class EpreuveOuverte extends Epreuve{

    private Reponse reponse;

    public EpreuveOuverte(int num, String question, String uri, int points, Reponse reponse) {
        super(num, question, uri, Type.OUVERTE, points);
        this.reponse = reponse;
    }

    public boolean estReponseCorrecte(Reponse reponse){
        return this.reponse.equals(reponse);
    }
}
