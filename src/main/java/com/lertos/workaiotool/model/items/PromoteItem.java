package com.lertos.workaiotool.model.items;

import com.lertos.workaiotool.model.EnumWithLabel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PromoteItem promoteItem = (PromoteItem) o;

        return Objects.equals(description, promoteItem.description) & Objects.equals(pathType, promoteItem.pathType) & Objects.equals(promoteType, promoteItem.promoteType) & Objects.equals(fileNames, promoteItem.fileNames) & Objects.equals(originPaths, promoteItem.originPaths) & Objects.equals(destinationPaths, promoteItem.destinationPaths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, pathType, promoteType, fileNames, originPaths, destinationPaths);
    }

    public enum PathTypes implements EnumWithLabel {
        PROVIDE_FILE_NAMES_IN_PATHS("Provide File Names In Paths"), PROVIDE_FILE_NAMES_SEPARATELY("Provide File Names Separately");

        final String label;

        public String getLabel() {
            return label;
        }

        PathTypes(String label) {
            this.label = label;
        }
    }

    public enum PromoteType implements EnumWithLabel {
        COPY("Copy"), MOVE("Move");

        final String label;

        public String getLabel() {
            return label;
        }

        PromoteType(String label) {
            this.label = label;
        }
    }
}
