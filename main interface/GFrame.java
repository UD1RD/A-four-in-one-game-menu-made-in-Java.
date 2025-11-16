import javax.swing.JFrame;

public class GFrame extends JFrame{

	private static final long serialVersionUID = 1L;

	GFrame() {
		GP panel = new GP();
		this.add(panel);
		this.setTitle("Snake");
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}
