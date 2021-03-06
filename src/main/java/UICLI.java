import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class UICLI {

    GameBrowser gameBrowser;



    public UICLI(String filepath) throws IllegalArgumentException, DataSourceException {
        gameBrowser = new GameBrowser(filepath, new UIPluginCLI());
    }

    private void init() {
        try {
            Scanner in = new Scanner(System.in);
            String choice = null;

            System.out.println("Welcome to the Big Girl Game Library!");
            while (!isInt(choice) || (parseInt(choice) < 1 || parseInt(choice) > 4)) {
                System.out.println("What would you like to do?");
                System.out.println("1. Login");
                System.out.println("2. Register a new account");
                System.out.println("3. Quit");
                choice = in.nextLine();
            }

            if (isInt(choice)) {
                switch (parseInt(choice)) {
                    case 1:
                        login();
                        break;
                    case 2:
                        createAccount();
                        init();
                        break;
                    case 3:
                        System.out.println("Have a nice day!");
                        break;
                    default:
                        System.out.println("We're not so sure about that...");
                        init();
                        break;
                }
            }
        } catch(Exception e) {
            System.out.print("Error: " + e.getMessage() + "\n");
            init();
        }
    }

    private void createAccount() throws DataSourceException{
        Scanner in = new Scanner(System.in);
        String email = null,
                username = null,
                password = null;
        Developer developer = null;
        User user = null;

        System.out.println("Would you like to make a new user account(1), a new developer account(2), or a new dual(user and developer) account(3)? To cancel press 0");
        String accountType = in.nextLine();
        while(!isInt(accountType) || parseInt(accountType) < 0 || parseInt(accountType) > 3){
            System.out.println("Invalid input");
            System.out.println("Would you like to make a user account(1) or a developer account(2)? To cancel press 0");
            accountType = in.nextLine();
        }
        if (parseInt(accountType) != 0){
            System.out.println("Please enter your email: ");
            email = in.nextLine();
            System.out.println("Please create a username: ");
            username = in.nextLine();
            System.out.println("Please create a password: ");
            password = in.nextLine();
            if (parseInt(accountType) == 1){
                gameBrowser.createUserAccount(username, email, password);
                System.out.println("User account created!");
            }
            else if (parseInt(accountType) == 2){
                gameBrowser.createDeveloperAccount(username, email, password);
                System.out.println("Developer account created!");
            }
            else{
                gameBrowser.createDualAccount(username,email,password);
                System.out.println("Dual account created!");
            }
        }
    }

    private void login() throws DataSourceException {
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter your username: ");

        String usernameEnter = in.nextLine();

        System.out.println("Please enter your password: ");

        String passwordEnter = in.nextLine();

        try {
            Accounts userAccounts = gameBrowser.login(usernameEnter, passwordEnter);


            //admin login
            if (userAccounts.admin != null) {
                System.out.println("Logged IN! Welcome " + userAccounts.admin.getUsername());
                administratorTakeAction(userAccounts.admin);
            }
            //user
            else if (userAccounts.user != null && userAccounts.dev == null) {
                System.out.println("Logged IN! you own " + userAccounts.user.getOwnedGames().getGames().size() + " Games!");
                commercialUserTakeAction(userAccounts.user, null, false);
            }
            //developer
            else if (userAccounts.user == null && userAccounts.dev != null) {
                System.out.println("Logged IN! Welcome " + userAccounts.dev.getName());
                developerTakeAction(userAccounts.dev, null, false);
            }


            //user and dev dual account
            else if (userAccounts.user != null && userAccounts.dev != null) {
                System.out.println("You have both a User and a Developer account; which would you like to login as?");
                System.out.println("1. User");
                System.out.println("2. Dev");

                int choice = in.nextInt();

                while(choice > 2 || choice < 1){
                    System.out.println("Invalid choice.");
                    System.out.println("Would you like to login as a user or dev?");
                    System.out.println("1. User");
                    System.out.println("2. Dev");

                    choice = in.nextInt();
                }

                if (choice == 1){
                    commercialUserTakeAction(userAccounts.user, userAccounts.dev, true);
                }

                else{
                    developerTakeAction(userAccounts.dev, userAccounts.user, true);
                }
            } else {
                System.out.println("I'm sorry, either your username or password is incorrect.");
                init();

                //password recovery code moved below main for now
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage() + "\nPlease try again");
            init();
        }
    }

    public boolean isInt( String input ) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }

    //ADMINISTRATOR UI

    private void administratorTakeAction(Administrator adminAccount) throws DataSourceException {
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome. Please choose what action you'd like to take:");
        System.out.println("1: Review Pending Games");
        System.out.println("2: Review Approved Games");
        System.out.println("3: View All Games in Library");
        System.out.println("4: Logout");

        String adminChoice = in.nextLine();
        while (!isInt(adminChoice) || (parseInt(adminChoice) < 1 || parseInt(adminChoice) > 4)) {
            System.out.println("Please enter a valid choice");
            adminChoice = in.nextLine();
        }
//---------------pending-------------------
        if (parseInt(adminChoice) == 1) {
            //display pending games
            gameBrowser.pullGameList("Master Game List");
            System.out.println(gameBrowser.displayableNumberedListOfGamesGivenStatus(Status.PENDING));
            GameList pendingGames = gameBrowser.getGamesGivenStatus(Status.PENDING);
            if(pendingGames.getGameCount()<1){
                System.out.println("No pending games");
                administratorTakeAction(adminAccount);
            }
            else {
                System.out.println("Please choose which pending game you'd like to review, or press 0 to exit:");

                String devReviewGameChoice = in.nextLine();
                while (!isInt(devReviewGameChoice) || (parseInt(devReviewGameChoice) < 0 || parseInt(devReviewGameChoice) > gameBrowser.getGameList().getGameCount())) {
                    System.out.println("Please enter a valid choice");
                    System.out.println("Please choose which pending game you'd like to review, or press 0 to exit:");
                    devReviewGameChoice = in.nextLine();
                }

                if (parseInt(devReviewGameChoice) == 0) {
                    administratorTakeAction(adminAccount);

                } else if (parseInt(devReviewGameChoice) >= 1 && parseInt(devReviewGameChoice) <= pendingGames.getGames().size()) {

                    String devReviewGame = pendingGames.getGames().get(parseInt(devReviewGameChoice) - 1);

                    //get game
                    Game chosenGame = gameBrowser.loadGame(devReviewGame);

                    System.out.println("Would you like to accept or reject this game?, or press any other number to quit");
                    System.out.println("1: Approve");
                    System.out.println("2: Reject");

                    int devApproveReject = in.nextInt();

                    if (devApproveReject == 1) {
                        chosenGame.changeStatus(Status.ACCEPTED);
                        gameBrowser.saveGame(chosenGame);
                        System.out.println("Game has been approved");
                        administratorTakeAction(adminAccount);
                    } else if (devApproveReject == 2) {
                        chosenGame.changeStatus(Status.REJECTED);
                        gameBrowser.saveGame(chosenGame);
                        System.out.println("Game has been rejected.");
                        administratorTakeAction(adminAccount);
                    } else {
                        System.out.println("Game status not changed");
                        administratorTakeAction(adminAccount);
                    }
                }
            }

        }
//--------------approved--------------------
        else if (parseInt(adminChoice) == 2) {
            gameBrowser.pullGameList("Master Game List");

            System.out.println("Approved Games:");

            //nothing in list
            if (gameBrowser.getGamesGivenStatus(Status.ACCEPTED).getGameCount() < 1) {
                System.out.println("No approved games\n");
                administratorTakeAction(adminAccount);
            }

            else {
                //yes

                System.out.println("Please select the game you would like to remove, or 0 to cancel");

                System.out.println(gameBrowser.displayableNumberedListOfGamesGivenStatus(Status.ACCEPTED));

                System.out.println("Would you like to remove any games?");
                System.out.println("1: Yes");
                System.out.println("2: No");

                String adminApprovedChoice = in.nextLine();
                while (!isInt(adminApprovedChoice) || (parseInt(adminApprovedChoice) < 1 || parseInt(adminApprovedChoice) > 2)) {
                    System.out.println("Please enter a valid choice");
                    adminApprovedChoice = in.nextLine();
                }

                if (parseInt(adminApprovedChoice) == 1) {
                    gameBrowser.pullGameList("Master Game List");
                    GameList approvedGames = gameBrowser.getGamesGivenStatus(Status.ACCEPTED);

                    System.out.println("Which game would you like to remove? Please enter its number.");
                    String adminRemoveChoice = in.nextLine();
                    while (!isInt(adminRemoveChoice) || (parseInt(adminRemoveChoice) < 1 || parseInt(adminRemoveChoice) > gameBrowser.getGameList().getGameCount())) {
                        System.out.println("Please enter a valid choice");
                        adminRemoveChoice = in.nextLine();
                    }

                    Game chosenGame = gameBrowser.loadGame(approvedGames.getGames().get(parseInt(adminRemoveChoice) - 1));
                    chosenGame.changeStatus(Status.REJECTED);
                    gameBrowser.saveGame(chosenGame);
                    System.out.println("Game has been rejected.");
                }
                administratorTakeAction(adminAccount);

            }
        }
//----------------------------------
        else if (parseInt(adminChoice)==3){
            gameBrowser.pullGameList("Master Game List");
            System.out.println(gameBrowser.displayableNumberedListOfFullGames());

            administratorTakeAction(adminAccount);
        }
//----------------------------------log out
        //log out
        else {
            System.out.println("Thank you for using the Big Girl Game Library.");
            System.out.println("See you soon!");
            init();
        }

    }


    //DEVELOPER UI
    private void developerTakeAction(Developer devAccount, User userAccount, boolean dual) throws DataSourceException {
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome. Please choose what action you'd like to take:");
        System.out.println("1: Submit Game");
        System.out.println("2: Update Game");
        System.out.println("3: View Your Games");
        System.out.println("4: Logout");
        if(dual){
            System.out.println("5: Switch Accounts");
        }

        String devChoice = in.nextLine();

        if(!dual){
            while (!isInt(devChoice) || (parseInt(devChoice) < 1 || parseInt(devChoice) > 4)) {
                System.out.println("Please enter a valid choice");
                devChoice = in.nextLine();
            }
        }
        else{
            while (!isInt(devChoice) || (parseInt(devChoice) < 1 || parseInt(devChoice) > 5)) {
                System.out.println("Please enter a valid choice");
                devChoice = in.nextLine();
            }
        }

//-----------------------submit
        if (parseInt(devChoice) == 1) {
            Scanner in1 = new Scanner(System.in);
            System.out.println("Please enter the title of your game:");
            String gameName = in1.nextLine();

            System.out.println("Please enter the description of your game:");
            String gameDescription = in1.nextLine();

            //submit
            Game newGame = new Game(gameName, gameDescription, devAccount.getName(), Status.PENDING);
            //adds to master list and saves in database
            gameBrowser.addGame(newGame);
            //includes game in dev's gamelist
            devAccount.submitGame(newGame.getTitle());
            //saves dev's gamelist
            gameBrowser.saveGameList(devAccount.getGameList());

            gameBrowser.saveGameList(gameBrowser.getGameList());

            System.out.println("Thank you! Your game has been submitted and is under review.");
            //System.out.println("Expect a response in your inbox shortly.");

            //back to menu screen
            developerTakeAction(devAccount, userAccount, dual);
        }
//-------------------------update game
        else if (parseInt(devChoice) == 2) {

            gameBrowser.pullGameList(devAccount.getGameList().getName());
            System.out.println(gameBrowser.displayableGameTitlesNumberedList());

            //only will do things if list size is at least 1.
            if (gameBrowser.getGameList().getGameCount()>0) {
                System.out.println("Please select the game that you'd like to update, or press 0 to cancel:");
                String devUpdateChoice = in.nextLine();
                while (!isInt(devUpdateChoice) || (parseInt(devUpdateChoice) < 0 || parseInt(devUpdateChoice) > devAccount.getGameList().getGameCount())) {
                    System.out.println("Please pick a valid choice\n");
                    System.out.println("Please select the game that you'd like to update, or press 0 to cancel:");
                    devUpdateChoice = in.nextLine();
                }
                if (parseInt(devUpdateChoice) == 0) {
                    developerTakeAction(devAccount, userAccount, dual);
                } else {

                    Game updatingGame = gameBrowser.loadGame(devAccount.getGameList().getGames().get(parseInt(devUpdateChoice) - 1));


                    System.out.println("Please select one of the following options:");
                    System.out.println("1: Update Title");
                    System.out.println("2: Update Description");

                    String devModifyChoice = in.nextLine();
                    while (!isInt(devModifyChoice) || (parseInt(devModifyChoice) < 1 || parseInt(devModifyChoice) > 2)) {
                        System.out.println("Please select one of the following options:");
                        System.out.println("1: Update Title");
                        System.out.println("2: Update Description");

                        devModifyChoice = in.nextLine();
                    }

                    //update title
                    if (parseInt(devModifyChoice) == 1) {
                        System.out.println("This feature is currently under maintenance.");
//                        //get old game title
//                        String oldTitle = updatingGame.getTitle();
//                        System.out.println("Please enter the new game title:");
//                        String updatedTitle = in.nextLine();
//                        gameBrowser.changeTitle(updatingGame, updatedTitle);
//                        System.out.println(gameBrowser.getGameList().getGames());
//                        gameBrowser.addGameToDevGameList(devAccount, updatingGame);
//                        //remove old title copy
//                        gameBrowser.removeGame(oldTitle);
//                        gameBrowser.saveGameList(devAccount.getGameList());
//                        gameBrowser.saveGameList(gameBrowser.getGameList());
//                        System.out.println("Title Updated!");
//                        developerTakeAction(devAccount, userAccount, dual);
                    }
                    //update description
                    else if (parseInt(devModifyChoice) == 2) {
                        Scanner in3 = new Scanner(System.in);
                        System.out.println("Please enter the new game description:");
                        String updatedBio = in3.nextLine();
                        updatingGame.changeDescription(updatedBio);
                        gameBrowser.saveGame(updatingGame);
                        gameBrowser.saveGameList(gameBrowser.getGameList());

                        System.out.println("Description updated!");
                        developerTakeAction(devAccount, userAccount, dual);
                    } else {
                        System.out.println("ERROR: Invalid answer.");
                        developerTakeAction(devAccount, userAccount, dual);
                    }
                }
            }
            developerTakeAction(devAccount, userAccount, dual);
        }
//---------------------view games
        else if (parseInt(devChoice) == 3) {

            gameBrowser.pullGameList(devAccount.getGameList().getName());
            System.out.println(gameBrowser.displayableGameTitlesNumberedList());

            Scanner dc3 = new Scanner(System.in);

            System.out.println("Enter the number associated with the game you'd like to view more details of, or press 0 to cancel");
            String gameNum = dc3.nextLine();

            //ask for answer until a valid one is given
            while (!isInt(gameNum) || (parseInt(gameNum) < 0 || parseInt(gameNum) > devAccount.getGameList().getGameCount())) {
                System.out.println("Please enter a valid choice");
                gameNum = in.nextLine();
            }

            if (parseInt(gameNum) == 0){
                developerTakeAction(devAccount, userAccount, dual);
            }
            else{
                gameBrowser.pullGame(devAccount.getGameList().getGames().get(parseInt(gameNum)-1));
                System.out.println(gameBrowser.displayableGameAndReviews());
                developerTakeAction(devAccount, userAccount, dual);
            }


        }
//----------------------log out
        else if (parseInt(devChoice) == 4) {
            System.out.println("Thank you for using the Big Girl Game Library");
            System.out.println("See you soon!");

            init();
        }

        else if(parseInt(devChoice) == 5){
            System.out.println("You will now be logged into your Commercial User account.");
            commercialUserTakeAction(userAccount, devAccount, dual);
        }

        else {
            System.out.println("ERROR: Invalid answer.");
            System.out.println("Please try again.");
            //back to the top of the list
            developerTakeAction(devAccount, userAccount, dual);
        }

    }


    private void commercialUserTakeAction(User userAccount, Developer devAccount, boolean dual) throws DataSourceException {
        Scanner in = new Scanner(System.in);
        System.out.println("Please choose the action you'd like to take");
        System.out.println("1: View Games / Write Reviews");
        System.out.println("2: Add Game to Owned Games List");
        System.out.println("3: Remove Game from Owned Games List");
        System.out.println("4: View Owned Games List");
        System.out.println("5: Logout");
        if(dual){
            System.out.println("6: Switch Accounts");
        }

        String userChoice = in.nextLine();
        if(!dual){
            while (!isInt(userChoice) || (parseInt(userChoice) < 1 || parseInt(userChoice) > 5)) {
                System.out.println("Please enter a valid choice:");
                userChoice = in.nextLine();
            }
        }
        else{
            while (!isInt(userChoice) || (parseInt(userChoice) < 1 || parseInt(userChoice) > 6)) {
                System.out.println("Please enter a valid choice:");
                userChoice = in.nextLine();
            }
        }
//-----View Games / Write Reviews-----
        if (parseInt(userChoice) == 1) {
            System.out.println("Big Girl Game Browser Game List:");
            gameBrowser.pullGameList("Master Game List");
            System.out.println(gameBrowser.displayableNumberedListOfGamesGivenStatus(Status.ACCEPTED));

            System.out.println("Enter the number associated with the game you'd like to view more details of, or press 0 to cancel");
            String userViewChoice = in.nextLine();

            while(!isInt(userViewChoice) || parseInt(userViewChoice) < 0 || parseInt(userViewChoice) > gameBrowser.getGamesGivenStatus(Status.ACCEPTED).getGames().size()){
                System.out.println("Enter the number associated with the game you'd like to view more details of, or press 0 to cancel");
                userViewChoice = in.nextLine();
            }

            if(parseInt(userViewChoice) == 0){
                commercialUserTakeAction(userAccount, devAccount, dual);
            }
            else{
                //gets all approves games
                GameList approvedGames = gameBrowser.getGamesGivenStatus(Status.ACCEPTED);
                //gets the game id
                String userGameToReview = approvedGames.getGames().get(parseInt(userViewChoice) - 1);
                //loads game
                Game gameToReview = gameBrowser.loadGame(userGameToReview);
                //pulls into gb
                gameBrowser.pullGame(gameToReview.getTitle());
                //prints game
                System.out.println(gameBrowser.displayableGameAndReviews());

                System.out.println("Press 1 to review this game, or any other key to cancel");
                String userReviewChoice = in.nextLine();

                if (userReviewChoice.equals("1")) {

                    //TODO: this won't work yet because reviews aren't being saved in the database
                    List<Review> revs = gameToReview.getReviews();

                    Review madeReview = null;
                    for (Review review : revs) {
                        if (review.getAuthor().equals(userAccount.getName())) {
                            madeReview = review;
                        }
                    }

                    if (madeReview == null) {


                        System.out.println("Please write a summary for " + gameToReview.getTitle());

                        String reviewDescriptionToAdd = in.nextLine();

                        System.out.println("Please rate " + gameToReview.getTitle() + " on a scale from 1 star to 5 stars.");

                        String reviewStarRating = in.nextLine();

                        while (!isInt(reviewStarRating) || parseInt(reviewStarRating) < 1 || parseInt(reviewStarRating) > 5) {
                            System.out.println("ERROR: Invalid rating");
                            System.out.println("Please try again.");
                            System.out.println("Please rate " + gameToReview.getTitle() + "on a scale from 1 star to 5 stars (integers only).");

                            reviewStarRating = in.nextLine();

                        }

                        gameBrowser.addReview(gameToReview, userAccount.getName(), reviewDescriptionToAdd, parseInt(reviewStarRating));

                        System.out.println("Thank you! Your response has been recorded.");
                        //back to user menu
                        commercialUserTakeAction(userAccount, devAccount, dual);
                    } else {
                        System.out.println("Review already made, press 1 to edit your review or 0 to cancel");
                        String revChoice = in.nextLine();
                        while(!isInt(revChoice) || (parseInt(revChoice)!= 0 && parseInt(revChoice)!= 1)){
                            System.out.println("Press 1 to edit your review or 0 to cancel");
                            revChoice = in.nextLine();
                        }

                        if (parseInt(revChoice) == 1){
                            System.out.println("Please write a new summary for " + gameToReview.getTitle());

                            String reviewDescriptionToUpdate = in.nextLine();

                            System.out.println("Please enter a new rating " + gameToReview.getTitle() + " on a scale from 1 star to 5 stars.");

                            String updatedReviewStarRating = in.nextLine();

                            while (!isInt(updatedReviewStarRating) || parseInt(updatedReviewStarRating) < 1 || parseInt(updatedReviewStarRating) > 5) {
                                System.out.println("ERROR: Invalid rating, please enter a number between 1 and 5.");
                                System.out.println("Please rate " + gameToReview.getTitle() + "on a scale from 1 star to 5 stars (integers only).");
                                updatedReviewStarRating = in.nextLine();
                            }

                            gameBrowser.updateReview(gameToReview, userAccount.getName(), reviewDescriptionToUpdate, parseInt(updatedReviewStarRating));
                            System.out.println("Review updated!");
                        }

                        commercialUserTakeAction(userAccount, devAccount, dual);
                    }
                }
                else{
                    commercialUserTakeAction(userAccount, devAccount, dual);
                }
            }

        }
//-----Add Game to Owned Games List-----
        else if (parseInt(userChoice) == 2) {
            gameBrowser.pullGameList("Master Game List");
            System.out.println(gameBrowser.displayableNumberedListOfGamesGivenStatus(Status.ACCEPTED));

            System.out.println("Please enter the number of the game you'd like to add to your owned list");
            System.out.println("Or press 0 to exit.");

            String userAddChoice = in.nextLine();
            while (!isInt(userAddChoice) || parseInt(userAddChoice) < 0 || parseInt(userAddChoice) > gameBrowser.getGameList().getGameCount()) {

                System.out.println("Please enter the number of the game you'd like to add to your owned list");
                System.out.println("Or press 0 to exit.");
                userAddChoice = in.nextLine();
            }

            if (parseInt(userAddChoice) == 0) {
                //back to user menu
                commercialUserTakeAction(userAccount, devAccount, dual);

            } else if (parseInt(userAddChoice) > 0 && parseInt(userAddChoice) < gameBrowser.getGameList().getGameCount()) {

                //get game based on user input
                GameList approvedGames = gameBrowser.getGamesGivenStatus(Status.ACCEPTED);
                String userGameToAdd = approvedGames.getGames().get(parseInt(userAddChoice) - 1);
                Game gameToAdd = gameBrowser.loadGame(userGameToAdd);
                String gameTitleFound = null;
                for (String gt : userAccount.getOwnedGames().getGames()) {
                    if (gt.equals(gameToAdd.getTitle())) {
                        gameTitleFound = gt;
                        break;
                    }
                }
                if (gameTitleFound == null){
                    gameBrowser.addGameToUserGameList(userAccount, gameToAdd);
                    gameBrowser.saveGameList(userAccount.getOwnedGames());

                    System.out.println("Game has successfully been added to your list.");
                }
                else{
                    System.out.println("Already in your list!");
                }
                commercialUserTakeAction(userAccount, devAccount, dual);
            }
            else{
                System.out.println("Please use the numbers displayed by the Game Titles.");
            }
        }
//-----Remove Game from Owned Games List-----
        else if (parseInt(userChoice) == 3) {
            System.out.println("Owned Games:");
            gameBrowser.pullGameList(userAccount.getOwnedGames().getName());
            System.out.println(gameBrowser.displayableGameTitlesNumberedList());

            //if they have owned gamed
            if (userAccount.getOwnedGames().getGameCount()  > 0) {
                System.out.println("Please select the game that you'd like to remove, or press 0 to cancel:");
                String removeOwnedListChoice = in.nextLine();
                //make sure input is good
                while (!isInt(removeOwnedListChoice) || parseInt(removeOwnedListChoice) < 0 || parseInt(removeOwnedListChoice) > userAccount.getOwnedGames().getGameCount()) {

                    System.out.println("Please enter the number of the game you'd like to remove from your owned list");
                    System.out.println("Or press 0 to exit.");
                    removeOwnedListChoice = in.nextLine();
                }
                //wanna quit this task
                if(parseInt(removeOwnedListChoice) == 0){
                    System.out.println("Returning you to the User menu");
                }
                //remove a game
                else if (parseInt(removeOwnedListChoice) > 0 && parseInt(removeOwnedListChoice) <= userAccount.getOwnedGames().getGameCount()){

                    Game updatingGame = gameBrowser.loadGame(userAccount.getOwnedGames().getGames().get(parseInt(removeOwnedListChoice) - 1));
                    gameBrowser.pullGame(updatingGame.getTitle());
                    gameBrowser.removeGameFromUserGameList(userAccount, updatingGame);
                    gameBrowser.saveGameList(userAccount.getOwnedGames());
                }

                else{
                    System.out.println("ERROR: Not a valid input.");
                    System.out.println("You will now be returned to the User Menu.");
                }
            }
            else
                System.out.println("You have no games to remove");
            commercialUserTakeAction(userAccount, devAccount, dual);
        }
//----- View Owned Games List-----
        else if (parseInt(userChoice) == 4) {
            gameBrowser.pullGameList(userAccount.getOwnedGames().getName());
            System.out.println(gameBrowser.displayableAllGames());
            commercialUserTakeAction(userAccount, devAccount, dual);
        }
//-----Logout-----
        else if(parseInt(userChoice) == 5){
            System.out.println("Thank you for using the Big Girl Game Browser.");
            System.out.println("Have a good one!");
            init();
        }
//-----switch to dev-----
        else if(dual && parseInt(userChoice) == 6){
            System.out.println("You will now be logged in as a developer.");
            developerTakeAction(devAccount, userAccount, true);
        }

    }


    public static void main(String[] args) throws IOException, ParseException, DataSourceException {
        UICLI myBGGLTest = new UICLI("src/databases/presentation.db");
//        myBGGLTest.login();
        myBGGLTest.init();

    }
}
