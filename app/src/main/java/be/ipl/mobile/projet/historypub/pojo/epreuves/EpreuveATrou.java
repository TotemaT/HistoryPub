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

import java.util.ArrayList;
import java.util.List;

/**
 * Epreuve spécifique représentant une question à trou, la réponse étant composée d'une liste de mots.
 */
public class EpreuveATrou extends Epreuve {

    private final List<String> mots;

    public EpreuveATrou(int num, String question, String uri, int points, String expliquation) {
        super(num, question, uri, Type.ATROU, points, expliquation);
        mots = new ArrayList<>();
    }

    public void addMot(String mot) {
        mots.add(mot);
    }

    public boolean estRéponseCorrecte(List<String> reponses) {
        for (int i = 0; i < mots.size(); i++) {
            if (!mots.get(i).equals(reponses.get(i))) {
                return false;
            }
        }
        return true;
    }

    public List<String> getMots(){
        return this.mots;
    }
}
