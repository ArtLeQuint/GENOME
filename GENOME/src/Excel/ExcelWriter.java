package Excel;

import Data.*;
import Utils.Logs;
import Utils.Options;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;


public final class ExcelWriter {

    // EDIT STYLE HERE :
    private static final XSSFColor s_PRIMARY_COLOR = new XSSFColor(new Color(104, 151, 187));
    private static final XSSFColor s_ALTERNATING_COLOR_A = new XSSFColor(new Color(255, 249, 220));
    private static final XSSFColor s_ALTERNATING_COLOR_B = new XSSFColor(new Color(247, 247, 247));
    private static final XSSFColor s_BORDER_COLOR = new XSSFColor(new Color(68, 114, 196));
    private static final XSSFColor s_FONT_COLOR = new XSSFColor(new Color(0, 0, 0));
    private static final String s_FONT_NAME = "Calibri";
    private static final short s_FONT_SIZE = 11;
    private static final boolean s_BOLD = true;

    private XSSFWorkbook m_workbook;

    private XSSFCellStyle m_styleAlternateColorA;
    private XSSFCellStyle m_styleAlternateColorB;
    private XSSFCellStyle m_stylePrimaryColorBorders;
    private XSSFCellStyle m_styleAlternateColorABorders;
    private XSSFCellStyle m_styleAlternateColorBBorders;
    private XSSFCellStyle m_styleAlternateColorAFloat;
    private XSSFCellStyle m_styleAlternateColorBFloat;

    private XSSFCellStyle m_stylePrimaryColorLeftRightBorders;
    private XSSFCellStyle m_styleAlternateColorARightBorders;
    private XSSFCellStyle m_styleAlternateColorBRightBorders;

    /**
     * Class constructor
     *
     * @param _idataBase the data use to write
     */
    private ExcelWriter(IDataBase _idataBase) {
        writeWorkbook(_idataBase);
    }

    /**
     * Function call to write database summary in Excel workbook
     */
    public static void writeDatabase(DataBase _data) throws IOException {
        final String path = FileManager.getPathFor(_data);
        final ExcelWriter result = new ExcelWriter(_data);
        result.saveSheet(path);
    }

    /**
     * Function call to write kingdom summary in Excel workbook
     */
    public static void writeKingdom(Kingdom _kingdom) throws IOException {
        final String path = FileManager.getPathFor(_kingdom);
        final ExcelWriter result = new ExcelWriter(_kingdom);
        result.saveSheet(path);
    }

    /**
     * Function call to write group summary in Excel workbook
     */
    public static void writeGroup(Group _group) throws IOException {
        final String path = FileManager.getPathFor(_group);
        final ExcelWriter result = new ExcelWriter(_group);
        result.saveSheet(path);
    }

    /**
     * Function call to write subgroup summary in Excel workbook
     */
    public static void writeSubGroup(SubGroup _subGroup) throws IOException {
        final String path = FileManager.getPathFor(_subGroup);
        final ExcelWriter result = new ExcelWriter(_subGroup);
        result.saveSheet(path);
    }

    /**
     * Function call to write organism summary in Excel workbook
     */
    public static void writeOrganism(Organism _organism) throws IOException {
        final String path = FileManager.getPathFor(_organism);
        final ExcelWriter result = new ExcelWriter(_organism);
        result.createRepliconsSheets(_organism);
        result.saveSheet(path);
    }

    /**
     * Write a in a cell using the numeric format
     *
     * @param _cell the cell to write in
     * @param _f    the long to write in the numeric cell
     */
    private void writeNumericCell(XSSFCell _cell, float _f) {
        _cell.setCellValue(_f);
        _cell.setCellType(CellType.NUMERIC);
    }

    /**
     * Write a in a cell using the numeric format
     *
     * @param _cell the cell to write in
     * @param _l    the long to write in the numeric cell
     */
    private void writeNumericCell(XSSFCell _cell, long _l) {
        _cell.setCellValue(_l);
        _cell.setCellType(CellType.NUMERIC);
    }

    /**
     * Write the workbook
     *
     * @param _idataBase the data to write
     */
    private void writeWorkbook(IDataBase _idataBase) {
        m_workbook = new XSSFWorkbook();
        createStyles();
        final XSSFSheet generalInfoSheet = m_workbook.createSheet(_idataBase.getName());
        createGeneralInfoSheet(generalInfoSheet, _idataBase);
        createStatisticsSheets(_idataBase);
    }

