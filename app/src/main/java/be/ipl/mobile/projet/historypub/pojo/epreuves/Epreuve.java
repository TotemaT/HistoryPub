package be.ipl.mobile.projet.historypub.pojo.epreuves;

/**
 * Created by matt on 10/11/15.
 */
public abstract class Epreuve {
    private final int num;
    private final String question;
    private final String uri;
    private final Type type;
    private final int points;

    Epreuve(int num, String question, String uri, Type type, int points) {
        this.num = num - 1;
        this.question = question;
        this.uri = uri;
        this.type = type;
        this.points = points;
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
