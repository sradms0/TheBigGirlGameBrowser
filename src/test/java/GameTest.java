import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    public void gameConstructorTest(){
        Game game = new Game();

        assertEquals("testGame", game.getTitle());
        assertEquals("This is a test to create a new game object", game.getDescription());
        assertNotNull(game.getDevelopers());

        Game newGame = new Game("daBears", new Developer("Sheila"));

        assertEquals("daBears", newGame.getTitle());
        assertEquals("No Description Given", newGame.getDescription());
        assertNotNull(newGame.getDevelopers());

        Game describedGame = new Game("badTitle", "Bad Description", new Developer("Kelly"));
        assertEquals("badTitle", describedGame.getTitle());
        assertEquals("Bad Description", describedGame.getDescription());
        assertNotNull(describedGame.getDevelopers());
    }

    @Test
    public void changeDescriptionTest(){
        Game game = new Game("daBears", new Developer("Wendell"));

        assertEquals("No Description Given", game.getDescription());

        String aDescription = "Hey Fans! Checkout this awesome 'New Game' that I just released on the Big Girls Game Browser!!!";

        game.changeDescription(aDescription);

        assertEquals(aDescription, game.getDescription());
    }

    @Test
    public void changeTitleTest(){
        Game game = new Game("badTitle", "Bad Description", new Developer("Winston"));

        assertEquals("badTitle", game.getTitle());

        game.changeTitle("goodTitle");

        assertEquals("goodTitle", game.getTitle());
    }

    @Test
    public void enumTest(){
        Game game = new Game("title", "description", new Developer("Jimmy"), Status.ACCEPTED);
        assertEquals(Status.ACCEPTED, game.getStatus()); //checks that the fullfull constructor works

        game.changeStatus(Status.PENDING);
        assertEquals(Status.PENDING, game.getStatus()); //checks assignment

        game.changeStatus(Status.REJECTED);
        assertEquals(Status.REJECTED, game.getStatus());//checks assignment

        game = new Game("title", "description", new Developer("Carter"));
        assertEquals(Status.PENDING, game.getStatus()); //Checks other constructors default assignment

        game.changeStatus(Status.LIMBO);
        assertEquals(Status.LIMBO, game.getStatus());//checks assignment
    }

    @Test
    public void addDeveloperTest(){
        List<Developer> developers = new ArrayList<Developer>();
        developers.add(new Developer("Fluffy"));
        developers.add(new Developer("Nut"));
        developers.add(new Developer("Face"));

        Game multiDevGame = new Game("FlufferNutter",
                "The adventures of a cat and their Penutbutter", developers, Status.ACCEPTED);
        assertEquals(developers, multiDevGame.getDevelopers());

        Developer thatGuy = new Developer("Pual");

        multiDevGame.addDeveloper(thatGuy);
        developers.add(thatGuy);

        assertEquals(developers, multiDevGame.getDevelopers());
    }
}
