package be.ipl.mobile.projet.historypub.pojo.epreuves;

/**
 * Created by nat on 11-11-15.
 */
public class ReponseQCM extends Reponse{
    private boolean bonne;

    public ReponseQCM(String reponse, boolean bonne) {
        super(reponse);
        this.bonne = bonne;
    }

    public boolean estBonne() {
        return bonne;
    }
}
