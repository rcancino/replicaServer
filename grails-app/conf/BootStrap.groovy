import com.luxor.security.*
import com.luxsoft.replica.Sucursal
import com.luxsoft.replica.*

class BootStrap {

    def init = { servletContext ->
    	development{
    		def admin=User.findByUsername('admin')
			if(!admin){
					admin=new User(username:"admin"
						,password:"admin"
						,enabled:true).save(flush:true);
			}
    	}

    	production{
    		def admin=User.findByUsername('admin')
			if(!admin){
				admin=new User(username:"admin"
				,password:"siipapweb"
				,enabled:true).save(flush:true);
			}
    	}
		def oficinas=Sucursal.findOrSaveWhere(nombre:'OFICINAS',dataSourceName:'oficinasDataSource')
		def tacuba=Sucursal.findOrSaveWhere(nombre:"TACUBA",dataSourceName:'tacubaDataSource')
		def andrade=Sucursal.findOrSaveWhere(nombre:"ANDRADE",dataSourceName:'andradeDataSource')
		def bolivar=Sucursal.findOrSaveWhere(nombre:"BOLIVAR",dataSourceName:'bolivarDataSource')
		def calle4=Sucursal.findOrSaveWhere(nombre:"CALLE4",dataSourceName:'calle4DataSource')
		def cincoFebrero=Sucursal.findOrSaveWhere(nombre:"CF5FEBRERO",dataSourceName:'cincoFebreroDataSource')
		def vertiz=Sucursal.findOrSaveWhere(nombre:"VERTIZ",dataSourceName:'vertizDataSource')
		//def bellavista=Sucursal.findOrSaveWhere(nombre:"BELLAVISTA",dataSourceName:'bellavistaDataSource')
		def solis=Sucursal.findOrSaveWhere(nombre:"SOLIS",dataSourceName:'solisDataSource')
		
		
    }
	
    def destroy = {
    }
	
	//def insertarReplicaConfig(){}
}
