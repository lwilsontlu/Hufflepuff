// EmptyDeckException.java

package deck;

public class EmptyDeckException extends RuntimeException
{
    public EmptyDeckException()
	{
	    super();
	}

	public EmptyDeckException(String message)
	{
	    super(message);
	}
}
