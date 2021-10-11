import java.util.Objects;

public class Bloc {
    private String nom;

    public Bloc(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bloc bloc = (Bloc) o;

        return nom.equals(bloc.nom);
    }

    @Override
    public int hashCode() {
        return nom != null ? nom.hashCode() : 0;
    }

    @Override
    public String toString() {
        return nom;
    }


}
