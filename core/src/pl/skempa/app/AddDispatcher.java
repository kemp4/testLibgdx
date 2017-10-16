package pl.skempa.app;

public class AddDispatcher implements IValidateModel, IDescribeModel
{
	private static final String EMPTY_NAME_ERROR = "Name cannot be empty";
	private static final String EMPTY_SURNAME_ERROR = "Surname cannot be empty";
	private static final String EMPTY_LOGIN_ERROR = "Login cannot be empty";
	private static final String EMPTY_PASSWORD_ERROR = "Password cannot be empty";

	private String Name;
	public final String getName()
	{
		return Name;
	}
	public final void setName(String value)
	{
		Name = value;
	}
	private String Surname;
	public final String getSurname()
	{
		return Surname;
	}
	public final void setSurname(String value)
	{
		Surname = value;
	}
	private String Login;
	public final String getLogin()
	{
		return Login;
	}
	public final void setLogin(String value)
	{
		Login = value;
	}
	private String Password;
	public final String getPassword()
	{
		return Password;
	}
	public final void setPassword(String value)
	{
		Password = value;
	}

	public final void Validate()
	{
		if (tangible.DotNetToJavaStringHelper.isNullOrEmpty(getName()))
		{
			throw new TaximedServiceException(EMPTY_NAME_ERROR, ResponseCode.INVALID_PARAMETER);
		}
		if (tangible.DotNetToJavaStringHelper.isNullOrEmpty(getSurname()))
		{
			throw new TaximedServiceException(EMPTY_SURNAME_ERROR, ResponseCode.INVALID_PARAMETER);
		}
		if (tangible.DotNetToJavaStringHelper.isNullOrEmpty(getLogin()))
		{
			throw new TaximedServiceException(EMPTY_LOGIN_ERROR, ResponseCode.INVALID_PARAMETER);
		}
		if (tangible.DotNetToJavaStringHelper.isNullOrEmpty(getPassword()))
		{
			throw new TaximedServiceException(EMPTY_PASSWORD_ERROR, ResponseCode.INVALID_PARAMETER);
		}
	}

	public final String Describe()
	{
		return "";
	}
}