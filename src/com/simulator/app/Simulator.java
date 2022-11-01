package com.simulator.app;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.simulator.app.Court.Section;

public class Simulator {
    private static int playersCreated = 0;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        ArrayList<Player> players = new ArrayList<>();
        players.add(getPlayerFromInput(scanner));
        players.add(getPlayerFromInput(scanner));

        Tournament t = getTournamentFromInput(scanner, players);

        boolean rematch = false;
        do {
            t.simulate();
            System.out.println("Want a rematch?: y/n");
            rematch = (scanner.nextLine().toLowerCase().equals("y")) ? true : false;
            if (rematch) {
                t.rematch();
            }
        } while(rematch);
    }

    private static Player getPlayerFromInput(Scanner scanner) {
        System.out.println("Enter player name");
        String name = scanner.nextLine();

        int winnable = -1;
        do {
            try {
                System.out.println("Enter probability of winning the match (between 1 - 100)");
                winnable = scanner.nextInt();
            } catch (InputMismatchException ime) {
                System.out.println("Invalid input. Please enter a number.");
            }
            scanner.nextLine();
        } while(winnable < 1 || 100 < winnable);

        System.out.println();

        Section sectionIn = (playersCreated % 2 == 0) ? 
                                Section.LeftBackcourt : 
                                Section.RightBackcourt;
        playersCreated++;
        return new Player(name, winnable, sectionIn);
    }

    private static Tournament getTournamentFromInput(Scanner scanner, ArrayList<Player> players) {
        System.out.println("Enter the tournament name");
        String tournamentName = scanner.nextLine();

        int bestOf = -1;
        do {
            try {
                System.out.println("Best of 3 or 5?");
                bestOf = scanner.nextInt();
            } catch (InputMismatchException ime) {
                System.out.println("Invalid input. Please enter a number.");
            }
            scanner.nextLine();
        } while(bestOf != 3 && bestOf != 5);

        return new Tournament(tournamentName, bestOf, players);
    }
}
