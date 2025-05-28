package com.genio.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;

@Data
@EqualsAndHashCode
public class UtilisateurUpdateDTO {
    private String nom;
    private String prenom;
    private String role;
    private Boolean actif;

    @Override
    public String toString() {
        return "UtilisateurUpdateDTO{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", role='" + role + '\'' +
                ", actif=" + actif +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UtilisateurUpdateDTO)) return false;
        UtilisateurUpdateDTO that = (UtilisateurUpdateDTO) o;
        return Objects.equals(nom, that.nom) &&
                Objects.equals(prenom, that.prenom) &&
                Objects.equals(role, that.role) &&
                Objects.equals(actif, that.actif);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, prenom, role, actif);
    }
}