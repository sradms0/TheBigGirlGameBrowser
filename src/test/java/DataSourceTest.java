import org.junit.Assert;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DataSourceTest {
    public static void dataSourceSaveGameTest(DataSource ds) throws DataSourceException{
        System.out.println("Warning: DataSource must be empty for correct testing");
        System.out.println("Warning: Used Combined View to verify");
        Game cooltestgame = new Game("testGame", "This is a test to save a game object", new Developer("Frank"));

        // Saving a game
        try {
            ds.saveGame(cooltestgame);
            System.out.println("Warning: Requires Manual Check");
            System.out.println("Values Should Be: " +
                    "\n      title = 'testGame'" +
                    "\n  developer = 'Frank'" +
                    "\ndescription = 'This is a test to save a game object'" +
                    "\n     status = 'PENDING'");
        }catch (Exception e){
            System.out.println(e.getMessage());
            fail("Should not throw exception");
        }

        // Resaving same game does not throw an error
        try {
            ds.saveGame(cooltestgame);
        }catch (Exception e){
            System.out.println(e.getMessage());
            fail("Should not throw exception");
        }

        // Updating a saved game works - Description
        cooltestgame = new Game("Test-zx the Game","ahhhhhhhhh",new Developer("Ted"));
        ds.saveGame(cooltestgame);
        cooltestgame.changeDescription("cool robot game");
        ds.saveGame(cooltestgame);

        // Updating a saved game works - Status
        cooltestgame.changeStatus(Status.ACCEPTED);
        ds.saveGame(cooltestgame);

        System.out.println("Warning: Requires Manual Check");
        System.out.println("Values Should Be: " +
                "\n      title = 'Test-zx the Game'" +
                "\n  developer = 'Ted'" +
                "\ndescription = 'cool robot game'" +
                "\n     status = 'ACCEPTED'");

        // Handles Games with Multiple Developers

        cooltestgame = new Game("Toot Scooters",
                      "mario kart hack where all sounds are balloons popping",
                                new Developer("Ted"));
        cooltestgame.addDeveloper(new Developer("CorgiLover87"));

        ds.saveGame(cooltestgame);
        System.out.println("Warning: Requires Manual Check");
        System.out.println("Values Should Be: " +
                "\n      title = 'Toot Scooters'" +
                "\n  developer = 'Ted', 'CorgiLover87'" +
                "\ndescription = 'mario kart hack where all sounds are balloons popping'" +
                "\n     status = 'PENDING'");

        // Can't give a null game
        assertThrows(IllegalArgumentException.class, () -> ds.saveGame(null));
    }

    public static void dataSourceLoadGameTest(DataSource ds) throws DataSourceException{
        System.out.println("Note, there cannot be a game with the title 'LoadGameTest3', otherwise tests will break");

        // Adding Two Test Games
        Game g = new Game("LoadGameTest1", "description", new Developer("LGT_A"));
        ds.saveGame(g);
        g = new Game("LoadGameTest2", "noitpircsed", new Developer("LGT_B"));
        g.addDeveloper(new Developer("LGT_C"));
        g.changeStatus(Status.ACCEPTED);
        ds.saveGame(g);

        // Can Find A Game
        g = ds.loadGame("LoadGameTest1");
        assertNotNull(g);
        assertEquals("description", g.getDescription());
        assertEquals("LGT_A", g.getDevelopers().get(0).getName());
        assertEquals(Status.PENDING, g.getStatus());

        // Can Find A Game with Multiple Developers
        g = ds.loadGame("LoadGameTest2");
        assertNotNull(g);
        assertEquals("noitpircsed", g.getDescription());
        assertEquals("LGT_B", g.getDevelopers().get(0).getName());
        assertEquals("LGT_C", g.getDevelopers().get(1).getName());
        assertEquals(Status.ACCEPTED, g.getStatus());

        // Putting null gives null
        assertNull(ds.loadGame(null));

        // Putting bogus gives null
        assertNull(ds.loadGame("LoadGameTest3"));
    }
}
