package softcrafties.phx.hungarian

import softcrafties.phx.hungarian.abstractions.Resource;

class Worker implements Resource{
	String name
	
	@Override
	public boolean equals(Object another){
		if(!(another instanceof Worker)){
			return false
		}
		return ((Worker)another).name.equals(name)
	}
	
	@Override
	public int hashCode(){
		return name.hashCode()
	}
	
	@Override
	public String toString() { name }
}
