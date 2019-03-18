package mk;


public interface TesterI {

	public void javaStreamsTester() ;

	public default void defaultMethod() 
	{
		System.out.println("Default method");
	}

}
