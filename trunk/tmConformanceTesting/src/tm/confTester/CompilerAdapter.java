/*
 * Created on Mar 25, 2005
 */
package tm.confTester;

import java.io.File ;
/**
 * @author theo
 * 
 * State transitions
 * <pre>
 *            init
 * any --------------> INIT
 * 
 *       compile
 * INIT ------------ <> ------------->READY_TO_RUN
 *                    |
 *                    |
 *                    V
 *                    COMPILE_FAILED
 * 
 *                run                                           reinit
 * READY_TO_RUN ----------------<>---------> RUN_SUCCEEDED  ------------> READY_TO_RUN 
 *                              |                                           ^
 *                              |                                           |
 *                              V                        reinit             |
 *                              RUN_FAILED ---------------------------------+
 * </pre>
 */
interface CompilerAdapter {
    static final int UNINIT = -1,
                     INIT = 0,
                     COMPILE_FAILED = 1,
                     READY_TO_RUN = 2,
                     RUN_FAILED = 3,
                     RUN_SUCCEEDED = 4 ;
    void init() ;
    void reinit() ;
    int getState() ;
    void compile( File directory, String fileName ) ;
    void run( String input ) ;
    String getCompilerErrorOutput() ;
    String getExecuteErrorString() ;
    String getExecuteOutputString() ;
    boolean isFileNameSuitable( String fileName ) ;
}
