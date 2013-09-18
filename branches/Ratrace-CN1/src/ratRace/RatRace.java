package src.ratRace;

import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.layouts.BoxLayout;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import telford.ratrace.cn1.ControllerCN1;
import telford.ratrace.cn1.ModelCN1;
import telford.ratrace.cn1.ViewCN1;


public class RatRace extends Component implements Observer {
	private ModelCN1 model ;
	private ViewCN1 view ;
	private ControllerCN1 controller ;
	private Button goButton = new Button("Go") ;
	private Button pauseButton = new Button("Pause") ;
	private Button restartButton = new Button("Restart") ;
		
	void rebuildMVC() {
		if( view != null ) { view.terminate(); remove( view ) ; }
		model = new ModelCN1(50, 50) ;
		model.addObserver( this ) ;
		view = new ViewCN1( model ) ;
		controller = new ControllerCN1(model, view) ;
		addComponent(view);
		setVisible( true ) ;
		update(null, null) ; }
	
	public RatRace() {
		setDefaultCloseOperation(EXIT_ON_CLOSE) ;
		
		goButton.addActionListener( new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0) {
				model.setPaused( false ) ;
			}} ) ;
		pauseButton.addActionListener( new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0) {
				model.setPaused( true ) ;
			}} ) ;
		restartButton.addActionListener( new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0) {
				rebuildMVC() ;
			}} ) ;
		
		ToolBar toolBar = new ToolBar() ;
		toolBar.add( restartButton ) ;
		toolBar.add(goButton ) ;
		toolBar.add( pauseButton ) ;
		add( toolBar, BorderLayout.NORTH ) ;

		
		setSize( 500, 500 ) ;

		rebuildMVC() ;
	}

	@Override
	public void update(Observable o, Object arg) {
		goButton.setEnabled( model.getPaused() ) ;
		pauseButton.setEnabled( !model.getPaused() ) ;
		if( model.getPaused() ) {
			goButton.requestFocusInWindow() ; }
		else {
			pauseButton.requestFocusInWindow() ; } }
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater( new Runnable() {
			@Override public void run() {
				new RatRace() ;
			}} ) ;
	}
}