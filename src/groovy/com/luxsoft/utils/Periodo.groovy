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
	
	public static int obtenerMes(Date d){
		Calendar c=Calendar.getInstance();
		c.setTime(d);
		int mes=c.get(Calendar.MONTH);
		return mes;
	}
	
	public static int obtenerYear(Date d){
		Calendar c=Calendar.getInstance();
		c.setTime(d);
		int year=c.get(Calendar.YEAR);
		return year;
	}
	
	public String toString(){
		return "${fechaInicial} al ${fechaFinal}" 
	} 

}
