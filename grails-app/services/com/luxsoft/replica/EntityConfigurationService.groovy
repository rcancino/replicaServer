package com.luxsoft.replica

import groovy.sql.Sql
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * EntityConfigurationService
 * A service class encapsulates the core business logic of a Grails application
 */
class EntityConfigurationService {

    static transactional = false

    def crearConfiguracion(def name,def table,def ds){
		SimpleJdbcInsert insert=new SimpleJdbcInsert(ds).withTableName(table)
		def sql=new Sql(ds)
		insert.compile()
		def config=new EntityConfiguration(name:name,tableName:table)
		config.updateSql=resolverUdateQuery(sql,config.tableName)
		config.insertSql=insert.getInsertString()
		config.pk=resolverPk(sql, config.tableName)
		if("SX_VENTAS"==table){
			config.excludeInsertColumns="PEDIDO_ID"
			config.excludeUpdateColumns="PEDIDO_ID"
		}
		config.save(failOnError:true)
	}
}
