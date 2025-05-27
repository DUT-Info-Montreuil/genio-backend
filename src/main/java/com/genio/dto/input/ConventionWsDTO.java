package com.genio.dto.input;

import com.genio.dto.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ConventionWsDTO {

    @Valid
    @NotNull(message = "L'étudiant ne peut pas être nul.")
    private EtudiantDTO etudiant;


    @Valid
    @NotNull(message = "Le maître de stage ne peut pas être nul.")
    private MaitreDeStageDTO maitreDeStage;


    @Valid
    @NotNull(message = "L'organisme est obligatoire.")
    private OrganismeDTO organisme;


    @Valid
    @NotNull(message = "Les informations du stage sont obligatoires.")
    private StageDTO stage;

    @NotNull
    private TuteurDTO tuteur;


    @NotNull(message = "L'ID du modèle est obligatoire.")
    private Long modeleId;

    public ConventionWsDTO(EtudiantDTO etudiant, MaitreDeStageDTO maitreDeStage, OrganismeDTO organisme, StageDTO stage, TuteurDTO tuteur, Long modeleId) {
        this.etudiant = etudiant;
        this.maitreDeStage = maitreDeStage;
        this.organisme = organisme;
        this.stage = stage;
        this.tuteur = tuteur;
        this.modeleId = modeleId;
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


}