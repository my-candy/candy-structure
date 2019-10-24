package com.candy.structure.candystructure.service.impl;

import com.candy.structure.candystructure.domain.db.StructureColumns;
import com.candy.structure.candystructure.domain.db.StructureTable;
import com.candy.structure.candystructure.domain.db.enums.StructureColumnsEnum;
import com.candy.structure.candystructure.domain.db.enums.StructureTableEnum;
import com.candy.structure.candystructure.service.StructureService;
import com.candy.structure.candystructure.utils.Result;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return result;
    }

    @Override
    public Result getDiff(String sourceCataLog,String targetCataLog){
        Result result = new Result();
        try {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            DatabaseMetaData tagetMetaData = dataSource.getConnection().getMetaData();

            List<StructureTable> sourceList = getTables(metaData,sourceCataLog,null,null,new String[]{"table"});
            List<StructureTable> targetList = getTables(metaData,targetCataLog,null,null,new String[]{"table"});
            //查找数据库中的表格是否一致
            Map<String,List<StructureTable>> diffTableList = getDiffTable(sourceCataLog,targetCataLog,sourceList,targetList);
            result.addDefaultModel("diffTable",diffTableList);

            result.setSuccess(true);
            Map<String,Map<String,StructureColumns>> sourceColumnsMap = getColumnsMap(sourceList,metaData,sourceCataLog);
            Map<String,Map<String,StructureColumns>> targetColumnsMap = getColumnsMap(targetList,metaData,targetCataLog);

            //查找表格中的列是否一致
            Map<String,Map<String,List<StructureColumns>>> diffColumnsMap = getDiffColumns(sourceCataLog,targetCataLog,sourceColumnsMap,targetColumnsMap);
            result.addDefaultModel("diffColumns",diffColumnsMap);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    Map<String,List<StructureTable>> getDiffTable(String sourceCataLog,String targetCataLog,List<StructureTable> sourceList,List<StructureTable> targetList){
        Map<String,List<StructureTable>> map = new HashMap<>();
        List<StructureTable> sourceDiffTable = new ArrayList<>();
        List<StructureTable> targetDiffTable = new ArrayList<>();

        //当原始数据库，和目标数据库都不为空时候开始比较
        if((sourceList!=null&&sourceList.size()>0)&&(targetList!=null&&targetList.size()>0)){
            targetDiffTable = getDiffTableDetail(sourceList,targetList);
            sourceDiffTable = getDiffTableDetail(targetList,sourceList);
        }
        map.put(sourceCataLog,sourceDiffTable);
        map.put(targetCataLog,targetDiffTable);
        return map;
    }

    Map<String,Map<String,List<StructureColumns>>> getDiffColumns(String sourceCataLog, String targetCataLog, Map<String, Map<String,StructureColumns>> sourceColumnsMap, Map<String, Map<String,StructureColumns>> targetColumnsMap){
        Map<String,List<StructureColumns>> sourceColumnsDiffMap = new HashMap();
        Map<String,List<StructureColumns>> targetColumnsDiffMap = new HashMap();
        sourceColumnsMap.entrySet().stream().forEach(entry->{
            Map<String,StructureColumns> sourceMap =  entry.getValue();
            Map<String,StructureColumns> targetMap = targetColumnsMap.get(entry.getKey());
            if((sourceMap!=null&&sourceMap.size()>0)&&(targetMap!=null&&targetMap.size()>0)){
                List<StructureColumns> targetcolumnsList = getDiffColumnsDatil( sourceCataLog,  targetCataLog,sourceMap,targetMap);
                List<StructureColumns> sourceColumnsList = getDiffColumnsDatil(sourceCataLog,  targetCataLog,targetMap,sourceMap);
                if(targetcolumnsList!=null&&targetcolumnsList.size()>0){
                    sourceColumnsDiffMap.put(entry.getKey(),targetcolumnsList);
                }
                if(sourceColumnsList!=null&&sourceColumnsList.size()>0){
                    targetColumnsDiffMap.put(entry.getKey(),sourceColumnsList);
                }
            }
        });
        Map<String,Map<String,List<StructureColumns>>> diffColumnsMap = new HashMap<>();
        diffColumnsMap.put(sourceCataLog,sourceColumnsDiffMap);
        diffColumnsMap.put(targetCataLog,targetColumnsDiffMap);
        return diffColumnsMap;
    }

    private List<StructureColumns> getDiffColumnsDatil(String sourceCataLog, String targetCataLog,Map<String,StructureColumns> sourceColumnMap,Map<String,StructureColumns> targetColumnMap){
        List<StructureColumns> diffColumnsList = new ArrayList<>();
        sourceColumnMap.entrySet().stream().forEach(entry->{
            //对吧表中的字段，如果没有取到，说明target中不包含该字段
            StructureColumns targetColumns = targetColumnMap.get(entry.getKey());
            if(targetColumns==null){
                diffColumnsList.add(entry.getValue());
                entry.getValue().setRemarks(sourceCataLog+"-->"+entry.getValue().getTableName()+"-->"+entry.getValue().getColumnName()+"字段不存在");
            }else if(!targetColumns.getColumnSize().equals(entry.getValue().getColumnSize())){
                targetColumns.setRemarks(sourceCataLog+"-->"+targetColumns.getTableName()+"-->"+targetColumns.getColumnName()+"-->"+targetColumns.getColumnSize()+"字段长度不相等");
                diffColumnsList.add(targetColumns);
            }
        });
        return diffColumnsList;
    }

    private List<StructureTable> getTables(DatabaseMetaData metaData  ,String catalog, String schemaPattern, String tableNamePattern, String types[]) {
        List<StructureTable> structureTableList = new ArrayList<StructureTable>();
        try {
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

        } catch (SQLException e) {
            LOGGER.error("获取表格信息错误",e);
        }
        return structureTableList;
    }

    private List<StructureColumns> getColumns(DatabaseMetaData metaData ,String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern){
        List<StructureColumns> structureColumnsList = new ArrayList<StructureColumns>();
        try {
            ResultSet resultSet = metaData.getColumns(catalog,schemaPattern,tableNamePattern,columnNamePattern);

            while(resultSet.next()){
                StructureColumns structureColumns = new StructureColumns();
                structureColumns.setColumnName(resultSet.getString(StructureColumnsEnum.COLUMN_NAME.getName()));
                structureColumns.setTableName(resultSet.getString(StructureColumnsEnum.TABLE_NAME.getName()));
                structureColumns.setBufferLength(resultSet.getString(StructureColumnsEnum.BUFFER_LENGTH.getName()));
                structureColumns.setColumnSize(resultSet.getString(StructureColumnsEnum.COLUMN_SIZE.getName()));
                structureColumns.setCharOctetLength(resultSet.getString(StructureColumnsEnum.CHAR_OCTET_LENGTH.getName()));
                structureColumns.setTableCat(resultSet.getString(StructureColumnsEnum.TABLE_CAT.getName()));
                structureColumns.setColumnDef(resultSet.getString(StructureColumnsEnum.COLUMN_DEF.getName()));
                structureColumnsList.add(structureColumns);
            }
        } catch (SQLException e) {
            LOGGER.error("获取表格信息错误",e);
        }
        return structureColumnsList;
    }

    Map<String, Map<String, StructureColumns>> getColumnsMap(List<StructureTable> tableList, DatabaseMetaData metaData, String sourceCataLog){
        Map<String,Map<String,StructureColumns>> sourceColumnsMap = new HashMap<>();
        if(tableList!=null&&tableList.size()>0){
            tableList.stream().forEach(table -> {
                List<StructureColumns> columnsList = getColumns(metaData,sourceCataLog,null,table.getTableName(),null);
                Map<String,StructureColumns> columnsMap = new HashMap<>();
                if(columnsList!=null&&columnsList.size()>0){
                    columnsList.stream().forEach(columns->{
                        columnsMap.put(columns.getColumnName(),columns);
                    });
                    sourceColumnsMap.put(table.getTableName(),columnsMap);
                }
            });
        }
        return sourceColumnsMap;
    }

    private List<StructureTable> getDiffTableDetail(List<StructureTable> source,List<StructureTable> target){
        Map<String,StructureTable> targetTableMap = new HashMap<>();
        List<StructureTable> diffTableList = new ArrayList<>();
        if(target!=null && target.size()>0){
            target.stream().forEach(table->{
                targetTableMap.put(table.getTableName(),table);
            });
        }

        source.stream().forEach(table->{
            StructureTable structureTable = targetTableMap.get(table.getTableName());
            if(structureTable==null){
                diffTableList.add(table);
            }
        });
        return diffTableList;
    }
}