xulUnit.println = function(message) {
	putToJSConsole( message ) ;
}

xulUnit.addTest( "tests of framework", {
    test_pass: function () {
    	xulUnit.assertTrue( true, "Holy cats" ) ; }
    ,
    test_fail: function() {
        xulUnit.assertTrue( false, "Jump'n jimney" ) ; }
    ,
    test_error0: function() {
        throw null ; }
    ,
    test_error1: function() {
        throw new Error() ; }
    } ) ;

xulUnit.addTest( "tests of ElementDescriptor.js", {  
	make_fragment : function() { 
		var frag = document.createDocumentFragment() ;
    	var child = document.createElement("fred") ;
    	frag.appendChild( child ) ;
    	child = document.createElement("george") ;
    	frag.appendChild( child ) ;
    	child = document.createTextNode("a") ;
    	frag.appendChild( child ) ;
    	child = document.createTextNode("b") ;
    	frag.appendChild( child ) ;
    	child = document.createTextNode("c") ;
    	frag.appendChild( child ) ;
    	child = document.createComment("") ;
    	frag.appendChild( child ) ;
    	child = document.createComment("") ;
    	frag.appendChild( child ) ;
    	return frag ;
    }
    ,	
    test_node2ContentArray_0: function() {
    	
    	var frag = this.make_fragment() ;
    	
    	// Ok then, let's run the FUT
    	
    	var a = node2ContentArray( frag ) ;
    	
    	xulUnit.assertTrue( a.length == 7, "wrong number of children "+a) ;
    	
    	xulUnit.assertTrue( a[0] == "fred", "child 0") ;
    	xulUnit.assertTrue( a[1] == "george", "child 1") ;
    	xulUnit.assertTrue( a[2] == "#PCDATA", "child 2") ;
    	xulUnit.assertTrue( a[3] == "#PCDATA", "child 3") ;
    	xulUnit.assertTrue( a[4] == "#PCDATA", "child 4") ;
    	xulUnit.assertTrue( a[5] == "", "child 5") ;
    	xulUnit.assertTrue( a[6] == "", "child 6") ;
    }
    ,	
    test_ContentArray2ContentString_0: function() {
    	
    	var frag = this.make_fragment() ;
    	
    	// Ok then, let's run the FUT
    	
    	var a = node2ContentArray( frag ) ;
    	var s = contentArray2ContentString( a ) ;
    	var expected = "@fred@george#PCDATA" ;
    	xulUnit.assertEquals( expected, s ) ;
    }
    ,
    test_ElementDescriptor_acceptsContent_0: function() {
    	
    	var a = [ "head", "body"] ;
    	
    	var elemDesc = newElementDescriptor( "simple",
		            reSeq(reElem("head"), reElem("body") ) )
    	
    	// Ok then, let's run the FUT
    	
    	var r = elemDesc.acceptsContent( a ) ;
    	xulUnit.assertTrue( r ) ;
    	
    }
    ,
    test_ElementDescriptor_acceptsContent_1: function() {
    	
    	var a = [ "fred"] ;
    	
    	var elemDesc = newElementDescriptor( "simple",
		            reSeq(reElem("block"), reElem("head") ) )
    	
    	// Ok then, let's run the FUT
    	
    	var r = elemDesc.acceptsContent( a ) ;
    	xulUnit.assertFalse( r ) ;
    	
    }
    ,
    test_ElementDescriptor_acceptsContent_2: function() {
    	
    	var a = [ "title" ] ;
    	
    	var elemDesc = newElementDescriptor( "head",
		            reOpt( reElem("title")))
    	
    	// Ok then, let's run the FUT
    	
    	var r = elemDesc.acceptsContent( a ) ;
    	xulUnit.assertTrue( r ) ;
    	
    }
    ,
    test_ElementDescriptor_acceptsContent_3: function() {
    	
    	var a = [ ] ;
    	
    	var elemDesc = newElementDescriptor( "head",
		            reOpt( reElem("title")))
    	
    	// Ok then, let's run the FUT
    	
    	var r = elemDesc.acceptsContent( a ) ;
    	xulUnit.assertTrue( r ) ;
    	
    }
    ,
    test_ElementDescriptor_acceptsContent_4: function() {
    	
    	var a = [ "body" ] ;
    	
    	var elemDesc = newElementDescriptor( "head",
		            reOpt( reElem("title")))
    	
    	// Ok then, let's run the FUT
    	
    	var r = elemDesc.acceptsContent( a ) ;
    	xulUnit.assertFalse( r ) ;
    	
    }
    ,
    test_ElementDescriptor_acceptsContent_5: function() {
    	
    	var a = [ "em" ] ;
    	
    	var elemDesc = newElementDescriptor( "head",
		            reAlt( reElem("em"), reElem("strong"), reElem("kbd"), reElem("form") ) ) ;
    	
    	// Ok then, let's run the FUT
    	
    	var r = elemDesc.acceptsContent( a ) ;
    	xulUnit.assertTrue( r ) ;
    	
    }
    ,
    test_ElementDescriptor_acceptsContent_6: function() {
    	
    	var a = [ "em" ] ;
    	
    	var elemDesc = newElementDescriptor( "head",
		            reAlt( reElem("em"), reElem("strong"), reElem("kbd"), reElem("form") ) ) ;
    	
    	// Ok then, let's run the FUT
    	
    	var r = elemDesc.acceptsContent( a ) ;
    	xulUnit.assertTrue( r ) ;
    	
    }
    ,
    test_ElementDescriptor_acceptsContent_7: function() {
    	
    	var a = [ "form" ] ;
    	
    	var elemDesc = newElementDescriptor( "head",
		            reAlt( reElem("em"), reElem("strong"), reElem("kbd"), reElem("form") ) ) ;
    	
    	// Ok then, let's run the FUT
    	
    	var r = elemDesc.acceptsContent( a ) ;
    	xulUnit.assertTrue( r ) ;
    	
    }
    ,
    test_ElementDescriptor_acceptsContent_8: function() {
    	
    	var a = [  ] ;
    	
    	var elemDesc = newElementDescriptor( "head",
		            reAlt( reElem("em"), reElem("strong"), reElem("kbd"), reElem("form") ) ) ;
    	
    	// Ok then, let's run the FUT
    	
    	var r = elemDesc.acceptsContent( a ) ;
    	xulUnit.assertFalse( r ) ;
    	
    }
    ,
    test_ElementDescriptor_acceptsContent_9: function() {
    	
    	var a = [ "em", "strong" ] ;
    	
    	var elemDesc = newElementDescriptor( "head",
		            reAlt( reElem("em"), reElem("strong"), reElem("kbd"), reElem("form") ) ) ;
    	
    	// Ok then, let's run the FUT
    	
    	var r = elemDesc.acceptsContent( a ) ;
    	xulUnit.assertFalse( r ) ;
    	
    }
    ,
    test_ElementDescriptor_acceptsContent_10: function() {
    	
    	var a = [ "en" ] ;
    	
    	var elemDesc = newElementDescriptor( "head",
		            reAlt( reElem("em"), reElem("strong"), reElem("kbd"), reElem("form") ) ) ;
    	
    	// Ok then, let's run the FUT
    	
    	var r = elemDesc.acceptsContent( a ) ;
    	xulUnit.assertFalse( r ) ;
    	
    }
    ,
    test_ElementDescriptor_acceptsContent_11: function() {
    	
    	var a = [  ] ;
    	
    	var elemDesc = newElementDescriptor( "head",
		            reStar( rePCDATA() ) ) ;
    	
    	// Ok then, let's run the FUT
    	
    	var r = elemDesc.acceptsContent( a ) ;
    	xulUnit.assertTrue( r ) ;
    	
    }
    ,
    test_ElementDescriptor_acceptsContent_12: function() {
    	
    	var a = [ "#PCDATA" ] ;
    	
    	var elemDesc = newElementDescriptor( "head",
		            reStar( rePCDATA() ) ) ;
    	
    	// Ok then, let's run the FUT
    	
    	var r = elemDesc.acceptsContent( a ) ;
    	xulUnit.assertTrue( r ) ;
    	
    }
    ,
    test_ElementDescriptor_acceptsContent_14: function() {
    	
    	var a = [ "#PCDATA", "#PCDATA", "#PCDATA"  ] ;
    	
    	var elemDesc = newElementDescriptor( "head",
		            reStar( rePCDATA() ) ) ;
    	
    	// Ok then, let's run the FUT
    	
    	var r = elemDesc.acceptsContent( a ) ;
    	xulUnit.assertTrue( r ) ;
    	
    }
    ,
    test_ElementDescriptor_acceptsContent_15: function() {
    	
    	var a = [ "fred"  ] ;
    	
    	var elemDesc = newElementDescriptor( "head",
		            reStar( rePCDATA() ) ) ;
    	
    	// Ok then, let's run the FUT
    	
    	var r = elemDesc.acceptsContent( a ) ;
    	xulUnit.assertFalse( r ) ;
    	
    }
    ,
    test_ElementDescriptor_acceptsContent_16: function() {
    	
    	var a = [  ] ;
    	
    	var elemDesc = newElementDescriptor( "head",
		            rePlus( rePCDATA() ) ) ;
    	
    	// Ok then, let's run the FUT
    	
    	var r = elemDesc.acceptsContent( a ) ;
    	xulUnit.assertFalse( r ) ;
    	
    }
    ,
    test_ElementDescriptor_acceptsContent_17: function() {
    	
    	var a = [ "#PCDATA" ] ;
    	
    	var elemDesc = newElementDescriptor( "head",
		            rePlus( rePCDATA() ) ) ;
    	
    	// Ok then, let's run the FUT
    	
    	var r = elemDesc.acceptsContent( a ) ;
    	xulUnit.assertTrue( r ) ;
    	
    }
    ,
    test_ElementDescriptor_acceptsContent_18: function() {
    	
    	var a = [ "#PCDATA", "#PCDATA", "#PCDATA"  ] ;
    	
    	var elemDesc = newElementDescriptor( "head",
		            rePlus( rePCDATA() ) ) ;
    	
    	// Ok then, let's run the FUT
    	
    	var r = elemDesc.acceptsContent( a ) ;
    	xulUnit.assertTrue( r ) ;
    	
    }
    ,
    test_ElementDescriptor_acceptsContent_19: function() {
    	
    	var a = [ "fred"  ] ;
    	
    	var elemDesc = newElementDescriptor( "head",
		            rePlus( rePCDATA() ) ) ;
    	
    	// Ok then, let's run the FUT
    	
    	var r = elemDesc.acceptsContent( a ) ;
    	xulUnit.assertFalse( r ) ;
    	
    }
    ,
    test_ElementDescriptor_acceptsContent_20: function() {
    	
    	var elemDesc = newElementDescriptor( "head",
		            rePCDATA() ) ;
    	
    	// Ok then, let's run the FUT
    	
    	var r = elemDesc.acceptsContent( ["#PCDATA"] ) ;
    	xulUnit.assertTrue( r ) ;
    	
    }
    
} ); 
    
xulUnit.runTestsNow() ;