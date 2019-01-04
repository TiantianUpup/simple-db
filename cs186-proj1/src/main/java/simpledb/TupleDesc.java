package simpledb;

import java.io.Serializable;
import java.util.*;

/**
 * TupleDesc describes the schema of a tuple.
 */
public class TupleDesc implements Serializable {

    /**
     * field collection
     * */
    private List<TDItem> tdItemList = new ArrayList<>(8);

    /**
     * A help class to facilitate organizing the information of each field
     * */
    public static class TDItem implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * The type of the field
         * */
        Type fieldType;
        
        /**
         * The name of the field
         * */
        String fieldName;

        public TDItem(Type t, String n) {
            this.fieldName = n;
            this.fieldType = t;
        }

        public String toString() {
            return fieldName + "(" + fieldType + ")";
        }
    }

    /**
     * @return
     * An iterator which iterates over all the field TDItems
     * that are included in this TupleDesc
     * */
    public Iterator<TDItem> iterator() {
        // some code goes here
        return tdItemList.iterator();
    }

    private static final long serialVersionUID = 1L;

    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the specified types, with associated named fields.
     * 
     * @param typeAr array specifying the number of and types of fields in this TupleDesc. It must contain at least one entry.
     * @param fieldAr array specifying the names of the fields. Note that names may be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {
        // some code goes here
        if (typeAr != null && typeAr.length > 0) {
            for (int i = 0; i < typeAr.length; i++) {
                tdItemList.add(new TDItem(typeAr[i], fieldAr[i]));
            }
        }
    }

    /**
     * Constructor. Create a new tuple desc with typeAr.length fields with
     * fields of the specified types, with anonymous (unnamed) fields.
     * 
     * @param typeAr
     * array specifying the number of and types of fields in this
     * TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
        // some code goes here
        if (typeAr != null && typeAr.length > 0) {
            for (int i = 0; i < typeAr.length; i++) {
                tdItemList.add(new TDItem(typeAr[i], null));
            }
        }
    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        // some code goes here
        return tdItemList.size();
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     * 
     * @param i index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
        if (i >= numFields() || i < 0) {
            throw new NoSuchElementException("Illegal number");
        }
        // some code goes here
        return tdItemList.get(i).fieldName;
    }

    /**
     * Gets the type of the ith field of this TupleDesc.
     * 
     * @param i The index of the field to get the type of. It must be a valid index.
     * @return the type of the ith field
     * @throws NoSuchElementException if i is not a valid field reference.
     */
    public Type getFieldType(int i) throws NoSuchElementException {
        if (i >= numFields() || i < 0) {
            throw new NoSuchElementException("Illegal number");
        }
        // some code goes here
        return tdItemList.get(i).fieldType;
    }

    /**
     * Find the index of the field with a given name.
     * 
     * @param name name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException if no field with a matching name is found.
     */
    public int fieldNameToIndex(String name) throws NoSuchElementException {
        // some code goes here
        int index = -1;
        for (int i = 0; i < tdItemList.size(); i++) {
            if (tdItemList.get(i).fieldName.equals(name)) {
                index = i;
                break;
            }
        }

        if (index < 0) {
            throw new NoSuchElementException("can't find such element");
        }

        return index;
    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     * Note that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
        // some code goes here
        int size = 0;
        for (int i = 0 ; i < tdItemList.size(); i++) {
            Type type = tdItemList.get(i).fieldType;
            size += type.getLen();
        }

        return size;
    }

    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields fields,
     * with the first td1.numFields coming from td1 and the remaining from td2.
     * 
     * @param td1 The TupleDesc with the first fields of the new TupleDesc
     * @param td2 The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc merge(TupleDesc td1, TupleDesc td2) {
        // some code goes here
        List<Type> typeList = new ArrayList<>();
        List<String> fieldList = new ArrayList<>();

        List<TDItem> tdItem1 = td1.tdItemList;
        List<TDItem> tdItem2 = td2.tdItemList;

        for (int i = 0; i < tdItem1.size(); i++) {
            Type type = tdItem1.get(i).fieldType;
            typeList.add(type);
            fieldList.add(tdItem1.get(i).fieldName);
        }

        for (int i = 0; i < tdItem2.size(); i++) {
            Type type = tdItem2.get(i).fieldType;
            typeList.add(type);
            fieldList.add(tdItem2.get(i).fieldName);
        }

        Type[] typeAr = new Type[typeList.size()];
        String[] fieldAr = new String[fieldList.size()];
        for (int i = 0; i < typeList.size(); i++) {
            typeAr[i] = typeList.get(i);
        }

        for (int i = 0; i < fieldList.size(); i++) {
            fieldAr[i] = fieldList.get(i);
        }

        TupleDesc newTupleDesc = new TupleDesc(typeAr, fieldAr);
        return newTupleDesc;
    }

    /**
     * Compares the specified object with this TupleDesc for equality. Two
     * TupleDescs are considered equal if they are the same size and if the n-th
     * type in this TupleDesc is equal to the n-th type in td.
     * 
     * @param o the Object to be compared for equality with this TupleDesc.
     *
     * @return true if the object is equal to this TupleDesc.
     */
    public boolean equals(Object o) {
        // some code goes here
        if(o != null && o instanceof TupleDesc) {
            if(((TupleDesc) o).tdItemList.size()==this.tdItemList.size()) {
                for(int i = 0; i < ((TupleDesc) o).tdItemList.size(); i++) {
                    //集合中的元素一个一个进行比较
                    if(!((TupleDesc) o).tdItemList.get(i).equals(this.tdItemList.get(i))) {
                        return false;
                    }
                }
                return true;
            }
        }

        return false;
    }

    public int hashCode() {
        // If you want to use TupleDesc as keys for HashMap, implement this so
        // that equal objects have equals hashCode() results
        return this.tdItemList.hashCode();
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although
     * the exact format does not matter.
     * 
     * @return String describing this descriptor.
     */
    public String toString() {
        // some code goes here
        StringBuilder sb=new StringBuilder();
        for(int i = 0; i < tdItemList.size(); i++){
            TDItem item = tdItemList.get(i);
            sb.append(item.fieldType).append("[").append(i).append("]")
                .append("(").append(item.fieldName).append("[").append(i).append("]").append(")");
        }
        return sb.toString();
    }
}
