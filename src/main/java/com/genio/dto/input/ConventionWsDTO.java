package com.genio.dto.input;

import com.genio.dto.*;
import jakarta.validation.constraints.NotNull;

public class ConventionWsDTO {

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

    public ConventionWsDTO(EtudiantDTO etudiant, MaitreDeStageDTO maitreDeStage, OrganismeDTO organisme, StageDTO stage, TuteurDTO tuteur, Long modeleId) {
        this.etudiant = etudiant;
        this.maitreDeStage = maitreDeStage;
        this.organisme = organisme;
        this.stage = stage;
        this.tuteur = tuteur;
        this.modeleId = modeleId;
    }

    public EtudiantDTO getEtudiant() {
        return etudiant;
    }

    public MaitreDeStageDTO getMaitreDeStage() {
        return maitreDeStage;
    }

    public OrganismeDTO getOrganisme() {
        return organisme;
    }

    public StageDTO getStage() {
        return stage;
    }

    public TuteurDTO getTuteur() {
        return tuteur;
    }

    public Long getModeleId() {
        return modeleId;
    }

    @Override
    public String toString() {
        return "ConventionWsDTO{" +
                "etudiant=" + etudiant +
                ", maitreDeStage=" + maitreDeStage +
                ", organisme=" + organisme +
                ", stage=" + stage +
                ", tuteur=" + tuteur +
                ", modeleId=" + modeleId +
                '}';
    }

    public ConventionWsDTO() {

    }

    public void setEtudiant(EtudiantDTO etudiant) {
        this.etudiant = etudiant;
    }

    public void setMaitreDeStage(MaitreDeStageDTO maitreDeStage) {
        this.maitreDeStage = maitreDeStage;
    }

    public void setOrganisme(OrganismeDTO organisme) {
        this.organisme = organisme;
    }

    public void setStage(StageDTO stage) {
        this.stage = stage;
    }

    public void setTuteur(TuteurDTO tuteur) {
        this.tuteur = tuteur;
    }

    public void setModeleId(Long modeleId) {
        this.modeleId = modeleId;
    }
}