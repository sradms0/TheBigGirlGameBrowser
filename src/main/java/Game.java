import java.util.ArrayList;
import java.util.List;

enum Status {PENDING, ACCEPTED, REJECTED}

public class Game {
    private String title;
    private String description;
    private List<Developer> developers;
    private Status status;

    /**
     * Default constructor
     */
    public Game(){
        title = "testGame";
        description = "This is a test to create a new game object";
        this.developers = new ArrayList<Developer>();
        status = Status.PENDING;
    }

    /**
     * FullFull constructor (should only be used for backend tests)
     * @param title game title
     * @param description descriptor for game
     * @param developer link to developer object
     */
    public Game(String title, String description, List<Developer> developer, Status status){
        this.title = title;
        this.description = description;
        this.developers = new ArrayList<Developer>(developer);
        this.status = status;
    }

    /**
     *  Full constructor
     * @param title game title
     * @param description descriptor for game
     * @param developer link to developer object
     */
    public Game(String title, String description,  List<Developer> developer){
        this.title = title;
        this.description = description;
        this.developers = new ArrayList<Developer>(developer);
        this.status = Status.PENDING;
    }

    /**
     * Game constructor for when you don't know the description yet
     * @param title game title
     * @param developer link to developer object
     */
    public Game(String title, List<Developer> developer){
        this.title = title;
        this.developers = new ArrayList<Developer>(developer);
        this.description = "No Description Given";
        this.status = Status.PENDING;
    }


    /**
     * FullFull constructor (should only be used for backend tests)
     * @param title game title
     * @param description descriptor for game
     * @param developer link to developer object
     */
    public Game(String title, String description, Developer developer, Status status){
        this.title = title;
        this.description = description;
        this.developers = new ArrayList<Developer>();
        this.developers.add(developer);
        this.status = status;
    }

    /**
     *  Full constructor
     * @param title game title
     * @param description descriptor for game
     * @param developer link to developer object
     */
    public Game(String title, String description, Developer developer){
        this.title = title;
        this.description = description;
        this.developers = new ArrayList<Developer>();
        this.developers.add(developer);
        this.status = Status.PENDING;
    }

    /**
     * Game constructor for when you don't know the description yet
     * @param title game title
     * @param developer link to developer object
     */
    public Game(String title, Developer developer){
        this.title = title;
        this.developers = new ArrayList<Developer>();
        this.developers.add(developer);
        this.description = "No Description Given";
        this.status = Status.PENDING;
    }

    /**
     * Allows for description to be replaced
     * TODO maybe we want to be able to add or do something other than overwrite a currently existing description?
     * @param description descriptor for game
     */
    public void changeDescription(String description){
        this.description = description;
    }

    /**
     * Allows for title to be replaced
     * TODO maybe we want to be able to add or do something other than overwrite a currently existing title?
     * @param title descriptor for game
     */
    public void changeTitle(String title){
        this.title = title;
    }

    /**
     * Allows for game status to be updated (should only be used by admin class)
     * @param status Game status (Approved, Pending, or Rejected)
     */
    public void changeStatus(Status status){
        this.status = status;
    }

    /**
     * Allows for the addition of multiple developers
     * @param developer a user who develops games for general users
     */
    public void addDeveloper(Developer developer) {this.developers.add(developer);}

    // ------GETTERS------

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Developer> getDevelopers() {
        return developers;
    }

    public Status getStatus() {
        return status;
    }

    public void displayGame() {
        String display = "Title: " + title + "\nDescription: " + description + "\nDeveloper(s): ";
        if (developers.size()==0){
            display += "None";
        }
        else{
            for(int i = 0; i < developers.size()-1; i++){
                display += developers.get(i).getName() + ", ";
            }
            display += developers.get(developers.size()-1).getName();
        }
        display += "\nStatus: "+ status.toString();
        System.out.println(display);
    }
}
