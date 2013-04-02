package com.luxsoft.replica

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.jdbc.core.JdbcTemplate

import spock.lang.Shared;
import spock.lang.Unroll;
import grails.plugin.spock.IntegrationSpec
import groovy.sql.Sql

/**
 * Integration test para la importacion de registros AuditLog
 * 
 * @author Ruben Cancino Ramos
 *
 */
class ImportarAuditLogsJobIntegrationSpec extends IntegrationSpec {
	
	def grailsApplication
	boolean transactional = false
	def jobLauncher
	def importacionDeAuditLogsJob
	def dataSource
	
	
	@Unroll
	void "Validar fuentes de datos"() {
		expect:
		grailsApplication.mainContext.getBean('tacubaDataSource')
	}
	
	def "Validar SpringBatch infraestructure"(){
		expect:"Instancia de JobLauncher inicializada e injectada"
		jobLauncher
		importacionDeAuditLogsJob
		importacionDeAuditLogsJob instanceof Job
	}
	
	@Unroll
	def "Tablas requeridas para Spring Batch"() {
		given:
		def sql = new Sql(dataSource)
		
		expect:
		sql.execute("select count(*) from $table;".toString())
		
		where:
		table << ['BATCH_JOB_EXECUTION', 'BATCH_JOB_EXECUTION_CONTEXT', 'BATCH_JOB_INSTANCE', 'BATCH_JOB_PARAMS','BATCH_STEP_EXECUTION', 'BATCH_STEP_EXECUTION_CONTEXT']
	}
	
	/*
	def "Importar registros"(){
		given:"Sin registros en produccion"
		JdbcTemplate jdbcTemplate=new JdbcTemplate(dataSource)
		
		
		when:"Lanzamos el job"
		jdbcTemplate.execute("DELETE FROM AUDIT_LOG")
		jobLauncher.run(importacionDeAuditLogsJob
			, new JobParametersBuilder()
			.addLong("timestamp", System.currentTimeMillis())
			.addString("sourceDataSource", "tacubaDataSource")
			.toJobParameters()
			)
		then:"Los registros son importados"
		jdbcTemplate.queryForInt("select count(1) from AUDIT_LOG")>0
	}
	*/
	
}