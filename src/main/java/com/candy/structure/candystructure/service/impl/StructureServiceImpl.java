package com.candy.structure.candystructure.service.impl;

import com.candy.structure.candystructure.domain.db.StructureColumns;
import com.candy.structure.candystructure.domain.db.StructureTable;
import com.candy.structure.candystructure.domain.db.enums.StructureColumnsEnum;
import com.candy.structure.candystructure.domain.db.enums.StructureTableEnum;
import com.candy.structure.candystructure.service.StructureService;
import com.candy.structure.candystructure.utils.Result;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component("structureService")
public class StructureServiceImpl implements StructureService {

    @Resource(name = "dataSource")
    private DataSource dataSource;

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public Result getTables(String catalog, String schemaPattern, String tableNamePattern, String types[]) {

        Result result = new Result(false);
        List<StructureTable> structureTableList = new ArrayList<StructureTable>();
        try {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            ResultSet resultSet = metaData.getTables(catalog,schemaPattern,tableNamePattern,types);
            while(resultSet.next()){
                StructureTable structureTable = new StructureTable();
                structureTable.setTableCat(resultSet.getString(StructureTableEnum.TABLE_CAT.getName()));
                structureTable.setTableSchem(resultSet.getString(StructureTableEnum.TABLE_SCHEM.getName()));
                structureTable.setTableName(resultSet.getString(StructureTableEnum.TABLE_NAME.getName()));
                structureTable.setTableType(resultSet.getString(StructureTableEnum.TABLE_TYPE.getName()));
                structureTable.setTypeCat(resultSet.getString(StructureTableEnum.TYPE_CAT.getName()));
                structureTable.setTypeName(resultSet.getString(StructureTableEnum.TYPE_NAME.getName()));
                structureTable.setSelfReferencingColName(resultSet.getString(StructureTableEnum.SELF_REFERENCING_COL_NAME.getName()));
                structureTable.setRefGeneRation(resultSet.getString(StructureTableEnum.REF_GENERATION.getName()));
                structureTable.setRemarks(resultSet.getString(StructureTableEnum.REMARKS.getName()));
                structureTableList.add(structureTable);
            }
            if (structureTableList.isEmpty()){
                result.setResultCode(404);
            }else{
                result.setSuccess(true);
                result.addDefaultModel(structureTableList);
            }

        } catch (SQLException e) {
            result.setResultCode(500);
            result.setErrorMessage("获取表格信息错误！");
            LOGGER.error("获取表格信息错误",e);
        }
        return result;
    }

    @Override
    public Result getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern){
        Result result = new Result(false);
        List<StructureColumns> structureColumnsList = new ArrayList<StructureColumns>();
        try {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            ResultSet resultSet = metaData.getColumns(catalog,schemaPattern,tableNamePattern,columnNamePattern);

            while(resultSet.next()){
                StructureColumns structureColumns = new StructureColumns();
                structureColumns.setColumnName(resultSet.getString(StructureColumnsEnum.COLUMN_NAME.getName()));
                structureColumns.setTableName(resultSet.getString(StructureColumnsEnum.TABLE_NAME.getName()));
                structureColumnsList.add(structureColumns);
            }
            if (structureColumnsList.isEmpty()){
                result.setResultCode(404);
            }else{
                result.setSuccess(true);
                result.addDefaultModel(structureColumnsList);
            }

        } catch (SQLException e) {
            result.setResultCode(500);
            result.setErrorMessage("获取表格信息错误！");
            LOGGER.error("获取表格信息错误",e);
        }
        return result;    }
}