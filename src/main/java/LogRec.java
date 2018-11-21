public class LogRec {
    private int id;
    private String date_time;
    private String username;
    private String activity;
    private String result;
    private String additional_info;

    public LogRec(int id, String date_time, String username,
                  String activity, String result, String additional_info) {
        this.id = id;
        this.date_time = date_time;
        this.username = username;
        this.activity = activity;
        this.result = result;
        this.additional_info = additional_info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }
}
