package com.luxsoft.replica

import com.luxsoft.utils.Periodo;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert

import groovy.sql.Sql

/**
 * 
 */
class CancelacionDeSolesNoAtenderService {

    static transactional = false
	
	def dataSourceLookup
	
	def borrarSolesOrigen(){
		
		def suc=[3L:'TACUBA',6L:'ANDRADE',9L:'CF5FEBRERO',5L:'BOLIVAR',2L:'CALLE4',11L:'VERTIZ',13L:'BELLAVISTA']
		
		
//		
			def dia=new Date().day
	   
			println "------------------------------------------------------------------------------------------------------------El dia de la semana es "+dia
			
			if (dia!=7){
				
				
				
				def sucursales=Sucursal.findAllByActivaAndNombreNotEqual('true','OFICINAS')
				sucursales.each{sucursal->
					
					try {
						println "Buscando soles a eliminar"+sucursal.nombre
						def targetSql=new Sql(dataSourceLookup.getDataSource(sucursal.dataSourceName))
						
						def sql="""
			SELECT S.SOL_ID,U.NOMBRE,S.SUCURSAL_ID,S.FECHA,S.ORIGEN_ID FROM sx_solicitud_traslados s left join sx_traslados t on (t.SOL_ID=s.SOL_ID)
			join sw_sucursales u on (s.origen_id=u.sucursal_id)
			where t.TRASLADO_ID is null and U.nombre=? and s.fecha>='2015/04/30' AND S.FECHA< CURRENT_DATE-1  and s.NO_ATENDER is true
            AND S.COMENTARIO NOT LIKE 'CANC%'
		"""
						def rows=targetSql.rows(sql,sucursal.nombre)
				//	def rows=targetSql.rows(sql)
						rows.each{sol->
						
							println "Sol a eliminar"+sol.get("SOL_ID")
							
						  def sqlSolDet=""" SELECT * FROM SX_SOLICITUD_TRASLADOSDET WHERE SOL_ID=?"""
			     def solDet=targetSql.rows(sqlSolDet,sol.get("SOL_ID"));
				 
				 solDet.each{det -> 
					 println "Partida :"+det.get("RENGLON")+" SOL_ID"+det.get("SOL_ID")+ "SUCURSAL_ID "+sol.get("SUCURSAL_ID")+"  "+sol.get("ORIGEN_ID")
				 }
				 
			// Conexion  a la Sucursal Solicitante 	 
				def sucursalSolicitante=Sucursal.findByNombre(suc.get(sol.SUCURSAL_ID))
				 def origenSql=new Sql(dataSourceLookup.getDataSource(sucursalSolicitante.dataSourceName))
				 println "conectando a la sucursal Solicitante" +sucursalSolicitante.nombre+  "..."
				 
				 def solDetOrigen=origenSql.rows(sqlSolDet,sol.SOL_ID)
				
				 solDetOrigen.each{detOri ->
					 println "Partida Origen :"+detOri.get("RENGLON")+" SOL_ID"+detOri.get("SOL_ID")+ "SUCURSAL_ID "+sol.get("SUCURSAL_ID")+" ORIGEN_ID  "+sol.get("ORIGEN_ID")
				 }
				 
			// Conexion a Central	
				 
				 def centralSql=new Sql(dataSourceLookup.getDataSource("oficinasDataSource"))
				 println "conectando a  OFICINAS..."
				  
								 
			//	 def deletePartidas="DELETE FROM SX_SOLICITUD_TRASLADOSDET WHERE SOL_ID=?"
			//	 def  deleteMaestro="DELETE FROM SX_SOLICITUD_TRASLADOS WHERE SOL_ID=?"
				def cancelacionMaestro="UPDATE SX_SOLICITUD_TRASLADOS SET COMENTARIO = 'CANCELACION AUTOMATICA' WHERE SOL_ID=?"
				 
				  
				 println "Borrando en la Sucursal que atiende"+sucursal.nombre 
				 
				 	targetSql.execute(cancelacionMaestro,sol.SOL_ID)
				   // targetSql.execute( deletePartidas,sol.SOL_ID)
					//targetSql.execute(deleteMaestro, sol.SOL_ID)
				 
				 println "Borrando en la sucursal que solicita"+sucursalSolicitante.nombre
				 
				    origenSql.execute(cancelacionMaestro,sol.SOL_ID)
				 //	origenSql.execute( deletePartidas,sol.SOL_ID)
				//	origenSql.execute(deleteMaestro, sol.SOL_ID)
				 
				 println "Borrando en Central - Oficinas"
				    centralSql.execute(cancelacionMaestro,sol.SOL_ID)
				 //	centralSql.execute( deletePartidas,sol.SOL_ID)
				//	centralSql.execute(deleteMaestro, sol.SOL_ID)
				 
			}
					} catch (Exception e) {
						e.printStackTrace()
					}
				
		}
			}
		
			
	}

  	
}

