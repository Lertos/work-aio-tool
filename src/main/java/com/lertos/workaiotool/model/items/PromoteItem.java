package com.lertos.workaiotool.model.items;

import java.io.Serializable;
import java.util.ArrayList;


public class PromoteItem implements Serializable {

    private String description;
    private PathTypes pathType;
    private PromoteType promoteType;
    private ArrayList<String> fileNames = new ArrayList<>();
    private ArrayList<String> originPaths = new ArrayList<>();
    private ArrayList<String> destinationPaths = new ArrayList<>();

    public PromoteItem(String description, PathTypes pathType, PromoteType promoteType) {
        this.description = description;
        this.pathType = pathType;
        this.promoteType = promoteType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PathTypes getPathType() {
        return pathType;
    }

    public void setPathType(PathTypes pathType) {
        this.pathType = pathType;
    }

    public PromoteType getPromoteType() {
        return promoteType;
    }

    public void setPromoteType(PromoteType promoteType) {
        this.promoteType = promoteType;
    }

    public ArrayList<String> getFileNames() {
        return fileNames;
    }

    public ArrayList<String> getOriginPaths() {
        return originPaths;
    }

    public ArrayList<String> getDestinationPaths() {
        return destinationPaths;
    }

    public enum PathTypes implements EnumWithLabel {
        PROVIDE_FILE_NAMES_IN_PATHS("Provide File Names In Paths"),
        PROVIDE_FILE_NAMES_SEPARATELY("Provide File Names Separately");

        final String label;

        public String getLabel() {
            return label;
        }

        PathTypes(String label) {
            this.label = label;
        }
    }

    public enum PromoteType implements EnumWithLabel {
        COPY("Copy"),
        MOVE("Move");

        final String label;

        public String getLabel() {
            return label;
        }

        PromoteType(String label) {
            this.label = label;
        }
    }

    public interface EnumWithLabel {
        String getLabel();
    }
}
