package mk.obj;

import java.util.ArrayList;
import java.util.List;

public class Fruit {
	@Override
	public String toString() {
		return "Fruit [index=" + index + ", name=" + name + ", levels=" + levels + "]";
	}


	public int index;
	public String name;
	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}


	private List<String> levels;

	public List<String> getLevels() {
		return levels;
	}

	private Fruit() {
	}

	public Fruit(int index, String name) {
		this.name = name;
		this.index = index;
	}

	public static Builder builder() {
		
		Builder biulder= new Builder();
		biulder.levels= new ArrayList<String>();
	    return biulder;
	}

	
	public static final class Builder{
		
		public int index;
		public String name;
		
		public Builder setIndex(int index) {
			this.index = index;
			return this;

		}


		public Builder setName(String name) {
			this.name = name;
			return this;
		}


		private List<String> levels;
		
		 public Builder addLevel(String level) {
		        this.levels.add(level);
		        return this;
		    }
		
		public Fruit build()
		{
			Fruit fruit = new Fruit();
			fruit.index= this.index;
			fruit.name= this.name;
			fruit.levels= this.levels;
			return fruit;
			
			
			
			
			
		}
		
			
	}
//	public static final class Builder{	
	
}
