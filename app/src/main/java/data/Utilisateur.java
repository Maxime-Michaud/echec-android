package data;

import android.support.annotation.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Contiens les informations d'un joueur
 * Created by Maxime on 4/30/2016.
 */
public class Utilisateur{
    enum TYPE_COMPTE{
        Publique,
        Privé,
        Amis_seulement,
        Anonyme;

        public int toInt()
        {
            switch (this)
            {
                case Publique:
                    return 1;
                case Privé:
                    return 2;
                case Amis_seulement:
                    return 3;
                case Anonyme:
                    return 4;
                default:
                    return 0;
            }
        }
    }
    enum RELATION{
        Ami,
        Attente_de_demande_dami,
        Refusé,
        Bloqué;

        public int toInt()
        {
            switch (this)
            {

                case Ami:
                    return 1;
                case Attente_de_demande_dami:
                    return 2;
                case Refusé:
                    return 3;
                case Bloqué:
                    return 4;
            }
            return 0;
        }
    }

    private int id;
    private String username;
    private String nom;
    private String prenom;
    private String email;
    private TYPE_COMPTE type;

    private List<Parties> parties;
    private List<Parties> nouvellesParties;

    private Map<Utilisateur, RELATION> relations;

    private List<Defi> defisEssaye;
    private List<Defi> nouveauxDefisEssaye;

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

    /**
     * Obtiens l'id de l'utilisateur
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiens le username de l'utilisateur
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Obtiens le nom de l'utilisateur
     * @return
     */
    @Nullable
    public String getNom() {
        return nom;
    }

    /**
     * Obtiens le prenom de l'utilisateur
     * @return
     */
    @Nullable
    public String getPrenom() {
        return prenom;
    }

    /**
     * Obtiens l'adresse email de l'utilisateur
     * @return
     */
    @Nullable
    public String getEmail() {
        return email;
    }

    /**
     * Obtiens le type de compte de l'utilisateur
     * @return
     */
    public TYPE_COMPTE getType() {
        return type;
    }

    /**
     * Obtiens la liste des parties jouées par l'utilisateur
     * @return
     */
    public List<Parties> getParties() {
        //TODO
        throw new UnsupportedOperationException("");
    }

    /**
     * Obtiens la liste d'amis de l'utilisateur
     * @return
     */
    public List<Utilisateur> getAmis(){
        //TODO
        throw new UnsupportedOperationException("");
    }

    /**
     * Obtiens la liste des demandes d'amis en cours pour l'utilisateur
     * @return
     */
    public List<Utilisateur> getDemandesAmis()
    {
        //TODO
        throw new UnsupportedOperationException("");
    }

    /**
     * Obtiens la liste des demandes d'amis refusées par l'utilisateur
     * @return
     */
    public List<Utilisateur> getDemandesRefuse()
    {
        //TODO
        throw new UnsupportedOperationException("");
    }

    /**
     * Obtiens la liste des utilisateurs bloqués par l'utilisateur
     * @return
     */
    public List<Utilisateur> getBloques()
    {
        //TODO
        throw new UnsupportedOperationException("");
    }

    /**
     * Obtiens la liste des défis essayés par l'utilisateur
     * @return
     */
    public List<Defi> getDefisEssaye() {
        //TODO
        throw new UnsupportedOperationException("");
    }

    /**
     * Assigne le nom de l'utilisateur
     * @param nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Assigne le prénom de l'utilisateur
     * @param prenom
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Assigne le email de l'utilisateur
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Assigne le type du compte utilisateur
     * @param type
     */
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