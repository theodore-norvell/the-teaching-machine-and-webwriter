# Proposed Web API for the Teaching Machine


History: Version 0. TSN 2015 Nov 11.
  Version 1. TSN 2015 Nov 12. Added retire.
  Version 2. TSN 2015 Dec 22. Changes to how state information is returned from the remote TM
  Version 3. TSN 2016 Mar 6. Converted to markdown format.

Objective: Allow Remote Teaching Machines (RTM) to be used as a web service.

This is a RESTless API!

## Section 0.  General comments on the Web API

### GET vs POST:

Since the results of a GET may be cached, it is important that all requests be sent using the HTTP POST method.

### On parameters

Parameters are extra bits of information that go along with the request.
We could implement parameters either by putting them in a query string (this is the jquery
default) or by passing in a JSON encoded object in the request body (also easy to do in
jquery).  My preference is for the later, as it is symmetric with the response.

### Response HTTP status code

Responses in HTTP are considered successes or failures based on 
their HTTP  status.  Status 200 means success.  The API below deals only with success
cases.  I.e. the  server should return a response with HTTP status code equal to 200
even in the case that it can't fulfill the request because of some problem such as
a bad or retired GUID.   Other codes might be used if the request was in error.  For
example if the URL is *BaseURL*/fuddleDuddle
then the HTTP status code would be 404. And if the parameters can't be parsed, then 
status code 400 makes sense.  Missing required parameters would also give a 400.  However
if say a bad or retired GUID is sent or a request is made when the remote TM is in the wrong state, then HTTP status would be 200.  HTTP status codes between 201 and 299 
inclusive should be avoided.

### Responses

Assuming the HTTP response status is 200, each request results in a response object. This
object should be a JSON encoded object in the body of the response.

### The "status" field of the responses

Independent of the HTTP status codes, the API defines its own set of statuses. These are returned in the "status" field of the response.  Generally the value of the "status" field indicates the state of a remote TM.

There is a subtle distinction between states and statuses.  All possible states of the
remote TM are statuses, but some statuses are not states. The states or the remote TM are
as listed in interface TMStatusCode. They are

```
    /** Status Code -- No Evaluator is present.*/
    NO_EVALUATOR = 0
    
    /**   Not used in Web API  */
    READY_TO_COMPILE = 1
    
    /**  Status Code -- Compilation failed. */
    DID_NOT_COMPILE = 2
    
    /**  Status Code -- Compilation was successful. Initialization has not been done.*/
    COMPILED = 3
    
    /**  Status Code -- Ready for next step. */
    READY = 4
    
    /**  Not used in Web API */
    BUSY_EVALUATING = 5
    
    /**  Status Code -- The program has run to completion. */
    EXECUTION_COMPLETE = 6
    
    /**  Status Code -- The program has crashed or otherwise completed in an unpleasant way. */
    EXECUTION_FAILED = 7 
```

[Implementation Note: These states are stored in the RTM's evaluator object (which in
turn, keeps them in its virtual machine state object.) However `NO_EVALUATOR` can not be
stored in the evaluator object since there isn't one. In the TMBigApplet, the
`NO_EVALUATOR` state is represented by the evaluator pointer being null.]

The "status" field of the response will usually hold one of the above states.
It will be the state of the RTM after the request has been processed. For example a "go" request might start with the RTM in the `READY` state and end with it being in the `EXECUTION_COMPLETE` state.  In this case the "status" field of the response would be `EXECUTION_COMPLETE`.

However there are a few cases where it does not make sense for the response to contain the state of the RTM.  For these cases, there are some additional values that the "status" field can take on.

```
   /** Status Code -- There is no RTM with that GUID */
   BAD_GUID = -1
   
   /** Status Code -- There was an RTM with that GUID, but it no longer exists. */
   RETIRED = -2
   
   /** Status Code -- RTM could not be created. */
   FAILED = -3
   
   /** Status Code -- The request succeeded. */
   SUCCEEDED = -4
```

The idea behind `RETIRED` is that if a remote TM is not used for a long time, the server might delete it.

## Section 1. State Information

### Wanted parameters

Many methods can return information about the state of the remoteTM.
The request specifies what information is wanted by the client.
For example

