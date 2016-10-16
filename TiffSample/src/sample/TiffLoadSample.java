package sample;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.media.jai.PlanarImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.TIFFEncodeParam;

public class TiffLoadSample extends JApplet implements ActionListener {

	public static void main(String[] args) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		String[] readerNames = ImageIO.getReaderFormatNames();
		System.out.println(Arrays.asList(readerNames));

		String path = "C:\\business\\01_sb\\01_tiff\\kbnet_sample.tif";

		JFrame frame = new JFrame("Tiff Sample");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		new TiffLoadSample().load3(frame.getContentPane(), path);

		frame.setSize(1024, 768);
		frame.setVisible(true);
	}

	JPanel cardPanel;
	CardLayout layout;

	public void init() {
		Container contentPane = getContentPane();
		try {
			this.load3(contentPane, "tiff/kbnet_sample.tif");
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			JPanel panel = new JPanel();
			JLabel label = new JLabel(e.getMessage());
			e.printStackTrace();
			panel.add(label);
			contentPane.add(panel);
		}
	}

	private void load3(Container contentPane, String path) throws Exception {

		FileSeekableStream ss = new FileSeekableStream(path);
		ImageDecoder dec = ImageCodec.createImageDecoder("tiff", ss, null);
		int count = dec.getNumPages();
		TIFFEncodeParam param = new TIFFEncodeParam();
		param.setCompression(TIFFEncodeParam.COMPRESSION_GROUP4);
		param.setLittleEndian(false); // Intel
		System.out.println("This TIF has " + count + " image(s)");

		this.cardPanel = new JPanel();
		JScrollPane sp = new JScrollPane(this.cardPanel);
		this.layout = new CardLayout();
		cardPanel.setLayout(layout);

		for (int i = 0; i < count; i++) {

			RenderedImage image = dec.decodeAsRenderedImage(i);
//			BufferedImage mine =
//				    new BufferedImage(image.getWidth(),
//				    		image.getHeight(),
//				                      BufferedImage.TYPE_INT_RGB);
//				mine.getGraphics().drawImage((Image)image,0,0,null);
			Icon imageIcon = new IconJAI(image);

			JPanel page = new JPanel();
			JLabel label = new JLabel(imageIcon);
			// sp.add(panel);
			page.add(label);
			cardPanel.add(page);
		}

		contentPane.add(sp, BorderLayout.CENTER);

		/* カード移動用ボタン */
		JButton firstButton = new JButton("First");
		firstButton.addActionListener(this);
		firstButton.setActionCommand("First");

		JButton prevButton = new JButton("Prev");
		prevButton.addActionListener(this);
		prevButton.setActionCommand("Prev");

		JButton nextButton = new JButton("Next");
		nextButton.addActionListener(this);
		nextButton.setActionCommand("Next");

		JButton lastButton = new JButton("Last");
		lastButton.addActionListener(this);
		lastButton.setActionCommand("Last");

		JPanel btnPanel = new JPanel();
		btnPanel.add(firstButton);
		btnPanel.add(prevButton);
		btnPanel.add(nextButton);
		btnPanel.add(lastButton);

		contentPane.add(btnPanel, BorderLayout.SOUTH);

	}

	private void load2(String path) throws Exception {

		PlanarImage image = JAIImageReader.readImage(path);
		Icon imageIcon = new IconJAI(image);

		JFrame frame = new JFrame("Tiff Sample");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		JLabel label = new JLabel(imageIcon);

		panel.add(label);
		// sp.add(panel);
		JScrollPane sp = new JScrollPane(label);

		frame.getContentPane().add(sp, BorderLayout.CENTER);
		frame.setSize(1024, 768);
		// frame.pack();
		frame.setVisible(true);

	}

	private void load(String path) throws Exception {

		File f = new File(path);
		BufferedImage image = ImageIO.read(f);
		this.initFrame(image);
	}

	private void initFrame(Image image) {

		JFrame frame = new JFrame("Tiff Sample");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImageIcon icon = new ImageIcon(image);
		JLabel label = new JLabel(icon);

		frame.getContentPane().add(label);
		frame.pack();
		frame.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		String cmd = e.getActionCommand();

		if (cmd.equals("First")) {
			layout.first(cardPanel);
		} else if (cmd.equals("Last")) {
			layout.last(cardPanel);
		} else if (cmd.equals("Next")) {
			layout.next(cardPanel);
		} else if (cmd.equals("Prev")) {
			layout.previous(cardPanel);
		}
	}

}
