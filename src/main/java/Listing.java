
public class Listing {
    int id;
    String username;
    String title;
    String city;
    String building;
    int num_of_rooms;
    String description;
    int price;
    String postdate;
    String contact_info;
    String status;
    String comment;
    String image;


    public Listing(int id, String username, String title, String city, String building, int num_of_rooms, String description, int price, String postdate, String contact_info, String status, String comment, String image) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.city = city;
        this.building = building;
        this.num_of_rooms = num_of_rooms;
        this.description = description;
        this.price = price;
        this.postdate = postdate;
        this.contact_info = contact_info;
        this.status = status;
        this.comment = comment;
        this.image = image;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public int getNum_of_rooms() {
        return num_of_rooms;
    }

    public void setNum_of_rooms(int num_of_rooms) {
        this.num_of_rooms = num_of_rooms;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public String getContact_info() {
        return contact_info;
    }

    public void setContact_info(String contact_info) {
        this.contact_info = contact_info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String toString() {
        String string = "";
        string += this.username;
        string += " " + city + " " + num_of_rooms + " " + price + "";

        return string;
    }
}