* If the request contains an `expressionWanted` parameter equal to `"yes"`,
the response must contain the current state of the expression being evaluated
(see the `expression` field below for details).
* If the value of the `expressionWanted` parameter is
equal to "maybe", the response would contain an "expression" field unless the
value of that field would be the same as the value of the field in the most recent response that had had that field. I.e. the client gets a new value iff it is different from the old value.
* If there is no `expressionWanted` parameter or its value is "no", there will be no `expression` field in the response.

The following table lists the  "wanted parameters" and the corresponding response fields. 


   Parameter          | Response field
   -------------------|---------------
   `expressionWanted`   |   `expression`
   `outputWanted`		 |	  `output`
   `consoleWanted`      |   `console`
   `codeWanted`         |   `code`
   `focusWanted`        |   `focus`
   `stackWanted`        |   `stackId`
   `heapWanted`         |   `heapId`
   `staticWanted`       |   `staticId`
   `scratchWanted`      |   `scratchId`

### The `expression` field of the responses

The `expression` field in the response is a specially encoded string that shows the current state of the execution of the current expression. Details of the encoding can be found in the TM's source code.

### The `console` field of the responses:

The `console` field. This is an array of specially encoded strings that shows the current state of the console.  By default the characters represent output.  However, within each line, \ufffe indicates the start of input and \uffff indicates the resumption of output.  Note that tabs are not expanded, so tab expansion needs to be done on the client.

### The `output` field of the responses:

The `output` field is an array of strings that shows data that has been output to the console.  It differs from `console` in that it does not show any input.  There is no special encoding.

### The `code` field of the responses:

The `code` field consists of all the currently selected lines in the current source file.    It is an array of objects where each object has three fields.



  Field     | Type    | Comment
  :---------|:--------|:-----------
   chars    | string | one line of code
   coords   | object | an object (see below for details)
   markup   | array  | an array of objects (see below for details) sorted by column


The chars of course represent the line of code.

(See class tm.virtualMachine.CodeStore for details.  The code field corresponds to the array of lines one gets from the evaluator as follows

```
   SourceCoords focus = evaluator.getCodeFocus();
   TMFile file = focus.getFile() ;
   int n = evaluator.getNumSelectedCodeLines(file, false) ;
   for( int i=0; i < n ; ++i ) {
       CodeLine line = evaluator.getSelectedCodeLine(file, false, i);
       ...
   }
```
)
   
The coords objects have fields `fileName`, which is a string, and `line` which is an int.

Each markup object is an object with two or three fields

Field     | Type    | Comment
:---------|:--------|:-----------
 column   |  int    |
 command  |  int    | see `tm.interfaces.MarkUp`
 tagSet   |  string | see `tm.interfaces.MarkUp`


For details concerning markup, see class `tm.interfaces.MarkUp` in the TM source code. The tagSet string is only needed when the command is `CHANGE_TAG_SET` (i.e. 5) and will be a string of lower-case letters (a-z) without duplicates in alphabetical order.

### The "focus" field of the responses:

The this is a "coords" object (described above under "code") that indicates the file name and line number of the line that is currently being executed.

### The `stackId`, `heapId`, `staticId`, and `scratchId` fields of the responses:

These fields give the `id` numbers of datums (see next section) representing the four regions of the Teaching Machine's store.  These will be compound regions.

### The `store` field of the responses and `Datums`.

Note there is no `storeWanted` parameter. The `store` field is present in the response when it needs to be.  This will be when there is a `stackId`, `heapId`, `staticId`, or `scratchId` field in the response and there has been some change to the store since the last response that included a `store` field.

The `store`, is an abstraction of the memory of the virtual machine. Each TM's store is represented by an set of objects conforming to the following Datum interface.  The `store` field itself is an array, but the order in the array is not important; i.e. we use the array to represent a set. 

Scalar datums represent such things as integer, char, bool, and pointer locations, arrays, structs, objects, etc.  Compound datums represent arrays, structs, objects. 

[Note for future: In the future we might make this more efficient by only sending modifications to the store. However for now, each time the store is modified, even a little, the entire store should be sent from the server; and the client should rebuild a new store.]

