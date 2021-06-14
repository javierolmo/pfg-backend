package com.javi.uned.pfgbackend.domain.sheet.model;

import com.javi.uned.pfgbackend.domain.exceptions.UnavailableResourceException;

public class Availability {

    private boolean specs;
    private boolean xml;
    private boolean pdf;

    public Availability(boolean specs, boolean xml, boolean pdf) {
        this.specs = specs;
        this.xml = xml;
        this.pdf = pdf;
    }

    public boolean isSpecs() {
        return specs;
    }

    public boolean isXml() {
        return xml;
    }

    public boolean isPdf() {
        return pdf;
    }

    public void checkAvailability() throws UnavailableResourceException {
        if (!pdf) throw new UnavailableResourceException("PDF not available");
        if (!xml) throw new UnavailableResourceException("MusicXML not available");
        if (!specs) throw new UnavailableResourceException("Specs not available");
    }
}
