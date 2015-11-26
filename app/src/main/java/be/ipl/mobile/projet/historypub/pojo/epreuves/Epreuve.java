/*
    History Pub est une application de jeu de piste proposant de découvrir la ville de Soignies,
    en parcourant cette dernière de bar en bar.

    Copyright (C) 2015
        Matteo Taroli <contact@matteotaroli.be>
        Nathan Raspe <raspe_nathan@live.be>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package be.ipl.mobile.projet.historypub.pojo.epreuves;

/**
 * Epreuve du jeu, composée d'un numéro d'identification, d'une question, d'un URI, d'un type et
 * d'un nombre de points gagnés lors de sa résolution.
 */
public abstract class Epreuve {
    private final int num;
    private final String question;
    private final String uri;
    private final Type type;
    private final int points;
    private final String explication;

    Epreuve(int num, String question, String uri, Type type, int points, String explication) {
        this.num = num - 1;
        this.question = question;
        this.uri = uri;
        this.type = type;
        this.points = points;
        this.explication = explication;
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

    public String getExplication() {
        return explication;
    }
}
