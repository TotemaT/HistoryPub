package be.ipl.mobile.projet.historypub.pojo.epreuves;

/**
 * Created by nat on 19-11-15.
 */
public class Reponse {
    private String reponse;

    public Reponse(String reponse) {
        this.reponse = reponse;
    }

    public String getReponse() {
        return reponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reponse)) return false;

        Reponse reponse1 = (Reponse) o;

        return !(reponse != null ? !reponse.equals(reponse1.reponse) : reponse1.reponse != null);
    }
}
