package Excel;

import Data.*;
import Utils.Logs;
import Utils.Options;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

final class FileManager {

    /**
     * Class constructor
     */
    private FileManager() {
    }

    /**
     * Create path for dataBase
     *
     * @param _data the dataBase
     * @return the total path file name
     * @throws IOException if it's impossible to create the directory
     */
    static String getPathFor(DataBase _data) throws IOException {
        return createPathEnd(createPath(""), _data);
    }

    /**
     * Create path for kingdom
     *
     * @param _kindgom the kingdom
     * @return the total path file name
     * @throws IOException if it's impossible to create the directory
     */
    static String getPathFor(Kingdom _kindgom) throws IOException {
        return createPathEnd(createPath(""), _kindgom);
    }

    /**
     * Create path for group
     *
     * @param _group the group
     * @return the total path file name
     * @throws IOException if it's impossible to create the directory
     */
    static String getPathFor(Group _group) throws IOException {
        String groupPath = _group.getKingdomName();
        return createPathEnd(createPath(groupPath), _group);
    }

    /**
     * Create path for subGroup
     *
     * @param _subGroup the subGroup
     * @return the total path file name
     * @throws IOException if it's impossible to create the directory
     */
    static String getPathFor(SubGroup _subGroup) throws IOException {
        String subGroupPath = _subGroup.getKingdomName() + File.separator + _subGroup.getGroupName();
        return createPathEnd(createPath(subGroupPath), _subGroup);
    }

    /**
     * Create path for organism
     *
     * @param _organism the organism
     * @return the total path file name
     * @throws IOException if it's impossible to create the directory
     */
    static String getPathFor(Organism _organism) throws IOException {
        String organismPath = _organism.getKingdomName() + File.separator + _organism.getGroupName() + File.separator + _organism.getSubGroupName();
        return createPath(organismPath) + File.separator + _organism.getName() + Options.getExcelExtension();
    }

    /**
     * Create path prefix
     *
     * @param _pathName the sub path to create
     * @return the prefix
     * @throws IOException if it's impossible to create the directory
     */
    private static String createPath(String _pathName) throws IOException {
        final String pathName = Options.getResultDirectory() + File.separator + _pathName;

        final Path Path = Paths.get(pathName);
        if (Files.notExists(Path)) {
            try {
                Files.createDirectories(Path);
            } catch (IOException e) {
                final String message = "Unable to create the path : " + pathName;
                Logs.warning(message);
                Logs.exception(new IOException(message, e));
                throw e;
            }
        }

        return pathName;
    }

    /**
     * Create path suffix
     *
     * @param _prefix    the prefix to used first
     * @param _idataBase the IDataBase use to get the name
     * @return the total path file name
     */
    private static String createPathEnd(String _prefix, IDataBase _idataBase) {
        return _prefix + File.separator + Options.getTotalPrefix() + _idataBase.getName() + Options.getExcelExtension();
    }
}
