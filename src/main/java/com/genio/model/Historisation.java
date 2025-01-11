package com.genio.model;

import jakarta.persistence.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;

@Table(name = "historisation")
@Entity
public class Historisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "convention_id", nullable = false)
    private Convention convention;

    private String status;

    @Column(name = "timestamp", nullable = false)
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
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.timestamp = sdf.format(new Date(timestamp.getTime()));
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
}