    /**
     * Create style
     */
    private void createStyles() {
        final XSSFFont font = m_workbook.createFont();
        font.setFontHeightInPoints(s_FONT_SIZE);
        font.setFontName(s_FONT_NAME);
        font.setBold(s_BOLD);
        font.setColor(s_FONT_COLOR);

        m_stylePrimaryColorBorders = m_workbook.createCellStyle();
        m_stylePrimaryColorBorders.setFillForegroundColor(s_PRIMARY_COLOR);
        m_stylePrimaryColorBorders.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        m_stylePrimaryColorBorders.setFont(font);
        m_stylePrimaryColorBorders.setBorderTop(BorderStyle.MEDIUM);
        m_stylePrimaryColorBorders.setBorderBottom(BorderStyle.MEDIUM);
        m_stylePrimaryColorBorders.setBorderLeft(BorderStyle.MEDIUM);
        m_stylePrimaryColorBorders.setBorderRight(BorderStyle.MEDIUM);
        m_stylePrimaryColorBorders.setBorderColor(XSSFCellBorder.BorderSide.TOP, s_BORDER_COLOR);
        m_stylePrimaryColorBorders.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, s_BORDER_COLOR);
        m_stylePrimaryColorBorders.setBorderColor(XSSFCellBorder.BorderSide.LEFT, s_BORDER_COLOR);
        m_stylePrimaryColorBorders.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, s_BORDER_COLOR);

        m_stylePrimaryColorLeftRightBorders = m_workbook.createCellStyle();
        m_stylePrimaryColorLeftRightBorders.setFillForegroundColor(s_PRIMARY_COLOR);
        m_stylePrimaryColorLeftRightBorders.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        m_stylePrimaryColorLeftRightBorders.setFont(font);
        m_stylePrimaryColorLeftRightBorders.setBorderLeft(BorderStyle.MEDIUM);
        m_stylePrimaryColorLeftRightBorders.setBorderRight(BorderStyle.MEDIUM);
        m_stylePrimaryColorLeftRightBorders.setBorderColor(XSSFCellBorder.BorderSide.LEFT, s_BORDER_COLOR);
        m_stylePrimaryColorLeftRightBorders.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, s_BORDER_COLOR);

        m_styleAlternateColorA = m_workbook.createCellStyle();
        m_styleAlternateColorA.setFillForegroundColor(s_ALTERNATING_COLOR_A);
        m_styleAlternateColorA.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        m_styleAlternateColorA.setFont(font);

        m_styleAlternateColorAFloat = m_workbook.createCellStyle();
        m_styleAlternateColorAFloat.setFillForegroundColor(s_ALTERNATING_COLOR_A);
        m_styleAlternateColorAFloat.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        m_styleAlternateColorAFloat.setFont(font);
        m_styleAlternateColorAFloat.setDataFormat(m_workbook.createDataFormat().getFormat("0.00"));

        m_styleAlternateColorABorders = m_workbook.createCellStyle();
        m_styleAlternateColorABorders.setFillForegroundColor(s_ALTERNATING_COLOR_A);
        m_styleAlternateColorABorders.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        m_styleAlternateColorABorders.setFont(font);
        m_styleAlternateColorABorders.setBorderTop(BorderStyle.MEDIUM);
        m_styleAlternateColorABorders.setBorderBottom(BorderStyle.MEDIUM);
        m_styleAlternateColorABorders.setBorderLeft(BorderStyle.MEDIUM);
        m_styleAlternateColorABorders.setBorderRight(BorderStyle.MEDIUM);
        m_styleAlternateColorABorders.setBorderColor(XSSFCellBorder.BorderSide.TOP, s_BORDER_COLOR);
        m_styleAlternateColorABorders.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, s_BORDER_COLOR);
        m_styleAlternateColorABorders.setBorderColor(XSSFCellBorder.BorderSide.LEFT, s_BORDER_COLOR);
        m_styleAlternateColorABorders.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, s_BORDER_COLOR);

        m_styleAlternateColorBBorders = m_workbook.createCellStyle();
        m_styleAlternateColorBBorders.setFillForegroundColor(s_ALTERNATING_COLOR_B);
        m_styleAlternateColorBBorders.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        m_styleAlternateColorBBorders.setFont(font);
        m_styleAlternateColorBBorders.setBorderTop(BorderStyle.MEDIUM);
        m_styleAlternateColorBBorders.setBorderBottom(BorderStyle.MEDIUM);
        m_styleAlternateColorBBorders.setBorderLeft(BorderStyle.MEDIUM);
        m_styleAlternateColorBBorders.setBorderRight(BorderStyle.MEDIUM);
        m_styleAlternateColorBBorders.setBorderColor(XSSFCellBorder.BorderSide.TOP, s_BORDER_COLOR);
        m_styleAlternateColorBBorders.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, s_BORDER_COLOR);
        m_styleAlternateColorBBorders.setBorderColor(XSSFCellBorder.BorderSide.LEFT, s_BORDER_COLOR);
        m_styleAlternateColorBBorders.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, s_BORDER_COLOR);

        m_styleAlternateColorARightBorders = m_workbook.createCellStyle();
        m_styleAlternateColorARightBorders.setFillForegroundColor(s_ALTERNATING_COLOR_A);
        m_styleAlternateColorARightBorders.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        m_styleAlternateColorARightBorders.setFont(font);
        m_styleAlternateColorARightBorders.setBorderRight(BorderStyle.MEDIUM);
        m_styleAlternateColorARightBorders.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, s_BORDER_COLOR);

        m_styleAlternateColorB = m_workbook.createCellStyle();
        m_styleAlternateColorB.setFillForegroundColor(s_ALTERNATING_COLOR_B);
        m_styleAlternateColorB.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        m_styleAlternateColorB.setFont(font);

        m_styleAlternateColorBFloat = m_workbook.createCellStyle();
        m_styleAlternateColorBFloat.setFillForegroundColor(s_ALTERNATING_COLOR_B);
        m_styleAlternateColorBFloat.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        m_styleAlternateColorBFloat.setFont(font);
        m_styleAlternateColorBFloat.setDataFormat(m_workbook.createDataFormat().getFormat("0.00"));

        XSSFCellStyle styleAlternateColorBBorders = m_workbook.createCellStyle();
        styleAlternateColorBBorders.setFillForegroundColor(s_ALTERNATING_COLOR_B);
        styleAlternateColorBBorders.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleAlternateColorBBorders.setFont(font);
        styleAlternateColorBBorders.setBorderTop(BorderStyle.MEDIUM);
        styleAlternateColorBBorders.setBorderBottom(BorderStyle.MEDIUM);
        styleAlternateColorBBorders.setBorderLeft(BorderStyle.MEDIUM);
        styleAlternateColorBBorders.setBorderRight(BorderStyle.MEDIUM);
        styleAlternateColorBBorders.setBorderColor(XSSFCellBorder.BorderSide.TOP, s_BORDER_COLOR);
        styleAlternateColorBBorders.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, s_BORDER_COLOR);
        styleAlternateColorBBorders.setBorderColor(XSSFCellBorder.BorderSide.LEFT, s_BORDER_COLOR);
        styleAlternateColorBBorders.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, s_BORDER_COLOR);

        m_styleAlternateColorBRightBorders = m_workbook.createCellStyle();
        m_styleAlternateColorBRightBorders.setFillForegroundColor(s_ALTERNATING_COLOR_B);
        m_styleAlternateColorBRightBorders.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        m_styleAlternateColorBRightBorders.setFont(font);
        m_styleAlternateColorBRightBorders.setBorderRight(BorderStyle.MEDIUM);
        m_styleAlternateColorBRightBorders.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, s_BORDER_COLOR);

        short formatFloat = m_workbook.createDataFormat().getFormat("0.##");
        m_styleAlternateColorAFloat.setDataFormat(formatFloat);
        m_styleAlternateColorBFloat.setDataFormat(formatFloat);
    }

    /**
     * Function do save the excel sheet
     *
     * @param _path the path of the file
     * @throws IOException if an exception appear
     */
    private void saveSheet(String _path) throws IOException {
        //	Check if file exist
        final File file = new File(_path);
        if (file.exists()) {
            try {
                if (!file.delete()) {
                    Logs.warning("Enable to delete file : " + file.getName());
                }
            } catch (SecurityException e) {
                Logs.warning("Enable to delete file : " + file.getName());
                Logs.exception(e);
            }
        }
        try {
            if (!file.createNewFile()) {
                Logs.warning("Enable to create file : " + file.getName());
            }
        } catch (IOException | SecurityException e) {
            Logs.exception(e);
            throw e;
        }

        // Save file
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            m_workbook.write(fileOut);
        } catch (IOException e) {
            final String message = "Unable to save file : " + _path;
            Logs.warning(message);
            Logs.exception(new IOException(message, e));
            throw e;
        }
    }

    /**
     * Create the General Info Sheet of the excel file
     *
     * @param _general_info_sheet the sheet to write
     * @param _data               the data to write
     */
    private void createGeneralInfoSheet(XSSFSheet _general_info_sheet, IDataBase _data) {
        _general_info_sheet.createRow(0).createCell(0).setCellValue("Information");
        _general_info_sheet.getRow(0).getCell(0).setCellStyle(m_stylePrimaryColorBorders);

        XSSFRow r;

        r = _general_info_sheet.createRow(2);
        r.createCell(0).setCellValue("Name");
        r.getCell(0).setCellStyle(m_stylePrimaryColorBorders);
        r.createCell(1).setCellValue(_data.getName());
        r.getCell(1).setCellStyle(m_styleAlternateColorABorders);
        r.createCell(5).setCellValue("Genome");
        r.getCell(5).setCellStyle(m_stylePrimaryColorBorders);

        r = _general_info_sheet.createRow(4);
        r.createCell(0).setCellValue("Modification Date");
        r.createCell(1).setCellValue(new SimpleDateFormat("d MMM yyyy").format(_data.getModificationDate()));
        r.getCell(0).setCellStyle(m_stylePrimaryColorBorders);
        r.getCell(1).setCellStyle(m_styleAlternateColorABorders);

        r = _general_info_sheet.createRow(6);
        r.createCell(0).setCellValue("Total number of CDS sequences");
        r.createCell(1).setCellValue(_data.getCDSNumber());
        r.getCell(0).setCellStyle(m_stylePrimaryColorBorders);
        r.getCell(1).setCellStyle(m_styleAlternateColorABorders);

        r = _general_info_sheet.createRow(8);
        r.createCell(0).setCellValue("Number of valid CDS");
        r.createCell(1).setCellValue(_data.getValidCDSNumber());
        r.getCell(0).setCellStyle(m_stylePrimaryColorBorders);
        r.getCell(1).setCellStyle(m_styleAlternateColorABorders);

        r = _general_info_sheet.createRow(10);
        r.createCell(0).setCellValue("Number of invalid CDS");
        r.createCell(1).setCellValue(_data.getCDSNumber() - _data.getValidCDSNumber());
        r.getCell(0).setCellStyle(m_stylePrimaryColorBorders);
        r.getCell(1).setCellStyle(m_styleAlternateColorABorders);

        if (_data.getClass() != Organism.class) {
            r = _general_info_sheet.createRow(12);
            r.createCell(0).setCellValue("Number of Organisms");
            r.createCell(1).setCellValue(_data.getTotalOrganism());
            r.getCell(0).setCellStyle(m_stylePrimaryColorBorders);
            r.getCell(1).setCellStyle(m_styleAlternateColorABorders);
        } else {
            r = _general_info_sheet.createRow(12);
            r.createCell(0).setCellValue("Id");
            r.createCell(1).setCellValue(((Organism) _data).getId());
            r.getCell(0).setCellStyle(m_stylePrimaryColorBorders);
            r.getCell(1).setCellStyle(m_styleAlternateColorABorders);

            r = _general_info_sheet.createRow(14);
            r.createCell(0).setCellValue("Version");
            r.createCell(1).setCellValue(((Organism) _data).getVersion());
            r.getCell(0).setCellStyle(m_stylePrimaryColorBorders);
            r.getCell(1).setCellStyle(m_styleAlternateColorABorders);
        }

        int genomeInfoRowNumber = 3;
        for (Map.Entry<Statistics.Type, Long> entry : _data.getGenomeNumber().entrySet()) {
            XSSFRow row = _general_info_sheet.getRow(genomeInfoRowNumber);
            if (row == null) {
                row = _general_info_sheet.createRow(genomeInfoRowNumber);
            }
            row.createCell(5).setCellValue(entry.getKey().toString());
            writeNumericCell(row.createCell(6), entry.getValue());
            row.getCell(5).setCellStyle(m_styleAlternateColorABorders);
            row.getCell(6).setCellStyle(m_styleAlternateColorBBorders);
            genomeInfoRowNumber++;
        }

        _general_info_sheet.setColumnWidth(0, 7340);
        _general_info_sheet.autoSizeColumn(1);
        _general_info_sheet.setColumnWidth(5, 4450);
    }

    /**
     * Create a sheet for statistics
     *
     * @param _data, the data use to create statistics
     */
    private void createStatisticsSheets(IDataBase _data) {
        for (Statistics stat : _data.getStatistics().values()) {
            final XSSFSheet s = m_workbook.createSheet(Options.getSumPrefix() + stat.getType());
            createHeaderSheet(s);
            createTable(s, stat);
        }
    }

    /**
     * Create a sheet for replicon
     *
     * @param _organism, the data use to create statistics
     */
    private void createRepliconsSheets(Organism _organism) {
        for (Replicon replicon : _organism.getReplicons()) {
            final XSSFSheet s = m_workbook.createSheet(replicon.getName());
            createHeaderSheet(s);
            createTable(s, replicon);
        }
    }

    /**
     * Create the header of the sheet
     *
     * @param _sheet the sheet requiring a header
     */
    private void createHeaderSheet(XSSFSheet _sheet) {
        XSSFRow r = _sheet.createRow(0);
        XSSFCell c = r.createCell(0);

        // TRINUC TABLE
        c.setCellValue("Trinucleotide");
        c.setCellStyle(m_stylePrimaryColorBorders);
        _sheet.setColumnWidth(0, 3600);
        c = r.createCell(1);
        c.setCellValue(Statistics.StatLong.PHASE0.toString());
        _sheet.setColumnWidth(1, 3000);
        c.setCellStyle(m_stylePrimaryColorBorders);
        c = r.createCell(2);
        c.setCellValue(Statistics.StatFloat.FREQ0.toString());
        _sheet.setColumnWidth(2, 3000);
        c.setCellStyle(m_stylePrimaryColorBorders);
        c = r.createCell(3);
        c.setCellValue(Statistics.StatLong.PHASE1.toString());
        _sheet.setColumnWidth(3, 3000);
        c.setCellStyle(m_stylePrimaryColorBorders);
        c = r.createCell(4);
        c.setCellValue(Statistics.StatFloat.FREQ1.toString());
        _sheet.setColumnWidth(4, 3000);
        c.setCellStyle(m_stylePrimaryColorBorders);
        c = r.createCell(5);
        c.setCellValue(Statistics.StatLong.PHASE2.toString());
        _sheet.setColumnWidth(5, 3000);
        c.setCellStyle(m_stylePrimaryColorBorders);
        c = r.createCell(6);
        c.setCellValue(Statistics.StatFloat.FREQ2.toString());
        _sheet.setColumnWidth(6, 3000);
        c.setCellStyle(m_stylePrimaryColorBorders);
        c = r.createCell(7);
        c.setCellValue(Statistics.StatLong.PREF0.toString());
        _sheet.setColumnWidth(7, 3000);
        c.setCellStyle(m_stylePrimaryColorBorders);
        c = r.createCell(8);
        c.setCellValue(Statistics.StatLong.PREF1.toString());
        _sheet.setColumnWidth(8, 3000);
        c.setCellStyle(m_stylePrimaryColorBorders);
        c = r.createCell(9);
        c.setCellValue(Statistics.StatLong.PREF2.toString());
        _sheet.setColumnWidth(9, 3000);
        c.setCellStyle(m_stylePrimaryColorBorders);

        // DINUC TABLE
        c = r.createCell(12);
        c.setCellValue("Dinucléotides");
        c.setCellStyle(m_stylePrimaryColorBorders);
        _sheet.setColumnWidth(12, 3600);
        c = r.createCell(13);
        c.setCellValue(Statistics.StatLong.PHASE0.toString());
        _sheet.setColumnWidth(13, 3000);
        c.setCellStyle(m_stylePrimaryColorBorders);
        c = r.createCell(14);
        c.setCellValue(Statistics.StatFloat.FREQ0.toString());
        _sheet.setColumnWidth(14, 3000);
        c.setCellStyle(m_stylePrimaryColorBorders);
        c = r.createCell(15);
        c.setCellValue(Statistics.StatLong.PHASE1.toString());
        _sheet.setColumnWidth(15, 3000);
        c.setCellStyle(m_stylePrimaryColorBorders);
        c = r.createCell(16);
        c.setCellValue(Statistics.StatFloat.FREQ1.toString());
        _sheet.setColumnWidth(16, 3000);
        c.setCellStyle(m_stylePrimaryColorBorders);
        c = r.createCell(17);
        c.setCellValue(Statistics.StatLong.PREF0.toString());
        _sheet.setColumnWidth(17, 3000);
        c.setCellStyle(m_stylePrimaryColorBorders);
        c = r.createCell(18);
        c.setCellValue(Statistics.StatLong.PREF1.toString());
        _sheet.setColumnWidth(18, 3000);
        c.setCellStyle(m_stylePrimaryColorBorders);


    }

    /**
     * Create the statistic table in the excel file
     *
     * @param _sheet the excel sheet
     * @param _stat  the statistics to write in the sheet
     */
    private void createTable(XSSFSheet _sheet, Statistics _stat) {
        XSSFRow r;
        XSSFCell c;

        // TRINUC TABLE
        XSSFCellStyle style1 = m_styleAlternateColorB;
        XSSFCellStyle style2 = m_styleAlternateColorA;
        XSSFCellStyle style3 = m_styleAlternateColorARightBorders;
        XSSFCellStyle alternate1;
        XSSFCellStyle alternate2 = m_styleAlternateColorBRightBorders;
        XSSFCellStyle styleFloat1 = m_styleAlternateColorBFloat;
        XSSFCellStyle styleFloat2 = m_styleAlternateColorAFloat;
        int i = 1;
        for (Statistics.Trinucleotide tri : Statistics.Trinucleotide.values()) {

            // Design for color 1/2
            alternate1 = style1;
            style1 = style2;
            style2 = alternate1;
            alternate1 = alternate2;
            alternate2 = style3;
            style3 = alternate1;
            alternate1 = styleFloat1;
            styleFloat1 = styleFloat2;
            styleFloat2 = alternate1;

            Tuple row = _stat.getTriTable()[tri.ordinal()];
            r = _sheet.createRow(i);
            c = r.createCell(0);
            c.setCellValue(tri.toString());
            c.setCellStyle(m_stylePrimaryColorLeftRightBorders);

            c = r.createCell(1);
            writeNumericCell(c, row.get(Statistics.StatLong.PHASE0));
            c.setCellStyle(style1);

            c = r.createCell(2);
            writeNumericCell(c, row.get(Statistics.StatFloat.FREQ0) * 100);
            c.setCellStyle(styleFloat1);

            c = r.createCell(3);
            writeNumericCell(c, row.get(Statistics.StatLong.PHASE1));
            c.setCellStyle(style1);

            c = r.createCell(4);
            writeNumericCell(c, row.get(Statistics.StatFloat.FREQ1) * 100);
            c.setCellStyle(styleFloat1);

            c = r.createCell(5);
            writeNumericCell(c, row.get(Statistics.StatLong.PHASE2));
            c.setCellStyle(style1);

            c = r.createCell(6);
            writeNumericCell(c, row.get(Statistics.StatFloat.FREQ2) * 100);
            c.setCellStyle(styleFloat1);

            c = r.createCell(7);
            writeNumericCell(c, row.get(Statistics.StatLong.PREF0));
            c.setCellStyle(style2);

            c = r.createCell(8);
            writeNumericCell(c, row.get(Statistics.StatLong.PREF1));
            c.setCellStyle(style2);

            c = r.createCell(9);
            writeNumericCell(c, row.get(Statistics.StatLong.PREF2));
            c.setCellStyle(style3);
            i++;
        }

        r = _sheet.createRow(i);
        c = r.createCell(0);
        c.setCellValue("TOTAL");
        c.setCellStyle(m_stylePrimaryColorBorders);
        for (int p = 1; p <= 9; p++) {
            c = r.createCell(p);
            c.setCellType(CellType.NUMERIC);
            // JUST TO KNOW : A + P IS OKAY ONLY BECAUSE WE DON'T GO FURTHER THAN Z. REMEMBER IT
            c.setCellFormula("SUM(" + (char) ('A' + p) + "2:" + (char) ('A' + p) + "65)");
            c.setCellStyle(m_stylePrimaryColorBorders);
        }

        // DINUC TABLE
        style1 = m_styleAlternateColorB;
        style2 = m_styleAlternateColorA;
        style3 = m_styleAlternateColorARightBorders;
        alternate2 = m_styleAlternateColorBRightBorders;
        styleFloat1 = m_styleAlternateColorBFloat;
        styleFloat2 = m_styleAlternateColorAFloat;
        i = 1;
        for (Statistics.Dinucleotide tri : Statistics.Dinucleotide.values()) {

            // Design for color 1/2
            alternate1 = style1;
            style1 = style2;
            style2 = alternate1;
            alternate1 = alternate2;
            alternate2 = style3;
            style3 = alternate1;
            alternate1 = styleFloat1;
            styleFloat1 = styleFloat2;
            styleFloat2 = alternate1;

            Tuple row = _stat.getDiTable()[tri.ordinal()];
            r = _sheet.getRow(i);
            c = r.createCell(12);
            c.setCellValue(tri.toString());
            c.setCellStyle(m_stylePrimaryColorLeftRightBorders);

            c = r.createCell(13);
            writeNumericCell(c, row.get(Statistics.StatLong.PHASE0));
            c.setCellStyle(style1);

            c = r.createCell(14);
            writeNumericCell(c, row.get(Statistics.StatFloat.FREQ0) * 100);
            c.setCellStyle(styleFloat1);

            c = r.createCell(15);
            writeNumericCell(c, row.get(Statistics.StatLong.PHASE1));
            c.setCellStyle(style1);

            c = r.createCell(16);
            writeNumericCell(c, row.get(Statistics.StatFloat.FREQ1) * 100);
            c.setCellStyle(styleFloat1);

            c = r.createCell(17);
            writeNumericCell(c, row.get(Statistics.StatLong.PREF0));
            c.setCellStyle(style2);

            c = r.createCell(18);
            writeNumericCell(c, row.get(Statistics.StatLong.PREF1));
            c.setCellStyle(style3);
            i++;
        }

        r = _sheet.getRow(i);
        c = r.createCell(12);
        c.setCellValue("TOTAL");
        c.setCellStyle(m_stylePrimaryColorBorders);
        for (int p = 1; p <= 6; p++) {
            c = r.createCell(p + 12);
            c.setCellType(CellType.NUMERIC);
            // JUST TO KNOW : A + P IS OKAY ONLY BECAUSE WE DON'T GO FURTHER THAN Z. REMEMBER IT
            c.setCellFormula("SUM(" + (char) ('M' + p) + "2:" + (char) ('M' + p) + "17)");
            c.setCellStyle(m_stylePrimaryColorBorders);
        }

        i += 2;
        r = _sheet.getRow(i);
        r.createCell(12).setCellValue("Total number of CDS sequences");
        r.createCell(15).setCellValue(_stat.getCDSNumber());
        _sheet.addMergedRegion(new CellRangeAddress(i, i, 12, 14));
        r.getCell(12).setCellStyle(m_stylePrimaryColorBorders);
        r.getCell(15).setCellStyle(m_stylePrimaryColorBorders);

        i += 2;
        r = _sheet.getRow(i);
        r.createCell(12).setCellValue("Number of valid CDS");
        r.createCell(15).setCellValue(_stat.getValidCDSNumber());
        _sheet.addMergedRegion(new CellRangeAddress(i, i, 12, 14));
        r.getCell(12).setCellStyle(m_stylePrimaryColorBorders);
        r.getCell(15).setCellStyle(m_stylePrimaryColorBorders);

        i += 2;
        r = _sheet.getRow(i);
        r.createCell(12).setCellValue("Number of invalids CDS");
        r.createCell(15).setCellValue(_stat.getCDSNumber() - _stat.getValidCDSNumber());
        _sheet.addMergedRegion(new CellRangeAddress(i, i, 12, 14));
        r.getCell(12).setCellStyle(m_stylePrimaryColorBorders);
        r.createCell(13).setCellStyle(m_stylePrimaryColorBorders);
        r.createCell(14).setCellStyle(m_stylePrimaryColorBorders);
        r.getCell(15).setCellStyle(m_stylePrimaryColorBorders);
    }
}

