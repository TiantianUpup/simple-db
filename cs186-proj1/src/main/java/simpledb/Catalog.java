package simpledb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * The Catalog keeps track of all available tables in the database and their
 * associated schemas.
 * For now, this is a stub catalog that must be populated with tables by a
 * user program before it can be used -- eventually, this should be converted
 * to a catalog that reads a catalog table from disk.
 */

public class Catalog {
    /**
     * 内部类
     * */
    class Table {
        /**
         * 表名
         * */
        private String name;

        /**
         * 表的主键
         * */
        private String pkeyField;

        private DbFile dbFile;

        public Table(DbFile dbFile, String name, String pkeyField) {
            this.name = name;
            this.pkeyField = pkeyField;
            this.dbFile = dbFile;
        }
    }

    /**
     * tableId和Table之间的映射
     * */
    private Map<Integer, Table> tableMap;

    /**
     * Constructor.
     * Creates a new, empty catalog.
     */
    public Catalog() {
        // some code goes here
        tableMap = new HashMap<>();
    }


    /**
     * Add a new table to the catalog.
     * This table's contents are stored in the specified DbFile.
     * @param file the contents of the table to add;  file.getId() is the identfier of
     * this file/tupledesc param for the calls getTupleDesc and getFile
     * @param name the name of the table -- may be an empty string.  May not be null.  If a name conflict exists, use the last table to be added as the table for a given name.
     * @param pkeyField the name of the primary key field
     *
     */
    public void addTable(DbFile file, String name, String pkeyField) {
        for (Map.Entry<Integer, Table> tableEntry : tableMap.entrySet()) {
            if (tableEntry.getValue().name.equals(name)) {
                throw new IllegalArgumentException("the table name is exist");
            }
        }

        tableMap.put(file.getId(), new Table(file, name, pkeyField));
    }

    public void addTable(DbFile file, String name) {
        addTable(file, name, "");
    }

    /**
     * Add a new table to the catalog.
     * This table has tuples formatted using the specified TupleDesc and its
     * contents are stored in the specified DbFile.
     * @param file the contents of the table to add;  file.getId() is the identfier of
     * this file/tupledesc param for the calls getTupleDesc and getFile
     */
    public void addTable(DbFile file) {
        addTable(file, (UUID.randomUUID()).toString());
    }

    /**
     * Return the id of the table with a specified name,
     * @throws NoSuchElementException if the table doesn't exist
     */
    public int getTableId(String name) throws NoSuchElementException {
        // some code goes here
        for (Map.Entry<Integer, Table> tableEntry : tableMap.entrySet()) {
            if (tableEntry.getValue().name.equals(name))  {
                return tableEntry.getValue().dbFile.getId();
            }
        }

        throw new NoSuchElementException("the table you find doesn't exist");
    }

    /**
     * Returns the tuple descriptor (schema) of the specified table
     * @param tableid The id of the table, as specified by the DbFile.getId() function passed to addTable
     * @throws NoSuchElementException if the table doesn't exist
     */
    public TupleDesc getTupleDesc(int tableid) throws NoSuchElementException {
        // some code goes here
        if (tableMap.containsKey(tableid)) {
            return getDbFile(tableid).getTupleDesc();
        }


        throw new NoSuchElementException("the table doesn't exist");
    }

    /**
     * Returns the DbFile that can be used to read the contents of the
     * specified table.
     * @param tableid The id of the table, as specified by the DbFile.getId()
     * function passed to addTable
     */
    public DbFile getDbFile(int tableid) throws NoSuchElementException {
        if (tableMap.containsKey(tableid)) {
            return tableMap.get(tableid).dbFile;
        }

        throw new NoSuchElementException("the table doesn't exist");
    }

    public String getPrimaryKey(int tableid) {
        // some code goes here
        if (tableMap.containsKey(tableid)) {
            return tableMap.get(tableid).pkeyField;
        }

        throw new NoSuchElementException("the table doesn't exist");
    }

    public Iterator<Integer> tableIdIterator() {
        // some code goes here
       return tableMap.keySet().iterator();
    }

    public String getTableName(int id) {
        // some code goes here
        if (tableMap.get(id)  != null) {
            return tableMap.get(id).name;
        }

        throw new NoSuchElementException("the table doesn't exist");
    }
    
    /** Delete all tables from the catalog */
    public void clear() {
        // some code goes here
        tableMap.clear();
    }
    
    /**
     * Reads the schema from a file and creates the appropriate tables in the database.
     * @param catalogFile
     */
    public void loadSchema(String catalogFile) {
        String line = "";
        //根目录
        String baseFolder=new File(catalogFile).getParent();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(catalogFile)));

            //读取catalogFile中的文件
            while ((line = br.readLine()) != null) {
                //assume line is of the format name (field type, field type, ...)
                String name = line.substring(0, line.indexOf("(")).trim();
                //System.out.println("TABLE NAME: " + name);
                String fields = line.substring(line.indexOf("(") + 1, line.indexOf(")")).trim();
                //els为field type 的集合 TupleDesc中的一组TdItem
                String[] els = fields.split(",");
                ArrayList<String> names = new ArrayList<String>();
                ArrayList<Type> types = new ArrayList<Type>();
                String primaryKey = "";
                for (String e : els) {
                    //els2[0]为field， els[1]为name
                    String[] els2 = e.trim().split(" ");
                    names.add(els2[0].trim());
                    //判断数据类型
                    if (els2[1].trim().toLowerCase().equals("int"))
                        types.add(Type.INT_TYPE);
                    else if (els2[1].trim().toLowerCase().equals("string"))
                        types.add(Type.STRING_TYPE);
                    else {
                        System.out.println("Unknown type " + els2[1]);
                        System.exit(0);
                    }

                    //即field type pk
                    if (els2.length == 3) {
                        if (els2[2].trim().equals("pk"))
                            primaryKey = els2[0].trim();
                        else {
                            System.out.println("Unknown annotation " + els2[2]);
                            System.exit(0);
                        }
                    }
                }


                Type[] typeAr = types.toArray(new Type[0]);
                String[] namesAr = names.toArray(new String[0]);
                //转化为TupleDesc
                TupleDesc t = new TupleDesc(typeAr, namesAr);
                //创建保存数据的文件
                HeapFile tabHf = new HeapFile(new File(baseFolder+"/"+name + ".dat"), t);
                //加入到catalog中
                addTable(tabHf,name,primaryKey);
                System.out.println("Added table : " + name + " with schema " + t);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (IndexOutOfBoundsException e) {
            System.out.println ("Invalid catalog entry : " + line);
            System.exit(0);
        }
    }
}

