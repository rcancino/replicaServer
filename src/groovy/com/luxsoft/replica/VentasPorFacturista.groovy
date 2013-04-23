package com.luxsoft.replica

import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.luxsoft.utils.Periodo;

import groovy.sql.Sql

class VentasPorFacturista {
	
	def dataSourceLookup
	
	
	def execute(def periodo){
		
		if(!periodo)
			periodo=Periodo.getMesActual()
		//Limpiar periodo
		String DELETE="DELETE FROM SX_VENTAS_FACTURISTA WHERE DATE(FECHA) BETWEEN ? AND ?";
		println DELETE + 'Periodo: '+periodo 
		def ds=dataSourceLookup.getDataSource('oficinasDataSource')
		Sql oficinasSql =new Sql(ds)
		oficinasSql.execute(DELETE,[periodo.fechaInicial.format('yyyy-MM-dd'),periodo.fechaFinal.format('yyyy-MM-dd')])
		
		sucursales.each {suc ->
			def query=VENTAS_X_FAC_SQL
			query=query.replaceAll("@FECHA_INI","\'"+periodo.fechaInicial.format("yyyy-MM-dd")+"\'");
			query=query.replaceAll("@FECHA_FIN","\'"+periodo.fechaFinal.format("yyyy-MM-dd")+"\'");
			query=query.replaceAll("@SUCURSAL_ID", suc.value.toString());
			println query
			def sourceSql=new Sql(dataSourceLookup.getDataSource(suc.key))
			
			//SimpleJdbcInsert insert=new SimpleJdbcInsert(dataSourceLookup.getDataSource(suc.key)).withTableName("SX_VENTAS_FACTURISTA")
			
			def rows=sourceSql.eachRow(query) {row->
				//println 'Insertando: '+row
				String INSERT="""
					INSERT INTO SX_VENTAS_FACTURISTA  
					(FECHA,SUCURSAL_ID,CREADO_USR,FACTURISTA,PED,MOS,CAM,CRE,CANC,FACS,IMPORTE,PART)
			 		VALUES('${row.FECHA}',${row.SUCURSAL_ID},'${row.CREADO_USR}'
					 	,'${row.FACTURISTA}',${row.PED},${row.MOS},${row.CAM},${row.CRE},${row.CANC},${row.FACS},${row.IMPORTE},${row.PART})
				"""
				//println INSERT
				oficinasSql.executeInsert(INSERT)
				
				
				//insert.execute(row)
			}
		}
		
	}
	
	def sucursales=['tacubaDataSource':3L,'andradeDataSource':6L,'cincoFebreroDataSource':9L,'bolivarDataSource':5L,'calle4DataSource':2L,'vertizDataSource':11L]
	
	
	static def VENTAS_X_FAC_SQL="""
	SELECT A.FECHA,A.SUCURSAL_ID,A.CREADO_USR,A.FACTURISTA
	,SUM(CASE WHEN A.TIPO='PED' THEN REG ELSE 0 END) AS PED
	,SUM(CASE WHEN A.TIPO='MOS' THEN REG ELSE 0 END) AS MOS
	,SUM(CASE WHEN A.TIPO='CAM' THEN REG ELSE 0 END) AS CAM
	,SUM(CASE WHEN A.TIPO='CRE' THEN REG ELSE 0 END) AS CRE
	,SUM(CASE WHEN A.TIPO='CNC' THEN REG ELSE 0 END) AS CANC
	,SUM(CASE WHEN A.TIPO IN('MOS','CAM','CRE','CNC') THEN REG ELSE 0 END) AS FACS
	,SUM(A.IMPORTE) AS IMPORTE
	,SUM(CASE WHEN A.TIPO='PRT' THEN REG ELSE 0 END) AS PART
	FROM (
	SELECT 'PED' AS TIPO,P.fecha,P.SUCURSAL_ID,P.CREADO_USR,P.CREADO_USR AS FACTURISTA,COUNT(*) AS REG,0 AS IMPORTE
	FROM sx_pedidos P WHERE P.FECHA BETWEEN @FECHA_INI AND @FECHA_FIN AND P.SUCURSAL_ID=@SUCURSAL_ID GROUP BY P.fecha,P.SUCURSAL_ID,P.CREADO_USR
	UNION
	SELECT 'MOS' AS TIPO,V.fecha,V.SUCURSAL_ID,V.MODIFICADO_USERID,V.MODIFICADO_USERID AS FACTURISTA,COUNT(*) AS REG,SUM(V.IMPORTE) AS IMPORTE
	FROM sx_VENTAS V WHERE V.TIPO='FAC' AND V.FECHA BETWEEN @FECHA_INI AND @FECHA_FIN AND V.ORIGEN='MOS' AND V.SUCURSAL_ID=@SUCURSAL_ID GROUP BY V.fecha,V.SUCURSAL_ID,V.MODIFICADO_USERID
	UNION
	SELECT 'CAM' AS TIPO,V.fecha,V.SUCURSAL_ID,V.MODIFICADO_USERID,V.MODIFICADO_USERID AS FACTURISTA,COUNT(*) AS REG,SUM(V.IMPORTE) AS IMPORTE
	FROM sx_VENTAS V WHERE V.TIPO='FAC' AND V.FECHA BETWEEN @FECHA_INI AND @FECHA_FIN AND V.ORIGEN='CAM' AND V.SUCURSAL_ID=@SUCURSAL_ID GROUP BY V.fecha,V.SUCURSAL_ID,V.MODIFICADO_USERID
	UNION
	SELECT 'CRE' AS TIPO,V.fecha,V.SUCURSAL_ID,V.MODIFICADO_USERID,V.MODIFICADO_USERID AS FACTURISTA,COUNT(*) AS REG,SUM(V.IMPORTE) AS IMPORTE
	FROM sx_VENTAS V WHERE V.TIPO='FAC' AND V.FECHA BETWEEN @FECHA_INI AND @FECHA_FIN AND V.ORIGEN='CRE' AND V.SUCURSAL_ID=@SUCURSAL_ID GROUP BY V.fecha,V.SUCURSAL_ID,V.MODIFICADO_USERID
	UNION
	SELECT 'PRT' AS TIPO,V.fecha,V.SUCURSAL_ID,V.MODIFICADO_USERID,V.MODIFICADO_USERID AS FACTURISTA,COUNT(*) AS REG,0 AS IMPORTE
	FROM sx_VENTASDET D JOIN SX_VENTAS V ON(V.CARGO_ID=D.VENTA_ID) WHERE V.TIPO='FAC' AND V.FECHA BETWEEN @FECHA_INI AND @FECHA_FIN AND V.SUCURSAL_ID=@SUCURSAL_ID GROUP BY V.fecha,V.SUCURSAL_ID,V.MODIFICADO_USERID
	UNION
	SELECT 'CNC' AS TIPO,V.fecha,V.SUCURSAL_ID,V.MODIFICADO_USERID,V.MODIFICADO_USERID AS FACTURISTA,COUNT(*)*-1 AS REG,0 AS IMPORTE
	FROM sx_cxc_cargos_cancelados C JOIN SX_VENTAS V ON(V.CARGO_ID=C.CARGO_ID) WHERE V.FECHA BETWEEN @FECHA_INI AND @FECHA_FIN AND V.SUCURSAL_ID=@SUCURSAL_ID GROUP BY V.fecha,V.SUCURSAL_ID,V.MODIFICADO_USERID
	) A
	GROUP BY A.FECHA,A.SUCURSAL_ID,A.CREADO_USR,A.FACTURISTA
	
	"""


}
