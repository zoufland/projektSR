/**
 * Created by Zoufland on 31.05.2017.
 */
public class Service {
    private String timestamp;

    public void setCurrentTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public boolean checkTimestamp(String timestamp)
    {
        if(timestamp.compareTo(this.timestamp) == 1);
    }
}
