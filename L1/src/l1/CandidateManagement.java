/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package l1;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 *
 * L01 - Create a Java console program to manage Candidates of company.
 *
 * @author DUONGNCCE180374
 */
public class CandidateManagement {

    private ArrayList<Candidate> candidates = new ArrayList<>(); // List to store candidates
    private Scanner sc = new Scanner(System.in); // Scanner for user input
    private ValidationHelper validator = new ValidationHelper(); // Validator for input validation

    /**
     * Shows the list of Experience candidates.
     */
    public void showExperienceCandidates() {
        // List to store experience-type candidates
        List<Candidate> experienceCandidates = new ArrayList<>();
        // Filter candidates who are instances of Experience
        for (Candidate candidate : candidates) {
            if (candidate instanceof Experience) {
                experienceCandidates.add(candidate);
            }
        }
        // Display the list if there are experience candidates
        if (!experienceCandidates.isEmpty()) {
            System.out.println("===========EXPERIENCE CANDIDATE============");
            for (Candidate candidate : experienceCandidates) {
                System.out.println(candidate.getFullName());
                System.out.println();
            }
        }
    }

    /**
     * Shows the list of Fresher candidates.
     */
    public void showFresherCandidates() {
        // List to store fresher-type candidates
        List<Candidate> fresherCandidates = new ArrayList<>();
        // Filter candidates who are instances of Fresher
        for (Candidate candidate : candidates) {
            if (candidate instanceof Fresher) {
                fresherCandidates.add(candidate);
            }
        }
        // Display the list if there are fresher candidates
        if (!fresherCandidates.isEmpty()) {
            System.out.println("==========FRESHER CANDIDATE==============");
            for (Candidate candidate : fresherCandidates) {
                System.out.println(candidate.getFullName());
                System.out.println();
            }
        }
    }

    /**
     * Shows the list of Intern candidates.
     */
    public void showInternCandidates() {
        // List to store intern-type candidates
        List<Candidate> internCandidates = new ArrayList<>();
        // Filter candidates who are instances of Intern
        for (Candidate candidate : candidates) {
            if (candidate instanceof Intern) {
                internCandidates.add(candidate);
            }
        }
        // Display the list if there are intern candidates
        if (!internCandidates.isEmpty()) {
            System.out.println("===========INTERN CANDIDATE==============");
            for (Candidate candidate : internCandidates) {
                System.out.println(candidate.getFullName());
                System.out.println();
            }
        }
    }

    /**
     * Shows all candidates by calling respective methods for each type.
     */
    public void showCandidateList() {
        if (!candidates.isEmpty()) {
            showExperienceCandidates();
            showFresherCandidates();
            showInternCandidates();
        }
    }

