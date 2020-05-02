public class Review {
    private int rating;
    private String summary;
    private String ID;
    private String author;


    public Review(int rateIn, String commentIn, String authorIn){
        if (!(rateIn >= 1 && rateIn <= 5)){
            throw new IllegalArgumentException("Invalid rating");
        }
        else if(!(commentIn.length() >= 1 && commentIn.length() <= 10000)){
            throw new IllegalArgumentException("Invalid summary");
        }
        rating = rateIn;
        summary = commentIn;
        ID = "ID01"; //TODO: make this update based on number of reviews in DB
        author = authorIn;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getID(){
        return this.ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAuthor() {
        return author;
    }
}
