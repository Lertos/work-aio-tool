package com.lertos.workaiotool.model.items;

import com.lertos.workaiotool.model.EnumWithLabel;
import com.lertos.workaiotool.model.ItemSQL;

import java.io.Serializable;

public class SQLCompareItem implements Serializable {

    private String description;
    private String procedureName;
    private SQLType sqlType;
    private ItemSQL itemSQL;

    public SQLCompareItem(String description, String procedureName, SQLType sqlType, ItemSQL itemSQL) {
        this.description = description;
        this.procedureName = procedureName;
        this.sqlType = sqlType;
        this.itemSQL = itemSQL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public SQLType getSqlType() {
        return sqlType;
    }

    public void setSqlType(SQLType sqlType) {
        this.sqlType = sqlType;
    }

    public ItemSQL getItemSQL() {
        return itemSQL;
    }

    public void setItemSQL(ItemSQL itemSQL) {
        this.itemSQL = itemSQL;
    }

    public enum SQLType implements EnumWithLabel {
        MYSQL("MySQL"),
        TRANSACT_SQL("Transact SQL");

        final String label;

        SQLType(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }
}
