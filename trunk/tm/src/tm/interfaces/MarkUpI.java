package tm.interfaces ;

import java.io.Serializable ;

public interface MarkUpI extends Serializable {

    public static final short CHANGE_TAG_SET = 5 ;
    public static final short COMMENT = 2 ;
    public static final short CONSTANT = 4 ;
    public static final short KEYWORD = 1 ;
    public static final short NORMAL = 0 ;
    public static final short PREPROCESSOR = 3 ;

    String toString() ;
    
    int getColumn() ;
    
    int getCommand() ;
    
    TagSetInterface getTagSet() ;

}