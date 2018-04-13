package com.candy.structure.candystructure.domain.db.enums;

public enum StructureTableEnum {

    TABLE_CAT("TABLE_CAT"),
    TABLE_SCHEM("TABLE_SCHEM"),
    TABLE_NAME("TABLE_NAME"),
    TABLE_TYPE("TABLE_TYPE"),
    REMARKS("REMARKS"),
    TYPE_CAT("TYPE_CAT"),
    TYPE_SCHEM("TYPE_SCHEM"),
    TYPE_NAME("TYPE_NAME"),
    SELF_REFERENCING_COL_NAME("SELF_REFERENCING_COL_NAME"),
    REF_GENERATION("REF_GENERATION"),
    DEF_TABLE_NAMEPATTERN("%");

    private String name;

    private StructureTableEnum(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }
}
