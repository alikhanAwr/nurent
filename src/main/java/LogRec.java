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
}