    /**
     * Creates an Experience candidate by collecting and validating user input.
     */
    public void createExperienceCandidate() {
        // Get and validate candidate ID
//        String candidateId;
//        do {
//            System.out.print("Enter candidate ID: ");
//            candidateId = sc.nextLine();
//            if (!validator.isValidId(candidateId)) {
//                System.out.println("Invalid input. Please enter letters and numbers only.");
//            } else if (validator.isExisted(candidateId, candidates)) {
//                System.out.println("This ID already exists. Please enter a different one.");
//            }
//        } while (!validator.isValidId(candidateId) || validator.isExisted(candidateId, candidates));
        
         String candidateId;
        do {
            System.out.print("Enter candidate ID: ");
            candidateId = sc.nextLine();
            if (!validator.isValidId(candidateId)) {
                System.out.println("Invalid input. Please enter letters and numbers only.");
            } else if (validator.isExisted(candidateId, candidates)) {
                System.out.println("This ID already exists. Please enter a different one.");
            }
        } while (!validator.isValidId(candidateId) || validator.isExisted(candidateId, candidates));

        // Get and validate first name
        String firstName;
        do {
            System.out.print("Enter first name: ");
            firstName = sc.nextLine();
            if (!validator.isValidName(firstName)) {
                System.out.println("Invalid input. Please enter letters and spaces only.");
            }
        } while (!validator.isValidName(firstName));

        // Get and validate last name
        String lastName;
        do {
            System.out.print("Enter last name: ");
            lastName = sc.nextLine();
            if (!validator.isValidName(lastName)) {
                System.out.println("Invalid input. Please enter letters and spaces only.");
            }
        } while (!validator.isValidName(lastName));

        // Input and validate birth year for Experience candidates
        int birthDate;
        boolean isValidBirthDate;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int minValidYear = currentYear - Experience.MIN_AGE;
        int maxValidYear = currentYear - Experience.MAX_AGE;

        do {
            // Prompt the user to enter a birth year within the valid range
            System.out.print("Enter birth year (" + maxValidYear + " - " + minValidYear + "): ");
            String birthDateStr = sc.nextLine();

            // Validate if the input is a valid integer
            while (!validator.isValidInteger(birthDateStr)) {
                System.out.print("Invalid input. Please enter a valid year: ");
                birthDateStr = sc.nextLine();
            }

            // Convert the input string to an integer
            birthDate = Integer.parseInt(birthDateStr);

            // Check if the birth year is in the future
            if (birthDate > currentYear) {
                System.out.println("Error: Birth year cannot be in the future.");
                isValidBirthDate = false;
                // Check if the birth year falls within the allowed range

            } else if (birthDate < maxValidYear || birthDate > minValidYear) {
                System.out.println("Invalid input. Experience candidates must be born between " + maxValidYear + " and " + minValidYear + ".");
                isValidBirthDate = false;
            } else {
                // If all checks pass, the birth year is valid
                isValidBirthDate = true;
            }
        } while (!isValidBirthDate);

        // Get and validate address
        String address;
        do {
            System.out.print("Enter address: ");
            address = sc.nextLine();
            if (!validator.isValidAddress(address)) {
                System.out.println("Invalid input. Address cannot be empty.");
            }
        } while (!validator.isValidAddress(address));

        // Get and validate phone number
        String phone;
        do {
            System.out.print("Enter phone: ");
            phone = sc.nextLine();
            if (!validator.isValidPhone(phone)) {
                System.out.println("Invalid input. Please enter a valid Vietnamese phone number.");
            }
        } while (!validator.isValidPhone(phone));

        // Get and validate email
        String email;
        do {
            System.out.print("Enter email: ");
            email = sc.nextLine();
            if (!validator.isValidEmail(email)) {
                System.out.println("Invalid input. Please enter a valid email address.");
            }
        } while (!validator.isValidEmail(email));

        // Get and validate years of experience
        int expInYear = 0;
        boolean validInput = false;
        do {
            System.out.print("Enter years of experience: ");
            String expInYearStr = sc.nextLine();

            if (!validator.isValidInteger(expInYearStr)) {
                System.out.println("Invalid input. Please enter a valid number.");
                continue;
            }

            expInYear = Integer.parseInt(expInYearStr);
            int maxExperience = Calendar.getInstance().get(Calendar.YEAR) - birthDate - Experience.MIN_AGE;

            if (!validator.isValidExperienceYears(expInYear, birthDate)) {
                System.out.println("Invalid input. Years of experience must be between "
                        + Experience.MIN_EXPERIENCE + " and " + maxExperience + ".");
            } else {
                validInput = true;
            }
        } while (!validInput);

        // Get and validate professional skill
        String proSkill;
        do {
            System.out.print("Enter professional skill: ");
            proSkill = sc.nextLine();
            if (!validator.isValidName(proSkill)) {
                System.out.println("Invalid input. Professional skill can only contain letters and spaces.");
            }
        } while (!validator.isValidName(proSkill));

        // Add the new Experience candidate
        candidates.add(new Experience(candidateId, firstName, lastName, birthDate,
                address, phone, email, 0, expInYear, proSkill));

        if (askToContinue()) {
            showExperienceCandidates();
            createExperienceCandidate();
        }
    }

