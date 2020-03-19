package com.candy.structure.candystructure;

import com.candy.structure.candystructure.domain.db.StructureTable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableDiscoveryClient
public class CandyStructureApplication {

    public static void main(String[] args) {
        SpringApplication.run(CandyStructureApplication.class, args);
    }


    @RequestMapping(value = "/getTable", method = RequestMethod.GET)
    public ResponseEntity getTest() {
        StructureTable structureTable = new StructureTable();
        structureTable.setTableName("aaa");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(structureTable);
    }
}


