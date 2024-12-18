package flyweight;

import java.util.ArrayList;
import java.util.List;

public class Texte {
    private CaractereAttributFlyweighted flyweight = new CaractereAttributFlyweighted();
    private List<Caractere> caracteres = new ArrayList<>();

    public void ajouter(Character codeClavier, int couleur, int taille, int police, boolean useFlyweight) {
        // Avec Flyweight
        if (useFlyweight) {
            this.caracteres.add(new Caractere(codeClavier, flyweight.getCaractereAttribut(couleur, taille, police)));
        }

        // Sans Flyweight
        else {
            this.caracteres.add(new Caractere(codeClavier, flyweight.getCaractereAttributNoFlyweight(couleur, taille, police)));
        }
    }

    public void imprimer() {
        for (Caractere c : this.caracteres) {
            System.out.println(c.getCodeClavier() + " [" + c.getAttribut() + "]");
        }
    }
}