    /**
     * Creates a Fresher candidate by collecting and validating user input.
     */
    public void createFresherCandidate() {
        // Get and validate candidate ID
        String candidateId;
        do {
            System.out.print("Enter candidate ID: ");
            candidateId = sc.nextLine();
            if (!validator.isValidId(candidateId)) {
                System.out.println("Invalid input. Please enter letters and numbers only.");
            } else if (validator.isExisted(candidateId, candidates)) {
                System.out.println("This ID already exists. Please enter a different one.");
            }
        } while (!validator.isValidId(candidateId) || validator.isExisted(candidateId, candidates));

        // Get and validate first name
        String firstName;
        do {
            System.out.print("Enter first name: ");
            firstName = sc.nextLine();
            if (!validator.isValidName(firstName)) {
                System.out.println("Invalid input. Please enter letters and spaces only.");
            }
        } while (!validator.isValidName(firstName));

        // Get and validate last name
        String lastName;
        do {
            System.out.print("Enter last name: ");
            lastName = sc.nextLine();
            if (!validator.isValidName(lastName)) {
                System.out.println("Invalid input. Please enter letters and spaces only.");
            }
        } while (!validator.isValidName(lastName));

        // Input and validate birth year for Fresher candidates
        int birthDate;
        boolean isValidBirthDate;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int minValidYear = currentYear - Fresher.MIN_AGE;
        int maxValidYear = currentYear - Fresher.MAX_AGE;

        do {
            // Prompt the user to enter a birth year within the valid range
            System.out.print("Enter birth year (" + maxValidYear + " - " + minValidYear + "): ");
            String birthDateStr = sc.nextLine();

            // Validate if the input is a valid integer
            while (!validator.isValidInteger(birthDateStr)) {
                System.out.print("Invalid input. Please enter a valid year: ");
                birthDateStr = sc.nextLine();
            }

            // Convert the input string to an integer
            birthDate = Integer.parseInt(birthDateStr);

            // Check if the birth year is in the future
            if (birthDate > currentYear) {
                System.out.println("Error: Birth year cannot be in the future.");
                isValidBirthDate = false;
                // Check if the birth year falls within the allowed range

            } else if (birthDate < maxValidYear || birthDate > minValidYear) {
                System.out.println("Invalid input. Fresher candidates must be born between " + maxValidYear + " and " + minValidYear + ".");
                isValidBirthDate = false;
                // If all checks pass, the birth year is valid

            } else {
                isValidBirthDate = true;
            }
        } while (!isValidBirthDate);

        // Get and validate address
        String address;
        do {
            System.out.print("Enter address: ");
            address = sc.nextLine();
            if (!validator.isValidAddress(address)) {
                System.out.println("Invalid input. Address cannot be empty.");
            }
        } while (!validator.isValidAddress(address));

        // Get and validate phone number
        String phone;
        do {
            System.out.print("Enter phone: ");
            phone = sc.nextLine();
            if (!validator.isValidPhone(phone)) {
                System.out.println("Invalid input. Please enter a valid Vietnamese phone number.");
            }
        } while (!validator.isValidPhone(phone));

        // Get and validate email
        String email;
        do {
            System.out.print("Enter email: ");
            email = sc.nextLine();
            if (!validator.isValidEmail(email)) {
                System.out.println("Invalid input. Please enter a valid email address.");
            }
        } while (!validator.isValidEmail(email));

        // Get and validate graduation year
        int graduationDate;
        do {
            System.out.print("Enter graduation year: ");
            String graduationDateStr = sc.nextLine();
            while (!validator.isValidInteger(graduationDateStr)) {
                System.out.print("Invalid input. Please enter a valid year: ");
                graduationDateStr = sc.nextLine();
            }
            graduationDate = Integer.parseInt(graduationDateStr);
            if (!validator.isValidGraduationYear(graduationDate, birthDate)) {
                System.out.println("Invalid input. Graduation year must be between " + (birthDate + 18) + " and " + currentYear + ".");
            }
        } while (!validator.isValidGraduationYear(graduationDate, birthDate));

        // Get and validate graduation rank
        String graduationRank;
        do {
            System.out.print("Enter rank of graduation (Excellence, Good, Fair, Poor): ");
            graduationRank = sc.nextLine();
            if (!validator.isValidGraduationRank(graduationRank)) {
                System.out.println("Invalid input. Please enter one of: Excellence, Good, Fair, Poor");
            }
        } while (!validator.isValidGraduationRank(graduationRank));

        // Get and validate university name
        String universityName;
        do {
            System.out.print("Enter university name: ");
            universityName = sc.nextLine();
            if (!validator.isValidName(universityName)) {
                System.out.println("Invalid input. University name can only contain letters and spaces.");
            }
        } while (!validator.isValidName(universityName));

        // Add the new Fresher candidate
        candidates.add(new Fresher(candidateId, firstName, lastName, birthDate,
                address, phone, email, 1, graduationDate, graduationRank, universityName));

        if (askToContinue()) {
            showFresherCandidates();
            createFresherCandidate();
        }
    }

