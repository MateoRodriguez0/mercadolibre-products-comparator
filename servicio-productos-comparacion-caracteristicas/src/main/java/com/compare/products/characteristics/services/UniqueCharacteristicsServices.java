package com.compare.products.characteristics.services;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import com.compare.products.characteristics.models.Attribute;

@Service
@Scope("prototype")
public class UniqueCharacteristicsServices {

	public String CompletePhrase(Attribute attribute) {
		String name= attribute.getName();
		String value= attribute.getValue();
		
		if (name.startsWith("Con")) {
			if(value.equals("No")) {
				name= "No "+name.replace("Con", "tiene");
			}
			if(value.equals("Si")) {
				name=name.replace("Con", "Tiene");
			}
		}
		else if (name.startsWith("Com")) {
			if(value.equals("No")) {
				name= "No "+name.replace("Com", "Tem");
			}
			if(value.equals("Sim")) {
				name=name.replace("Com", "Tem");
			}
		
		}
		else if(name.startsWith("Es")) {
			if(value.equals("No")) {
				name= "No "+name;
			}
		}
		else {
			name= name+": "+value;
		}
		
		return name;	
		}

}
