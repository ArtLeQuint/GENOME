package Data;

public interface IOrganismCallback {

    /**
     * Method to call when Organism has finished is work.
     *
     * @param _organism the data use to update
     */
    void finish(Organism _organism);

}
