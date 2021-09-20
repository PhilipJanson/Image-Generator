import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.imageio.ImageIO;

public class ImageGen {

	private String imgName;

	public ImageGen(String name) {
		imgName = name;
	}

	public static void main(String[] args) throws IOException {		
		if (args.length < 1) {
			System.err.println("Error: No image name provided");
			return;
		}
		else if (args.length > 1) {
			System.err.println("Error: Too many arguments");
			return;
		}
		
		ImageGen imgGen = new ImageGen(args[0]);
		imgGen.genImage();
	}

	public void genImage() throws IOException {
		File file = new File(imgName);
		BufferedImage image = ImageIO.read(file);
		
		if (image.getHeight() != 8 && image.getWidth() != 8) {
			System.err.println("Error: Image is not 8x8");
			return;
		}
		
		Color[][] matrix = new Color[8][8];

		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				matrix[x][y] = new Color(image.getRGB(x, y));
			}
		}
		
		String output = "from sense_hat import SenseHat\n\n\nsense = SenseHat()\n\n\nimage_pixels = [\n\n";
		
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				Color color = matrix[x][y];
				output += "(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")";
				
				if (x == 7) {
					output += ",\n";
				}
				else {
					output += ", ";
				}
			}
		}
		
		
		output = output.substring(0, output.length() - 2);
		output += "\n\n]\n\nsense.set_pixels(image_pixels)";

		try (PrintStream ps = new PrintStream(new FileOutputStream(imgName.replace(".png", "").replace(".jpg", "") + ".py"))) {
		    ps.print(output);
		}
	}
}