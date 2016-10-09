package s3390317.mad.ass2.view.model;

import java.io.IOException;

public class HttpException extends IOException
{
    private int responseCode;

    public HttpException(int responseCode)
    {
        this.responseCode = responseCode;
    }

    public int getResponseCode()
    {
        return responseCode;
    }
}
