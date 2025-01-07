package com.genio.model;

import jakarta.persistence.*;
@Entity
public class Historisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) // Relation obligatoire
    @JoinColumn(name = "convention_id", nullable = false)
    private Convention convention;

    private String status;
    private String timestamp;
    private String details;

    @Lob
    private byte[] docxBinaire;

    @Lob
    private byte[] fluxJsonBinaire;

    @Lob
    private byte[] pdfBinaire;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    public void setDetails(String details) {
        this.details = details;
    }

    public void setDocxBinaire(byte[] docxBinaire) {
        this.docxBinaire = docxBinaire;
    }

    public void setFluxJsonBinaire(byte[] fluxJsonBinaire) {
        this.fluxJsonBinaire = fluxJsonBinaire;
    }

    public void setConvention(Convention convention) {
        this.convention = convention;
    }
}