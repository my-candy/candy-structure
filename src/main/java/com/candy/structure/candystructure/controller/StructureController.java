package com.candy.structure.candystructure.controller;

import com.candy.structure.candystructure.domain.db.StructureTable;
import com.candy.structure.candystructure.service.StructureService;
import com.candy.structure.candystructure.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class StructureController {

    @Resource(name = "structureService")
    private StructureService structureService;

    /**
     * 查询某一个catalog下的所有表信息
     * @param catalog
     * @param schemaPattern
     * @param type
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getTables/{catalog}", method = RequestMethod.GET)
    public ResponseEntity<Result> getTables(@PathVariable String catalog, String schemaPattern,String[] type, HttpServletRequest request, HttpServletResponse response){
        Result result = structureService.getTables(catalog,schemaPattern,null,type);
        if(result.isSuccess()){
            return ResponseEntity.ok(result);
        }else{
            return ResponseEntity.status(HttpStatus.resolve(result.getResultCode())).body(null);
        }
    }

    /**
     * 查找某一个catalog下的表名为xxx的表
     * @param catalog
     * @param tableNamePattem
     * @param schemaPattern
     * @param type
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getTables/{catalog}/{tableNamePattem}", method = RequestMethod.GET)
    public ResponseEntity<Result> getTables(@PathVariable String catalog, @PathVariable String tableNamePattem, String schemaPattern,String[] type, HttpServletRequest request, HttpServletResponse response){
        Result result = structureService.getTables(catalog,schemaPattern,tableNamePattem,null);
        if(result.isSuccess()){
            return ResponseEntity.ok(result);
        }else{
            return ResponseEntity.status(HttpStatus.resolve(result.getResultCode())).body(null);
        }
    }

    /**
     * 查询某一个数据库下的某一张表的列信息
     * @param catalog
     * @param schemaPattern
     * @param tableNamePattern
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getColumns/{catalog}/{tableNamePattern}", method = RequestMethod.GET)
    public ResponseEntity<Result> getColumns(@PathVariable String catalog, @PathVariable String tableNamePattern, String schemaPattern, HttpServletRequest request, HttpServletResponse response){
        Result result = structureService.getColumns(catalog,schemaPattern,tableNamePattern,null);
        if(result.isSuccess()){
            return ResponseEntity.ok(result);
        }else{
            return ResponseEntity.status(HttpStatus.resolve(result.getResultCode())).body(null);
        }
    }


//    /**
//     *
//     * @param catalog
//     * @param schemaPattern
//     * @param tableNamePattern
//     * @param types
//     * @param request
//     * @param response
//     * @return
//     */
//    @RequestMapping(value = "/getTables/{catalog}", method = RequestMethod.GET)
//    public ResponseEntity<Result> getTables(@PathVariable String catalog, String schemaPattern, String tableNamePattern, String types[], HttpServletRequest request, HttpServletResponse response){
//        Result result = structureService.getTables(catalog,schemaPattern,tableNamePattern,types);
//        return ResponseEntity.status(HttpStatus.resolve(result.getResultCode())).body(result);
//    }
//
//    @RequestMapping(value = "/getColumns/{catalog}/{tableName}", method = RequestMethod.GET)
//    public ResponseEntity<Result> getColumns(@PathVariable String catalog, @PathVariable String tableName, String schemaPattern, String tableNamePattern, String columnNamePattern, HttpServletRequest request, HttpServletResponse response){
//        Result result = structureService.getColumns(catalog,schemaPattern,tableNamePattern,columnNamePattern);
//        return ResponseEntity.status(HttpStatus.resolve(result.getResultCode())).body(result);
//    }
}
