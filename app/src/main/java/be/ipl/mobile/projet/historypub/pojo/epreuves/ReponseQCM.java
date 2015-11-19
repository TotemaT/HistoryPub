package be.ipl.mobile.projet.historypub.pojo.epreuves;

/**
 * Created by nat on 11-11-15.
 */
public class ReponseQCM {
    private String reponse;
    private boolean bonne;

    public ReponseQCM(String reponse, boolean bonne) {
        this.reponse = reponse;
        this.bonne = bonne;
    }

    public String getReponse() {
        return reponse;
    }

    public boolean estBonne() {
        return bonne;
    }
}
