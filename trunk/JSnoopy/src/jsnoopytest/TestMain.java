package jsnoopytest;

/**
 * Title:        JSnoopy
 * Description:  Regression testing based on event sequences.
 * Copyright:    Copyright (c) 2002
 * Company:      Memorial University of Newfoundland
 * @author Theodore S. Norvell
 * @version 1.0
 */

import javax.swing.* ;
import java.awt.*;

import jsnoopy.* ;
import java.awt.event.*;

public class TestMain extends JFrame {
    static boolean packFrame = true;
    Interface1 obj1 ;
    private JPanel jPanel1 = new JPanel();
    private JPanel jPanel2 = new JPanel();
    private JButton goButton = new JButton();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JRadioButton baselineButton = new JRadioButton();
    private JRadioButton variantButton = new JRadioButton();
    private JRadioButton shortVariant = new JRadioButton();
    private JRadioButton longButton = new JRadioButton();
    private GridLayout gridLayout1 = new GridLayout();
    private ButtonGroup buttonGroup1 = new ButtonGroup();
    private GridLayout gridLayout2 = new GridLayout();
    static final int BASELINE=0, VARIANT=1, SHORT=2, LONG=3, BADARGUMENT=4 ;
    static int mode = BASELINE ;
    private JRadioButton badArgumentButton = new JRadioButton();

    /**Construct the application*/
    private TestMain() {
        try {
            jbInit();
            finishInit() ;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * Main program.
    */

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        ToolTipManager.sharedInstance().setInitialDelay( 200 );
        ToolTipManager.sharedInstance().setReshowDelay( 200 );


        JFrame frame = new TestMain() ;

        //Validate frames that have preset sizes
        //Pack frames that have useful preferred size info, e.g. from their layout
        if (packFrame) {
            frame.pack();
        }
        else {
            frame.validate();
        }
        //Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
    }

    private void jbInit() throws Exception {
        goButton.setText("GO1");
        goButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                go(e);
            }
        });
        this.getContentPane().setLayout(gridBagLayout1);
        jPanel1.setBorder(BorderFactory.createEtchedBorder());
        jPanel1.setLayout(gridLayout1);
        jPanel2.setBorder(BorderFactory.createEtchedBorder());
        jPanel2.setLayout(gridLayout2);
        baselineButton.setSelected(true);
        baselineButton.setText("baseline");
        baselineButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                baselineButton_actionPerformed(e);
            }
        });
        variantButton.setText("variant");
        variantButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                variantButton_actionPerformed(e);
            }
        });
        shortVariant.setText("short");
        shortVariant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shortVariant_actionPerformed(e);
            }
        });
        longButton.setText("long");
        longButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                longButton_actionPerformed(e);
            }
        });
        badArgumentButton.setText("badArgument");
        badArgumentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                badArgumentButton_actionPerformed(e);
            }
        });
        this.getContentPane().add(jPanel1,      new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        jPanel1.add(baselineButton, null);
        jPanel1.add(variantButton, null);
        jPanel1.add(shortVariant, null);
        jPanel1.add(longButton, null);
        jPanel1.add(badArgumentButton, null);
        this.getContentPane().add(jPanel2,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        jPanel2.add(goButton, null);
        buttonGroup1.add(baselineButton);
        buttonGroup1.add(variantButton);
        buttonGroup1.add(shortVariant);
        buttonGroup1.add(longButton);
        buttonGroup1.add(badArgumentButton);
    }

    private void finishInit() {
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
        JSnoopy.setActive(true);
        Interface3 obj3 = (Interface3) JSnoopy.getInstrumentor()
                        .instrument("obj3",
                                    new Class3(),
                                    Interface3.class ) ;
        Interface2 obj2 = (Interface2) JSnoopy.getInstrumentor()
                        .instrument("obj2",
                                    new Class2( obj3 ),
                                    Interface2.class ) ;
        obj1 = (Interface1) JSnoopy.getInstrumentor()
                        .instrument("obj1",
                                    new Class1( obj2 ),
                                    Interface1.class ) ;
    }

    void go(ActionEvent e) {
        obj1.a(3, "abc" ) ;

        obj1.b(null, true, false, (byte)200, (char)65, (char)10000 );

        obj1.c(0, 1234.6789e+100,-12345.98765e-100,Double.MAX_VALUE,
               Double.MIN_VALUE, Double.NaN, Double.NEGATIVE_INFINITY,
               Double.POSITIVE_INFINITY);

        obj1.d(0, 1234.6789e+20f,-12345.98765e-20f,Float.MAX_VALUE,
               Float.MIN_VALUE, Float.NaN, Float.NEGATIVE_INFINITY,
               Float.POSITIVE_INFINITY);

        obj1.e(0, Integer.MAX_VALUE, Integer.MIN_VALUE, -123456,
                0L, Long.MAX_VALUE, Long.MIN_VALUE, -123456789L,
                (short)0, Short.MAX_VALUE, Short.MIN_VALUE, (short)-12345 ) ;

        obj1.f("", "\t\n\"\\\uABCD",
               "abcdefghijklmnopqrstuvwxyz0123456789,!@#$%^&*()" ) ;

        int[] ia0 = new int[0] ;
        int[] ia1 = new int[]{1,2,3} ;
        int[] ia2 = new int[100];
        obj1.g( ia0, ia1, ia2 );

        short [] arrayShort = new short[3] ;
        arrayShort[0] = (short)0 ;
        arrayShort[1] = (short)-1 ;
        arrayShort[2] = (short)+1 ;
        short[][] arrayArrayShort = new short[3][] ;
        arrayArrayShort[0] = new short[0] ;
        arrayArrayShort[1] = arrayShort ;
        arrayArrayShort[2] = arrayShort ;
        String[] arrayString = new String[2] ;
        arrayString[0] = "abcdefghijklmnop" ;
        arrayString[1] = "\\\"\n\t\f" ;
        obj1.h( arrayShort, arrayArrayShort, arrayString );

        obj1.i( new Integer(123) ) ;

        if( mode==BADARGUMENT ) {
            obj1.z( this ); }
    }

    void baselineButton_actionPerformed(ActionEvent e) {
        mode = BASELINE ;
    }

    void variantButton_actionPerformed(ActionEvent e) {
        mode = VARIANT ;
    }

    void shortVariant_actionPerformed(ActionEvent e) {
        mode = SHORT ;
    }

    void longButton_actionPerformed(ActionEvent e) {
        mode = LONG ;
    }

    void badArgumentButton_actionPerformed(ActionEvent e) {
        mode = BADARGUMENT ;
    }
}