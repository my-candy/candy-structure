package com.candy.structure.candystructure.service;

import com.candy.structure.candystructure.domain.db.StructureTable;
import com.candy.structure.candystructure.utils.Result;

import java.util.List;

public interface StructureService {

    public Result getTables(String catalog, String schemaPattern, String tableNamePattern, String types[]);

    public Result getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern);

    public Result getDiff(String sourceCataLog,String targetCataLog);

}
