package be.ipl.mobile.projet.historypub.pojo.epreuves;

/**
 * Created by nat on 19-11-15.
 */
public class Reponse {
    private String reponse;

    public Reponse(String reponse) {
        this.reponse = reponse.trim();
    }

    public String getReponse() {
        return reponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reponse reponse1 = (Reponse) o;

        return !(reponse != null ? !reponse.equalsIgnoreCase(reponse1.reponse) : reponse1.reponse != null);

    }

    @Override
    public int hashCode() {
        return reponse != null ? reponse.hashCode() : 0;
    }
}