    /**
     * Creates an Intern candidate by collecting and validating user input.
     */
    public void createInternCandidate() {
        // Get and validate candidate ID
        String candidateId;
        do {
            System.out.print("Enter candidate ID: ");
            candidateId = sc.nextLine();
            if (!validator.isValidId(candidateId)) {
                System.out.println("Invalid input. Please enter letters and numbers only.");
            } else if (validator.isExisted(candidateId, candidates)) {
                System.out.println("This ID already exists. Please enter a different one.");
            }
        } while (!validator.isValidId(candidateId) || validator.isExisted(candidateId, candidates));

        // Get and validate first name
        String firstName;
        do {
            System.out.print("Enter first name: ");
            firstName = sc.nextLine();
            if (!validator.isValidName(firstName)) {
                System.out.println("Invalid input. Please enter letters and spaces only.");
            }
        } while (!validator.isValidName(firstName));

        // Get and validate last name
        String lastName;
        do {
            System.out.print("Enter last name: ");
            lastName = sc.nextLine();
            if (!validator.isValidName(lastName)) {
                System.out.println("Invalid input. Please enter letters and spaces only.");
            }
        } while (!validator.isValidName(lastName));
        // Input and validate birth year for Intern candidates
        int birthDate;
        boolean isValidBirthDate;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int minValidYear = currentYear - Intern.MIN_AGE;
        int maxValidYear = currentYear - Intern.MAX_AGE;

        do {
            // Prompt the user to enter a birth year within the valid range
            System.out.print("Enter birth year (" + maxValidYear + " - " + minValidYear + "): ");
            String birthDateStr = sc.nextLine();

            // Validate if the input is a valid integer
            while (!validator.isValidInteger(birthDateStr)) {
                System.out.print("Invalid input. Please enter a valid year: ");
                birthDateStr = sc.nextLine();
            }

            // Convert the input string to an integer
            birthDate = Integer.parseInt(birthDateStr);

            // Check if the birth year is in the future
            if (birthDate > currentYear) {
                System.out.println("Error: Birth year cannot be in the future.");
                isValidBirthDate = false;
            } else if (birthDate < maxValidYear || birthDate > minValidYear) {
                System.out.println("Invalid input. Intern candidates must be born between " + maxValidYear + " and " + minValidYear + ".");
                isValidBirthDate = false;
                // If all checks pass, the birth year is valid

            } else {
                isValidBirthDate = true;
            }
        } while (!isValidBirthDate);

        // Get and validate address
        String address;
        do {
            System.out.print("Enter address: ");
            address = sc.nextLine();
            if (!validator.isValidAddress(address)) {
                System.out.println("Invalid input. Address cannot be empty.");
            }
        } while (!validator.isValidAddress(address));

        // Get and validate phone number
        String phone;
        do {
            System.out.print("Enter phone: ");
            phone = sc.nextLine();
            if (!validator.isValidPhone(phone)) {
                System.out.println("Invalid input. Please enter a valid Vietnamese phone number.");
            }
        } while (!validator.isValidPhone(phone));

        // Get and validate email
        String email;
        do {
            System.out.print("Enter email: ");
            email = sc.nextLine();
            if (!validator.isValidEmail(email)) {
                System.out.println("Invalid input. Please enter a valid email address.");
            }
        } while (!validator.isValidEmail(email));

        // Get and validate major
        String major;
        do {
            System.out.print("Enter major: ");
            major = sc.nextLine();
            if (!validator.isValidMajor(major)) {
                System.out.println("Invalid input. Major can only contain letters and spaces.");
            }
        } while (!validator.isValidMajor(major));

        // Get and validate semester
        String semester;
        do {
            System.out.print("Enter semester: ");
            semester = sc.nextLine();
            if (!validator.isValidNonEmpty(semester)) {
                System.out.println("Invalid input. Semester cannot be empty.");
            }
        } while (!validator.isValidNonEmpty(semester));

        // Get and validate university name
        String universityName;
        do {
            System.out.print("Enter university name: ");
            universityName = sc.nextLine();
            if (!validator.isValidName(universityName)) {
                System.out.println("Invalid input. University name can only contain letters and spaces.");
            }
        } while (!validator.isValidName(universityName));

        // Add the new Intern candidate
        candidates.add(new Intern(candidateId, firstName, lastName, birthDate,
                address, phone, email, 2, major, semester, universityName));

        if (askToContinue()) {
            showInternCandidates();
            createInternCandidate();
        }
    }

