package sprite;

import java.lang.reflect.Constructor;

public class Sprite {
	private AttackerAttribute attacker;
	private MoverAttribute mover;
	
	//Just an example constructor, not the real one
	public Sprite(){
		mover = new StationaryMoverAttribute();
		attacker = new MeleeAttackerAttribute();
		

	}
	
	public void setMoverAttribute(String attributeClassName) throws ClassNotFoundException{
		mover = (MoverAttribute) setAttribute(attributeClassName);
	}
	
	public void setAttackerAttribute(String attributeClassName) throws ClassNotFoundException{
		attacker = (AttackerAttribute) setAttribute(attributeClassName);
	}
	
	public void setAnimatorAttribute(String attributeClassName) throws ClassNotFoundException{
		//animator = (AnimatorAttribute) setAttribute(attributeClassName);
	}
	
	public void setHealthAttribute(String attributeClassName) throws ClassNotFoundException{
		//health = (HealthAttribute) setAttribute(attributeClassName);
	}

	private Object setAttribute(String attributeClassName) throws ClassNotFoundException{
		//The success of this is dependent on the package the attribute is in
		//need to either fix or standardize attribute placement
		String prefix = "sprite.";
		attributeClassName = prefix.concat(attributeClassName);
		Class<?> clazz = Class.forName(attributeClassName);
		 try{
			Constructor<?> ctor = clazz.getDeclaredConstructor();
         	Object o = ctor.newInstance();
         	String attributeType = getAttributeType(attributeClassName);
    		return o;
		 }
		 catch(Exception e){
			 //Handle on front-end 
			 return null;
		 }
	}
	
	//http://stackoverflow.com/questions/7957944/search-for-capital-letter-in-string
	private String getAttributeType(String className){
		int count = 0;
		for(int i = 10; i <= className.length() - 9; i++){
			count = i;
			if(Character.isUpperCase(className.charAt(className.length() - i))){
				break;
			}
		}		
		return className.substring(className.length() - count);
	}
	
	public void move(){
		mover.move();
	}
	
}


