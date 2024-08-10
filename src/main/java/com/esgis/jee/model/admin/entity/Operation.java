package com.esgis.jee.model.admin.entity;

/***********************************************************************
 * Module:  Operation.java
 * Author:  EL Capitain
 * Purpose: Defines the Class Operation
 ***********************************************************************/

public enum Operation {
   CREATE("Créer"),
   READ("Lire"),
   UPDATE("Modifier"),
   DELETE("Supprimer"),
   PRINT("Imprimer"),
   EXPORT("Exporter"),
   IMPORT("Importer"),
   ALL("Tous");
   
   private final String label;
    
    private Operation(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getCode() {
        return super.toString();
    }
    
    @Override
    public String toString() {
        return label;
    }
}