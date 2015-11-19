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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EpreuveOuverte)) return false;

        EpreuveOuverte that = (EpreuveOuverte) o;

        return reponse.equals(that.reponse);

    }
}
