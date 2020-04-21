import java.util.Iterator;

public class UIPluginCLI implements UIPlugin {
    Game g;
    GameList gl;
    Developer d;
    GameBrowser gb;

    UIPluginCLI(){
        g = null;
        gl = null;
        d = null;
        gb = null;
    }

    @Override
    public void pullGame(Game game) {
        g = game;
    }

    @Override
    public void pullGameList(GameList gl) {
        this.gl = gl;
    }

    @Override
    public void pullDeveloper(Developer dev) {
        d = dev;
    }

    @Override
    public void pullGameBrowser(GameBrowser gb){
        this.gb = gb;
    }

    @Override
    public boolean hasGame() {
        return g!=null;
    }

    @Override
    public boolean hasGameList() {
        return gl!=null;
    }

    @Override
    public boolean hasDeveloper() {
        return d!=null;
    }

    @Override
    public boolean hasGameBrowser() {
        return gb!=null;
    }


    /**
     * @return a string with the games information
     * @throws IllegalStateException if no pulled game
     */
    public String displayableGame() {
        if(g == null){
            throw new IllegalStateException("no game pulled");
        }
        String gameString = "Title: " + g.getTitle() + "\n";
        gameString += "Description: " + g.getDescription() + "\n";
        if(g.getDevelopers().isEmpty()) {
            gameString += "Developer(s): " + "None" + "\n";
        }
        else{
            StringBuffer devs = new StringBuffer();
            for(String d : g.getDevelopers()){
                devs.append(d).append(", ");
            }
            gameString += "Developer(s): " + devs.substring(0, devs.length()-2)+ "\n";
        }
        gameString += "Status: " + g.getStatus() + "\n\n";
        return gameString;
    }

    /**
     * @return a string with the developers information
     * @throws IllegalStateException if no pulled developers
     */
    public String displayDeveloper() {
        if(d == null){
            throw new IllegalStateException("no developer pulled");
        }
        String devString = "Name: " + d.getName() + "\n";

        // allows use of GameList displaying while keeping current GameList
        GameList temp = gl;
        gl = d.getGameList();
        devString +=  displayListNameAndGameTitles();
        gl = temp;

        return devString;
    }

    /**
     * @return each games information in the gamelist
     * @throws IllegalStateException if no pulled gamelist and/or gamebrowser
     */
    public String displayAllGames(){
        if(gl == null && gb == null){
            throw new IllegalStateException("no pulled gamelist & no pulled gamebrowser");
        }
        else if(gl == null){
            throw new IllegalStateException("no pulled gamelist");
        }
        else if(gb == null){
            throw new IllegalStateException("no pulled gamebrowser");
        }

        String allGames = gl.getName() + ":\n\n";

        if (gl.getGameCount()==0){
            return allGames+"This list is empty\n";
         }
        Game temp = g;

        Iterator<String> games = gl.getGames().iterator();
        while (games.hasNext()){
            String gameName = games.next();
            gb.pullGame(gameName);
            allGames += this.displayableGame();
        }

        g = temp;
        return allGames;
    }

    /**
     * @return gamelist name and all game titles
     * @throws IllegalStateException if no pulled gamelist and/or gamebrowser
     */
    public String displayListNameAndGameTitles() {
        if(gl == null){
            throw new IllegalStateException("no gamelist pulled");
        }
        String display = gl.getName() + ": ";
        if(gl.getGameCount()<1){
            display += "This list is empty";
        }
        else{
            for (int i = 0; i < gl.getGameCount() - 1; i++) {
                display += gl.getGames().get(i) + ", ";
            }
            display += gl.getGames().get(gl.getGames().size() - 1);
        }
        display += "\n";
        return display;
    }

    /**
     * @param status game status to filter by
     * @return each games information in the gamelist where their status matches passed in status
     * @throws IllegalStateException if no pulled gamelist and/or gamebrowser
     */
    public String displayGamesGivenStatus(Status status) {
        if(gl == null && gb == null){
            throw new IllegalStateException("no pulled gamelist & no pulled gamebrowser");
        }
        else if(gl == null){
            throw new IllegalStateException("no pulled gamelist");
        }
        else if(gb == null){
            throw new IllegalStateException("no pulled gamebrowser");
        }

        String gamestatus = gl.getName() + "(" + status +"):\n\n";
        if (gl.getGameCount()==0){
            return gamestatus + "This list is empty\n";
        }

        Game temp = g;
        StringBuilder sb = new StringBuilder();
        Iterator<String> games = gl.getGames().iterator();
        while (games.hasNext()){
            gb.pullGame(games.next());
            if(g.getStatus() == status)
                sb.append(this.displayableGame());
        }


        g = temp;

        if(sb.length() == 0){
            return gamestatus + "This list is empty\n";
        }
        return gamestatus + sb;
    }

    /**
     * @return game titles in a numbered list
     * @throws IllegalStateException if no pulled gamelist
     */
    public String displayGameTitlesNumberedList(){
        if(gl == null){
            throw new IllegalStateException("no gamelist pulled");
        }
        if(gl.getGameCount()==0){
            return "There are no games to display\n";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<String> games = gl.getGames().iterator();
        int count = 1;
        while (games.hasNext()){
            sb.append(count);
            sb.append(". ");
            sb.append(games.next());
            sb.append("\n");
            count++;
        }
        return  sb.toString();
    }


    public String displayNumberedListOfGamesGivenStatus(Status status) throws DataSourceException{
        if(gl == null){
            throw new IllegalStateException("no gamelist pulled");
        }
        if(gl.getGameCount()==0){
            return "There are no games to display\n";
        }

        Iterator<String> games = gl.getGames().iterator();
        StringBuilder sb = new StringBuilder();
        int count = 1;

        while (games.hasNext()){
            gb.pullGame(games.next());
            Game game = gb.loadGame(g.getTitle());
            if(game.getStatus() == status) {
                sb.append(count);
                sb.append(". ");
                sb.append(g.getTitle());
                sb.append("\n");
                count++;
            }
        }
        return  sb.toString();
    }

    public GameList getGamesGivenStatus(Status status) throws DataSourceException{
        if(gl == null){
            throw new IllegalStateException("no gamelist pulled");
        }
        if (gl.getGameCount()==0){
            return new GameList("empty");
        }
        else {
            GameList list = new GameList(status.toString());
            for (int i = 0; i < gl.getGameCount(); i++) {
                gb.pullGame(gl.getGames().get(i));
                Game game = gb.loadGame(g.getTitle());
                if (game.getStatus()==status) {
                    list.includeGame(gl.getGames().get(i));
                }
            }
            return list;
        }
    }
}
