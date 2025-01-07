package com.genio.dto.input;

import com.genio.dto.*;
import jakarta.validation.constraints.NotNull;

public class ConventionServiceDTO {

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

        private String sujetDuStage;


        public EtudiantDTO getEtudiant() {
            return etudiant;
        }

        public void setEtudiant(EtudiantDTO etudiant) {
            this.etudiant = etudiant;
        }

        public TuteurDTO getTuteur() {
            return tuteur;
        }

        public void setTuteur(TuteurDTO tuteur) {
            this.tuteur = tuteur;
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

        public EnseignantDTO getEnseignant() {
            return enseignant;
        }

        public void setEnseignant(EnseignantDTO enseignant) {
            this.enseignant = enseignant;
        }

        public ConventionDTO getConvention() {
            return convention;
        }

        public void setConvention(ConventionDTO convention) {
            this.convention = convention;
        }

        public String getAnnee() {
            return annee;
        }

        public void setAnnee(String annee) {
            this.annee = annee;
        }

        public Long getModeleId() {
            return modeleId;
        }

        public void setModeleId(Long modeleId) {
            this.modeleId = modeleId;
        }
}

