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
public class Candidate {

    protected String candidateId;    // Candidate ID
    protected String firstName;      // First name of the candidate
    protected String lastName;       // Last name of the candidate
    protected int birthDate;         // Birth date (year)
    protected String address;        // Address of the candidate
    protected String phone;          // Phone number of the candidate
    protected String email;          // Email of the candidate
    protected int candidateType;  // Type of candidate (Experience, Fresher, Intern)

    /**
     * Constructor to initialize candidate details.
     *
     * @param candidateId Candidate ID.
     * @param firstName First name of the candidate.
     * @param lastName Last name of the candidate.
     * @param birthDate Birth date of the candidate.
     * @param address Address of the candidate.
     * @param phone Phone number of the candidate.
     * @param email Email of the candidate.
     * @param candidateType Type of candidate ( Experience, Fresher, Intern).
     */
    public Candidate(String candidateId, String firstName, String lastName, int birthDate,
            String address, String phone, String email, int candidateType) {
        this.candidateId = candidateId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.candidateType = candidateType;
    }

    // Getter and Setter Methods for all attributes
    /**
     * Get the candidate ID.
     *
     * @return Candidate ID.
     */
    public String getCandidateId() {
        return candidateId;
    }

    /**
     * Set the candidate ID.
     *
     * @param candidateId Candidate ID to set.
     */
    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    /**
     * Get the first name of the candidate.
     *
     * @return First name of the candidate.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the first name of the candidate.
     *
     * @param firstName First name to set.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get the last name of the candidate.
     *
     * @return Last name of the candidate.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set the last name of the candidate.
     *
     * @param lastName Last name to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Get the birth date of the candidate.
     *
     * @return Birth date of the candidate.
     */
    public int getBirthDate() {
        return birthDate;
    }

    /**
     * Set the birth date of the candidate.
     *
     * @param birthDate Birth date to set.
     */
    public void setBirthDate(int birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Get the address of the candidate.
     *
     * @return Address of the candidate.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the address of the candidate.
     *
     * @param address Address to set.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get the phone number of the candidate.
     *
     * @return Phone number of the candidate.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Set the phone number of the candidate.
     *
     * @param phone Phone number to set.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Get the email of the candidate.
     *
     * @return Email of the candidate.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email of the candidate.
     *
     * @param email Email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the candidate type (0 for Experience, 1 for Fresher, 2 for Intern).
     *
     * @return Candidate type.
     */
    public int getCandidateType() {
        return candidateType;
    }

    /**
     * Set the candidate type (0 for Experience, 1 for Fresher, 2 for Intern).
     *
     * @param candidateType Candidate type to set.
     */
    public void setCandidateType(int candidateType) {
        this.candidateType = candidateType;
    }

    /**
     * Method to get the full name of the candidate.
     *
     * @return Full name (First name + Last name).
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Method to display detailed information of the candidate. This method
     * prints the candidate's full name, birth date, address, phone number,
     * email, and candidate type in a formatted manner.
     *
     * @return A formatted string with candidate information.
     */
    @Override
    public String toString() {
        return String.format("| %-15s | %10d | %-10s | %10s | %-10s | %-10s |",
                getFullName(), birthDate, address, phone, email, candidateType);
    }

}
