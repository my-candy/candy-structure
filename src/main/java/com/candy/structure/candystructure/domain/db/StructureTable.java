package com.candy.structure.candystructure.domain.db;

import java.util.List;

public class StructureTable {



    private String tableCat;

    private String tableSchem;

    private String tableName;

    private String tableType;

    private String typeCat;

    private String typeSchem;

    private String typeName;

    private String selfReferencingColName;

    private String refGeneRation;

    private String remarks;

    private List<StructureKey> tableKey;

    private List<StructureColumns> columns;

    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(String tableCat) {
        this.tableCat = tableCat;
    }

    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(String tableSchem) {
        this.tableSchem = tableSchem;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<StructureKey> getTableKey() {
        return tableKey;
    }

    public void setTableKey(List<StructureKey> tableKey) {
        this.tableKey = tableKey;
    }

    public List<StructureColumns> getColumns() {
        return columns;
    }

    public void setColumns(List<StructureColumns> columns) {
        this.columns = columns;
    }

    public String getTypeCat() {
        return typeCat;
    }

    public void setTypeCat(String typeCat) {
        this.typeCat = typeCat;
    }

    public String getTypeSchem() {
        return typeSchem;
    }

    public void setTypeSchem(String typeSchem) {
        this.typeSchem = typeSchem;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSelfReferencingColName() {
        return selfReferencingColName;
    }

    public void setSelfReferencingColName(String selfReferencingColName) {
        this.selfReferencingColName = selfReferencingColName;
    }

    public String getRefGeneRation() {
        return refGeneRation;
    }

    public void setRefGeneRation(String refGeneRation) {
        this.refGeneRation = refGeneRation;
    }
}
