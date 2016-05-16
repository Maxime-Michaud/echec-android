package data;

import android.support.annotation.Nullable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Contiens les informations d'un joueur
 * Created by Maxime on 4/30/2016.
 */
public class Utilisateur{
    private int id;
    private String username;
    private String nom;
    private String prenom;
    private String email;
    private TYPE_COMPTE type;
    private List<Partie> parties;
    private List<Partie> nouvellesParties;
    private Map<Utilisateur, RELATION> relations;
    private List<ResultatDefi> defis;

    /**
     * Initialise un utilisateur
     *
     * @param id       id de l'utilisateur
     * @param username username de l'utilisateur
     * @param nom      nom de l'utilisateur
     * @param prenom   prenom de l'utilisateur
     * @param email    email de l'utilisateur
     * @param type     type de compte
     */
    Utilisateur(int id, String username, String nom, String prenom, String email, int type) {
        this.id = id;
        this.username = username;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.type = intToType(type);

        parties = null;
        defis = null;
        relations = null;
    }

    /**
     * @param i int a convertir
     * @return le type correstpondant
     */
    static TYPE_COMPTE intToType(int i) {
        switch (i) {
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

    /**
     * Obtiens l'id de l'utilisateur
     * @return id de l'utilisateur
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiens le username de l'utilisateur
     * @return username de l'utilisateur
     */
    public String getUsername() {
        return username;
    }

    /**
     * Obtiens le nom de l'utilisateur
     * @return nom de l'utilisateur
     */
    @Nullable
    public String getNom() {
        return nom;
    }

    /**
     * Assigne le nom de l'utilisateur
     * @param nom nom de l'utilisateur
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Obtiens le prenom de l'utilisateur
     * @return prenom de l'utilisateur
     */
    @Nullable
    public String getPrenom() {
        return prenom;
    }

    /**
     * Assigne le prénom de l'utilisateur
     *
     * @param prenom prénom de l'utilisateur
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Obtiens l'adresse email de l'utilisateur
     * @return adresse email de l'utilisateur
     */
    @Nullable
    public String getEmail() {
        return email;
    }

    /**
     * Assigne le email de l'utilisateur
     * @param email email de l'utilisateur
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiens le type de compte de l'utilisateur
     * @return type de compte de l'utilisateur
     */
    public TYPE_COMPTE getType() {
        return type;
    }

    /**
     * Assigne le type du compte utilisateur
     * @param type type du compte utilisateur
     */
    public void setType(TYPE_COMPTE type) {
        this.type = type;
    }

    public List<Defi> getSuggestions() {
        return GestionnaireSuggestion.get(this);
    }

    /**
     * Charge les relations de la base de données en mémoire
     */
    private void loadRelations() {
        relations = GestionnaireUtilisateurs.getRelations(id);
    }

    private List<Utilisateur> getUtilisateursPourRelation(RELATION rel) {
        if (relations == null)
            loadRelations();

        return Stream.of(relations)
                .filter(e -> e.getValue() == rel)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Obtiens la liste d'amis de l'utilisateur
     * @return liste d'amis de l'utilisateur
     */
    public List<Utilisateur> getAmis() {
        return Collections.unmodifiableList(getUtilisateursPourRelation(RELATION.Ami));
    }

    /**
     * Obtiens la liste des demandes d'amis en cours pour l'utilisateur
     * @return liste des demandes d'amis en cours pour l'utilisateur
     */
    public List<Utilisateur> getDemandesAmis() {
        return Collections.unmodifiableList(getUtilisateursPourRelation(RELATION.Attente_de_demande_dami));
    }

    /**
     * Obtiens la liste des demandes d'amis refusées par l'utilisateur
     * @return liste des demandes d'amis refusées par l'utilisateur
     */
    public List<Utilisateur> getDemandesRefuse() {
        return Collections.unmodifiableList(getUtilisateursPourRelation(RELATION.Refusé));
    }

    /**
     * Obtiens la liste des utilisateurs bloqués par l'utilisateur
     * @return liste des utilisateurs bloqués par l'utilisateur
     */
    public List<Utilisateur> getBloques() {
        return Collections.unmodifiableList(getUtilisateursPourRelation(RELATION.Bloqué));
    }

    /**
     * Obtiens la liste des défis essayés par l'utilisateur
     * @return liste des défis essayés par l'utilisateur
     */
    public List<ResultatDefi> getDefisEssaye() {
        return Collections.unmodifiableList(GestionnaireDefi.getResultats(this));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Utilisateur that = (Utilisateur) o;

        return getId() == that.getId()
                && getUsername().equals(that.getUsername())
                && !(getNom() != null ? !getNom().equals(that.getNom()) : that.getNom() != null)
                && !(getPrenom() != null ? !getPrenom().equals(that.getPrenom()) : that.getPrenom() != null)
                && ((!(getEmail() != null ? !getEmail().equals(that.getEmail()) : that.getEmail() != null))
                && getType() == that.getType());

    }

    @Override
    public int hashCode() {
        return getId();
    }

    /**
     * Ajoute le résultat d'un défi a la bd
     *
     * @param defi    défi effectué par l'utilisateur
     * @param nbTours nombre de tours
     * @param reussi  si l'utilisateur a réussi
     * @return Si le résultat a été bien ajouté
     */
    public boolean ajouterResultat(Defi defi, int nbTours, boolean reussi) {
        return GestionnaireDefi.ajouterResultat(this, new ResultatDefi(defi, nbTours, reussi));
    }

    /**
     * Obtiens les résultats d'une collection de défi pour l'utilisateur
     *
     * @param collection Les défis a vérifier
     */
    public List<ResultatDefi> getResultats(Collection<Defi> defis) {
        ArrayList<ResultatDefi> resultats = new ArrayList<>();
        List<ResultatDefi> resultatsUtilisateur = getDefisEssaye();

        for (Defi d : defis) {
            resultats.add(
                    Stream.of(resultatsUtilisateur)
                            .filter(r -> r.getDefi().equals(d))
                            .filter(ResultatDefi::isReussi)
                            .sortBy(ResultatDefi::getNbTour)
                            .findFirst().orElse(new ResultatDefi(d, 0, false)));
        }

        return Collections.unmodifiableList(resultats);
    }

    /**
     * Ajoute une relation a un utilisateur
     * @param u Utilisateur avec lequel on ajoute une relation
     * @param r type de relation a ajoutée a l'utilisateur
     * @return si la relation a été ajoutée
     */
    public boolean ajouterRelation(Utilisateur u, RELATION r)
    {
        return GestionnaireUtilisateurs.ajouterRelation(this, u, r);
    }

    enum TYPE_COMPTE {
        Publique,
        Privé,
        Amis_seulement,
        Anonyme;

        public int toInt() {
            switch (this) {
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

    enum RELATION {
        Ami,
        Attente_de_demande_dami,
        Refusé,
        Bloqué;

        public static RELATION fromInt(int anInt) {
            switch (anInt) {
            case 1:
                return Ami;
                case 2:
                    return Attente_de_demande_dami;
                case 3:
                    return Refusé;
                case 4:
                    return Bloqué;
                default:
                    return Bloqué;
            }
        }

        public int toInt() {
            switch (this) {

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
}
