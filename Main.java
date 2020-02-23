import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import javax.swing.border.EtchedBorder;

public class Main {

	private JFrame frame;
	private JTextPane editorCode;
	private JTextPane editorConsole;
	private Lexer lexer;
	private Parser parser;
    boolean isExcLocked =true;
    private LinkedList<Lexer.Token> tokens;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
		lexer = new Lexer();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 50, 900, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnfile = new JMenu("File");
		menuBar.add(mnfile);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				System.exit(0);
			}
		});
		mnfile.add(mntmExit);

		JToolBar toolBar = new JToolBar();
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);

		JButton btnRun = new JButton("Execute");
		ImageIcon runIcon = new ImageIcon(
				Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/execute.png"))
						.getScaledInstance(24, 24, Image.SCALE_SMOOTH));
		btnRun.setIcon(runIcon);
		toolBar.add(btnRun);
		btnRun.setEnabled(isExcLocked);
		btnRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
				editorConsole.setText("");
				Memory.variables=new HashMap<>();
				Memory.functions=new HashMap<>();
                Runnable Parse = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            isExcLocked=false;
                            btnRun.setEnabled(isExcLocked);
                            Parser p = new Parser(tokens);
                            Program program =p.parseProg();
							Evaluator evaluator=new Evaluator(editorConsole, program.sroot);
////                            checkSemantics(program)
//                            evaluate(program);

                            isExcLocked=true;
                            btnRun.setEnabled(isExcLocked);
                        } catch (Exception e) {

                        }
                    }
                };
                if (tokens!=null){

					new Thread(Parse).start();
				}else {
                	editorConsole.setText("Error: Editor is empty!");
				}
            }
        });

		JSplitPane splitPane = new JSplitPane();
		splitPane.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		splitPane.setOneTouchExpandable(true);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);

		editorCode = new JTextPane();
		editorCode.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				highlight(e);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				highlight(e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {

			}
		});
		editorCode.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		editorCode.setMinimumSize(new Dimension(0, 500));
		splitPane.setLeftComponent(editorCode);

		editorConsole = new JTextPane();
		editorConsole.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		splitPane.setRightComponent(editorConsole);
	}

	protected void highlight(DocumentEvent e) {
		Runnable doHighlight = new Runnable() {
			@Override
			public void run() {
				try {
					Document doc = e.getDocument();
					String code = doc.getText(0, doc.getLength());

					tokens = lexer.lex(code);

					HashMap<String, Color> tokenColors = new HashMap<>();
//					tokenColors.put(Lexer.TokenType.FLOAT.name(), Color.BLUE);
					tokenColors.put(Lexer.TokenType.INT.name(), Color.BLUE);
					tokenColors.put(Lexer.TokenType.PLUS.name(), Color.CYAN);
					tokenColors.put(Lexer.TokenType.DIV.name(), Color.CYAN);
					tokenColors.put(Lexer.TokenType.MUL.name(), Color.CYAN);
					tokenColors.put(Lexer.TokenType.MINUS.name(), Color.CYAN);
					tokenColors.put(Lexer.TokenType.STRING.name(), Color.GREEN.darker());
					tokenColors.put(Lexer.TokenType.TRUE.name(), Color.orange.darker());
					tokenColors.put(Lexer.TokenType.FALSE.name(), Color.orange.darker());
					tokenColors.put(Lexer.TokenType.IF.name(), Color.orange.darker());
					tokenColors.put(Lexer.TokenType.PRINT.name(), Color.orange.darker());
					tokenColors.put(Lexer.TokenType.WHILE.name(), Color.orange.darker());
					tokenColors.put(Lexer.TokenType.ELSE.name(), Color.orange.darker());
					tokenColors.put(Lexer.TokenType.FOR.name(), Color.orange.darker());
					tokenColors.put(Lexer.TokenType.TYPEINT.name(), Color.orange.darker());
					tokenColors.put(Lexer.TokenType.TYPESTRING.name(), Color.orange.darker());
					tokenColors.put(Lexer.TokenType.TYPEBOOL.name(), Color.orange.darker());

					HashMap<String, Style> tokenStyles = new HashMap<>();
					for (String key : tokenColors.keySet()) {
						Color color = tokenColors.get(key);
						Style style = editorCode.addStyle(key, null);
						StyleConstants.setForeground(style, color);
						tokenStyles.put(key, style);
					}
					
					Style defaultStyle = editorCode.addStyle("default", null);
					StyleConstants.setForeground(defaultStyle, Color.BLACK);

					// now iterate through tokens and try to highlight them
					// see the example below

					for (Lexer.Token token : tokens) {
						Style tokenStyle = tokenStyles.get(token.getType().name());
						if (tokenStyle != null)
							editorCode
								.getStyledDocument()
								.setCharacterAttributes(token.getPosition(), token.getData().length(), tokenStyle, true);
						else
							editorCode
								.getStyledDocument()
								.setCharacterAttributes(token.getPosition(), token.getData().length(), defaultStyle, true);
					}
				} catch (BadLocationException e1) {
					// exception
				}
			}
		};
		SwingUtilities.invokeLater(doHighlight);
	}
}