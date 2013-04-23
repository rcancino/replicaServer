package com.luxsoft.utils


class Periodo {
	
	Date fechaInicial
	Date fechaFinal
	
	
	
	static Periodo getMesActual(){
		
		//int year=obtenerYear(d2);
		Calendar c=Calendar.getInstance();
		Date d2=c.getTime();
		c.set(Calendar.DATE,1);
		
		Date d1=c.getTime();
		return new Periodo(fechaInicial:d1,fechaFinal:d2);
		
	}
	
	public String toString(){
		return "${fechaInicial} al ${fechaFinal}" 
	} 

}
