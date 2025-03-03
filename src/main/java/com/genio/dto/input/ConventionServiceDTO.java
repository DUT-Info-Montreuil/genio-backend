package com.genio.dto.input;

import com.genio.dto.*;
import jakarta.validation.constraints.NotNull;

public class ConventionServiceDTO {

    @NotNull
    private EtudiantDTO etudiant;

    @NotNull
    private MaitreDeStageDTO maitreDeStage;

    @NotNull
    private OrganismeDTO organisme;

    @NotNull
    private StageDTO stage;

    @NotNull
    private TuteurDTO tuteur;

    @NotNull
    private Long modeleId;

    public EtudiantDTO getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(EtudiantDTO etudiant) {
        this.etudiant = etudiant;
    }

    public MaitreDeStageDTO getMaitreDeStage() {
        return maitreDeStage;
    }

    public void setMaitreDeStage(MaitreDeStageDTO maitreDeStage) {
        this.maitreDeStage = maitreDeStage;
    }

    public OrganismeDTO getOrganisme() {
        return organisme;
    }

    public void setOrganisme(OrganismeDTO organisme) {
        this.organisme = organisme;
    }

    public StageDTO getStage() {
        return stage;
    }

    public void setStage(StageDTO stage) {
        this.stage = stage;
    }

    public TuteurDTO getTuteur() {
        return tuteur;
    }

    public void setTuteur(TuteurDTO tuteur) {
        this.tuteur = tuteur;
    }

    public Long getModeleId() {
        return modeleId;
    }

    public void setModeleId(Long modeleId) {
        this.modeleId = modeleId;
    }

    @Override
    public String toString() {
        return "ConventionServiceDTO{" +
                "etudiant=" + etudiant +
                ", maitreDeStage=" + maitreDeStage +
                ", organisme=" + organisme +
                ", stage=" + stage +
                ", tuteur=" + tuteur +
                ", modeleId=" + modeleId +
                '}';
    }
}