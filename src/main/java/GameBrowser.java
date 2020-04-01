import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

public class GameBrowser {
    private DataSource dataSource;
    private final String gameListName = "Master Game List";
    private GameList gameList;
    private List<Administrator> administrators;
    private List<Developer> developers;
    private List<GameList> allGameLists;

    /**
     * Constructor (
     * @param dataFilePath - file path to load all data from
     */
    public GameBrowser(String dataFilePath) throws IllegalArgumentException, DataSourceException {
        if (dataFilePath.length() == 0)
            throw new IllegalArgumentException("Please supply a filename.");

        dataSource = new SQLiteSource(dataFilePath);
        developers = new ArrayList<Developer>();
        allGameLists = new ArrayList<GameList>();
        loadAllGames();
        //TODO loadAllLists helper functions
        //TODO loadAllDevelopers helper functions
    }

    /**
     * Invalid Default Constructor
     * @throws Exception - a path to a data file is always needed
     */
    public GameBrowser() throws Exception {
        throw new Exception("Invalid constructor. Supply a data source file.");
    }

    public void addGame(Game game){
        gameList.includeGame(game);
    }

    public void addGame(String title, String description, List<Developer> developer, Status status){
        Game game = new Game(title, description, developer, status);
        gameList.includeGame(game);
    }

    public Game removeGame(String title) {
        return gameList.removeGame(title);
    }


    /**
     * A new developer is created and added to the developer list
     * @param username - the username of the developer
     */
    public void addDeveloper(String username) {
        developers.add( new Developer(username) );
    }

    /**
     * A new developer is created and added to the developer list
     * @param developer - a developer object
     */
    public void addDeveloper(Developer developer) {
        developers.add( developer );
    }

    /**
     * Removes a developer from the developer list
     * @param username - the username of the developer
     * @return Developer - the developer that was removed
     */
    public Developer removeDeveloper(String username) {
        Developer developer = null;

        for (Developer d : developers) {
            if (d.getName().equals(username)) {
                developer = d;
                developers.remove(developer);
                break;
            }
        }
        return developer;
    }

    /**
     * Adds a game list to allGameLists
     * @param gameList
     */
    public void addGameList(GameList gameList){ allGameLists.add(gameList);}

    //TODO Remove a GameList

    // ------HELPERS------
    private void loadAllGames() throws DataSourceException {
        try {
            gameList = dataSource.loadGameList(gameListName);
        } catch(DataSourceException dse) {
            System.out.println(dse.getMessage());
            throw new DataSourceException(dse.getMessage());
        }
    }

    /**
     * Saves all the local contents into the database
     * @throws DataSourceException
     */
    public void save() throws DataSourceException {
        //TODO WIPE GAME LIST SUCH THAT REMOVALS GET NOTICED
        dataSource.saveGameList(gameList); // Save Master List
        for (Developer developer: developers) {
            dataSource.setInTransaction(false);
            dataSource.saveDeveloper(developer); // Save Developers
        }
        for (GameList gameList : allGameLists){
            System.out.println(gameList.getName());
            dataSource.setInTransaction(false);
            dataSource.saveGameList(gameList); // Save additional gameLists
        }
        //TODO save Administrators doesn't exist yet
    }

    // ------GETTERS------
    public GameList getGameList() { return gameList; }

    public List<Developer> getDevelopers() { return developers; }
}
