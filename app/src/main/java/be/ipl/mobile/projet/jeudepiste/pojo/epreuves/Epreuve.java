package be.ipl.mobile.projet.jeudepiste.pojo.epreuves;

/**
 * Created by matt on 10/11/15.
 */
public abstract class Epreuve {
    private int num;
    private String question;
    private String uri;
    private Type type;
    private int points;

    public Epreuve(int num, String question, String uri, Type type, int points) {
        this.num = num;
        this.question = question;
        this.uri = uri;
        this.type = type;
        this.points = points;
    }

    enum Type {
        QCM, PHOTO, ATROU;
    }
}
