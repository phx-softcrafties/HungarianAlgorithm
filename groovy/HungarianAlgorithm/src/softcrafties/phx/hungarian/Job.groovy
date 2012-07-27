package softcrafties.phx.hungarian

import softcrafties.phx.hungarian.abstractions.Task;

class Job implements Task {
	String name
	
	@Override
	public boolean equals(Object another){
		if(!(another instanceof Job)){
			return false
		}
		return ((Job)another).name.equals(name)
	}
	
	@Override
	public int hashCode(){
		return name.hashCode()
	}
}
