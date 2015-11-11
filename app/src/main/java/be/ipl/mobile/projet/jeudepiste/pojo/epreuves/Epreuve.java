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

    public enum Type {
        QCM, PHOTO, ATROU;
    }

    public int getNum() {
        return num;
    }

    public String getQuestion() {
        return question;
    }

    public String getUri() {
        return uri;
    }

    public Type getType() {
        return type;
    }

    public int getPoints() {
        return points;
    }
}
