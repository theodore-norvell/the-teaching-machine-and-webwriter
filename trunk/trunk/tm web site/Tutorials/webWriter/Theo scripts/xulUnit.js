var xulUnit = new Object() ;

/* Print some output.
 * This should be overridden if dump doesn't work
 */
xulUnit.println = function(message) {
	dump( message ) ;
}
xulUnit.tests = [] ;

xulUnit.addTest = function( testName, testObject ) {
	xulUnit.tests.push( {testName: testName, testObject: testObject} ) ;
}

xulUnit.runTestsNow = function() {
	xulUnit.println( "xulUnit.") ;
	var numberOfTestsFailed = 0 ;
	var numberOfTestsErrored = 0 ;
	var numberOfTestsPassed = 0 ;
	var i ;
	for( i=0 ; i < xulUnit.tests.length ; ++i ) {
		var testName = xulUnit.tests[i].testName ;
		var testObject = xulUnit.tests[i].testObject ;
		if( testObject.setUp ) {
			xulUnit.println( "xulUnit running "+testName+" setup") ;
			try { testObject.setUp.call( testObject ) ; }
			catch( e ) { xulUnit.println("setup fails") ; } } 
		var name ;
		for( name in testObject ) {
			xulUnit.resetResult() ;
			if( name.search(/^test/) == 0
			&& testObject[name] instanceof Function ) {
				xulUnit.println( "xulUnit running "+testName+" "+name) ;
				try {
					testObject[name].call( testObject ) ; }
				catch( e ) {
					if( e == xulUnit.failureObject )
					    xulUnit.recordFailure( e ) ;
					else 
					    xulUnit.recordException( e ) ; }
				if( xulUnit.testResult.result == "exception" ) {
					xulUnit.println( "Errored. Exception is: '"+
						(xulUnit.testResult.exception
						 ? xulUnit.testResult.exception.toString()
						 : "null")
						+"'" ) ;
					xulUnit.println( "......... Message is: "+xulUnit.testResult.message ) ;
					xulUnit.println( "......... Stack trace is: "+xulUnit.testResult.stackTrace ) ;
					xulUnit.println( "---> error " ) ;
					numberOfTestsErrored++ ; }
					
				else if( xulUnit.testResult.result == "fail" ) {
					xulUnit.println( "Failed. Message is: '"+xulUnit.testResult.message+"'" ) ;
					xulUnit.println( "....... Stack trace is: "+xulUnit.testResult.stackTrace ) ;
					xulUnit.println( "---> failed " ) ;
					numberOfTestsFailed++ ; }
					
				else  {
					xulUnit.println( "---> passed " ) ;
					numberOfTestsPassed++ ; } }
			if( testObject.tearDown ) {
				xulUnit.println( "xulUnit running "+testName+" tearDown") ;
				try { testObject.tearDown.call( testObject ) ; }
				catch( e ) { xulUnit.println("tearDown fails") ; } }
		}
	}
	xulUnit.println("xulUnit complete."
	                           + "  passes: " + numberOfTestsPassed
	                           + "  fails: " + numberOfTestsFailed
	                           + "  errors: " + numberOfTestsErrored ) ;
}

xulUnit.resetResult = function() {
	xulUnit.testResult = { result: "pass" } ;
}

xulUnit.recordException = function( e ) {
	var stackTrace = "no stack trace available" ;
	if( e && e.stack ) stackTrace = e.stack.toString() ;
	var message = "no message available" ;
	if( e && e.message ) message = e.message ;
	xulUnit.testResult = {
			result: "exception",
			exception: e,
			message: message,
			stackTrace: stackTrace } ;
}

xulUnit.recordFailure = function( ) {
	xulUnit.testResult = { result: "fail",
	                       message: xulUnit.failureObject.message,
	                       stackTrace: xulUnit.failureObject.stackTrace } ;
}

xulUnit.failureObject = { message: null, stackTrace: null } ; 

xulUnit.assertTrue = function( q, message ) {
	if( ! q ) { 
		message = message || "assertion failed" ;
		xulUnit.failureObject.message = message ;
		var error = new Error() ;
		if( error.stack ) {
			xulUnit.failureObject.stackTrace = error.stack.toString() ;	}
		else {
			xulUnit.failureObject.stackTrace = "no stack trace available" ;	}
		throw xulUnit.failureObject ;
	}
}

xulUnit.assertFalse = function( q, message ) {
	if( q ) { 
		message = message || "assertion failed" ;
		xulUnit.failureObject.message = message ;
		var error = new Error() ;
		if( error.stack ) {
			xulUnit.failureObject.stackTrace = error.stack.toString() ;	}
		else {
			xulUnit.failureObject.stackTrace = "no stack trace available" ;	}
		throw xulUnit.failureObject ;
	}
}

xulUnit.assertNull = function( q, message ) {
	if( q===null ) { 
		message = message || "assertion failed" ;
		xulUnit.failureObject.message = message ;
		var error = new Error() ;
		if( error.stack ) {
			xulUnit.failureObject.stackTrace = error.stack.toString() ;	}
		else {
			xulUnit.failureObject.stackTrace = "no stack trace available" ;	}
		throw xulUnit.failureObject ;
	}
}

xulUnit.assertNotNull = function( q, message ) {
	if( q!==null ) { 
		message = message || "assertion failed" ;
		xulUnit.failureObject.message = message ;
		var error = new Error() ;
		if( error.stack ) {
			xulUnit.failureObject.stackTrace = error.stack.toString() ;	}
		else {
			xulUnit.failureObject.stackTrace = "no stack trace available" ;	}
		throw xulUnit.failureObject ;
	}
}

xulUnit.assertSame = function( expected, actual, message ) {
	if( actual !== expected ) { 
		message = message || "assertion failed." ;
		message += " Values not the same: Actual was '" + actual + "' expected was '" + expected + "'" ;
		xulUnit.failureObject.message = message ;
		var error = new Error() ;
		if( error.stack ) {
			xulUnit.failureObject.stackTrace = error.stack.toString() ;	}
		else {
			xulUnit.failureObject.stackTrace = "no stack trace available" ;	}
		throw xulUnit.failureObject ;
	}
}

xulUnit.assertEquals = function( expected, actual, message ) {
	if( actual != expected ) { 
		message = message || "assertion failed." ;
		message += " Values not equal: Actual was '" + actual + "' expected was '" + expected + "'" ;
		xulUnit.failureObject.message = message ;
		var error = new Error() ;
		if( error.stack ) {
			xulUnit.failureObject.stackTrace = error.stack.toString() ;	}
		else {
			xulUnit.failureObject.stackTrace = "no stack trace available" ;	}
		throw xulUnit.failureObject ;
	}
}
