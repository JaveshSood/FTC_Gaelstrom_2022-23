package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="BlueInside2", group="Autonomous")
public class BlueInside2 extends LinearOpMode{
    MSJHardware robot = new MSJHardware(); // Use our hardware

    private ElapsedTime     runtime = new ElapsedTime();

    static final double COUNTS_PER_MOTOR_REV = 537.6;    // eg: goBILDA Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 2.95276;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 0.5;
    static final double TURN_SPEED = 0.25;
    //public double duckLevel = 3;
    // public double sHubDistance = 3.0;
    //  @Override
    NormalizedColorSensor colorSensor;
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        telemetry.addData("Red:", robot.colorSensor.red());
        telemetry.addData("Blue:", robot.colorSensor.blue());
        telemetry.addData("Green:",robot.colorSensor.green());

        robot.frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.extenderMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // robot.liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.extenderMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        robot.frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        robot.backRightMotor.setDirection(DcMotor.Direction.FORWARD);
        robot.backLeftMotor.setDirection(DcMotor.Direction.REVERSE);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0", "Starting at %7d :%7d",
                robot.frontLeftMotor.getCurrentPosition(),
                robot.frontRightMotor.getCurrentPosition(),
                robot.backLeftMotor.getCurrentPosition(),
                robot.backRightMotor.getCurrentPosition(),
                robot.extenderMotor.getCurrentPosition()
        );
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)


        // Drive forwards to get off wall
        //cone auto
        robot.clawServo.setPosition(0.26);
        encoderDrive(DRIVE_SPEED, 4, 4, 2);
        encoderStrafe(DRIVE_SPEED*0.5, 45, -45, 15.0);   //strafe
        encoderStrafe(DRIVE_SPEED, -3.5, 3.5, 3);
        robot.extenderMotor.setPower(1);                                       //lift
        sleep(2100);
        robot.extenderMotor.setPower(.0);
        encoderDrive(DRIVE_SPEED, 6, 6, 2.6);    //line up with rod
        robot.clawServo.setPosition(0.65);                                      //drop
        sleep(1000);
        encoderDrive(DRIVE_SPEED, -5, -5, 2.6);    //go back
        encoderStrafe(DRIVE_SPEED, -6, 6, 5);
        encoderDrive(DRIVE_SPEED*0.5, 20, -20, 7);
        encoderStrafe(DRIVE_SPEED, 2, -2, 3 );
        encoderDrive(DRIVE_SPEED, 6,6,5);
        encoderStrafe(DRIVE_SPEED, 3, -3, 3 );


        double green =robot.colorSensor.red()/robot.colorSensor.blue();
        double yellow = robot.colorSensor.red()/robot.colorSensor.green();
        double purple = robot.colorSensor.green()/robot.colorSensor.blue();
        sleep(4000);
        if(robot.colorSensor.red()/robot.colorSensor.blue()>=0.8 && robot.colorSensor.red()/robot.colorSensor.blue()<=1.2) {
            encoderStrafe(DRIVE_SPEED, -4, 4, 5);
            encoderDrive(DRIVE_SPEED, -14, -14, 12);
            encoderDrive(DRIVE_SPEED, 19, -19, 7);
            encoderDrive(DRIVE_SPEED, -22, -22, 7);
        }

        else if(robot.colorSensor.red()/robot.colorSensor.green()>=0.9 && robot.colorSensor.red()/robot.colorSensor.green()<=1.1){
            encoderStrafe(DRIVE_SPEED, -4, 4, 5);
            encoderDrive(DRIVE_SPEED, -14, -14, 12);
            encoderDrive(DRIVE_SPEED, 19, -19, 7);
        }

        else if (robot.colorSensor.blue()/robot.colorSensor.green()>=1.0) {


            encoderStrafe(DRIVE_SPEED, -4, 4, 5);
            encoderDrive(DRIVE_SPEED, -12, -12, 12);
            encoderDrive(DRIVE_SPEED, 19, -19, 7);
            encoderDrive(DRIVE_SPEED, 22, 22, 7);
        }
        else {
            encoderStrafe(DRIVE_SPEED, -4, 4, 5);
            encoderDrive(DRIVE_SPEED, -12, -12, 12);
            encoderDrive(DRIVE_SPEED, 19, -19, 7);
            encoderDrive(DRIVE_SPEED, 22, 22, 7);
        }


        //encoderStrafe(DRIVE_SPEED, -65, 65, 5.0);   //strafe
        //encoderDrive(DRIVE_SPEED, -25, -25, 2.6);    //go back
        // robot.extenderMotor.setPower(-.6);                                       //un-lift
        //sleep(5400);
        //robot.extenderMotor.setPower(0);                                       //stop



    }

    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */

    public void encoderDrive(double speed,
                             double leftInches1, double rightInches1,
                             double timeoutS) {

        robot.frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        int newLeftTarget1;
        int newRightTarget1;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget1 = robot.frontLeftMotor.getCurrentPosition() + (int) (leftInches1 * COUNTS_PER_INCH);
            newRightTarget1 = robot.frontRightMotor.getCurrentPosition() + (int) (rightInches1 * COUNTS_PER_INCH);

            robot.frontLeftMotor.setTargetPosition(newLeftTarget1);
            robot.frontRightMotor.setTargetPosition(newRightTarget1);
            robot.backLeftMotor.setTargetPosition(newLeftTarget1);
            robot.backRightMotor.setTargetPosition(newRightTarget1);

            // Turn On RUN_TO_POSITION
            robot.frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.frontLeftMotor.setPower(Math.abs(speed));
            robot.frontRightMotor.setPower(Math.abs(speed));
            robot.backLeftMotor.setPower(Math.abs(speed));
            robot.backRightMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.frontLeftMotor.isBusy() && robot.frontRightMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget1, newRightTarget1);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.frontLeftMotor.getCurrentPosition(),
                        robot.frontRightMotor.getCurrentPosition(),
                        robot.backLeftMotor.getCurrentPosition(),
                        robot.backRightMotor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.frontLeftMotor.setPower(0);
            robot.frontRightMotor.setPower(0);
            robot.backLeftMotor.setPower(0);
            robot.backRightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move

            //testing computer at school
        }
    }

    public void encoderStrafe(double speed,
                              double leftInches, double rightInches,
                              double timeoutS) {


        robot.frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.frontLeftMotor.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.frontRightMotor.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);

            robot.frontLeftMotor.setTargetPosition(newLeftTarget);
            robot.frontRightMotor.setTargetPosition(newRightTarget);
            robot.backLeftMotor.setTargetPosition(newRightTarget);
            robot.backRightMotor.setTargetPosition(newLeftTarget);

            // Turn On RUN_TO_POSITION
            robot.frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.frontLeftMotor.setPower(Math.abs(speed));
            robot.frontRightMotor.setPower(Math.abs(speed));
            robot.backLeftMotor.setPower(Math.abs(speed));
            robot.backRightMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.frontLeftMotor.isBusy() && robot.frontRightMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.frontLeftMotor.getCurrentPosition(),
                        robot.frontRightMotor.getCurrentPosition(),
                        robot.backLeftMotor.getCurrentPosition(),
                        robot.backRightMotor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.frontLeftMotor.setPower(0);
            robot.frontRightMotor.setPower(0);
            robot.backLeftMotor.setPower(0);
            robot.backRightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move

            robot.frontLeftMotor.setTargetPosition(newLeftTarget);
            robot.frontRightMotor.setTargetPosition(newRightTarget);
            robot.backLeftMotor.setTargetPosition(newLeftTarget);
            robot.backRightMotor.setTargetPosition(newRightTarget);
            //testing computer at school
        }

    }

}