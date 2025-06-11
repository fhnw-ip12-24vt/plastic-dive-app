package ch.IP12.fish.components;

import ch.IP12.fish.components.helpers.*;


/**
 * An analog joystick needs to use an analog-digital convertor (ADC) to be attached to RaspPi.
 * <p>
 * In this implementation we use an 'Ads115' and attach two 'Potentiometer', using 2 of the ADC channels,
 * and one 'SimpleButton', connected to one of the digital pins, to it.
 * <p>
 * We use the terms 'normalized value' and 'raw value'.
 * <ul>
 *     <li>Normalized values are between -1 and 1. 0 means that joystick is in home position</li>
 *     <li>Raw value is the measured voltage</li>
 * </ul>
 *
 */
public class JoystickAnalog extends Component {

    private final Ads1115 ads1115;
    /**
     * potentiometer x axis
     */
    private final Potentiometer xAxis;
    /**
     * potentiometer y axis
     */
    private final Potentiometer yAxis;

    private final double normThreshold;
    final double homeArea = 0.1;

    private double strength = 0.0;
    private double direction = 0.0;

    private double xActualValue;
    private double yActualValue;

    private double xLastNotifiedValue = 999;
    private double yLastNotifiedValue = 999;


    /**
     * Builds a new JoystickAnalog component with custom input for x-, y-axis, custom pin for push button.
     * ads component needs to be created outside this clas, other channels may be used for other components.
     *
     * @param ads1115      ads object
     * @param channelXAxis analog potentiometer x-axis
     * @param channelYAxis analog potentiometer y-axis
     */
    public JoystickAnalog(Ads1115 ads1115, Ads1115.Channel channelXAxis, Ads1115.Channel channelYAxis) {
        this(ads1115,
                new Potentiometer(ads1115, channelXAxis, Potentiometer.Range.MINUS_ONE_TO_ONE),
                new Potentiometer(ads1115, channelYAxis, Potentiometer.Range.MINUS_ONE_TO_ONE),
                0.05
        );
    }

    /**
     * @param potentiometerX potentiometer object for x-axis
     * @param potentiometerY potentiometer object for y-axis
     */
    JoystickAnalog(Ads1115 ads1115, Potentiometer potentiometerX, Potentiometer potentiometerY, double normThreshold) {
        this.ads1115       = ads1115;
        this.xAxis         = potentiometerX;
        this.yAxis         = potentiometerY;
        this.normThreshold = normThreshold;
    }

    /**
     * @param onMove Additional logic to be done when joystick moves outside the dead zone
     * @param onCenter Additional logic to be done when joystick moves into the dead zone
     */
    public void onMove(PositionConsumer onMove, Runnable onCenter){
        xAxis.onNormalizedValueChange((xPos) -> {
            xActualValue = -xPos;
            updateVals(onMove, onCenter);
        });

        yAxis.onNormalizedValueChange((yPos) -> {
            yActualValue = -yPos;
            updateVals(onMove, onCenter);
        });
    }

    public void startReadingAllJoysticks(){
        if (!ads1115.isContinuousReadingActive())
            ads1115.startContinuousReading(0.1);
    }

    private synchronized void updateVals(PositionConsumer onMove, Runnable onCenter){
        if (Math.abs(xActualValue) <= homeArea && Math.abs(yActualValue) <= homeArea) {
            xLastNotifiedValue = xActualValue;
            yLastNotifiedValue = yActualValue;

            strength = 0.0;
            direction = 0.0;

            if (onCenter != null) {
                onCenter.run();
            }
        }
        else {
            double distance = Math.sqrt(Math.pow(xActualValue - xLastNotifiedValue, 2) + Math.pow(yActualValue - yLastNotifiedValue, 2));

            if(distance > normThreshold){
                xLastNotifiedValue = xActualValue;
                yLastNotifiedValue = yActualValue;

                if(onMove != null){
                    onMove.accept(xLastNotifiedValue, yLastNotifiedValue);
                }

                // Compute the angle in radians
                direction = Math.atan2(-yActualValue, xActualValue);

                //calculate magnitude of joystick direction vector
                double magnitude = Math.sqrt(Math.pow(yActualValue, 2) + Math.pow(xActualValue, 2));

                strength = (double) Math.round(Math.min(1, magnitude)*100) / 100;
            }
        }
    }

    /**
     * @return Direction joystick is pointing in, in radians
     */
    public double getDirection() {
        return direction;
    }

    /**
     * @return How far from the center the joystick is (Scale: 0-1)
     */
    public double getStrength(){
        return strength;
    }

    /**
     * disables all the handlers on joystick events
     */
    @Override
    public void reset() {
        ads1115.stopContinuousReading();
        xAxis.reset();
        yAxis.reset();
    }

    /**
     * @return Ads1115 instance that this joystick is connected to
     */
    public Ads1115 getAds1115() {
        return ads1115;
    }

    /**
     * Interface to be used in lambda statements instead of a regular function
     */
    @FunctionalInterface
    public interface PositionConsumer{
        void accept(double xPos, double YPos);
    }

}