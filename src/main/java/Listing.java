

public class Listing {
    String email;
    String title;
    String city;
    String building;
    int num_of_rooms;
    String description;
    int price;
    String postdate;
    String contact_info;



    public Listing(String email, String title, String city, String building, int num_of_rooms, String description, int price, String postdate, String contact_info){
        this.email = email;
        this.title = title;
        this.city = city;
        this.building = building;
        this.description = description;
        this.price = price;
        this.contact_info = contact_info;
        this.postdate = postdate;
        this.num_of_rooms = num_of_rooms;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