    /**
     * Displays a formatted table of candidates with their details.
     *
     * @param candidates The list of Candidate objects to be displayed.
     */
    private void displayCandidateTable(List<Candidate> candidates) {
        // Print table header with column names
        System.out.println("+---+------------------------+----------+-------------------+------------+---------------------+-------------+");
        System.out.println("|No.| Fullname               | Birthdate| Address           | Phone      | Email               |    Type     |");
        System.out.println("+---+------------------------+----------+-------------------+------------+---------------------+-------------+");

        int index = 1;
        // Iterate through the list of candidates and print their details
        for (Candidate candidate : candidates) {
            String type;
            // Determine the candidate type based on the instance
            if (candidate instanceof Experience) {
                type = "Experience"; // Assign type "Experience" if candidate is an Experience object
            } else if (candidate instanceof Fresher) {
                type = "Fresher"; // Assign type "Fresher" if candidate is a Fresher object
            } else {
                type = "Intern"; // Assign type "Intern" if candidate is an Intern object
            }

            System.out.printf("|%3d| %-23s| %9s| %-18s| %11s| %-20s| %-4s|\n",
                    index++,
                    candidate.getFullName(), // Candidate's full name
                    candidate.getBirthDate(), // Candidate's birth year
                    candidate.getAddress(), // Candidate's address
                    candidate.getPhone(), // Candidate's phone number
                    candidate.getEmail(), // Candidate's email
                    type); // Candidate's type (Experience, Fresher, or Intern)
        }
        System.out.println("+---+------------------------+----------+-------------------+------------+---------------------+-------------+");
    }

    /**
     * Searches for candidates based on a user-provided keyword. The search is
     * case-insensitive and checks multiple attributes, including ID, full name,
     * phone number, email, address, birth date, and candidate type (Experience,
     * Fresher, Intern).
     */
    public void searchCandidates() {
        // Prompt the user to input a search keyword
        System.out.print("Input Candidate name (First name, Last name, Address, Phone, Email, Birth Year): ");
        String searchKey = sc.nextLine().toLowerCase(); // Convert input to lowercase for case-insensitive search
        // Filter the list of candidates based on the search key
        List<Candidate> foundCandidates = candidates.stream()
                .filter(c -> c.getCandidateId().toLowerCase().contains(searchKey) // Search by candidate ID
                || c.getFullName().toLowerCase().contains(searchKey) // Search by full name
                || c.getPhone().contains(searchKey) // Search by phone number
                || c.getEmail().toLowerCase().contains(searchKey) // Search by email
                || c.getAddress().toLowerCase().contains(searchKey) // Search by address
                || (c instanceof Experience && "experience".contains(searchKey)) // Search by Experience type
                || (c instanceof Fresher && "fresher".contains(searchKey)) // Search by Fresher type
                || (c instanceof Intern && "intern".contains(searchKey))) // Search by Intern type
                .collect(Collectors.toList()); // Collect matching candidates into a list
        // Display search results
        if (!foundCandidates.isEmpty()) {
            System.out.println("\nThe candidates found:");
            displayCandidateTable(foundCandidates); // Display matching candidates in a formatted table
        } else {
            System.out.println("No candidates found with the search key: " + searchKey); // Notify if no matches are found
        }
    }

    /**
     * Helper method to ask if the user wants to continue adding candidates.
     *
     * @return true if the user wants to continue, false otherwise.
     */
    private boolean askToContinue() {
        String choiceStr;
        do {
            System.out.print("\nDo you want to continue to add more candidates? (Y/N): ");
            choiceStr = sc.nextLine();
            if (!choiceStr.equalsIgnoreCase("Y") && !choiceStr.equalsIgnoreCase("N")) {
                System.out.println("Invalid input. Please enter Y or N.");
            }
        } while (!choiceStr.equalsIgnoreCase("Y") && !choiceStr.equalsIgnoreCase("N"));

        return choiceStr.equalsIgnoreCase("Y");
    }

    /**
     * Main menu of the application that allows user to choose actions.
     */
    public void mainMenu() {
        while (true) {

            System.out.println("CANDIDATE MANAGEMENT SYSTEM");
            System.out.println("1. Experience");
            System.out.println("2. Fresher");
            System.out.println("3. Internship");
            System.out.println("4. Searching");
            System.out.println("5. Exit");
            System.out.print("Please choose: ");

            String choiceStr = sc.nextLine();
            while (!validator.isValidInteger(choiceStr)) {
                System.out.print("Invalid input. Please enter a number from 1 - 5! ");
                choiceStr = sc.nextLine();
            }

            switch (Integer.parseInt(choiceStr)) {
                case 1:
                    createExperienceCandidate();
                    break;
                case 2:
                    createFresherCandidate();
                    break;
                case 3:
                    createInternCandidate();
                    break;
                case 4:
                    searchCandidates();
                    break;
                case 5:
                    System.out.println("Exiting system. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please select again!");
            }
        }
    }
}
