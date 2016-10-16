package sample;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import javax.media.jai.JAI;
import javax.media.jai.NullOpImage;
import javax.media.jai.OpImage;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;

import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.SeekableStream;
import com.sun.media.jai.codec.TIFFEncodeParam;

public class TiffConcatSample {

	public static void main(String[] args) throws IOException {
		new TiffConcatSample().concatTiff();
//		new TiffConcatSample().doitJAI();
	}

	public void doitJAI() throws IOException {
		FileSeekableStream ss = new FileSeekableStream("C:\\business\\01_sb\\01_tiff\\kbnet_sample.tif");
		ImageDecoder dec = ImageCodec.createImageDecoder("tiff", ss, null);
		int count = dec.getNumPages();
		TIFFEncodeParam param = new TIFFEncodeParam();
		param.setCompression(TIFFEncodeParam.COMPRESSION_GROUP4);
		param.setLittleEndian(false); // Intel
		System.out.println("This TIF has " + count + " image(s)");
		for (int i = 0; i < count; i++) {
			RenderedImage page = dec.decodeAsRenderedImage(i);
			File f = new File("C:\\business\\01_sb\\01_tiff\\single_" + i + ".tif");
			System.out.println("Saving " + f.getCanonicalPath());
			ParameterBlock pb = new ParameterBlock();
			pb.addSource(page);
			pb.add(f.toString());
			pb.add("tiff");
			pb.add(param);
			RenderedOp r = JAI.create("filestore", pb);
			r.dispose();
		}
	}

	public void concatTiff() throws IOException {

		File[] files = new File("C:\\business\\01_sb\\01_tiff").listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith("single_");
			}
		});

		BufferedImage image[] = new BufferedImage[files.length];
		for (int i = 0; i < files.length; i++) {
		    SeekableStream ss = new FileSeekableStream(files[i]);
		    ImageDecoder decoder = ImageCodec.createImageDecoder("tiff", ss, null);
		    PlanarImage op = new NullOpImage(decoder.decodeAsRenderedImage(0), null, null, OpImage.OP_IO_BOUND);
		    image[i] = op.getAsBufferedImage();
		}

		TIFFEncodeParam params = new TIFFEncodeParam();
		OutputStream out = new FileOutputStream("C:\\business\\01_sb\\01_tiff\\sample_concat.tiff");
		ImageEncoder encoder = ImageCodec.createImageEncoder("tiff", out, params);

		params.setExtraImages(Arrays.asList(image).listIterator(1)); // this may need a check to avoid IndexOutOfBoundsException when vector is empty
		encoder.encode(image[0]);
		out.close();

	}
}