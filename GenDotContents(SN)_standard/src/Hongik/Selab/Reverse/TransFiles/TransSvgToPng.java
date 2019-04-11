package Hongik.Selab.Reverse.TransFiles;

import java.io.*;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;

public class TransSvgToPng {
	public TransSvgToPng(String file_name) {
		// TODO Auto-generated constructor stub
		try {
			TranscoderInput input_svg_image = new TranscoderInput(new FileInputStream(file_name + ".svg"));
			// Step-2: Define OutputStream to JPG file and attach to
			// TranscoderOutput
			OutputStream png_ostream = new FileOutputStream(file_name + ".png");
			TranscoderOutput output_png_image = new TranscoderOutput(png_ostream);
			// Step-3: Create JPEGTranscoder and define hints
			PNGTranscoder my_converter = new PNGTranscoder();      
			my_converter.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(
					0.8));
			// Step-4: Write output
			my_converter.transcode(input_svg_image, output_png_image);
			// Step 5- close / flush Output Stream
			png_ostream.flush();
			png_ostream.close();
		} catch (TranscoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
