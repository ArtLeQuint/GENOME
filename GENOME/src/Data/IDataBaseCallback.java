package Data;

public interface IDataBaseCallback {

    /**
     * Method to call when DataBase has finished is work.
     *
     * @param _dataBase the data use to update
     */
    void finish(DataBase _dataBase);

}
