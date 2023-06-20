package com.lertos.workaiotool.model;

import java.io.Serializable;
import java.util.Objects;

public class TodoItem implements Serializable {

    private boolean isDone;
    private String description;

    public TodoItem(boolean isDone, String description) {
        this.isDone = isDone;
        this.description = description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        TodoItem todoItem = (TodoItem) o;

        return Objects.equals(isDone, todoItem.isDone) && Objects.equals(description, todoItem.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isDone, description);
    }
}