Note: The corresponding Java interface is tm.interfaces.Datum. More explanation can be found there.  There are a few items missing from tm.interfaces.Datum; these are items that I don't think will ever be needed by the Javascript implementations of the views.

```typescript
    interface Datum {
        id : number ; // An integer that uniquely identifies
              // the datum. The value is
              // in the range 0 through +9007199254740991
              // and so can be represented by a javascript number.
              // These numbers are unique throughout any
              // execution of a target program in the TM.
        highlight : number ; // 0 if not highlighted,
                             // 1 if highlighted
        typeString : string ;// A string indicating the datum's
                          // type. E.g. "int" or "char*"
        name : string ; // A name for the datum for display.
                       // This might be a fully qualified name
                       // such as "pkg.Klass.a[4].f"
        valueString: string ; // The value of the datum for display.
                       // E.g. "42" or "{..}".
        bytes : Array<number> ; // The bytes' values from -128 through 127.
                    // Item 0 of this array is the byte at 
                    // address `.address` of the memory.
        address : number ; // The address of the first byte.
        
        parent: number|null ; // is null if the datum has no parent. 
                         // Otherwise it is the id of another
                         // datum in the same store
        childId : Array<number> ; // The ids of the children.
                // This array is empty if there are no children.
                // Note that if the parent field of d is not null,
                // the parent datum p must have d.id exactly once
                // in its childId array.
                // The converse is NOT an invariant. I.e. if
                //  d.id is in the childId array of p,
                // it is possible that d.parent is not p.id!
        childLabel : Array<string> ; // It is an invariant that the
                // length of this array equals the length of the
                // childId array. This is an array of labels
                // for the children. E.g. for an array this might
                // be the ["0", "1", "2"], for an object, it might
                // be [ ".real", ".imag" ].
        kind : number ; // An integer identifying the kind of
                   // Datum.  Possibilities include:
                   // COMPOUND = 0, REGION = 1, SCALAR = 3, POINTER = 4
                   // If SCALAR or POINTER the .childId array must
                   // be empty.

```

When the `kind` field is `REGION`, there is a further field

```typescript
        frameBoundary? : number // An integer that says
           // how many variables are not in the top frame.
           // For most regions this will be 0.
           // For the stack region it is the length of the
           // .childId array minus the number of variables in
           // the top frame.
```

When the `kind` field is `POINTER`, there are two more fields.

```typescript
        isNull? : boolean ; // True if the pointer is null
        dereIdf?: number|null ; // This is either null or the id of a
                        // datum in the store.
                        
        // Invariant: isNull implies derefId == null.
        // There are three categories of values:
        //   isNull && derefId == null   --- It's a null pointer.
        //   !isNull && derefId != null  -- Then deref is the target of the pointer
        //   !isNull && derefId == null -- The pointer is broken.
}
```

A broken pointer is one that does not point to a datum.  For example in C++ an uninitialized or dangling pointer is broken.

[Implementation note: Since each datum has a unique identifying integer, it can be used as an index into an object. Given an array of datums, d, we can make an object that acts as a map from ids to datums like this

```typescript
    var store = {} ;
    for( i = 0 ; i < d.length ; ++i )
        store[ d[i].id ] = d ;
```
]



### The `stackId`, `

## Section 2. The API calls

### createRemoteTM

**URL:** *BaseURL*/createRemoteTM

**Parameters:** None

**Initial state:**  Initially there is no RTM, so there is no initial state.

**Action:** The server creates a new RTM and assigns it a globally unique identifier.

**Final status:**  If successful the final state will be `NO_EVALUATOR`. Otherwise it will be `FAILED`.

**Response:**

  Field     | Type    | Comment
  :---------|:--------|:-----------
  status    | int     | This will be `NO_EVALUATOR` or `FAILED`.
  guid      | string  |If the status is `NO_EVALUATOR`, this will be the GUID of the RTM created.
  reason    | string  | If the status is `NO_EVALUATOR`, this will be "". Otherwise, it will be an explanation of the problem.
                      

### retireRemoteTM

**URL:** *BaseURL*/retireRemoteTM

**Parameters:** 
     guid: string   -- The GUID of the remote TM (returned from a previous createRemoteTM)

**Initial state:**  Any

