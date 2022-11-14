package org.firstinspires.ftc.teamcode.pipelines;


import org.openftc.easyopencv.OpenCvPipeline;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Class to apply a canny edge detector in an EasyOpenCV pipeline.
 */
public class PoleDetector extends OpenCvPipeline {
    /**
     * Telemetry object to display data to the console
     */
    private Telemetry telemetry;
    private Mat copyInput;

    /**
     * Constructor to assign the telemetry object and actually have telemetry work.
     * This works because the OpenCvPipeline object has this built in interface to
     * use telemetry.
     * @param telemetry the input Telemetry type object.
     */
    public PoleDetector(Telemetry telemetry) {
        this.telemetry = telemetry;

    }


    /**
     * Apply a canny edge detector to an input matrix.
     * @param src is the source matrix - what is put into the function
     * @return a mat with only the edges of the main bodies given some contrast threshold.
     */
    public Mat canny(Mat src){
        // Define the size of the input image
        int frameWidth = src.cols();
        int frameHeight = src.rows();

        Mat gray = new Mat(frameHeight, frameWidth, src.type());
        Mat edges = new Mat(frameHeight, frameWidth, src.type());

        Imgproc.cvtColor(src, gray, Imgproc.COLOR_RGB2GRAY);
        Imgproc.blur(gray, gray, new Size(3, 3));
        Imgproc.Canny(gray, edges, 5, 100);

        Mat circles = new Mat();
        Imgproc.HoughCircles(edges, circles, Imgproc.HOUGH_GRADIENT, 1.0, frameWidth/13.0, 90.0, 30.0, 10, 50);

        for(int x=0; x<circles.cols(); x++){
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));

            Imgproc.circle(src, center, 1, new Scalar(0, 200, 0), 2, 2, 0);
            int radius = (int)Math.round(c[2]);
            Imgproc.circle(src, center, radius, new Scalar(0, 200, 0), 2, 2, 0);
        }


        gray.release();
        edges.release();

        telemetry.addLine("Pole Detector Running!");
        telemetry.update();

        return src;
    }

    /**
     * Process the frame by returning the canny edge of the input image
     * @param img the input image to this pipeline.
     * @return The canny edge version of the input image.
     */
    @Override
    public Mat processFrame(Mat img) {
        return canny(img);
    }
}