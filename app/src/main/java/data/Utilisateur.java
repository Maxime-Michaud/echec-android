package data;

import java.util.List;
import java.util.Map;

/**
 * Contiens les informations d'un joueur
 * Created by Maxime on 4/30/2016.
 */
public class Utilisateur {
    enum TYPE_COMPTE{
        Publique,
        Privé,
        Amis_seulement,
        Anonyme
    }
    enum RELATION{
        Ami,
        Attente_de_demande_dami,
        Refusé,
        Bloqué
    }

    int id;
    String username;
    String nom;
    String prenom;
    String email;
    TYPE_COMPTE type;
    List<Parties> parties;
    Map<Utilisateur, RELATION> relations;
    List<Defi> defisEssaye;

    /**
     * Initialise un utilisateur
     * @param id
     * @param username
     * @param nom
     * @param prenom
     * @param email
     * @param type
     */
    Utilisateur(int id, String username, String nom, String prenom, String email, int type) {
        this.id = id;
        this.username = username;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.type = intToType(type);

        parties = null;
        defisEssaye = null;
        relations = null;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public TYPE_COMPTE getType() {
        return type;
    }

    public List<Parties> getParties() {
        //TODO
        throw new UnsupportedOperationException("");
    }

    public Map<Utilisateur, RELATION> getRelations() {
        //TODO
        throw new UnsupportedOperationException("");
    }

    public List<Defi> getDefisEssaye() {
        //TODO
        throw new UnsupportedOperationException("");
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setType(TYPE_COMPTE type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Utilisateur that = (Utilisateur) o;

        if (getId() != that.getId()) return false;
        if (!getUsername().equals(that.getUsername())) return false;
        if (getNom() != null ? !getNom().equals(that.getNom()) : that.getNom() != null)
            return false;
        if (getPrenom() != null ? !getPrenom().equals(that.getPrenom()) : that.getPrenom() != null)
            return false;
        if (getEmail() != null ? !getEmail().equals(that.getEmail()) : that.getEmail() != null)
            return false;
        return getType() == that.getType();

    }

    @Override
    public int hashCode() {
        return getId();
    }

    /**
     * @param i
     * @return
     */
    static TYPE_COMPTE intToType(int i)
    {
        switch (i){
            case 1:
                return TYPE_COMPTE.Publique;
            case 2:
                return TYPE_COMPTE.Privé;
            case 3:
                return TYPE_COMPTE.Amis_seulement;
            case 4:
                return TYPE_COMPTE.Anonyme;
            default:
                throw new IllegalArgumentException("Le type est invalide");
        }
    }
}
