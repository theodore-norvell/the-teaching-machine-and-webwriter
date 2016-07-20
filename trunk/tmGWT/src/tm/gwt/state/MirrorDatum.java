package tm.gwt.state;

import java.util.ArrayList ;

import tm.interfaces.Datum ;

public class MirrorDatum implements Datum {

    final protected int address;
    protected Datum parent;
    final protected String name ;
    final protected String typeString ;
    final protected String valueString ;
    final protected byte[] bytes ;
    final protected int highlight ;
    final protected int birthOrder = 0;
    final protected int serialNumber ;
    private ArrayList<MirrorDatum> children = null ;

    @Override
    public int getHighlight() {
        return highlight ;
    }

    @Override
    public Datum getParent() {
        return parent ;
    }

    @Override
    public String getTypeString() {
        return typeString ;
    }

    @Override
    public String getName() {
        return name ;
    }

    @Override
    public String getValueString() {
        return valueString ;
    }

    @Override
    public int getNumBytes() {
        return bytes.length ;
    }

    @Override
    public int getAddress() {
        return address ;
    }

    @Override
    public int getByte(int i) {
        return bytes[i] ;
    }

    @Override
    public int getNumChildren() {
        if( children == null ) return 0 ;
        else return children.size() ;
    }

    @Override
    public Datum getChildAt(int i) {
        if( children == null ) throw new AssertionError("No child") ;
        else return children.get( i ) ;
    }

    @Override
    public int getBirthOrder() {
        return birthOrder ;
    }

    @Override
    public String getChildLabelAt(int i) {
        // TODO Auto-generated method stub
        return null ;
    }

    @Override
    public void setProperty(String name, Object info) {
        // TODO Auto-generated method stub

    }

    @Override
    public Object getProperty(String name) {
        // TODO Auto-generated method stub
        return null ;
    }

    @Override
    public int getSerialNumber() {
        return serialNumber ;
    }

}
