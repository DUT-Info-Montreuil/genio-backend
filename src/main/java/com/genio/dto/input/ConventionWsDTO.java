package com.genio.dto.input;

import com.genio.dto.*;
import jakarta.validation.constraints.NotNull;

public class ConventionWsDTO {

    @NotNull
    private EtudiantDTO etudiant;

    @NotNull
    private TuteurDTO tuteur;

    @NotNull
    private OrganismeDTO organisme;

    @NotNull
    private StageDTO stage;

    @NotNull
    private EnseignantDTO enseignant;

    @NotNull
    private ConventionDTO convention;

    @NotNull
    private String annee;

    @NotNull
    private Long modeleId;

    public ConventionWsDTO(EtudiantDTO etudiant, TuteurDTO tuteur, OrganismeDTO organisme, StageDTO stage, EnseignantDTO enseignant, ConventionDTO convention, String annee, Long modeleId) {
        this.etudiant = etudiant;
        this.tuteur = tuteur;
        this.organisme = organisme;
        this.stage = stage;
        this.enseignant = enseignant;
        this.convention = convention;
        this.annee = annee;
        this.modeleId = modeleId;
    }

    public EtudiantDTO getEtudiant() {
        return etudiant;
    }

    public TuteurDTO getTuteur() {
        return tuteur;
    }

    public OrganismeDTO getOrganisme() {
        return organisme;
    }

    public StageDTO getStage() {
        return stage;
    }

    public EnseignantDTO getEnseignant() {
        return enseignant;
    }

    public ConventionDTO getConvention() {
        return convention;
    }

    public String getAnnee() {
        return annee;
    }

    public Long getModeleId() {
        return modeleId;
    }
}