package com.candy.structure.candystructure.domain.db.enums;

public enum StructureColumnsEnum {

    TABLE_CAT("TABLE_CAT"),
    TABLE_SCHEM("TABLE_SCHEM"),
    TABLE_NAME("TABLE_NAME"),
    COLUMN_NAME("COLUMN_NAME"),
    DATA_TYPE("DATA_TYPE"),
    TYPE_NAME("TYPE_NAME"),
    COLUMN_SIZE("COLUMN_SIZE"),
    BUFFER_LENGTH("BUFFER_LENGTH"),
    DECIMAL_DIGITS("DECIMAL_DIGITS"),
    NUM_PREC_RADIX("NUM_PREC_RADIX"),
    NULLABLE("NULLABLE"),
    REMARKS("REMARKS"),
    COLUMN_DEF("COLUMN_DEF"),
    SQL_DATA_TYPE("SQL_DATA_TYPE"),
    SQL_DATETIME_SUB("SQL_DATETIME_SUB"),
    CHAR_OCTET_LENGTH("CHAR_OCTET_LENGTH"),
    ORDINAL_POSITION("ORDINAL_POSITION"),
    IS_NULLABLE("IS_NULLABLE");

    private String name;

    private StructureColumnsEnum(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public static void main(String[] args) {
        for(StructureColumnsEnum structureColumnsEnum:StructureColumnsEnum.values()){
            System.out.println(structureColumnsEnum.getName());

        }
    }
}
