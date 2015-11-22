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

package be.ipl.mobile.projet.historypub.pojo;

import java.util.ArrayList;
import java.util.List;

import be.ipl.mobile.projet.historypub.pojo.epreuves.Epreuve;

/**
 * Etape du jeu, composée d'un numéro d'identification, une zone d'activité, une url et une liste
 * d'épreuves.
 */
public class Etape {
    private int num;
    private String url;
    private Zone zone;
    private List<Epreuve> epreuves;

    public Etape(int num, String url, Zone zone) {
        this.num = num - 1;
        this.url = url;
        this.zone = zone;
        this.epreuves = new ArrayList<>();
    }

    public int getNum() {
        return num;
    }

    public String getUrl() {
        return url;
    }

    public Zone getZone() {
        return zone;
    }

    public int getNombreEpreuves() {
        return epreuves.size();
    }

    public Epreuve getEpreuve(String uri) {
        for (Epreuve epreuve : epreuves) {
            if (epreuve.getUri().equals(uri)) {
                return epreuve;
            }
        }
        throw new IllegalArgumentException("L'uri n'existe pas");
    }

    public Epreuve getEpreuve(int num) {
        if (num < 0 || num > epreuves.size() - 1) {
            return null;
        }
        return epreuves.get(num);
    }

    public void addEpreuve(Epreuve epreuve) {
        epreuves.add(epreuve);
    }
}
