package com.lertos.workaiotool.model.items;

import java.io.Serializable;
import java.util.ArrayList;


public class PromoteItem implements Serializable {

    private String description;
    private TransferTypes transferType;
    private PathTypes pathType;
    private PromoteType promoteType;
    private ArrayList<String> fileNames;
    private ArrayList<String> originPaths;
    private ArrayList<String> destinationPaths;

    public PromoteItem(String description, TransferTypes transferType, PathTypes pathType, PromoteType promoteType) {
        this.description = description;
        this.transferType = transferType;
        this.pathType = pathType;
        this.promoteType = promoteType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TransferTypes getTransferType() {
        return transferType;
    }

    public void setTransferType(TransferTypes transferType) {
        this.transferType = transferType;
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

    public void setFileNames(ArrayList<String> fileNames) {
        this.fileNames = fileNames;
    }

    public ArrayList<String> getOriginPaths() {
        return originPaths;
    }

    public void setOriginPaths(ArrayList<String> originPaths) {
        this.originPaths = originPaths;
    }

    public ArrayList<String> getDestinationPaths() {
        return destinationPaths;
    }

    public void setDestinationPaths(ArrayList<String> destinationPaths) {
        this.destinationPaths = destinationPaths;
    }

    public enum TransferTypes {
        FILES_WITH_SAME_NAMES_DIFFERENT_PLACES,
        RAW_ABSOLUTE_PATHS
    }

    public enum PathTypes {
        BOTH_UNKNOWN,
        ORIGIN_KNOWN_DEST_UNKNOWN,
        ORIGIN_UNKNOWN_DEST_KNOWN,
        BOTH_KNOWN
    }

    public enum PromoteType {
        COPY,
        MOVE
    }
}
