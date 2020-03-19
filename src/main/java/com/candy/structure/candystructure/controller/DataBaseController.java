package com.candy.structure.candystructure.controller;

import com.candy.structure.candystructure.service.StructureService;
import com.candy.structure.candystructure.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class DataBaseController {

    @Resource(name = "structureService")
    private StructureService structureService;

    @RequestMapping(value = "/catalogs", method = RequestMethod.GET)
    public ResponseEntity<Result> getCatalogs(HttpServletRequest request, HttpServletResponse response){
        Result result = structureService.getCatalogs();
        if(result.isSuccess()){
            return ResponseEntity.ok(result);
        }else{
            return ResponseEntity.status(HttpStatus.resolve(result.getResultCode())).body(null);
        }
    }

    /**
     * 查询某一个catalog下的所有表信息
     * @param catalog
     * @param schemaPattern
     * @param type
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/tables/{catalog}", method = RequestMethod.GET)
    public ResponseEntity<Result> getTables(@PathVariable String catalog, String schemaPattern,String[] type, HttpServletRequest request, HttpServletResponse response){
        Result result = structureService.getTables(catalog,schemaPattern,null,type);
        if(result.isSuccess()){
            return ResponseEntity.ok(result);
        }else{
            result = new Result();
            result.setErrorMessage("aaaaaaaaaaa");
            return ResponseEntity.status(HttpStatus.resolve(404)).body(result);
        }
    }

    /**
     * 查找某一个catalog下的表名为xxx的表
     * @param catalog
     * @param tableNamePattern
     * @param schemaPattern
     * @param type
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/tables/{catalog}/{tableNamePattern}", method = RequestMethod.GET)
    public ResponseEntity<Result> getTables(@PathVariable String catalog, @PathVariable String tableNamePattern, String schemaPattern,String[] type, HttpServletRequest request, HttpServletResponse response){
        Result result = structureService.getColumns(catalog,schemaPattern,tableNamePattern,null);
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
    @RequestMapping(value = "/columns/{catalog}/{tableNamePattern}", method = RequestMethod.GET)
    public ResponseEntity<Result> getColumns(@PathVariable String catalog, @PathVariable String tableNamePattern, String schemaPattern, HttpServletRequest request, HttpServletResponse response){
        Result result = structureService.getColumns(catalog,schemaPattern,tableNamePattern,null);
        if(result.isSuccess()){
            return ResponseEntity.ok(result);
        }else{
            return ResponseEntity.status(HttpStatus.resolve(result.getResultCode())).body(null);
        }
    }


    @RequestMapping(value = "/catalogs/getDiff/{sourceCatalog}/{targetCatalog}", method = RequestMethod.GET)
    public ResponseEntity<Result> getDiff(@PathVariable String sourceCatalog, @PathVariable String targetCatalog, HttpServletRequest request, HttpServletResponse response){
        Result result = structureService.getDiff(sourceCatalog,targetCatalog);
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


    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping(value = "test")
    public String test(){
        List<String> services = discoveryClient.getServices();
        for(String s : services){
            System.out.println(s);
        }
        return "hello spring cloud!";
    }
}
