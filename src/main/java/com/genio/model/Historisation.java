package com.genio.model;

import jakarta.persistence.*;
import java.util.Date;


@Table(name = "historisation")
@Entity
public class Historisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = true)
    @JoinColumn(name = "convention_id", nullable = true)
    private Convention convention;

    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp", nullable = false)
    private Date timestamp;

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    @Lob
    @Column(name = "docx_binaire", columnDefinition = "LONGBLOB")
    private byte[] docxBinaire;

    @Lob
    @Column(name = "flux_json_binaire", columnDefinition = "LONGBLOB")
    private byte[] fluxJsonBinaire;

    @Lob
    @Column(name = "pdf_binaire", columnDefinition = "LONGBLOB")
    private byte[] pdfBinaire;

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Convention getConvention() {
        return convention;
    }

    public String getStatus() {
        return status;
    }

    public void setTimestamp() {
        this.timestamp = new Date();
    }

    public String getDetails() {
        return details;
    }

    public byte[] getDocxBinaire() {
        return docxBinaire;
    }

    public byte[] getFluxJsonBinaire() {
        return fluxJsonBinaire;
    }

    public byte[] getPdfBinaire() {
        return pdfBinaire;
    }

    public void setPdfBinaire(byte[] pdfBinaire) {
        this.pdfBinaire = pdfBinaire;
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

    public Date getTimestamp() {
        return timestamp;
    }

}