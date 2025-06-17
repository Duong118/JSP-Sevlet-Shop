/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package l1;

/**
 * L01 - Create a Java console program to manage Candidates of company.
 *
 * @author DUONGNCCE180374
 */
public class L01 {

    /**
     * The main method.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Create an instance of CandidateManagementSystem to run the program
        CandidateManagement system = new CandidateManagement();

        // Display the main menu
        system.mainMenu();
    }

}
