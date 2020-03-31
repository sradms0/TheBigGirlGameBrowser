public class Developer {

    private String name;
    private GameList gameList;

    public Developer(String nameIn, GameList gameListIn){
        name = nameIn;
        gameList = gameListIn;
    }

    public Developer(String nameIn){
        name = nameIn;
        gameList = new GameList(nameIn+"'s Games");
    }

    public void displayDeveloper() {
        System.out.println("Name: " + name );
        gameList.displayListNameAndGameTitles();
    }

    public String getName(){
        return name;
    }

    public GameList getGameList(){
        return gameList;
    }

    public String getGameListName() {
        return gameList.getName();
    }
}
