package com.mercadolibre.productscomparator.vendedores.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class DatesUtil {

	
	public static Period compareDates(String date1,String date2,String zone) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		Date dateTime=null;
		try {
			dateTime = sdf.parse(date1);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		Instant instant = dateTime.toInstant();
		LocalDate startDate = LocalDate.ofInstant(instant,
				ZoneId.of(zone));
		LocalDate endDate = LocalDate.now();
        
		
        return Period.between(startDate, endDate);
	}
	
	
	public static int getMinutes (String date1,String date2) {
		
		Instant fechaInicioInstant = Instant.parse(date1);
		Instant fechaFinInstant = Instant.parse(date2);
		Duration duracion = Duration.between(fechaInicioInstant, fechaFinInstant);

		return (int) duracion.toMinutes();
	}
	
}
