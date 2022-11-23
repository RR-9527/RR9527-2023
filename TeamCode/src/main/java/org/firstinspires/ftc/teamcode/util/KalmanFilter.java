package org.firstinspires.ftc.teamcode.util;

/**
 * Kalman filter object for use with distance sensors and (possibly?) motor encoders
 * Proven to actually work!!!
 *
 * @author T Lindauer
 */
public class KalmanFilter {


    /**
     * Vector A
     */
    private double A = 1;
    /**
     * Vector B
     */
    private double B = 0;
    /**
     * Vector C
     */
    private double C = 1;

    /**
     * Process noise
     */
    private double R;
    /**
     * Measurement noise
     */
    private double Q;

    /**
     * Covariance variable
     */
    private double cov = Double.NaN;

    /**
     * State variable
     */
    private double x = Double.NaN;

    /**
     * Kalman Filter for driving motors during pathing
     * Set to defaults
     */
    public KalmanFilter() {
        R = 0.01;
        Q = 0.01;

        C = 1;
        B = 0;
        A = 1;
    }

    /**
     * Instead of specifying a deviceCode, make a custom Kalman Filter.
     *
     * @param R is process noise
     * @param Q is measurement noise
     * @param A is state vector
     * @param B is control vector
     * @param C is measurement vector
     */
    public KalmanFilter(double R, double Q, double A, double B, double C) {
        this.R = R;
        this.Q = Q;
        this.A = A;
        this.B = B;
        this.C = C;

        cov = Double.NaN;
        x = Double.NaN; // estimated signal without noise
    }

    /**
     * Only specify noise
     *
     * @param R is process noise
     * @param Q is measurement noise
     */
    // R is process noise, Q is measurement noise. No specified state/control/measurement vectors, set to default 1,0,1
    public KalmanFilter(double R, double Q) {
        this.R = R;
        this.Q = Q;

        A = 1;
        B = 0;
        C = 1;
    }

    /**
     * Feed a new value into the Kalman filter and return what the predicted state is.
     *
     * @param measurement the measured value
     * @param inputVal           is the controlled input value
     * @return the predicted result.
     * Postcondition: the appropriate filtered value has been returned
     */
    // Filter a measurement: measured value is measurement, controlled input value is u.
    public final double filter(double measurement, double inputVal) {

        if (Double.isNaN(x)) {
            x = measurement / C;
            cov = Q / (C * C);
        } else {
            double predX = A * x + B * inputVal;
            double predCov = A * A * cov + R;

            // Kalman gain
            double K = predCov * C * (1 / ((C * predCov * C) + Q));

            // Correction
            x = predX + K * (measurement - C * predX);
            cov = predCov - (K * C * predCov);
        }
        return x;
    }

    /**
     * Feed a new value into the Kalman filter and return what the predicted state is.
     *
     * @param measurement the measured value
     * @return the predicted result.
     * Postcondition: the appropriate filtered value has been returned
     */
    // Filter a measurement taken
    public final double filter(double measurement) {
        double inputVal = 0;
        if (Double.isNaN(x)) {
            x = measurement / C;
            cov = Q / (C * C);
        } else {
            double predX = A * x + B * inputVal;
            double predCov = cov * A * A + R;

            // Kalman gain
            double K = predCov * C * (1 / ((C * C * predCov) + Q));

            // Correction
            x = predX + K * (measurement - (C * predX));
            cov = predCov - (K * C * predCov);
        }
        return x;
    }


    /**
     * Return the last measurement taken.
     *
     * @return the last measurement
     * Postcondition: returns the last measurement accurately
     */
    // Return the last measurement taken
    public final double lastMeasurement() {
        return x;
    }

    /**
     * Set the measurement noise
     *
     * @param noise the measurement noise.
     *              Postcondition: sets the measurement noise accurately
     */
    // Set measurement noise
    public final void setMeasurementNoise(double noise) {
        Q = noise;
    }

    /**
     * Set the process noise
     *
     * @param noise the process noise.
     *              Postcondition: sets the process noise accurately
     */
    public final void setProcessNoise(double noise) {
        R = noise;
    }
}