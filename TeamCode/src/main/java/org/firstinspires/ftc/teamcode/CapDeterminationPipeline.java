package org.firstinspires.ftc.teamcode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class CapDeterminationPipeline extends OpenCvPipeline {
    enum ConePosition { //possibilities of cap position
        RIGHT,
        CENTER,
        LEFT
    }

    //color constants
    static final Scalar BLUE = new Scalar(0, 0, 255); // <-- Mark on camera
    static final Scalar GREEN = new Scalar(0, 255, 0); //Colors on cone RGB values
    static final Scalar PURPLE = new Scalar(128, 0, 128); //Colors on cone RGB values
    static final Scalar ORANGE = new Scalar(255, 165, 0); //Colors on cone RGB values

    //Last year: game was identfy the color on the cone and then move based on color on cone
    // Blue


    //sets points
    static final Point REGION1_TOPLEFT_ANCHOR_POINT = new Point(360, 205); // Box region for cone
    static final int REGION_WIDTH = 120;
    static final int REGION_HEIGHT = 90;

    //creates points for rectangles
    Point region_pointA = new Point(
            REGION1_TOPLEFT_ANCHOR_POINT.x,
            REGION1_TOPLEFT_ANCHOR_POINT.y);
    Point region_pointB = new Point(
            REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
            REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);



    Mat region_Cr;
    Mat YCrCb = new Mat();
    Mat Cr = new Mat();
    int avg;

    private volatile ConePosition position = ConePosition.CENTER;

    void inputToCr(Mat input) { //extracts chroma red channel for analysis
        Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(YCrCb, Cr, 2);
    }

    @Override
    public void init(Mat firstFrame) {
        inputToCr(firstFrame);

        //creates 3 boxes which will be the regions for detection (one for each possible cap position)
        region_Cr = Cr.submat(new Rect(region_pointA, region_pointB));

    }

    @Override
    public Mat processFrame(Mat input) {
        inputToCr(input);

        //average Cr values in each box
        avg = (int) Core.mean(region_Cr).val[0];


        //outlines box area on camera stream
        Imgproc.rectangle(
                input, // Buffer to draw on
                region_pointA, // First point which defines the rectangle
                region_pointB, // Second point which defines the rectangle
                BLUE, // The color the rectangle is drawn in
                1); // Thickness of the rectangle lines


        //whichever is min, fill in box with green and assign position to according value (LEFT, CENTER, or RIGHT)
        if (avg <= 118) { // ORANGE = Inner
            position = ConePosition.LEFT;
            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region_pointA, // First point which defines the rectangle
                    region_pointB, // Second point which defines the rectangle
                    ORANGE, // The color the rectangle is drawn in
                    -1); // Negative thickness means solid fill

        } else if (avg > 118 && avg < 136) { // GREEN = Center
            position = ConePosition.CENTER;
            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region_pointA, // First point which defines the rectangle
                    region_pointB, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    -1); // Negative thickness means solid fill

        } else if (avg >= 136) { // PURPLE = Outer
            position = ConePosition.RIGHT;
            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region_pointA, // First point which defines the rectangle
                    region_pointB, // Second point which defines the rectangle
                    PURPLE, // The color the rectangle is drawn in
                    -1); // Negative thickness means solid fill


            //Camera points at where cone is
            //Based off of that (not most efficient way, Tanya recommends another way) is pointed at
            // a square (only focus on CONE) all extra stuff is in the way so
            // focus through area (boxed area) where estimated cone will be
            // Square area will be outlined in blue (POINT OF THE BLUE THING MENTIONED EARLIER)
            // Average the number of CR / CB (how much red is in the picture?? etc)
            // How much color is in that box???


            //Machine learning 4 barcode codes
            //Compare qr code to tested value
            //FTC PROVIDED RESOURCES!!!!
            //CHECK OUT OTHER TEAMS!!! AND GET HELP FROM OTHER TEAMMSSSS!!!! (used from last year)
        }

        return input;
    }

    public double findAvg() {return avg;}

    //to be called in auto files
    public boolean isCapLeft() {
        return position == ConePosition.LEFT;
    }
    public boolean isCapCenter() {
        return position == ConePosition.CENTER;
    }
    public boolean isCapRight() {
        return position == ConePosition.RIGHT;
    }

}