**Action:** The server retires the RTM.

**Final status:**

Value | Meaning
:-----|:--
`BAD_GUID`  | if no RTM has ever had the given GUID.
`RETIRED`   | if the RTM with the GUID has already retired.
`SUCCEEDED` | if the RTM was retired.

**Response:**

  Field     | Type    | Comment
  :---------|:--------|:-----------
  status    | int     | See above created.
  reason    | string  | If the status is `SUCCEEDED`, this will be "". Otherwise, it will be an explanation of the problem.
  
###loadString

**URL:** *BaseURL*/loadString

**Parameters:**

  Field     | Type    | Comment
  :---------|:--------|:-----------
  guid      |  string | The GUID of the remote TM (returned from a previous createRemoteTM)
    language | string | Either "cpp" or "java"
    fileName | string | The file name for the main program.
    program  | string | The text of the program.
    
**Initial state:**  The RTM may be in any state.

**Action:**

   Unless the initial state is `NO_EVALUATOR`, the current evaluator destroyed. The RTM then builds an evaluator and compiles the program.
   
**Final status:**

Value | Meaning
:-----|:--
   `BAD_GUID` | if no RTM has ever had the given GUID
   `RETIRED` | if the RTM with the GUID has retired
   `NO_EVALUATOR` | if any error happens prior to compilation.
   `DID_NOT_COMPILE` | If there was an error during compilation.
   `COMPILED` | Otherwise
   
**Response:**


  Field     | Type    | Comment
  :---------|:--------|:-----------
   status   | int     |   see above
   reason   | string  | If the status is `COMPILED`, this will be "". Otherwise, a string representing the reason why the status is not `COMPILED`. (e.g. the error message from the compiler). Note that this string may contain newline characters.

### initializeTheState


**URL:** *BaseURL*/initializeTheState

**Parameters:**


  Field     | Type    | Comment
  :---------|:--------|:-----------
    guid    | string  | The GUID
    expressionWanted, outputWanted, etc  | boolean |  see the section on State Information.
    
**Initial state:**  `COMPILED`

**Action:**

* If the RTM is not in the `COMPILED` state, then the request has no effect.
* If the RTM is in the `COMPILED` state, then the program is executed until either it can not be executed any further, or the first visible expression of the program is about to be evaluated.

**Final status:**

Value | Meaning
:-----|:--
   `BAD_GUID` | if no RTM has ever had the given GUID.
   `RETIRED`  | if the RTM with the GUID has retired.
   `READY`    | if the RTM is ready for another action.
   `EXECUTION_COMPLETE` | if the execution of the program is complete
   `EXECUTION_FAILED` | if there was a failure during execution. For example a run time
   error.
   

  Field     | Type    | Comment
  :---------|:--------|:-----------
   status   |  int    |  see above
   reason   |  string | If the status is `READY` or `EXECUTION_COMPLETE`, this will be "". Otherwise, a string representing the reason why the status is not `READY` or `EXECUTION_COMPLETE`. Note that this string may contain newline characters.
   expression, output, code, etc | | see section on State Information.
   
   
### go

