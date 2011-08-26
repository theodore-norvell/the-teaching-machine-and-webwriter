/*#TA*//*#TB*//*#TC*//** A type representing books */
class Book{/*#B="Book"*/
    private String /*#b="Book"*/author/*#/b*/ ;
    private String /*#b="Book"*/title/*#/b*/ ;
    private String /*#b="Book"*/publisher/*#/b*/ ;
    private int /*#b="Book"*/year/*#/b*/ ;
/*#/TA*//*#/TC*//*#/B*/
    public Book( String author,
                 String title,
                 String publisher,
                 int year ) {
        this.author = author ;
        this.title = title ;
        this.publisher = publisher ;
        this.year = year ; }
/*#B="Book"*/ /*#/TB*/ /*#T/C*/
    public String getAuthor() {
        return author ; }
    
    public String getTitle() {
        return title ; }
    
    public String getPublisher() {
        return publisher ; }
/*#TC*/   
    public int getYear() {
        return year ; }/*#/B*/
/*#TA*//*#TB*/
}