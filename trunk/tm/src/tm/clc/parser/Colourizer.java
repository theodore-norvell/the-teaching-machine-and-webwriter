//     Copyright 1998--2010 Michael Bruce-Lockhart and Theodore S. Norvell
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License. 
// You may obtain a copy of the License at 
//
//     http://www.apache.org/licenses/LICENSE-2.0 
//
// Unless required by applicable law or agreed to in writing, 
// software distributed under the License is distributed on an 
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language 
// governing permissions and limitations under the License.

package tm.clc.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import tm.interfaces.ExternalCommandInterface;
import tm.interfaces.MarkUp ;
import tm.interfaces.MarkUpI ;
import tm.interfaces.SourceCoords;
import tm.interfaces.TagSet ;
import tm.interfaces.TagSetInterface ;
import tm.utilities.Assert;
import tm.utilities.TMFile;
import tm.virtualMachine.CodeStore;
import tm.virtualMachine.VMCodeLine;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Company: </p>
 * @author Theodore Norvell
 * @version 1.0
 */

public class Colourizer {
    static final TagSet completeSelection = new TagSet( ExternalCommandInterface.COMPLETE_SELECTION ) ;
    CodeStore codeStore ;
    
    /** The current line being build */
    StringBuffer currentLine = new StringBuffer() ;
    
    /** The mark up for the current line */
    Vector<MarkUp> currentMarkUp = new Vector<MarkUp>() ;
    
    /** The set of all tags that currently apply. */
    TagSet currentTagSet = TagSet.EMPTY ;
    
    /** A list of all TagSets that apply to the current line. */
    Set<TagSetInterface> tagSetsForCurrentLine   ;
    
    TMFile tmFile ;
    int lineNumber = 0 ;
    
    /** If newLineStarted is false, we need to start a new line.
     * If newLineStarted is true, current line has been initialized.*/
    boolean newLineStarted = false ;

    public static final int PLAIN = 0,
                            KEYWORD = 1,
                            COMMENT = 2,
                            CONSTANT = 3,
                            PREPROCESSOR = 4,
                            MARKUP = 5 ;

    private int currentColour = PLAIN ;

    public Colourizer( CodeStore codeStore, TMFile tmFile ) {
        this.codeStore = codeStore ;
        this.tmFile = tmFile ; }

    public void addToken(String tokenImage, int colourClass) {
        if( ! newLineStarted ) startNewLine(colourClass) ;
        // Tokens that are of class MARKUP must not contain newlines.
        if( colourClass==MARKUP ) return ;

        if( currentColour != colourClass ) {
            endColour() ; }
        if( currentColour != colourClass ) {
            startColour( colourClass ) ; }

        for( int i = 0 ; i < tokenImage.length() ; ++i ) {
            if( ! newLineStarted ) startNewLine(colourClass) ;
            char ch = tokenImage.charAt( i ) ;
            switch( ch ) {

              case '\n' : {
                colourClass = currentColour ;
                endColour() ;
                if( tagSetsForCurrentLine.isEmpty() )
                    tagSetsForCurrentLine.add(currentTagSet) ;
                VMCodeLine codeLine = new VMCodeLine( currentLine,
                                                        currentMarkUp,
                                                        new SourceCoords( tmFile, lineNumber ),
                                                        tagSetsForCurrentLine ) ;
                codeStore.addCodeLine( codeLine ) ;
                newLineStarted = false ; }
              break ;

              case '\r' : {}
              break ;

              default : {
                  tagSetsForCurrentLine.add( currentTagSet ) ;
                  currentLine.append( ch ) ; } } }
    }

    public void startSelection( String tagName )  {
        if( ! newLineStarted ) startNewLine(PLAIN) ;
        currentTagSet = TagSet.union( currentTagSet, new TagSet(tagName)) ;
        emitCommand(MarkUpI.CHANGE_TAG_SET, currentTagSet) ;
    }

    public void endSelection( String tagName )  {
        if( ! newLineStarted ) startNewLine(PLAIN) ;
        currentTagSet = TagSet.subtract( currentTagSet, new TagSet(tagName)) ;
        emitCommand(MarkUpI.CHANGE_TAG_SET, currentTagSet) ;
    }

    private void startNewLine(int colourClass) {
        currentLine.setLength(0) ;
        currentMarkUp.setSize(0) ;
        tagSetsForCurrentLine = new HashSet<TagSetInterface>() ; 
        // Emit a mark-up command to set the current tag set
        if( currentTagSet != TagSet.EMPTY ) 
            emitCommand( MarkUpI.CHANGE_TAG_SET, currentTagSet ) ;
        
        startColour(colourClass) ;
        lineNumber += 1 ;
        
        // Been there done that.
        newLineStarted = true ;
    }

    private void startColour(int colourClass) {
        currentColour = colourClass ;
        switch( colourClass ) {

          case PLAIN : { }
          break ;

          case KEYWORD : {
            emitCommand( MarkUpI.KEYWORD ) ; }
          break ;

          case COMMENT :{
            emitCommand( MarkUpI.COMMENT ) ; }
          break ;

          case CONSTANT :{
            emitCommand( MarkUpI.CONSTANT ) ; }
          break ;

          case PREPROCESSOR : {
            emitCommand( MarkUpI.PREPROCESSOR ) ; }
          break ;

          default : Assert.check( false ) ; }
    }

    private void endColour() {
        switch( currentColour ) {

          case PLAIN : { }
          break ;

          case KEYWORD :
          case COMMENT :
          case CONSTANT :
          case PREPROCESSOR : {
            emitCommand( MarkUpI.NORMAL ) ; }
          break ;

          default : Assert.check( false ) ; }
        currentColour = PLAIN ;
    }
 
    private void emitCommand( short command ) {
        currentMarkUp.addElement( new MarkUp( currentLine.length(), command ) ) ; }
    
    private void emitCommand( short command, TagSet tagSet) {
        currentMarkUp.addElement( new MarkUp( currentLine.length(), command, tagSet ) ) ;
    }
}