[Implementation note:  Although this seems very complicated, it is mostly already implemented inside the current Evaluator class, so don't worry.]

**URL:** *BaseURL*/go

**Parameters:**

  Field     | Type    | Comment
  :---------|:--------|:-----------
   guid     | string   |The GUID
    commandString | string | See below. 
    expressionWanted, outputWanted, etc  | boolean |  see the section on State Information.

    
 The commandString field should following syntax.
 
        command -->  ws [ simpleCommand ws [ ";" command] ]
        simpleCommand --> [integer ws "*" ws] primaryCommand
        primaryCommand --> "s" | "e" | "o" | "f" | "b" | "m"
        integer -->  digit [ integer ]
        digit --> "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
        ws --> [ " " ws ]
        
**Initial state:**  Any

**Action:**

*   If the state is not `READY`, this request does nothing to the state of the RTM.
*  If the state is `READY` and the commandString does not follow the given syntax, the
   result is not defined.
*  If the state is `READY` and the commandString follows the syntax above, then each command
*  of the string will be executed in turn as long as the state remains `READY`.
*  Once the state is not `READY`, the remaining commands have no effect.
*  Integers indicate repetition, thus "5*s" is the same as "s;s;s;s;s".
*   Primary commands have the following meanings
   -      s  Step to the next expression, even if it is in a deeper subroutine invocation.
   -     e  Step to the next expression at an equal or lesser depth.
   -     o  Step out of the current subroutine invocation.
   -     f  Step forward to the next operation.
   -     b  Step until the next break point.
   -     m  Take a microstep, i.e. the smallest possible advance of the machine's state.

   
**Final status:**
<dl>
   <dt>`BAD_GUID`:</dt><dd> if no RTM has ever had the given GUID.</dd>
    <dt>`RETIRED`:</dt><dd> if the RTM with the GUID has retired.</dd>
    <dt>`READY`:</dt><dd> if the RTM is ready for another action.</dd>
    <dt>`EXECUTION_COMPLETE`:</dt><dd> if the execution of the program is complete</dd>
   <dt> `EXECUTION_FAILED`:</dt><dd> if there was a failure during execution. For example a run time
   error.</dd>
    <dt>Any other:</dt><dd> if the request started in a state that was not `READY`.</dd>
 </dl>
 
**Response:**



  Field     | Type    | Comment
  :---------|:--------|:-----------
   status |  int | see above
   reason | string | If the final status is `READY` or `EXECUTION_COMPLETE`, the reason will be "".  If the final status is `EXECUTION_FAILED`, the reason will   be an error message. Otherwise, reason will be a string indicating what the problem is.
   expression, output, code, etc | |  see section on State Information.
   
### goBack

**URL:** *BaseURL*/goBack

**Parameters:**

  Field     | Type    | Comment
  :---------|:--------|:-----------
    guid    | string  | The GUID
    expressionWanted, outputWanted etc | | see the section on State Information.

**Initial state:**  Any

**Action:**

*   The state of the evaluator is backed up to where it was just before
   the most recent checkpoint.   Note that the evaluator can only be
   backed up to the first checkpoint, which happens right after global
   variables are initialized.  After that new checkpoints are created
   at the start of each "go" command.

**Final status:**

<dl>
   <dt>`BAD_GUID`:</dt><dd> if no RTM has ever had the given GUID.</dd>
   <dt>`RETIRED`:</dt><dd> if the RTM with the GUID has retired.</dd>
   <dt>`NO_EVALUATOR`:</dt><dd> if (and only if) the initial state is `NO_EVALUATOR`</dd>
   <dt>Otherwise:</dt><dd>If there is nothing to undo, the state is unchanged.
   If there was something to undo the final state will be whatever the state
   was before the checkpoint.</dd>
</dl>   

**Response:**


Field     | Type    | Comment
:---------|:--------|:-----------
   status | int | see above
   reason | string | If the status is `BAD_GUID`, `RETIRED`, or `NO_EVALUATOR`, a reason why. Otherwise this string should be "".
   expression, output, code, etc | |  see section on State Information.
   
###redo

**URL:** *BaseURL*/redo

**Parameters:**

Field     | Type    | Comment
:---------|:--------|:-----------
 guid     | string  | The GUID
 expressionWanted, outputWanted etc | see the section on State Information.
 
**Initial state:**  Any

**Action:**

The most recent goBack is undone.  If the machine has moved forward since that goBack, this request has no effect.
   
**Final status:**

<dl>
   <dt>`BAD_GUID`:</dt><dd> if no RTM has ever had the given GUID.</dd>
   <dt>`RETIRED`:</dt><dd> if the RTM with the GUID has retired.</dt><dd>
   <dt>`NO_EVALUATOR`:</dt><dd> if (and only if) the initial state is `NO_EVALUATOR`</dt><dd>
   <dt>Otherwise</dt><dd>If there is nothing to redo, the state is unchanged.</dt><dd> If there was something to redo, the final state will be whatever the state was before goBack was executed.
</dl>   

**Response:**

Field     | Type    | Comment
:---------|:--------|:-----------
 status   | int     | see above
reason    | string  | If the status is `BAD_GUID`, `RETIRED`, or `NO_EVALUATOR`, a reason why. Otherwise this string should be "".
   expression, output, code, | | see section on State Information.

